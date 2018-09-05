package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/17.
 */

public class Sign extends  BaseResponse implements Serializable{
    @SerializedName("apiVersion") public String apiVersion;
    @SerializedName("appid") public String appid;
    @SerializedName("licence") public String licence;
    @SerializedName("nonce") public String nonce;
    @SerializedName("orderNo") public String orderNo;
    @SerializedName("sign") public String sign;
    @SerializedName("type") public String type;
}
