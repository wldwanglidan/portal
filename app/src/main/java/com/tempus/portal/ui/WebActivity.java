package com.tempus.portal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.AppUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.dao.CObserver;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.manager.UserManager;
import com.tempus.portal.model.Base;
import com.tempus.portal.model.BaseHead;
import com.tempus.portal.model.event.ListenUrl;
import com.tempus.portal.model.event.LoginEvent;
import com.tempus.portal.model.event.RefreshWebEvent;
import com.tempus.portal.view.widget.BrowserLayout;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.tempus.portal.R.id.toolbar;

/**
 * Created by Administrator on 2016/12/27.
 */

public class WebActivity extends BaseActivity {
    @BindView(toolbar) Toolbar mToolbar;
    public @BindView(R.id.bl) BrowserLayout mBrowserLayout;
    public static final int REQUEST_CODE_PAY = 0x10;
    public static final int REQUEST_CODE_LOGIN = 0x11;
    private CookieManager cookieManager;
    private String mWebUrl, mMerchId = null;

    public static Bundle buildBundle(String url, String merchId) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("merchId", merchId);
        return bundle;
    }

    //public static Bundle buildBundle(HomeMenu homeMenu) {
    //    Bundle bundle = new Bundle();
    //    bundle.putSerializable("homeMenu", homeMenu);
    //    return bundle;
    //}


    @Override protected void getBundleExtras(Bundle extras) {
        mWebUrl = extras.getString("url");
        mMerchId = extras.getString("merchId");
    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_web;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {
        getData();

    }


    @Override protected void initToolbar() {
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            //if (isEdit) {
            //    getSupportActionBar().setHomeButtonEnabled(true);
            //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //}
            //else {
            //    mTvRight.setText("完成");
            //}
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("");
            mToolbar.setNavigationOnClickListener(view -> {
                if (mBrowserLayout.getWebView().canGoBack()) {
                    mBrowserLayout.getWebView()
                                  .goBack(); // goBack()表示返回WebView的上一页面
                }
                else {
                    finish();
                }
            });
        }
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!TextUtils.isEmpty(mMerchId)) {
            //当mMerchId不为空的的时候，显示R.menu.menu_web，可以实现分享和收藏功能
            getMenuInflater().inflate(R.menu.menu_web, menu);
        }
        return true;
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_pop) {
            //弹出分享和收藏界面
            AppUtils.getPopupWindow(ButterKnife.findById(this, R.id.action_pop),
                    mContext, mMerchId, mBrowserLayout.getWebView().getTitle(),
                    mWebUrl);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    //@OnClick({ R.id.tvRight }) public void onClick(View view) {
    //    switch (view.getId()) {
    //        case R.id.tvRight:
    //            finish();
    //            break;
    //
    //    }
    //}


    private void getData() {
        if (!TextUtils.isEmpty(mWebUrl)) {
            runOnUiThread(() -> mBrowserLayout.loadUrl(mWebUrl));
        }
        else {
            ToastUtils.showLongToast("获取URL地址失败");
        }
        syncCookie(this, mWebUrl);

    }


    //@Override public void onStart() {
    //    super.onStart();
    //    EventBus.getDefault().register(this);
    //}
    //
    //
    //@Override protected void onStop() {
    //    EventBus.getDefault().unregister(this);
    //    super.onStop();
    //}

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    @Override protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onListenUrl(ListenUrl listenUrl) {
        if (listenUrl.url.equals(
                "http://m.haidaowang.com/index.html#back_flag")) {
            setTitle("腾邦物流旗下跨境电商-海捣网");
        }
        else {
            Observable.timer(1, TimeUnit.SECONDS)
                      .compose(bindToLifecycle())
                      .subscribe(new CObserver<Long>() {
                          @Override public void onPrepare() {

                          }


                          @Override
                          public void onError(ErrorThrowable throwable) {

                          }


                          @Override public void onSuccess(Long aLong) {
                              runOnUiThread(() -> setTitle(
                                      mBrowserLayout.getWebView().getTitle()));
                          }
                      });
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent resultEvent) {
        //Log.d("result", "onLoginEvent: onLoginEvent");
        runOnUiThread(() -> mBrowserLayout.getWebView()
                                          .loadUrl("javascript: " +
                                                  "loginFinish( '" +
                                                  new Gson().toJson(
                                                          UserManager.getInstance()
                                                                     .getUserInfo())
                                                            .toString() +
                                                  "')"));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshWebEvent(RefreshWebEvent resultEvent) {
        //Log.d("result", "onRefreshWebEvent: RefreshWebEvent");
        refresh();

    }


    public void refresh() {
        runOnUiThread(() -> mBrowserLayout.getWebView().reload());

        syncCookie(this, mWebUrl);
        //if (Build.VERSION.SDK_INT < 21) {
        //    CookieSyncManager.getInstance().sync();
        //}
        //else {
        //    CookieManager.getInstance().flush();
        //}
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) &&
                mBrowserLayout.getWebView().canGoBack()) {
            mBrowserLayout.getWebView().goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (REQUEST_CODE_PAY == requestCode) {
            Base base = (Base) data.getSerializableExtra("rps");
            if (base == null) return;
            runOnUiThread(() -> mBrowserLayout.getWebView()
                                              .loadUrl(
                                                      "javascript: payFinish( '" +
                                                              new Gson().toJson(
                                                                      base)
                                                                        .toString() +
                                                              "')"));
        }
        //if (REQUEST_CODE_LOGIN == requestCode) {
        //    User user = (User) data.getSerializableExtra("user");
        //    Log.d("result", new Gson().toJson(user).toString() + "Uesr数据");
        //    runOnUiThread(() -> mBrowserLayout.getWebView()
        //                                      .loadUrl("javascript: " +
        //                                              "loginFinish( '" +
        //                                              new Gson().toJson(user)
        //                                                        .toString() +
        //                                              "')"));
        //    refresh();
        //}
    }


    private void syncCookie(Context context, String url) {
        try {
            //  Log.d("Nat: webView.syncCookie.url", url);
            CookieSyncManager.createInstance(context);
            cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);

            cookieManager.setCookie(url, "sessionContext=" +
                    new Gson().toJson(new BaseHead()).toString());

            if (Build.VERSION.SDK_INT < 21) {
                CookieSyncManager.getInstance().sync();
            }
            else {
                CookieManager.getInstance().flush();
            }
            String newCookie = cookieManager.getCookie(url);
            if (newCookie != null) {
                Log.d("result", "newCookie" + newCookie);
            }
        } catch (Exception e) {
            Log.e("result", e.toString());
        }
    }
}
