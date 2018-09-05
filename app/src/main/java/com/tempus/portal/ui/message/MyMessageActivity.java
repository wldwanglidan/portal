package com.tempus.portal.ui.message;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.ActivityUtils;
import com.tempus.portal.dao.CObserver;
import com.tempus.portal.dao.ReturnCode;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.model.ListData;
import com.tempus.portal.model.Message;
import com.tempus.portal.ui.WebActivity;
import com.tempus.portal.view.adapter.MyMessageAdapter;
import com.tempus.portal.view.widget.DividerItemDecoration;
import java.util.List;

public class MyMessageActivity extends BaseActivity
        implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.swipeToLoadLayout) SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.swipe_target) RecyclerView mRvMessage;
    private MyMessageAdapter mMyMessageAdapter;
    private int pageNumber = 1;
    private View mEmptyViewGroud;


    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_my_message;
    }


    @Override protected View getLoadingTargetView() {
         return ButterKnife.findById(this, R.id.swipeToLoadLayout);
    }


    @Override protected void initView(Bundle savedInstanceState) {
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        DividerItemDecoration mDividerItemDecoration
                = new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST, new ColorDrawable(
                getResources().getColor(R.color.transparent)));
        mDividerItemDecoration.setHeight(
                getResources().getDimensionPixelSize(R.dimen.dp_15));
        mRvMessage.addItemDecoration(mDividerItemDecoration);
        mEmptyViewGroud = getLayoutInflater().inflate(
                R.layout.layout_empty_view, (ViewGroup) mRvMessage.getParent(),
                false);
        TextView tvEmptyContent = mEmptyViewGroud.findViewById(
                R.id.tvEmptyContent);
        tvEmptyContent.setText("暂无消息,先去逛逛吧!");
        mMyMessageAdapter = new MyMessageAdapter();
        mMyMessageAdapter.openLoadAnimation();
        mRvMessage.setLayoutManager(new LinearLayoutManager(mContext));
        mRvMessage.setAdapter(mMyMessageAdapter);
        mRvMessage.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityUtils.launchActivity(mContext, WebActivity.class,
                        WebActivity.buildBundle(mMyMessageAdapter.getData()
                                                                 .get
                                                                         (position).newsLink,
                                ""));
            }
        });
        mSwipeToLoadLayout.setRefreshing(true);
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("我的消息");
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
        DataManager.myMessage(pageNumber)
                   .compose(bindToLifecycle())
                   .subscribe(new CObserver<ListData<Message>>() {
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
                           //else {
                           //    toggleShowError(true, "", v -> {
                           //        getData();
                           //    });
                           //}
                           if (pageNumber > 1 &&
                                   throwable.code == ReturnCode.CODE_EMPTY) {
                               pageNumber--;
                           }
                           else {
                               mMyMessageAdapter.getData().clear();
                               mMyMessageAdapter.notifyDataSetChanged();
                               mMyMessageAdapter.setEmptyView(mEmptyViewGroud);
                               mSwipeToLoadLayout.setLoadMoreEnabled(false);
                           }
                           //ToastUtils.showLongToast(throwable.msg);
                       }


                       @Override
                       public void onSuccess(ListData<Message> listData) {
                           if (mSwipeToLoadLayout.isLoadingMore()) {
                               mSwipeToLoadLayout.setLoadingMore(false);
                           }
                           if (mSwipeToLoadLayout.isRefreshing()) {
                               mSwipeToLoadLayout.setRefreshing(false);
                           }
                           List<Message> list = listData.list;
                           if (pageNumber == 1) {
                               mMyMessageAdapter.setNewData(list);
                           }
                           else {
                               mMyMessageAdapter.addData(list);
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
}
