package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.EmptyPageHolder;
import com.zjrb.core.recycleView.HeaderRefresh;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.NativeLiveBean;
import com.zjrb.zjxw.detailproject.apibean.task.NativeLiveTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.biz.core.DailyFragment;
import cn.daily.news.biz.core.constant.C;

import static com.zjrb.zjxw.detailproject.ui.mediadetail.VideoDetailFragment.FRAGMENT_DETAIL_BEAN;

/**
 * 视频直播间Fragment
 * Created by wanglinjie.
 * create time:2019/3/26  上午11:16
 */
public class VideoLiveFragment extends DailyFragment implements HeaderRefresh
        .OnRefreshListener {
    public static final String FRAGMENT_VIDEO_LIVE = "fragment_video_live";
    @BindView(R2.id.tv_read_num)
    TextView tvReadNum;
    @BindView(R2.id.tv_read_sort)
    TextView tvReadSort;
    @BindView(R2.id.lv_notice)
    RecyclerView lvNotice;
    @BindView(R2.id.fy_container)
    LinearLayout fyContainer;

    private DraftDetailBean mNewsDetail;
    private NativeLiveBean mNativeLiveBean;
    private HeaderRefresh refresh;
    //上一次返回的结果的最后一条数据的id
    private long startId = -1L;
    //是否反转
    private boolean isReverse = false;
    private VideoLiveAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsDetail = (DraftDetailBean) getArguments().getSerializable(FRAGMENT_DETAIL_BEAN);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        lvNotice.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                refresh.setRefreshing(false);
                refreshData(startId, 10, isReverse);
            }
        });
    }

    @OnClick({R2.id.tv_read_sort})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        if (v.getId() == R.id.tv_read_sort) {
            if (!isReverse) {
                isReverse = true;
                tvReadSort.setText("正序浏览");
            } else {
                isReverse = false;
                tvReadSort.setText("倒序浏览");
            }
            refreshData(startId, 10, isReverse);
        }
    }

    /**
     * @param isResort 是否反转 默认为false倒序  true为正序
     */
    private void refreshData(long start, int size, boolean isResort) {
        new NativeLiveTask(new LoadingCallBack<NativeLiveBean>() {
            @Override
            public void onSuccess(NativeLiveBean nativeLiveBean) {
                mNativeLiveBean = nativeLiveBean;
                tvReadNum.setText(mNewsDetail.getArticle().getRead_count_general() + "观看");
                if (mNativeLiveBean != null && mNativeLiveBean.getList() != null && mNativeLiveBean.getList().size() > 0) {
                    startId = mNativeLiveBean.getList().get(mNativeLiveBean.getList().size()).getId();
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
                T.showShort(getContext(), errMsg);
            }
        }).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).bindLoadViewHolder(replaceLoad(fyContainer)).exe(mNewsDetail.getArticle().getNative_live_info().getLive_id(), start, size, isResort);
    }

    //初始化适配器
    private void initAdapter(NativeLiveBean bean) {
        if (adapter == null) {
            adapter = new VideoLiveAdapter(lvNotice, bean);
            adapter.setHeaderRefresh(refresh.getItemView());
            adapter.setEmptyView(
                    new EmptyPageHolder(fyContainer,
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无内容").resId(R.mipmap.module_detail_live_empty)
                    ).itemView);
            lvNotice.setAdapter(adapter);
        } else {
            adapter.setData(bean);
            adapter.notifyDataSetChanged();
        }
    }
}
