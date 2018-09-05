package com.tempus.portal.ui.collection;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.ActivityUtils;
import com.tempus.portal.dao.CObserver;
import com.tempus.portal.dao.ReturnCode;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.model.Collection;
import com.tempus.portal.model.ListData;
import com.tempus.portal.ui.WebActivity;
import com.tempus.portal.view.adapter.MyCollectionAdapter;
import com.tempus.portal.view.widget.DividerItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class MyCollectionActivity extends BaseActivity
        implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.swipeToLoadLayout) SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.swipe_target) RecyclerView mRvCollection;
    @BindView(R.id.tvRight) TextView mTvRight;
    @BindView(R.id.btnCancelCollection) Button mBtnCancelCollection;
    private MyCollectionAdapter mMyCollectionAdapter;
    private int pageNumber = 1;
    private View mEmptyViewGroud;


    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_my_collection;
    }


    @Override protected View getLoadingTargetView() {
        return ButterKnife.findById(this, android.R.id.content);
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
        Button btnGo = mEmptyViewGroud.findViewById(R.id.btnGo);
        btnGo.setVisibility(View.VISIBLE);
        btnGo.setOnClickListener(view -> finish());
        tvEmptyContent.setText("暂无收藏,快去逛逛吧");
        mMyCollectionAdapter = new MyCollectionAdapter();
        mMyCollectionAdapter.openLoadAnimation();
        mRvCollection.setLayoutManager(new LinearLayoutManager(mContext));
        mRvCollection.setAdapter(mMyCollectionAdapter);
        //mRvCollection.addOnItemTouchListener(new OnItemClickListener() {
        //    @Override
        //    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
        //        if (mMyCollectionAdapter.getData() != null &&
        //                mMyCollectionAdapter.getData().size() > 0) {
        //            ActivityUtils.launchActivity(mContext, WebActivity.class,
        //                    WebActivity.buildBundle(
        //                            mMyCollectionAdapter.getData()
        //                                                .get(position).url,
        //                            ""));
        //        }
        //    }
        //});

        mRvCollection.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }


            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
            }


            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Collection collection = mMyCollectionAdapter.getData()
                                                            .get(position);
                switch (view.getId()) {
                    case R.id.llCollection:
                        ActivityUtils.launchActivity(mContext,
                                WebActivity.class,
                                WebActivity.buildBundle(collection.url, ""));
                        break;
                    default:
                        break;
                }
            }


            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
        mSwipeToLoadLayout.setRefreshing(true);
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("我的收藏");
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
                           }
                           if (throwable.code == ReturnCode.LOCAL_NO_NETWORK) {
                               toggleNetworkError(true, v -> {
                                   getData();
                               });
                           }
                           if (pageNumber > 1 &&
                                   throwable.code == ReturnCode.CODE_EMPTY) {
                               pageNumber--;
                           }
                           else {
                               mMyCollectionAdapter.setEdit(false);
                               mBtnCancelCollection.setVisibility(View.GONE);
                               mTvRight.setVisibility(View.GONE);
                               mMyCollectionAdapter.getData().clear();
                               mMyCollectionAdapter.notifyDataSetChanged();
                               mMyCollectionAdapter.setEmptyView(
                                       mEmptyViewGroud);
                               mSwipeToLoadLayout.setLoadMoreEnabled(false);
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
                           mMyCollectionAdapter.setEdit(false);
                           mBtnCancelCollection.setVisibility(View.GONE);
                           if (listData.list != null &&
                                   listData.list.size() > 0) {
                               List<Collection> list = listData.list;
                               if (pageNumber == 1) {
                                   mMyCollectionAdapter.setNewData(list);
                               }
                               else {
                                   mMyCollectionAdapter.addData(list);
                               }
                               mTvRight.setText("编辑");
                               mTvRight.setVisibility(View.VISIBLE);
                           }
                           else {
                               mTvRight.setVisibility(View.GONE);
                           }
                           if (listData.isHasMore()) {
                               mSwipeToLoadLayout.setLoadMoreEnabled(true);
                           }
                           else {
                               mSwipeToLoadLayout.setLoadMoreEnabled(false);
                           }
                       }
                   });
    }


    @OnClick({ R.id.tvRight, R.id.btnCancelCollection })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvRight:
                if (mBtnCancelCollection.getVisibility() == View.VISIBLE) {
                    mBtnCancelCollection.setVisibility(View.GONE);
                    mTvRight.setText("编辑");
                    mMyCollectionAdapter.setEdit(false);
                }
                else {
                    mBtnCancelCollection.setVisibility(View.VISIBLE);
                    mMyCollectionAdapter.setEdit(true);
                    mTvRight.setText("完成");
                }
                break;
            case R.id.btnCancelCollection:
                if (mMyCollectionAdapter == null) {
                    return;
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        mContext).setMessage("确认取消收藏吗?")
                                 .setNegativeButton(android.R.string.cancel,
                                         (dialogInterface, i) -> {
                                             dialogInterface.dismiss();
                                         })
                                 .setPositiveButton("确定", (dialog, which) -> {
                                     dialog.dismiss();
                                     List<Integer> ids = new ArrayList<>();
                                     for (Collection collection : mMyCollectionAdapter
                                             .getData()) {
                                         if (collection.isSelect) {
                                             ids.add(collection.favorId);
                                         }
                                     }
                                     DataManager.cancelCollection(ids).
                                             subscribe(getNotProSubscribe(b -> {
                                                 getData();
                                                 //for (Collection collection : mMyCollectionAdapter
                                                 //        .getData()) {
                                                 //    if (collection.isSelect) {
                                                 //        mMyCollectionAdapter.getData()
                                                 //                            .remove(collection);
                                                 //    }
                                                 //}
                                                 //if (mMyCollectionAdapter.getData()
                                                 //                        .size() ==
                                                 //        0) {
                                                 //    mTvRight.setVisibility(
                                                 //            View.GONE);
                                                 //    mMyCollectionAdapter.setEmptyView(
                                                 //            mEmptyViewGroud);
                                                 //    mSwipeToLoadLayout.setLoadMoreEnabled(
                                                 //            false);
                                                 //}
                                                 //else {
                                                 //    mTvRight.setVisibility(
                                                 //            View.VISIBLE);
                                                 //}
                                                 //mMyCollectionAdapter.notifyDataSetChanged();
                                                 //mBtnCancelCollection.setVisibility(
                                                 //        View.GONE);
                                                 //mTvRight.setText("编辑");
                                                 //mMyCollectionAdapter.setEdit(
                                                 //        false);
                                             }));
                                 });
                alert.setCancelable(false);
                alert.create();
                alert.show();
                break;
        }
    }
}
