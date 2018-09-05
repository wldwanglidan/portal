package com.tempus.portal.model;

/**
 * Created by Administrator on 2017/9/4.
 */

public class HomeNavigation {
    public int image;
    public String name;
    public boolean isUnread=false;


    public HomeNavigation (int image, String name) {
        this.image = image;
        this.name = name;
    }
}
