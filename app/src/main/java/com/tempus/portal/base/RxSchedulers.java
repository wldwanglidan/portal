package com.tempus.portal.base;

import com.tempus.portal.dao.ResponseHandle;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/7/29.
 */
public class RxSchedulers {
    public static <T> ObservableTransformer<T, T> io_main() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io()).unsubscribeOn(
                Schedulers.computation())
                                         .observeOn(
                                                 AndroidSchedulers.mainThread())
                                         .onErrorResumeNext(
                                                 ResponseHandle.errorResumeFunc());
    }
}
