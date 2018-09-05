package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * <订单信息>
 *
 * @author chenml@cncn.com
 * @data: 2015/11/19 15:53
 * @version: V1.0
 */
public class RechargeOrderInfo implements Serializable {
    //订单号
    @SerializedName("orderId") public String orderId;

    //微信支付订单号
    @SerializedName("prepayId") public String prepayId;

    //时间戳
    @SerializedName("timestamp") public long timestamp;

    //随机串
    @SerializedName("nonestr") public String noneStr;

    //签名
    @SerializedName("sign") public String sign;

    //交易金额
    @SerializedName("tranAmt") public double tranAmt;
    //订单信息
    @SerializedName("orderDesc") public String orderDesc;
    //是否支持T币抵扣
    @SerializedName("supportTcoin") public String supportTcoin;
    //是否国内或国际
    @SerializedName("domestic") public boolean domestic;

    @SerializedName("allInPayReq") public String allInPayReq;
    @SerializedName("appId") public String appId;
    @SerializedName("sysid") public String sysid;
    @SerializedName("v") public String v;
}
