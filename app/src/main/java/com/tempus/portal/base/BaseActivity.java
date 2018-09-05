package com.tempus.portal.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.tempus.portal.R;
import com.tempus.portal.app.AppStatusConstant;
import com.tempus.portal.app.AppStatusManager;
import com.tempus.portal.base.utils.EmptyUtils;
import com.tempus.portal.base.utils.SmartBarUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.base.utils.statusbar.StatusBarFontHelper;
import com.tempus.portal.ui.MainActivity;
import com.tempus.portal.view.widget.loading.VaryViewHelperController;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.umeng.analytics.MobclickAgent;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author jianhao025@gmail.com
 * @data: 2016/07/27 08:59
 * @version: V1.0
 */
public abstract class BaseActivity extends BaseFuncActivity
        implements LifecycleProvider<ActivityEvent> {

    /**
     * Log tag
     */
    protected static String TAG_LOG = null;

    /**
     * context
     */
    protected Context mContext = null;

    //Unbinder对象，用来解绑ButterKnife
    private Unbinder mUnbinder;

    /**
     * loading view controller
     */
    private VaryViewHelperController mVaryViewHelperController = null;

    private final BehaviorSubject<ActivityEvent> lifecycleSubject
            = BehaviorSubject.create();

    /**
     * overridePendingTransition mode
     */
    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    protected void restartApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(AppStatusConstant.KEY_HOME_ACTION,
                AppStatusConstant.ACTION_RESTART_APP);
        startActivity(intent);
    }

    @Override @CallSuper protected void onCreate(Bundle savedInstanceState) {
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in,
                            R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in,
                            R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in,
                            R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
        super.onCreate(savedInstanceState);
        //PushAgent.getInstance(this).onAppStart();
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        StatusBarFontHelper.setStatusBarMode(this, true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                       .setSwipeBackEnable(true)
                       .setSwipeEdge(200)//可滑动的范围。px。200表示为左边200px的屏幕
                       .setSwipeEdgePercent(0.2f)//可滑动的范围。百分比。0.2表示为左边20%的屏幕
                       .setSwipeSensitivity(0.5f)//对横向滑动手势的敏感程度。0为迟钝 1为敏感
                       .setSwipeRelateEnable(true)//是否与下一级activity联动(微信效果)。默认关
                       .setSwipeRelateOffset(300);//activity联动时的偏移量。默认500px。
        //不抢占事件，默认关（事件将先由子View处理再由滑动关闭处理）
        // hand the events first.default false;
        // base setup
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }
        SmartBarUtils.hide(getWindow().getDecorView());
        mContext = this;
        TAG_LOG = this.getClass().getSimpleName();
        BaseAppManager.getInstance().addActivity(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        }
        else {
            throw new IllegalArgumentException(
                    "You must return a right contentView layout resource Id");
        }
        initToolbar();
        switch (AppStatusManager.getInstance().getAppStatus()) {
            case AppStatusConstant.STATUS_FORCE_KILLED:
                restartApp();
                break;
            case AppStatusConstant.STATUS_NORMAL:
                initView(savedInstanceState);
                break;
        }
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mUnbinder = ButterKnife.bind(this);
        if (null != getLoadingTargetView()) {
            mVaryViewHelperController = new VaryViewHelperController(
                    getLoadingTargetView());
        }
    }


    @Override public void finish() {
        super.finish();
        BaseAppManager.getInstance().removeActivity(this);
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in,
                            R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in,
                            R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in,
                            R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
    }


    @Override protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }


    /**
     * get bundle data
     */
    protected abstract void getBundleExtras(Bundle extras);

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();

    /**
     * get loading target view
     */
    protected abstract View getLoadingTargetView();

    /**
     * init all views and add events
     */
    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initToolbar();

    /**
     * toggle overridePendingTransition
     */
    protected abstract boolean toggleOverridePendingTransition();

    /**
     * get the overridePendingTransition mode
     */
    protected abstract TransitionMode getOverridePendingTransitionMode();


    /**
     * show toast
     */
    protected void showToast(String msg) {
        //防止遮盖虚拟按键
        if (null != msg && !EmptyUtils.isEmpty(msg)) {
            Snackbar.make(getLoadingTargetView(), msg, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }


    /**
     * toggle show loading
     */
    public void toggleShowLoading(boolean toggle, String msg) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException(
                    "You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showLoading(msg);
        }
        else {
            mVaryViewHelperController.restore();
        }
    }


    /**
     * toggle show empty
     */
    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException(
                    "You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(msg, onClickListener);
        }
        else {
            mVaryViewHelperController.restore();
        }
    }


    /**
     * toggle show error
     */
    protected void toggleShowError(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException(
                    "You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showError(msg, onClickListener);
        }
        else {
            mVaryViewHelperController.restore();
        }
    }


    /**
     * toggle show network error
     */
    protected void toggleNetworkError(boolean toggle, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException(
                    "You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showNetworkError(onClickListener);
        }
        else {
            mVaryViewHelperController.restore();
        }
    }


     @Override public Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }


     @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(
             ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }


     @Override public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }


    @Override @CallSuper protected void onStart() {
        super.onStart();

        lifecycleSubject.onNext(ActivityEvent.START);
    }


    @Override @CallSuper protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
        MobclickAgent.onResume(this);
    }


    @Override @CallSuper protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override @CallSuper protected void onStop() {

        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }


    @Override @CallSuper protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
        runOnUiThread(() -> ToastUtils.cancelToast());
        SwipeBackHelper.onDestroy(this);
        if (mUnbinder != null) {
            //取消绑定ButterKnife
            mUnbinder.unbind();
        }
    }

}
