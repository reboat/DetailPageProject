package com.zjrb.zjxw.detailproject.topic;

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
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.api.callback.LocationCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.OutSizeAnalyticsBean;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.holder.EmptyPageHolder;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.ui.widget.web.ZBJsInterface;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.CommentRefreshBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.broadCast.SubscribeReceiver;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.interFace.DetailWMHelperInterFace;
import com.zjrb.zjxw.detailproject.interFace.SubscribeSyncInterFace;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.task.CommentListTask;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.topic.adapter.TopicAdapter;
import com.zjrb.zjxw.detailproject.topic.holder.FloorBarHolder;
import com.zjrb.zjxw.detailproject.topic.holder.FooterPlaceHolder;
import com.zjrb.zjxw.detailproject.topic.holder.HeaderTopicTop;
import com.zjrb.zjxw.detailproject.topic.holder.OverlyHolder;
import com.zjrb.zjxw.detailproject.topic.holder.TopBarHolder;
import com.zjrb.zjxw.detailproject.topic.widget.ColorImageView;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;
import com.zjrb.zjxw.detailproject.utils.YiDunToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * 话题详情页
 * Created by wanglinjie.
 * create time:2017/9/13  上午9:08
 */
public class ActivityTopicActivity extends BaseActivity implements
        TopicAdapter.CommonOptCallBack, LoadMoreListener<CommentRefreshBean>,
        DetailCommentHolder.deleteCommentListener, LocationCallBack,
        SubscribeSyncInterFace, DetailWMHelperInterFace.TopicDetailWM {

    @BindView(R2.id.recycler)
    RecyclerView mRecycler;

    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.ry_container)
    FitWindowsFrameLayout mContainer;
    @BindView(R2.id.ly_bottom_comment)
    RelativeLayout mFloorBar;
    @BindView(R2.id.fl_comment)
    FrameLayout mFyContainer;
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

//    private Analytics.AnalyticsBuilder builder1;
//    private Analytics mAnalytics1;

    /**
     * 初始化/拉取数据
     */
    private void loadData() {
        SPHelper.get().remove(ZBJsInterface.ZJXW_JS_SHARE_BEAN); // onCreate和onNewIntent时清空js下发的分享数据
        mTopBarHolder.setShareVisible(false);
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean data) {
                if (data == null) return;
                mTopBarHolder.setShareVisible(true);
                if (data.getArticle() != null) {
                    builder = pageStayTime(data);
//                    builder1 = pageStayTime2(data);
                }
                fillData(data);
                YiDunToken.synYiDunToken(mArticleId);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //话题撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
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
        //是否可以点赞
        if (data.getArticle().isLike_enabled()) {
            mMenuPrised.setVisibility(View.VISIBLE);
            mMenuPrised.setSelected(data.getArticle().isLiked());
        } else {
            mMenuPrised.setVisibility(View.GONE);
        }

        //禁止评论，隐藏评论框及评论按钮
        if (data.getArticle().getComment_level() == 0) {
            mFyContainer.setVisibility(View.GONE);
            mTvCommentsNum.setVisibility(View.GONE);
        } else {
            mFyContainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 订阅操作
     */
    @Override
    public void onOptSubscribe() {
        new ColumnSubscribeTask(new APIExpandCallBack<Void>() {

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_subscribe_success));
            }

            @Override
            public void onAfter() {
                mAdapter.updateSubscribeInfo();
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
        new DraftPraiseTask(new APIExpandCallBack<Void>() {

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
            R2.id.tv_top_bar_subscribe_text, R2.id.tv_top_bar_title})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        //点赞
        if (view.getId() == R.id.menu_prised) {
            ClickPriseIcon();
            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting) {
            if (mDetailData != null && mDetailData.getArticle() != null) {
                ClickMoreIcon();
                MoreDialog.newInstance(mDetailData).setWebViewCallBack(mAdapter.getWebViewHolder
                        (), mAdapter.getWebViewHolder()).show(getSupportFragmentManager(),
                        "MoreDialog");
            }
            //评论框
        } else if (view.getId() == R.id.tv_comment) {
            if (mDetailData != null && mDetailData.getArticle() != null) {
                ClickCommentBox();

                //评论发表成功
                Analytics analytics = new Analytics.AnalyticsBuilder(getActivity(), "A0023", "A0023", "Comment", false)
                        .setEvenName("发表评论，且发送成功")
                        .setObjectID(mDetailData.getArticle().getMlf_id() + "")
                        .setObjectName(mDetailData.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mDetailData.getArticle().getChannel_id())
                        .setClassifyName(mDetailData.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mDetailData.getArticle().getColumn_id() + "")
                                .put("subject", mDetailData.getArticle().getId() + "")
                                .toString())
                        .setSelfObjectID(mDetailData.getArticle().getId() + "").newsID(mDetailData.getArticle().getMlf_id() + "")
                        .selfNewsID(mDetailData.getArticle().getId() + "")
                        .newsTitle(mDetailData.getArticle().getDoc_title())
                        .selfChannelID(mDetailData.getArticle().getChannel_id())
                        .channelName(mDetailData.getArticle().getChannel_name())
                        .pageType("新闻详情页")
                        .commentType("文章")
                        .build();

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
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mDetailData.getArticle().getColumn_id() + "")
                                .put("subject", mDetailData.getArticle().getId() + "")
                                .toString())
                        .setSelfobjectID(mDetailData.getArticle().getId() + "");

                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setArticleId(mDetailData.getArticle().getId() + "")
                        .setImgUri(mDetailData.getArticle().getFirstPic())
                        .setTextContent(mDetailData.getArticle().getSummary())
                        .setTitle(mDetailData.getArticle().getDoc_title())
                        .setAnalyticsBean(bean)
                        .setTargetUrl(mDetailData.getArticle().getUrl()).setEventName("NewsShare")
                        .setShareType("文章"));
            }

        } else if (view.getId() == R.id.iv_top_back) {
            ClickBack();
            finish();
        } else if (view.getId() == R.id.tv_top_bar_subscribe_text) {
            //已订阅状态->取消订阅
            if (mTopBarHolder.getSubscribe().isSelected()) {
                SubscribeAnalytics("点击\"取消订阅\"栏目", "A0114", "SubColumn", "取消订阅");
                new ColumnSubscribeTask(new APIExpandCallBack<Void>() {

                    @Override
                    public void onSuccess(Void baseInnerData) {
                        mTopBarHolder.getSubscribe().setSelected(false);
                        mTopBarHolder.getSubscribe().setText("+订阅");
                        SyncSubscribeColumn(false, mDetailData.getArticle().getColumn_id());
                    }

                    @Override
                    public void onError(String errMsg, int errCode) {
                        T.showShortNow(ActivityTopicActivity.this, "取消订阅失败");
                    }

                }).setTag(this).exe(mDetailData.getArticle().getColumn_id(), false);
            } else {//未订阅状态->订阅
                if (!mTopBarHolder.getSubscribe().isSelected()) {
                    SubscribeAnalytics("点击\"订阅\"栏目", "A0014", "SubColumn", "订阅");
                    new ColumnSubscribeTask(new APIExpandCallBack<Void>() {

                        @Override
                        public void onSuccess(Void baseInnerData) {
                            mTopBarHolder.getSubscribe().setSelected(true);
                            mTopBarHolder.getSubscribe().setText("已订阅");
                            SyncSubscribeColumn(true, mDetailData.getArticle().getColumn_id());
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
            SubscribeAnalytics("点击进入栏目详情页", "800031", "ToDetailColumn", "");
            Bundle bundle = new Bundle();
            bundle.putString(IKey.ID, String.valueOf(mDetailData.getArticle().getColumn_id()));
            Nav.with(UIUtils.getContext()).setExtras(bundle)
                    .toPath("/subscription/detail");
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
            //TODO 20条将不再作为无数据的依据
            if (commentList.size() == 0) {
                loadMore.setState(LoadMore.TYPE_NO_MORE);
            }
//            if (commentList.size() < C.PAGE_SIZE) {
//                loadMore.setState(LoadMore.TYPE_NO_MORE);
//            }

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

            //5.6SB需求
//            if (builder1 != null) {
//                mAnalytics1 = builder1.build();
//                if (mAnalytics1 != null) {
//                    mAnalytics1.sendWithDuration();
//                }
//            }


        }
        SPHelper.get().remove(ZBJsInterface.ZJXW_JS_SHARE_BEAN);
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
            String subscriptionText = subscribe ? "已订阅" : "+订阅";
            //确定是该栏目需要同步
            if (id == mDetailData.getArticle().getColumn_id()) {
                mTopBarHolder.getSubscribe().setSelected(subscribe);
                mTopBarHolder.getSubscribe().setText(subscriptionText);
//                if (subscribe) {
//                    SubscribeAnalytics("点击\"订阅\"栏目", "A0114", "SubColumn", "订阅");
//                } else {
//                    SubscribeAnalytics("点击\"取消订阅\"栏目", "A0014", "SubColumn", "取消订阅");
//                }
            }
        }
    }

    @Override
    public void SubscribeAnalytics(String eventNme, String eventCode, String scEventName, String operationType) {
        new Analytics.AnalyticsBuilder(getContext(), eventCode, eventCode, scEventName, false)
                .setEvenName(eventNme)
                .setObjectID(mDetailData.getArticle().getMlf_id() + "")
                .setObjectName(mDetailData.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(mDetailData.getArticle().getChannel_id())
                .setClassifyName(mDetailData.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("customObjectType", "RelatedColumnType")
                        .toString())
                .setSelfObjectID(mDetailData.getArticle().getId() + "")
                .columnID(mDetailData.getArticle().getColumn_id() + "")
                .columnName(mDetailData.getArticle().getColumn_name())
                .pageType("新闻详情页")
                .operationType(operationType)
                .build()
                .send();
    }

    @Override
    public Analytics.AnalyticsBuilder pageStayTime(DraftDetailBean bean) {
        return new Analytics.AnalyticsBuilder(ActivityTopicActivity.this, "A0010",
                "800021", "ViewAppNewsDetail", true)
                .setEvenName("页面停留时长/阅读深度")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", bean.getArticle().getId() + "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("新闻详情页")
                .pubUrl(bean.getArticle().getUrl());
    }

    public Analytics.AnalyticsBuilder pageStayTime2(DraftDetailBean bean) {
        return new Analytics.AnalyticsBuilder(ActivityTopicActivity.this, "A0010",
                "800021", "PageStay", true)
                .setEvenName("新闻详情页停留时长")
                .setPageType("新闻详情页")
                .pageType("新闻详情页");
    }

    @Override
    public void ClickPriseIcon() {
        if (mDetailData != null && mDetailData.getArticle() != null) {
            new Analytics.AnalyticsBuilder(getActivity(), "A0021", "A0021", "Support", false)
                    .setEvenName("点击点赞")
                    .setObjectID(mDetailData.getArticle().getMlf_id() + "")
                    .setObjectName(mDetailData.getArticle().getDoc_title())
                    .setObjectType(ObjectType.NewsType)
                    .setClassifyID(mDetailData.getArticle().getChannel_id())
                    .setClassifyName(mDetailData.getArticle().getChannel_name())
                    .setPageType("新闻详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", "SubjectType")
                            .put("subject", mDetailData.getArticle().getId() + "")
                            .toString())
                    .setSelfObjectID(mDetailData.getArticle().getId() + "").newsID(mDetailData.getArticle().getMlf_id() + "")
                    .selfNewsID(mDetailData.getArticle().getId() + "")
                    .newsTitle(mDetailData.getArticle().getDoc_title())
                    .selfChannelID(mDetailData.getArticle().getChannel_id())
                    .channelName(mDetailData.getArticle().getChannel_name())
                    .pageType("新闻详情页")
                    .supportType("文章")
                    .build()
                    .send();
        }
    }

    @Override
    public void ClickMoreIcon() {
        new Analytics.AnalyticsBuilder(getActivity(), "800005", "800005", "AppTabClick", false)
                .setEvenName("点击更多")
                .setObjectID(mDetailData.getArticle().getMlf_id() + "")
                .setObjectName(mDetailData.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(mDetailData.getArticle().getChannel_id())
                .setClassifyName(mDetailData.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", mDetailData.getArticle().getColumn_id() + "")
                        .put("subject", mDetailData.getArticle().getId() + "")
                        .toString())
                .setSelfObjectID(mDetailData.getArticle().getId() + "").pageType("新闻详情页")
                .clickTabName("更多")
                .build()
                .send();
    }

    @Override
    public void ClickCommentBox() {
        new Analytics.AnalyticsBuilder(getActivity(), "800002", "800002", "AppTabClick", false)
                .setEvenName("点击评论输入框")
                .setObjectID(mDetailData.getArticle().getMlf_id() + "")
                .setObjectName(mDetailData.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(mDetailData.getArticle().getChannel_id())
                .setClassifyName(mDetailData.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", mDetailData.getArticle().getColumn_id() + "")
                        .put("subject", mDetailData.getArticle().getId() + "")
                        .toString())
                .setSelfObjectID(mDetailData.getArticle().getId() + "").pageType("新闻详情页")
                .clickTabName("评论输入框")
                .build()
                .send();
    }

    @Override
    public void ClickBack() {
        if (mDetailData != null && mDetailData.getArticle() != null) {
            new Analytics.AnalyticsBuilder(getActivity(), "800001", "800001", "AppTabClick", false)
                    .setEvenName("点击返回")
                    .setObjectID(mDetailData.getArticle().getMlf_id() + "")
                    .setObjectName(mDetailData.getArticle().getDoc_title())
                    .setObjectType(ObjectType.NewsType)
                    .setClassifyID(mDetailData.getArticle().getChannel_id())
                    .setClassifyName(mDetailData.getArticle().getChannel_name())
                    .setPageType("新闻详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", mDetailData.getArticle().getColumn_id() + "")
                            .put("subject", mDetailData.getArticle().getId() + "")
                            .toString())
                    .setSelfObjectID(mDetailData.getArticle().getId() + "").pageType("新闻详情页").clickTabName("返回")
                    .build()
                    .send();
        }
    }
}
