package com.tempus.portal.model.information;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public class PassengerInformation implements Serializable {

    @SerializedName("birthday") public String birthday;
    @SerializedName("enName") public String enName;
    @SerializedName("mobile") public String mobile;
    @SerializedName("name") public String name;
    @SerializedName("passengerId") public int passengerId;
    @SerializedName("selfFlag") public String selfFlag;
    @SerializedName("sex") public String sex;
    @SerializedName("surname") public String surname;
    @SerializedName("certInfos") public List<CertInfosBean> certInfos;

    public static class CertInfosBean implements Serializable {
        @SerializedName("certId") public int certId;
        @SerializedName("certNo") public String certNo;
        @SerializedName("certType") public String certType;
        @SerializedName("expireDate") public String expireDate;
        @SerializedName("nationality") public String nationality;
        @SerializedName("nationlityName") public String nationlityName;
    }
}
