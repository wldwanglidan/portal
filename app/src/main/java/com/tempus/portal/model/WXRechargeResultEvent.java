package com.tempus.portal.model;

/**
 * Created by Administrator on 2016/8/1.
 */
public class WXRechargeResultEvent {
    public WXRechargeResultEvent(int code, String errStr) {
        this.errCode = code;
        this.errStr = errStr;
    }

    public int errCode;
    public String errStr;
    public String transaction;
    public String openId;
    public RechargeOrderInfo rechargeOrderInfo;
}
