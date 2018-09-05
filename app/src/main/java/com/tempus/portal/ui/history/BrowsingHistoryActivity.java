package com.tempus.portal.ui.history;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.dao.CObserver;
import com.tempus.portal.dao.ReturnCode;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.model.Collection;
import com.tempus.portal.model.ListData;
import com.tempus.portal.view.adapter.MyCollectionAdapter;
import com.tempus.portal.view.widget.DividerItemDecoration;
import java.util.List;

public class BrowsingHistoryActivity extends BaseActivity
        implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.swipeToLoadLayout) SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.swipe_target) RecyclerView mRvCollection;
    @BindView(R.id.tvRight) TextView mTvRight;
    private MyCollectionAdapter mMyCollectionAdapter;
    private int pageNumber = 1;
    private View mEmptyViewGroud;


    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_my_collection;
    }


    @Override protected View getLoadingTargetView() {
        return null;
        // return ButterKnife.findById(this, R.id.swipeToLoadLayout);
    }


    @Override protected void initView(Bundle savedInstanceState) {
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        DividerItemDecoration mDividerItemDecoration
                = new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST,
                new ColorDrawable(getResources().getColor(R.color.divider)));
        mDividerItemDecoration.setHeight(1);
        mRvCollection.addItemDecoration(mDividerItemDecoration);
        mEmptyViewGroud = getLayoutInflater().inflate(
                R.layout.layout_empty_view,
                (ViewGroup) mRvCollection.getParent(), false);
        TextView tvEmptyContent = mEmptyViewGroud.findViewById(
                R.id.tvEmptyContent);
        Drawable drawable = getResources().getDrawable(
                R.drawable.bg_empty_history);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        tvEmptyContent.setCompoundDrawables(null, drawable, null, null);
        tvEmptyContent.setText("暂无浏览历史");
        mMyCollectionAdapter = new MyCollectionAdapter();
        mMyCollectionAdapter.openLoadAnimation();
        mRvCollection.setLayoutManager(new LinearLayoutManager(mContext));
        mRvCollection.setAdapter(mMyCollectionAdapter);
        mSwipeToLoadLayout.setRefreshing(true);
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("浏览历史");
        mTvRight.setText("清空");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @Override public void onLoadMore() {
        pageNumber++;
        getData();
    }


    @Override public void onRefresh() {
        pageNumber = 1;
        getData();
    }


    private void getData() {
        DataManager.myCollection(pageNumber)
                   .compose(bindToLifecycle())
                   .subscribe(new CObserver<ListData<Collection>>() {
                       @Override public void onPrepare() {
                       }


                       @Override public void onError(ErrorThrowable throwable) {
                           if (mSwipeToLoadLayout.isLoadingMore()) {
                               mSwipeToLoadLayout.setLoadingMore(false);
                           }
                           if (mSwipeToLoadLayout.isRefreshing()) {
                               mSwipeToLoadLayout.setRefreshing(false);
                           }  if (throwable.code == ReturnCode.LOCAL_NO_NETWORK) {
                               toggleNetworkError(true, v -> {
                                   getData();
                               });
                           }
                           else {
                               toggleShowError(true, "", v -> {
                                   getData();
                               });
                           }
                           if (pageNumber > 1 &&
                                   throwable.code == ReturnCode.CODE_EMPTY) {
                               pageNumber--;
                           }
                           else {
                               mMyCollectionAdapter.getData().clear();
                               mMyCollectionAdapter.notifyDataSetChanged();
                               mMyCollectionAdapter.setEmptyView(
                                       mEmptyViewGroud);
                               mSwipeToLoadLayout.setLoadMoreEnabled(false);
                               mTvRight.setText("");
                           }
                           //ToastUtils.showLongToast(throwable.msg);
                       }


                       @Override
                       public void onSuccess(ListData<Collection> listData) {
                           if (mSwipeToLoadLayout.isLoadingMore()) {
                               mSwipeToLoadLayout.setLoadingMore(false);
                           }
                           if (mSwipeToLoadLayout.isRefreshing()) {
                               mSwipeToLoadLayout.setRefreshing(false);
                           }
                           List<Collection> list = listData.list;
                           if (pageNumber == 1) {
                               mMyCollectionAdapter.setNewData(list);
                           }
                           else {
                               mMyCollectionAdapter.addData(list);
                           }
                           if (listData.isHasMore()) {
                               mSwipeToLoadLayout.setLoadMoreEnabled(true);
                           }
                           else {
                               mSwipeToLoadLayout.setLoadMoreEnabled(false);
                           }
                           //Log.d("result", listData.data.list.size() + "数据");
                       }
                   });
    }


    @OnClick({ R.id.tvRight, R.id.btnCancelCollection })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvRight:
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        mContext).setMessage("确认清空历史吗?")
                                 .setNegativeButton("确定",
                                         (dialogInterface, i) -> {

                                         })
                                 .setPositiveButton("取消", (dialog, which) -> {
                                     dialog.dismiss();
                                 });
                alert.setCancelable(true);
                alert.create();
                alert.show();
                break;
            case R.id.btnCancelCollection:
                break;
        }
    }
}
