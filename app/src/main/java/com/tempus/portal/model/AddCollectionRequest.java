package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/12.
 */

public class AddCollectionRequest extends BaseRequest{
    @SerializedName("merchId") public String merchId;
    @SerializedName("title") public String title;
    @SerializedName("url") public String url;
    public AddCollectionRequest(String merchId, String title, String url) {
        this.merchId = merchId;
        this.title = title;
        this.url = url;
    }
}
