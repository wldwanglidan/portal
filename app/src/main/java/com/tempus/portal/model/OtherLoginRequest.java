package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/2/13.
 */

public class OtherLoginRequest extends BaseRequest {
    @SerializedName("address") public String address;
    @SerializedName("avatar") public String avatar;
    @SerializedName("city") public String city;
    @SerializedName("country") public String country;
    @SerializedName("gender") public String gender;
    @SerializedName("nickname") public String nickname;
    @SerializedName("openId") public String openId;
    @SerializedName("province") public String province;
    @SerializedName("chlType") public String chlType;
    @SerializedName("unionid") public String unionid;



    public OtherLoginRequest(String avatar, String city, String gender, String nickname, String openId, String province, String chlType, String unionid) {
        this.avatar = avatar;
        this.city = city;
        this.gender = gender;
        this.nickname = nickname;
        this.openId = openId;
        this.province = province;
        this.chlType = chlType;
        this.unionid = unionid;
    }


    public OtherLoginRequest(String avatar, String city, String country, String gender, String nickname, String openId, String province, String chlType, String unionid) {
        this.avatar = avatar;
        this.city = city;
        this.country = country;
        this.gender = gender;
        this.nickname = nickname;
        this.openId = openId;
        this.province = province;
        this.chlType = chlType;
        this.unionid = unionid;
    }
}
