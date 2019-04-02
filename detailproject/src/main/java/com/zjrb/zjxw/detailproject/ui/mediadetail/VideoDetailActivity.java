package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aliya.dailyplayer.sub.DailyPlayerManager;
import com.aliya.dailyplayer.sub.OnPlayerManagerCallBack;
import com.aliya.dailyplayer.sub.PlayerCache;
import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.aliya.view.ratio.RatioFrameLayout;
import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.utils.BundleHelper;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.daily.news.ui.widget.SlidingTabLayout;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.callback.DetailInterface;
import com.zjrb.zjxw.detailproject.ui.boardcast.CommentNumReceiver;
import com.zjrb.zjxw.detailproject.ui.boardcast.SubscribeReceiver;
import com.zjrb.zjxw.detailproject.ui.boardcast.VideoReceiver;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.ui.persionaldetail.adapter.TabPagerAdapterImpl;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;
import com.zjrb.zjxw.detailproject.utils.YiDunToken;
import com.zjrb.zjxw.detailproject.utils.global.C;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.glide.AppGlideOptions;
import cn.daily.news.biz.core.glide.PH;
import cn.daily.news.biz.core.model.CommentDialogBean;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;
import cn.daily.news.biz.core.ui.dialog.CommentWindowDialog;
import cn.daily.news.biz.core.ui.toolsbar.BIZTopBarFactory;
import cn.daily.news.biz.core.ui.toolsbar.holder.CommonTopBarHolder;
import cn.daily.news.biz.core.web.JsMultiInterfaceImp;
import cn.daily.news.update.util.NetUtils;

import static com.aliya.dailyplayer.FullscreenActivity.KEY_URL;
import static com.zjrb.core.utils.UIUtils.getContext;
import static com.zjrb.zjxw.detailproject.ui.mediadetail.VideoCommentFragment.FRAGMENT_DETAIL_COMMENT;
import static com.zjrb.zjxw.detailproject.ui.mediadetail.VideoDetailFragment.FRAGMENT_DETAIL_BEAN;

/**
 * 视频/直播页面
 * Created by wanglinjie.
 * create time:2019/3/22  上午9:05
 */
final public class VideoDetailActivity extends DailyActivity implements DetailInterface.CommentInterFace, NewsDetailAdapter.CommonOptCallBack,
        CommentWindowDialog.LocationCallBack, DetailInterface.SubscribeSyncInterFace,
        DetailInterface.VideoBCnterFace, OnPlayerManagerCallBack {

    @BindView(R2.id.iv_image)
    ImageView ivImage;
    @BindView(R2.id.video_container)
    RatioFrameLayout videoContainer;
    @BindView(R2.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R2.id.menu_prised)
    ImageView menuPrised;
    @BindView(R2.id.v_container)
    FrameLayout vContainer;
    @BindView(R2.id.ry_container)
    RelativeLayout ryContainer;
    @BindView(R2.id.ly_bottom_comment)
    FitWindowsRelativeLayout mFloorBar;
    @BindView(R2.id.viewpager)
    ViewPager viewPager;
    @BindView(R2.id.ly_comment_num)
    RelativeLayout lyComment;
    @BindView(R2.id.iv_play)
    ImageView ivPlay;


    private int ui;
    public String mArticleId;
    private String mFromChannel;
    private DraftDetailBean mNewsDetail;
    private VideoReceiver receive;
    private LocalBroadcastManager localBroadcastManager;
    private SubscribeReceiver mReceiver;
    private CommentNumReceiver commentNumReceiver;
    private TabPagerAdapterImpl pagerAdapter;
    private VideoDetailFragment videoDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_video_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        ui = getWindow().getDecorView().getSystemUiVisibility();
        lyComment.setVisibility(View.GONE);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        receive = new VideoReceiver(this);
        IntentFilter intentFilter = new IntentFilter(UIUtils.getString(R.string.intent_action_close_video));
        intentFilter.addAction(UIUtils.getString(R.string.intent_action_open_video));
        localBroadcastManager.registerReceiver(receive, intentFilter);
        mReceiver = new SubscribeReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter("subscribe_success"));
        commentNumReceiver = new CommentNumReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(commentNumReceiver, new IntentFilter("sync_comment_num"));

        getIntentData(getIntent());
        loadData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
        loadData();
    }

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

    public CommonTopBarHolder topHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = BIZTopBarFactory.createCommonTopBar(view, this);
        return topHolder.getView();
    }

    /**
     * 判断是否竖视频
     *
     * @param bean
     */
    private boolean isVertical(DraftDetailBean.ArticleBean bean) {
        if (TextUtils.isEmpty(bean.getVideo_url())) {
            return false;
        }
        Uri uri = Uri.parse(bean.getVideo_url());
        String type = uri.getQueryParameter("isVertical");//1 竖视频 2普通
        boolean isVertical = Integer.valueOf(type) == 1;
        return isVertical;
    }


    //初始化视频UI
    private void initVideo(DraftDetailBean.ArticleBean bean) {
        String url = bean.getVideo_url();
        String title = bean.getDoc_title();
        String imagePath = mNewsDetail.getArticle().getList_pics().get(0);
        if (bean.isNative_live() || !TextUtils.isEmpty(url)) {
            //UI
            videoContainer.setVisibility(View.VISIBLE);
            GlideApp.with(ivImage).load(imagePath).placeholder(PH.zheBig()).centerCrop()
                    .apply(AppGlideOptions.bigOptions()).into(ivImage);

            if (bean.isNative_live()) {
                url = bean.getLive_url();
                title = bean.getNative_live_info().getTitle();
                imagePath = bean.getNative_live_info().getCover();
                //直播回放并且回放地址不为空  取直播回放地址
                if (bean.getNative_live_info().getStream_status() == 2) {
                    if (!TextUtils.isEmpty(bean.getNative_live_info().getPlayback_url())) {
                        url = bean.getNative_live_info().getPlayback_url();
                    } else {
                        url = "";
                    }
                }
            }


            DailyPlayerManager.Builder builder = new DailyPlayerManager.Builder(this)
                    .setImageUrl(imagePath)
                    .setPlayUrl(url)
                    .setLive(bean.isNative_live())
                    .setStreamStatus(bean.getLive_status())
                    .setVertical(isVertical(bean))
                    .setOnPlayerManagerCallBack(this)
                    .setTitle(title)
                    .setPlayContainer(videoContainer);
            if (PlayerCache.get().getPlayer(url) != null && PlayerCache.get().getPlayer(url).getPlayWhenReady()) {//播放器正在播放
                DailyPlayerManager.get().play(builder);
            } else {
                DailyPlayerManager.get().init(builder);
            }
        } else {
            videoContainer.setVisibility(View.GONE);
        }

        if (!NetUtils.isAvailable(getApplication())) {
            T.showShort(getContext(), getString(R.string.module_detail_no_network));
        }
    }

    //初始化视频/评论fragment
    private void initViewPage(DraftDetailBean bean) {
        pagerAdapter = new TabPagerAdapterImpl(getSupportFragmentManager(), this);
        //视频/简介
        Bundle bundleVideo = BundleHelper.creatBundle(IKey
                .FRAGMENT_ARGS, VideoDetailFragment.FRAGMENT_DETAIL_VIDEO);
        bundleVideo.putSerializable(FRAGMENT_DETAIL_BEAN, bean);
        if (bean.getArticle().isNative_live()) {
            pagerAdapter.addTabInfo(VideoDetailFragment.class, "简介", bundleVideo);
        } else {
            pagerAdapter.addTabInfo(VideoDetailFragment.class, "视频", bundleVideo);
        }
        videoDetailFragment = (VideoDetailFragment) pagerAdapter.getItem(0);

        //直播间
        if (bean.getArticle().isNative_live()) {
            Bundle bundleLive = BundleHelper.creatBundle(IKey
                    .FRAGMENT_ARGS, VideoLiveFragment.FRAGMENT_VIDEO_LIVE);
            bundleLive.putSerializable(FRAGMENT_DETAIL_BEAN, bean);
            pagerAdapter.addTabInfo(VideoLiveFragment.class, "直播间", bundleLive);
        }
        //评论
        Bundle bundleComment = BundleHelper.creatBundle(IKey
                .FRAGMENT_ARGS, FRAGMENT_DETAIL_COMMENT);
        bundleComment.putSerializable(FRAGMENT_DETAIL_BEAN, bean);
        if (bean.getArticle().getComment_count() > 0) {
            pagerAdapter.addTabInfo(VideoCommentFragment.class, "评论 (" + bean.getArticle().getComment_count() + ")", bundleComment);
        } else {
            pagerAdapter.addTabInfo(VideoCommentFragment.class, "评论", bundleComment);
        }

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(viewPager);
    }

    private void loadData() {
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        if (mArticleId == null || mArticleId.isEmpty()) return;
        DraftDetailTask task = new DraftDetailTask(new LoadingCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;

                if (vContainer.getVisibility() == View.VISIBLE) {
                    vContainer.setVisibility(View.GONE);
                }
                mNewsDetail = draftDetailBean;
                if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                    initVideo(mNewsDetail.getArticle());
                    initViewPage(mNewsDetail);
                }
                fillData(mNewsDetail);
                YiDunToken.synYiDunToken(mArticleId);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                //撤稿
                if (errCode == C.DRAFFT_IS_NOT_EXISE) {
                    topHolder.getShareView().setVisibility(View.GONE);
                    showEmptyNewsDetail();
                } else {
                    T.showShortNow(getApplication(), errMsg);
                }
            }
        });
        task.setTag(this).bindLoadViewHolder(replaceLoad(ryContainer)).exe(mArticleId, mFromChannel);
    }

    public void fillData(DraftDetailBean data) {
        mFloorBar.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        DraftDetailBean.ArticleBean article = data.getArticle();
        if (article != null) {
            ReadNewsDaoHelper.get().asyncRecord(
                    ReadNewsBean.newBuilder().id(article.getId())
                            .mlfId(article.getMlf_id())
                            .tag(article.getList_tag())
                            .title(article.getList_title())
                            .url(article.getUrl()));
        }

        topHolder.setViewVisible(topHolder.getShareView(), View.VISIBLE);
        //中间栏目布局处理
        if (!TextUtils.isEmpty(article.getColumn_name())) {
            //栏目名称
            topHolder.setViewVisible(topHolder.getFitRelativeLayout(), View.VISIBLE);
            topHolder.getTitleView().setText(article.getColumn_name());
            //栏目头像
            GlideApp.with(topHolder.getIvIcon()).load(article.getColumn_logo()).placeholder(R.mipmap.ic_top_bar_redboat_icon)
                    .error(R.mipmap.ic_top_bar_redboat_icon).centerCrop().into(topHolder.getIvIcon());
            if (article.isColumn_subscribed()) {
                topHolder.getSubscribe().setSelected(true);
            } else {
                topHolder.getSubscribe().setSelected(false);
            }
        } else {
            topHolder.setViewVisible(topHolder.getFitRelativeLayout(), View.GONE);
        }

        mNewsDetail = data;
        initViewState(mNewsDetail);
    }

    private void initViewState(DraftDetailBean data) {
        //是否已点赞
        if (data.getArticle().isLike_enabled()) {
            menuPrised.setVisibility(View.VISIBLE);
            menuPrised.setSelected(data.getArticle().isLiked());
        } else {
            menuPrised.setVisibility(View.GONE);
        }
    }

    @OnClick({R2.id.menu_prised, R2.id.menu_setting,
            R2.id.tv_comment, R2.id.iv_top_share, R2.id.iv_top_bar_back,
            R2.id.tv_top_bar_subscribe_text, R2.id.tv_top_bar_title})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.menu_prised) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickPriseIcon(mNewsDetail);
            }
            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickMoreIcon(mNewsDetail);
                MoreDialog.newInstance(mNewsDetail).show(getSupportFragmentManager(), "MoreDialog");
            }

            //评论框
        } else if (view.getId() == R.id.tv_comment) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                //进入评论编辑页面(不针对某条评论)
                DataAnalyticsUtils.get().ClickCommentBox(mNewsDetail);

                //评论发表成功
                Analytics analytics = DataAnalyticsUtils.get().CreateCommentAnalytics(mNewsDetail);
                try {
                    CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(mNewsDetail.getArticle().getId()))).setWMData(analytics).setLocationCallBack(this).show(getSupportFragmentManager(), "CommentWindowDialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //分享
        } else if (view.getId() == R.id.iv_top_share) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null && !TextUtils.isEmpty(mNewsDetail.getArticle().getUrl())) {
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
                //分享操作
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setArticleId(mNewsDetail.getArticle().getId() + "")
                        .setImgUri(mNewsDetail.getArticle().getFirstPic())
                        .setAnalyticsBean(bean)
                        .setTextContent(mNewsDetail.getArticle().getSummary())
                        .setTitle(mNewsDetail.getArticle().getDoc_title())
                        .setTargetUrl(mNewsDetail.getArticle().getUrl()).setEventName("NewsShare")
                        .setShareType("文章"));
                //点击分享操作
                DataAnalyticsUtils.get().ClickShare(mNewsDetail);
            }

        } else if (view.getId() == R.id.iv_top_bar_back) {
            //点击返回操作
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickBack(mNewsDetail);
            }
            onBackPressed();
        } else if (view.getId() == R.id.tv_top_bar_subscribe_text) {
            //已订阅状态->取消订阅
            if (topHolder.getSubscribe().isSelected()) {
                DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "点击\"取消订阅\"栏目", "A0114", "SubColumn", "取消订阅");
                new ColumnSubscribeTask(new LoadingCallBack<Void>() {

                    @Override
                    public void onSuccess(Void baseInnerData) {
                        topHolder.getSubscribe().setSelected(false);
                        T.showShortNow(getApplicationContext(), "取消订阅成功");
                        SyncSubscribeColumn(false, mNewsDetail.getArticle().getColumn_id());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(String errMsg, int errCode) {
                        T.showShortNow(getApplication(), "取消订阅失败");
                    }

                }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), false);
            } else {//未订阅状态->订阅
                DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "点击\"订阅\"栏目", "A0014", "SubColumn", "订阅");
                if (!topHolder.getSubscribe().isSelected()) {
                    new ColumnSubscribeTask(new LoadingCallBack<Void>() {

                        @Override
                        public void onSuccess(Void baseInnerData) {
                            topHolder.getSubscribe().setSelected(true);
                            T.showShortNow(getApplicationContext(), "订阅成功");
                            SyncSubscribeColumn(true, mNewsDetail.getArticle().getColumn_id());
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(String errMsg, int errCode) {
                            T.showShortNow(getApplication(), "订阅失败");
                        }

                    }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), true);
                }

            }
            //进入栏目
        } else if (view.getId() == R.id.tv_top_bar_title) {
            DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "点击进入栏目详情页", "800031", "ToDetailColumn", "");
            Bundle bundle = new Bundle();
            bundle.putString(IKey.ID, String.valueOf(mNewsDetail.getArticle().getColumn_id()));
            Nav.with(UIUtils.getContext()).setExtras(bundle)
                    .toPath("/subscription/detail");
        }
    }

    public void onOptFabulous() {
        if (mNewsDetail == null) return;
        // 点赞
        if (mNewsDetail.getArticle().isLiked()) {
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
                    mNewsDetail.getArticle().setLiked(true);
                    menuPrised.setSelected(true);
                    T.showShort(getBaseContext(), "已点赞成功");
                } else {
                    T.showShort(getBaseContext(), errMsg);
                }

            }

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_prise_success));
                mNewsDetail.getArticle().setLiked(true);
                menuPrised.setSelected(true);
            }
        }).setTag(this).exe(mArticleId, true, mNewsDetail.getArticle().getUrl());
    }


    @Override
    protected void onDestroy() {
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(receive);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(commentNumReceiver);
    }

    private void showEmptyNewsDetail() {
        vContainer.setVisibility(View.VISIBLE);
        mFloorBar.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        videoContainer.setVisibility(View.GONE);
        topHolder.setViewVisible(topHolder.getFitRelativeLayout(), View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.v_container, EmptyStateFragment.newInstance()).commit();
    }

    private void SyncSubscribeColumn(boolean isSubscribe, int columnid) {
        Intent intent = new Intent("subscribe_success");
        intent.putExtra("subscribe", isSubscribe);
        intent.putExtra("id", (long) columnid);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DailyPlayerManager.get().onDestroy();
        PlayerCache.get().clear();
    }


    @Override
    public void subscribeSync(Intent intent) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction()) && "subscribe_success".equals(intent.getAction())) {
            long id = intent.getLongExtra("id", 0);
            boolean subscribe = intent.getBooleanExtra("subscribe", false);
            //确定是该栏目需要同步
            if (id == mNewsDetail.getArticle().getColumn_id()) {
                topHolder.getSubscribe().setSelected(subscribe);
                if (subscribe) {
                    DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "点击\"订阅\"栏目", "A0014", "SubColumn", "订阅");
                } else {
                    DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "点击\"取消订阅\"栏目", "A0114", "SubColumn", "取消订阅");
                }
            }
        }
    }

    @Override
    public void videoBC(Intent intent) {
        String action = intent.getAction();
        if (UIUtils.getString(R.string.intent_action_close_video).equals(action)) {
            videoContainer.setVisibility(View.GONE);
        } else if (UIUtils.getString(R.string.intent_action_open_video).equals(action)) {
            videoContainer.setVisibility(View.VISIBLE);
        }
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

    //保持UI状态
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //横屏去掉topbar
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideTopBar();
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            showTopBar();
            getWindow().getDecorView().setSystemUiVisibility(ui);
        }
    }

    @Override
    public void onOptPageFinished() {
        videoDetailFragment.getmAdapter().showAll();
    }

    @Override
    public void onOptClickChannel() {
        videoDetailFragment.onOptClickChannel();
    }

    @Override
    public void onReadingScaleChange(float scale) {
        videoDetailFragment.onReadingScaleChange(scale);
    }

    //同步评论数
    @Override
    public void syncCommentNum(Intent intent) {
        if (intent != null && intent.hasExtra("video_comment_title")) {
            //TODO WLJ 这里更新title没有生效
            if (pagerAdapter.getCount() == 2) {
                pagerAdapter.setPageTitle(1, intent.getStringExtra("video_comment_title"));
            } else {
                pagerAdapter.setPageTitle(2, intent.getStringExtra("video_comment_title"));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DailyPlayerManager.get().onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && !TextUtils.isEmpty(data.getStringExtra(KEY_URL))) {
            DailyPlayerManager.Builder builder = new DailyPlayerManager.Builder(this)
                    .setPlayContainer(videoContainer)
                    .setImageUrl(mNewsDetail.getArticle().getList_pics().get(0))
                    .setPlayUrl(data.getStringExtra(KEY_URL))
                    .setTitle(mNewsDetail.getArticle().getDoc_title())
                    .setVertical(isVertical(mNewsDetail.getArticle()))
                    .setOnPlayerManagerCallBack(this)
                    .setLive(mNewsDetail.getArticle().isNative_live());
            DailyPlayerManager.get().play(builder);
        }
    }

    //视频播放结束的分享按钮
    @Override
    public void onShareClicked(View view) {
        UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                .setSingle(false)
                .setArticleId(mNewsDetail.getArticle().getId() + "")
                .setImgUri(mNewsDetail.getArticle().getFirstPic())
                .setTextContent(mNewsDetail.getArticle().getSummary())
                .setTitle(mNewsDetail.getArticle().getDoc_title())
                .setTargetUrl(mNewsDetail.getArticle().getUrl()).setEventName("NewsShare")
                .setShareType("文章"));
    }
}
