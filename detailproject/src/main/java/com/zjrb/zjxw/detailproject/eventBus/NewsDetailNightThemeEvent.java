package com.zjrb.zjxw.detailproject.eventBus;


import com.zjrb.core.domain.eventbus.EventBase;

/**
 * 详情页设置夜间模式
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsDetailNightThemeEvent extends EventBase<Boolean> {

    public NewsDetailNightThemeEvent(boolean flag) {
        super(flag);
    }

}
