package com.zjrb.zjxw.detailproject.ui.topic;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadMoreListener;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.EmptyPageHolder;
import com.zjrb.core.recycleView.FooterLoadMore;
import com.zjrb.core.recycleView.LoadMore;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.CommentRefreshBean;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.apibean.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.apibean.task.CommentListTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.callback.DetailInterface;
import com.zjrb.zjxw.detailproject.ui.boardcast.SubscribeReceiver;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.ui.topic.adapter.TopicAdapter;
import com.zjrb.zjxw.detailproject.ui.topic.holder.FloorBarHolder;
import com.zjrb.zjxw.detailproject.ui.topic.holder.FooterPlaceHolder;
import com.zjrb.zjxw.detailproject.ui.topic.holder.HeaderTopicTop;
import com.zjrb.zjxw.detailproject.ui.topic.holder.OverlyHolder;
import com.zjrb.zjxw.detailproject.ui.topic.holder.TopBarHolder;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;
import com.zjrb.zjxw.detailproject.utils.YiDunToken;
import com.zjrb.zjxw.detailproject.utils.global.C;
import com.zjrb.zjxw.detailproject.widget.ColorImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.model.CommentDialogBean;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;
import cn.daily.news.biz.core.ui.dialog.CommentWindowDialog;
import cn.daily.news.biz.core.web.JsMultiInterfaceImp;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * 话题详情页
 * Created by wanglinjie.
 * create time:2017/9/13  上午9:08
 */
public class ActivityTopicActivity extends DailyActivity implements
        TopicAdapter.CommonOptCallBack, LoadMoreListener<CommentRefreshBean>,
        DetailCommentHolder.deleteCommentListener, CommentWindowDialog.LocationCallBack,
        DetailInterface.SubscribeSyncInterFace {

    @BindView(R2.id.recycler)
    RecyclerView mRecycler;

    @BindView(R2.id.ry_container)
    FitWindowsFrameLayout mContainer;
    @BindView(R2.id.ly_bottom_comment)
    RelativeLayout mFloorBar;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.top_bar)
    FrameLayout mTopBar;
    @BindView(R2.id.iv_top_share)
    ColorImageView mIvShare;
    @BindView(R2.id.v_container)
    FrameLayout mView;

    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.fl_comment)
    RelativeLayout mFyContainer;
    @BindView(R2.id.ly_comment_num)
    RelativeLayout ly_comment_num;
    @BindView(R2.id.menu_setting_relpace)
    ImageView ivSettingReplace;
    @BindView(R2.id.menu_setting)
    ImageView ivSetting;


    private TopicAdapter mAdapter;

    // 加载更多
    private FooterLoadMore mLoadMore;
    private HeaderTopicTop mTopicTop;

    // top bar
    private TopBarHolder mTopBarHolder;
    // overlay
    private OverlyHolder mOverlyHolder;
    private FloorBarHolder mFloorBarHolder;

    //订阅同步广播
    private SubscribeReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_topic_activity);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        initView();
        mReceiver = new SubscribeReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter("subscribe_success"));
        loadData();
    }

    /**
     * 文章ID
     */
    private String mArticleId;
    private DraftDetailBean mDetailData;
    private String mFromChannel;

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                if (data.getQueryParameter(IKey.ID) != null) {
                    mArticleId = data.getQueryParameter(IKey.ID);
                }
                if (data.getQueryParameter(IKey.FROM_CHANNEL) != null) {
                    mFromChannel = data.getQueryParameter(IKey.FROM_CHANNEL);
                }
            }

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mTopBar != null) {
            mTopBar.setVisibility(View.VISIBLE);
        }
        getIntentData(intent);
        loadData();
    }

    private void initView() {
        mMenuComment.setVisibility(View.GONE);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        if (mTopBar != null) {
            mTopBarHolder = new TopBarHolder(mTopBar);
        }
        mOverlyHolder = new OverlyHolder(findViewById(R.id.layout_fixed));
        mFloorBarHolder = new FloorBarHolder(mFloorBar);

        mLoadMore = new FooterLoadMore(mRecycler, this);
        mTopicTop = new HeaderTopicTop(mRecycler);
        if (mTopBarHolder != null) {
            mTopicTop.setTopBar(mTopBarHolder);
        }
        mTopicTop.setOverlayHolder(mOverlyHolder);
        mTopicTop.setFloorBarHolder(mFloorBarHolder);
    }

    @Override
    public boolean isShowTopBar() {
        return false;
    }

    private Analytics.AnalyticsBuilder builder;
    private Analytics mAnalytics;

    /**
     * 初始化/拉取数据
     */
    private void loadData() {
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN); // onCreate和onNewIntent时清空js下发的分享数据
        mTopBarHolder.setShareVisible(false);
        new DraftDetailTask(new LoadingCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean data) {
                if (data == null) return;
                mTopBarHolder.setShareVisible(true);
                if (data.getArticle() != null) {
                    builder = DataAnalyticsUtils.get().pageStayTime(data);
                }
                fillData(data);
                YiDunToken.synYiDunToken(mArticleId);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                //话题撤稿
                if (errCode == C.DRAFFT_IS_NOT_EXISE) {
                    showEmptyNewsDetail();
                } else {
                    T.showShortNow(ActivityTopicActivity.this, errMsg);
                }
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(mArticleId, mFromChannel);
    }

    private void fillData(DraftDetailBean data) {
        mView.setVisibility(View.GONE);
        mIvShare.setVisibility(View.VISIBLE);
        // 记录阅读记录
        if (data != null && data.getArticle() != null) {
            DraftDetailBean.ArticleBean article = data.getArticle();
            ReadNewsDaoHelper.get().asyncRecord(
                    ReadNewsBean.newBuilder().id(article.getId())
                            .mlfId(article.getMlf_id())
                            .tag(article.getList_tag())
                            .title(article.getList_title())
                            .url(article.getUrl()));
            //设置互动的加载更多
            lastMinPublishTime = getLastCommentMinPublishTime(data.getArticle().getTopic_comment_list());
        }

        mDetailData = data;

        if (mAdapter == null) {
            mAdapter = new TopicAdapter(data, mLoadMore);
            mAdapter.addHeaderView(mTopicTop.itemView);
            mAdapter.setEmptyView(
                    new EmptyPageHolder(mRecycler,
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据")
                    ).itemView);
            mRecycler.setAdapter(mAdapter);
        } else {
            mAdapter.setData(data);
            mAdapter.notifyDataSetChanged();
        }

        mTopicTop.setData(data);
        initViewState(data);
    }

    /**
     * 刷新底部栏状态
     *
     * @param data
     */
    private void initViewState(DraftDetailBean data) {
        //不允许点赞及评论
        if (!data.getArticle().isLike_enabled() && data.getArticle().getComment_level() == 0) {
            mFyContainer.setVisibility(View.GONE);
            ly_comment_num.setVisibility(View.GONE);
            mMenuPrised.setVisibility(View.GONE);
            ivSetting.setVisibility(View.GONE);
            ivSettingReplace.setVisibility(View.VISIBLE);
        } else {
            ivSetting.setVisibility(View.VISIBLE);
            ivSettingReplace.setVisibility(View.GONE);

            //是否允许点赞
            if (data.getArticle().isLike_enabled()) {
                mMenuPrised.setVisibility(View.VISIBLE);
                mMenuPrised.setSelected(data.getArticle().isLiked());
            } else {
                mMenuPrised.setVisibility(View.GONE);
            }

            //禁止评论，隐藏评论框及评论按钮
            if (data.getArticle().getComment_level() == 0) {
                mFyContainer.setVisibility(View.GONE);
                ly_comment_num.setVisibility(View.GONE);
            } else {
                mFyContainer.setVisibility(View.VISIBLE);
                ly_comment_num.setVisibility(View.VISIBLE);
                //大致评论数量
                if (!TextUtils.isEmpty(data.getArticle().getComment_count_general())) {
                    mTvCommentsNum.setVisibility(View.VISIBLE);
                    mTvCommentsNum.setText(data.getArticle().getComment_count_general());
                } else {
                    mTvCommentsNum.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 订阅操作
     */
    @Override
    public void onOptSubscribe() {
        new ColumnSubscribeTask(new LoadingCallBack<Void>() {

            @Override
            public void onSuccess(Void baseInnerData) {
                mAdapter.updateSubscribeInfo();
                T.showShort(getBaseContext(), getString(R.string.module_detail_subscribe_success));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShortNow(ActivityTopicActivity.this, errMsg);
            }

        }).setTag(this).exe(mDetailData.getArticle().getColumn_id(), true);

    }

    @Override
    public void onOptPageFinished() {
        if (mAdapter.getFooterCount() == 0) {
            mAdapter.addFooterView(mLoadMore.getItemView());
            mAdapter.addFooterView(new FooterPlaceHolder(mRecycler).getItemView());
        }
        mAdapter.showAll();
    }

    private float mScale;

    @Override
    public void onReadingScaleChange(float scale) {
        mScale = scale;
    }

    /**
     * 点赞操作
     */
    public void onOptFabulous() {
        if (mDetailData == null) return;
        // 点赞
        if (mDetailData.getArticle().isLiked()) {
            T.showNow(this, getString(R.string.module_detail_you_have_liked), Toast.LENGTH_SHORT);
            return;
        }
        new DraftPraiseTask(new LoadingCallBack<Void>() {

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                if (errCode == 50013) {
                    mDetailData.getArticle().setLiked(true);
                    mMenuPrised.setSelected(true);
                    T.showShort(getBaseContext(), "已点赞成功");
                } else {
                    T.showShort(getBaseContext(), errMsg);
                }
            }

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_prise_success));
                mDetailData.getArticle().setLiked(true);
                mMenuPrised.setSelected(true);
            }
        }).setTag(this).exe(mArticleId, true, mDetailData.getArticle().getUrl());
    }

    @OnClick({R2.id.menu_prised, R2.id.menu_setting,
            R2.id.tv_comment, R2.id.iv_top_share, R2.id.iv_top_back,
            R2.id.tv_top_bar_subscribe_text, R2.id.tv_top_bar_title, R2.id.menu_setting_relpace})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        //点赞
        if (view.getId() == R.id.menu_prised) {
            DataAnalyticsUtils.get().ClickPriseIcon(mDetailData);
            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting || view.getId() == R.id.menu_setting_relpace) {
            if (mDetailData != null && mDetailData.getArticle() != null) {
                DataAnalyticsUtils.get().ClickMoreIcon(mDetailData);
                MoreDialog.newInstance(mDetailData).setWebViewCallBack(mAdapter.getWebViewHolder
                        (), mAdapter.getWebViewHolder()).show(getSupportFragmentManager(),
                        "MoreDialog");
            }
            //评论框
        } else if (view.getId() == R.id.tv_comment) {
            if (mDetailData != null && mDetailData.getArticle() != null) {
                DataAnalyticsUtils.get().ClickCommentBox(mDetailData);

                //评论发表成功
                Analytics analytics = DataAnalyticsUtils.get().CreateCommentAnalytics(mDetailData);
                //进入评论编辑页面(不针对某条评论)
                try {
                    CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(String
                            .valueOf(mDetailData.getArticle().getId())))).setWMData(analytics).setLocationCallBack(this).show
                            (getSupportFragmentManager(), "CommentWindowDialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //分享
        } else if (view.getId() == R.id.iv_top_share) {
            if (mDetailData != null && mDetailData.getArticle() != null && !TextUtils.isEmpty
                    (mDetailData.getArticle().getUrl())) {
                //分享专用bean
                OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                        .setObjectID(mDetailData.getArticle().getMlf_id() + "")
                        .setObjectName(mDetailData.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mDetailData.getArticle().getChannel_id() + "")
                        .setClassifyName(mDetailData.getArticle().getChannel_name())
                        .setUrl(mDetailData.getArticle().getUrl())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mDetailData.getArticle().getColumn_id() + "")
                                .put("subject", mDetailData.getArticle().getId() + "")
                                .toString())
                        .setSelfobjectID(mDetailData.getArticle().getId() + "");

                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setNewsCard(true)
                        .setCardUrl(mDetailData.getArticle().getCard_url())
                        .setArticleId(mDetailData.getArticle().getId() + "")
                        .setImgUri(mDetailData.getArticle().getFirstPic())
                        .setTextContent(mDetailData.getArticle().getSummary())
                        .setTitle(mDetailData.getArticle().getDoc_title())
                        .setAnalyticsBean(bean)
                        .setTargetUrl(mDetailData.getArticle().getUrl()).setEventName("NewsShare")
                        .setShareType("文章"));
            }

        } else if (view.getId() == R.id.iv_top_back) {
            DataAnalyticsUtils.get().ClickBack(mDetailData);
            finish();
        } else if (view.getId() == R.id.tv_top_bar_subscribe_text) {
            //已订阅状态->取消订阅
            if (mTopBarHolder.getSubscribe().isSelected()) {
                DataAnalyticsUtils.get().SubscribeAnalytics(mDetailData, "订阅号取消订阅", "A0114", "SubColumn", "取消订阅");
                new ColumnSubscribeTask(new LoadingCallBack<Void>() {

                    @Override
                    public void onSuccess(Void baseInnerData) {
                        mTopBarHolder.getSubscribe().setSelected(false);
                        mTopBarHolder.getSubscribe().setText("+订阅");
                        SyncSubscribeColumn(false, mDetailData.getArticle().getColumn_id());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(String errMsg, int errCode) {
                        T.showShortNow(ActivityTopicActivity.this, "取消订阅失败");
                    }

                }).setTag(this).exe(mDetailData.getArticle().getColumn_id(), false);
            } else {//未订阅状态->订阅
                if (!mTopBarHolder.getSubscribe().isSelected()) {
                    DataAnalyticsUtils.get().SubscribeAnalytics(mDetailData, "订阅号订阅", "A0014", "SubColumn", "订阅");
                    new ColumnSubscribeTask(new LoadingCallBack<Void>() {

                        @Override
                        public void onSuccess(Void baseInnerData) {
                            mTopBarHolder.getSubscribe().setSelected(true);
                            mTopBarHolder.getSubscribe().setText("已订阅");
                            SyncSubscribeColumn(true, mDetailData.getArticle().getColumn_id());
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(String errMsg, int errCode) {
                            T.showShortNow(ActivityTopicActivity.this, "订阅失败");
                        }

                    }).setTag(this).exe(mDetailData.getArticle().getColumn_id(), true);
                }

            }
            //进入栏目
        } else if (view.getId() == R.id.tv_top_bar_title) {
            DataAnalyticsUtils.get().SubscribeAnalytics(mDetailData, "点击进入栏目详情页", "800031", "ToDetailColumn", "");
            if (!TextUtils.isEmpty(mDetailData.getArticle().getColumn_url())) {
                Nav.with(UIUtils.getContext()).toPath(mDetailData.getArticle().getColumn_url());
            }
        }
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mIvShare.setVisibility(View.GONE);
        mView.setVisibility(View.VISIBLE);
        mTopBar.setVisibility(View.GONE);
        mFloorBar.setVisibility(View.GONE);
        mTopBarHolder.getSubscribeRelativeLayout().setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.v_container, EmptyStateFragment.newInstance()).commit();
    }

    private long lastMinPublishTime = 0L;


    /**
     * 互动评论加载更多成功
     *
     * @param data
     * @param loadMore
     */
    @Override
    public void onLoadMoreSuccess(CommentRefreshBean data, LoadMore loadMore) {
        if (data != null && data.getComments() != null) {
            List<HotCommentsBean> commentList = data.getComments();
            if (commentList.size() > 0) {
                lastMinPublishTime = getLastCommentMinPublishTime(commentList);
            }
            mAdapter.addData(commentList, true);
            if (commentList.size() == 0) {
                loadMore.setState(LoadMore.TYPE_NO_MORE);
            }
        } else {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    /**
     * 互动评论加载更多
     *
     * @param callback
     */
    @Override
    public void onLoadMore(LoadingCallBack<CommentRefreshBean> callback) {
        new CommentListTask(callback, false).setTag(this).exe(mArticleId, lastMinPublishTime);
    }


    /**
     * @return 获取最后一次互动评论刷新的时间戳
     */
    private long getLastCommentMinPublishTime(List<HotCommentsBean> list) {
        if (list != null && list.size() > 0) {
            int size = list.size();
            if (size > 0) {
                int count = 1;
                while (size - count >= 0) {
                    HotCommentsBean data = list.get(size - count++);
                    return data.getSort_number();
                }
            }
        }

        return 0;
    }

    /**
     * 删除评论，局部刷新
     */
    @Override
    public void onDeleteComment(int position) {
        mAdapter.remove(position);
    }

    @Override
    protected void onDestroy() {
        //阅读深度
        if (mDetailData != null && mDetailData.getArticle() != null) {
            if (builder != null) {
                builder.setPercentage(mScale + "");
                builder.readPercent(mScale + "");
                mAnalytics = builder.build();
                if (mAnalytics != null) {
                    mAnalytics.sendWithDuration();
                }
            }
        }
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    /**
     * 点击评论时,获取用户所在位置
     */
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

    /**
     * 同步订阅栏目
     */
    private void SyncSubscribeColumn(boolean isSubscribe, int columnid) {
        Intent intent = new Intent("subscribe_success");
        intent.putExtra("subscribe", isSubscribe);
        intent.putExtra("id", (long) columnid);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    /**
     * 订阅同步
     *
     * @param intent
     */
    @Override
    public void subscribeSync(Intent intent) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction()) && "subscribe_success".equals(intent.getAction())) {
            long id = intent.getLongExtra("id", 0);
            boolean subscribe = intent.getBooleanExtra("subscribe", false);
            //确定是该栏目需要同步
            if (id == mDetailData.getArticle().getColumn_id()) {
                mTopBarHolder.getSubscribe().setSelected(subscribe);
                if (subscribe) {
                    DataAnalyticsUtils.get().SubscribeAnalytics(mDetailData, "订阅号订阅", "A0014", "SubColumn", "订阅");
                } else {
                    DataAnalyticsUtils.get().SubscribeAnalytics(mDetailData, "订阅号取消订阅", "A0114", "SubColumn", "取消订阅");
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //新华智云
        if (mDetailData != null && mDetailData.getArticle() != null) {
            new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.comeIn)
                    .setTargetID(mDetailData.getArticle().getId() + "")
                    .setUrl(mDetailData.getArticle().getUrl())
                    .build()
                    .send();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //新华智云
        if (mDetailData != null && mDetailData.getArticle() != null) {
            new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.leave)
                    .setTargetID(mDetailData.getArticle().getId() + "")
                    .setUrl(mDetailData.getArticle().getUrl())
                    .build()
                    .send();
        }
    }
}
