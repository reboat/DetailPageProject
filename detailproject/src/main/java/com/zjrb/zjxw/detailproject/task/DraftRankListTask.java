package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.DraftHotTopNewsBean;
import com.zjrb.zjxw.detailproject.global.APIManager;

/**
 * 撤稿获取频道的热门新闻
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */

public class DraftRankListTask extends APIGetTask<DraftHotTopNewsBean> {
    public DraftRankListTask(LoadingCallBack<DraftHotTopNewsBean> callBack) {
        super(callBack);
    }

    /**
     * @param params column_id:栏目id(int)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("channel_id", "739716");
    }

    @Override
    protected String getApi() {
        return APIManager.endpoint.GET_RANK_LIST;
    }
}
