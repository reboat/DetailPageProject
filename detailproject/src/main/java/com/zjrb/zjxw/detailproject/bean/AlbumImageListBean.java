package com.zjrb.zjxw.detailproject.bean;


import com.zjrb.core.domain.base.BaseInnerData;

/**
 * 图集
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */

public class AlbumImageListBean extends BaseInnerData {
    private String image_url;
    private String description;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
