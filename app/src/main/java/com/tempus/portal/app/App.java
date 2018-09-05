package com.tempus.portal.app;

import android.support.multidex.MultiDexApplication;
import android.util.Log;
import com.tempus.portal.base.BaseAppManager;
import com.tempus.portal.base.utils.ACache;
import com.tempus.portal.base.utils.NetworkConfig;
import com.tempus.portal.dao.ReturnCode;
import com.tempus.portal.dao.ReturnCodeConfig;
import com.tempus.portal.dao.cache.DataProvider;
import com.tempus.portal.social.SocialConfig;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

/**
 * 应用
 *
 * @author jianhao025@gmail.com
 * @data: 2016/07/27 08:59
 * @version: V1.0
 */
public class App extends MultiDexApplication {
    private static App mInstance;


    public static synchronized App getInstance() {
        return mInstance;
    }


    private ACache mACache;


    public void onCreate() {
        super.onCreate();
        App.mInstance = this;
        mACache = ACache.get(this);
        AppContext.getInstance().init(this);
        SocialConfig.init(this);
        UMShareAPI.get(this);
        DataProvider.init(this);
        NetworkConfig.setBaseUrl(Constant.HOST_URL);
        NetworkConfig.setCacheFile(
                DirContext.getInstance().getDir(DirContext.DirEnum.CACHE),
                Constant.NETWORK_URL_CACHE_SIZE);
        ReturnCodeConfig.getInstance()
                        .initReturnCode(ReturnCode.CODE_SUCCESS,
                                ReturnCode.CODE_EMPTY);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                Log.e("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub

            }
        };
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }


    @Override public void onLowMemory() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onLowMemory();
    }


    public void exitApp() {
        MobclickAgent.onKillProcess(this);
        BaseAppManager.getInstance().clear();
        System.gc();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    public static ACache getACache() {
        return getInstance().mACache;
    }
}
