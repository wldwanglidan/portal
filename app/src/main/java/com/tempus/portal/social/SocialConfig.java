package com.tempus.portal.social;

import com.tempus.portal.app.App;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

/**
 * Created by Administrator on 2017/1/22.
 */

public class SocialConfig {
    private static final String QQ_ID = "1106393174";
    private static final String QQ_KEY = "UQvEmlVYCjg9CgGz";
    //正式:
    //商户号1446221502
    //appid：wxee6d0b3686c2935c
    //secret：dde1d53558eb3c5bd72fa3975fd56bc9
    //key：key:fa245ff5e1109dcf6fc61bec11c40b2c
    //测试:
    //商户号1434435902
    //appid：wxabd03a452a481919
    //secret：adfhkuv78d331dfdfcnv777fdsolm818
    //key：adfhkuv78d331dfdfcnv777fdsolm818
    public static final String WECHAT_ID = "wx7dd12e6025d6b5f6";
    public static final String WECHAT_SECRET
            = "d94023d3b27e32b798673086ceb5a093";
    private static final String SINA_ID = "2496457566";
    private static final String SINA_KEY = "ecda86794275f662db2cf3c52025a249";

    private static UMSocial sShareControl;


    public static void init(App appApplication) {
        Config.DEBUG = false;
        Config.isJumptoAppStore = true;//对应平台没有安装的时候跳转转到应用商店下载
        PlatformConfig.setQQZone(QQ_ID, QQ_KEY);
        PlatformConfig.setWeixin(WECHAT_ID, WECHAT_SECRET);
        PlatformConfig.setSinaWeibo(SINA_ID, SINA_KEY,
                "http://sns.whalecloud.com/sina2/callback");
        if (sShareControl == null) {
            sShareControl = new UMSocial(appApplication);
        }
        //UMShareConfig config = new UMShareConfig();
        //config.isNeedAuthOnGetUserInfo(true);
        //UMShareAPI.get(appApplication).setShareConfig(config);
        //PushAgent mPushAgent = PushAgent.getInstance(appApplication);
        ////注册推送服务，每次调用register方法都会回调该接口
        //mPushAgent.register(new IUmengRegisterCallback() {
        //
        //    @Override public void onSuccess(String deviceToken) {
        //        //注册成功会返回device token
        //    }
        //
        //
        //    @Override public void onFailure(String s, String s1) {
        //
        //    }
        //});
        //mPushAgent.setDebugMode(false);
    }


    public static synchronized UMSocial getShareControl() {
        return sShareControl;
    }
}
