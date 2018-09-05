package com.tempus.portal.ui.user;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.glide.GlideUtils;
import com.tempus.portal.manager.UserManager;

/**
 * Created by Administrator on 2017/9/5.
 */

public class VipLevelActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;


    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_vip_level;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {
        GlideUtils.displayCircleHeader(
                ButterKnife.findById(this, R.id.ivUserHead),
                UserManager.getInstance().getUserInfo().headUrl);
        ((TextView) ButterKnife.findById(this, R.id.tvVipInfo)).setText(
                "腾邦生活会员"+UserManager.getInstance().getUserInfo().vipLevel);
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("会员等级");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }
}
