package com.zjrb.zjxw.detailproject.ui.nomaldetail;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliya.dailyplayer.PlayerManager;
import com.aliya.dailyplayer.utils.Recorder;
import com.aliya.dailyplayer.vertical.VFullscreenActivity;
import com.aliya.dailyplayer.vertical.VerticalManager;
import com.aliya.view.fitsys.FitWindowsRecyclerView;
import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.aliya.view.ratio.RatioFrameLayout;
import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.google.gson.Gson;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.EmptyPageHolder;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.daily.news.global.biz.Format;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.callback.DetailInterface;
import com.zjrb.zjxw.detailproject.ui.boardcast.NetWorkChangeReceiver;
import com.zjrb.zjxw.detailproject.ui.boardcast.SubscribeReceiver;
import com.zjrb.zjxw.detailproject.ui.boardcast.VideoReceiver;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;
import com.zjrb.zjxw.detailproject.utils.PlayerAnalytics;
import com.zjrb.zjxw.detailproject.utils.YiDunToken;
import com.zjrb.zjxw.detailproject.utils.global.C;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.db.SettingManager;
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
import cn.daily.news.biz.core.utils.RouteManager;
import cn.daily.news.biz.core.web.JsMultiInterfaceImp;
import cn.daily.news.update.util.NetUtils;

import static com.aliya.dailyplayer.FullscreenActivity.KEY_URL;
import static com.aliya.dailyplayer.vertical.VFullscreenActivity.KEY_TITLE;
import static com.aliya.dailyplayer.vertical.VFullscreenActivity.REQUEST_CODE;
import static com.zjrb.core.utils.UIUtils.getContext;


/**
 * 普通详情页
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
final public class NewsDetailActivity extends DailyActivity implements
        NewsDetailAdapter.CommonOptCallBack, DetailCommentHolder.deleteCommentListener,
        CommentWindowDialog.LocationCallBack, DetailInterface.SubscribeSyncInterFace, DetailInterface.VideoBCnterFace, DetailInterface.NetWorkInterFace {
    @BindView(R2.id.video_container)
    RatioFrameLayout mVideoContainer;
    @BindView(R2.id.rv_content)
    FitWindowsRecyclerView mRvContent;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.ly_bottom_comment)
    FitWindowsRelativeLayout mFloorBar;
    @BindView(R2.id.ry_container)
    RelativeLayout mContainer;
    @BindView(R2.id.iv_image)
    ImageView mivVideoBG;
    @BindView(R2.id.fl_comment)
    RelativeLayout mFyContainer;
    @BindView(R2.id.v_container)
    FrameLayout mView;
    @BindView(R2.id.tv_duration)
    TextView mTvDuration;
    @BindView(R2.id.ll_net_hint)
    LinearLayout llNetHint;
    @BindView(R2.id.iv_type_video)
    LinearLayout llStart;
    @BindView(R2.id.tv_net_hint)
    TextView tvNetHint;
    @BindView(R2.id.ly_comment_num)
    RelativeLayout ly_comment_num;


    /**
     * 稿件ID
     */
    public String mArticleId;
    private String mFromChannel;

    /**
     * 详情页数据
     */
    private DraftDetailBean mNewsDetail;
    /**
     * 详情页适配器
     */
    private NewsDetailAdapter mAdapter;
    /**
     * 视频广播
     */
    private VideoReceiver receive;
    private LocalBroadcastManager localBroadcastManager;
    //订阅同步广播
    private SubscribeReceiver mReceiver;
    //网络监听关闭
    private NetWorkChangeReceiver networkChangeReceiver;
    private int ui;//记录系统状态栏和导航栏样式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_activity_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        ui = getWindow().getDecorView().getSystemUiVisibility();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        receive = new VideoReceiver(this);
        IntentFilter intentFilter = new IntentFilter(UIUtils.getString(R.string.intent_action_close_video));
        intentFilter.addAction(UIUtils.getString(R.string.intent_action_open_video));
        localBroadcastManager.registerReceiver(receive, intentFilter);
        mReceiver = new SubscribeReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter("subscribe_success"));
        setBreoadcast();
        mFloorBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        getIntentData(getIntent());
        loadData();
    }

    /**
     * 设置网络监听
     */
    private void setBreoadcast() {
        networkChangeReceiver = new NetWorkChangeReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

    /**
     * 页面复用调用
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
        loadData();
    }

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


    /**
     * 5.3.0版本通用topbar
     */
    public CommonTopBarHolder topHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = BIZTopBarFactory.createCommonTopBar(view, this);
        return topHolder.getView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 初始化视频
     */
    private void initVideo(DraftDetailBean.ArticleBean bean) {
        String url = bean.getVideo_url();
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            String type = uri.getQueryParameter("isVertical");//1 竖视频 2普通
            if (!TextUtils.isEmpty(type)) {
                boolean isVertical = Integer.valueOf(type) == 1;
                if (isVertical) {
                    mVideoContainer.setVisibility(View.VISIBLE);
//                    VerticalManager.getInstance().init(this, mVideoContainer, url, String.valueOf(Format.duration(bean.getVideo_duration() * 1000)), bean.getFirstPic(), bean.getDoc_title());
                    if (SettingManager.getInstance().isAutoPlayVideoWithWifi() && NetUtils.isWifi(getApplication())) {
                        Intent intent = new Intent(this, VFullscreenActivity.class);
                        intent.putExtra(KEY_URL, url);
                        intent.putExtra(KEY_TITLE, bean.getDoc_title());
                        ActivityCompat.startActivityForResult(this, intent, REQUEST_CODE, null);
                    }
                    return;
                }
            }
        }


        if (!TextUtils.isEmpty(bean.getVideo_url())) {
            mVideoContainer.setVisibility(View.VISIBLE);
            if (bean.getVideo_duration() > 0) {
                mTvDuration.setText(Format.duration(bean.getVideo_duration() * 1000));
                mTvDuration.setVisibility(View.VISIBLE);
            } else {
                mTvDuration.setVisibility(View.GONE);
            }
            GlideApp.with(mivVideoBG).load(mNewsDetail.getArticle().getList_pics().get(0)).placeholder(PH.zheBig()).centerCrop()
                    .apply(AppGlideOptions.bigOptions()).into(mivVideoBG);
            if (SettingManager.getInstance().isAutoPlayVideoWithWifi() && NetUtils.isWifi(getApplication())) {
                PlayerManager.get().play(mVideoContainer, bean.getVideo_url(), new Gson().toJson(bean));
                PlayerManager.setPlayerCallback(mVideoContainer, PlayerAnalytics.get());
            }
        } else {
            mVideoContainer.setVisibility(View.GONE);
        }

        if (!NetUtils.isAvailable(getApplication())) {
            T.showShort(getContext(), getString(R.string.module_detail_no_network));
        }
    }

    Analytics.AnalyticsBuilder builder;

    /**
     * 请求详情页数据
     */
    private void loadData() {
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        if (mArticleId == null || mArticleId.isEmpty()) return;
        DraftDetailTask task = new DraftDetailTask(new LoadingCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;

                builder = DataAnalyticsUtils.get().pageStayTime(draftDetailBean);
                if (mView.getVisibility() == View.VISIBLE) {
                    mView.setVisibility(View.GONE);
                }
                mNewsDetail = draftDetailBean;
                if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                    initVideo(mNewsDetail.getArticle());
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
                    T.showShortNow(NewsDetailActivity.this, errMsg);
                }
            }
        });
        task.setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(mArticleId, mFromChannel);
    }

    /**
     * @param data 填充详情页数据
     */
    public void fillData(DraftDetailBean data) {
        mFloorBar.setVisibility(View.VISIBLE);
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
            //订阅状态 采用select
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
        List datas = new ArrayList<>();
        //先加载头部布局和webview布局，等webview高度渲染之后再添加剩余布局
        //添加头布局
        datas.add(data);
        //添加web布局
        datas.add(data);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        //TODO WLJ 分割线不要
        mRvContent.addItemDecoration(new NewsDetailSpaceDivider(0.5f, R.color._dddddd_7a7b7d));
        mAdapter = new NewsDetailAdapter(datas,false);
        mAdapter.setEmptyView(
                new EmptyPageHolder(mRvContent,
                        EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据")
                ).itemView);
        mRvContent.setAdapter(mAdapter);
    }

    /**
     * 刷新底部栏状态
     *
     * @param data
     */
    private void initViewState(DraftDetailBean data) {
        //是否已点赞
        if (data.getArticle().isLike_enabled()) {
            mMenuPrised.setVisibility(View.VISIBLE);
            mMenuPrised.setSelected(data.getArticle().isLiked());
        } else {
            mMenuPrised.setVisibility(View.GONE);
        }

        //大致评论数量
        if (!TextUtils.isEmpty(data.getArticle().getComment_count_general())) {
            mTvCommentsNum.setVisibility(View.VISIBLE);
            mTvCommentsNum.setText(data.getArticle().getComment_count_general());
        } else {
            mTvCommentsNum.setVisibility(View.GONE);
        }

        //禁止评论，隐藏评论框及评论按钮
        if (data.getArticle().getComment_level() == 0) {
//            mFyContainer.setVisibility(View.GONE);
            ly_comment_num.setVisibility(View.GONE);
        } else {
//            mFyContainer.setVisibility(View.VISIBLE);
            ly_comment_num.setVisibility(View.VISIBLE);
        }
    }

    /**
     * WebView加载完毕
     */
    @Override
    public void onOptPageFinished() {
        mAdapter.showAll();
    }

    private Bundle bundle;


    /**
     * 进入频道详情页
     */
    @Override
    public void onOptClickChannel() {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(IKey.CHANNEL_NAME, mNewsDetail.getArticle().getSource_channel_name());
        bundle.putString(IKey.CHANNEL_ID, mNewsDetail.getArticle().getSource_channel_id());
        Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.SUBSCRIBE_PATH);
    }

    private float mScale;

    /**
     * 阅读百分比
     *
     * @param scale
     */
    @Override
    public void onReadingScaleChange(float scale) {
        mScale = scale;
    }

    /**
     * 点赞操作
     */
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
                    mMenuPrised.setSelected(true);
                    T.showShort(getBaseContext(), "已点赞成功");
                } else {
                    T.showShort(getBaseContext(), errMsg);
                }

            }

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_prise_success));
                mNewsDetail.getArticle().setLiked(true);
                mMenuPrised.setSelected(true);
            }
        }).setTag(this).exe(mArticleId, true, mNewsDetail.getArticle().getUrl());
    }

    @OnClick({R2.id.ly_comment_num, R2.id.menu_prised, R2.id.menu_setting,
            R2.id.tv_comment, R2.id.iv_top_share, R2.id.iv_type_video, R2.id.iv_top_bar_back,
            R2.id.tv_top_bar_subscribe_text, R2.id.tv_top_bar_title, R2.id.ll_net_hint})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        //评论列表
        if (view.getId() == R.id.ly_comment_num) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                //埋点，进入评论列表
                DataAnalyticsUtils.get().ClickInCommentList(mNewsDetail);
                //进入评论列表页面
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(IKey.NEWS_DETAIL, mNewsDetail);
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.COMMENT_ACTIVITY_PATH);
            }
            //点赞
        } else if (view.getId() == R.id.menu_prised) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickPriseIcon(mNewsDetail);
            }
            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickMoreIcon(mNewsDetail);
                MoreDialog.newInstance(mNewsDetail).setWebViewCallBack(mAdapter.getWebViewHolder(), mAdapter.getWebViewHolder()).show(getSupportFragmentManager(), "MoreDialog");
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

            //重新加载
        } else if (view.getId() == R.id.iv_type_video) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null && !TextUtils.isEmpty(mNewsDetail.getArticle().getVideo_url())) {

                if (NetUtils.isAvailable(getApplication())) {
                    if (NetUtils.isMobile(getApplication())) {
                        if (Recorder.get().isAllowMobileTraffic(mNewsDetail.getArticle().getVideo_url())) {
                            PlayerManager.get().play(mVideoContainer, mNewsDetail.getArticle().getVideo_url(), new Gson().toJson(mNewsDetail.getArticle()));
                            PlayerManager.setPlayerCallback(mVideoContainer, PlayerAnalytics.get());
                        } else {
                            llStart.setVisibility(View.GONE);
                            llNetHint.setVisibility(View.VISIBLE);
                            tvNetHint.setText("用流量播放");
                            tvNetHint.setVisibility(View.VISIBLE);
                        }
                        return;
                    }
                    if (NetUtils.isWifi(getApplication())) {
                        PlayerManager.get().play(mVideoContainer, mNewsDetail.getArticle().getVideo_url(), new Gson().toJson(mNewsDetail.getArticle()));
                        PlayerManager.setPlayerCallback(mVideoContainer, PlayerAnalytics.get());
                        return;
                    }
                    PlayerManager.get().play(mVideoContainer, mNewsDetail.getArticle().getVideo_url(), new Gson().toJson(mNewsDetail.getArticle()));
                    PlayerManager.setPlayerCallback(mVideoContainer, PlayerAnalytics.get());
                }

            }
        } else if (view.getId() == R.id.iv_top_bar_back) {
            //点击返回操作
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickBack(mNewsDetail);
            }
            finish();
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
                        T.showShortNow(NewsDetailActivity.this, "取消订阅失败");
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
                            T.showShortNow(NewsDetailActivity.this, "订阅失败");
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
        } else if (view.getId() == R.id.ll_net_hint) {//网络提醒下点击播放
            PlayerManager.get().play(mVideoContainer, mNewsDetail.getArticle().getVideo_url(), new Gson().toJson(mNewsDetail.getArticle()));
            PlayerManager.setPlayerCallback(mVideoContainer, PlayerAnalytics.get());
            if (NetUtils.isMobile(getApplication())) {
                Recorder.get().allowMobileTraffic(mNewsDetail.getArticle().getVideo_url());
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.onWebViewResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.onWebViewPause();
        }
    }


    @Override
    protected void onDestroy() {
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        super.onDestroy();
        if (builder != null) {
            //阅读深度
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                builder.setPercentage(mScale + "");
            }
            builder.readPercent(mScale + "");
            Analytics mAnalytics = builder.build();
            if (mAnalytics != null) {
                mAnalytics.sendWithDuration();
            }
        }

        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
        localBroadcastManager.unregisterReceiver(receive);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mView.setVisibility(View.VISIBLE);
        mFloorBar.setVisibility(View.GONE);
        mVideoContainer.setVisibility(View.GONE);
        topHolder.setViewVisible(topHolder.getFitRelativeLayout(), View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.v_container, EmptyStateFragment.newInstance()).commit();
    }

    /**
     * 删除评论，局部刷新
     */
    @Override
    public void onDeleteComment(int position) {
        mAdapter.remove(position);
    }


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
     * 广播修改订阅状态
     *
     * @param intent
     */
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

    /**
     * 打开/关闭视频回调
     *
     * @param intent
     */
    @Override
    public void videoBC(Intent intent) {
        String action = intent.getAction();
        if (UIUtils.getString(R.string.intent_action_close_video).equals(action)) {
            mVideoContainer.setVisibility(View.GONE);
        } else if (UIUtils.getString(R.string.intent_action_open_video).equals(action)) {
            mVideoContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void networkBC(Intent intent) {
        if (NetUtils.isMobile(getApplication()) && tvNetHint.getVisibility() == View.VISIBLE) {
            tvNetHint.setText("用流量播放");
            return;
        }
        if (NetUtils.isWifi(getApplication()) && tvNetHint.getVisibility() == View.VISIBLE) {
            tvNetHint.setText("已切换至wifi");
            return;
        }
    }
}


