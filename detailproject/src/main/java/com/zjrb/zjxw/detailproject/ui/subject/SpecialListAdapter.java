package com.zjrb.zjxw.detailproject.ui.subject;

import android.support.v7.widget.RecyclerView;

import com.zjrb.core.load.LoadMoreListener;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.FooterLoadMoreV2;
import com.zjrb.core.recycleView.LoadMore;
import com.zjrb.daily.news.bean.ArticleItemBean;
import com.zjrb.daily.news.bean.DataArticleList;
import com.zjrb.daily.news.other.Page;
import com.zjrb.daily.news.ui.adapter.NewsBaseAdapter;
import com.zjrb.zjxw.detailproject.apibean.bean.SubjectListBean;
import com.zjrb.zjxw.detailproject.apibean.task.DraftTopicListTask;

import java.util.List;

/**
 * @author: lujialei
 * @date: 2019/3/7
 * @describe:
 */


public class SpecialListAdapter extends NewsBaseAdapter implements LoadMoreListener<SubjectListBean> {
    private FooterLoadMoreV2<SubjectListBean> mLoadMore;
    private String groupId;
    public SpecialListAdapter(RecyclerView parent,List datas,String groupId) {
        super(datas);
        this.groupId = groupId;
        mLoadMore = new FooterLoadMoreV2<>(parent, this);
        setFooterLoadMore(mLoadMore.itemView);
    }

    @Override
    public void onLoadMoreSuccess(SubjectListBean data, LoadMore loadMore) {
        if (noMore(data)) {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
            return;
        }
        if (data != null) {
            addData(data.getArticle_list(),false);
            loadMore.setState(LoadMore.TYPE_IDLE);
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack<SubjectListBean> callback) {
        new DraftTopicListTask(callback).setTag(this).exe(groupId,getLastOneTag());
    }

    private Long getLastOneTag() {
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
        return null;
    }

    private boolean noMore(SubjectListBean data) {
        return data == null || data.getArticle_list() == null
                || data.getArticle_list().size() == Page.ZERO;
    }
}
