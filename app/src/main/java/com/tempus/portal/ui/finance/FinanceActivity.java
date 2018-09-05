package com.tempus.portal.ui.finance;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.ActivityUtils;
import com.tempus.portal.base.utils.AppUtils;
import com.tempus.portal.base.utils.glide.GlideUtils;
import com.tempus.portal.dao.CObserver;
import com.tempus.portal.dao.ReturnCode;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.model.Finance;
import com.tempus.portal.model.HomeMenu;
import com.tempus.portal.model.event.LoginEvent;
import com.tempus.portal.ui.WebActivity;
import com.tempus.portal.view.adapter.FinanceBannerImageHolderView;
import com.tempus.portal.view.adapter.InsuranceAdapter;
import com.tempus.portal.view.adapter.TourAdapter;
import com.tempus.portal.view.widget.DividerItemDecoration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.tempus.portal.R.id.toolbar;

/**
 * Created by Administrator on 2017/9/4.
 */

public class FinanceActivity extends BaseActivity {
    @BindView(toolbar) Toolbar mToolbar;
    @BindView(R.id.rvTour) RecyclerView mRvTour;
    @BindView(R.id.rvInsurance) RecyclerView mRvInsurance;
    @BindView(R.id.tvToolbarTitle) TextView mTvToolbarTitle;
    @BindView(R.id.convenientBanner) ConvenientBanner mConvenientBanner;
    @BindView(R.id.ivFinancial) ImageView mIvFinancial;
    @BindView(R.id.ivFund) ImageView mIvFund;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout
            mCollapsing_toolbar;

    private TourAdapter mTourAdapter;
    private InsuranceAdapter mInsuranceAdapter;
    private Finance mFinance;
    private HomeMenu mHomeMenu;
    @BindView(R.id.appbar) AppBarLayout mAppbar;
    public static Bundle buildBundle(HomeMenu homeMenu) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("homeMenu", homeMenu);
        return bundle;
    }


    @Override protected void getBundleExtras(Bundle extras) {
        mHomeMenu = (HomeMenu) extras.getSerializable("homeMenu");
    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_finance;
    }


    @Override protected View getLoadingTargetView() {
        return ButterKnife.findById(this, android.R.id.content);
    }


    @Override protected void initView(Bundle savedInstanceState) {

        mConvenientBanner.setPageIndicatorAlign(
                ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                         .setPageIndicator(
                                 new int[] { R.drawable.ic_page_indicator,
                                         R.drawable.ic_page_indicator_focused });

        mConvenientBanner.setOnItemClickListener(position -> {
            if (mFinance.tpurseProductList != null &&
                    mFinance.tpurseProductList.size() > 0) {
                Finance.TpurseProductListBean tpurseProductListBean
                        = mFinance.tpurseProductList.get(position);
                if (TextUtils.isEmpty(tpurseProductListBean.productLink)) {
                    AppUtils.launchTPayApp(mContext);
                }
                else {
                    ActivityUtils.launchActivity(mContext, WebActivity.class,
                            WebActivity.buildBundle(
                                    tpurseProductListBean.productLink, ""));
                }
            }
        });
        mTourAdapter = new TourAdapter();
        mInsuranceAdapter = new InsuranceAdapter();
        mRvTour.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(mRvTour);
        mRvTour.setNestedScrollingEnabled(false);
        mRvTour.setFocusable(false);
        mRvTour.setHasFixedSize(true);
        mRvTour.setAdapter(mTourAdapter);
        mRvTour.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mTourAdapter.getData() != null &&
                        mTourAdapter.getData().size() > 0) {
                    ActivityUtils.launchActivity(mContext, WebActivity.class,
                            WebActivity.buildBundle(mTourAdapter.getData()
                                                                .get(position).productLink,
                                    mHomeMenu.merchId));
                }
            }
        });
        DividerItemDecoration mDividerItemDecoration
                = new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST,
                new ColorDrawable(getResources().getColor(R.color.divider)));
        mDividerItemDecoration.setHeight(1);
        mRvInsurance.addItemDecoration(mDividerItemDecoration);
        mRvInsurance.setLayoutManager(new LinearLayoutManager(mContext));
        mRvInsurance.setAdapter(mInsuranceAdapter);
        mRvInsurance.setNestedScrollingEnabled(false);
        mRvInsurance.setFocusable(false);
        mRvInsurance.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mInsuranceAdapter.getData() != null &&
                        mInsuranceAdapter.getData().size() > 0) {
                    ActivityUtils.launchActivity(mContext, WebActivity.class,
                            WebActivity.buildBundle(mInsuranceAdapter.getData()
                                                                     .get(position).productLink,
                                    mHomeMenu.merchId));
                }
            }
        });
        getData();
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        mCollapsing_toolbar.setCollapsedTitleGravity(
                Gravity.CENTER);//设置收缩后标题的位置
        mCollapsing_toolbar.setExpandedTitleGravity(
                Gravity.CENTER);////设置展开后标题的位置
        mCollapsing_toolbar.setTitle("");//设置标题的名字
        mCollapsing_toolbar.setExpandedTitleColor(Color.WHITE);//设置展开后标题的颜色
        mCollapsing_toolbar.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后标题的颜色
        mTvToolbarTitle.setText("金融");
        mAppbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                mTvToolbarTitle.setTextColor(
                        getResources().getColor(R.color.White));
                mToolbar.setNavigationIcon( R.drawable
                        .ibt_return);
            }
            else if (Math.abs(verticalOffset) >=
                    appBarLayout.getTotalScrollRange()) {
                mTvToolbarTitle.setTextColor(getResources().getColor(
                        R.color.textTitleColor));
                mToolbar.setNavigationIcon(R.drawable.ic_back);
            }
            else {
                mTvToolbarTitle.setTextColor(
                        getResources().getColor(R.color.White));
                mToolbar.setNavigationIcon( R.drawable
                        .ibt_return);
            }
        });


    }

    //@Override public boolean onCreateOptionsMenu(Menu menu) {
    //    // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_finance, menu);
    //    return true;
    //}
    //
    //
    //@Override public boolean onOptionsItemSelected(MenuItem item) {
    //    int id = item.getItemId();
    //    if (id == R.id.action_pop) {
    //        AppUtils.getPopupWindow(ButterKnife.findById(this, R.id.action_pop),
    //                mContext, mHomeMenu.merchId, mHomeMenu.merchName,
    //                mHomeMenu.merchLink);
    //        return true;
    //    }
    //    return super.onOptionsItemSelected(item);
    //}


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @OnClick({ R.id.rlMoreFund, R.id.btnGoImmediately })
    public void onViewClicked(View view) {
        if (mFinance == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnGoImmediately:
                ActivityUtils.launchActivity(mContext, WebActivity.class,
                        WebActivity.buildBundle(mFinance.vcProductLink, ""));
                break;
            case R.id.rlMoreFund:
                ActivityUtils.launchActivity(mContext, WebActivity.class,
                        WebActivity.buildBundle(mFinance.fundProductLink,
                                mHomeMenu.merchId));
                break;
        }
    }


    private void getData() {
        DataManager.getFinance()
                   .compose(bindToLifecycle())
                   .subscribe(new CObserver<Finance>() {
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


                       @Override public void onSuccess(Finance finance) {
                           toggleShowLoading(false, "load...");
                           mFinance = finance;
                           if (finance.tpurseProductList != null &&
                                   finance.tpurseProductList.size() > 0) {
                               mConvenientBanner.setPages(
                                       () -> new FinanceBannerImageHolderView(),
                                       finance.tpurseProductList);
                           }

                           if (finance.tourProductList != null &&
                                   finance.tourProductList.size() > 0) {
                               mTourAdapter.setNewData(finance.tourProductList);
                           }
                           if (finance.insuranceProductList != null &&
                                   finance.insuranceProductList.size() > 0) {
                               mInsuranceAdapter.setNewData(
                                       finance.insuranceProductList);
                           }
                           GlideUtils.displayUrl(mIvFinancial,
                                   finance.vcProductPicture,
                                   R.drawable.bg_iv_default_banner);
                           GlideUtils.displayUrl(mIvFund,
                                   finance.fundProductPicture,
                                   R.drawable.bg_iv_default_banner);
                       }
                   });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent resultEvent) {
        getData();
    }


    @Override @CallSuper protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override @CallSuper protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    // 开始自动翻页
    @Override protected void onResume() {
        super.onResume();
        //开始自动翻页
        mConvenientBanner.startTurning(5000);
    }


    // 停止自动翻页
    @Override protected void onPause() {
        super.onPause();
        //停止翻页
        mConvenientBanner.stopTurning();
    }
}

