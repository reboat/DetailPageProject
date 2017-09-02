package com.zjrb.zjxw.detailproject.eventBus;


import com.zjrb.core.domain.eventbus.EventBase;
import com.zjrb.zjxw.detailproject.bean.SubjectNewsBean;

/**
 * 点击专题channel item
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class ChannelItemClickEvent extends EventBase<SubjectNewsBean.GroupArticlesBean> {

    public ChannelItemClickEvent(SubjectNewsBean.GroupArticlesBean data) {
        super(data);
    }

}
