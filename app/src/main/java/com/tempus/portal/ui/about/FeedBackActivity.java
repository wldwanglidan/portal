package com.tempus.portal.ui.about;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.KeyboardUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.manager.DataManager;

/**
 * Created by Administrator on 2017/9/6.
 */

public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tvRight) TextView mTvRight;
    @BindView(R.id.etContent) EditText mEtContent;
    @BindView(R.id.tvFontsNumber) TextView mTvFontsNumber;


    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_feed_back;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {
        //监听输入字数
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTvFontsNumber.setText(s.length() + "/300");
            }


            @Override public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("意见反馈");
        mTvRight.setText("提交");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @OnClick({ R.id.tvRight }) public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvRight:
                submitFeedBack();
                break;
        }
    }


    public void submitFeedBack() {
        String content = mEtContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showLongToast("还没填反馈呢～");
            return;
        }
        DataManager.submitFeedback(content).compose(bindToLifecycle()).
                subscribe(getSubscribe(R.string.submitFeedBackIng, s -> {
                    ToastUtils.showLongToast(R.string.submitFeedBackSuccess);
                    mEtContent.setText("");
                    KeyboardUtils.hideSoftInput(this);
                    finish();
                }));
    }
}
