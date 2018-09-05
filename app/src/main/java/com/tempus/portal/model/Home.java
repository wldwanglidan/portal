package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 */

public class Home extends BaseResponse implements Serializable {
    @SerializedName("merchList") public List<HomeMenu> merchList;
}
