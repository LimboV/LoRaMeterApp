package com.seck.hzy.lorameterapp.LoRaApp.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_NetExp_Builder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils.GetCRC16;
import static com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils.countSum;

/**
 * Created by ssHss on 2016/7/18.
 */
public class BluetoothConnectThread extends Thread {

    static public final int NETWORK_MESSAGE_READ = 0x00;
    static public final int NETWORK_CONNECTED = 0x01;
    static public final int NETWORK_FAILED = 0x02;
    static public final int METER = 0x03;//表类型更新提示


    public String deviceName = "";
    public static BluetoothSocket mmSocket;
    public static InputStream mmInStream;
    public static OutputStream mmOutStream;

    private long timeRef = 0;//用于超时设定


    private byte[] mmRevbuffer = new byte[1024000];
    private Queue_Index mmRQ = new Queue_Index();
    /**
     * 创建蓝牙连接
     */
    public BluetoothConnectThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.v("limbo", e.toString());
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    @Override
    public void run() {
        mmRQ.l = 0; // queue init
        mmRQ.r = 0;
        int retnum = 0;
        byte[] buffer = new byte[128]; // buffer store for the stream
        while(true){
            try {
                retnum = mmInStream.read(buffer);
                for (int i = 0; i < retnum; i++) {
                    queueInsert(buffer[i]);
                }


                String msg = b2sn(buffer, retnum);
                Log.d("limbo", msg);

                MenuActivity.Cjj_CB_MSG = MenuActivity.Cjj_CB_MSG + msg;
                /*if (MenuActivity.Cjj_CB_MSG.length() > 5000){
                    MenuActivity.Cjj_CB_MSG ="";
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static String b2sn(byte[] src, int n) {
        if (src == null || src.length <= 0) {
            return null;
        }
        String saddr = "";

        for (int i = 0; i < n; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hv = "0" + hv;
            }
            saddr = saddr + " 0x" + hv;
        }
        return saddr;
    }

    public void cancel() throws IOException {
        mmSocket.close();
        this.interrupt();
    }
    public void write(byte[] bytes) throws IOException {
        mmOutStream.write(bytes);
    }

    /**
     * 具备默认补齐方式的转换Addr获取
     *
     * @param hexString
     * @return
     */
    public byte[] getSBAddr(String hexString) {
        int hexlen = 14; // 7位地址

        hexString = hexString.toUpperCase();
        while (hexString.length() < hexlen) {
            hexString = "0" + hexString;
        }

        byte[] addr_raw = HzyUtils.getHexBytes(hexString);
        return addr_raw;
    }

    /**
     * 读取图片
     *
     * @param addr
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws NetException
     */
    public Bitmap ImageTest(byte[] addr) throws InterruptedException,
            IOException, NetException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};
        write(cmd_pre);
        byte[] sndBuf = new byte[16];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x32;
        sndBuf[10] = 0x03;
        sndBuf[11] = (byte) 0x91;
        sndBuf[12] = 0x2f;
        sndBuf[13] = 0x00;
        sndBuf[14] = countSum(sndBuf, 0, 14);
        sndBuf[15] = 0x16;
        write(sndBuf);
        mmRevbuffer = new byte[1024000];
        mmRQ.r = 0;
        mmRQ.l = 0;
        Bitmap jpg = reciveImage();
        return jpg;
    }

    /**
     * 接受P型表图片
     */
    private Bitmap reciveImage() throws NetException {

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            Log.d("limbo",e.toString());
        }
        prepareTimeStart();
        while (true) {
            byte feFlag = waitTopByte(MenuActivity.timeDelayMax);
            if (feFlag != (byte) 0xfe) {
                continue;
            }
            Log.d("limbo", "1");
            byte csFlag = waitAByte(0, MenuActivity.timeDelayMax);
            if (csFlag != (byte) 0x68) {
                continue;
            }
            Log.d("limbo", "2");
            csFlag = waitAByte(1, MenuActivity.timeDelayMax);
            if (csFlag != (byte) 0x91) {
                continue;
            }
            Log.d("limbo", "3");
            int dataL = (waitAByte(10, MenuActivity.timeDelayMax) & 0xff) * 256;
            int dataL2 = (waitAByte(13, MenuActivity.timeDelayMax) & 0xff);

            dataL = dataL + dataL2;
            Log.d("limbo", "数据区长度:" + dataL);
            byte[] frame_data = new byte[12 + dataL];
            for (int i = 0; i < 11 + dataL; i++) {
                frame_data[i] = waitAByte(i, MenuActivity.timeDelayMax);
            }
            byte cs_va = countSum(frame_data, 0, frame_data.length - 3);
            byte cs = frame_data[frame_data.length - 3];
            byte endFlag = frame_data[frame_data.length - 2];
            if (endFlag != (byte) 0x16) {
                //                Log.i("limbo", "rcmdProtocalGeneral endFlag " + b2s(frame_data));
                continue;
            }

            if (cs != cs_va) {
                //                Log.i("limbo", "rcmdProtocalGeneral cs " + b2s(frame_data));
                continue;
            }
            String hexStringToBytes = "FFD8FFDB010600100B0C0E0C0A100E0D0E1211101318281A181616183123251D283A333D3C3933383740485C4E404457453738506D51575F626768673E4D71797064785C656763011112121815182F1A1A2F634238426363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363023021242A241E302A272A3633303948784E4842424893696F5778AE99B7B4AB99A8A5C0D8FFEAC0CCFFCFA5A8F0FFF3FFFFFFFFFFFFBAE7FFFFFFFFFFFFFFFFFF03333636483F488D4E4E8DFFC6A8C6FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC401A20000010501010101010100000000000000000102030405060708090A0B0100030101010101010101010000000000000102030405060708090A0B100002010303020403050504040000017D01020300041105122131410613516107227114328191A1082342B1C11552D1F02433627282090A161718191A25262728292A3435363738393A434445464748494A535455565758595A636465666768696A737475767778797A838485868788898A92939495969798999AA2A3A4A5A6A7A8A9AAB2B3B4B5B6B7B8B9BAC2C3C4C5C6C7C8C9CAD2D3D4D5D6D7D8D9DAE1E2E3E4E5E6E7E8E9EAF1F2F3F4F5F6F7F8F9FA1100020102040403040705040400010277000102031104052131061241510761711322328108144291A1B1C109233352F0156272D10A162434E125F11718191A262728292A35363738393A434445464748494A535455565758595A636465666768696A737475767778797A82838485868788898A92939495969798999AA2A3A4A5A6A7A8A9AAB2B3B4B5B6B7B8B9BAC2C3C4C5C6C7C8C9CAD2D3D4D5D6D7D8D9DAE2E3E4E5E6E7E8E9EAF2F3F4F5F6F7F8F9FA";
            byte[] headArray = HzyUtils.getHexBytes(hexStringToBytes);

            byte[] jpegSource = new byte[headArray.length + dataL - 3];
            System.arraycopy(headArray, 0, jpegSource, 0, headArray.length);

            System.arraycopy(frame_data, 14, jpegSource, headArray.length, dataL - 3);

            for (int i = 0; i < 11 + dataL; i++)
                queuePop();
            return BitmapFactory.decodeByteArray(jpegSource, 0, jpegSource.length);
        }
    }

    public void queueInsert(byte b) {
        synchronized (mmRQ) {
            mmRevbuffer[mmRQ.r++] = b;

            // Cicle array.
            if (mmRQ.r >= mmRevbuffer.length)
                mmRQ.r = 0;
        }
    }
    /**
     * 写Flash
     *
     * @param addr
     * @throws IOException
     */
    public void SCMD_WFlash(byte[] addr, byte[] data, int offset, int length) throws IOException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};
        write(cmd_pre);

        byte[] sndBuf = new byte[length + 16];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x25;
        sndBuf[10] = (byte) (length + 3);
        sndBuf[11] = (byte) 0xa0;
        sndBuf[12] = (byte) 0xfb;
        sndBuf[13] = 0x00;
        System.arraycopy(data, offset, sndBuf, 14, length);
        sndBuf[length + 14] = countSum(sndBuf, 0, length + 14);
        sndBuf[length + 15] = 0x16;

        write(sndBuf);
    }

    /**
     * 获得第一个字符
     */
    public byte waitTopByte(int maxTimeThresh) throws NetException {
        while (!this.timeRefPassed(maxTimeThresh)) {
            if (!queueSizeTest(0)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                return queuePop();
            }
        }

        throw P_NetExp_Builder.getExp(5);
    }
    /**
     * 获得中间的某个值
     *
     * @param prepareToRead
     * @param maxTimeThresh
     * @return
     */
    public byte waitAByte(int prepareToRead, int maxTimeThresh) throws NetException {
        while (!this.timeRefPassed(maxTimeThresh)) {
            if (!queueSizeTest(prepareToRead)) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                return this.queueGet(prepareToRead);
            }
        }
        Log.d("limbo", "测试缓冲区大小  r:" + mmRQ.r + "\nl:" + mmRQ.l + "  需要读取的大小：" + prepareToRead);
        throw P_NetExp_Builder.getExp(5);
    }
    /**
     * 测试缓冲区的实际大小
     *
     * @param prepareToRead
     * @return
     */
    public boolean queueSizeTest(int prepareToRead) {
        synchronized (mmRQ) {
            if (mmRQ.r >= mmRQ.l) {
                //				Log.d("limbo","测试缓冲区大小  r:"+mmRQ.r+"  l:"+mmRQ.l+"  需要读取的大小："+prepareToRead);
                return (mmRQ.r - mmRQ.l) > prepareToRead;
            } else {
                return (mmRQ.r + mmRevbuffer.length - mmRQ.l) > prepareToRead;
            }
        }
    }
    /**
     *
     * 开始协议设定时间
     *
     */
    private void prepareTimeStart() {
        timeRef = System.currentTimeMillis();
    }

    /**
     *
     * 判断是否超时
     *
     */
    private boolean timeRefPassed(long timeThresh) {
        if (System.currentTimeMillis() - timeRef > timeThresh)
            return true;
        else
            return false;
    }

    public class Queue_Index {
        int l, r;
    }

    public byte queuePop() {
        synchronized (mmRQ) {
            byte n = mmRevbuffer[mmRQ.l];
            mmRQ.l++;

            // Cicle array.
            if (mmRQ.l >= mmRevbuffer.length)
                mmRQ.l = 0;
            return n;
        }
    }
    public byte queueGet(int i) {
        synchronized (mmRQ) {
            if (mmRQ.l + i >= mmRevbuffer.length)
                return mmRevbuffer[mmRQ.l + i - mmRevbuffer.length];
            else
                return mmRevbuffer[mmRQ.l + i];
        }
    }

    public static class SXInfo {
        public String addr;
        public float cflow;
        public String recog_data[] = new String[8];
        public byte recog_belief[] = new byte[8];
    }
    public SXInfo readSXMeter(byte[] addr) throws NetException, IOException {
        scmdRCFlow_SX(addr);
        return rcmdRCFlow_SX();
    }
    public class PrFrame {
        byte type;
        byte[] addr;
        byte controlByte;
        int dataLength;
        byte dataDI0, dataDI1;
        byte ser;
        byte[] data;
        byte cs;
    }
    /**
     * 读取摄像表数据
     *
     * @param addr
     * @throws IOException
     */
    private void scmdRCFlow_SX(byte addr[]) throws IOException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};

        write(cmd_pre);

        byte[] sndBuf = new byte[16];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x21;
        sndBuf[10] = 0x03;
        sndBuf[11] = (byte) 0x90;
        sndBuf[12] = 0x1f;
        sndBuf[13] = 0x00;
        sndBuf[14] = countSum(sndBuf, 0, 14);
        sndBuf[15] = 0x16;

        write(sndBuf);
    }
    /**
     * 获得特殊的某个协议
     *
     * @param DI0
     * @param DI1
     * @return
     * @throws com.seck.hzy.lorameterapp.LoRaApp.utils.NetException
     */
    @SuppressLint("LongLogTag")
    private PrFrame rcmdProtocalGeneral(byte controlByte, byte DI0, byte DI1)
            throws NetException {
        int timeDelayMax = 15000; // ms
        /*try {
            Thread.sleep(2000);
        } catch (Exception e) {
            Log.d("limbo", e.toString());
        }*/
        prepareTimeStart();

        while (true) {
            byte feFlag = waitTopByte(timeDelayMax);
            if (feFlag != (byte) 0xfe) {
                continue;
            }

            byte csFlag = waitAByte(0, timeDelayMax);
            if (csFlag != (byte) 0x68) {
                Log.d("limbo", "不是68开头");
                continue;
            }

            int dataL = waitAByte(10, timeDelayMax) & 0xff;
            Log.d("limbo", dataL + "");

            byte[] frame_data = new byte[10 + dataL + 1];
            for (int i = 0; i < 10 + dataL + 1; i++) {
                frame_data[i] = waitAByte(i, timeDelayMax);
            }

            byte cs_va = countSum(frame_data, 0, frame_data.length);
            //            byte cs_va = countSum(frame_data, 0, frame_data.length - 3);
            byte cs = waitAByte(10 + dataL + 1, timeDelayMax);
            //            byte cs = frame_data[frame_data.length - 3];
            byte endFlag = waitAByte(10 + dataL + 2, timeDelayMax);
            //            byte endFlag = frame_data[frame_data.length - 2];
            if (endFlag != (byte) 0x16) {
                Log.d("limbo", "不是16结尾" + b2s(frame_data));
                continue;
            }

            if (cs != cs_va) {
                Log.i("limbo", "校验和错误" + b2s(frame_data));
                continue;
            }

            PrFrame frame = new PrFrame();
            frame.type = frame_data[1];
            frame.addr = new byte[7];
            System.arraycopy(frame_data, 2, frame.addr, 0, frame.addr.length);
            frame.controlByte = frame_data[9];

            byte receive_cb = (byte) (controlByte | 0x80);
            if (frame.controlByte != receive_cb) {
                Log.i("rcmdProtocalGeneral controlByte", b2s(frame_data));
                continue;
            }

            frame.dataLength = dataL;
            frame.dataDI0 = frame_data[11];
            frame.dataDI1 = frame_data[12];

            //if (frame.dataDI0 != DI0 || frame.dataDI1 != DI1) {
            //if ((frame.dataDI0 != DI0 && frame.dataDI0 != DI1) || (frame.dataDI1 != DI1 && frame.dataDI1 != DI0)) {
            if ((frame.dataDI0 + frame.dataDI1) != (DI0 + DI1)) {
                Log.i("rcmdProtocalGeneral dataDI0,1", b2s(frame_data));
                continue;
            }

            frame.ser = frame_data[13];
            frame.data = new byte[frame_data.length - 14];

            if (frame.data.length != 0)
                System.arraycopy(frame_data, 14, frame.data, 0,
                        frame.data.length);

            frame.cs = cs;

            for (int i = 0; i < 10 + dataL + 3; i++)
                queuePop();
            return frame;
        }
    }
    public static String b2s(byte[] src) {
        if (src == null || src.length <= 0) {
            return null;
        }

        String saddr = "";

        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hv = "0" + hv;
            }
            saddr = saddr + " 0x" + hv;
        }
        return saddr;
    }
    /**
     * 关闭DSP
     *
     * @param addr
     * @throws IOException
     */
    public void scmdCloseDsp(byte addr[]) throws IOException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};

        write(cmd_pre);

        byte[] sndBuf = new byte[16];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x30;
        sndBuf[10] = 0x03;
        sndBuf[11] = (byte) 0xA5;
        sndBuf[12] = (byte) 0x02;
        sndBuf[13] = 0x00;
        sndBuf[14] = countSum(sndBuf, 0, 14);
        sndBuf[15] = 0x16;

        write(sndBuf);
    }
    public void readBinaryPic_Parse1(byte[] addr) throws NetException,
            IOException {

        scmdReadBinary(addr);
        rcmdReadBinary();

    }
    /**
     * 拍摄二值图像
     *
     * @param addr
     * @throws IOException
     */
    private void scmdReadBinary(byte addr[]) throws IOException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};

        write(cmd_pre);

        byte[] sndBuf = new byte[16];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x31;
        sndBuf[10] = 0x03;
        sndBuf[11] = (byte) 0x90;
        sndBuf[12] = (byte) 0xf0;
        sndBuf[13] = 0x00;
        sndBuf[14] = countSum(sndBuf, 0, 14);
        sndBuf[15] = 0x16;

        write(sndBuf);
    }
    private boolean rcmdReadBinary() throws NetException {
        PrFrame frameRVer = rcmdProtocalGeneral((byte) 0x31, (byte) 0x90,
                (byte) 0xf0);
        if (frameRVer != null)
            return true;
        else
            return false;
    }
    public byte[] readBinaryPicBytes(byte[] addr, int picno) throws NetException, IOException {

        scmdGetBinary(addr, picno);
        return rcmdGetBinary();
    }
    byte[] rcmdGetBinary() throws NetException {
        PrFrame frameRVer = rcmdProtocalGeneral((byte) 0x31, (byte) 0x90,
                (byte) 0xf1);
        byte[] picdata = frameRVer.data;
        byte[] crc = GetCRC16(picdata, 113);

        if (frameRVer.data[113] != crc[0] || frameRVer.data[114] != crc[1]) {
            Log.v("CRC", crc[0] + " " + crc[1]);
            throw NetExp_Builder.getExp(4);
        }

        return picdata;
    }
    /**
     * 读取二值图像
     *
     * @param addr
     * @throws IOException
     */
    private void scmdGetBinary(byte addr[], int picno) throws IOException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};

        write(cmd_pre);

        byte[] sndBuf = new byte[17];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x31;
        sndBuf[10] = 0x04;
        sndBuf[11] = (byte) 0x90;
        sndBuf[12] = (byte) 0xf1;
        sndBuf[13] = 0x00;
        sndBuf[14] = (byte) picno;
        sndBuf[15] = countSum(sndBuf, 0, 15);
        sndBuf[16] = 0x16;

        write(sndBuf);
    }
    /**
     * 读地址
     *
     * @param addr
     * @return
     * @throws NetException
     * @throws IOException
     */
    public byte[] readAddr(byte[] addr) throws NetException, IOException {

        scmdRAddr_NB(addr);
        return rcmdRAddr_NB();
    }
    /**
     * 写地址
     *
     * @param addr
     * @return
     * @throws NetException
     * @throws IOException
     */
    public byte[] writeAddr(byte[] addr, byte[] newaddr) throws NetException,
            IOException {
        scmdWAddr_NB(addr, newaddr);
        return rcmdWAddr_NB();
    }
    /**
     * 读写地址
     *
     * @param newaddr
     * @return
     * @throws NetException
     */
    private byte[] rcmdWAddr_NB() throws NetException {

        PrFrame frameRVer = rcmdProtocalGeneral((byte) 0x15, (byte) 0xa0,
                (byte) 0x18);

        return frameRVer.addr;
    }
    /**
     * 读取系统配置参数
     *
     * @param addr
     * @param paramAddr
     * @param dataLen
     * @return
     * @throws NetException
     * @throws IOException
     */
    public int readParam(byte[] addr, int paramAddr, int dataLen)
            throws NetException, IOException {
        scmdReadPara(addr, paramAddr, dataLen);
        byte[] revData = rcmdReadParam();
        int nVal = 0;
        for (int i = 0; i < revData.length; i++)
            nVal += (revData[i] & 0xff) << (8 * i);
        return nVal;
    }
    /**
     * 写入参数
     *
     * @param addr
     * @param paramAddr
     * @param dataLen
     * @return
     * @throws NetException
     * @throws IOException
     */
    public boolean writeParam(byte[] addr, int paramAddr, byte[] param)
            throws NetException, IOException {
        byte[] crcThis = this.scmdWritePara(addr, paramAddr, param);
        byte[] crcRev = this.rcmdWriteParam();
        return true;
    }
    /**
     * 自动定位
     *
     * @param addr
     * @throws IOException
     */
    public void scmdAutoPos(byte addr[]) throws IOException, NetException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};
        write(cmd_pre);

        byte[] sndBuf = new byte[17];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x30;
        sndBuf[10] = 0x04;
        sndBuf[11] = (byte) 0xaa;
        sndBuf[12] = 0x04;
        sndBuf[13] = 0x00;
        sndBuf[14] = 0x00;
        sndBuf[15] = countSum(sndBuf, 0, 15);
        sndBuf[16] = 0x16;

        write(sndBuf);
    }
    /**
     * 读取一张Jpeg图像
     *
     * @param picno
     * @param trytimes
     * @throws NetException
     * @throws IOException
     */
    public Bitmap readPic(byte[] addr, int picno, int ty, Handler progress)
            throws NetException, IOException {
        byte[] jpegSource = readPicBytes(addr, picno, ty, progress);
        return BitmapFactory.decodeByteArray(jpegSource, 0, jpegSource.length);
    }
    /**
     * 写入参数，返回CRC校验
     *
     * @return
     * @throws NetException
     */
    private byte[] rcmdWriteParam() throws NetException {
        PrFrame frameRVer = rcmdProtocalGeneral((byte) 0x23, (byte) 0xa0,
                (byte) 0xf0);
        return frameRVer.data;
    }
    public Bitmap readBinaryPic_Parse2(byte[] addr, int picno)
            throws NetException, IOException {
        byte[] picdata = readBinaryPicBytes(addr, picno);
        if (picdata != null) {
            return binaryPicBytesToBitmap(picdata);
        }

        return null;
    }
    /**
     * 写入参数，返回写入参数的CRC16校验
     *
     * @param addr
     * @param parmAddr
     * @param params
     * @throws IOException
     */
    private byte[] scmdWritePara(byte addr[], int parmAddr, byte[] params)
            throws IOException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};

        write(cmd_pre);

        byte[] sndBuf = new byte[16 + params.length + 2];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x23;
        sndBuf[10] = (byte) (0x05 + params.length);
        sndBuf[11] = (byte) 0xa0;
        sndBuf[12] = (byte) 0xf0;
        sndBuf[13] = 0x00;
        sndBuf[14] = (byte) (parmAddr & 0xFF);
        sndBuf[15] = (byte) (params.length & 0xFF);
        System.arraycopy(params, 0, sndBuf, 16, params.length);
        sndBuf[16 + params.length] = countSum(sndBuf, 0, 16 + params.length);
        sndBuf[16 + params.length + 1] = 0x16;

        byte[] crc = GetCRC16(params, params.length);
        write(sndBuf);
        return crc;
    }
    /**
     * 读取参数
     *
     * @param paramLen
     * @return
     * @throws NetException
     */
    private byte[] rcmdReadParam() throws NetException {
        PrFrame frameRVer = rcmdProtocalGeneral((byte) 0x22, (byte) 0x81,
                (byte) 0xf0);
        return frameRVer.data;
    }
    /**
     * 发送读取摄像表参数命令
     *
     * @param addr
     * @param parmAddr
     * @throws IOException
     */
    private void scmdReadPara(byte addr[], int parmAddr, int paramLen)
            throws IOException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};

        write(cmd_pre);

        byte[] sndBuf = new byte[18];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x22;
        sndBuf[10] = 0x05;
        sndBuf[11] = (byte) 0x81;
        sndBuf[12] = (byte) 0xf0;
        sndBuf[13] = 0x00;
        sndBuf[14] = (byte) (parmAddr & 0xFF);
        sndBuf[15] = (byte) (paramLen & 0xFF);
        sndBuf[16] = countSum(sndBuf, 0, 16);
        sndBuf[17] = 0x16;

        write(sndBuf);
    }
    /**
     * 写地址NB表
     *
     * @param addr
     * @throws IOException
     */
    private void scmdWAddr_NB(byte addr[], byte newaddr[]) throws IOException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};
        write(cmd_pre);

        byte[] sndBuf = new byte[23];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x15;
        sndBuf[10] = 0x0a;
        sndBuf[11] = 0x18;
        sndBuf[12] = (byte) 0xa0;
        sndBuf[13] = 0x00;
        System.arraycopy(newaddr, 0, sndBuf, 14, newaddr.length);
        sndBuf[21] = countSum(sndBuf, 0, 21);
        sndBuf[22] = 0x16;

        write(sndBuf);
    }
    /**
     * 接受宁波表地址
     *
     * @throws NetException
     */
    byte[] rcmdRAddr_NB() throws NetException {

        PrFrame frameRVer = rcmdProtocalGeneral((byte) 0x03, (byte) 0x81,
                (byte) 0x0a);

        return frameRVer.addr;
    }
    /**
     * 读宁波表地址
     *
     * @throws IOException
     */
    private void scmdRAddr_NB(byte addr[]) throws IOException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};
        write(cmd_pre);

        byte[] sndBuf = new byte[16];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x03;
        sndBuf[10] = 0x03;
        sndBuf[11] = 0x0a;
        sndBuf[12] = (byte) 0x81;
        sndBuf[13] = 0x00;
        sndBuf[14] = countSum(sndBuf, 0, 14);
        sndBuf[15] = 0x16;

        write(sndBuf);
    }
    /**
     * 读版本号
     *
     * @param addr
     * @return
     * @throws NetException
     * @throws IOException
     */
    public String readVerNo(byte[] addr) throws NetException, IOException {
        scmdRVer(addr);
        return rcmdRVer();
    }
    /**
     * 读版本号
     *
     * @param addr
     * @throws IOException
     */
    private void scmdRVer(byte addr[]) throws IOException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};

        write(cmd_pre);

        byte[] sndBuf = new byte[16];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x20;
        sndBuf[10] = 0x03;
        sndBuf[11] = (byte) 0x81;
        sndBuf[12] = 0x00;
        sndBuf[13] = 0x00;
        sndBuf[14] = countSum(sndBuf, 0, 14);
        sndBuf[15] = 0x16;

        write(sndBuf);
    }
    /**
     * 读版本号
     *
     * @return
     * @throws NetException
     */
    private String rcmdRVer() throws NetException {

        PrFrame frameRVer = rcmdProtocalGeneral((byte) 0x20, (byte) 0x81,
                (byte) 0x00);

        String recvOutput = "";
        for (byte b : frameRVer.addr) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            recvOutput += hex;
            recvOutput += " ";
        }
        return recvOutput;
    }
    public void WhileSend(int dly, byte ch) throws IOException {
        int i;
        dly /= 100;

        byte[] sndBuf = new byte[]{ch};

        for (i = 0; i < dly; i++)//
        {
            write(sndBuf);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * 读取几种不同水表类型的读数
     *
     * @param addr
     * @param Maker
     * @return
     * @throws NetException
     * @throws IOException
     */
    public long readMeterCFlow(byte[] addr, int Maker) throws NetException,
            IOException {

        int ComDelay = MenuActivity.getInt("TYYS");

        if (Maker == 0) // 山科摄像表
        {
            return -1;
        } else if (Maker == 1 || Maker == 2) // 宁波mBus/485表
        {
            scmdRCFlow_NB(addr);
            return rcmdRCFlow_NB();
        }

        return -1;
    }
    long rcmdRCFlow_NB() throws NetException {
        PrFrame frameRVer = rcmdProtocalGeneral((byte) 0x01, (byte) 0x1f,
                (byte) 0x90);

        return bcdtol(frameRVer.data, 0, 4);
    }
    /**
     * 宁波表读取发送字符串
     *
     * @throws IOException
     */
    private void scmdRCFlow_NB(byte addr[]) throws IOException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};

        write(cmd_pre);

        byte[] sndBuf = new byte[16];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x01;
        sndBuf[10] = 0x03;
        sndBuf[11] = 0x1f;
        sndBuf[12] = (byte) 0x90;
        sndBuf[13] = 0x00;
        sndBuf[14] = countSum(sndBuf, 0, 14);
        sndBuf[15] = 0x16;

        write(sndBuf);
    }
    public static Bitmap binaryPicBytesToBitmap(byte[] picdata) {
        Bitmap a = Bitmap.createBitmap(25, 36, Bitmap.Config.ARGB_8888);
        int bindex = 0;
        for (int j = 0; j < 36; j++)
            for (int i = 0; i < 25; i++) {
                int byteindex = bindex / 8;
                int bitindex = bindex % 8;
                int color = ((picdata[byteindex] & 0xff) >> bitindex) & 0x01;
                if (color == 1)
                    a.setPixel(i, j, 0xffffffff);
                else
                    a.setPixel(i, j, Color.BLACK);
                bindex++;
            }

        return a;
    }
    SXInfo rcmdRCFlow_SX() throws NetException {
        SXInfo res = new SXInfo();

        PrFrame frameRVer = rcmdProtocalGeneral((byte) 0x21, (byte) 0x90,(byte) 0x1f);

        res.addr = HzyUtils.bytes2HexString(frameRVer.addr);

        res.cflow = bcdtol(frameRVer.data, 0, 4);
        // 小数部分
        res.cflow += ((float) (frameRVer.data[8] & 0x0f)) / ((float) 10);

        res.recog_data[0] = (frameRVer.data[4] & 0x0f) + "."
                + (frameRVer.data[8] & 0x0f); // 最右位
        res.recog_data[1] = ((frameRVer.data[4] & 0xff) >> 4) + "."
                + (((frameRVer.data[8] & 0xff) >> 4) & 0x0f);
        res.recog_data[2] = (frameRVer.data[5] & 0x0f) + "."
                + (frameRVer.data[9] & 0x0f);
        res.recog_data[3] = ((frameRVer.data[5] & 0xff) >> 4) + "."
                + (((frameRVer.data[9] & 0xff) >> 4) & 0x0f);
        res.recog_data[4] = (frameRVer.data[6] & 0x0f) + "."
                + (frameRVer.data[10] & 0x0f);
        res.recog_data[5] = ((frameRVer.data[6] & 0xff) >> 4) + "."
                + (((frameRVer.data[10] & 0xff) >> 4) & 0x0f);
        res.recog_data[6] = (frameRVer.data[7] & 0x0f) + "."
                + (frameRVer.data[11] & 0x0f);
        res.recog_data[7] = ((frameRVer.data[7] & 0xff) >> 4) + "."
                + (((frameRVer.data[11] & 0xff) >> 4) & 0x0f);
        // 置信度
        res.recog_belief[0] = frameRVer.data[12]; // 最右位
        res.recog_belief[1] = frameRVer.data[13];
        res.recog_belief[2] = frameRVer.data[14];
        res.recog_belief[3] = frameRVer.data[15];
        res.recog_belief[4] = frameRVer.data[16];
        res.recog_belief[5] = frameRVer.data[17];
        res.recog_belief[6] = frameRVer.data[18];
        res.recog_belief[7] = frameRVer.data[19];

        return res;
    }
    /**
     * 启动读取图像
     *
     * @param addr
     * @throws IOException
     */
    private void scmd_PicReadStart(byte addr[], int picno) throws IOException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};

        write(cmd_pre);

        byte[] sndBuf = new byte[24];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x31;
        sndBuf[10] = 3 + 8;
        sndBuf[11] = (byte) 0x90;
        sndBuf[12] = (byte) 0x1f;
        sndBuf[13] = 0x00;
        sndBuf[14] = (byte) picno;
        sndBuf[15] = 0; // 包号
        sndBuf[16] = (byte) 128; // 包长
        sndBuf[17] = 0; // 包长
        sndBuf[18] = 0;
        sndBuf[19] = 0;
        sndBuf[20] = 0;
        sndBuf[21] = 0;
        sndBuf[22] = countSum(sndBuf, 0, 22);
        sndBuf[23] = 0x16;

        write(sndBuf);
    }
    public class Pic_Head {
        public int len;
        public int packsize;
        public int packcount;
    }
    /**
     * 读取图像信息
     *
     * @return
     * @throws NetException
     */
    Pic_Head rcmd_PicHead() throws NetException {
        PrFrame frameRVer = rcmdProtocalGeneral((byte) 0x31, (byte) 0x90,
                (byte) 0x1f);

        int piclen = frameRVer.data[1] & 0xFF;
        piclen = piclen << 8;
        int piclen2 = frameRVer.data[0] & 0xFF;
        piclen += piclen2;

        if (piclen < 256 || piclen > 1536)
            throw NetExp_Builder.getExp(0x21);

        int packn = piclen / 128;
        if (piclen % 128 != 0)
            packn++;

        Pic_Head h = new Pic_Head();
        h.len = piclen;
        h.packsize = 128;
        h.packcount = packn;

        return h;
    }
    /**
     * 读取图像包
     *
     * @param addr
     * @throws IOException
     */
    private void scmd_PicReadPack(byte addr[], int picno, int packn)
            throws IOException {
        byte[] cmd_pre = new byte[]{(byte) 0xfe, (byte) 0xfe, (byte) 0xfe};

        write(cmd_pre);

        byte[] sndBuf = new byte[24];
        sndBuf[0] = 0x68;
        sndBuf[1] = 0x10;
        System.arraycopy(addr, 0, sndBuf, 2, addr.length);
        sndBuf[9] = 0x31;
        sndBuf[10] = 3 + 8;
        sndBuf[11] = (byte) 0x90;
        sndBuf[12] = (byte) 0x1f;
        sndBuf[13] = 0x00;
        sndBuf[14] = (byte) picno;
        sndBuf[15] = (byte) (packn + 1); // 包号
        sndBuf[16] = (byte) 128; // 包长
        sndBuf[17] = 0; // 包长
        sndBuf[18] = (byte) 128;
        sndBuf[19] = 0;
        sndBuf[20] = (byte) 128;
        sndBuf[21] = 0;
        sndBuf[22] = countSum(sndBuf, 0, 22);
        sndBuf[23] = 0x16;

        write(sndBuf);
    }
    /**
     * 读取图像包
     *
     * @return
     * @throws NetException
     */
    byte[] rcmd_PicPack() throws NetException {
        PrFrame frameRVer = rcmdProtocalGeneral((byte) 0x31, (byte) 0x90,
                (byte) 0x1f);
        byte[] picdata = frameRVer.data;
        byte[] crc = GetCRC16(picdata, 128);

        if (frameRVer.data[128] != crc[0] || frameRVer.data[129] != crc[1]) {
            Log.v("CRC", crc[0] + " " + crc[1]);
            Log.v("CRC REAL", frameRVer.data[128] + " " + frameRVer.data[129]);
            throw NetExp_Builder.getExp(4);
        }

        return picdata;
    }
    public byte[] readPicBytes(byte[] addr, int picno, int ty, Handler progress)
            throws NetException, IOException {
        int ComDelay = MenuActivity.getInt("TYYS");
        Pic_Head pichead = null;
        int delaytimes = 8000;

        scmd_PicReadStart(addr, picno);
        pichead = rcmd_PicHead();

        progress.obtainMessage(picno, 0, pichead.packcount).sendToTarget();

        if (pichead.len < 100 || pichead.len > 20000)
            return null;

        byte[] jpegSource = new byte[pichead.len];
        Log.v("Pic head length", pichead.len + "");

        // 获取每一包图像
        // delaytimes = 8000;
        for (int i = 0; i < pichead.packcount; i++) {
            scmd_PicReadPack(addr, picno, i);
            byte[] pck = rcmd_PicPack();

            int pcklen = Math.min(128, pichead.len - i * 128);
            System.arraycopy(pck, 0, jpegSource, i * 128, pcklen);
            progress.obtainMessage(picno, i + 1, pichead.packcount)
                    .sendToTarget();
        }

        return jpegSource;
    }
    /**
     * BCD码（低字节在前）转换成整数
     */
    long bcdtol(byte str[], int pos, int len) {
        int i;
        long tmp = 0;
        long data = 0;
        long[] ten = new long[]{1, 10, 100, 1000, 10000, 100000, 1000000,
                10000000};

        if (len > 4)
            len = 4;

        for (i = 0; i < len; i++) {
            tmp = (str[pos + i] >> 4) & 0x0f;
            data += tmp * ten[i * 2 + 1];
            tmp = str[pos + i] & 0x0f;
            data += tmp * ten[i * 2];
        }
        return data;
    }
}
