package com.tempus.portal.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tempus.portal.R;
import com.tempus.portal.app.App;
import com.tempus.portal.app.AppContext;
import com.tempus.portal.app.AppStatusConstant;
import com.tempus.portal.app.AppStatusManager;
import com.tempus.portal.app.Constant;
import com.tempus.portal.base.utils.AppUtils;
import com.tempus.portal.dao.CObserver;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.model.GlobalParams;
import com.tempus.portal.view.widget.AdCountView;
import com.umeng.analytics.MobclickAgent;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * 注意，我们这里让WelcomeActivity继承Activity不要继承AppCompatActivity，因为AppCompatActivity会默认去加载主题，造成卡顿
 */
public class StartPageActivity extends Activity
        implements AdCountView.OnStatusChangeListener {
    @BindView(R.id.iv_entry) ImageView mIVEntry;
    @BindView(R.id.tvCountdown) AdCountView mTvCountdown;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        ButterKnife.bind(this);
        AppContext.STATUS_HEIGHT = AppUtils.getStatusBarHeight(this);
        AppStatusManager.getInstance()
                        .setAppStatus(
                                AppStatusConstant.STATUS_NORMAL);//进入应用初始化设置成未登录状态
        mTvCountdown.setBackgroundColor(
                getResources().getColor(R.color.transparent));
        mTvCountdown.setOnStatusChangeListener(this);
        mTvCountdown.setOutRingColor(
                getResources().getColor(R.color.colorAccent));
        mTvCountdown.setTextColor(
                getResources().getColor(R.color.textTitleColor));
        mTvCountdown.setText("跳过");
        mTvCountdown.setTextSize(12);
        mTvCountdown.setTime(3000);
        mTvCountdown.start();
        mTvCountdown.setOnClickListener(v -> detectPermission());
    }


    @Override protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //Glide.with(this).resumeRequests();
    }


    @Override protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        // Glide.with(this).pauseRequests();
    }


    private void startPage() {
        float nowVersionCode = AppUtils.getAppVersionCode(
                StartPageActivity.this);
        float spVersionCode = RxSharedPreferences.create(
                getDefaultSharedPreferences(this))
                                                 .getFloat(
                                                         Constant.KEY_VERSION_CODE)
                                                 .get();
        if (nowVersionCode > spVersionCode) {
            RxSharedPreferences.create(getDefaultSharedPreferences(this))
                               .getFloat(Constant.KEY_VERSION_CODE)
                               .set(nowVersionCode);
            startActivity(new Intent(StartPageActivity.this,
                    WelcomeGuideActivity.class));
            //首次启动
        }
        else {
            startActivity(
                    new Intent(StartPageActivity.this, MainActivity.class));
            //非首次启动
        }
        finish();
    }


    private void detectPermission() {
        new RxPermissions(this).request(Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_SMS)
                      .subscribe(granted -> {
                          if (granted) {// 在android 6.0之前会默认返回true 已经获取权限
                              runOnUiThread(() -> {
                                  AppContext.initAppParams(
                                          StartPageActivity.this);
                                  DataManager.getGlobalParameters()
                                             .subscribe(
                                                     new CObserver<GlobalParams>() {
                                                         @Override
                                                         public void onPrepare() {

                                                         }


                                                         @Override
                                                         public void onError(ErrorThrowable throwable) {
                                                             //ToastUtils.showLongToast(throwable.getMessage());
                                                             startPage();
                                                         }


                                                         @Override
                                                         public void onSuccess(GlobalParams globalParams) {
                                                             App.getACache().put(Constant.KEY_GLOBALPARAMS, globalParams);
                                                             //if (!TextUtils.isEmpty(
                                                             //        globalParams.appStartUpImage)) {
                                                             //    GlideUtils.display(
                                                             //            mIVEntry,
                                                             //            globalParams.appStartUpImage);
                                                             //}
                                                             startPage();
                                                         }
                                                     });
                              });
                          }
                          else {
                              // 未获取权限
                              finish();
                          }
                      });
    }


    //只显示一次启动页（ App 没被 kill 的情况下）
    @Override public void onBackPressed() {
        // super.onBackPressed(); 	不要调用父类的方法
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }


    @Override public void onCountViewStart() {
    }


    @Override public void onCountViewStop() {
        detectPermission();
    }
}
