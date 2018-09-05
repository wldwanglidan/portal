package com.tempus.portal.ui.finance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.tempus.portal.R;
import com.tempus.portal.base.BasePayActivity;
import com.tempus.portal.base.utils.AppUtils;
import com.tempus.portal.base.utils.MathUtil;
import com.tempus.portal.dao.CObserver;
import com.tempus.portal.dao.ReturnCode;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.model.Base;
import com.tempus.portal.model.OrderInfo;
import com.tempus.portal.model.OrderInfoRequest;

/**
 * Created by Administrator on 2017/9/4.
 */

public class PayActivity extends BasePayActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tvOrderName) TextView mTvOrderName;
    @BindView(R.id.tvOrderPrice) TextView mTvOrderPrice;
    @BindView(R.id.tvPayPrice) TextView mTvPayPrice;
    private OrderInfoRequest mOrderInfoRequest;
    private OrderInfo mOrderInfo;
    private boolean mIsPaySuccess = false;
    private String mMsg;


    public static Bundle buildBundle(OrderInfoRequest orderInfoRequest) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderInfoRequest", orderInfoRequest);
        return bundle;
    }


    @Override protected void getBundleExtras(Bundle extras) {
        mOrderInfoRequest = (OrderInfoRequest) extras.getSerializable(
                "orderInfoRequest");
    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_pay;
    }


    @Override protected View getLoadingTargetView() {
        return ButterKnife.findById(this, android.R.id.content);
    }


    @Override protected void initView(Bundle savedInstanceState) {
        getData();
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("收银台");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @OnClick(R.id.llPay) public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llPay:
                AppUtils.launchTPayApp(mContext, mOrderInfo);
                finish();
                break;
        }
    }


    @Override public void onPaySuccess() {
        mIsPaySuccess = true;
        finish();
        // ToastUtils.showLongToast(R.string.pay_success);
    }


    @Override public void onPayFail(String msg) {
        // ToastUtils.showLongToast(msg);
        mIsPaySuccess = false;
        mMsg = msg;
        finish();
    }


    @Override public void finish() {
        if (mIsPaySuccess) {
            setResult(RESULT_OK,
                    new Intent().putExtra("rps", new Base(0, "", "")));
        }
        else {
            setResult(RESULT_OK,
                    new Intent().putExtra("rps", new Base(1, "支付失败", "")));
        }
        super.finish();
    }


    private void getData() {
        DataManager.getOrderInfo(mOrderInfoRequest)
                   .compose(bindToLifecycle())
                   .subscribe(new CObserver<OrderInfo>() {
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


                       @Override public void onSuccess(OrderInfo orderInfo) {
                           toggleShowLoading(false, "load...");
                           mOrderInfo = orderInfo;
                           mTvOrderName.setText(orderInfo.orderName);
                           mTvOrderPrice.setText(MathUtil.formatFloat2String(
                                   orderInfo.orderPrice));
                           mTvPayPrice.setText(MathUtil.formatFloat2String(
                                   orderInfo.orderPrice));
                       }
                   });
    }
}

