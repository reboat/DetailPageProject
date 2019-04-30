package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aliya.dailyplayer.sub.DailyPlayerManager;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.EmptyPageHolder;
import com.zjrb.core.recycleView.HeaderRefresh;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.NativeLiveBean;
import com.zjrb.zjxw.detailproject.apibean.task.NativeLiveTask;
import com.zjrb.zjxw.detailproject.callback.DetailInterface;
import com.zjrb.zjxw.detailproject.ui.boardcast.RefreshHeadReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.DailyFragment;
import cn.daily.news.biz.core.constant.C;
import cn.daily.news.biz.core.ui.toast.ZBToast;

import static com.zjrb.zjxw.detailproject.ui.mediadetail.VideoDetailFragment.FRAGMENT_DETAIL_BEAN;

/**
 * 视频直播间Fragment
 * Created by wanglinjie.
 * create time:2019/3/26  上午11:16
 */
public class VideoLiveFragment extends DailyFragment implements HeaderRefresh
        .OnRefreshListener, DetailInterface.RefreshHeadInterFace {
    public static final String FRAGMENT_VIDEO_LIVE = "fragment_video_live";

    @BindView(R2.id.lv_notice)
    RecyclerView lvNotice;
    @BindView(R2.id.fy_container)
    LinearLayout fyContainer;

    private DraftDetailBean mNewsDetail;
    private NativeLiveBean mNativeLiveBean;
    private HeaderRefresh refresh;
    private long startId = -1L;
    private boolean isReverse = false;
    private boolean isClick = false;
    private VideoDetailHeaderHolder headHolder;
    private VideoLiveAdapter adapter;
    private RefreshHeadReceiver refreshHeadReceiver;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsDetail = (DraftDetailBean) getArguments().getSerializable(FRAGMENT_DETAIL_BEAN);
        }
        refreshHeadReceiver = new RefreshHeadReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(refreshHeadReceiver, new IntentFilter("refresh_head"));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        lvNotice.setLayoutManager(mLinearLayoutManager);
        refresh = new HeaderRefresh(lvNotice);
        refresh.setOnRefreshListener(this);
        refreshData(startId, 10, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.module_detail_fragment_video_live, container, false);
    }


    //下拉刷新
    @Override
    public void onRefresh() {
        lvNotice.post(new Runnable() {
            @Override
            public void run() {
                startId = -1L;
                refresh.setRefreshing(false);
                refreshData(startId, 10, isReverse);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(refreshHeadReceiver);
    }

    /**
     * @param isResort 是否反转 默认为false倒序  true为正序
     */
    private void refreshData(long start, int size, boolean isResort) {
        //点击切换的时候,都不能传start
        if (isClick) {
            start = -1;
            isClick = false;
        }
        if (mNewsDetail == null || mNewsDetail.getArticle() == null || mNewsDetail.getArticle().getNative_live_info() == null) {
            return;
        }
        new NativeLiveTask(new LoadingCallBack<NativeLiveBean>() {
            @Override
            public void onSuccess(NativeLiveBean nativeLiveBean) {
                mNativeLiveBean = nativeLiveBean;
                if (mNativeLiveBean != null && mNativeLiveBean.getList() != null && mNativeLiveBean.getList().size() > 0) {
                    startId = mNativeLiveBean.getList().get(mNativeLiveBean.getList().size() - 1).getId();
                }
                initAdapter(nativeLiveBean);
                lvNotice.scrollToPosition(0);
                refresh.setRefreshing(false);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                ZBToast.showShort(getContext(), errMsg);
            }
        }).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).bindLoadViewHolder(replaceLoad(lvNotice)).exe(mNewsDetail.getArticle().getNative_live_info().getLive_id(), start, size, isResort);
    }

    //初始化适配器
    private void initAdapter(NativeLiveBean bean) {
        if (adapter == null) {
            adapter = new VideoLiveAdapter(lvNotice, bean, mNewsDetail);
            adapter.setHeaderRefresh(refresh.getItemView());
            headHolder = new VideoDetailHeaderHolder(lvNotice, isReverse);
            adapter.addHeaderView(headHolder.getItemView());
            headHolder.setData(mNewsDetail);
            adapter.setEmptyView(
                    new EmptyPageHolder(fyContainer,
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无内容").resId(R.mipmap.module_detail_live_empty)
                    ).itemView);
            lvNotice.setAdapter(adapter);
        } else {
            headHolder.setData(mNewsDetail);
            adapter.setData(bean);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //解决8.1系统bug 当横屏返回竖屏activity时 竖屏activity会先拉成横屏再变成竖屏
        if (adapter != null) {
            adapter.setCanDestory(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.setCanDestory(true);
                }
            }, 500);
        }
    }

    //点击倒序浏览
    @Override
    public void refresh(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra("isReverse")) {
                isReverse = intent.getBooleanExtra("isReverse", false);
            }
            if (intent.hasExtra("isClick")) {
                isClick = intent.getBooleanExtra("isClick", false);
                if(adapter != null){
                    adapter.setResort(isClick);
                }
            }
            refreshData(startId, 10, intent.getBooleanExtra("isReverse", false));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (mLinearLayoutManager == null) {
                return;
            }
            ViewGroup playContainer = findListPlayingView();
            if (playContainer != null && playContainer == DailyPlayerManager.get().getBuilder().getPlayContainer()) {
                DailyPlayerManager.get().onDestroy();
            }
        }
    }

    public ViewGroup findListPlayingView() {
        if (mLinearLayoutManager == null) {
            return null;
        }
        for (int i = 0; i < mLinearLayoutManager.getChildCount(); i++) {
            View view = mLinearLayoutManager.getChildAt(i);
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                ViewGroup playContainer = viewGroup.findViewWithTag("video_container");
                if (playContainer != null) {
                    return playContainer;
                }
            }
        }
        return null;
    }
}
