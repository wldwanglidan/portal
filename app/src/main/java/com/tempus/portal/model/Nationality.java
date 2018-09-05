package com.tempus.portal.model;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/21.
 */

public class Nationality implements Serializable {
    public int resId;

    @SerializedName("countryName") public String name;
    @SerializedName("countryCode") public String countryCode;//2字码
       public String pinyin;
    @SerializedName("initial")  public String header;
    public boolean top = false;


    public String getHeader() {
        if (this.header != null) return this.header;
        if (TextUtils.isEmpty(this.pinyin) || Character.isDigit(this.pinyin.charAt(0))) {
            this.header = null;
        } else {
            this.header = this.pinyin.substring(0, 1).toUpperCase();
            char temp = this.header.charAt(0);
            if (temp < 'A' || temp > 'Z') {
                this.header = "#";
            }
        }
        return this.header;
    }
}
