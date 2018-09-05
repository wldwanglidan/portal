package com.tempus.portal.model;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.tempus.portal.app.AppContext;
import com.tempus.portal.manager.TokenManager;
import com.tempus.portal.manager.UserManager;

/**
 * Created by Administrator on 2016/8/11.
 */
public abstract class BaseRequest {
    //@SerializedName("sessionContext")
    //public SessionContext mSessionContext;
    @SerializedName("channel") public String channel = "P";
    @SerializedName("serviceCode") public String serviceCode;
    @SerializedName("localDateTime") public String localDateTime;
    //@SerializedName("userId") public String userId;
    @SerializedName("externalReferenceNo") public String externalReferenceNo;
    @SerializedName("userReferenceNo") public String userReferenceNo;
    @SerializedName("stepCode") public String stepCode = "1";
    @SerializedName("accessSource") public String accessSource = "3";
    @SerializedName("accessSourceType") public String accessSourceType;
    @SerializedName("version") public String version
            = AppContext.APP_VERSION_NAME;
    @SerializedName("accessToken") public String accessToken
            = (TokenManager.getInstance().getSimpleTokenInfo() != null &&
            !TextUtils.isEmpty(TokenManager.getInstance()
                                           .getSimpleTokenInfo().accessToken))
              ? TokenManager.getInstance().getSimpleTokenInfo().accessToken
              : "";

    @SerializedName("userId") public String userId = UserManager.getInstance()
                                                                .isLogin()
                                                     ? UserManager.getInstance()
                                                                  .getUserInfo().userId
                                                     : "";

}


