package com.tempus.portal.base.utils;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.tempus.portal.R;
import com.tempus.portal.app.AppContext;
import com.tempus.portal.app.Constant;
import com.tempus.portal.app.DirContext;
import com.tempus.portal.base.update.UpgradeService;
import java.io.File;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by T086 on 2015/12/30.
 */
public class DownloadUtil {

    private static final String TAG = "DownloadUtil";


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void startDownload(Context mContext, String fileUrl) {
        if (RxSharedPreferences.create(getDefaultSharedPreferences(mContext))
                               .getBoolean(Constant.KEY_DOWNLOAD_STATE, false)
                               .get()) {
            ToastUtils.showShortToastSafe(R.string.is_downloading);
            return;
        }
        RxSharedPreferences.create(getDefaultSharedPreferences(mContext))
                           .getBoolean(Constant.KEY_DOWNLOAD_STATE)
                           .set(true);
        //check the Download Manager enable or not,some models such as Samsung(GT-N7102) disabled this feature
        int state = mContext.getPackageManager()
                            .getApplicationEnabledSetting(
                                    "com.android.providers.downloads");
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER ||
                state ==
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
            //disabled,download apk via custom download service
            downloadSelf(mContext, fileUrl);
        }
        else {
            //enable,use Android Download Manager to download apk
            boolean sdCardExist = Environment.getExternalStorageState()
                                             .equals(Environment.MEDIA_MOUNTED);
            boolean useDownloadManager = true;
            if (!sdCardExist) {
                //need Pre-check the relate file stats(android 4.0/4.1/4.2)
                File file = mContext.getExternalFilesDir(
                        Environment.DIRECTORY_DOWNLOADS);
                if (file == null) {
                    Log.d(TAG,
                            "Failed to get external storage files directory");
                    useDownloadManager = false;
                }
                else if (file.exists()) {
                    if (!file.isDirectory()) {
                        useDownloadManager = false;
                        Log.d(TAG, file.getAbsolutePath() +
                                " already exists and is not a directory");
                    }
                }
                else {
                    if (!file.mkdirs()) {
                        useDownloadManager = false;
                        Log.d(TAG, "Unable to create directory: " +
                                file.getAbsolutePath());
                    }
                }
                if (!useDownloadManager) {
                    //get external storage files directory failed or catch other exception,start local download service
                    downloadSelf(mContext, fileUrl);
                    return;
                }
            }
            DownloadManager downloadManager
                    = (DownloadManager) mContext.getSystemService(
                    Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(fileUrl));

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }

            String fileName = Md5.digest32(fileUrl) + ".apk";
            if (sdCardExist) {
                String dir = DirContext.DirEnum.ROOT_dir.getValue() +
                        File.separator + DirContext.DirEnum.DOWNLOAD;
                request.setDestinationInExternalPublicDir(dir, fileName);
            }
            else {
                request.setDestinationInExternalFilesDir(mContext,
                        Environment.DIRECTORY_DOWNLOADS, fileName);
            }

            request.setTitle(mContext.getString(R.string.app_name));
            request.setDescription(mContext.getString(R.string.update_waiting));
            request.setMimeType("application/vnd.android.package-archive");

            RxSharedPreferences.create(getDefaultSharedPreferences(mContext))
                               .getLong(Constant.KEY_APK_PACKAGE_ID)
                               .set( downloadManager.enqueue(request));
        }
    }


    private static void downloadSelf(Context mContext, String dlfileURL) {
        //if downloadService is running,waiting download success. else start service
        if (ServiceUtils.isServiceRunning(mContext, UpgradeService.class.getName())) {
            return;
        }
        Intent service = new Intent(mContext, UpgradeService.class);
        service.setAction(UpgradeService.ACTION_START);
        service.putExtra("url", dlfileURL);
        mContext.startService(service);
    }


    public static String getLocalFile(String url) {
        String saveFileName = Md5.digest32(url) + ".apk";

        String saveDir;
        if (Environment.getExternalStorageState()
                       .equals(Environment.MEDIA_MOUNTED)) {
            saveDir = DirContext.getInstance()
                                .getDir(DirContext.DirEnum.DOWNLOAD)
                                .getAbsolutePath();
        }
        else {
            final File file = AppContext.getContext()
                                        .getExternalFilesDir(
                                                Environment.DIRECTORY_DOWNLOADS);
            if (!file.exists()) {
                if (!file.exists() || !file.isDirectory()) {
                    file.mkdirs();
                }
                saveDir = file.getAbsolutePath();
            }
            else {
                return null;
            }
        }
        File file = new File(saveDir, saveFileName);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        else {
            return null;
        }
    }
}
