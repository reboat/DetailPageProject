package com.zjrb.zjxw.detailproject.apibean.task;


import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftHotTopNewsBean;
import com.zjrb.zjxw.detailproject.utils.global.APIManager;

import cn.daily.news.biz.core.network.compatible.APIGetTask;

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
    public void onSetupParams(Object... params) {
    }

    @Override
    public String getApi() {
        return APIManager.endpoint.GET_RANK_LIST;
    }
}
