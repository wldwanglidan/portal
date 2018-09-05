package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import com.tempus.portal.dao.ReturnCodeConfig;
import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/11.
 */
public abstract class BaseResponse implements Serializable {
    @SerializedName("errorCode") public String errorCode;
    @SerializedName("errorMsg") public String errorMsg;
    @SerializedName("retStatus") public int retStatus;
    public boolean isSuccess() {
        return retStatus == ReturnCodeConfig.getInstance().successCode;
    }


    public boolean isEmptyCode() {
        return ReturnCodeConfig.getInstance().isEmptyCode(retStatus);
    }
}
