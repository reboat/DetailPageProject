package com.zjrb.zjxw.detailproject.comment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.ui.UmengUtils.OutSizeAnalyticsBean;
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
import com.zjrb.zjxw.detailproject.comment.adapter.CommentAdapter;
import com.zjrb.zjxw.detailproject.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.task.CommentListTask;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;


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
    @BindView(R2.id.activity_comment)
    RelativeLayout ry_containerl;

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

    /**
     * 初始化评论界面数据
     */
    private void initData() {
        mRvContent.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        mRvContent.addItemDecoration(new NewsDetailCommentDivider(0.5f, R.attr.dc_dddddd));
        head = UIUtils.inflate(R.layout.module_detail_comment_head, mRvContent, false);
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
        //初始化标题
        mBean = bean;
        if (bean.getComment_count() == 0) {
            head.setVisibility(View.GONE);
        } else {
            head.setVisibility(View.VISIBLE);
            ((TextView) head).setText(getString(R.string.module_detail_new_comment));
        }

        if (mBean != null && mBean.getShare_article_info() != null && !TextUtils.isEmpty(mBean.getShare_article_info().getList_title())) {
            tvTitle.setText(mBean.getShare_article_info().getList_title());
        }

        //评论数
        if (bean != null && bean.getComment_count() > 0) {
            tvCommentNum.setVisibility(View.VISIBLE);
            if (bean.getComment_count() <= 99999) {
                tvCommentNum.setText(bean.getComment_count() + "条评论");
            } else {
                tvCommentNum.setText("99999+条评论");
            }
        } else {
            tvCommentNum.setVisibility(View.GONE);
        }

        //初始化适配器
        if (mCommentAdapter == null) {
            mCommentAdapter = new CommentAdapter(bean, mRvContent, head, articleId, is_select_list, mNewsDetail);
            mCommentAdapter.setHeaderRefresh(refresh.getItemView());
            mCommentAdapter.addHeaderView(head);
            mCommentAdapter.setEmptyView(
                    new EmptyPageHolder(mRvContent,
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("目前没有任何评论").attrId(R.attr.ic_comment_empty)
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
        }, is_select_list).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).bindLoadViewHolder(isFirst ? replaceLoad(ry_containerl) : null).exe(articleId);
    }


    private String mArticleId;

    /**
     * 获取稿件ID
     *
     * @param bean
     */
    private String getID(CommentRefreshBean bean) {
        if (bean != null && bean.getShare_article_info() != null && !TextUtils.isEmpty(bean.getShare_article_info().getUrl())) {
            Uri data = Uri.parse(bean.getShare_article_info().getUrl());
            if (data != null) {
                if (data.getQueryParameter(IKey.ID) != null) {
                    mArticleId = data.getQueryParameter(IKey.ID);
                }
            }
        }
        return mArticleId;
    }

    @OnClick({R2.id.tv_comment, R2.id.iv_top_share})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        if (v.getId() == R.id.tv_comment) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                new Analytics.AnalyticsBuilder(this, "800002", "800002")
                        .setEvenName("点击评论输入框")
                        .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                        .setObjectName(mNewsDetail.getArticle().getDoc_title())
                        .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                        .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                        .setPageType("评论页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                                .toString())
                        .setSelfObjectID(mNewsDetail.getArticle().getId() + "")
                        .build()
                        .send();
            }
            CommentWindowDialog.newInstance(new CommentDialogBean(articleId)).setListen(this).show(getSupportFragmentManager(), "CommentWindowDialog");
        } else if (v.getId() == R.id.iv_top_share) {
            if (mBean != null && mBean.getShare_article_info() != null && !TextUtils.isEmpty(mBean.getShare_article_info().getUrl())) {
                //分享专用bean
                OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                        .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                        .setObjectName(mNewsDetail.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mNewsDetail.getArticle().getChannel_id() + "")
                        .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfobjectID(mNewsDetail.getArticle().getId() + "");

                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setArticleId(getID(mBean))
                        .setImgUri(mBean.getShare_article_info().getArticle_pic())
                        .setTextContent(mBean.getShare_article_info().getSummary())
                        .setTitle(mBean.getShare_article_info().getList_title())
                        .setAnalyticsBean(bean)
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
                    new Analytics.AnalyticsBuilder(CommentActivity.this, "A0023", "A0023")
                            .setEvenName("发表评论")
                            .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                            .setObjectName(mNewsDetail.getArticle().getDoc_title())
                            .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                            .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                            .setPageType("评论页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                                    .toString())
                            .setSelfObjectID(mNewsDetail.getArticle().getId() + "")
                            .build()
                            .send();
                }

                refresh.setRefreshing(false);
                requestData(false);
            }
        });
    }

    /**
     * 友盟分享回调(主要是QQ)
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengShareUtils.getInstance().initResult(requestCode, resultCode, data);
    }
}
