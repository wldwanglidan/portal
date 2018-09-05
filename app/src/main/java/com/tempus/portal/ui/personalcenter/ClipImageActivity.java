package com.tempus.portal.ui.personalcenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.view.ClipViewLayout;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class ClipImageActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tvRight) TextView mTvRight;

    @BindView(R.id.clipViewLayout) ClipViewLayout mClipViewLayout;




    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_clip_image;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {

    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("裁剪");
        mTvRight.setText("保存");

    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @Override protected void onResume() {
        super.onResume();
        mClipViewLayout.setVisibility(View.VISIBLE);
        //设置图片资源
        mClipViewLayout.setImageSrc(getIntent().getData());


    }

    @OnClick({ R.id.tvRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRight:
                generateUriAndReturn();
                break;

        }
    }

    /**
     * 生成Uri并且通过setResult返回给打开的activity
     */
    private void generateUriAndReturn() {
        //调用返回剪切图
        Bitmap zoomedCropBitmap;
            zoomedCropBitmap = mClipViewLayout.clip();
        if (zoomedCropBitmap == null) {
            //Log.e("android", "zoomedCropBitmap == null");
            return;
        }
        Uri mSaveUri = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis() + ".jpg"));
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException ex) {
                //Log.e("android", "Cannot open file: " + mSaveUri, ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Intent intent = new Intent();
            intent.setData(mSaveUri);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
