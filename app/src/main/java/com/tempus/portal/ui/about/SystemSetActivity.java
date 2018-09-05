package com.tempus.portal.ui.about;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.tempus.portal.R;
import com.tempus.portal.app.Constant;
import com.tempus.portal.app.DirContext;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.CleanUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.dao.cache.DataProvider;
import java.io.File;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by Administrator on 2017/9/6.
 */

public class SystemSetActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.scMessage) SwitchCompat mScMessage;
    @BindView(R.id.scWifiUpdate) SwitchCompat mScWifiUpdate;
    @BindView(R.id.tvCacheSize) TextView mTvCacheSize;


    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_system_set;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {
        mTvCacheSize.setText(DataProvider.getInstance().getDiskSize());
        mScWifiUpdate.setChecked(
                RxSharedPreferences.create(getDefaultSharedPreferences(mContext))
                                   .getBoolean(Constant.KEY_WIFI_UPDATE,false)
                                   .get());
        mScMessage.setChecked(
                RxSharedPreferences.create(getDefaultSharedPreferences(mContext))
                                   .getBoolean(Constant.KEY_MESSAGE,false)
                                   .get());
        mScMessage.setOnCheckedChangeListener(
                (compoundButton, b) -> RxSharedPreferences.create(getDefaultSharedPreferences(mContext))
                                                          .getBoolean
                                                                  (Constant.KEY_MESSAGE)
                                                          .set(b));
        mScWifiUpdate.setOnCheckedChangeListener(
                (compoundButton, b) -> RxSharedPreferences.create(getDefaultSharedPreferences(mContext))
                                                          .getBoolean(Constant.KEY_WIFI_UPDATE)
                                                          .set(b));
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("系统设置");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @OnClick({ R.id.llMessage, R.id.llClearCache, R.id.llWifiUpdate })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llMessage:
                if (mScMessage.isChecked()) {
                    mScMessage.setChecked(false);
                }
                else {
                    mScMessage.setChecked(true);
                }
                break;
            case R.id.llWifiUpdate:
                if (mScWifiUpdate.isChecked()) {
                    mScWifiUpdate.setChecked(false);
                }
                else {
                    mScWifiUpdate.setChecked(true);
                }
                break;
            case R.id.llClearCache:
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        mContext).setMessage("你希望清除缓存吗?")
                                 .setNegativeButton(android.R.string.cancel,
                                         (dialogInterface, i) -> {
                                             dialogInterface.dismiss();
                                         })
                                 .setPositiveButton("确定", (dialog, which) -> {
                                     dialog.dismiss();
                                     File[] files = new File[] {
                                             DirContext.getInstance().getDir(
                                                     DirContext.DirEnum.IMAGE),
                                             DirContext.getInstance().getDir(
                                                     DirContext.DirEnum.LUBANCACHE),
                                             DirContext.getInstance().getDir(
                                                     DirContext.DirEnum.CACHE) };
                                     boolean isSuccess = false;
                                     for (File dir : files) {
                                         isSuccess
                                                 &= CleanUtils.cleanCustomCache(
                                                 dir);
                                     }
                                     ToastUtils.showLongToast("缓存清除成功");
                                     mTvCacheSize.setText("0KB");
                                 });
                alert.setCancelable(false);
                alert.create();
                alert.show();
                break;
        }
    }
}
