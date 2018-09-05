package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/8/10.
 */
public class AppIdApplyRequest extends BaseRequest {
        @SerializedName("deviceId") public String deviceId;

        public AppIdApplyRequest(String deviceId) {
            this.deviceId = deviceId;
        }
}
