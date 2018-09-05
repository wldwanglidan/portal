package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/30.
 */

public class HomeMenu implements Serializable {
    @SerializedName("finFlag") public String finFlag;
    @SerializedName("merchIcon") public String merchIcon;
    @SerializedName("merchId") public String merchId;
    @SerializedName("merchLink") public String merchLink;
    @SerializedName("merchName") public String merchName;
    @SerializedName("needLogin") public String needLogin;
    //标志是否是金融界面
    public boolean isFin(){
        return finFlag.equals("1");
    }
    public boolean isNeedLogin(){
        return needLogin.equals("1");
    }
}
