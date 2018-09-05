package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/17.
 */

public class GetSignRequest implements Serializable {
    @SerializedName("userId") public String userId;
    public GetSignRequest(String userId) {
        this.userId = userId;
    }
}
