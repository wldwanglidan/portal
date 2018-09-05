package com.tempus.portal.dao.retrofit;

/**
 *
 * @author jianhao025@gmail.com
 * @data: 2016/07/27 08:59
 * @version: V1.0
 */
public class ErrorThrowable extends Throwable {

    public int code;

    public String msg;

    public ErrorThrowable(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "code:" + code + ", msg:" + msg;
    }
}
