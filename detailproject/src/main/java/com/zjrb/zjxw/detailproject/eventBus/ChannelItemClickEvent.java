package com.zjrb.zjxw.detailproject.eventBus;


import com.zjrb.core.domain.eventbus.EventBase;

/**
 * 点击专题channel item
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class ChannelItemClickEvent extends EventBase<Integer> {

    public ChannelItemClickEvent(int groupId) {
        super(groupId);
    }

}
