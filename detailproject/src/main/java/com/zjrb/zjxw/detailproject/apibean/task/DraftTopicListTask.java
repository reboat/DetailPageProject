package com.zjrb.zjxw.detailproject.apibean.task;



import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.apibean.bean.SubjectListBean;
import com.zjrb.zjxw.detailproject.utils.global.APIManager;

import cn.daily.news.biz.core.constant.C;
import cn.daily.news.biz.core.network.compatible.APIGetTask;

/**
 * 稿件专题分组列表 - Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class DraftTopicListTask extends APIGetTask<SubjectListBean> {

    public DraftTopicListTask(LoadingCallBack<SubjectListBean> callBack) {
        super(callBack);
    }

    /**
     * @param params group_id:专题分组id(int)
     *               start:最后一条的时间戳(long)
     *               size:分页条数(int)
     */
    @Override
    public void onSetupParams(Object... params) {
        put("group_id", params[0]);
        if (params.length > 1) {
            put("start", params[1]);
        }
        put("size", C.PAGE_SIZE);
    }

    @Override
    public String getApi() {
        return APIManager.endpoint.SUBJECT_LIST;
    }
}
