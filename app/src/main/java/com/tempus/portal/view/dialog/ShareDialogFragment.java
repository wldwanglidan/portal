package com.tempus.portal.view.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import butterknife.OnClick;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseDialogFragment;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.social.SocialConfig;
import com.tempus.portal.social.UMSocial;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by Administrator on 2016/12/22.
 */

public class ShareDialogFragment extends BaseDialogFragment {

    private String mShareUrl;
    private String mShareTitle;


    public static ShareDialogFragment newInstance(String shareUrl, String shareTitle) {
        ShareDialogFragment fragment = new ShareDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("shareUrl", shareUrl);
        bundle.putString("shareTitle", shareTitle);
        fragment.setArguments(bundle);
        return fragment;
    }


    private void getExtra() {
        mShareUrl = getArguments().getString("shareUrl");
        mShareTitle = getArguments().getString("shareTitle");
    }


    @Override protected int getContentViewID() {
        return R.layout.dialog_fragment_share;
    }


    @Override protected void initView(View view) {
        getExtra();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    @Override public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            //  Dialog dialog = new Dialog(mContext, R.style.BottomDialog);
            //            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //
            dialog.getWindow()
                  .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(true); // 外部点击取消
            // 设置宽度为屏宽, 靠近屏幕底部。
            final Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.AnimBottom);
            //设置dialog的位置
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.gravity = Gravity.BOTTOM;   //设置在底部
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(attributes);
        }
    }


    @OnClick({ R.id.ibClose, R.id.tvShareFriendsCircle, R.id.tvShareWx,
                     R.id.tvQq, R.id.tvShareQqZone })
    public void onClick(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.ibClose:
                break;
            case R.id.tvQq:
                SocialConfig.getShareControl()
                            .share(mContext, UMSocial.SHARE_QQ, mShareTitle,
                                    mShareUrl, umShareListener);
                break;
            case R.id.tvShareFriendsCircle:
                SocialConfig.getShareControl()
                            .share(mContext, UMSocial.SHARE_CIRCLE, mShareTitle,
                                    mShareUrl, umShareListener);
                break;
            case R.id.tvShareWx:
                SocialConfig.getShareControl()
                            .share(mContext, UMSocial.SHARE_WECHAT, mShareTitle,
                                    mShareUrl, umShareListener);
                break;
            case R.id.tvShareQqZone:
                SocialConfig.getShareControl()
                            .share(mContext, UMSocial.SHARE_QZONE, mShareTitle,
                                    mShareUrl, umShareListener);
                break;
        }
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override public void onStart(SHARE_MEDIA share_media) {

        }


        @Override public void onResult(SHARE_MEDIA platform) {
            ToastUtils.showLongToast("分享成功");
        }


        @Override public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.d("result", t.getMessage() + "啊啊啊");
            ToastUtils.showLongToast("分享失败" + t.getMessage());
        }


        @Override public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showLongToast("分享取消");
        }
    };
}
