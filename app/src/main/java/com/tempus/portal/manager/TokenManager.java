package com.tempus.portal.manager;

import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.tempus.portal.app.App;
import com.tempus.portal.app.AppContext;
import com.tempus.portal.app.Constant;
import com.tempus.portal.base.utils.ACache;
import com.tempus.portal.base.utils.DES;
import com.tempus.portal.base.utils.Md5Tool;
import com.tempus.portal.base.utils.StringUtils;
import com.tempus.portal.dao.exception.NoNetworkException;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.model.AccessTokenInfo;
import com.tempus.portal.model.AppInfo;
import com.tempus.portal.model.TokenApplyRequest;
import io.reactivex.Observable;

/**
 * @author jianhao025@gmail.com
 * @data: 2016/07/27 08:59
 * @version: V1.0
 */
public class TokenManager {
    private static final String TAG = "TokenManager";
    private static TokenManager mInstance;

    private AppInfo mAppInfo;
    private AccessTokenInfo mAccessTokenInfo;

    private boolean refreshLoginFlag;
    private ACache mACache;


    public static TokenManager getInstance() {
        if (mInstance == null) {
            synchronized (TokenManager.class) {
                if (mInstance == null) {
                    mInstance = new TokenManager();
                }
            }
        }
        return mInstance;
    }


    private TokenManager() {
        mACache = App.getACache();
    }


    //验证设置是否已经获取Token且Token有效
    private boolean isTokenVerification() {
        if (mAccessTokenInfo != null && !isTimeout(mAccessTokenInfo)) {
            initAppInfo();
            return true;
        }
        else {
            String appStr = mACache.getAsString(Constant.KEY_ACCESS_TOKEN);
            if (!TextUtils.isEmpty(appStr)) {
                try {
                    String decryptToken = DES.decryptDES(Constant.LOCAL_KEY_DES,
                            appStr);
                    AccessTokenInfo accessTokenInfo = new Gson().fromJson(
                            decryptToken, AccessTokenInfo.class);
                    if (!isTimeout(accessTokenInfo)) {
                        this.mAccessTokenInfo = accessTokenInfo;
                        return true;
                    }
                    else {
                        mACache.put(Constant.KEY_ACCESS_TOKEN, "");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "initApi(" + e + ")");
                }
            }
        }
        mAccessTokenInfo = null;
        return false;
    }


    private boolean isTimeout(AccessTokenInfo accessTokenInfo) {
        return System.currentTimeMillis() / 1000 >
                accessTokenInfo.getAccessTokenTime;
    }


    private void initAppInfo() {
        if (mAppInfo == null) {
            String str = mACache.getAsString(Constant.KEY_APP);
            try {
                str = DES.decryptDES(Constant.LOCAL_KEY_DES, str);
                mAppInfo = new Gson().fromJson(str, AppInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public AccessTokenInfo getSimpleTokenInfo() {
        return mAccessTokenInfo;
    }


    public Observable<AccessTokenInfo> getTokenInfo() {
        return Observable.just(refreshLoginFlag).flatMap(o1 -> {
            //if (refreshLoginFlag) {
            //    refreshLoginFlag = false;
            //    return TokenManager.getInstance()
            //                       .getTokenInfoObservable()
            //                       .flatMap(
            //                               tokenInfo -> UserManager.getRefreshLoginParams(
            //                                       tokenInfo))
            //                       .flatMap(o -> getTokenInfoObservable());
            //}
            return getTokenInfoObservable();
        });
    }


    private Observable<AccessTokenInfo> getTokenInfoObservable() {
        if (!AppContext.isNetworkAvailable()) {
            return Observable.error(new NoNetworkException());
        }
        if (isTokenVerification()) {
            return Observable.just(mAccessTokenInfo);
        }
        else {
            String str = mACache.getAsString(Constant.KEY_APP);
            if (TextUtils.isEmpty(str)) {
                mACache.put(Constant.KEY_UUID, AppContext.DEVICE_ID);
                return DataManager.getAppInfo().flatMap(appInfoResponse -> {
                    if (appInfoResponse.isSuccess()) {
                        mAppInfo = appInfoResponse;
                        try {
                            mACache.put(Constant.KEY_APP,
                                    DES.encryptDES(Constant.LOCAL_KEY_DES,
                                            new Gson().toJson(
                                                    appInfoResponse)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return requestToken(mAppInfo.appId);
                    }
                    else {
                        return Observable.error(new ErrorThrowable(
                                appInfoResponse.retStatus,
                                appInfoResponse.errorMsg));
                    }
                });
            }
            else {
                initAppInfo();
                return requestToken(mAppInfo.appId);
            }
        }
    }


    private Observable<AccessTokenInfo> requestToken(String appId) {
        long timestamp = System.currentTimeMillis() / 1000;
        String uuid = mACache.getAsString(Constant.KEY_UUID);
        if (StringUtils.isEmpty(uuid)) {
            uuid = AppContext.DEVICE_ID;
            mACache.put(Constant.KEY_UUID, uuid);
        }
        AppContext.DEVICE_ID = uuid;
        String signature = Md5Tool.MD5(uuid + mAppInfo.appSecret + timestamp);
        return DataManager.getTokenInfo(new TokenApplyRequest(uuid, appId, signature, timestamp + ""))
                          .flatMap(tokenInfoResponse -> {
                              if (tokenInfoResponse.isSuccess()) {
                                  tokenInfoResponse.getAccessTokenTime =
                                          System.currentTimeMillis() / 1000 +
                                                  tokenInfoResponse.expireTime;
                                  mAccessTokenInfo = tokenInfoResponse;
                                  try {
                                      mACache.put(Constant.KEY_ACCESS_TOKEN,
                                              DES.encryptDES(
                                                      Constant.LOCAL_KEY_DES,
                                                      new Gson().toJson(
                                                              tokenInfoResponse)));
                                  } catch (Exception e) {
                                      e.printStackTrace();
                                  }
                                  return Observable.just(mAccessTokenInfo);
                              }
                              else {
                                  return Observable.error(new ErrorThrowable(
                                          tokenInfoResponse.retStatus,
                                          tokenInfoResponse.errorMsg));
                              }
                          });
    }


    public AccessTokenInfo resetToken() {
        String tokenTemp = new Gson().toJson(this.mAccessTokenInfo);
        AccessTokenInfo temp = new Gson().fromJson(tokenTemp,
                AccessTokenInfo.class);
        this.mAccessTokenInfo = null;
        mACache.put(Constant.KEY_ACCESS_TOKEN, "");
        return temp;
    }


    public AppInfo getAppInfo() {
        return mAppInfo;
    }


    public void setRefreshLogin() {
        refreshLoginFlag = true;
    }
}
