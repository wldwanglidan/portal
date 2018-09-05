package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/5.
 */

public class Collection   {
    public boolean isSelect;

    @SerializedName("date") public String date;
    @SerializedName("favorId") public int favorId;
    @SerializedName("logo") public String logo;
    @SerializedName("title") public String title;
    @SerializedName("url") public String url;
}
