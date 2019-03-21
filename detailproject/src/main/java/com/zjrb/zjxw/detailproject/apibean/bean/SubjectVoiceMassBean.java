package com.zjrb.zjxw.detailproject.apibean.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 群众之声列表
 * Created by wanglinjie.
 * create time:2019/3/13  下午2:06
 */
public class SubjectVoiceMassBean implements Serializable {
    private static final long serialVersionUID = -8437686263821847163L;

    private List<HotCommentsBean> comment_list;

    public List<HotCommentsBean> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<HotCommentsBean> comment_list) {
        this.comment_list = comment_list;
    }
}
