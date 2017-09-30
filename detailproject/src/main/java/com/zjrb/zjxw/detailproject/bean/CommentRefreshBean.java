package com.zjrb.zjxw.detailproject.bean;


import com.zjrb.core.domain.base.BaseData;

import java.util.List;

/**
 * 评论列表bean
 * Created by wanglinjie.
 * create time:2017/7/21  下午4:20
 */

public class CommentRefreshBean extends BaseData {


    /**
     * id : 59817b9991f2af09dc48db1d
     * channel_article_id : 27
     * content : 教育部要求减轻学生过重暑假学业负担 禁止组织补课
     * nick_name : 读者+WO38eX
     * created_at : 1501658010000
     * like_count : 0
     * account_id : 598415f1f9253e3ff8eba88c
     * status : 2
     * liked : false
     * portrait_url : https://www.baidu.com/s?rsv_idx=1&wd=%E8%BD%AF%E4%BB%B6%E6%9E%B6%E6%9E%84%E8%AE%BE%E8%AE%A1&ie=utf-8&rsv_cq=mybatis+%E6%89%B9%E5%A4%84%E7%90%86&rsv_dl=0_right_recommends_merge_21180&euri=ac7ba1cfb121481483f05744f83a726e
     * parent_id : 597ad67b2c1d4007315ce9b1
     * parent_content : 浙江
     * parent_nick_name : 读者+QONqts
     * parent_created_at : 1501222523000
     * parent_like_count : 1
     * parent_account_id : 59841424f9253e20e02c186c
     * parent_liked : false
     * parent_portrait_url : https://www.baidu.com/s?rsv_idx=1&wd=%E8%BD%AF%E4%BB%B6%E6%9E%B6%E6%9E%84%E8%AE%BE%E8%AE%A1&ie=utf-8&rsv_cq=mybatis+%E6%89%B9%E5%A4%84%E7%90%86&rsv_dl=0_right_recommends_merge_21180&euri=ac7ba1cfb121481483f05744f83a726e
     */

    private List<HotCommentsBean> comments;

    public List<HotCommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<HotCommentsBean> comments) {
        this.comments = comments;
    }

}
