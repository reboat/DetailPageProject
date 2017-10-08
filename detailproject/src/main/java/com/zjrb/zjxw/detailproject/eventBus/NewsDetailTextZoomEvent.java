package com.zjrb.zjxw.detailproject.eventBus;


import com.zjrb.core.domain.eventbus.EventBase;

/**
 * 详情页设置字体大小
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsDetailTextZoomEvent extends EventBase<Float> {

    public NewsDetailTextZoomEvent(float size) {
        super(size);
    }

}
