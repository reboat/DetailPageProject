package com.zjrb.zjxw.detailproject.persionaldetail.adapter;

import android.view.ViewGroup;

import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.common.manager.APICallManager;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.daily.news.ui.adapter.NewsBaseAdapter;
import com.zjrb.zjxw.detailproject.bean.ArticleItemBean;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.task.OfficalDetailTask;

/**
 * 官员相关新闻 - Adapter
 *
 * @author a_liYa
 * @date 2017/10/15 下午4:06.
 */
public class OfficerRelatedNewsAdapter extends NewsBaseAdapter implements
        LoadMoreListener<OfficalDetailBean> {

    private String official_id;
    private final FooterLoadMore<OfficalDetailBean> mLoadMore;

    public OfficerRelatedNewsAdapter(OfficalDetailBean data, ViewGroup parent, String officialId) {
        super(null);
        official_id = officialId;
        mLoadMore = new FooterLoadMore<>(parent, this);
        setFooterLoadMore(mLoadMore.itemView);
        setData(data);
    }

    public void setData(OfficalDetailBean data) {
        cancelLoadMore();
        mLoadMore.setState(noMore(data) ? LoadMore.TYPE_NO_MORE : LoadMore.TYPE_IDLE);
        setData((data != null && data.getOfficer() != null && data.getOfficer().getArticle_list() != null) ? data.getOfficer().getArticle_list() : null);
    }

    public boolean noMore(OfficalDetailBean data) {
        //TODO 20条将不再作为无数据的依据
        return data == null || data.getOfficer() == null || data.getOfficer().getArticle_list() == null || data.getOfficer().getArticle_list().size() == 0;
//                || data.getOfficer().getArticle_list().size() < C.PAGE_SIZE;
    }

    public void cancelLoadMore() {
        APICallManager.get().cancel(this);
    }

    @Override
    public void onLoadMoreSuccess(OfficalDetailBean data, LoadMore loadMore) {
        if (noMore(data)) {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
        if (data != null && data.getOfficer() != null && data.getOfficer().getArticle_list() != null && data.getOfficer().getArticle_list().size() > 0) {
            addData(data.getOfficer().getArticle_list(), true);
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack<OfficalDetailBean> callback) {
        new OfficalDetailTask(callback).setTag(this).exe(official_id, getLastOneTag());
    }

    /**
     * 获取列表最后一条数据的官员ID
     *
     * @return
     */
    private int getLastOneTag() {
        int size = getDataSize();
        if (size > 0) {
            int count = 1;
            while (size - count >= 0) {
                Object data = getData(size - count++);
                if (data instanceof ArticleItemBean) {
                    return ((ArticleItemBean) data).getId();
                }
            }
        }
        return -1;
    }
}