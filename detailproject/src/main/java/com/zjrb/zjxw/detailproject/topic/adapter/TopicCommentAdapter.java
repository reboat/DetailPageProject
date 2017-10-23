package com.zjrb.zjxw.detailproject.topic.adapter;

import android.view.ViewGroup;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.holder.DetailCommentHolder;

import java.util.List;

/**
 * 话题/普通详情页评论Adapter
 * Created by wanglinjie.
 * create time:2017/7/19  上午10:14
 */
public class TopicCommentAdapter extends BaseRecyclerAdapter {

    private String articleId;

    public TopicCommentAdapter(List<HotCommentsBean> datas, String articleId) {
        super(datas);
        this.articleId = articleId;
    }


    @Override
    public DetailCommentHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        return new DetailCommentHolder(UIUtils.inflate(R.layout.module_detail_item_comment, parent, false), articleId);
    }
}