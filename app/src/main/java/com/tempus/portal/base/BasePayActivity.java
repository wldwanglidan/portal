package com.tempus.portal.base;

import android.content.Intent;
import android.os.Bundle;
import com.tempus.portal.R;
import com.tempus.portal.model.WXRechargeResultEvent;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.tempus.portal.app.Constant.REQUEST_CODE_T_PAY;

/**
 * @author jianhao025@gmail.com
 * @data: 2016/07/27 08:59
 * @version: V1.0
 */
public abstract class BasePayActivity extends BaseActivity {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxEvent(WXRechargeResultEvent resultEvent) {
        switch (resultEvent.errCode) {
            case BaseResp.ErrCode.ERR_OK://正确返回
                onPaySuccess();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED: //认证被否决
                onPayFail(getString(R.string.pay_failure));
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL: //用户取消
                onPayFail(getString(R.string.sc_pay_result_cancel_pay));
                break;
            case BaseResp.ErrCode.ERR_COMM: //一般错误
                onPayFail(getString(R.string.pay_failure));
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED://发送失败
                onPayFail(getString(R.string.pay_failure));
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT://不支持错误
                onPayFail(getString(R.string.sc_not_support_pay));
                break;
            default:
                onPayFail(getString(R.string.pay_failure));
                break;
        }
    }


    @Override public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //public interface OnPayListener {
    //    void onPaySuccess();
    //}


    public abstract void onPaySuccess();
    public abstract void onPayFail(String msg);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_T_PAY) {
            boolean isSuccess = data.getBooleanExtra("isSuccess", false);
            if (isSuccess) {
                onPaySuccess();
            }
            else {
                onPayFail(getString(R.string.pay_failure));
            }
        }
    }
}
