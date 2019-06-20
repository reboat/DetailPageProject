package com.zjrb.zjxw.detailproject.apibean.bean;

import java.io.Serializable;

/**
 * 详情页内分享bean
 * Created by wanglinjie.
 * create time:2017/7/21  下午4:20
 */
public final class DetailShareItemBean implements Serializable {
    public DraftDetailBean draftDetailBean;

    public DetailShareItemBean(DraftDetailBean draftDetailBean) {
        this.draftDetailBean = draftDetailBean;
    }
}