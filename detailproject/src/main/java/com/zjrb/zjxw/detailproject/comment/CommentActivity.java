package com.zjrb.zjxw.detailproject.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
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
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.CommentRefreshBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.comment.adapter.CommentAdapter;
import com.zjrb.zjxw.detailproject.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.task.CommentListTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 评论列表页面
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */

public class CommentActivity extends BaseActivity implements HeaderRefresh.OnRefreshListener, DetailCommentHolder.deleteCommentListener, CommentWindowDialog.updateCommentListener {

    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_comment_num)
    TextView tvCommentNum;

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

    /**
     * 是否是精选列表
     */
    private boolean is_select_list = false;

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
                mNewsDetail = (DraftDetailBean) intent.getExtras().get(IKey.NEWS_DETAIL);
                articleId = String.valueOf(mNewsDetail.getArticle().getId());
                mlfId = mNewsDetail.getArticle().getMlf_id();
                commentSet = mNewsDetail.getArticle().getComment_level();
                title = mNewsDetail.getArticle().getList_title();
            }

            if (intent.hasExtra(IKey.IS_SELECT_LIST)) {
                is_select_list = intent.getBooleanExtra(IKey.IS_SELECT_LIST, false);
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
        mRvContent.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        mRvContent.addItemDecoration(new NewsDetailCommentDivider(0.5f, R.attr.bc_dddddd));
        head = UIUtils.inflate(R.layout.module_detail_comment_head, mRvContent, false);
        tvHot = (TextView) head.findViewById(R.id.tv_hot);
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
        if (bean != null && !TextUtils.isEmpty(bean.getComment_count())) {
            tvCommentNum.setText(bean.getComment_count() + "条评论");
        } else {
            tvCommentNum.setVisibility(View.GONE);
        }

        //初始化适配器
        if (mCommentAdapter == null) {
            mCommentAdapter = new CommentAdapter(bean, mRvContent, articleId, is_select_list);
            mCommentAdapter.setHeaderRefresh(refresh.getItemView());
            mCommentAdapter.addHeaderView(head);
            mCommentAdapter.setEmptyView(
                    new EmptyPageHolder(mRvContent,
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据").attrId(R.attr.ic_comment_empty)
                    ).itemView);
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
        }, is_select_list).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).bindLoadViewHolder(replaceLoad(mRvContent)).exe(articleId);
    }


    @OnClick({R2.id.tv_comment, R2.id.iv_top_share})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        if (v.getId() == R.id.tv_comment) {
            CommentWindowDialog.newInstance(new CommentDialogBean(articleId)).setListen(this).show(getSupportFragmentManager(), "CommentWindowDialog");
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
    protected void onResume() {
        super.onResume();
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

    /**
     * 删除评论回调
     */
    @Override
    public void onDeleteComment(String comment_id) {
        List list = mCommentAdapter.getData();
        for (Object obj : list) {
            int count = 0;
            if (obj instanceof HotCommentsBean && ((HotCommentsBean) obj).getId().equals(comment_id)) {
                mCommentAdapter.getData().remove(obj);
                mCommentAdapter.notifyItemMoved(count, count);
//                mCommentAdapter.notifyItemRangeChanged(count, mCommentAdapter.getDataSize());
                break;
            }
            count++;
        }
    }


    /**
     * 评论框提交评论后回调
     */
    @Override
    public void onUpdateComment() {
        mCommentAdapter.notifyItemInserted(0);
    }
}
