package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/7.
 */

public class VersionUpdateRequest extends BaseRequest {
    @SerializedName("verCode") public int verCode;
    public VersionUpdateRequest(int vC) {
        this.verCode = vC;
    }
}
