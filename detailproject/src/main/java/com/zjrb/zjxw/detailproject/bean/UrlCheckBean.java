package com.zjrb.zjxw.detailproject.bean;

import java.io.Serializable;

/**
 * url检测请求
 * Created by wanglinjie.
 * create time:2019/2/28  上午9:00
 */
public class UrlCheckBean implements Serializable {
    private static final long serialVersionUID = 1132609864021533004L;
    private String pass; // true 通过 false 不通过

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
