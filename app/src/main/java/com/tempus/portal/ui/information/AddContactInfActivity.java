package com.tempus.portal.ui.information;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.TextCheckUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.model.information.ContactInformation;
import com.tempus.portal.view.widget.ClearEditText;

/**
 * Created by Administrator on 2016/12/14.
 */

public class AddContactInfActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tvRight) TextView mTvRight;
    @BindView(R.id.cetName) ClearEditText mCetName;
    @BindView(R.id.cetMobile) ClearEditText mCetMobile;
    @BindView(R.id.cetEmail) ClearEditText mCetEmail;

    @BindView(R.id.cbDefault) CheckBox mCbContactDefault;
    private ContactInformation mContact;


    public static Bundle buildBundle(ContactInformation contact) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("contact", contact);
        return bundle;
    }


    @Override protected void getBundleExtras(Bundle extras) {
        mContact = (ContactInformation) extras.getSerializable("contact");
    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_add_contact;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {
        if (mContact != null) {
            mCetEmail.setText(mContact.email);
            mCetMobile.setText(mContact.mobile);
            mCetName.setText(mContact.name);

            if (mContact.selfFlag.equals("1")) {
                mCbContactDefault.setChecked(true);
            }
        }
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(
                mContact == null ? "新增联系人信息" : "编辑联系人信息");
        mTvRight.setText("保存");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @OnClick({ R.id.tvRight }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRight:
                saveOrChange();
                break;
        }
    }


    private void saveOrChange() {

        String contactName = mCetName.getText().toString().trim();
        String contactMobile = mCetMobile.getText().toString().trim();
        String contactEmail = mCetEmail.getText().toString().trim();
        String contactSelfFlag = "0";
        if (mCbContactDefault.isChecked()) {
            contactSelfFlag = "1";
        }
        if (TextUtils.isEmpty(contactName)) {
            ToastUtils.showLongToast(R.string.notChinaName);
            return;
        }
        if (!TextUtils.isEmpty(contactName) && !TextCheckUtils.checkNameChese(contactName)) {
            ToastUtils.showLongToast(R.string.notRightChinaName);
            return;
        }
        if (TextUtils.isEmpty(contactMobile)) {
            ToastUtils.showLongToast(R.string.notMobile);
            return;
        }
        if (!TextUtils.isEmpty(contactMobile) &&
                !TextCheckUtils.isMobileNO(contactMobile)) {
            ToastUtils.showLongToast(R.string.notRightMobile);
            return;
        }
        if (TextUtils.isEmpty(contactEmail)) {
            ToastUtils.showLongToast(R.string.notEmail);
            return;
        }
        if (!TextUtils.isEmpty(contactEmail) &&
                !TextCheckUtils.isEmail(contactEmail)) {
            ToastUtils.showLongToast(R.string.notRightEmail);
            return;
        }
        //新增联系人
        if (mContact == null) {
            DataManager.addContact(contactName, contactMobile, contactEmail,
                    contactSelfFlag).compose(bindToLifecycle()).
                               subscribe(getSubscribe(R.string.saveIng, s -> {
                                   //这里应该通知界面刷新，2个activity之间的交互
                                   ToastUtils.showLongToast(
                                           R.string.addSuccess);
                                   setResult(RESULT_OK);
                                   AddContactInfActivity.this.finish();
                               }));
        }
        //编辑联系人
        else {
            DataManager.editContact(contactName, contactMobile, contactEmail,
                    contactSelfFlag, mContact.contactId)
                       .compose(bindToLifecycle())
                       .
                               subscribe(getSubscribe(R.string.saveIng, s -> {
                                   ToastUtils.showLongToast(
                                           R.string.addSuccess);
                                   setResult(RESULT_OK);
                                   AddContactInfActivity.this.finish();
                               }));
        }
    }
}