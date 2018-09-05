package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/8/9.
 */
public class VersionInfo extends BaseResponse {

    @SerializedName("appName")
    public String appName;      //应用名称
    @SerializedName("verCode")
    public int verCode;      //版本号
    @SerializedName("verName")
    public String verName;      //版本

    @SerializedName("packName")
    public String  packName;      //安装包名称

    @SerializedName("downloadUrl")
    public String downloadUrl;          //下载地址
    @SerializedName("updateInfo") public String updateInfo;
    @SerializedName("updateType") public String updateType;

    //强制升级内容
    //public ForceUpdate forceUpdate;




}
