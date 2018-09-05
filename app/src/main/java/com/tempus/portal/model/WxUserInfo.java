package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class WxUserInfo extends BaseInfo implements Serializable {

    @SerializedName("openid") public String openid;//普通用户标识,对当前开发者账号唯一

    @SerializedName("name") public String name;//普通用户昵称

    @SerializedName("gender") public String gender;//普通用户性别 1为男性 2未女性

    @SerializedName("city") public String city;//普通用户个人资料填写的城市

    @SerializedName("language") public String language;//zh_CN

    @SerializedName("province") public String province;//普通用户个人资料填写的省份

    @SerializedName("country") public String country;//国家 如中国为CN

    @SerializedName("iconurl") public String iconurl; //用户头像

    @SerializedName("uid") public String uid;//用户统一标识

    @SerializedName("openIdType") public String openIdType;
    @SerializedName("unionid") public String unionid;

    @SerializedName("scope") public String scope;
    @SerializedName("screen_name") public String screenName;
    @SerializedName("refresh_token") public String refreshToken;
    @SerializedName("profile_image_url") public String profileImageUrl;
    @SerializedName("access_token") public String accessToken;
    @SerializedName("expires_in") public String expiresIn;
}
