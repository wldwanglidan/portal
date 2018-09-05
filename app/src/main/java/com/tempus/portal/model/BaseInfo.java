package com.tempus.portal.model;

/**
 * <基础类>
 *
 * @author fuxj
 * @data: 15/11/10 下午7:32
 * @version: V1.0
 */
public abstract class BaseInfo {

    //实体类返回提示语
    private String successHintMsg;

    public void setSussceHintMsg(String msg) {
        this.successHintMsg = msg;
    }

    public String getSuccessHintMsg() {
        return successHintMsg;
    }
}
