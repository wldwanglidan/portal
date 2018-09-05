package com.tempus.portal.ui.personalcenter;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.TextCheckUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.manager.UserManager;
import com.tempus.portal.model.User;
import com.tempus.portal.model.event.LoginEvent;
import com.tempus.portal.view.widget.ClearEditText;
import org.greenrobot.eventbus.EventBus;

public class EditNickNameActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tvRight) TextView mTvRight;
    @BindView(R.id.edtNickName) ClearEditText mCetNickName;

    private String mNickname;
    private User mUser;


    public static Bundle buildBundle(String nickname) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("nickname", nickname);
        return bundle;
    }


    @Override protected void getBundleExtras(Bundle extras) {
        mNickname = (String) extras.getSerializable("nickname");
    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_edit_nick_name;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {
        mUser = UserManager.getInstance().getUserInfo();
        mCetNickName.setText(mNickname);
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("修改昵称");
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
                saveNickName();
                break;
        }
    }


    private void editSuccess() {
        mUser.nickname = mCetNickName.getText().toString();
        UserManager.getInstance().setUserInfo(mUser);
        EventBus.getDefault().post(new LoginEvent());
        ToastUtils.showLongToast("编辑成功");
        setResult(RESULT_OK);
        finish();
    }


    private void saveNickName() {

        String nickName = mCetNickName.getText().toString().trim();
        if (TextUtils.isEmpty(nickName)) {
            ToastUtils.showLongToast(R.string.notNickName);
            return;
        }
        if (nickName.equals(mNickname)) {
            ToastUtils.showLongToast(R.string.notChangeNickName);
            return;
        }
        if (!TextCheckUtils.checkUsername(nickName,6,12)) {
            ToastUtils.showLongToast(R.string.notRightNickName);
            return;
        }


        //编辑联系人
        DataManager.editNickname(nickName).compose(bindToLifecycle()).
                subscribe(getSubscribe(R.string.editIng, s -> editSuccess()));

    }
}
