package com.tempus.portal.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tempus.portal.R;
import com.tempus.portal.base.utils.glide.GlideUtils;
import com.tempus.portal.model.Message;

/**
 * Created by Administrator on 2017/9/5.
 */

public class MyMessageAdapter
        extends BaseQuickAdapter<Message, BaseViewHolder> {

    public MyMessageAdapter() {
        super(R.layout.item_my_message, null);
    }


    @Override protected void convert(BaseViewHolder helper, Message message) {
        helper.setText(R.id.tvTitle, message.newsTitle)
              .setText(R.id.tvMessageType, message.newsType)
              .setText(R.id.tvDate, message.newsDate);
        GlideUtils.displayUrl(helper.getView(R.id.ivMessage), message.newsImg,
                R.drawable.bg_iv_default_banner);
    }
}
