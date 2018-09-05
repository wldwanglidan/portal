package com.tempus.portal.ui.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.tempus.portal.R;
import com.tempus.portal.app.AppContext;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.ActivityUtils;
import com.tempus.portal.base.utils.TextCheckUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.base.utils.glide.GlideUtils;
import com.tempus.portal.dao.CObserver;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.manager.UserManager;
import com.tempus.portal.model.Base;
import com.tempus.portal.model.User;
import com.tempus.portal.model.event.LoginEvent;
import com.tempus.portal.ui.user.VipLevelActivity;
import org.greenrobot.eventbus.EventBus;

public class EditPersonalActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tvRight) TextView mTvRight;

    @BindView(R.id.tvUserPhone) TextView mTvUserPhone;
    @BindView(R.id.ivUserHead) ImageView mIvUserHead;
    @BindView(R.id.tvNickName) TextView mTvNickName;

    private String userPhone;
    private String nickName;
    private User mUser;
    //头像更新
    private static final int REQUEST_CODE_AVATAR = 103;
    //昵称更新
    private static final int REQUEST_CODE_NIVKNAME = 104;


    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_edit_personal;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {
        mUser = UserManager.getInstance().getUserInfo();
        setUserView();
    }


    private void setUserView() {
        if (UserManager.getInstance().isLogin()) {
            //设置头像
            GlideUtils.displayCircleHeader(mIvUserHead, mUser.headUrl);
            userPhone = mUser.mobile;
            mTvUserPhone.setText(TextCheckUtils.getEncryptNum(userPhone));
            nickName = mUser.nickname;
            if (nickName.equals("")) {
                Log.d("result", "-----nickName==null");
                mTvNickName.setText(TextCheckUtils.getEncryptNum(userPhone));
            }
            else {
                Log.d("result", nickName);
                mTvNickName.setText(nickName);
            }
        }
        else {
            nickName = "";
            GlideUtils.displayNative(mIvUserHead, R.drawable.default_head);
            mTvUserPhone.setText("");
            mTvNickName.setText(nickName);
        }
    }


    private void updateAvatar() {
        if (UserManager.getInstance().isLogin()) {
            //设置头像
            GlideUtils.displayCircleHeader(mIvUserHead, mUser.headUrl);
        }
        else {
            GlideUtils.displayNative(mIvUserHead, R.drawable.default_head);
        }
    }


    private void updateNickName() {
        mTvNickName.setText(mUser.nickname);
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("个人中心");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @OnClick({ R.id.pllNickName, R.id.pllUserLevel, R.id.pllAvatar,
                     R.id.btnOutLogin }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pllNickName:
                ActivityUtils.launchActivity(mContext,
                        EditNickNameActivity.class,
                        EditNickNameActivity.buildBundle(nickName),
                        REQUEST_CODE_NIVKNAME);
                break;
            case R.id.pllUserLevel:
                startActivity(new Intent(this, VipLevelActivity.class));
                break;
            case R.id.pllAvatar:
                //startActivity(new Intent(this, EditAvatarActivity.class));
                ActivityUtils.launchActivity(mContext, EditAvatarActivity.class,
                        null, REQUEST_CODE_AVATAR);
                break;
            case R.id.btnOutLogin:
                if (!AppContext.isNetworkAvailable()) {
                    ToastUtils.showLongToast(
                            AppContext.getString(R.string.no_network));
                }
                else {
                    DataManager.getInstance().loginOut().
                            subscribe(new CObserver<Base>() {
                                @Override public void onPrepare() {
                                }


                                @Override
                                public void onError(ErrorThrowable throwable) {

                                }


                                @Override public void onSuccess(Base user) {
                                    UserManager.getInstance().loginOut();
                                    EventBus.getDefault()
                                            .post(new LoginEvent());
                                    finish();
                                }
                            });
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CODE_AVATAR:
                //当头像编辑页面返回的时候，页面更新头像，必须有
                updateAvatar();
                break;
            case REQUEST_CODE_NIVKNAME:
                updateNickName();
                nickName = mUser.nickname;
                break;
        }
    }
}
