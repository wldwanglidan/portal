package com.tempus.portal.model.information;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.tempus.portal.manager.TokenManager;
import com.tempus.portal.model.AccessTokenInfo;
import com.tempus.portal.model.BaseRequest;

/**
 * Created by Administrator on 2016/12/20.
 */

public class DeleteAddressRequest extends BaseRequest {
    @SerializedName("id") public int travellerId;
    public DeleteAddressRequest(int travellerId) {
        this.travellerId=travellerId;
        //this.head = AppContext.sHead;
        AccessTokenInfo info = TokenManager.getInstance().getSimpleTokenInfo();
        if (info != null && !TextUtils.isEmpty(info.accessToken)) {
           /// head.token = info.accessToken;
        }
    }
}
