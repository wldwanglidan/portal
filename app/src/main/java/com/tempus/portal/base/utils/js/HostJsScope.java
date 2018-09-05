package com.tempus.portal.base.utils.js;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.ActivityUtils;
import com.tempus.portal.base.utils.AppUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.model.OrderInfoRequest;
import com.tempus.portal.ui.WebActivity;
import com.tempus.portal.ui.finance.PayActivity;
import com.tempus.portal.ui.user.LoginActivity;
import com.tencent.smtt.sdk.WebView;

import static com.tempus.portal.ui.WebActivity.REQUEST_CODE_LOGIN;
import static com.tempus.portal.ui.WebActivity.REQUEST_CODE_PAY;

/**
 * Created by Administrator on 2016/7/27.
 */
public class HostJsScope {
    public Activity mActivity;


    public HostJsScope(Activity activity) {
        mActivity = activity;
    }


    /**
     * 跳转支付页面
     */
    public static void pay(WebView webView, String jsonData) {
        if (TextUtils.isEmpty(jsonData)) {
            return;
        }
        Log.d("result", jsonData + "#####");
        ActivityUtils.launchActivity(webView.getContext(), PayActivity.class,
                PayActivity.buildBundle(
                        new Gson().fromJson(jsonData, OrderInfoRequest.class)),
                REQUEST_CODE_PAY);
    }


    /**
     * 跳转登录页面
     */
    public static void login(WebView webView) {
        ActivityUtils.launchActivity(webView.getContext(), LoginActivity.class,
                null, REQUEST_CODE_LOGIN);
    }


    public static void showToast(WebView webView, String msg) {
        ToastUtils.showLongToast(msg);
    }


    public static void finishWeb(WebView webView) {
        ((BaseActivity) webView.getContext()).finish();
    }


    public static void backWeb(WebView webView) {
        ((WebActivity) webView.getContext()).mBrowserLayout.getWebView()
                                                           .goBack();
    }


    public static void callPhone(WebView webView, String phone) {
        AppUtils.callPhone(webView.getContext(), phone);
    }


}
