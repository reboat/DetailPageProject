package com.zjrb.zjxw.detailproject.subject.adapter;

import android.view.ViewGroup;

import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.common.manager.APICallManager;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.daily.news.bean.ArticleItemBean;
import com.zjrb.daily.news.ui.adapter.NewsBaseAdapter;
import com.zjrb.zjxw.detailproject.bean.SubjectListBean;
import com.zjrb.zjxw.detailproject.task.DraftTopicListTask;

/**
 * 专题列表 Adapter
 *
 * @author a_liYa
 * @date 2017/10/10 下午5:14.
 */
public class SpecialListAdapter extends NewsBaseAdapter implements LoadMoreListener<SubjectListBean> {

    private FooterLoadMore mLoadMore;
    private String id;

    public SpecialListAdapter(SubjectListBean data, ViewGroup parent, String id) {
        super(null);
        this.id = id;
        mLoadMore = new FooterLoadMore(parent, this);
        setFooterLoadMore(mLoadMore.itemView);

        setData(data);
    }

    public void setData(SubjectListBean data) {
        cancelLoadMore();
        mLoadMore.setState(noMore(data) ? LoadMore.TYPE_NO_MORE : LoadMore.TYPE_IDLE);
        setData(data != null ? data.getArticle_list() : null);
    }

    private boolean noMore(SubjectListBean data) {
        //TODO  WLJ 20条不再作为依据
        return data == null || data.getArticle_list() == null || data.getArticle_list().size() == 0;
//                || data.getArticle_list().size() < C.PAGE_SIZE;
    }

    @Override
    public void onLoadMoreSuccess(SubjectListBean data, LoadMore loadMore) {
        if (data != null) {
            addData(data.getArticle_list(), true);
        }
        if (noMore(data)) {
            mLoadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack callback) {
        new DraftTopicListTask(callback).setTag(this).exe(id, getLastOneTag());
    }

    public void cancelLoadMore() {
        APICallManager.get().cancel(this);
    }

    private Long getLastOneTag() {
        int size = getDataSize();
        if (size > 0) {
            Object o = getData(size - 1);
            if (o instanceof ArticleItemBean) {
                return ((ArticleItemBean) o).getSort_number();
            }
        }
        return null;
    }


}
