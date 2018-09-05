package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * <AccessToken实体类>
 *
 * @author fuxj
 * @data: 15/11/10 下午7:35
 * @version: V1.0
 */
public class AccessTokenInfo extends BaseResponse implements Serializable {

    @SerializedName("accessToken")
    public String accessToken;

    @SerializedName("expireTime")
    public long expireTime;

    @SerializedName("sessionKey")
    public String sessionKey;

    @SerializedName("sessionSecret")
    public String sessionSecret;

    //第一次获取token的时间
    @SerializedName("getAccessTokenTime")
    public long getAccessTokenTime;

}
