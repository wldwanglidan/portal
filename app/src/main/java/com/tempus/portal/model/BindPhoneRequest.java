package com.tempus.portal.model;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.tempus.portal.manager.TokenManager;

/**
 * Created by Administrator on 2017/9/4.
 */

public class BindPhoneRequest extends BaseRequest {
    @SerializedName("unionId") public String unionId;
    @SerializedName("mobile") public String mobile;
    @SerializedName("verifyCode") public String verifyCode;


    public BindPhoneRequest(String unionId, String mobile, String verifyCode) {
        this.unionId = unionId;
        this.mobile = mobile;
        this.verifyCode = verifyCode;
    }
}
