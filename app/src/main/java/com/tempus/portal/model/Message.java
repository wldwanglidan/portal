package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * <用户实体类>
 *
 * @author fuxj
 * @data: 15/11/10 下午7:38
 * @version: V1.0
 */
public class Message  implements Serializable {

    @SerializedName("newsDate") public String newsDate;
    @SerializedName("newsId") public int newsId;
    @SerializedName("newsImg") public String newsImg;
    @SerializedName("newsLink") public String newsLink;
    @SerializedName("newsTitle") public String newsTitle;
    @SerializedName("newsType") public String newsType;

}
