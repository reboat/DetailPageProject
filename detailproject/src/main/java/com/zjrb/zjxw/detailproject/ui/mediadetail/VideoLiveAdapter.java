package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.zjrb.core.load.LoadMoreListener;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.FooterLoadMore;
import com.zjrb.core.recycleView.LoadMore;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
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
    private int live_id = 0;
    private boolean isResort = false;
    private final FooterLoadMore<NativeLiveBean> mLoadMore;
    //图片
    private int VIDEO_LIVE_MUL_PIC = 1;
    //视频
    private int VIDEO_LIVE_VIDEO = 2;

    public VideoLiveAdapter(ViewGroup parent, NativeLiveBean data) {
        super(null);
        mLoadMore = new FooterLoadMore<>(parent, this);
        setFooterLoadMore(mLoadMore.itemView);
        setData(data);
    }


    public void setData(NativeLiveBean data) {
        cancelLoadMore();
        mLoadMore.setState(noMore(data) ? LoadMore.TYPE_NO_MORE : LoadMore.TYPE_IDLE);
        setData((data != null && data.getList() != null) ? data.getList() : null);
    }

    private boolean noMore(NativeLiveBean data) {
        return data.isHas_more();
    }

    private void cancelLoadMore() {
        APICallManager.get().cancel(this);
    }

    @Override
    public void onLoadMoreSuccess(NativeLiveBean data, LoadMore loadMore) {
        if (noMore(data)) {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
        if (data != null && data.getList() != null && data.getList().size() > 0) {
            addData(data.getList(), true);
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack<NativeLiveBean> callback) {
        new NativeLiveTask(callback).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).exe(live_id, getLastOneTag(), 10, isResort);
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
        }
        return super.getAbsItemViewType(position);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIDEO_LIVE_MUL_PIC) {
            return new VideoDetailPicHolder(parent);
        } else {
            return new DetailVideoHolder(parent);
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
