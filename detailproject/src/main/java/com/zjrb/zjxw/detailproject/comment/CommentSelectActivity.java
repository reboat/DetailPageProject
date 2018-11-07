package com.zjrb.zjxw.detailproject.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.ui.holder.EmptyPageHolder;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.CommentRefreshBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.comment.adapter.CommentSelectAdapter;
import com.zjrb.zjxw.detailproject.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.task.CommentListTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;


/**
 * 精选评论列表页面
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */

public class CommentSelectActivity extends BaseActivity implements HeaderRefresh.OnRefreshListener, DetailCommentHolder.deleteCommentListener, CommentWindowDialog.updateCommentListener {

    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R2.id.activity_comment)
    RelativeLayout ry_containerl;

    /**
     * 文章id
     */
    public String articleId = "";

    private CommentSelectAdapter mCommentAdapter;
    /**
     * 刷新头
     */
    private HeaderRefresh refresh;


    private DraftDetailBean mNewsDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData(getIntent());
        setContentView(R.layout.module_detail_select_comment);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    /**
     * 顶部topbar
     */
    private DefaultTopBarHolder1 topHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = TopBarFactory.createDefault1(view, this);
        topHolder.setViewVisible(topHolder.getShareView(), View.GONE);
        topHolder.setViewVisible(topHolder.getTitleView(), View.VISIBLE);
        topHolder.getTitleView().setText("精选");
        return topHolder.getView();
    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(IKey.NEWS_DETAIL)) {
                mNewsDetail = (DraftDetailBean) intent.getExtras().get(IKey.NEWS_DETAIL);
                if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                    articleId = String.valueOf(mNewsDetail.getArticle().getId());
                }
            }
        }
    }


    /**
     * 初始化评论界面数据
     */
    private void initData() {
        mRvContent.setLayoutManager(new LinearLayoutManager(CommentSelectActivity.this));
        //添加刷新头
        refresh = new HeaderRefresh(mRvContent);
        refresh.setOnRefreshListener(this);
        requestData(true);
    }

    /**
     * adapte处理
     *
     * @param bean
     */
    private void bindData(CommentRefreshBean bean) {
        //初始化适配器
        if (mCommentAdapter == null) {
            mCommentAdapter = new CommentSelectAdapter(bean, mRvContent, articleId, true, mNewsDetail);
            mCommentAdapter.setHeaderRefresh(refresh.getItemView());
            mCommentAdapter.setEmptyView(
                    new EmptyPageHolder(mRvContent,
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("目前没有任何评论").resId(R.mipmap.ic_comment_empty)
                    ).itemView);
            mRvContent.setAdapter(mCommentAdapter);
        } else {
            mCommentAdapter.setData(bean);
            mCommentAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 下拉刷新取评论数据
     */
    private void requestData(boolean isFirst) {
        new CommentListTask(new APIExpandCallBack<CommentRefreshBean>() {
            @Override
            public void onSuccess(CommentRefreshBean commentRefreshBean) {
                bindData(commentRefreshBean);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }

            @Override
            public void onAfter() {
                refresh.setRefreshing(false);
            }
        }, true).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).bindLoadViewHolder(isFirst ? replaceLoad(ry_containerl) : null).exe(articleId);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        mRvContent.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
                requestData(false);
            }
        });
    }

    /**
     * 删除评论回调
     */
    @Override
    public void onDeleteComment(int position) {
        mCommentAdapter.remove(position);
    }


    /**
     * 评论框提交评论后回调
     */
    @Override
    public void onUpdateComment() {
        mRvContent.post(new Runnable() {
            @Override
            public void run() {
                if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                    new Analytics.AnalyticsBuilder(getActivity(), "A0023", "A0023","Comment",false)
                            .setEvenName("发表评论")
                            .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                            .setObjectName(mNewsDetail.getArticle().getDoc_title())
                            .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                            .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                            .setPageType("评论页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                                    .toString())
                            .setSelfObjectID(mNewsDetail.getArticle().getId() + "").newsID(mNewsDetail.getArticle().getMlf_id() + "")
                            .selfNewsID(mNewsDetail.getArticle().getId()+"")
                            .newsTitle(mNewsDetail.getArticle().getDoc_title())
                            .selfChannelID(mNewsDetail.getArticle().getChannel_id())
                            .channelName(mNewsDetail.getArticle().getChannel_name())
                            .pageType("评论列表页")
                            .commentType("文章")
                            .build()
                            .send();
                }

                refresh.setRefreshing(false);
                requestData(false);
            }
        });
    }

}
