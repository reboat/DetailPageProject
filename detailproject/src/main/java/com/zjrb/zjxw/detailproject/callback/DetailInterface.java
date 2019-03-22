package com.zjrb.zjxw.detailproject.callback;

import android.content.Intent;

/**
 * 详情页内部接口
 * Created by wanglinjie.
 * create time:2019/3/19  下午2:28
 */
public interface DetailInterface {
    //订阅同步
    interface SubscribeSyncInterFace {
        void subscribeSync(Intent intent);
    }

    //视频打开/关闭回调
    interface VideoBCnterFace {
        void videoBC(Intent intent);
    }

    //网络监听
    interface NetWorkInterFace {
        void networkBC(Intent intent);
    }
}
