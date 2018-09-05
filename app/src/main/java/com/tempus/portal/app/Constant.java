package com.tempus.portal.app;

/**
 * 常量类
 *
 * @author jianhao025@gmail.com
 * @data: 2016/07/27 08:59
 * @version: V1.0
 */
public class Constant {

    public static final boolean DEBUG = false;

    public static final String APPLICATION_ID = "com.tempus.portal";

    private static final boolean USE_REAL_SERVER = true;

    private static final String SERVER_REAL
            = "https://tpurse.tempus.cn/tpurse/";//生产地址
    public static final String SERVER_TEST
            = "http://172.18.126.58:8011/tpurse/";//测试地址
    public static final String HOST_URL = USE_REAL_SERVER
                                          ? SERVER_REAL
                                          : SERVER_TEST;
    /**
     * 接口版本
     */
    public static final String KEY_DES = "wdvbjil/";
    public static final String LOCAL_KEY_DES = ")(*&^%$&";

    //请求网络的缓存文件大小
    public static final int NETWORK_URL_CACHE_SIZE = 1024 * 1024 * 100; //100M
    //图片缓存大小
    public static final int IMAGE_CACHE_SIZE = 1024 * 1024 * 100; //100M
    // 获取微信用户信息
    // RxBusEventName
    public static final String WX_PAY = "wxPayPlugin";
    public static final String T_PAY = "tPayPlugin";
    public static final String T_APP_PACKAGE_NAME = "com.tempus.wallet";//T钱包包名
    public static final String T_APP_CLASS_NAME
            = "com.tempus.wallet.ui.PayOrderActivity";//T钱包支付类
    public static final int REQUEST_CODE_T_PAY = 0x210;//T钱包支付

    /**
     * 检查更新
     */
    /**
     * 下载apkId
     **/
    public static final String KEY_APK_PACKAGE_ID = "key_apk_package_id";

    //下载状态
    public static final String KEY_DOWNLOAD_STATE = "key_download_state";
    //SPConstant
    /**
     * APP的请求码
     */
    public static final String KEY_APP = "key_app";
    /**
     * APP访问令牌
     */
    public static final String KEY_ACCESS_TOKEN = "key_access_token";

    /**
     * uuid
     */
    public static final String KEY_UUID = "key_uuid";

    //用户信息
    public static final String KEY_USER = "user";
    public static final String KEY_VERSION_CODE = "version_code";
    //全局参数
    public static final String KEY_GLOBALPARAMS = "globalParams";

    public static final String KEY_WIFI_UPDATE = "key_wifi_update";
    public static final String KEY_MESSAGE = "key_message";
}
