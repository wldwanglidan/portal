package com.tempus.portal.ui.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.ActivityUtils;
import com.tempus.portal.base.utils.KeyboardUtils;
import com.tempus.portal.base.utils.LoginTimeCount;
import com.tempus.portal.base.utils.NoDoubleClickUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.dao.CObserver;
import com.tempus.portal.dao.SmsObserver;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.manager.UserManager;
import com.tempus.portal.model.Base;
import com.tempus.portal.model.QqUserInfo;
import com.tempus.portal.model.User;
import com.tempus.portal.model.WxUserInfo;
import com.tempus.portal.model.event.LoginEvent;
import com.tempus.portal.model.event.RefreshWebEvent;
import com.tempus.portal.social.SocialConfig;
import com.tempus.portal.social.UMSocial;
import com.tempus.portal.view.widget.ClearEditText;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;

import static com.tempus.portal.ui.WebActivity.REQUEST_CODE_LOGIN;

public class LoginActivity extends BaseActivity {
    // @BindView(R.id.ivLoginBg) ImageView mIvLoginBg;
    @BindView(R.id.etPhone) ClearEditText mEtPhone;
    @BindView(R.id.etVerificationCode) ClearEditText mEtVerificationCode;
    @BindView(R.id.btnSendCode) Button mBtnSendCode;
    @BindView(R.id.btnLogin) Button mBtnLogin;
    private SmsObserver mSmsObserver;
    public LoginTimeCount mLoginTimeCount;
    private boolean mIsLoginSuccess = false;


    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageViewInFragment(LoginActivity.this,
                0, null);
        // GlideUtils.displayNative(mIvLoginBg, R.drawable.bg_login);
        mLoginTimeCount = new LoginTimeCount(60000, 1000, mBtnSendCode,
                mContext);//构造CountDownTimer对象
        mSmsObserver = new SmsObserver(this, new Handler(), smsContent -> {
            mEtVerificationCode.setText(smsContent);
        });
        this.getContentResolver()
            .registerContentObserver(Uri.parse("content://sms/"), true,
                    mSmsObserver);
        mEtPhone.addTextChangedListener(new EditChangedListener());
        mEtVerificationCode.addTextChangedListener(new EditChangedListener());
    }


    @Override protected void initToolbar() {

    }


    @Override protected boolean toggleOverridePendingTransition() {
        return true;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.BOTTOM;
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        this.getContentResolver().unregisterContentObserver(mSmsObserver);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialConfig.getShareControl()
                    .onActivityResult(this, requestCode, resultCode, data);
    }


    @OnClick({ R.id.ibtClose, R.id.btnSendCode, R.id.btnLogin, R.id.ibtWechat,
                     R.id.ibtQq }) public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtClose:
                finish();
                break;
            case R.id.btnSendCode:
                sendSms();
                break;
            case R.id.btnLogin:
                login();
                break;
            case R.id.ibtQq:
                if (NoDoubleClickUtils.isDoubleClick()) {
                    return;
                }
                SocialConfig.getShareControl()
                            .login(LoginActivity.this, UMSocial.LOGIN_QQ,
                                    new UMSocial.OnAutoLoginCallback() {
                                        @Override
                                        public void onSuccess(int type, Map<String, String> map) {
                                            Log.d("result",
                                                    "QQ" + map.toString());
                                            QqUserInfo qqUserInfo
                                                    = new Gson().fromJson(
                                                    new Gson().toJson(map),
                                                    QqUserInfo.class);
                                            UserManager.getInstance()
                                                       .qqLogin(qqUserInfo)
                                                       .
                                                               subscribe(
                                                                       getNotProSubscribe(
                                                                               s -> {
                                                                                   if (s.isBindPhone()) {
                                                                                       loginSuccess();
                                                                                   }
                                                                                   else {
                                                                                       finish();
                                                                                       ActivityUtils
                                                                                               .launchActivity(
                                                                                                       mContext,
                                                                                                       BindPhoneActivity.class,
                                                                                                       BindPhoneActivity
                                                                                                               .buildBundle(
                                                                                                                       qqUserInfo.openid));
                                                                                   }
                                                                               }));
                                        }


                                        @Override
                                        public void onFailed(Throwable t) {
                                            Log.e("result",
                                                    "QQ错误" + t.getMessage());
                                            ToastUtils.showLongToast(
                                                    t.getMessage());
                                        }


                                        @Override public void onCanceled() {

                                        }
                                    });

                break;
            case R.id.ibtWechat:
                if (NoDoubleClickUtils.isDoubleClick()) {
                    return;
                }
                SocialConfig.getShareControl()
                            .login(LoginActivity.this, UMSocial.LOGIN_WECHAT,
                                    new UMSocial.OnAutoLoginCallback() {
                                        @Override
                                        public void onSuccess(int type, Map<String, String> map) {
                                            Log.e("result",
                                                    "WX" + map.toString());
                                            WxUserInfo wxUserInfo
                                                    = new Gson().fromJson(
                                                    new Gson().toJson(map),
                                                    WxUserInfo.class);
                                            UserManager.getInstance()
                                                       .wxLogin(wxUserInfo)
                                                       .
                                                               subscribe(
                                                                       getNotProSubscribe(
                                                                               s -> {
                                                                                   if (s.isBindPhone()) {
                                                                                       loginSuccess();
                                                                                   }
                                                                                   else {
                                                                                       finish();
                                                                                       ActivityUtils
                                                                                               .launchActivity(
                                                                                                       mContext,
                                                                                                       BindPhoneActivity.class,
                                                                                                       BindPhoneActivity
                                                                                                               .buildBundle(
                                                                                                                       wxUserInfo.unionid),
                                                                                                       REQUEST_CODE_LOGIN);
                                                                                   }
                                                                               }));
                                        }


                                        @Override
                                        public void onFailed(Throwable t) {
                                            ToastUtils.showLongToast(
                                                    t.getMessage());
                                        }


                                        @Override public void onCanceled() {

                                        }
                                    });

                break;
        }
    }


    private void sendSms() {
        String phone = mEtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || phone.length() < 11) {
            ToastUtils.showLongToast(R.string.phone_cannot_be_empty);
            return;
        }
        DataManager.sendSms(phone, "01")
                   .compose(bindToLifecycle())
                   .subscribe(new CObserver<Base>() {
                       @Override public void onPrepare() {
                           mProgressDialogUtils.showProgress("发送验证码中...");
                       }


                       @Override public void onError(ErrorThrowable throwable) {
                           mProgressDialogUtils.hideProgress();
                           ToastUtils.showLongToast(throwable.msg + "");
                       }


                       @Override public void onSuccess(Base base) {
                           //ToastUtils.showLongToast(
                           //        baseResponse.+ "");
                           mProgressDialogUtils.hideProgress();
                           mLoginTimeCount.start();
                           KeyboardUtils.hideSoftInput(LoginActivity.this);
                       }
                   });
    }


    private void login() {
        String phone = mEtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || phone.length() < 11) {
            ToastUtils.showLongToast(R.string.phone_cannot_be_empty);
            return;
        }
        String verificationCode = mEtVerificationCode.getText()
                                                     .toString()
                                                     .trim();
        if (TextUtils.isEmpty(verificationCode)) {
            ToastUtils.showLongToast(
                    R.string.verification_code_cannot_be_empty);
            return;
        }

        UserManager.getInstance().login(phone, verificationCode).
                subscribe(new CObserver<User>() {
                    @Override public void onPrepare() {
                        mProgressDialogUtils.showProgress(R.string.loginIng);
                    }


                    @Override public void onError(ErrorThrowable throwable) {
                        loginFailure(throwable.msg);
                    }


                    @Override public void onSuccess(User user) {
                        Log.d("result",
                                "user:" + new Gson().toJson(user).toString());
                        setResult(RESULT_OK,
                                new Intent().putExtra("user", user));
                        loginSuccess();
                    }
                });
    }


    private void loginSuccess() {
        mIsLoginSuccess = true;
        mProgressDialogUtils.hideProgress();
        ToastUtils.showLongToast(R.string.loginSuccess);
        Log.d("result", "前onRefreshWebEvent: ");
        EventBus.getDefault().post(new LoginEvent());
        EventBus.getDefault().post(new RefreshWebEvent("loginRefresh"));
        Log.d("result", "后onRefreshWebEvent: ");
        finish();
    }


    @Override public void finish() {
        //setResult(RESULT_OK,
        //        new Intent().putExtra("isLoginSuccess", mIsLoginSuccess));
        super.finish();
    }


    private void loginFailure(String msg) {
        setResult(RESULT_OK,
                new Intent().putExtra("user", new Base(-1, msg, "")));
        mProgressDialogUtils.hideProgress();
        ToastUtils.showLongToast(msg);
    }


    private class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }


        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }


        @Override public void afterTextChanged(Editable editable) {
            if (mEtVerificationCode.length() == 4 && mEtPhone.length() == 11) {
                mBtnLogin.setEnabled(true);
                mBtnLogin.setTextColor(getResources().getColor(R.color.White));
            }
            else {
                mBtnLogin.setTextColor(
                        getResources().getColor(R.color.textGrayColor));
                mBtnLogin.setEnabled(false);
            }
        }
    }
}
