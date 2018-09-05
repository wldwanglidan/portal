package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/6.
 */

public class SubmitFeedbackRequest extends BaseRequest{
    @SerializedName("content") public String content;
    public SubmitFeedbackRequest(String content) {
        this.content = content;
    }
}

