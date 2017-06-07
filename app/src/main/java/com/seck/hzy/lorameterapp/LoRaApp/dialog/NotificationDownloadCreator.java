package com.seck.hzy.lorameterapp.LoRaApp.dialog;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.seck.hzy.lorameterapp.R;

import org.lzh.framework.updatepluginlib.callback.UpdateDownloadCB;
import org.lzh.framework.updatepluginlib.creator.DownloadCreator;
import org.lzh.framework.updatepluginlib.model.Update;

import java.io.File;
import java.util.UUID;

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
            mContext = activity;
            mManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            mId = Math.abs(UUID.randomUUID().hashCode());
        }

        @Override
        public void onUpdateStart() {
            notifyUser("开始下载", 0);
        }

        @Override
        public void onUpdateComplete(File file) {
            notifyUser("下载完成", 0);
            mManager.cancel(mId);
        }

        @Override
        public void onUpdateProgress(long current, long total) {
            int progress = (int) (current * 1f / total * 100);
            notifyUser("下载中：" + progress + "%", progress);
        }

        @Override
        public void onUpdateError(Throwable t) {
            mManager.cancel(mId);
        }


        private void notifyUser(String msg, int progress) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
            builder.setSmallIcon(R.drawable.pic_icon).
                    setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                            R.drawable.pic_icon))
                    .setContentTitle(mContext.getString(R.string.app_name))
                    .setOngoing(true)
                    .setContentText(msg);
            if (progress > 0 && progress < 100) {
                builder.setProgress(100, progress, false);
            } else {
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
