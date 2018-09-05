package com.tempus.portal.ui.about;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;

/**
 * Created by Administrator on 2017/9/18.
 */

public class IntroductionActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;



    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_introduction;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {

    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("我们简介");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }
    //@OnClick({ R.id.tvRight }) public void onViewClicked(View view) {
    //    switch (view.getId()) {
    //        case R.id.tvRight:
    //            submitFeedBack();
    //            break;
    //    }
    //}
}
