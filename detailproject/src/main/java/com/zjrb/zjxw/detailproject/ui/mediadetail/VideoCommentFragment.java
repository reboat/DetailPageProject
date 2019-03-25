package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.EmptyPageHolder;
import com.zjrb.core.recycleView.HeaderRefresh;
import com.zjrb.core.utils.T;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.CommentRefreshBean;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.task.CommentListTask;
import com.zjrb.zjxw.detailproject.ui.comment.adapter.CommentAdapter;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.DailyFragment;
import cn.daily.news.biz.core.constant.C;
import cn.daily.news.biz.core.ui.dialog.CommentWindowDialog;

/**
 * 视频详情页评论相关
 * Created by wanglinjie.
 * create time:2019/3/22  下午4:24
 */
public class VideoCommentFragment extends DailyFragment implements HeaderRefresh
        .OnRefreshListener, DetailCommentHolder.deleteCommentListener,
        CommentWindowDialog.updateCommentListener,
        CommentWindowDialog.LocationCallBack {
    public static final String FRAGMENT_DETAIL_COMMENT = "fragment_detail_comment";
    @BindView(R2.id.lv_notice)
    RecyclerView lvNotice;
    @BindView(R2.id.fy_container)
    FrameLayout mContainer;

    private HeaderRefresh refresh;
    private DraftDetailBean mNewsDetail;
    private CommentAdapter mCommentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsDetail = (DraftDetailBean) getArguments().getSerializable(FRAGMENT_DETAIL_COMMENT);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        refresh = new HeaderRefresh(lvNotice);
        refresh.setOnRefreshListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.module_detail_fragment_relate_news, container, false);
    }

    @Override
    public void onRefresh() {
        lvNotice.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
                refreshData();
            }
        });
    }

    //初始化适配器
    private void initAdapter(CommentRefreshBean commentRefreshBean, DraftDetailBean newsDetail) {
        if (mCommentAdapter == null) {
            mCommentAdapter = new CommentAdapter(commentRefreshBean, lvNotice, newsDetail, true);
            mCommentAdapter.setHeaderRefresh(refresh.getItemView());
            mCommentAdapter.setEmptyView(
                    new EmptyPageHolder(lvNotice,
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("目前没有任何评论").resId(R.mipmap.ic_comment_empty)
                    ).itemView);
            lvNotice.setAdapter(mCommentAdapter);
        } else {
            mCommentAdapter.setCommentCount(commentRefreshBean.getComment_count());
            mCommentAdapter.setData(commentRefreshBean);
            mCommentAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 下拉刷新
     */
    private void refreshData() {
        new CommentListTask(new LoadingCallBack<CommentRefreshBean>() {
            @Override
            public void onSuccess(CommentRefreshBean commentRefreshBean) {
                initAdapter(commentRefreshBean, mNewsDetail);
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
        }, false).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).bindLoadViewHolder(replaceLoad(mContainer)).exe(mNewsDetail.getArticle().getId());
    }

    @Override
    public String onGetLocation() {
        if (LocationManager.getInstance().getLocation() != null) {
            DataLocation.Address address = LocationManager.getInstance().getLocation().getAddress();
            if (address != null) {
                return address.getCountry() + "," + address.getProvince() + "," + address.getCity();
            } else {
                return "" + "," + "" + "," + "";
            }
        } else {
            return "" + "," + "" + "," + "";
        }
    }

    @Override
    public void onUpdateComment() {
        lvNotice.post(new Runnable() {
            @Override
            public void run() {
                if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                    DataAnalyticsUtils.get().UpdateComment(mNewsDetail);
                }

                refresh.setRefreshing(false);
                refreshData();
            }
        });
    }

    @Override
    public void onDeleteComment(int position) {
        mCommentAdapter.remove(position);
    }
}
