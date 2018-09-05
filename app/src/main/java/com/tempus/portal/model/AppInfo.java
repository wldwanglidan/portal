package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * <获取App实体类>
 *
 * @author fuxj
 * @data: 15/11/10 下午7:34
 * @version: V1.0
 */
public class AppInfo extends BaseResponse implements Serializable {

    @SerializedName("appId")
    public String appId;

    @SerializedName("appSecret")
    public String appSecret;

}
