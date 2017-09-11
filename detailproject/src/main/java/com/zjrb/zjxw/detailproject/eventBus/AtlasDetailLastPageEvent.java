package com.zjrb.zjxw.detailproject.eventBus;


import com.zjrb.core.domain.eventbus.EventBase;

/**
 * 评论个数回调事件
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class AtlasDetailLastPageEvent extends EventBase<String> {

    public AtlasDetailLastPageEvent(String isLastPage) {
        super(isLastPage);
    }

}
