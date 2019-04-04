package com.zjrb.zjxw.detailproject.ui.comment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.EmptyPageHolder;
import com.zjrb.core.recycleView.HeaderRefresh;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
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
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.C;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.model.CommentDialogBean;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;
import cn.daily.news.biz.core.ui.dialog.CommentWindowDialog;
import cn.daily.news.biz.core.ui.toolsbar.BIZTopBarFactory;
import cn.daily.news.biz.core.ui.toolsbar.holder.DefaultTopBarHolder1;


/**
 * 评论列表页面
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */

public class CommentActivity extends DailyActivity implements HeaderRefresh.OnRefreshListener,
        DetailCommentHolder.deleteCommentListener,
        CommentWindowDialog.updateCommentListener,
        CommentWindowDialog.LocationCallBack {

    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    //    @BindView(R2.id.ly_container)
//    LinearLayout mLyContainer;
//    @BindView(R2.id.tv_comment_num)
//    TextView tvCommentNum;
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

    private BroadcastReceiver mRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onUpdateComment();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData(getIntent());
        setContentView(R.layout.module_detail_comment);
        ButterKnife.bind(this);
        initData();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRefreshReceiver, new IntentFilter("refresh_comment"));
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
        mRvContent.addItemDecoration(new NewsDetailCommentDivider(0.5f, R.color._dddddd_7a7b7d));
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
        } else {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null && !TextUtils.isEmpty(mNewsDetail.getArticle().getDoc_title())) {
                tvTitle.setText(mNewsDetail.getArticle().getDoc_title());
            }
        }

        //初始化适配器
        if (mCommentAdapter == null) {
            mCommentAdapter = new CommentAdapter(bean, mRvContent, head, articleId, is_select_list, mNewsDetail, bean.getComment_count());
            mCommentAdapter.setHeaderRefresh(refresh.getItemView());
            mCommentAdapter.addHeaderView(head);
            mCommentAdapter.setEmptyView(
                    new EmptyPageHolder(mRvContent,
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("目前没有任何评论").resId(R.mipmap.module_detail_comment_empty)
                    ).itemView);
            mRvContent.setAdapter(mCommentAdapter);
        } else {
            mCommentAdapter.setCommentCount(bean.getComment_count());
            mCommentAdapter.setData(bean);
            mCommentAdapter.notifyDataSetChanged();
        }
    }


    private CommentRefreshBean mBean;

    /**
     * 下拉刷新取评论数据
     */
    private void requestData(boolean isFirst) {
        new CommentListTask(new LoadingCallBack<CommentRefreshBean>() {
            @Override
            public void onSuccess(CommentRefreshBean commentRefreshBean) {
                bindData(commentRefreshBean);
                mRvContent.scrollToPosition(0);
                refresh.setRefreshing(false);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
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
                DataAnalyticsUtils.get().AppTabCommentClick(mNewsDetail);
            }
            try {
                CommentWindowDialog.newInstance(new CommentDialogBean(articleId)).setListen(this).setLocationCallBack(this).show(getSupportFragmentManager(), "CommentWindowDialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.iv_top_share) {
            if (mBean != null && mBean.getShare_article_info() != null && !TextUtils.isEmpty(mBean.getShare_article_info().getUrl())) {
                DataAnalyticsUtils.get().AppTabClick(mNewsDetail);
                //分享专用bean
                OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                        .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                        .setObjectName(mNewsDetail.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mNewsDetail.getArticle().getChannel_id() + "")
                        .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                        .setPageType("评论页")
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
                        .setTargetUrl(mBean.getShare_article_info().getUrl())
                        .setShareType("评论")
                        .setEventName("PageShare"));
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
        try {
            mCommentAdapter.remove(position);
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        if (!isFinishing()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRefreshReceiver);
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
}
