package com.tempus.portal.model.information;

import com.google.gson.annotations.SerializedName;
import com.tempus.portal.model.BaseResponse;
import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */

public class ContactInformation  implements Serializable {
        //extends BaseResponse  这里是必须的吗？因为ListData<T> extends
        // BaseResponse，所以这里不需要extends BaseResponse
        @SerializedName("contactId") public int contactId;
        @SerializedName("email") public String email;
        @SerializedName("mobile") public String mobile;
        @SerializedName("name") public String name;
        @SerializedName("selfFlag") public String selfFlag;
        @SerializedName("userId") public boolean userId;


}
