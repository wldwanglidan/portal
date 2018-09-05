package com.tempus.portal.base.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.tempus.portal.app.Constant;
import com.tempus.portal.model.event.InstallApkEvent;
import java.io.File;
import org.greenrobot.eventbus.EventBus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * <apk下载广播>
 *
 * @author wujh@cncn.com
 * @data: 2015/12/18 19:29
 * @version: V1.0
 */
public class AppDownLoadBroadcastReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        long apkPackageID = intent.getLongExtra(
                DownloadManager.EXTRA_DOWNLOAD_ID, 0l);

        long downloadId = RxSharedPreferences.create(
                getDefaultSharedPreferences(context))
                                             .getLong(
                                                     Constant.KEY_APK_PACKAGE_ID)
                                             .get();

        if (downloadId == apkPackageID) {
            RxSharedPreferences.create(getDefaultSharedPreferences(context))
                               .getBoolean(Constant.KEY_DOWNLOAD_STATE)
                               .set(false);
            installApk(context, apkPackageID);
        }
    }


    private static void installApk(Context context, long apkPackageID) {
        installApk(context, apkPackageID, true);
    }


    public static void installApk(Context context, long apkPackageID, boolean sendPost) {
        try {
            //有可能下载失败，故要捕获异常
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(apkPackageID);
            DownloadManager dm = (DownloadManager) context.getSystemService(
                    Context.DOWNLOAD_SERVICE);
            Cursor downloadResult = dm.query(query);

            if (downloadResult.moveToFirst()) {
                int statusColumnIndex = downloadResult.getColumnIndex(
                        DownloadManager.COLUMN_STATUS);
                int status = downloadResult.getInt(statusColumnIndex);

                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    //download completed successfully
                    int localFileNameId = downloadResult.getColumnIndex(
                            DownloadManager.COLUMN_LOCAL_FILENAME);

                    String downloadPathFile = downloadResult.getString(
                            localFileNameId);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(
                            Uri.fromFile(new File(downloadPathFile)),
                            "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
            if (sendPost) {
                EventBus.getDefault().post(new InstallApkEvent(apkPackageID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
