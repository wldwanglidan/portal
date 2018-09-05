package com.tempus.portal.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tempus.portal.R;

/**
 * Created by Administrator on 2017/9/4.
 */

public class InsuranceLabelAdapter
        extends BaseQuickAdapter<String, BaseViewHolder> {

    public InsuranceLabelAdapter() {
        super(R.layout.item_insurance_label, null);
    }


    @Override
    protected void convert(BaseViewHolder helper, String str) {
        helper.setText(R.id.tvLabel,
                str);

    }
}
