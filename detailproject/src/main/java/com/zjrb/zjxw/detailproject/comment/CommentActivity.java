package com.zjrb.zjxw.detailproject.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.holder.EmptyPageHolder;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.CommentRefreshBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.comment.adapter.CommentAdapter;
import com.zjrb.zjxw.detailproject.eventBus.CommentDeleteEvent;
import com.zjrb.zjxw.detailproject.task.CommentListTask;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 评论列表页面
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */

public class CommentActivity extends BaseActivity implements OnItemClickListener, HeaderRefresh.OnRefreshListener {

    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R2.id.tv_comment)
    TextView tvComment;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_comment_num)
    TextView tvCommentNum;
    @BindView(R2.id.activity_comment)
    RelativeLayout activityComment;

    /**
     * 文章id
     */
    public String articleId;
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
     * 评论标题
     */
    public String title;

    private CommentAdapter mCommentAdapter;
    /**
     * 刷新头
     */
    private HeaderRefresh refresh;


    private DraftDetailBean mNewsDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData(getIntent());
        setContentView(R.layout.module_detail_comment);
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
        topHolder.setViewVisible(topHolder.getShareView(), View.VISIBLE);
        return topHolder.getView();
    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(IKey.NEWS_DETAIL)) {
                mNewsDetail = (DraftDetailBean) getIntent().getExtras().get(IKey.NEWS_DETAIL);
                articleId = String.valueOf(mNewsDetail.getArticle().getId());
                mlfId = mNewsDetail.getArticle().getMlf_id();
                commentSet = mNewsDetail.getArticle().getComment_level();
                title = mNewsDetail.getArticle().getList_title();
            }
        }
    }

    /**
     * 头部布局
     */
    private View head;
    private TextView tvHot;

    /**
     * 初始化评论界面数据
     */
    private void initData() {
        head = UIUtils.inflate(R.layout.module_detail_comment_head);
        tvHot = (TextView) head.findViewById(R.id.tv_hot);
        mRvContent.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        mRvContent.addItemDecoration(new ListSpaceDivider(0.5f, UIUtils.getActivity().getResources().getColor(R.color.dc_f5f5f5), true, true));
        //添加刷新头
        refresh = new HeaderRefresh(mRvContent);
        refresh.setOnRefreshListener(this);
        requestData();
    }

    /**
     * adapte处理
     *
     * @param bean
     */
    private void bindData(CommentRefreshBean bean) {
        //初始化标题
        mBean = bean;
        tvHot.setText(getString(R.string.module_detail_new_comment));
        if (title != null && !title.isEmpty()) {
            tvTitle.setText(title);
        }

        //评论数
        if (mNewsDetail != null && !TextUtils.isEmpty(mNewsDetail.getArticle().getComment_count_general())) {
            tvCommentNum.setText(mNewsDetail.getArticle().getComment_count_general() + "条评论");
        } else {
            tvCommentNum.setVisibility(View.GONE);
        }

        //初始化适配器
        if (mCommentAdapter == null) {
            mCommentAdapter = new CommentAdapter(bean, mRvContent, articleId);
            mCommentAdapter.addHeaderView(head);
            mCommentAdapter.setHeaderRefresh(refresh.getItemView());
            mCommentAdapter.setEmptyView(
                    new EmptyPageHolder(mRvContent,
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据").attrId(R.attr.ic_comment_empty)
                    ).itemView);
            mCommentAdapter.setOnItemClickListener(this);
            mRvContent.setAdapter(mCommentAdapter);
        } else {
            mCommentAdapter.setData(bean);
            mCommentAdapter.notifyDataSetChanged();
        }
    }


    private CommentRefreshBean mBean;

    /**
     * 下拉刷新取评论数据
     */
    private void requestData() {
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
        }).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).bindLoadViewHolder(replaceLoad(mRvContent)).exe(articleId);
    }


    @OnClick({R2.id.tv_comment})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        if (v.getId() == R.id.tv_comment) {
            if (BizUtils.isCanComment(this, commentSet)) {
                tvComment.setVisibility(View.VISIBLE);
                CommentWindowDialog.newInstance(new CommentDialogBean(articleId)).show(getSupportFragmentManager(), "CommentWindowDialog");
            }
        } else if (v.getId() == R.id.iv_top_share) {
            CommentRefreshBean.ShareArtcleInfo bean = mBean.getShare_article_info();
            if (bean != null) {
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setImgUri(mBean.getShare_article_info().getArticle_pic())
                        .setTextContent(mBean.getShare_article_info().getSummary())
                        .setTitle(mBean.getShare_article_info().getList_title())
                        .setTargetUrl(mBean.getShare_article_info().getUrl()));
            }

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @param event 删除评论后刷新列表
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(CommentDeleteEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        if (event != null) {
            requestData();
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
    public void onItemClick(View itemView, int position) {
        if (BizUtils.isCanComment(CommentActivity.this, commentSet)) {
            tvComment.setVisibility(View.VISIBLE);
            CommentWindowDialog.newInstance(new CommentDialogBean(articleId)).show(getSupportFragmentManager(), "CommentWindowDialog");
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

}
