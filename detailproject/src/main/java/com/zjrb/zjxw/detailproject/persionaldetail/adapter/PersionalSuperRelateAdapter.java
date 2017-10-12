package com.zjrb.zjxw.detailproject.persionaldetail.adapter;

import android.view.ViewGroup;

import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.common.manager.APICallManager;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.zjxw.detailproject.bean.ArticleItemBean;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.task.OfficalDetailTask;

import java.util.List;

/**
 * 个人官员相关新闻列表适配器
 * 支持类型:图文，文字，多图，直播
 * 稿件类型普通新闻、链接新闻、直播
 * 注意:以上是理论支持了类型，实际上根据媒立方，会回传所有类型的稿件
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class PersionalSuperRelateAdapter extends PersionalRelateNewsAdapter implements LoadMoreListener<OfficalDetailBean> {
    private final FooterLoadMore<OfficalDetailBean> mLoadMore;
    private String official_id;

    public PersionalSuperRelateAdapter(OfficalDetailBean datas, ViewGroup parent, String officialId) {
        super(null);
        official_id = officialId;
        mLoadMore = new FooterLoadMore<>(parent, this);
        setFooterLoadMore(mLoadMore.itemView);
        setData(datas);
    }

    public void setData(OfficalDetailBean data) {
        cancelLoadMore();
        mLoadMore.setState(noMore(data) ? LoadMore.TYPE_NO_MORE : LoadMore.TYPE_IDLE);

        setData(data != null ? data.getArticle_list() : null);
    }

    public void addData(List<ArticleItemBean> data) {
        // 全量刷新
        addData(data, false);
        notifyDataSetChanged();
    }

    public boolean noMore(OfficalDetailBean data) {
        return data == null || data.getArticle_list() == null
                || data.getArticle_list().size() < C.PAGE_SIZE;
    }

    public void cancelLoadMore() {
        APICallManager.get().cancel(this);
    }

    @Override
    public void onLoadMoreSuccess(OfficalDetailBean data, LoadMore loadMore) {
        if (noMore(data)) {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
        if (data != null) {
            addData(data.getArticle_list());
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack<OfficalDetailBean> callback) {
        new OfficalDetailTask(callback).setTag(this).exe(official_id, getLastOneTag());
    }

    /**
     * @return 获取最后一次刷新的ID
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