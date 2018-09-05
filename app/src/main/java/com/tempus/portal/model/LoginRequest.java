package com.tempus.portal.model;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.tempus.portal.manager.TokenManager;

/**
 * Created by Administrator on 2017/9/4.
 */

public class LoginRequest extends BaseRequest{
    @SerializedName("mobile") public String mobile;
    @SerializedName("verifyCode") public String verifyCode;

    public LoginRequest(String mobile, String verifyCode) {
        this.mobile = mobile;
        this.verifyCode = verifyCode;
    }
}
