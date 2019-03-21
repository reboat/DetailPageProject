package com.zjrb.zjxw.detailproject.ui.persionaldetail.adapter;

import android.view.ViewGroup;

import com.zjrb.core.load.LoadMoreListener;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.FooterLoadMore;
import com.zjrb.core.recycleView.LoadMore;
import com.zjrb.daily.news.ui.adapter.NewsBaseAdapter;
import com.zjrb.zjxw.detailproject.apibean.bean.ArticleItemBean;
import com.zjrb.zjxw.detailproject.apibean.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.apibean.task.OfficalDetailTask;

import cn.daily.news.biz.core.network.compatible.APICallManager;

/**
 * 官员相关新闻 - Adapter
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:12
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
        return data == null || data.getOfficer() == null || data.getOfficer().getArticle_list() == null || data.getOfficer().getArticle_list().size() == 0;
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
    private long getLastOneTag() {
        int size = getDataSize();
        if (size > 0) {
            int count = 1;
            while (size - count >= 0) {
                Object data = getData(size - count++);
                if (data instanceof ArticleItemBean) {
                    return ((ArticleItemBean) data).getSort_number();
                }
            }
        }
        return -1L;
    }
}