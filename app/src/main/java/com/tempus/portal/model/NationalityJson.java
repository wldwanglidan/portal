package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/4/21.
 */

public class NationalityJson {

    @SerializedName("countryCode") public String countryCode;
    @SerializedName("countryName") public String countryName;
    @SerializedName("initial") public String initial;
    @SerializedName("hotFlag") public String hotFlag;
}
