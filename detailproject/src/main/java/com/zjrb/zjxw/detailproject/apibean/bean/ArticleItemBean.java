package com.zjrb.zjxw.detailproject.apibean.bean;


/**
 * 稿件Bean
 *
 * Created by wanglinjie.
 * @date 2017/10/10 下午5:26.
 */
public class ArticleItemBean extends com.zjrb.daily.news.bean.ArticleItemBean {

    private static final long serialVersionUID = -8000736659086978442L;
    private int size;
    private int position;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
