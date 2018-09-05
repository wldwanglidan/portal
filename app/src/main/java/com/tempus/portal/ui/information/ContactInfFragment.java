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
import com.tempus.portal.model.information.ContactInformation;
import com.tempus.portal.view.adapter.ContactInfAdapter;
import com.tempus.portal.view.widget.DividerItemDecoration;
import java.util.List;

import static com.tempus.portal.ui.information.ComInformationActivity.REQUEST_CODE_CONTACT;

/**
 * Created by Administrator on 2017/1/3.
 */

public class ContactInfFragment extends BaseFragment
        implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.swipe_target) RecyclerView mRv;
    @BindView(R.id.swipeToLoadLayout) SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.btnContact) Button mAddBtn;
    @BindView(R.id.llAddBtn) LinearLayout mLlAdd;
    public ContactInfAdapter mContactInfAdapter;
    private View mEmptyViewGroud;

    private int pageNumber = 1;
    private String mStatus;


    public static ContactInfFragment newInstance(String status) {
        ContactInfFragment fragment = new ContactInfFragment();
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


    @Override protected int getContentViewID() {
        return R.layout.fragment_contact_inf;
    }


    //protected abstract void initView(View view); 父类的定义是这样的
    //@Override protected void initView(Bundle savedInstanceState) {
    //
    //}
    @Override public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        if (savedInstanceState == null) {
            getExtra();
            mSwipeToLoadLayout.setOnLoadMoreListener(this);
            mSwipeToLoadLayout.setOnRefreshListener(this);
            mContactInfAdapter = new ContactInfAdapter();
            mContactInfAdapter.openLoadAnimation();
            mRv.setAdapter(mContactInfAdapter);
            mLlAdd.setVisibility(View.GONE);
            mEmptyViewGroud = getActivity().getLayoutInflater()
                                           .inflate(
                                                   R.layout.layout_infempty_view,
                                                   (ViewGroup) mRv.getParent(),
                                                   false);
            TextView tvEmptyContent = mEmptyViewGroud.findViewById(
                    R.id.tvEmptyContent);

            tvEmptyContent.setText("暂无常用联系人信息");

            TextView tvEmpty = mEmptyViewGroud.findViewById(R.id.tvEmpty);
            tvEmpty.setText("添加常用联系人信息，预定更方便快捷！");
            Button btnAdd = mEmptyViewGroud.findViewById(R.id.btnAdd);
            btnAdd.setText("新增联系人");
            btnAdd.setOnClickListener(
                    view -> ActivityUtils.launchActivity(mContext,
                            AddContactInfActivity.class, null,
                            REQUEST_CODE_CONTACT));
            //设置列表分割线
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
                    //ActivityUtils.launchActivity(getActivity(),
                    //        OrderDetailsActivity.class,
                    //        OrderDetailsActivity.buildBundle(
                    //                mMyOrderAdapter.getData().get(position).id));
                }


                @Override
                public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                }


                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    ContactInformation contactInformation
                            = mContactInfAdapter.getData().get(position);
                    switch (view.getId()) {
                        case R.id.ibEdit:
                            //打开编辑联系人页面,同时将数据传递过去
                            //ActivityUtils.launchActivity(mContext,
                            //        AddContactInfActivity.class,
                            //        AddContactInfActivity.buildBundle(
                            //                contactInformation));
                            ActivityUtils.launchActivity(mContext,
                                    AddContactInfActivity.class,
                                    AddContactInfActivity.buildBundle(
                                            contactInformation),
                                    REQUEST_CODE_CONTACT);

                            break;
                        case R.id.ibDelete:
                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                    mContext).setTitle("确定删除联系人信息？")
                                             .setPositiveButton("确定",
                                                     (dialog, which) -> {
                                                         //删除联系人
                                                         deleteContact(
                                                                 contactInformation);
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
            mSwipeToLoadLayout.setRefreshing(true);//
            mAddBtn.setText("新增联系人");
            mAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    ActivityUtils.launchActivity(mContext,
                            AddContactInfActivity.class, null,
                            REQUEST_CODE_CONTACT);
                }
            });
        }
    }


    /**
     * 删除联系人
     */
    private void deleteContact(ContactInformation contactInformation) {

        DataManager.deleteContact(contactInformation.contactId)
                   .compose(bindToLifecycle())
                   .
                           subscribe(getSubscribe(R.string.deleteIng, s -> {
                               mContactInfAdapter.getData()
                                                 .remove(contactInformation);
                               mContactInfAdapter.notifyDataSetChanged();
                               isTopBtnShow();
                               ToastUtils.showLongToast(R.string.deleteSuccess);
                           }));
    }


    @Override protected void initView(View view) {

    }


    public void getData() {
        Log.d("result", "啊啊啊");
        DataManager.getContactList(pageNumber)
                   .compose(this.bindToLifecycle())
                   .subscribe(new CObserver<ListData<ContactInformation>>() {
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
                               mContactInfAdapter.getData().clear();
                               mContactInfAdapter.notifyDataSetChanged();
                               mContactInfAdapter.setEmptyView(mEmptyViewGroud);
                               mLlAdd.setVisibility(View.GONE);
                               mSwipeToLoadLayout.setLoadMoreEnabled(false);
                           }
                           //Log.d("result", throwable.msg + "");
                       }


                       @Override
                       public void onSuccess(ListData<ContactInformation> listData) {
                           //if(mSwipeToLoadLayout==null){
                           //    //return;
                           //}
                           if (mSwipeToLoadLayout.isLoadingMore()) {
                               mSwipeToLoadLayout.setLoadingMore(false);
                           }
                           if (mSwipeToLoadLayout.isRefreshing()) {
                               mSwipeToLoadLayout.setRefreshing(false);
                           }
                           List<ContactInformation> list = listData.list;

                           if (pageNumber == 1) {
                               mContactInfAdapter.setNewData(list);
                           }
                           else {
                               mContactInfAdapter.addData(list);
                           }
                           isTopBtnShow();
                       }
                   });
    }


    /**
     * 顶部按钮是否显示
     */
    private void isTopBtnShow() {
        if (mContactInfAdapter.getData().size() == 0) {
            mContactInfAdapter.setEmptyView(mEmptyViewGroud);
            mLlAdd.setVisibility(View.GONE);
        }
        else {
            mLlAdd.setVisibility(View.VISIBLE);
        }
    }
}
