package com.tempus.portal.view.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tempus.portal.R;
import com.tempus.portal.base.utils.glide.GlideUtils;
import com.tempus.portal.model.Finance;
import com.tempus.portal.view.widget.GridSpacingItemDecoration;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */

public class InsuranceAdapter
        extends BaseQuickAdapter<Finance.InsuranceProductListBean, BaseViewHolder> {

    public InsuranceAdapter() {
        super(R.layout.item_insurance, null);
    }


    @Override
    protected void convert(BaseViewHolder helper, Finance.InsuranceProductListBean insuranceProductListBean) {
        helper.setText(R.id.tvInsuranceName,
                insuranceProductListBean.productName)
              .setText(R.id.tvInsurancePrice,
                      "￥" + insuranceProductListBean.productPrice);
        GlideUtils.display(helper.getView(R.id.ivInsurance),
                insuranceProductListBean.productPicture);
        List<String> list = Arrays.asList(insuranceProductListBean
                .productDesc.split("[|]"));

        if (list != null &&list.size() > 0) {
            InsuranceLabelAdapter insuranceLabelAdapter
                    = new InsuranceLabelAdapter();
            RecyclerView rv = helper.getView(R.id.rv);
            int spanCount = 2;//跟布局里面的spanCount属性是一致的
            int spacing = 10;//每一个矩形的间距
            boolean includeEdge = false;//如果设置成false那边缘地带就没有间距
            rv.addItemDecoration(
                    new GridSpacingItemDecoration(spanCount, spacing,
                            includeEdge));
            rv.setLayoutManager(new GridLayoutManager(mContext, 2));
            rv.setAdapter(insuranceLabelAdapter);
            rv.setNestedScrollingEnabled(false);
            rv.setFocusable(false);
            //rv.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            //    @Override
            //    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            //        JourneyShare journeyShare = item.travelSharings.get(
            //                position);
            //        ActivityUtils.launchActivity(mContext,
            //                ShareWebActivity.class,
            //                ShareWebActivity.buildBundle(false,
            //                        UserManager.getInstance()
            //                                   .getUserInfo().mobile.equals(
            //                                journeyShare.username)
            //                        ? true
            //                        : false, journeyShare));
            //    }
            //});
            insuranceLabelAdapter.setNewData(list);
        }
    }
}
