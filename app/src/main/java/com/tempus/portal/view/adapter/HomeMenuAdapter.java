package com.tempus.portal.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tempus.portal.R;
import com.tempus.portal.base.utils.glide.GlideUtils;
import com.tempus.portal.model.HomeMenu;

/**
 * Created by Administrator on 2017/6/29.
 */

public class HomeMenuAdapter
        extends BaseQuickAdapter<HomeMenu, BaseViewHolder> {

    public HomeMenuAdapter() {
        super(R.layout.item_home_menu, null);
    }


    @Override protected void convert(BaseViewHolder helper, HomeMenu homeMenu) {
        //helper.setText(R.id.tvTime,announcement.date).setText(R.id.tvContent,
        //        announcement.notice);
        GlideUtils.displayUrl(helper.getView(R.id.ivMenu), homeMenu.merchIcon,
                R.drawable.bg_iv_default_tour);
    }
}
