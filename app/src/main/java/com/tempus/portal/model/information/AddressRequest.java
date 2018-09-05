package com.tempus.portal.model.information;

import com.google.gson.annotations.SerializedName;
import com.tempus.portal.model.BaseRequest;

public class AddressRequest extends BaseRequest {

    //@SerializedName("version") public String version;
    // version字段BaseRequest里面就有
    // 注意BaseRequest里面有的字段不要重复定义，不然会导致参数错误

    @SerializedName("detailAddress") public String detailAddress;
    @SerializedName("province") public String province;
    @SerializedName("city") public String city;
    @SerializedName("area") public String area;
    @SerializedName("receiverMobile") public String receiverMobile;
    @SerializedName("receiverName") public String receiverName;

    @SerializedName("postCode") public String postCode;
    @SerializedName("seqNo") public String seqNo;
    @SerializedName("selfFlag") public String selfFlag;//0-否  1-是
    @SerializedName("addressId") public int addressId;



    //增加
    public AddressRequest(String receiverName, String receiverMobile, String province, String city, String area, String detailAddress, String postCode, String selfFlag) {
        this.receiverName = receiverName;
        this.receiverMobile = receiverMobile;
        this.province = province;
        this.city = city;
        this.area = area;
        this.detailAddress = detailAddress;
        this.postCode = postCode;
        this.selfFlag = selfFlag;
    }

    //修改
    public AddressRequest(String receiverName, String receiverMobile, String province, String city, String area, String detailAddress, String postCode, String selfFlag, int addressId) {
        this.receiverName = receiverName;
        this.receiverMobile = receiverMobile;
        this.province = province;
        this.city = city;
        this.area = area;
        this.detailAddress = detailAddress;
        this.postCode = postCode;
        this.selfFlag = selfFlag;
        this.addressId = addressId;
    }

    //删除
    public AddressRequest(int addressId) {
        this.addressId = addressId;
    }


}