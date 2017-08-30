package com.zjrb.zjxw.detailproject.comment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjrb.coreprojectlibrary.api.callback.APIExpandCallBack;
import com.zjrb.coreprojectlibrary.api.callback.LoadingCallBack;
import com.zjrb.coreprojectlibrary.common.base.BaseActivity;
import com.zjrb.coreprojectlibrary.common.base.adapter.OnItemClickListener;
import com.zjrb.coreprojectlibrary.common.base.page.LoadMore;
import com.zjrb.coreprojectlibrary.common.base.toolbar.TopBarFactory;
import com.zjrb.coreprojectlibrary.common.global.C;
import com.zjrb.coreprojectlibrary.common.listener.LoadMoreListener;
import com.zjrb.coreprojectlibrary.nav.Nav;
import com.zjrb.coreprojectlibrary.ui.holder.FooterLoadMore;
import com.zjrb.coreprojectlibrary.ui.holder.HeaderRefresh;
import com.zjrb.coreprojectlibrary.ui.widget.divider.ListSpaceDivider;
import com.zjrb.coreprojectlibrary.utils.T;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.coreprojectlibrary.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.CommentRefreshBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.comment.adapter.CommentAdapter;
import com.zjrb.zjxw.detailproject.eventBus.CommentResultEvent;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.task.CommentListTask;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 评论列表页面
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */

public class CommentActivity extends BaseActivity implements OnItemClickListener, HeaderRefresh.OnRefreshListener, LoadMoreListener<CommentRefreshBean> {

    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R2.id.tv_comment)
    TextView tvComment;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_hot)
    TextView tvHot;
    @BindView(R2.id.activity_comment)
    RelativeLayout activityComment;

    /**
     * 文章id
     */
    public int articleId = -1;
    /**
     * 媒立方id
     */
    public int mlfId = -1;
    /**
     * 评论登记(是否可以评论)
     * 0 禁止评论 1 先审后发 2 先发后审
     */
    public int commentSet = -1;

    /**
     * 父评论id
     */
    public int parentId = 0;
    /**
     * 是否来自评论页
     */
    public boolean isFromCommentAct = false;
    /**
     * 评论标题
     */
    public String title;

    private CommentAdapter mCommentAdapter;
    /**
     * 刷新头
     */
    private HeaderRefresh refresh;
    /**
     * 加载更多
     */
    private FooterLoadMore more;
    /**
     * 评论数据
     */
    private List<HotCommentsBean> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_comment);
        ButterKnife.bind(this);
        initState();
        getIntentData(getIntent());
        initData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, "").getView();
    }

    /**
     * 设置评论等级(禁言)
     */
    private void initState() {
        BizUtils.setCommentSet(tvComment, commentSet);
    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            if (intent.hasExtra(Key.ARTICLE_ID)) {
                articleId = Integer.parseInt(data.getQueryParameter(Key.ARTICLE_ID));
            }
            if (intent.hasExtra(Key.MLF_ID)) {
                mlfId = Integer.parseInt(data.getQueryParameter(Key.MLF_ID));
            }
            if (intent.hasExtra(Key.COMMENT_SET)) {
                commentSet = Integer.parseInt(data.getQueryParameter(Key.COMMENT_SET));
            }
            if (intent.hasExtra(Key.TITLE)) {
                title = data.getQueryParameter(Key.TITLE);
            }
            if (intent.hasExtra(Key.PARENT_ID)) {
                parentId = Integer.parseInt(data.getQueryParameter(Key.PARENT_ID));
            }
            if (intent.hasExtra(Key.FROM_TYPE)) {
                isFromCommentAct = data.getBooleanQueryParameter(Key.FROM_TYPE, false);
            }
        }
    }

    /**
     * 初始化评论界面数据
     */
    private void initData() {
        if (title != null && !title.isEmpty()) {
            tvTitle.setText(title);
        }
        mRvContent.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        mRvContent.addItemDecoration(new ListSpaceDivider(0.5f, UIUtils.getColor(R.color.dc_f5f5f5), true, true));
        //添加刷新头
        refresh = new HeaderRefresh(mRvContent);
        refresh.setOnRefreshListener(this);
        more = new FooterLoadMore(mRvContent, this);
        requestData();
    }

    /**
     * 初始化适配器
     * 如果禁言，则不允许弹出评论框
     */
    private void initAdapter() {
        mCommentAdapter.setHeaderRefresh(refresh.getItemView());
        mCommentAdapter.setFooterLoadMore(more.getItemView());
        mCommentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (BizUtils.isCanComment(CommentActivity.this, commentSet)) {
                    tvComment.setVisibility(View.VISIBLE);
                    Nav.with(CommentActivity.this).to(Uri.parse("http://www.8531.cn/detail/CommentWindowActivity")
                            .buildUpon()
                            .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(articleId))
                            .appendQueryParameter(Key.MLF_ID, String.valueOf(mlfId))
                            .appendQueryParameter(Key.FROM_TYPE, String.valueOf(isFromCommentAct))
                            .appendQueryParameter(Key.PARENT_ID, String.valueOf(parentId))
                            .build(), RESULT_OK);
                }
            }
        });
    }

    /**
     * 下拉刷新取评论数据
     */
    private void requestData() {
        new CommentListTask(new APIExpandCallBack<CommentRefreshBean>() {
            @Override
            public void onSuccess(CommentRefreshBean commentRefreshBean) {
                if (commentRefreshBean == null) {
                    return;
                }
                if (commentRefreshBean.getResultCode() == 0) {//成功
                    commentList = commentRefreshBean.getComments();
                    if (commentList != null) {
                        if (mCommentAdapter == null) {
                            mCommentAdapter = new CommentAdapter(commentList);
                            initAdapter();
                        }
                        mRvContent.setAdapter(mCommentAdapter);
                        mCommentAdapter.setData(commentList);
                        mCommentAdapter.notifyDataSetChanged();
                    }
                } else {
                    T.showShort(getBaseContext(), commentRefreshBean.getResultMsg());
                }
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }

            @Override
            public void onAfter() {
            }
        }).setTag(this).exe(articleId, lastMinPublishTime + "", "20");
    }

    @OnClick({R2.id.tv_comment})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        if (v.getId() == R.id.tv_comment) {
            if (BizUtils.isCanComment(this, commentSet)) {
                tvComment.setVisibility(View.VISIBLE);
                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/CommentWindowActivity")
                        .buildUpon()
                        .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(articleId))
                        .appendQueryParameter(Key.MLF_ID, String.valueOf(mlfId))
                        .appendQueryParameter(Key.FROM_TYPE, String.valueOf(isFromCommentAct))
                        .appendQueryParameter(Key.PARENT_ID, String.valueOf(parentId))
                        .build(), RESULT_OK);
            }
        }
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data        评论提交成功后刷新评论数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == C.request.COMMENT_WINDOW) {
                // 增量更新
                requestData();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvComment.setVisibility(View.VISIBLE);
    }

    @Override
    public void finish() {
        EventBus.getDefault().postSticky(new CommentResultEvent());
        super.finish();
    }


    @Override
    public void onItemClick(View itemView, int position) {
        if (BizUtils.isCanComment(this, commentSet)) {
            Nav.with(UIUtils.getContext()).to(Uri.parse("http://www.8531.cn/detail/CommentWindowActivity")
                    .buildUpon()
                    .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(articleId))
                    .appendQueryParameter(Key.MLF_ID, String.valueOf(mlfId))
                    .appendQueryParameter(Key.FROM_TYPE, String.valueOf(isFromCommentAct))
                    .appendQueryParameter(Key.PARENT_ID, String.valueOf(parentId))
                    .build(), 0);
        }

    }

    @Override
    public void onRefresh() {
        mRvContent.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
                requestData();
            }
        });
    }

    //最后刷新时间
    private long lastMinPublishTime = 0;

    /**
     * @param data
     * @param loadMore 加载更多成功
     */
    @Override
    public void onLoadMoreSuccess(CommentRefreshBean data, LoadMore loadMore) {
        if (data != null) {
            List<HotCommentsBean> commentList = data.getComments();
            if (commentList != null && commentList.size() > 0) {
                lastMinPublishTime = getLastMinPublishTime(commentList);//获取最后的刷新时间
            }
            mCommentAdapter.addData(commentList, true);
        } else {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    /**
     * @param callback 加载更多操作
     */
    @Override
    public void onLoadMore(LoadingCallBack callback) {
        new CommentListTask(callback).setTag(this).exe(articleId, lastMinPublishTime, "20");
    }

    /**
     * @param commentList
     * @return 获取最后一次刷新的时间戳
     */
    private long getLastMinPublishTime(List<HotCommentsBean> commentList) {
        return commentList.get(commentList.size() - 1).getCreated_at();
    }

}
