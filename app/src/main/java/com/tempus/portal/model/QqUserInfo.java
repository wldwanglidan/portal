package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/9.
 */

public class QqUserInfo implements Serializable {
    @SerializedName("screen_name") public String screenName;
    @SerializedName("vip") public String vip;
    @SerializedName("city") public String city;
    @SerializedName("gender") public String gender;
    @SerializedName("province") public String province;
    @SerializedName("auth_time") public String authTime;
    @SerializedName("pf") public String pf;
    @SerializedName("openid") public String openid;
    @SerializedName("yellow_vip_level") public String yellowVipLevel;
    @SerializedName("iconurl") public String iconurl;
    @SerializedName("page_type") public String pageType;
    @SerializedName("pfkey") public String pfkey;
    @SerializedName("expiration") public String expiration;
    @SerializedName("sendinstall") public String sendinstall;
    @SerializedName("level") public String level;
    @SerializedName("ret") public String ret;
    @SerializedName("is_yellow_vip") public String isYellowVip;
    @SerializedName("unionid") public String unionid;
    @SerializedName("msg") public String msg;
    @SerializedName("appid") public String appid;
    @SerializedName("accessToken") public String accessToken;
    @SerializedName("pay_token") public String payToken;
    @SerializedName("is_yellow_year_vip") public String isYellowYearVip;
    @SerializedName("profile_image_url") public String profileImageUrl;
    @SerializedName("name") public String name;
    @SerializedName("uid") public String uid;
    @SerializedName("expires_in") public String expiresIn;
}
