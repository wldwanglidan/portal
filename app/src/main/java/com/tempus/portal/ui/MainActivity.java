package com.tempus.portal.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.lishide.recyclerview.scroll.ScrollRecyclerView;
import com.lishide.recyclerview.scroll.SpaceItemDecoration;
import com.tempus.portal.R;
import com.tempus.portal.app.App;
import com.tempus.portal.app.AppContext;
import com.tempus.portal.app.AppStatusConstant;
import com.tempus.portal.app.Constant;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.ActivityUtils;
import com.tempus.portal.base.utils.DownloadUtil;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.base.utils.glide.GlideUtils;
import com.tempus.portal.dao.CObserver;
import com.tempus.portal.dao.ReturnCode;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.manager.UserManager;
import com.tempus.portal.model.GlobalParams;
import com.tempus.portal.model.HomeMenu;
import com.tempus.portal.model.HomeNavigation;
import com.tempus.portal.model.ListData;
import com.tempus.portal.model.User;
import com.tempus.portal.model.event.LoginEvent;
import com.tempus.portal.ui.about.AboutActivity;
import com.tempus.portal.ui.collection.MyCollectionActivity;
import com.tempus.portal.ui.finance.FinanceActivity;
import com.tempus.portal.ui.information.ComInformationActivity;
import com.tempus.portal.ui.message.MyMessageActivity;
import com.tempus.portal.ui.personalcenter.EditPersonalActivity;
import com.tempus.portal.ui.user.LoginActivity;
import com.tempus.portal.ui.user.VipLevelActivity;
import com.tempus.portal.view.adapter.HomeMenuAdapter;
import com.tempus.portal.view.adapter.HomeNavigationAdapter;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static long DOUBLE_CLICK_TIME = 0L;
    @BindView(R.id.ivHomeBg) ImageView mIvHomeBg;
    @BindView(R.id.ivUserHead) ImageView mIvUserHead;
    @BindView(R.id.ivTranWhite) ImageView mIvTranWhite;
    @BindView(R.id.tvNickName) TextView mTvNickName;
    @BindView(R.id.ivVipLevel) ImageView mIvVipLevel;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.rvMenu) ScrollRecyclerView mRvMenu;
    @BindView(R.id.rvHomeNavigation) RecyclerView mRvHomeNavigation;
    private HomeMenuAdapter mHomeMenuAdapter;
    private HomeNavigationAdapter mHomeNavigationAdapter;
    private int[] imageRes = { R.drawable.ic_common_information,
            R.drawable.ic_my_collection, R.drawable.ic_my_message,
            R.drawable.ic_about };
    private String[] name = { "常用信息", "我的收藏", "我的消息", "关于我们" };
    private GlobalParams mGlobalParams;


    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }


    @Override protected View getLoadingTargetView() {
        return ButterKnife.findById(this, android.R.id.content);
    }


    @Override protected void initView(Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, 0,
                null);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        SwipeBackHelper.getCurrentPage(this)
                       .setDisallowInterceptTouchEvent(true);
        checkForUpdates();
        mRvHomeNavigation.setLayoutManager(new LinearLayoutManager(mContext));
        //mRvHomeNavigation.setNestedScrollingEnabled(false);
        //mRvHomeNavigation.setFocusable(false);
        mRvHomeNavigation.addOnItemTouchListener(
                new com.chad.library.adapter.base.listener.OnItemClickListener() {
                    @Override
                    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                        if (!UserManager.getInstance().isLogin()) {
                            ActivityUtils.launchActivity(mContext,
                                    LoginActivity.class);
                            return;
                        }
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        if (position == 0) {
                            ActivityUtils.launchActivity(mContext,
                                    ComInformationActivity.class);
                        }
                        else if (position == 1) {
                            ActivityUtils.launchActivity(mContext,
                                    MyCollectionActivity.class);
                        }
                        //else if (position == 2) {
                        //    ActivityUtils.launchActivity(mContext,
                        //            BrowsingHistoryActivity.class);
                        //}
                        else if (position == 2) {
                            ActivityUtils.launchActivity(mContext,
                                    MyMessageActivity.class);
                        }
                        else if (position == 3) {
                            ActivityUtils.launchActivity(mContext,
                                    AboutActivity.class);
                        }
                    }
                });

        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(mRvMenu);
        mHomeMenuAdapter = new HomeMenuAdapter();
        // 设置动画
        mRvMenu.setItemAnimator(new DefaultItemAnimator());
        // 设置布局管理器：瀑布流式
        mRvMenu.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.HORIZONTAL));
        // 根据需要设置间距等
        int right = (int) getResources().getDimension(R.dimen.dp_10);
        int bottom = (int) getResources().getDimension(R.dimen.dp_10);
        RecyclerView.ItemDecoration spacingInPixel = new SpaceItemDecoration(
                right, bottom);
        mRvMenu.addItemDecoration(spacingInPixel);
        mRvMenu.setAdapter(mHomeMenuAdapter);
        mHomeMenuAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeMenu homeMenu = mHomeMenuAdapter.getData().get(position);
            if (homeMenu.isNeedLogin()) {
                if (!UserManager.getInstance().isLogin()) {
                    ActivityUtils.launchActivity(mContext, LoginActivity.class);
                    return;
                }
            }
            //打开金融界面
            if (homeMenu.isFin()) {
                ActivityUtils.launchActivity(mContext, FinanceActivity.class,
                        FinanceActivity.buildBundle(homeMenu));
            }
            else {
                ActivityUtils.launchActivity(mContext, WebActivity.class,
                        WebActivity.buildBundle(homeMenu.merchLink, ""));
            }
        });

        List<HomeNavigation> homeNavigationList = new ArrayList<>();
        for (int i = 0; i < imageRes.length; i++) {
            homeNavigationList.add(new HomeNavigation(imageRes[i], name[i]));
        }
        mHomeNavigationAdapter = new HomeNavigationAdapter(homeNavigationList);
        mRvHomeNavigation.setAdapter(mHomeNavigationAdapter);
        mGlobalParams = (GlobalParams) App.getACache()
                                          .getAsObject(
                                                  Constant.KEY_GLOBALPARAMS);
        if (mGlobalParams != null) {
            GlideUtils.display(mIvHomeBg, mGlobalParams.backgroundImg);
        }
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }


            @Override public void onDrawerOpened(View drawerView) {
                myInfo();
            }


            @Override public void onDrawerClosed(View drawerView) {
            }


            @Override public void onDrawerStateChanged(int newState) {

            }
        });
        getData();
    }


    @Override protected void initToolbar() {

    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    private void getData() {
        DataManager.getHome()
                   .compose(bindToLifecycle())
                   .subscribe(new CObserver<ListData<HomeMenu>>() {
                       @Override public void onPrepare() {
                           toggleShowLoading(true, "load...");
                       }


                       @Override public void onError(ErrorThrowable throwable) {
                           toggleShowLoading(false, "load...");
                           if (throwable.code == ReturnCode.LOCAL_NO_NETWORK) {
                               toggleNetworkError(true, v -> {
                                   getData();
                               });
                           }
                           else {
                               toggleShowError(true, "", v -> {
                                   getData();
                               });
                           }
                           Log.d("result", throwable.msg + "");
                       }


                       @Override
                       public void onSuccess(ListData<HomeMenu> homeMenuListData) {
                           toggleShowLoading(false, "load...");
                           if (homeMenuListData.list != null &&
                                   homeMenuListData.list.size() > 0) {
                               mHomeMenuAdapter.setNewData(
                                       homeMenuListData.list);
                           }
                       }
                   });
    }


    private void myInfo() {
        DataManager.myInfo().compose(bindToLifecycle()).
                subscribe(new CObserver<User>() {
                    @Override public void onPrepare() {

                    }


                    @Override public void onError(ErrorThrowable throwable) {
                        setUserView(null);
                    }


                    @Override public void onSuccess(User user) {
                        setUserView(user);
                    }
                });
    }


    @OnClick({ R.id.ibtMenu, R.id.llUser, R.id.ivVipLevel, R.id.ivUserHead })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtMenu:
                if (UserManager.getInstance().isLogin()) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                else {
                    login();
                }
                break;
            case R.id.ivVipLevel:
                if (UserManager.getInstance().isLogin()) {
                    startActivity(new Intent(this, VipLevelActivity.class));
                }
                else {
                    login();
                }
                break;
            case R.id.ivUserHead:
                if (UserManager.getInstance().isLogin()) {
                    startActivity(new Intent(this, EditPersonalActivity.class));
                }
                else {
                    login();
                }
                break;
            case R.id.llUser:
                if (UserManager.getInstance().isLogin()) {
                    ActivityUtils.launchActivity(mContext,
                            EditPersonalActivity.class);
                }
                else {
                    login();
                }
                break;
        }
    }


    /**
     * 登录
     */
    private void login() {startActivity(new Intent(this, LoginActivity.class));}


    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
            else {
                if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 2000) {
                    // showToast(getString(R.string.double_click_exit));
                    ToastUtils.showLongToast(R.string.double_click_exit);
                    DOUBLE_CLICK_TIME = System.currentTimeMillis();
                }
                else {
                    ((App) getApplication()).exitApp();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent resultEvent) {
        //Log.d("result", "首页LoginEvent执行");
        myInfo();
        getData();
    }


    private void setUserView(User user) {
        if (user != null) {
            //设置头像
            GlideUtils.displayCircleHeader(mIvUserHead, user.headUrl);
            mTvNickName.setText(!TextUtils.isEmpty(user.nickname)
                                ? user.nickname
                                : user.mobile);
            if (mHomeNavigationAdapter != null) {
                HomeNavigation homeNavigation = mHomeNavigationAdapter.getData()
                                                                      .get(2);
                homeNavigation.isUnread = user.isUnreadNews();
                mHomeNavigationAdapter.setData(2, homeNavigation);
            }
            if (user.vipLevel.equals("T1")) {
                mIvVipLevel.setImageResource(R.drawable.ic_t1);
            }
            else if (user.vipLevel.equals("T2")) {
                mIvVipLevel.setImageResource(R.drawable.ic_t2);
            }
            else if (user.vipLevel.equals("T3")) {
                mIvVipLevel.setImageResource(R.drawable.ic_t3);
            }
            if (user.vipLevel.equals("T4")) {
                mIvVipLevel.setImageResource(R.drawable.ic_t4);
            }
            mIvVipLevel.setVisibility(View.VISIBLE);
            mIvTranWhite.setVisibility(View.VISIBLE);
        }
        else {
            GlideUtils.displayNative(mIvUserHead, R.drawable.default_head);
            mTvNickName.setText("");
            mIvVipLevel.setVisibility(View.GONE);
            mIvTranWhite.setVisibility(View.GONE);
        }
    }


    //@Override @CallSuper protected void onStart() {
    //    super.onStart();
    //    EventBus.getDefault().register(this);
    //}
    //
    //
    //@Override @CallSuper protected void onStop() {
    //    EventBus.getDefault().unregister(this);
    //    super.onStop();
    //}


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    @Override protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private void checkForUpdates() {
        DataManager.checkForUpdates(AppContext.APP_VERSION_CODE)
                   .compose(bindToLifecycle())
                   .
                           subscribe(getNotProSubscribe(v -> {
                               if (v.updateType.equals("2")) {
                                   AlertDialog.Builder alert1
                                           = new AlertDialog.Builder(
                                           mContext).setTitle("检测到新版本,是否更新")
                                                    .setMessage(v.updateInfo)
                                                    .setPositiveButton("更新",
                                                            (dialog, which) -> {
                                                                mProgressDialogUtils
                                                                        .showProgress(
                                                                                "下载更新中");
                                                                DownloadUtil.startDownload(
                                                                        AppContext
                                                                                .getContext(),
                                                                        v.downloadUrl);
                                                            });
                                   alert1.setCancelable(false);
                                   alert1.create();
                                   alert1.show();
                               }
                               else if (v.updateType.equals("1")) {
                                   AlertDialog.Builder alert
                                           = new AlertDialog.Builder(
                                           mContext).setTitle("检测到新版本,是否更新")
                                                    .setMessage(v.updateInfo)
                                                    .setNegativeButton(
                                                            android.R.string.cancel,
                                                            (dialogInterface, i) -> {
                                                                dialogInterface.dismiss();
                                                            })
                                                    .setPositiveButton("更新",
                                                            (dialog, which) -> {
                                                                mProgressDialogUtils
                                                                        .showProgress(
                                                                                "下载更新中");
                                                                DownloadUtil.startDownload(
                                                                        AppContext
                                                                                .getContext(),
                                                                        v.downloadUrl);
                                                            });
                                   alert.setCancelable(false);
                                   alert.create();
                                   alert.show();
                               }
                           }));
    }


    @Override protected void restartApp() {
        ToastUtils.showLongToast("应用被回收重启");
        startActivity(new Intent(this, StartPageActivity.class));
        finish();
    }


    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int action = intent.getIntExtra(AppStatusConstant.KEY_HOME_ACTION,
                AppStatusConstant.ACTION_BACK_TO_HOME);
        switch (action) {
            case AppStatusConstant.ACTION_RESTART_APP:
                restartApp();
                break;
        }
    }



}
