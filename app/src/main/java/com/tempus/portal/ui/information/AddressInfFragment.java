package com.tempus.portal.ui.information;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseFragment;
import com.tempus.portal.base.utils.ActivityUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.dao.CObserver;
import com.tempus.portal.dao.ReturnCode;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.model.ListData;
import com.tempus.portal.model.information.AddressInformation;
import com.tempus.portal.view.adapter.AddressInfAdapter;
import com.tempus.portal.view.widget.DividerItemDecoration;
import java.util.List;

import static com.tempus.portal.ui.information.ComInformationActivity.REQUEST_CODE_ADDRESS;

/**
 * Created by Administrator on 2017/1/3.
 */

public class AddressInfFragment extends BaseFragment
        implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.swipe_target) RecyclerView mRv;
    @BindView(R.id.swipeToLoadLayout) SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.btnAddAddress) Button mAddBtn;
    @BindView(R.id.llAddBtn) LinearLayout mLlAdd;
    private AddressInfAdapter mAddressInfAdapter;
    private View mEmptyViewGroud;

    private int pageNumber = 1;
    private String mStatus;


    public static AddressInfFragment newInstance(String status) {
        AddressInfFragment fragment = new AddressInfFragment();
        Bundle bundle = new Bundle();
        bundle.putString("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }


    private void getExtra() {
        mStatus = getArguments().getString("status");
    }


    @Override public void onRefresh() {
        pageNumber = 1;
        getData();
    }


    @Override public void onLoadMore() {
        pageNumber++;
        getData();
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }

    //@Override protected void initView() {
    //
    //}


    @Override protected int getContentViewID() {
        return R.layout.fragment_address_inf;
    }


    @Override public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        if (savedInstanceState == null) {

            getExtra();
            mSwipeToLoadLayout.setOnLoadMoreListener(this);
            mSwipeToLoadLayout.setOnRefreshListener(this);
            mAddressInfAdapter = new AddressInfAdapter();
            mAddressInfAdapter.openLoadAnimation();
            mRv.setAdapter(mAddressInfAdapter);

            mLlAdd.setVisibility(View.GONE);
            mEmptyViewGroud = getActivity().getLayoutInflater()
                                           .inflate(R.layout.layout_infempty_view,
                                                   (ViewGroup) mRv.getParent(),
                                                   false);
            TextView tvEmptyContent = mEmptyViewGroud.findViewById(
                    R.id.tvEmptyContent);

            tvEmptyContent.setText("暂无常用地址信息");

            TextView tvEmpty = mEmptyViewGroud.findViewById(R.id.tvEmpty);
            tvEmpty.setText("添加常用地址信息，预定更方便快捷！");

            Button btnAdd = mEmptyViewGroud.findViewById(R.id.btnAdd);
            btnAdd.setText("新增地址");
            btnAdd.setOnClickListener(
                    view -> ActivityUtils.launchActivity(mContext,
                            AddAddressInfActivity.class, null,
                            REQUEST_CODE_ADDRESS));

            DividerItemDecoration mDividerItemDecoration
                    = new DividerItemDecoration(getContext(),
                    DividerItemDecoration.VERTICAL_LIST, new ColorDrawable(
                    mContext.getResources().getColor(R.color.line5)));
            mDividerItemDecoration.setHeight(
                    getResources().getDimensionPixelSize(R.dimen.dp_10));
            mRv.addItemDecoration(mDividerItemDecoration);
            mRv.setLayoutManager(new LinearLayoutManager(getContext()));

            mRv.setFocusable(false);
            mRv.addOnItemTouchListener(new SimpleClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                }


                @Override
                public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                }


                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    AddressInformation addressInformation
                            = mAddressInfAdapter.getData().get(position);
                    switch (view.getId()) {
                        //tvName  ivDelete  ivEdit  tvPhone  tvEmail
                        case R.id.ibEdit:
                            //打开编辑联系人页面,同时将数据传递过去
                            ActivityUtils.launchActivity(mContext,
                                    AddAddressInfActivity.class,
                                    AddAddressInfActivity.buildBundle(
                                            addressInformation),
                                    REQUEST_CODE_ADDRESS);

                            break;
                        case R.id.ibDelete:
                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                    mContext).setTitle("确定删除地址信息？")
                                             .setPositiveButton("确定",
                                                     (dialog, which) -> {
                                                         //删除地址
                                                         deleteAddress(
                                                                 addressInformation);
                                                     })
                                             .setNegativeButton("取消", null);
                            alert.setCancelable(false);
                            alert.create();
                            alert.show();
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

            mAddBtn.setText("新增地址");
            mAddBtn.setOnClickListener(view -> ActivityUtils.launchActivity(mContext,
                    AddAddressInfActivity.class, null,
                    REQUEST_CODE_ADDRESS));
        }
    }


    /**
     * 删除地址
     */
    private void deleteAddress(AddressInformation addressInformation) {
        DataManager.deleteAddress(addressInformation.addressId)
                   .compose(bindToLifecycle())
                   .
                           subscribe(getSubscribe(R.string.deleteIng, s -> {
                               mAddressInfAdapter.getData()
                                                 .remove(addressInformation);
                               mAddressInfAdapter.notifyDataSetChanged();
                               if (mAddressInfAdapter.getData().size() == 0) {
                                   mAddressInfAdapter.setEmptyView(
                                           mEmptyViewGroud);
                               }
                               isTopBtnShow();
                               ToastUtils.showLongToast(R.string.deleteSuccess);
                           }));
    }


    @Override protected void initView(View view) {

    }


    public void getData() {
        DataManager.getAddressList(pageNumber)
                   .compose(this.bindToLifecycle())
                   .subscribe(new CObserver<ListData<AddressInformation>>() {
                       @Override public void onPrepare() {

                       }


                       @Override public void onError(ErrorThrowable throwable) {

                           if (mSwipeToLoadLayout.isLoadingMore()) {
                               mSwipeToLoadLayout.setLoadingMore(false);
                           }
                           if (mSwipeToLoadLayout.isRefreshing()) {
                               mSwipeToLoadLayout.setRefreshing(false);
                           }
                           //这一段代码会导致崩掉
                           //if (throwable.code == ReturnCode.LOCAL_NO_NETWORK) {
                           //    toggleNetworkError(true, v -> {
                           //        getData();
                           //    });
                           //}
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
                               mAddressInfAdapter.getData().clear();
                               mAddressInfAdapter.notifyDataSetChanged();
                               mAddressInfAdapter.setEmptyView(mEmptyViewGroud);
                               mLlAdd.setVisibility(View.GONE);
                               mSwipeToLoadLayout.setLoadMoreEnabled(false);
                           }
                           Log.d("result", throwable.msg + "");
                       }


                       @Override
                       public void onSuccess(ListData<AddressInformation> listData) {
                           //if(mSwipeToLoadLayout==null){
                           //    //return;
                           //}
                           if (mSwipeToLoadLayout.isLoadingMore()) {
                               mSwipeToLoadLayout.setLoadingMore(false);
                           }
                           if (mSwipeToLoadLayout.isRefreshing()) {
                               mSwipeToLoadLayout.setRefreshing(false);
                           }
                           List<AddressInformation> list = listData.list;
                           if (pageNumber == 1) {
                               mAddressInfAdapter.setNewData(list);
                           }
                           else {
                               mAddressInfAdapter.addData(list);
                           }
                           isTopBtnShow();
                       }
                   });
    }


    /**
     * 顶部按钮是否显示
     */
    private void isTopBtnShow() {
        if (mAddressInfAdapter.getData().size() == 0) {
            mAddressInfAdapter.setEmptyView(mEmptyViewGroud);
            mLlAdd.setVisibility(View.GONE);
        }
        else {
            mLlAdd.setVisibility(View.VISIBLE);
        }
    }
}
