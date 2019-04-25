package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.aliya.dailyplayer.sub.DailyPlayerManager;
import com.zjrb.core.load.LoadMoreListener;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.FooterLoadMore;
import com.zjrb.core.recycleView.LoadMore;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.NativeLiveBean;
import com.zjrb.zjxw.detailproject.apibean.task.NativeLiveTask;

import cn.daily.news.biz.core.constant.C;
import cn.daily.news.biz.core.network.compatible.APICallManager;

/**
 * 视频直播适配器
 * Created by wanglinjie.
 * create time:2019/3/26  下午2:36
 */
public class VideoLiveAdapter extends BaseRecyclerAdapter implements LoadMoreListener<NativeLiveBean> {
    private boolean isResort = false;
    private final FooterLoadMore<NativeLiveBean> mLoadMore;
    private DraftDetailBean mDraftDetailBean;
    //图片
    private int VIDEO_LIVE_MUL_PIC = 1;
    //视频
    private int VIDEO_LIVE_VIDEO = 2;
    //纯文字
    private int VIDEO_LIVE_TEXT = 3;

    public VideoLiveAdapter(ViewGroup parent, NativeLiveBean data, DraftDetailBean newsDetail) {
        super(null);
        mDraftDetailBean = newsDetail;
        mLoadMore = new FooterLoadMore<>(parent, this);
        setFooterLoadMore(mLoadMore.itemView);
        setData(data);
    }


    public void setData(NativeLiveBean data) {
        cancelLoadMore();
        mLoadMore.setState(noMore(data) ? LoadMore.TYPE_NO_MORE : LoadMore.TYPE_IDLE);
        setData((data != null && data.getList() != null) ? data.getList() : null);
    }

    //是否没有更多
    private boolean noMore(NativeLiveBean data) {
        if(data == null){
            return true;
        }else{
            return !data.isHas_more();
        }
    }

    private void cancelLoadMore() {
        APICallManager.get().cancel(this);
    }

    @Override
    public void onLoadMoreSuccess(NativeLiveBean data, LoadMore loadMore) {
        if (noMore(data) || data == null ) {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
        if (data != null && data.getList() != null && data.getList().size() > 0) {
            addData(data.getList(), true);
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack<NativeLiveBean> callback) {
        new NativeLiveTask(callback).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).exe(mDraftDetailBean.getArticle().getNative_live_info().getLive_id(), getLastOneTag(), 10, isResort);
    }


    @Override
    public int getAbsItemViewType(int position) {
        if (getData(position) instanceof NativeLiveBean.ListBean &&
                !TextUtils.isEmpty(((NativeLiveBean.ListBean) getData(position)).getVideo_url())) {
            return VIDEO_LIVE_VIDEO;
        } else if (getData(position) instanceof NativeLiveBean.ListBean &&
                ((NativeLiveBean.ListBean) getData(position)).getPics() != null &&
                ((NativeLiveBean.ListBean) getData(position)).getPics().size() > 0) {
            return VIDEO_LIVE_MUL_PIC;
        } else if ((getData(position) instanceof NativeLiveBean.ListBean && TextUtils.isEmpty(((NativeLiveBean.ListBean) getData(position)).getVideo_url())
                && (((NativeLiveBean.ListBean) getData(position)).getPics() == null || ((NativeLiveBean.ListBean) getData(position)).getPics().size() == 0))) {
            return VIDEO_LIVE_TEXT;
        }
        return super.getAbsItemViewType(position);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIDEO_LIVE_MUL_PIC) {
            return new VideoDetailPicHolder(parent);
        } else if (viewType == VIDEO_LIVE_VIDEO) {
            return new DetailVideoHolder(parent, mDraftDetailBean);
        } else {
            return new VideoDetailLiveTextHolder(parent);
        }
    }


    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        //如果有正在播放的view 找到并且删除
        if (holder.itemView instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) holder.itemView;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                if (DailyPlayerManager.get().getBuilder() != null && child == DailyPlayerManager.get().getBuilder().getPlayContainer()) {
                    DailyPlayerManager.get().onDestroy();
                    DailyPlayerManager.get().deleteControllers(DailyPlayerManager.get().getBuilder().getPlayContainer());
                    return;
                }
            }
        }
    }

    /**
     * @return 获取最后一次刷新的id
     */
    private Long getLastOneTag() {
        int size = getDataSize();
        if (size > 0) {
            int count = 1;
            while (size - count >= 0) {
                Object data = getData(size - count++);
                if (data instanceof NativeLiveBean.ListBean) {
                    return ((NativeLiveBean.ListBean) data).getId();
                }
            }
        }
        return null;
    }


}
