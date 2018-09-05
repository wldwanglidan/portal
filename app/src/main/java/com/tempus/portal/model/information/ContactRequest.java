package com.tempus.portal.model.information;

import com.google.gson.annotations.SerializedName;
import com.tempus.portal.model.BaseRequest;

public class ContactRequest extends BaseRequest {

    @SerializedName("mobile") public String mobile;
    @SerializedName("name") public String name;
    @SerializedName("email") public String email;
    @SerializedName("curUserId") public String curUserId;
    @SerializedName("seqNo") public String seqNo;
    @SerializedName("selfFlag") public String selfFlag;//0-否  1-是
    @SerializedName("contactId") public  int contactId;


    //增加
    public ContactRequest(String name, String mobile, String email, String selfFlag) {
        this.name=name;
        this.mobile=mobile;
        this.email=email;
        this.selfFlag=selfFlag;

    }

    //更新
    public ContactRequest(String name, String mobile, String email, String
            selfFlag, int contactId) {
        this.name=name;
        this.mobile=mobile;
        this.email=email;
        this.selfFlag=selfFlag;
        this.contactId=contactId;
    }

    //删除
    public ContactRequest(int contactId) {
        this.contactId=contactId;
    }

}
