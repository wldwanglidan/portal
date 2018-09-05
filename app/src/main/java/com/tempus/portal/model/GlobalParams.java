package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/22.
 */

public class GlobalParams extends BaseResponse implements Serializable {
    @SerializedName("backgroundImg") public String backgroundImg;
    @SerializedName("privacyAgreementUrl") public String privacyAgreementUrl;
    @SerializedName("serviceAgreementUrl") public String serviceAgreementUrl;
    @SerializedName("userAgreementUrl") public String userAgreementUrl;
}
