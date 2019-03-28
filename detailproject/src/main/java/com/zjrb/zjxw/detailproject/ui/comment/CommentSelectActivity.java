package com.zjrb.zjxw.detailproject.ui.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.EmptyPageHolder;
import com.zjrb.core.recycleView.HeaderRefresh;
import com.zjrb.core.utils.T;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.CommentRefreshBean;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.task.CommentListTask;
import com.zjrb.zjxw.detailproject.ui.comment.adapter.CommentSelectAdapter;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.C;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.ui.dialog.CommentWindowDialog;
import cn.daily.news.biz.core.ui.toolsbar.BIZTopBarFactory;
import cn.daily.news.biz.core.ui.toolsbar.holder.DefaultTopBarHolder1;


/**
 * 精选评论列表页面
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */

public class CommentSelectActivity extends DailyActivity implements HeaderRefresh.OnRefreshListener,
        DetailCommentHolder.deleteCommentListener,
        CommentWindowDialog.updateCommentListener {

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
        topHolder = BIZTopBarFactory.createDefault1(view, this);
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
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("目前没有任何评论").resId(R.mipmap.module_detail_comment_empty)
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
        new CommentListTask(new LoadingCallBack<CommentRefreshBean>() {
            @Override
            public void onSuccess(CommentRefreshBean commentRefreshBean) {
                bindData(commentRefreshBean);
                refresh.setRefreshing(false);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
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
                    DataAnalyticsUtils.get().UpdateComment(mNewsDetail);
                }

                refresh.setRefreshing(false);
                requestData(false);
            }
        });
    }

}
