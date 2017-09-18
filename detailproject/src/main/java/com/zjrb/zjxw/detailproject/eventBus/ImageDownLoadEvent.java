package com.zjrb.zjxw.detailproject.eventBus;


import com.zjrb.core.domain.eventbus.EventBase;

/**
 * 下载图片
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class ImageDownLoadEvent extends EventBase<Integer> {

    public ImageDownLoadEvent(int position) {
        super(position);
    }

}
