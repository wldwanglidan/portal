package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * <用户实体类>
 *
 * @author fuxj
 * @data: 15/11/10 下午7:38
 * @version: V1.0
 */
public class User extends BaseResponse implements Serializable {


    @SerializedName("authenTicket") public String authenTicket;
    @SerializedName("authenUserId") public String authenUserId;
    @SerializedName("userId") public String userId;
    @SerializedName("headUrl") public String headUrl;
    @SerializedName("loginTime") public String loginTime;
    @SerializedName("mobile") public String mobile;
    @SerializedName("nickname") public String nickname;
    @SerializedName("userName") public String userName;
    @SerializedName("vipLevel") public String vipLevel;
    @SerializedName("hasUnreadNews") public String hasUnreadNews;
    @SerializedName("bindFlag") public String bindFlag;//绑定用户手机标志0-否1-是
    @SerializedName("isQq") public boolean isQq = false;//用于本地判断常登陆是否为QQ登陆
    @SerializedName("isWxLogin") public boolean isWxLogin = false;

    //用于本地判断常登陆是否为微信登陆

    public boolean isWxLogin() {
        return isWxLogin;
    }


    public boolean isQq() {
        return isQq;
    }
    //
    //
    public boolean isBindPhone() {
        return bindFlag.equals("1");
    }
    public boolean isUnreadNews() {
        return hasUnreadNews.equals("1");
    }
}
