package com.tempus.portal.model.information;

import com.google.gson.annotations.SerializedName;
import com.tempus.portal.model.BaseResponse;
import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */

public class AddressInformation implements Serializable {
        @SerializedName("addressId") public int addressId;
        @SerializedName("area") public String area;
        @SerializedName("city") public String city;
        @SerializedName("detailAddress") public String detailAddress;
        @SerializedName("receiverMobile") public String receiverMobile;
        @SerializedName("postCode") public String postCode;
        @SerializedName("province") public String province;
        @SerializedName("receiverName") public String receiverName;
        @SerializedName("selfFlag") public String selfFlag;
        @SerializedName("userId") public String userId;


}
