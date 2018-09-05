package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

public class CancelCollectionRequest extends BaseRequest {
    @SerializedName("list") public List<Integer> ids;


    public CancelCollectionRequest(List<Integer> ids) {
        this.ids = ids;
    }
}
