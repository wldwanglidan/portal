package com.tempus.portal.dao;

import android.util.Log;
import com.google.gson.JsonParseException;
import com.tempus.portal.R;
import com.tempus.portal.app.AppContext;
import com.tempus.portal.app.Constant;
import com.tempus.portal.dao.exception.EncryptException;
import com.tempus.portal.dao.exception.NoNetworkException;
import com.tempus.portal.dao.exception.RefreshLoginException;
import com.tempus.portal.dao.retrofit.ErrorThrowable;
import com.tempus.portal.manager.TokenManager;
import com.tempus.portal.model.BaseResponse;
import com.tempus.portal.model.ListData;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Function;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLHandshakeException;

/**
 * @author jianhao025@gmail.com
 * @data: 2016/07/27 08:59
 * @version: V1.0
 */
public class ResponseHandle {

    private static final String TAG = "ResponseHandle";

    //请求token失败重连的次数
    private static final int RETRY_TIME = 4;


    private static String getString(int resId) {
        return AppContext.getString(resId);
    }


    public static <T> Function<Throwable, ? extends Observable<? extends T>> errorResumeFunc() {
        return throwable -> {
            if (throwable instanceof JsonParseException) {
                return Observable.error(
                        new ErrorThrowable(ReturnCode.LOCAL_GSON_ERROR,
                                getString(Constant.DEBUG
                                          ? R.string.error_gson_parse_error
                                          : R.string.error_system_error)));
            }
            else if (throwable instanceof UnknownHostException ||
                    throwable instanceof ConnectException ||
                    throwable instanceof SocketTimeoutException) {
                //                return Observable.error(new ErrorThrowable(ReturnCode.LOCAL_UNKNOWN_HOST_ERROR, AppContext.getString(R.string.error_unknown_host)));
                return Observable.error(
                        new ErrorThrowable(ReturnCode.LOCAL_UNKNOWN_HOST_ERROR,
                                Constant.DEBUG
                                ? throwable.toString()
                                : AppContext.getString(
                                        R.string.error_unknown)));
            }
            else if (throwable instanceof NoNetworkException) {
                return Observable.error(
                        new ErrorThrowable(ReturnCode.LOCAL_NO_NETWORK,
                                AppContext.getString(R.string.no_network)));
            }
            else if (throwable instanceof EncryptException) {
                return Observable.error(
                        new ErrorThrowable(ReturnCode.LOCAL_ENCRYPT_ERROR,
                                AppContext.getString(R.string.error_encrypt)));
            }
            else if (throwable instanceof RefreshLoginException) {
                return Observable.error(
                        new ErrorThrowable(ReturnCode.LOCAL_REFRESH_LOGIN_ERROR,
                                AppContext.getString(
                                        R.string.error_login_timeout)));
            }
            else if (throwable instanceof SSLHandshakeException) {
                return Observable.error(
                        new ErrorThrowable(ReturnCode.LOCAL_NO_NETWORK,
                                AppContext.getString(R.string.no_network)));
            }
            else if (throwable instanceof ErrorThrowable) {
                return Observable.error(throwable);
            }
            else {
                //                return Observable.error(new ErrorThrowable(ReturnCode.LOCAL_UNKNOWN_ERROR, AppContext.getString(R.string.error_unknown)));
                return Observable.error(
                        new ErrorThrowable(ReturnCode.LOCAL_UNKNOWN_ERROR,
                                Constant.DEBUG
                                ? (throwable == null
                                   ? AppContext.getString(
                                        R.string.error_unknown)
                                   : throwable.toString())
                                : AppContext.getString(
                                        R.string.error_unknown)));
            }
        };
    }


    //是否重试
    public static BiPredicate<Integer, Throwable> canRetry() {
        return (retryTime, throwable) -> {
            boolean retry = false;
            if (retryTime > ResponseHandle.RETRY_TIME) {
                retry = false;
            }
            else {
                if (throwable instanceof ErrorThrowable) {
                    retry = true;
                    ErrorThrowable errorThrowable = (ErrorThrowable) throwable;
                    switch (errorThrowable.code) {
                        case ReturnCode.CODE_INVALIDATED_TOKEN:  //token失效
                            Log.e(TAG, "token失效");
                            TokenManager.getInstance().resetToken();
                            break;
                        case ReturnCode.CODE_INVALIDATED_LOGIN:  //登录信息过期
                            Log.e(TAG, "登录信息失效");
                            TokenManager.getInstance().setRefreshLogin();
                            break;
                        //case ReturnCode.CODE_INVALIDATED_APPID:  //AppId无效
                        //    Log.e(TAG, "AppId无效");
                        //    TokenManager.getInstance().resetToken();
                        //    break;
                        default:
                            retry = false;
                            break;
                    }
                }
            }
            return retry;
        };
    }


    // 读取实体数据
    private static class ReadDataFunc<E> implements Function<E, Observable<E>> {
        @Override public Observable<E> apply(@NonNull E x) throws Exception {
            BaseResponse baseResponse = ((BaseResponse) x);
            if (baseResponse.retStatus == ReturnCode.CODE_SUCCESS) {
                return Observable.just(x);
            }
            else {
                return Observable.error(
                        new ErrorThrowable(baseResponse.retStatus,
                                baseResponse.errorMsg));
            }
        }
    }

    // 读取列表数据
    public static class ReadListFunc<E>
            implements Function<ListData<E>, Observable<ListData<E>>> {
        @Override public Observable<ListData<E>> apply(@NonNull ListData<E> x)
                throws Exception {
            if (x.retStatus == ReturnCode.CODE_SUCCESS) {
                if (x.list != null && x.list.size() > 0) {
                    return Observable.just(x);
                }
                else {
                    return Observable.error(
                            new ErrorThrowable(ReturnCode.CODE_EMPTY,
                                    x.errorMsg));
                }
            }
            else {
                return Observable.error(
                        new ErrorThrowable(x.retStatus, x.errorMsg));
            }
        }
    }


    //新建处理实体类型数据
    public static ReadDataFunc newEntityData() {
        return new ReadDataFunc();
    }


    //新建处理列表类型数据
    public static ReadListFunc newListData() { return new ReadListFunc(); }
}
