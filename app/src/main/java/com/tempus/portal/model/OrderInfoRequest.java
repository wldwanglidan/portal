package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/12.
 */

public class OrderInfoRequest implements Serializable{
    @SerializedName("orderId") public String orderId;
    @SerializedName("sign") public String sign;
    @SerializedName("sysId") public String sysId;
    @SerializedName("timestamp") public String timestamp;
    @SerializedName("v") public String v;

    //
    //public OrderInfoRequest(String orderId, String sign, String sysId, String timestamp, String v) {
    //    this.orderId = orderId;
    //    this.sign = sign;
    //    this.sysId = sysId;
    //    this.timestamp = timestamp;
    //    this.v = v;
    //}
}
