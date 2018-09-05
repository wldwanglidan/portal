package com.tempus.portal.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tempus.portal.R;
import com.tempus.portal.model.HomeNavigation;
import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */

public class HomeNavigationAdapter
        extends BaseQuickAdapter<HomeNavigation, BaseViewHolder> {
    public int position;


    public HomeNavigationAdapter(List data) {
        super(R.layout.item_home_navigation, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, HomeNavigation homeNavigation) {
        helper.setText(R.id.tv, homeNavigation.name)
              .setImageResource(R.id.iv, homeNavigation.image)
              .setVisible(R.id.ivUnread, homeNavigation.isUnread);
    }
}
