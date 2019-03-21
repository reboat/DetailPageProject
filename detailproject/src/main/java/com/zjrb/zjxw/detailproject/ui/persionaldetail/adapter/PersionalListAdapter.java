package com.zjrb.zjxw.detailproject.ui.persionaldetail.adapter;

import android.view.ViewGroup;

import com.zjrb.core.load.LoadMoreListener;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.FooterLoadMore;
import com.zjrb.core.recycleView.LoadMore;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.zjxw.detailproject.apibean.bean.OfficalArticlesBean;
import com.zjrb.zjxw.detailproject.apibean.bean.OfficalListBean;
import com.zjrb.zjxw.detailproject.apibean.task.OfficalListTask;
import com.zjrb.zjxw.detailproject.ui.persionaldetail.holder.PersionalListDetailHolder;
import com.zjrb.zjxw.detailproject.ui.persionaldetail.holder.PersionalTextHolder;
import com.zjrb.zjxw.detailproject.ui.topic.holder.NewsPlaceHolder;

import java.util.ArrayList;
import java.util.List;

import cn.daily.news.biz.core.constant.C;
import cn.daily.news.biz.core.network.compatible.APICallManager;

/**
 * 所有官员列表适配器
 * Created by wanglinjie.
 * create time:2017/8/21  上午10:14
 */
public class PersionalListAdapter extends BaseRecyclerAdapter implements LoadMoreListener<OfficalListBean> {

    public static int TYPE_PERSIONAL_DETAIL = -1;
    public static int TYPE_NOMAL = 0;

    private final FooterLoadMore<OfficalListBean> mLoadMore;

    public PersionalListAdapter(OfficalListBean datas, ViewGroup parent) {
        super(null);
        mLoadMore = new FooterLoadMore<>(parent, this);
        setFooterLoadMore(mLoadMore.itemView);
        setData(datas);
    }

    public void setData(OfficalListBean data) {
        cancelLoadMore();
        mLoadMore.setState(noMore(data) ? LoadMore.TYPE_NO_MORE : LoadMore.TYPE_IDLE);
        List list = new ArrayList<>();
        for (int i = 0; i < data.getOfficer_list().size(); i++) {
            list.add(data.getOfficer_list().get(i));
            if (data.getOfficer_list().get(i).getArticles() != null && !data.getOfficer_list().get(i).getArticles().isEmpty()) {
                list.addAll(data.getOfficer_list().get(i).getArticles());
            }
        }
        setData(data != null ? list : null);
    }

    public void addData(List<OfficalListBean.OfficerListBean> data) {
        List list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            list.add(data.get(i));
            if (data.get(i).getArticles() != null && !data.get(i).getArticles().isEmpty()) {
                list.addAll(data.get(i).getArticles());
            }
        }
        addData(list, false);
        notifyDataSetChanged();
    }

    public boolean noMore(OfficalListBean data) {
        return data == null || data.getOfficer_list() == null
                || data.getOfficer_list().size() < C.PAGE_SIZE;
    }

    public void cancelLoadMore() {
        APICallManager.get().cancel(this);
    }

    @Override
    public void onLoadMoreSuccess(OfficalListBean data, LoadMore loadMore) {
        if (noMore(data)) {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
        if (data != null) {
            addData(data.getOfficer_list());
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack<OfficalListBean> callback) {
        new OfficalListTask(callback).setTag(this).exe(getLastOneTag());
    }

    /**
     * @return 获取最后一次刷新的ID
     */
    private Integer getLastOneTag() {
        int size = getDataSize();
        if (size > 0) {
            int count = 1;
            while (size - count >= 0) {
                Object data = getData(size - count++);
                if (data instanceof OfficalListBean.OfficerListBean) {
                    return ((OfficalListBean.OfficerListBean) data).getId();
                }
            }
        }
        return null;
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        if (TYPE_PERSIONAL_DETAIL == viewType) {
            //官员详情
            return new PersionalListDetailHolder(parent);
        } else if (TYPE_NOMAL == viewType) {
            //链接稿
            return new PersionalTextHolder(parent);
        }
        return new NewsPlaceHolder(parent);
    }

    @Override
    public int getAbsItemViewType(int position) {
        if (datas.get(position) instanceof OfficalListBean.OfficerListBean) {
            return TYPE_PERSIONAL_DETAIL;
        } else if (datas.get(position) instanceof OfficalArticlesBean) {
            return TYPE_NOMAL;
        }
        return 0;
    }


}
