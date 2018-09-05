package com.tempus.portal.ui.about;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import com.tempus.portal.R;
import com.tempus.portal.app.App;
import com.tempus.portal.app.Constant;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.ActivityUtils;
import com.tempus.portal.model.GlobalParams;
import com.tempus.portal.ui.WebActivity;

/**
 * Created by Administrator on 2017/9/5.
 */

public class AgreementStatementActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    private GlobalParams mGlobalParams;


    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_agreement_statement;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {

        mGlobalParams = (GlobalParams) App.getACache()
                                          .getAsObject(
                                                  Constant.KEY_GLOBALPARAMS);
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("协议声明");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @OnClick({ R.id.tvUserAgreement, R.id.tvPrivacyAgreement,
                     R.id.tvServiceAgreement })
    public void onViewClicked(View view) {
        if (mGlobalParams == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.tvUserAgreement:
                ActivityUtils.launchActivity(mContext, WebActivity.class,
                        WebActivity.buildBundle(mGlobalParams.userAgreementUrl,
                                ""));
                break;
            case R.id.tvPrivacyAgreement:
                ActivityUtils.launchActivity(mContext, WebActivity.class,
                        WebActivity.buildBundle(
                                mGlobalParams.privacyAgreementUrl, ""));
                break;
            case R.id.tvServiceAgreement:
                ActivityUtils.launchActivity(mContext, WebActivity.class,
                        WebActivity.buildBundle(
                                mGlobalParams.serviceAgreementUrl, ""));
                break;
        }
    }
}
