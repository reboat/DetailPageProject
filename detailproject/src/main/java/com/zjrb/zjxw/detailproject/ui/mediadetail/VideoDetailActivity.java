package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.dailyplayer.sub.Constant;
import com.aliya.dailyplayer.sub.DailyPlayerManager;
import com.aliya.dailyplayer.sub.PlayerAction;
import com.aliya.dailyplayer.sub.PlayerCache;
import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.aliya.view.ratio.RatioFrameLayout;
import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.google.android.exoplayer2.Player;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.utils.BundleHelper;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.daily.news.other.Utils;
import com.zjrb.daily.news.ui.widget.SlidingTabLayout;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftMultyPraiseTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.callback.DetailInterface;
import com.zjrb.zjxw.detailproject.callback.DetailPlayerCallBack;
import com.zjrb.zjxw.detailproject.ui.boardcast.CommentNumReceiver;
import com.zjrb.zjxw.detailproject.ui.boardcast.SubscribeReceiver;
import com.zjrb.zjxw.detailproject.ui.boardcast.VideoReceiver;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.ui.persionaldetail.adapter.TabPagerAdapterImpl;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;
import com.zjrb.zjxw.detailproject.utils.YiDunToken;
import com.zjrb.zjxw.detailproject.utils.global.C;
import com.zjrb.zjxw.detailproject.widget.AnimationPriseView;
import com.zjrb.zjxw.detailproject.widget.LiveGiftView;

import bean.ZBJTOpenAppShareMenuBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;
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
import cn.daily.news.biz.core.ui.toast.ZBToast;
import cn.daily.news.biz.core.ui.toolsbar.BIZTopBarFactory;
import cn.daily.news.biz.core.ui.toolsbar.holder.CommonTopBarHolder;
import cn.daily.news.biz.core.web.JsMultiInterfaceImp;
import cn.daily.news.update.util.NetUtils;

import static com.aliya.dailyplayer.sub.DailyPlayerManager.Builder.STREAM_STATUS_FINISH;
import static com.aliya.dailyplayer.sub.DailyPlayerManager.Builder.STREAM_STATUS_NOT_START;
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
        DetailInterface.VideoBCnterFace, DetailCommentHolder.deleteCommentListener {

    @BindView(R2.id.iv_image)
    ImageView ivImage;
    @BindView(R2.id.video_container)
    RatioFrameLayout videoContainer;
    @BindView(R2.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R2.id.v_container)
    FrameLayout vContainer;
    @BindView(R2.id.ry_container)
    RelativeLayout ryContainer;
    @BindView(R2.id.ly_bottom_comment)
    RelativeLayout mFloorBar;
    @BindView(R2.id.viewpager)
    ViewPager viewPager;

    @BindView(R2.id.menu_prised)
    AnimationPriseView mMenuPrised;
    @BindView(R2.id.fl_comment)
    RelativeLayout mFyContainer;
    @BindView(R2.id.ly_comment_num)
    RelativeLayout ly_comment_num;
    @BindView(R2.id.menu_setting_relpace)
    ImageView ivSettingReplace;
    @BindView(R2.id.menu_setting)
    ImageView ivSetting;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.menu_prised_relpace)
    ImageView ivPrisedRelpace;
    @BindView(R2.id.tv_live_status)
    TextView tvLiveStatus;
    @BindView(R2.id.iv_play)
    ImageView ivPlay;
    @BindView(R2.id.mDivergeView)
    LiveGiftView mGiftView;


    private int ui;
    public String mArticleId;
    private String mFromChannel;
    private DraftDetailBean mNewsDetail;
    private VideoReceiver receive;
    private LocalBroadcastManager localBroadcastManager;
    private SubscribeReceiver mReceiver;
    private CommentNumReceiver commentNumReceiver;
    private VideoEventReceiver mVideoEventReceiver;
    private TabPagerAdapterImpl pagerAdapter;
    private VideoDetailFragment videoDetailFragment;
    private VideoLiveFragment mVideoLiveFragment;
    private VideoCommentFragment mCommentFragment;
    private int prisedCount;
    private Handler handler = new Handler();
    //合并一定时间内的点赞请求
    private Runnable mergePriseTask = new Runnable() {
        @Override
        public void run() {
            doMultyPrise(prisedCount);
            prisedCount=0;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_video_detail);
        ButterKnife.bind(this);
        init();
        initListener();
        mMenuPrised.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mGiftView.getLayoutParams();
                Rect rect = new Rect();
                mMenuPrised.getGlobalVisibleRect(rect);
                Rect rectBig = new Rect();
                mGiftView.getGlobalVisibleRect(rectBig);
                int marginRight = Utils.getScreenWidthPixels(getBaseContext())-rect.centerX()-rectBig.width()/2;
                int marginBottom = Utils.getScreenHeightPixels(getBaseContext())-rect.centerY();
                Log.e("lujialei","getScreenHeightPixels=="+Utils.getScreenHeightPixels(getBaseContext())+"==rect.centerY()=="+rect.centerY());
                layoutParams.rightMargin = marginRight;
                layoutParams.bottomMargin = marginBottom;
                mGiftView.setLayoutParams(layoutParams);
                if (!rect.isEmpty()&&!rectBig.isEmpty()){
                    mMenuPrised.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

            }
        });

    }

    private void initListener() {
        mMenuPrised.setOnTouchingListener(new AnimationPriseView.OnTouchingListener() {
            @Override
            public void onTouching(View view) {

            }

            @Override
            public void onNotPriseClick(View view) {
                if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                    DataAnalyticsUtils.get().ClickPriseIcon(mNewsDetail);
                }
                onOptFabulous();
            }

            @Override
            public void onPrisedClick(View view) {
                if (true){
//                if ((mNewsDetail != null && mNewsDetail.getArticle() != null&&mNewsDetail.getArticle().allow_repeat_like)){
                    mGiftView.addGiftView();
                    prisedCount++;
                    handler.removeCallbacks(mergePriseTask);
                    handler.postDelayed(mergePriseTask,2000);
                }
            }
        });
    }

    private void doMultyPrise(int count) {
        new DraftMultyPraiseTask(new LoadingCallBack<Void>() {

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {

            }

            @Override
            public void onSuccess(Void baseInnerData) {

            }
        }).setTag(this).exe(mNewsDetail.getArticle().getUrl(),count);
    }

    private void init() {
        ui = getWindow().getDecorView().getSystemUiVisibility();
        ly_comment_num.setVisibility(View.GONE);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        receive = new VideoReceiver(this);
        IntentFilter intentFilter = new IntentFilter(UIUtils.getString(R.string.intent_action_close_video));
        intentFilter.addAction(UIUtils.getString(R.string.intent_action_open_video));
        localBroadcastManager.registerReceiver(receive, intentFilter);
        mReceiver = new SubscribeReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter("subscribe_success"));
        commentNumReceiver = new CommentNumReceiver(this);
        mVideoEventReceiver = new VideoEventReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mVideoEventReceiver, new IntentFilter(Constant.VIDEO_EVENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(commentNumReceiver, new IntentFilter("sync_comment_num"));
        getIntentData(getIntent());
        loadData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
        loadData();
        DailyPlayerManager.get().onDestroy();
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
        try {
            boolean isVertical = Integer.valueOf(type) == 1;
            return isVertical;
        } catch (Exception e) {
            return false;
        }
    }


    //初始化视频UI
    private void initVideo(DraftDetailBean.ArticleBean bean) {
        String url = bean.getVideo_url();
        String title = bean.getDoc_title();
        DailyPlayerManager.Builder builder = new DailyPlayerManager.Builder(this);
        String imagePath = mNewsDetail.getArticle().getFirstPic();
        //直播/视频
        if (bean.isNative_live() || !TextUtils.isEmpty(url)) {

            //优先判定直播稿
            if (bean.isNative_live()) {
                builder.setLive(true);
                DraftDetailBean.ArticleBean.NativeLiveInfoBean beanLive = bean.getNative_live_info();
                if (beanLive != null) {
                    url = bean.getNative_live_info().getStream_url();
                    title = bean.getNative_live_info().getTitle();
                    if (!TextUtils.isEmpty(bean.getNative_live_info().getCover())) {
                        imagePath = bean.getNative_live_info().getCover();
                    }
                    //直播回放并且回放地址不为空  取直播回放地址
                    if (bean.getNative_live_info().getStream_status() == 2) {
                        if (!TextUtils.isEmpty(bean.getNative_live_info().getPlayback_url())) {
                            builder.setLive(false);//直播回放就当成普通视频
                            url = bean.getNative_live_info().getPlayback_url();
                        } else {
                            url = "";
                        }
                    }
                }


            }

            UmengShareBean shareBean = UmengShareBean.getInstance()
                    .setSingle(false)
                    .setNewsCard(true)
                    .setCardUrl(mNewsDetail.getArticle().getCard_url())
                    .setArticleId(mNewsDetail.getArticle().getId() + "")
                    .setImgUri(mNewsDetail.getArticle().getFirstPic())
                    .setTextContent(mNewsDetail.getArticle().getSummary())
                    .setTitle(mNewsDetail.getArticle().getDoc_title())
                    .setTargetUrl(mNewsDetail.getArticle().getUrl()).setEventName("NewsShare")
                    .setShareType("文章");
            builder.setImageUrl(imagePath)
                    .setPlayUrl(url)
                    .setStreamStatus(bean.getNative_live_info() == null ? 0 : bean.getNative_live_info().getStream_status())
                    .setVertical(isVertical(bean))
                    .setUmengShareBean(shareBean)
                    .setPlayAnalyCallBack(new DetailPlayerCallBack(mNewsDetail.getArticle()))
                    .setTitle(title)
                    .setPlayContainer(videoContainer);

            //UI
            videoContainer.setVisibility(View.VISIBLE);
            GlideApp.with(ivImage).load(imagePath).placeholder(PH.zheBig()).centerCrop()
                    .apply(AppGlideOptions.bigOptions()).into(ivImage);
            if (builder.isLive()) {
                if (builder.getStreamStatus() == STREAM_STATUS_NOT_START) {
                    tvLiveStatus.setText("暂未开始");
                    ivPlay.setVisibility(View.GONE);
                    tvLiveStatus.setVisibility(View.VISIBLE);
                } else if (builder.getStreamStatus() == STREAM_STATUS_FINISH) {
                    if (TextUtils.isEmpty(builder.getPlayUrl())) {
                        tvLiveStatus.setText("直播已结束");
                        ivPlay.setVisibility(View.GONE);
                        tvLiveStatus.setVisibility(View.VISIBLE);
                    }

                }
            }


            if (PlayerCache.get().getPlayer(url) != null && PlayerCache.get().getPlayer(url).getPlayWhenReady()) {//播放器正在播放
                DailyPlayerManager.get().play(builder);
                if (PlayerCache.get().getPlayer(url).getPlaybackState() == Player.STATE_ENDED) {//已经结束
                    DailyPlayerManager.get().showStateEnd(videoContainer);
                }
            } else {
                boolean b1 = "暂未开始".equals(tvLiveStatus.getText().toString());
                boolean b2 = "直播已结束".equals(tvLiveStatus.getText().toString());
                boolean b3 = tvLiveStatus.getVisibility() == View.VISIBLE;
                if ((b1 || b2) && b3) {//暂未开始和已经结束不初始化播放器

                } else {
                    DailyPlayerManager.get().onDestroy();
                    DailyPlayerManager.get().init(builder);
                }
            }
        } else {
            videoContainer.setVisibility(View.GONE);
        }

        if (!NetUtils.isAvailable(getApplication())) {
            ZBToast.showShort(getContext(), getString(R.string.module_detail_no_network));
        }
    }

    //初始化视频/评论fragment
    private void initViewPage(DraftDetailBean bean) {
        pagerAdapter = new TabPagerAdapterImpl(getSupportFragmentManager(), this);
        //视频/简介
        Bundle bundleVideo = BundleHelper.creatBundle(IKey
                .FRAGMENT_ARGS, VideoDetailFragment.FRAGMENT_DETAIL_VIDEO);
        bundleVideo.putSerializable(FRAGMENT_DETAIL_BEAN, bean);
        //视频
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
            mVideoLiveFragment = (VideoLiveFragment) pagerAdapter.getItem(1);
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
        if (bean.getArticle().isNative_live()) {
            mCommentFragment = (VideoCommentFragment) pagerAdapter.getItem(2);
        } else {
            mCommentFragment = (VideoCommentFragment) pagerAdapter.getItem(1);
        }

        //加载评论页面，防止评论更新数据时空指针
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < pagerAdapter.getCount(); i++) {
                    pagerAdapter.getItem(i).setUserVisibleHint(position == i);
                }
                if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                    if (mNewsDetail.getArticle().isNative_live()) {//直播
                        if (position == 0) {
                            //简介
                            DataAnalyticsUtils.get().SummaryTabClick(mNewsDetail);
                        } else if (position == 1) {
                            //直播间
                            DataAnalyticsUtils.get().LiveTabClick(mNewsDetail);
                        } else {
                            //评论
                            DataAnalyticsUtils.get().LiveCommentTabClick(mNewsDetail);
                        }
                    } else {//视频
                        if (position == 0) {
                            //视频
                            //切换到视频页面时,取消焦点
//                            ryContainer.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                            DataAnalyticsUtils.get().VideoTabClick(mNewsDetail);
                        } else {
                            //评论
                            DataAnalyticsUtils.get().VideoCommentTabCLick(mNewsDetail);
                        }
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
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
                    topHolder.getShareView().setVisibility(View.INVISIBLE);
                    showEmptyNewsDetail();
                } else {
                    ZBToast.showShort(getApplication(), errMsg);
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
            topHolder.setViewVisible(topHolder.getSubscribe(), View.VISIBLE);
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
            topHolder.setViewVisible(topHolder.getFitRelativeLayout(), View.INVISIBLE);
            topHolder.setViewVisible(topHolder.getSubscribe(), View.INVISIBLE);
        }

        mNewsDetail = data;
        initViewState(mNewsDetail);
    }

    private void initViewState(DraftDetailBean data) {
        ivSettingReplace.setVisibility(View.GONE);
        ivPrisedRelpace.setVisibility(View.GONE);
        //只在右边显示
        if (!data.getArticle().isLike_enabled() && data.getArticle().getComment_level() == 0) {
            mFyContainer.setVisibility(View.GONE);
            mMenuPrised.setVisibility(View.GONE);
            ivSetting.setVisibility(View.VISIBLE);
        } else {
            ivSetting.setVisibility(View.VISIBLE);
            //是否允许点赞
            if (data.getArticle().isLike_enabled()) {
                mMenuPrised.setVisibility(View.VISIBLE);
                mMenuPrised.setPrised(data.getArticle().isLiked());
            } else {
                mMenuPrised.setVisibility(View.GONE);
            }
            //是否允许评论
            //禁止评论，隐藏评论框及评论按钮
            if (data.getArticle().getComment_level() == 0) {
                mFyContainer.setVisibility(View.GONE);
            } else {
                mFyContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R2.id.menu_prised_relpace, R2.id.menu_setting,
            R2.id.fl_comment, R2.id.iv_top_share, R2.id.iv_top_bar_back,
            R2.id.tv_top_bar_subscribe_text, R2.id.tv_top_bar_title, R2.id.iv_play, R2.id.menu_setting_relpace})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.menu_prised_relpace) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickPriseIcon(mNewsDetail);
            }
            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting || view.getId() == R.id.menu_setting_relpace) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickMoreIcon(mNewsDetail);
                MoreDialog.newInstance(mNewsDetail).show(getSupportFragmentManager(), "MoreDialog");
            }
            //评论框
        } else if (view.getId() == R.id.fl_comment) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                //进入评论编辑页面(不针对某条评论)
                DataAnalyticsUtils.get().ClickCommentBox(mNewsDetail);
                //评论发表成功
                Analytics analytics = DataAnalyticsUtils.get().CreateCommentAnalytics(mNewsDetail, false);
                try {
                    CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(mNewsDetail.getArticle().getId()))).setListen(mCommentFragment).setWMData(analytics).setLocationCallBack(this).show(getSupportFragmentManager(), "CommentWindowDialog");
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
                        .setObjectType(ObjectType.C01)
                        .setUrl(mNewsDetail.getArticle().getUrl())
                        .setClassifyID(mNewsDetail.getArticle().getChannel_id() + "")
                        .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                        .setColumn_id(String.valueOf(mNewsDetail.getArticle().getColumn_id()))
                        .setColumn_name(mNewsDetail.getArticle().getColumn_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfobjectID(mNewsDetail.getArticle().getId() + "");
                //更新预分享
                UmengShareBean mJsShareBean = SPHelper.get().getObject(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
                ZBJTOpenAppShareMenuBean menuBean = null;
                boolean isUpdateShare = false;
                if (mJsShareBean != null) {
                    menuBean = mJsShareBean.getBean();
                    isUpdateShare = true;
                }

                //分享操作
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setNewsCard(true)
                        .setBean(menuBean)
                        .setShareUpdate(isUpdateShare)
                        .setCardUrl(mNewsDetail.getArticle().getCard_url())
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
                DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "订阅号取消订阅", "A0114", "SubColumn", "取消订阅");
                new ColumnSubscribeTask(new LoadingCallBack<Void>() {

                    @Override
                    public void onSuccess(Void baseInnerData) {
                        topHolder.getSubscribe().setSelected(false);
                        ZBToast.showShort(getApplicationContext(), "取消订阅成功");
                        SyncSubscribeColumn(false, mNewsDetail.getArticle().getColumn_id());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(String errMsg, int errCode) {
                        ZBToast.showShort(getApplication(), "取消订阅失败");
                    }

                }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), false);
            } else {//未订阅状态->订阅
                DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "订阅号订阅", "A0014", "SubColumn", "订阅");
                if (!topHolder.getSubscribe().isSelected()) {
                    new ColumnSubscribeTask(new LoadingCallBack<Void>() {

                        @Override
                        public void onSuccess(Void baseInnerData) {
                            topHolder.getSubscribe().setSelected(true);
                            ZBToast.showShort(getApplicationContext(), "订阅成功");
                            SyncSubscribeColumn(true, mNewsDetail.getArticle().getColumn_id());
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(String errMsg, int errCode) {
                            ZBToast.showShort(getApplication(), "订阅失败");
                        }

                    }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), true);
                }
            }
            //进入栏目
        } else if (view.getId() == R.id.tv_top_bar_title) {
            DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "点击进入栏目详情页", "800031", "ToDetailColumn", "");
            if (!TextUtils.isEmpty(mNewsDetail.getArticle().getColumn_url())) {
                Nav.with(UIUtils.getContext()).to(mNewsDetail.getArticle().getColumn_url());
            }
        } else if (view.getId() == R.id.iv_play) {//播放按钮
            if (mVideoLiveFragment != null && mVideoLiveFragment.findListPlayingView() != null) {//当前列表在播放
                DailyPlayerManager.get().onDestroy();
                DailyPlayerManager.get().deleteControllers(mVideoLiveFragment.findListPlayingView());
            }
            initVideo(mNewsDetail.getArticle());
        }
    }

    public void onOptFabulous() {
        if (mNewsDetail == null) return;
        // 点赞
        if (mNewsDetail.getArticle().isLiked()) {
            ZBToast.showShort(this, getString(R.string.module_detail_you_have_liked));
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
                    if (mMenuPrised.getVisibility() == View.VISIBLE) {
                        mMenuPrised.setPrised(true);
                        mGiftView.addGiftView();
                    }
                    if (ivPrisedRelpace.getVisibility() == View.VISIBLE) {
                        ivPrisedRelpace.setSelected(true);
                    }
                    ZBToast.showShort(getBaseContext(), "已点赞成功");
                } else {
                    ZBToast.showShort(getBaseContext(), errMsg);
                }

            }

            @Override
            public void onSuccess(Void baseInnerData) {
                ZBToast.showShort(getBaseContext(), getString(R.string.module_detail_prise_success));
                mNewsDetail.getArticle().setLiked(true);
                if (mMenuPrised.getVisibility() == View.VISIBLE) {
                    mMenuPrised.setPrised(true);
                    mGiftView.addGiftView();
                }
                if (ivPrisedRelpace.getVisibility() == View.VISIBLE) {
                    ivPrisedRelpace.setSelected(true);
                }
            }
        }).setTag(this).exe(mArticleId, true, mNewsDetail.getArticle().getUrl());
    }


    @Override
    protected void onDestroy() {
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(receive);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mVideoEventReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(commentNumReceiver);
    }

    private void showEmptyNewsDetail() {
        vContainer.setVisibility(View.VISIBLE);
        mFloorBar.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        videoContainer.setVisibility(View.GONE);
        topHolder.setViewVisible(topHolder.getSubscribe(), View.INVISIBLE);
        topHolder.setViewVisible(topHolder.getFitRelativeLayout(), View.INVISIBLE);
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
    public void finish() {
        if (videoDetailFragment != null && videoDetailFragment.getAdapter() != null && videoDetailFragment.getAdapter().getWebViewHolder() != null &&
                videoDetailFragment.getAdapter().getWebViewHolder().getWebView() != null &&
                videoDetailFragment.getAdapter().getWebViewHolder().getWebView().getmChromeClientWrapper() != null &&
                videoDetailFragment.getAdapter().getWebViewHolder().getWebView().getmChromeClientWrapper().getFullScreen()) {
            videoDetailFragment.getAdapter().getWebViewHolder().getWebView().getmChromeClientWrapper().onHideCustomView();
        } else {
            DailyPlayerManager.get().onDestroy();
            PlayerCache.get().clear();
            //详情页播放返回
            Intent intent = new Intent(Constant.VIDEO_EVENT);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.DATA, DailyPlayerManager.get().getBuilder());
            PlayerAction action = new PlayerAction();
            action.setFrom(PlayerAction.ACTIVITY_DETAIL);
            bundle.putSerializable(Constant.EVENT, action);
            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            super.finish();
        }

    }

    @Override
    public void subscribeSync(Intent intent) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction()) && "subscribe_success".equals(intent.getAction())) {
            long id = intent.getLongExtra("id", 0);
            boolean subscribe = intent.getBooleanExtra("subscribe", false);
            //确定是该栏目需要同步
            if (id == mNewsDetail.getArticle().getColumn_id()) {
                topHolder.getSubscribe().setSelected(subscribe);
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
        if (videoDetailFragment.getmAdapter() != null) {
            videoDetailFragment.getmAdapter().showAll();
        }
    }

    @Override
    public void onOptClickChannel() {
        videoDetailFragment.onOptClickChannel();
    }

    @Override
    public void onReadingScaleChange(float scale) {
        videoDetailFragment.onReadingScaleChange(scale);
    }

    @Override
    public void syncCommentNum(Intent intent) {
        if (intent != null && intent.hasExtra("video_comment_title")) {
            if (pagerAdapter != null) {
                if (pagerAdapter.getCount() == 2) {
                    pagerAdapter.setPageTitle(1, intent.getStringExtra("video_comment_title"));
                } else {
                    pagerAdapter.setPageTitle(2, intent.getStringExtra("video_comment_title"));
                }
            }
            tabLayout.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DailyPlayerManager.get().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DailyPlayerManager.get().onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //删除评论
    @Override
    public void onDeleteComment(int position) {
        ZBToast.showShort(getApplicationContext(), "删除成功");
        mCommentFragment.onDeleteComment(position);
    }

    public class VideoEventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ViewGroup currentPlayingView;

            if (mVideoLiveFragment != null && mVideoLiveFragment.findListPlayingView() != null) {
                currentPlayingView = mVideoLiveFragment.findListPlayingView();
            } else {
                currentPlayingView = videoContainer;
            }
            Bundle bundle = intent.getExtras();
            DailyPlayerManager.Builder builder = (DailyPlayerManager.Builder) bundle.getSerializable(Constant.DATA);
            PlayerAction playerAction = (PlayerAction) bundle.getSerializable(Constant.EVENT);
            if (playerAction.isRotateScreen()) {//旋转屏幕
                builder.setContext(getActivity());
                builder.setPlayContainer(currentPlayingView);
                DailyPlayerManager.get().play(builder);
                if (playerAction.isShouldPause()) {
                    DailyPlayerManager.get().userPause();
                }
            } else if (playerAction.isPlayEnd()) {//播放结束
                builder.setContext(getActivity());
                builder.setPlayContainer(currentPlayingView);
                DailyPlayerManager.get().init(builder, false);
                DailyPlayerManager.get().showStateEnd(currentPlayingView);
            } else if (PlayerAction.ACTIVITY_VERTICAL.equals(playerAction.getFrom())) {//竖视频返回
                DailyPlayerManager.get().deleteControllers(currentPlayingView);
            }
        }
    }
}
