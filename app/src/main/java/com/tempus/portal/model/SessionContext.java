package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/8/10.
 */
public class SessionContext {
    //@SerializedName("sign") public String sign;
    //@SerializedName("timestamp") public String timestamp;
    //@SerializedName("token") public String token;

    @SerializedName("channel") public String channel;
    @SerializedName("serviceCode") public String serviceCode;
    @SerializedName("localDateTime") public String localDateTime;
    @SerializedName("userId") public String userId;
    @SerializedName("externalReferenceNo") public String externalReferenceNo;
    @SerializedName("userReferenceNo") public String userReferenceNo;
    @SerializedName("stepCode") public String stepCode;
    @SerializedName("accessSource") public String accessSource;
    @SerializedName("accessSourceType") public String accessSourceType;
    @SerializedName("version") public String version;
    @SerializedName("accessToken") public String accessToken;
}
