package com.tempus.portal.ui.personalcenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.tempus.portal.R;
import com.tempus.portal.app.Constant;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.base.utils.glide.GlideUtils;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.manager.UserManager;
import com.tempus.portal.model.User;
import com.tempus.portal.model.event.LoginEvent;
import java.io.File;
import org.greenrobot.eventbus.EventBus;

import static com.tempus.portal.view.ClipViewLayout.getRealFilePathFromUri;

public class EditAvatarActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tvRight) TextView mTvRight;

    @BindView(R.id.ll_pop) LinearLayout mLlPop;
    @BindView(R.id.ivUserHead) ImageView mIvUserHead;
    @BindView(R.id.tv_photo) TextView mTvPhoto;
    @BindView(R.id.tv_camera) TextView mTvCamera;
    @BindView(R.id.tv_cancel) TextView mTvCancel;

    private int[] colors = { R.color.transTextRightColor,
            R.color.textNickNameColor};

    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;

    private String userPhone;
    private User mUser;

    //调用照相机返回图片文件
    private File tempFile;


    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_edit_avatar;
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
            GlideUtils.displayCircleHeader(mIvUserHead,
                    UserManager.getInstance().getUserInfo().headUrl);
            userPhone = UserManager.getInstance().getUserInfo().mobile;
        }
        else {
            GlideUtils.displayNative(mIvUserHead, R.drawable.default_head);
        }
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("设置头像");
        //mTvRight.setBackgroundResource(R.drawable.ic_share_point);//这样图片会严重失真
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_pop) {
            mLlPop.setVisibility(View.VISIBLE);
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


    @OnClick({ R.id.tv_camera, R.id.tv_photo, R.id.tv_cancel })
    public void onClick(View view) {
        switch (view.getId()) {
            //case R.id.tvRight:
            //    mLlPop.setVisibility(View.VISIBLE);
            //    break;
            case R.id.tv_camera:
                changeTextColor(mTvPhoto,colors[1]);
                changeTextColor(mTvCancel,colors[1]);
                changeTextColor(mTvCamera,colors[0]);
                gotoCamera();
                break;
            case R.id.tv_photo:
                changeTextColor(mTvPhoto,colors[0]);
                changeTextColor(mTvCancel,colors[1]);
                changeTextColor(mTvCamera,colors[1]);
                gotoPhoto();
                break;
            case R.id.tv_cancel:
                changeTextColor(mTvPhoto,colors[1]);
                changeTextColor(mTvCancel,colors[0]);
                changeTextColor(mTvCamera,colors[1]);
                mLlPop.setVisibility(View.INVISIBLE);
                break;
        }
    }


    /**
     * 改变TextColor
     * @param view
     * @param color
     */
    private void changeTextColor(TextView view,int color) {
        view.setTextColor(
                getResources().getColor(color));
    }


    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }


    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        //Log.d("evan", "*****************打开图库********************");
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"),
                REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCamera() {
        //Log.d("evan", "*****************打开相机********************");
        //创建拍照存储的图片文件
        tempFile = new File(checkDirPath(
                Environment.getExternalStorageDirectory().getPath() +
                        "/image/"), System.currentTimeMillis() + ".jpg");

        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(EditAvatarActivity
                    .this, Constant.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        }
        else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, REQUEST_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    //Log.d("evan", "**********pick path*******" + getRealFilePathFromUri(MainActivity.this, uri));
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(
                            getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);

                    mIvUserHead.setImageBitmap(bitMap);
                    //此处后面可以将图片上传后台网络

                    DataManager.editAvatar(cropImagePath)
                               .compose(bindToLifecycle())
                               .
                                       subscribe(getSubscribe(R.string.editIng,
                                               s -> editSuccess(s)));
                }
                break;
        }
    }


    private void editSuccess(User user) {
        //记住，mUser用之前一定要先赋值。
        mUser.headUrl = user.headUrl;//这里应答报文里面返回返回了headUrl，但是修改昵称里面没有返回
        UserManager.getInstance().setUserInfo(mUser);
        EventBus.getDefault().post(new LoginEvent());
        ToastUtils.showLongToast("编辑成功");
        setResult(RESULT_OK);
        finish();
    }


    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }
}
