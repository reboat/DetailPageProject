package com.zjrb.zjxw.detailproject.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.zjrb.coreprojectlibrary.common.base.BaseActivity;
import com.zjrb.coreprojectlibrary.common.base.toolbar.ToolBarFactory;
import com.zjrb.coreprojectlibrary.common.global.C;
import com.zjrb.coreprojectlibrary.common.global.IKey;
import com.zjrb.coreprojectlibrary.ui.widget.divider.ListSpaceDivider;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.coreprojectlibrary.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import org.greenrobot.eventbus.EventBus;


import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 评论列表页面
 *
 * @author a_liYa
 * @date 16/10/18 下午7:20.
 */
public class CommentActivity extends BaseActivity implements SwipeRefreshLayout
        .OnRefreshListener, View.OnClickListener, IOnItemClickListener<CommentItemBean> {

    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R2.id.srl_refresh)
    SwipeRefreshLayout mSrlRefresh;
    @BindView(R2.id.tv_comment)
    TextView tvComment;
    private int articleId;
    private int mlfId;
    private CommentAdapter mCommentAdapter;
    private int newCount;   // 新评论个数 只统计自己的评论
    private int commentSet;   // 评论权限类型

    public static Intent newIntent(int articleId, int newCount, int commentSet, int mlfId) {
        return IntentHelper.get(CommentActivity.class)
                .put(IKey.ARTICLE_ID, articleId)
                .put(IKey.MLF_ID, mlfId)
                .put(IKey.COUNT, newCount)
                .put(IKey.COMMENT_SET, commentSet).intent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_comment);
        ButterKnife.bind(this);

//        fitStatusBarMode(true);

        initState(savedInstanceState);

        initRefresh();

        initData();

        initOnclick();
    }

    /**
     * 初始化评论界面数据
     */
    private void initData() {
        mRvContent.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        mRvContent.addItemDecoration(new ListSpaceDivider(0.5f, UIUtils.getColor
                (R.color
                         .dc_f5f5f5), 15, true));

        requestData();
    }


    private void requestData() {
        startLoading();
        new ArticleCommentListTask(new APIExpandCallBack<CommentRefreshBean>() {
            private List<CommentItemBean> commentList;

            @Override
            public void onSuccess(CommentRefreshBean commentRefreshBean) {
                if (commentRefreshBean == null) {
                    return;
                }
                if (commentRefreshBean.getResultCode() == 0) {//成功
                    commentList = commentRefreshBean.getCommentList();
                    if (commentList != null) {
                        mCommentAdapter = new CommentAdapter(commentList);
                        mCommentAdapter.setEmptyInfo("客官，赶紧留个言!", R.mipmap.ic_empty_page_comment);
                        mCommentAdapter.setOnItemClickListener(CommentActivity.this);
                        mRvContent.setAdapter(mCommentAdapter);
                        //传递参数
                        mCommentAdapter.setparams(articleId);
                        //fucking wm mlfid params
                        mCommentAdapter.setMlfparams(mlfId);
                        WmUtil.onColumnList(mlfId, commentList.size());
                    }
                } else {
                    showShortToast(commentRefreshBean.getResultMsg());
                }
            }

            @Override
            public void onError(String errMsg, int errCode) {
                showShortToast(errMsg);
            }

            @Override
            public void onAfter() {
                stopLoading();
            }
        }).setTag(this).exe(articleId);
    }

    private void initOnclick() {
        tvComment.setOnClickListener(this);
    }

    /**
     * 初始化上拉刷新
     */
    private void initRefresh() {
        mSrlRefresh.setOnRefreshListener(this);
    }

    private void initState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            articleId = getIntent().getIntExtra(IKey.ARTICLE_ID, -1);
            mlfId = getIntent().getIntExtra(IKey.MLF_ID, -1);
            commentSet = getIntent().getIntExtra(IKey.COMMENT_SET, 0);
            newCount = getIntent().getIntExtra(IKey.COUNT, 0);
        } else {
            articleId = savedInstanceState.getInt(IKey.ARTICLE_ID, -1);
            mlfId = savedInstanceState.getInt(IKey.MLF_ID, -1);
            commentSet = savedInstanceState.getInt(IKey.COMMENT_SET, 0);
            newCount = savedInstanceState.getInt(IKey.COUNT);
        }

        BizUtils.setCommentSet(tvComment, commentSet);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(IKey.COUNT, newCount);
    }

    @Override
    protected void onSetUpToolBar(Toolbar toolbar, ActionBar actionBar) {
        ToolBarFactory.createStyle1(this, toolbar, R.string.label_comment);
    }

    @Override
    public void onRefresh() {
        mSrlRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                requestData();
            }
        }, 0);
    }

    @Override
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        switch (v.getId()) {
            case R.id.tv_comment:
                if (BizUtils.isCanComment(this, commentSet)) {
                    tvComment.setVisibility(View.GONE);
                    startActivityForResult(CommentWindowActivity.getIntent(articleId, true, mlfId),
                                           C.request.COMMENT_WINDOW);
                    overridePendingTransition(0, 0); // 关闭动切换动画
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == C.request.COMMENT_WINDOW) {
                newCount++;
                // 增量更新
                initData();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvComment.setVisibility(View.VISIBLE);
    }

    /**
     * 每个item的点击事件
     *
     * @param view
     * @param position
     * @param data
     */
    @Override
    public void onItemClick(View view, int position, CommentItemBean data) {
//        showShortToast("position: " + position);
    }

    private void startLoading() {
        mSrlRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSrlRefresh.setRefreshing(true);
            }
        });
    }

    private void stopLoading() {
        mSrlRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSrlRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    public void finish() {
        EventBus.getDefault().postSticky(new CommentResultEvent(newCount));
        super.finish();
    }

}
