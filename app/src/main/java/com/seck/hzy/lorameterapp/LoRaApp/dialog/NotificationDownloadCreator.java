package com.seck.hzy.lorameterapp.LoRaApp.dialog;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.seck.hzy.lorameterapp.R;

import org.lzh.framework.updatepluginlib.callback.UpdateDownloadCB;
import org.lzh.framework.updatepluginlib.creator.DownloadCreator;
import org.lzh.framework.updatepluginlib.model.Update;

import java.io.File;
import java.util.UUID;

/**
 * <p>
 *     很多小伙伴提意见说需要一个下载时在通知栏进行进度条显示更新的功能。
 *     此类用于提供此种需求的解决方案。以及如何对其进行定制。满足任意场景使用
 *     默认使用参考：{@link org.lzh.framework.updatepluginlib.creator.DefaultNeedDownloadCreator}
 * </p>
 */
/*public class NotificationDownloadCreator implements DownloadCreator {
    @Override
    public UpdateDownloadCB create(Update update, Activity activity) {
        // 返回一个UpdateDownloadCB对象用于下载时使用来更新界面。
        return new NotificationCB(activity);
    }

    private static class NotificationCB implements UpdateDownloadCB {

        NotificationManager manager;
        NotificationCompat.Builder builder;
        int id;

        NotificationCB (Activity activity) {
            this.manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            builder = new NotificationCompat.Builder(activity);
            builder.setProgress(100, 0, false)
                    .setSmallIcon(activity.getApplicationInfo().icon)
                    .setAutoCancel(false)
                    .setContentText("下载中...")
                    .build();
            id = Math.abs(UUID.randomUUID().hashCode());
        }

        @Override
        public void onUpdateStart() {
            // 下载开始时的通知回调。运行于主线程
            manager.notify(id,builder.build());
        }

        @Override
        public void onUpdateComplete(File file) {
            // 下载完成的回调。运行于主线程
            manager.cancel(id);
        }

        @Override
        public void onUpdateProgress(long current, long total) {
            // 下载过程中的进度信息。在此获取进度信息。运行于主线程
            int progress = (int) (current * 1f / total * 100);
            builder.setProgress(100,progress,false);
            manager.notify(id,builder.build());
        }

        @Override
        public void onUpdateError(Throwable t) {
            // 下载时出错。运行于主线程
            manager.cancel(id);
        }
    }
}*/
public class NotificationDownloadCreator implements DownloadCreator {

    @Override
    public UpdateDownloadCB create(Update update, Activity activity) {
        return new NotificationCB(activity);
    }

    private static class NotificationCB implements UpdateDownloadCB {
        private Context mContext;
        private NotificationManager mManager;
        private Notification mNotification;
        int mId;

        public NotificationCB(Activity activity) {
            Log.d("limbo","更新进度");
            mContext = activity;
            mManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            mId = Math.abs(UUID.randomUUID().hashCode());
        }

        @Override
        public void onUpdateStart() {
            Log.d("limbo","开始下载");
            notifyUser("开始下载", 0);
        }

        @Override
        public void onUpdateComplete(File file) {
            Log.d("limbo","下载完成");
            notifyUser("下载完成", 0);
            mManager.cancel(mId);
        }

        @Override
        public void onUpdateProgress(long current, long total) {
            int progress = (int) (current * 1f / total * 100);
            notifyUser("下载中:" + progress + "%", progress);
            Log.d("limbo","下载中:" + progress + "%");
        }

        @Override
        public void onUpdateError(Throwable t) {
            mManager.cancel(mId);
        }


        private void notifyUser(String msg, int progress) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
            builder.setSmallIcon(R.drawable.pic_seckicon).
                    setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                            R.drawable.pic_seckicon))
                    .setContentTitle(mContext.getString(R.string.app_name))
                    .setOngoing(true)
                    .setContentText(msg);
            if (progress > 0 && progress < 100) {
                Log.d("limbo","progress:"+progress);
                builder.setProgress(100, progress, false);
            } else {
                Log.d("limbo","progress:"+progress);
                builder.setProgress(0, 0, false);
            }
            builder.setAutoCancel(true);
            builder.setWhen(System.currentTimeMillis());
            builder.setTicker(msg);
            mNotification = builder.build();
            mManager.notify(mId, mNotification);
        }

    }
}
