package com.tempus.portal.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by Administrator on 2017/9/6.
 */

public class Finance extends BaseResponse{
    @SerializedName("fundProductLink") public String fundProductLink;
    @SerializedName("fundProductPicture") public String fundProductPicture;
    @SerializedName("vcProductLink") public String vcProductLink;
    @SerializedName("vcProductPicture") public String vcProductPicture;
    @SerializedName("insuranceProductList")
    public List<InsuranceProductListBean> insuranceProductList;
    @SerializedName("tourProductList") public List<TourProductListBean>
            tourProductList;
    @SerializedName("tpurseProductList") public List<TpurseProductListBean>
            tpurseProductList;

    public static class InsuranceProductListBean {
        @SerializedName("needLogin") public String needLogin;
        @SerializedName("productDesc") public String productDesc;
        @SerializedName("productLink") public String productLink;
        @SerializedName("productName") public String productName;
        @SerializedName("productPicture") public String productPicture;
        @SerializedName("productPrice") public String productPrice;
        @SerializedName("remark") public String remark;
    }

    public static class TourProductListBean {
        @SerializedName("needLogin") public String needLogin;
        @SerializedName("productDesc") public String productDesc;
        @SerializedName("productLink") public String productLink;
        @SerializedName("productName") public String productName;
        @SerializedName("productPicture") public String productPicture;
        @SerializedName("productPrice") public String productPrice;
        @SerializedName("remark") public String remark;
    }

    public static class TpurseProductListBean {
        @SerializedName("needLogin") public String needLogin;
        @SerializedName("productDesc") public String productDesc;
        @SerializedName("productLink") public String productLink;
        @SerializedName("productName") public String productName;
        @SerializedName("productPicture") public String productPicture;
        @SerializedName("productPrice") public String productPrice;
        @SerializedName("remark") public String remark;
    }
}
