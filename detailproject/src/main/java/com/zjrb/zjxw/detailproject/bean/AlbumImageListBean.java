package com.zjrb.zjxw.detailproject.bean;


import java.io.Serializable;

/**
 * 图集
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */

public class AlbumImageListBean implements Serializable {
    private static final long serialVersionUID = -3208332003586917497L;
    private String image_url;
    private String description;

    public String getImage_url() {
        return image_url;
    }


    public String getDescription() {
        return description;
    }

}
