package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.ChannelBean;
import com.zjrb.zjxw.detailproject.global.APIManager;

/**
 * 撤稿获取频道的热门新闻
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */

public class GetChannelListTask extends APIPostTask<ChannelBean> {
    public GetChannelListTask(LoadingCallBack<ChannelBean> callBack) {
        super(callBack);
    }

    /**
     * @param params column_id:栏目id(int)
     *               do_subscribe:订阅/取消订阅(boolean)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("channel_id", params[0]);
        put("start", params[1]);
        put("size", params[1]);
    }

    @Override
    protected String getApi() {
        return APIManager.endpoint.GET_CHANNEL_LIST;
    }
}
