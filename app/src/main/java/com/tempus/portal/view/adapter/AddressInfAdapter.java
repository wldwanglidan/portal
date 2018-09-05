package com.tempus.portal.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tempus.portal.R;
import com.tempus.portal.model.information.AddressInformation;

/**
 * Created by Administrator on 2017/1/3.
 */

public class AddressInfAdapter
        extends BaseQuickAdapter<AddressInformation, BaseViewHolder> {

    public AddressInfAdapter() {
        super(R.layout.item_com_address, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressInformation item) {
        helper.setText(R.id.tvName, item.receiverName)
              .setText(R.id.tvMobile  , item.receiverMobile)
              .setText(R.id.tvAddress, item.province+item.city+item.area+item.detailAddress)
              .addOnClickListener(R.id.ibDelete)
              .addOnClickListener(R.id.ibEdit);
    }


}
