package com.tempus.portal.model;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.tempus.portal.manager.TokenManager;

/**
 * Created by Administrator on 2017/2/13.
 */

public class PageRequest extends BaseRequest {
    @SerializedName("pageNum") public int pageNum;
    public PageRequest(int pageNum ) {
        this.pageNum = pageNum;
    }

}
