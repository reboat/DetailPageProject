package com.zjrb.zjxw.detailproject.task;


import com.zjrb.coreprojectlibrary.api.base.APIPostTask;
import com.zjrb.coreprojectlibrary.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.SubjectListBean;

/**
 * 稿件专题分组列表 - Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class DraftTopicListTask extends APIPostTask<SubjectListBean> {

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
        put("group_id", params[0]);
        if (!params[1].equals("")) {
            put("start", params[1]);
        }
        put("size", params[2]);
    }

    @Override
    protected String getApi() {
        return "";
//        return APIManager.endpoint.DRAFT_DETAIL;
    }
}
