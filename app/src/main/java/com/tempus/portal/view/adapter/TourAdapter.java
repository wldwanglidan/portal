package com.tempus.portal.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tempus.portal.R;
import com.tempus.portal.base.utils.glide.GlideUtils;
import com.tempus.portal.model.Finance;

/**
 * Created by Administrator on 2017/9/4.
 */

public class TourAdapter
        extends BaseQuickAdapter<Finance.TourProductListBean, BaseViewHolder> {

    public TourAdapter() {
        super(R.layout.item_tour, null);
    }


    @Override
    protected void convert(BaseViewHolder helper, Finance.TourProductListBean tourProductListBean) {
        helper.setText(R.id.tvPrice, "￥" + tourProductListBean
                .productPrice)
              .setText(R.id.tvRemark, "x" + tourProductListBean.remark)
              .setText(R.id.tvTourInfo,tourProductListBean
                      .productName+"\n"+tourProductListBean.productDesc);
        GlideUtils.displayUrl(helper.getView(R.id.ivTour),tourProductListBean
                .productPicture,R.drawable.bg_iv_default_tour);
    }
}
