package com.seck.hzy.lorameterapp.LoRaApp.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import org.lzh.framework.updatepluginlib.creator.DialogCreator;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.util.SafeDialogOper;

/**
 * 简单实现：使用通知对用户提示：检查到有更新
 */
public class CustomNeedUpdateCreator extends DialogCreator {

    @Override
    public Dialog create(final Update update, Activity context) {
        return new AlertDialog.Builder(context)
                .setTitle("升级提示")
                .setMessage("发现新版本，请及时更新")
                .setPositiveButton("立即升级", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendDownloadRequest(update);
                        SafeDialogOper.safeDismissDialog((Dialog) dialog);
                    }
                })
                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendUserCancel();
                        SafeDialogOper.safeDismissDialog((Dialog) dialog);
                    }
                })
                .setCancelable(false)
                .create();
    }
}
