package com.tempus.portal.model.usercenter;

import com.google.gson.annotations.SerializedName;
import com.tempus.portal.model.BaseRequest;

public class UserInfRequest extends BaseRequest {

    //@SerializedName("version") public String version;
    // version字段BaseRequest里面就有
    // 注意BaseRequest里面有的字段不要重复定义，不然会导致参数错误

    @SerializedName("curUserId") public String curUserId;
    @SerializedName("headUrl") public String headUrl;
    @SerializedName("seqNo") public String seqNo;

    @SerializedName("mobile") public String mobile;
    @SerializedName("nickname") public String nickname;
    @SerializedName("vipLevel") public String vipLevel;

    /**
     * 上传图片和得到用户的信息
     */
    public UserInfRequest() {
    }


    /**
     * 更新昵称
     * @param nickname
     */
    public UserInfRequest(String nickname) {
        this.nickname = nickname;
    }


    /**
     * 得到用户的信息？
     * @param curUserId
     * @param seqNo
     */
    public UserInfRequest(String curUserId,String seqNo) {
        this.curUserId = curUserId;
        this.seqNo = seqNo;
    }


}