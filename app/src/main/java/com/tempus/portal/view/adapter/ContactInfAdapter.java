package com.tempus.portal.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tempus.portal.R;
import com.tempus.portal.base.utils.glide.GlideUtils;
import com.tempus.portal.model.Message;
import com.tempus.portal.model.information.ContactInformation;

/**
 * Created by Administrator on 2017/1/3.
 */

public class ContactInfAdapter
        extends BaseQuickAdapter<ContactInformation, BaseViewHolder> {

    public ContactInfAdapter() {
        super(R.layout.item_com_contact, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactInformation item) {
        helper.setText(R.id.tvName, item.name)
              .setText(R.id.tvPhone, item.mobile)
              .setText(R.id.tvEmail, item.email)
              .addOnClickListener(R.id.ibDelete)
              .addOnClickListener(R.id.ibEdit)
        ;
    }

}
