package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/12.
 */

public class OrderInfo extends BaseResponse{
    @SerializedName("orderId") public String orderId;
    @SerializedName("orderName") public String orderName;
    @SerializedName("orderPrice") public double orderPrice;
    @SerializedName("supportTcoin") public String supportTcoin;
}
