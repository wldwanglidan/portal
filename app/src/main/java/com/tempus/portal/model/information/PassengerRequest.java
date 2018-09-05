package com.tempus.portal.model.information;

import com.google.gson.annotations.SerializedName;
import com.tempus.portal.model.BaseRequest;

public class PassengerRequest extends BaseRequest {
    @SerializedName("certNo") public String certNo;
    @SerializedName("certType") public String certType;
    @SerializedName("curUserId") public String curUserId;
    @SerializedName("enName") public String enName;
    @SerializedName("expireDate") public String expireDate;
    @SerializedName("mobile") public String mobile;
    @SerializedName("name") public String name;
    @SerializedName("nationality") public String nationality;
    @SerializedName("sex") public String sex;
    @SerializedName("birthday") public String birthday;
    @SerializedName("surname") public String surname;

    @SerializedName("seqNo") public String seqNo;
    @SerializedName("selfFlag") public String selfFlag;//0-否  1-是
    @SerializedName("passengerId") public int passengerId;


    //增加
    public PassengerRequest(String name, String surname, String enName, String mobile, String sex, String birthday, String nationality, String certType, String certNo, String expireDate, String selfFlag) {
        this.name = name;
        this.surname = surname;
        this.enName = enName;
        this.mobile = mobile;
        this.sex = sex;
        this.birthday = birthday;
        this.nationality = nationality;
        this.certType = certType;
        this.certNo = certNo;
        this.expireDate = expireDate;
        this.selfFlag = selfFlag;
    }


    //修改
    public PassengerRequest(String name, String surname, String enName, String mobile, String sex, String birthday, String nationality, String certType, String certNo, String expireDate, String selfFlag, int passengerId) {
        this.name = name;
        this.surname = surname;
        this.enName = enName;
        this.mobile = mobile;
        this.sex = sex;
        this.birthday = birthday;
        this.nationality = nationality;
        this.certType = certType;
        this.certNo = certNo;
        this.expireDate = expireDate;
        this.selfFlag = selfFlag;
        this.passengerId = passengerId;
    }


    //删除
    public PassengerRequest(int passengerId) {
        this.passengerId = passengerId;
    }
}
