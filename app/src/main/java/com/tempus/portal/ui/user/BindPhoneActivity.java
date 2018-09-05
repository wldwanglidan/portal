package com.tempus.portal.ui.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.jaeger.library.StatusBarUtil;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.KeyboardUtils;
import com.tempus.portal.base.utils.LoginTimeCount;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.dao.CObserver;
import com.tempus.portal.dao.SmsObserver;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.manager.UserManager;
import com.tempus.portal.model.Base;
import com.tempus.portal.model.User;
import com.tempus.portal.model.event.LoginEvent;
import com.tempus.portal.social.SocialConfig;
import com.tempus.portal.view.widget.ClearEditText;
import org.greenrobot.eventbus.EventBus;

public class BindPhoneActivity extends BaseActivity {
    //@BindView(R.id.ivLoginBg) ImageView mIvLoginBg;
    @BindView(R.id.etPhone) ClearEditText mEtPhone;
    @BindView(R.id.etVerificationCode) ClearEditText mEtVerificationCode;
    @BindView(R.id.btnSendCode) Button mBtnSendCode;
    @BindView(R.id.btnLogin) Button mBtnLogin;
    @BindView(R.id.tvTitle) TextView mTvTitle;

    private SmsObserver mSmsObserver;
    public LoginTimeCount mLoginTimeCount;

    private String mUnionId;


    public static Bundle buildBundle(String unionId) {
        Bundle bundle = new Bundle();
        bundle.putString("unionId", unionId);
        return bundle;
    }


    @Override protected void getBundleExtras(Bundle extras) {
        mUnionId = extras.getString("unionId");
    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageViewInFragment(
                BindPhoneActivity.this, 0, null);
        // StatusBarUtil.setTransparent(LoginActivity.this);
        //  GlideUtils.displayNative(mIvLoginBg, R.drawable.bg_login);
        //StatusBarUtil.setTranslucentForImageViewInFragment(
        //        LoginActivity.this, 0, null);
        ButterKnife.findById(this, R.id.llBottom).setVisibility(View.GONE);
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
        mBtnLogin.setText("立即绑定");
    }


    @Override protected void initToolbar() {
        mTvTitle.setText("绑定手机号");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return true;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.LEFT;
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


    @OnClick({ R.id.ibtClose, R.id.btnSendCode, R.id.btnLogin })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtClose:
                finish();
                break;
            case R.id.btnSendCode:
                sendSms();
                break;
            case R.id.btnLogin:
                bindPhone();
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
                           KeyboardUtils.hideSoftInput(BindPhoneActivity.this);
                       }
                   });
    }


    private void bindPhone() {
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

        UserManager.getInstance().bindPhone(mUnionId, phone, verificationCode).
                subscribe(new CObserver<User>() {
                    @Override public void onPrepare() {
                        mProgressDialogUtils.showProgress(
                                R.string.bindPhoneIng);
                    }


                    @Override public void onError(ErrorThrowable throwable) {
                        loginFailure(throwable.msg);
                    }


                    @Override public void onSuccess(User user) {
                        loginSuccess(user);
                    }
                });
    }


    private void loginSuccess(User user) {
        //setResult(RESULT_OK, new Intent().putExtra("user", user));
        mProgressDialogUtils.hideProgress();
        ToastUtils.showLongToast(R.string.bindPhoneSuccess);
        EventBus.getDefault().post(new LoginEvent());
        finish();
    }


    private void loginFailure(String msg) {
        //setResult(RESULT_OK,
        //        new Intent().putExtra("user", new Base(-1, msg, "")));
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
