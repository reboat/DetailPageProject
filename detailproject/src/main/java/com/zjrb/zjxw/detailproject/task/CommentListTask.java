package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.global.C;
import com.zjrb.zjxw.detailproject.bean.CommentRefreshBean;
import com.zjrb.zjxw.detailproject.global.APIManager;

/**
 * 文章评论列表 Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class CommentListTask extends APIGetTask<CommentRefreshBean> {

    /**
     * 是否是精选评论
     */
    private boolean is_select_list = false;

    public CommentListTask(LoadingCallBack<CommentRefreshBean> callBack, boolean is_select_list) {
        super(callBack);
        this.is_select_list = is_select_list;
    }

    /**
     * @param params channel_article_id：稿件id(int)
     *               start：最后一条评论时间戳(long)
     *               size：分页条数(int)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("channel_article_id", params[0]);
        if (params.length > 1) {
            put("start", params[1]);
        }
        put("size", C.PAGE_SIZE);
    }

    @Override
    protected String getApi() {
        if (is_select_list) {
            return APIManager.endpoint.SELECT_LIST;
        } else {
            return APIManager.endpoint.COMMENT_LIST;
        }

    }
}
