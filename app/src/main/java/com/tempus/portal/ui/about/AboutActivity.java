package com.tempus.portal.ui.about;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import com.tempus.portal.R;
import com.tempus.portal.app.AppContext;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.ActivityUtils;
import com.tempus.portal.base.utils.DownloadUtil;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.manager.DataManager;

/**
 * Created by Administrator on 2017/9/4.
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.ivUserHead) ImageView mIvUserHead;


    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_about;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("关于我们");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @OnClick({ R.id.tvIntroduction, R.id.tvFeedBack, R.id.tvCheckUpdates,
                     R.id.tvSystemSettings, R.id.tvAgreementStatement })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvIntroduction:
                ActivityUtils.launchActivity(mContext,
                        IntroductionActivity.class);
                break;
            case R.id.tvFeedBack:
                ActivityUtils.launchActivity(mContext, FeedBackActivity.class);
                break;
            case R.id.tvCheckUpdates:
                checkForUpdates();
                break;
            case R.id.tvSystemSettings:
                ActivityUtils.launchActivity(mContext, SystemSetActivity.class);
                break;
            case R.id.tvAgreementStatement:
                ActivityUtils.launchActivity(mContext,
                        AgreementStatementActivity.class);
                break;
        }
    }


    private void checkForUpdates() {
        DataManager.checkForUpdates(AppContext.APP_VERSION_CODE).
                subscribe(getNotProSubscribe(v -> {
                    if (v.verCode >
                            Integer.valueOf(AppContext.APP_VERSION_CODE)) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                mContext).setTitle("是否升级至最新版本？")
                                         .setMessage(v.downloadUrl)
                                         .setNegativeButton(android.R.string.cancel,
                                                 (dialogInterface, i) -> {
                                                     dialogInterface.dismiss();
                                                 })
                                         .setPositiveButton("确定",
                                                 (dialog, which) -> {
                                                     mProgressDialogUtils.showProgress(
                                                             "下载更新中");
                                                     DownloadUtil.startDownload(
                                                             AppContext.getContext(),
                                                             v.downloadUrl);
                                                 });
                        alert.setCancelable(false);
                        alert.create();
                        alert.show();
                    }
                    else {
                        ToastUtils.showLongToast("当前版本已是最新版本！");
                    }
                }));
    }
}

