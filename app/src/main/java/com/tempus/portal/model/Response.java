package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * <网络请求返回实体类>
 *
 * @author chenml@cncn.com
 * @data: 2015/11/16 20:31
 * @version: V1.0
 */
public class Response<T> {

    @SerializedName("head") public ResponseInfo mResponseInfo;

    @SerializedName("data") public T data;
}
