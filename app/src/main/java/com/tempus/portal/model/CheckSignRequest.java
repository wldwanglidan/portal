package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/4/17.
 */

public class CheckSignRequest {
    @SerializedName("order_no") public String order_no;
    @SerializedName("sign") public String sign;


    public CheckSignRequest(String order_no, String sign) {
        this.order_no = order_no;
        this.sign = sign;
    }
}
