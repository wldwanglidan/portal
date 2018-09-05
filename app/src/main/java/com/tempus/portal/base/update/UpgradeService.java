package com.tempus.portal.base.update;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.tempus.portal.R;
import com.tempus.portal.app.Constant;
import java.io.File;
import java.io.IOException;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * download apk,upgrade
 *
 * @author ruiming.wu
 * @data: 2015年5月8日 上午10:09:55
 * @version: V1.0
 */
public class UpgradeService extends Service {
    private static final String TAG = "UpgradeService";

    public static final int DL_UPDATE_PROGRESS = 1;
    public static final int DL_ERROR = 2;
    public static final int DL_SUCCESS = 3;
    public static final String ACTION_START = "start";
    public static final String ACTION_REPLAY = "replay";
    public static final String ACTION_STOP = "stop";
    private Context mContext;
    private long mFileLen;
    private long dlen;
    private String _cachefilename = "TempusPortal.apk";
    private String _url;
    private boolean mStopDownload;
    private static final int NOTICE_NUM = 1000;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private String mRootPath = null;//download directory
    private boolean mHasSDCard = true;

    private Handler mHandler = new Handler();


    @Override public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressWarnings("deprecation") @Override
    public void onStart(Intent intent, int startId) {

        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            mHasSDCard = Environment.getExternalStorageState()
                                    .equals(Environment.MEDIA_MOUNTED);
            if (mHasSDCard) {
                mRootPath = String.format("%s%s",
                        Environment.getExternalStorageDirectory().getPath(),
                        "/airfares/download/");
            }
            else {
                mRootPath = String.format("%s%s",
                        mContext.getFilesDir().getAbsolutePath(), "/download/");
            }
            File file = new File(mRootPath);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    //if create directories failed,save apk-file to current directory or sdcard root directory
                    mRootPath = mHasSDCard
                                ? Environment.getExternalStorageDirectory()
                                             .getPath()
                                : mContext.getFilesDir().getAbsolutePath();
                }
            }
            file = null;

            String action = intent.getAction();
            Log.d(TAG, "action = " + action);
            if (action.equals(ACTION_START)) {
                mStopDownload = false;
                _url = intent.getStringExtra("url");
                downFile(_url);
            }
            else if (action.equals(ACTION_STOP)) {
                stopUpdate();
            }
            else if (action.equals(ACTION_REPLAY)) {
                downFile(_url);
            }
        }
        super.onStart(intent, startId);
    }


    @Override public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }


    @SuppressLint("HandlerLeak") private Handler mProgressHandler
            = new Handler() {
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DL_UPDATE_PROGRESS:
                    if (dlen == mFileLen) {
                        clearNotification(mContext);
                    }
                    else if (dlen == 0) {
                        notification(mContext);
                        mProgressHandler.sendEmptyMessageDelayed(
                                DL_UPDATE_PROGRESS, 1500);
                    }
                    else {
                        if (!mStopDownload) {
                            long s = dlen * 100 / mFileLen;
                            updateRate((int) s);
                            mProgressHandler.sendEmptyMessageDelayed(
                                    DL_UPDATE_PROGRESS, 1000);
                        }
                        else {
                            clearNotification(mContext);
                        }
                    }
                    break;
                case DL_ERROR:
                    RxSharedPreferences.create(
                            getDefaultSharedPreferences(mContext))
                                       .getBoolean(Constant.KEY_DOWNLOAD_STATE)
                                       .set(false);
                    updateErr();
                    break;
                case DL_SUCCESS:
                    RxSharedPreferences.create(
                            getDefaultSharedPreferences(mContext))
                                       .getBoolean(Constant.KEY_DOWNLOAD_STATE)
                                       .set(false);
                    launchUpgrade();
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * stop download and stop service
     */
    private void stopUpdate() {
        mStopDownload = true;
        mProgressHandler.removeMessages(DL_UPDATE_PROGRESS);
        mProgressHandler.removeMessages(DL_ERROR);
        mProgressHandler.removeMessages(DL_SUCCESS);
        clearNotification(mContext);
        stopSelf();
    }


    /**
     *
     * @param url
     */
    private void downFile(final String url) {
        mFileLen = -1;
        dlen = 0;
        mProgressHandler.sendEmptyMessage(DL_UPDATE_PROGRESS);
        new Thread() {
            public void run() {
                setName("Update_APK");
                //				HttpClient client = new DefaultHttpClient();
                //				HttpParams httpParams = client.getParams();
                //				HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                //				HttpConnectionParams.setSoTimeout(httpParams, 15000);
                //				HttpGet get = new HttpGet(url);
                //				HttpResponse response;
                //				try {
                //					response = client.execute(get);
                //					HttpEntity entity = response.getEntity();
                //                    mFileLen = entity.getContentLength();
                //					dlen = 0;
                //
                //					InputStream is = entity.getContent();
                //					FileOutputStream fileOutputStream = null;
                //					if (is != null) {
                //						File file = new File(mRootPath, _cachefilename);
                //						LogUtil.d(TAG, "path = " + file.getAbsoluteFile());
                //						fileOutputStream = new FileOutputStream(file);
                //
                //						byte[] buf = new byte[1024];
                //						int ch = -1;
                //						int count = 0;
                //						while ((ch = is.read(buf)) != -1) {
                //							fileOutputStream.write(buf, 0, ch);
                //							count += ch;
                //							dlen = count;
                //							if (mStopDownload) {
                //								break;
                //							}
                //						}
                //                        fileOutputStream.flush();
                //                        fileOutputStream.close();
                //                        if (!mStopDownload) {//success
                //                            mProgressHandler.sendEmptyMessage(DL_SUCCESS);
                //                        }
                //					}else{
                //                        mProgressHandler.sendEmptyMessage(DL_ERROR);
                //                    }
                //				}catch (Exception ex){
                //                    ex.printStackTrace();
                //                    mProgressHandler.sendEmptyMessage(DL_ERROR);
                //                }
            }
        }.start();
    }


    /**
     * 下载失败界面修改
     */
    private void updateUIErr() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            //3.0以上的版本 button才能正常使用
            mNotification.contentView.setViewVisibility(R.id.ivReplayUpdate,
                    View.VISIBLE);
        }
        else {
            mNotification.contentView.setViewVisibility(R.id.ivReplayUpdate,
                    View.GONE);
        }
        mNotification.contentView.setViewVisibility(R.id.tvUpdateErrReplay,
                View.VISIBLE);
        mNotification.contentView.setViewVisibility(R.id.llUpdating, View.GONE);
    }


    /**
     * 下载中界面修改
     */
    private void updateUIDowning() {
        mNotification.contentView.setViewVisibility(R.id.tvUpdateErrReplay,
                View.GONE);
        mNotification.contentView.setViewVisibility(R.id.llUpdating,
                View.VISIBLE);
        mNotification.contentView.setViewVisibility(R.id.ivReplayUpdate,
                View.GONE);
    }


    /**
     * download failed
     */
    private void updateErr() {
        updateUIErr();
        Intent replay_update = new Intent(mContext, UpgradeService.class);
        replay_update.setAction(ACTION_REPLAY);
        PendingIntent pIntent_replay_update = PendingIntent.getService(mContext,
                0, replay_update, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.contentView.setOnClickPendingIntent(R.id.ivReplayUpdate,
                pIntent_replay_update);
        mNotificationManager.notify(NOTICE_NUM, mNotification);
    }


    /**
     * clear notification and upgrade
     */
    private void launchUpgrade() {
        mHandler.post(new Runnable() {
            public void run() {
                clearNotification(mContext);
                upgrade();
            }
        });
    }


    /**
     * upgrade
     */
    private void upgrade() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(mRootPath, _cachefilename);
        Log.d(TAG, "path = " + file.getAbsolutePath());
        if (!mHasSDCard) {
            String permission = "777";
            try {
                String command = "chmod " + permission + " " +
                        file.getAbsolutePath();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
            intent.setDataAndType(Uri.fromFile(file),
                    //					Uri.parse("file://" + _cachefilename),
                    "application/vnd.android.package-archive");
        }
        else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
        stopSelf();
    }


    /**
     * 更新进度
     */
    private void updateRate(int rate) {
        Log.d(TAG, "updateRate = " + rate);
        mNotification.contentView.setTextViewText(R.id.tvPercent, rate + "%");
        mNotification.contentView.setProgressBar(R.id.pbProgress, 100, rate,
                false);
        mNotificationManager.notify(NOTICE_NUM, mNotification);
    }


    /**
     * send notification
     */
    @SuppressWarnings("deprecation")
    private void notification(Context context) {
        int icon = R.mipmap.ic_launcher;
        String app_name = context.getString(R.string.app_name);
        CharSequence contentTitle = app_name;

        // NotificationManager
        mNotificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        // 定义通知栏展现的内容信息
        long when = System.currentTimeMillis();
        mNotification = new Notification(icon, contentTitle, when);
        mNotification.contentView = new RemoteViews(getPackageName(),
                R.layout.notify_download_app);
        updateUIDowning();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            //3.0以上的版本 button才能正常使用
            mNotification.contentView.setViewVisibility(R.id.ivCloseUpdate,
                    View.VISIBLE);
        }
        else {
            mNotification.contentView.setViewVisibility(R.id.ivCloseUpdate,
                    View.GONE);
        }
        mNotification.contentView.setTextViewText(R.id.tvPercent, 0 + "%");
        mNotification.contentView.setProgressBar(R.id.pbProgress, 100, 0,
                false);
        Intent stop_update = new Intent(context, UpgradeService.class);
        stop_update.setAction(ACTION_STOP);
        PendingIntent pIntent_stop_update = PendingIntent.getService(mContext,
                0, stop_update, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.contentView.setOnClickPendingIntent(R.id.ivCloseUpdate,
                pIntent_stop_update);
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(NOTICE_NUM, mNotification);
    }


    /**
     * remove notification
     */
    public void clearNotification(Context context) {
        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTICE_NUM);
    }
}
