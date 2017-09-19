package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.SubjectListBean;
import com.zjrb.zjxw.detailproject.global.APIManager;

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
     * @param params id:稿件id(int)
     *               group_id:专题分组id(int)
     *               start:最后一条的时间戳(long)
     *               size:分页条数(int)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("group_id", "5");
        put("start", params[1]);
        put("size", params[2]);
    }

    @Override
    protected String getApi() {
        return APIManager.endpoint.SUBJECT_LIST;
    }
}
