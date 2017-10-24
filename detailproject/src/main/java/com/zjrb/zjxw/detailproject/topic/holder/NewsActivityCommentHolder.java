package com.zjrb.zjxw.detailproject.topic.holder;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.CommentRefreshBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.task.CommentListTask;
import com.zjrb.zjxw.detailproject.topic.adapter.TopicCommentAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 话题详情页互动评论
 * Created by wanglinjie.
 * create time:2017/7/28  下午12:28
 */

public class NewsActivityCommentHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements LoadMoreListener<CommentRefreshBean> {

    @BindView(R2.id.ly_hot_comment)
    LinearLayout mLyHotContainer;
    @BindView(R2.id.tv_related)
    TextView mText;
    @BindView(R2.id.rv_content)
    RecyclerView mRecyleView;
    @BindView(R2.id.tv_more)
    TextView mMore;
    @BindView(R2.id.tv_all)
    TextView mTvAll;

    /**
     * 评论适配器
     */
    private TopicCommentAdapter adapter;

    /**
     * 加载更多
     */
    private FooterLoadMore more;

    public NewsActivityCommentHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_hot_comment, parent, false));
        ButterKnife.bind(this, itemView);
        initView();
    }

    private void initView() {
        mText.setText(itemView.getContext().getString(R.string.module_detail_interact));
        mTvAll.setVisibility(View.GONE);
        mRecyleView.addItemDecoration(new ListSpaceDivider(0.5d, 0, false));
        mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                LinearLayoutManager.VERTICAL, false));
        more = new FooterLoadMore(mRecyleView, this);
    }

    /**
     * 初始化适配器
     * 如果禁言，则不允许弹出评论框
     */
    private void initAdapter() {
        adapter.setFooterLoadMore(more.getItemView());
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (ClickTracker.isDoubleClick()) return;
                CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(String.valueOf(mData.getArticle().getId())))).show(((FragmentActivity) UIUtils.getActivity()).getSupportFragmentManager(), "CommentWindowDialog");
            }
        });
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        //互动
        mText.setText(itemView.getContext().getString(R.string.module_detail_interact));
        mMore.setVisibility(View.GONE);

        if (mData != null && mData.getArticle().getTopic_comment_list() != null && mData.getArticle().getTopic_comment_list().size() > 0) {
            adapter = new TopicCommentAdapter(mData.getArticle().getTopic_comment_list(), String.valueOf(mData.getArticle().getId()));
            initAdapter();
            mRecyleView.setAdapter(adapter);
        } else {
            mLyHotContainer.setVisibility(View.GONE);
        }
    }


    private Bundle bundle;

    /**
     * @param view 点击进入评论列表
     */
    @OnClick({R2.id.tv_more})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.tv_more) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSerializable(IKey.NEWS_DETAIL, mData);
            Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.COMMENT_ACTIVITY_PATH);
        }
    }

    private long lastMinPublishTime;

    /**
     * 加载更多成功
     *
     * @param data
     * @param loadMore
     */
    @Override
    public void onLoadMoreSuccess(CommentRefreshBean data, LoadMore loadMore) {
        if (data != null && data.getComments() != null) {
            List<HotCommentsBean> commentList = data.getComments();
            if (commentList.size() > 0) {
                lastMinPublishTime = getLastMinPublishTime(commentList);
            }
            adapter.addData(commentList, true);
            if (commentList.size() < C.PAGE_SIZE) {
                loadMore.setState(LoadMore.TYPE_NO_MORE);
            }

        } else {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    /**
     * 加载更多
     *
     * @param callback
     */
    @Override
    public void onLoadMore(LoadingCallBack<CommentRefreshBean> callback) {
        new CommentListTask(callback,true).setTag(this).exe(mData.getArticle().getId(), lastMinPublishTime == 0 ? null : lastMinPublishTime);
    }

    /**
     * @param commentList
     * @return 获取最后一次刷新的时间戳
     */
    private long getLastMinPublishTime(List<HotCommentsBean> commentList) {
        if (commentList != null && !commentList.isEmpty()) {
            return commentList.get(commentList.size() - 1).getCreated_at();
        }
        return 0;
    }
}
