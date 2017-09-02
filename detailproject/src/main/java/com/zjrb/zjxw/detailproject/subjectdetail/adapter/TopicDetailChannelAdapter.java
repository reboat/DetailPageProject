package com.zjrb.zjxw.detailproject.subjectdetail.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.bean.SubjectNewsBean;

import java.util.List;


/**
 * 专题详情页频道列表
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */

public final class TopicDetailChannelAdapter extends BaseRecyclerAdapter {

    public TopicDetailChannelAdapter(List data) {
        super(data);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        return new DetailTopicChannelViewHolder(UIUtils.inflate(R.layout.module_detail_topic_channel_item, parent, false));
    }

    /**
     * 频道holder
     */
    public class DetailTopicChannelViewHolder extends BaseRecyclerViewHolder<SubjectNewsBean.GroupArticlesBean>{
        /**
         * 频道名称
         */
        private TextView tv_channel_name;

        public DetailTopicChannelViewHolder(View itemView) {
            super(itemView);
            tv_channel_name = (TextView) itemView.findViewById(R.id.tv_channel_name);
        }

        @Override
        public void bindView() {
            tv_channel_name.setText(mData.getGroupName());
        }
    }

}
