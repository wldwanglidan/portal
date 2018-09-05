package com.tempus.portal.model;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.tempus.portal.manager.TokenManager;

/**
 * Created by Administrator on 2017/9/4.
 */

public class SendSmsRequest extends BaseRequest{
    @SerializedName("mobile") public String mobile;
    @SerializedName("type") public String type;

    public SendSmsRequest(String mobile, String type) {
        this.mobile = mobile;
        this.type = type;
    }
}
