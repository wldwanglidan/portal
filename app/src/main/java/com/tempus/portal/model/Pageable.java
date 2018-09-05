package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/12/8.
 */

public class Pageable {
    @SerializedName("pageNumber") public int pageNumber;
    @SerializedName("pageSize") public int pageSize;


    public Pageable(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
