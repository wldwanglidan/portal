package com.tempus.portal.dao;

/**
 *
 * @author jianhao025@gmail.com
 * @data: 2016/07/27 08:59
 * @version: V1.0
 */
public interface DataListener<T> {

    void onSuccess(T t);

    void onFail(int code, String failMsg);

}
