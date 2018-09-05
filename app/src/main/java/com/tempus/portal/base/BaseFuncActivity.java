package com.tempus.portal.base;

import com.tempus.portal.base.utils.ProgressDialogUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.dao.CObserver;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * @author jianhao025@gmail.com
 * @data: 2016/07/27 08:59
 * @version: V1.0
 */
public abstract class BaseFuncActivity extends SupportActivity {

    //异步操作对话框
    public ProgressDialogUtils mProgressDialogUtils;


    public BaseFuncActivity() {
        mProgressDialogUtils = new ProgressDialogUtils(this);
    }


    public <T> CObserver<T> getSubscribe(OnSubscribeSuccess<T> onSubscribeSuccess) {
        return getSubscribe(0, onSubscribeSuccess);
    }


    public <T> CObserver<T> getSubscribe(int loadingRes, OnSubscribeSuccess<T> onSubscribeSuccess) {
        return new CObserver<T>() {
            @Override public void onPrepare() {
                if (loadingRes != -1) {
                    if (loadingRes == 0) {
                        mProgressDialogUtils.showProgress();
                    }
                    else {
                        mProgressDialogUtils.showProgress(loadingRes);
                    }
                }
            }


            @Override public void onError(ErrorThrowable throwable) {
                ToastUtils.showLongToast(throwable.msg);
                if (loadingRes != -1) {
                    mProgressDialogUtils.hideProgress();
                }
            }


            @Override public void onSuccess(T t) {
                if (loadingRes != -1) {
                    mProgressDialogUtils.hideProgress();
                }
                if (onSubscribeSuccess != null) onSubscribeSuccess.onSuccess(t);
            }
        };
    }

    public <T> CObserver<T> getNotProSubscribe(OnSubscribeSuccess<T>
                                                    onSubscribeSuccess) {
        return new CObserver<T>() {
            @Override public void onPrepare() {

            }


            @Override public void onError(ErrorThrowable throwable) {
                mProgressDialogUtils.hideProgress();
                ToastUtils.showLongToast(throwable.msg);

            }


            @Override public void onSuccess(T t) {
                mProgressDialogUtils.hideProgress();
                if (onSubscribeSuccess != null) onSubscribeSuccess.onSuccess(t);
            }
        };
    }
    @Override protected void onDestroy() {
        mProgressDialogUtils.hideProgress();
        super.onDestroy();
    }


    public interface OnSubscribeSuccess<T> {
        void onSuccess(T t);
    }
}
