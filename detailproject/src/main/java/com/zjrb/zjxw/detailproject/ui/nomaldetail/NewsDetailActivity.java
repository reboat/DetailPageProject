package com.zjrb.zjxw.detailproject.ui.nomaldetail;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsRecyclerView;
import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.EmptyPageHolder;
import com.zjrb.core.utils.L;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.callback.DetailInterface;
import com.zjrb.zjxw.detailproject.ui.boardcast.SubscribeReceiver;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;
import com.zjrb.zjxw.detailproject.utils.YiDunToken;
import com.zjrb.zjxw.detailproject.utils.global.C;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.SettingBiz;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.model.CommentDialogBean;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;
import cn.daily.news.biz.core.ui.dialog.CommentWindowDialog;
import cn.daily.news.biz.core.ui.toast.ZBToast;
import cn.daily.news.biz.core.ui.toolsbar.BIZTopBarFactory;
import cn.daily.news.biz.core.ui.toolsbar.holder.CommonTopBarHolder;
import cn.daily.news.biz.core.utils.RouteManager;
import cn.daily.news.biz.core.web.JsMultiInterfaceImp;

import static com.zjrb.core.utils.UIUtils.getContext;


/**
 * 普通详情页
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
final public class NewsDetailActivity extends DailyActivity implements
        NewsDetailAdapter.CommonOptCallBack, DetailCommentHolder.deleteCommentListener,
        CommentWindowDialog.LocationCallBack, DetailInterface.SubscribeSyncInterFace {
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
    @BindView(R2.id.menu_setting_relpace)
    ImageView ivSettingReplace;
    @BindView(R2.id.menu_setting)
    ImageView ivSetting;
    @BindView(R2.id.fl_comment)
    RelativeLayout mFyContainer;
    @BindView(R2.id.v_container)
    FrameLayout mView;
    @BindView(R2.id.tv_net_hint)
    TextView tvNetHint;
    @BindView(R2.id.ly_comment_num)
    RelativeLayout ly_comment_num;
    @BindView(R2.id.menu_prised_relpace)
    ImageView ivPrisedRelpace;


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
    //订阅同步广播
    private SubscribeReceiver mReceiver;
    private int ui;//记录系统状态栏和导航栏样式
    private Analytics mAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_activity_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        ui = getWindow().getDecorView().getSystemUiVisibility();
        mReceiver = new SubscribeReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter("subscribe_success"));
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
        //如果是全屏
        if (mAdapter != null && mAdapter.getWebViewHolder() != null &&
                mAdapter.getWebViewHolder().getWebView() != null &&
                mAdapter.getWebViewHolder().getWebView().getmChromeClientWrapper() != null &&
                mAdapter.getWebViewHolder().getWebView().getmChromeClientWrapper().getFullScreen()) {
            mAdapter.getWebViewHolder().getWebView().getmChromeClientWrapper().onHideCustomView();
        } else {
            super.onBackPressed();
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
                mAnalytics = builder.build();
                if (mView.getVisibility() == View.VISIBLE) {
                    mView.setVisibility(View.GONE);
                }
                mNewsDetail = draftDetailBean;
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
                    ZBToast.showShort(NewsDetailActivity.this, errMsg);
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
        mRvContent.addItemDecoration(new NewsDetailSpaceDivider(0.5f, R.color._dddddd_7a7b7d));
        mAdapter = new NewsDetailAdapter(datas, false);
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
        //不允许点赞及评论,在左边显示更多
        if (!data.getArticle().isLike_enabled() && data.getArticle().getComment_level() == 0) {
            mFyContainer.setVisibility(View.GONE);
            ly_comment_num.setVisibility(View.GONE);
            mMenuPrised.setVisibility(View.GONE);
            ivSetting.setVisibility(View.GONE);
            ivSettingReplace.setVisibility(View.VISIBLE);
        } else {
            //允许评论 在右边显示
            if (data.getArticle().getComment_level() != 0) {
                ivPrisedRelpace.setVisibility(View.GONE);
                ivSettingReplace.setVisibility(View.GONE);
                ivSetting.setVisibility(View.VISIBLE);
                mFyContainer.setVisibility(View.VISIBLE);
                ly_comment_num.setVisibility(View.VISIBLE);
                //大致评论数量
                if (!TextUtils.isEmpty(data.getArticle().getComment_count_general())) {
                    mTvCommentsNum.setVisibility(View.VISIBLE);
                    mTvCommentsNum.setText(data.getArticle().getComment_count_general());
                } else {
                    mTvCommentsNum.setVisibility(View.INVISIBLE);
                }
                //是否允许点赞
                if (data.getArticle().isLike_enabled()) {
                    mMenuPrised.setVisibility(View.VISIBLE);
                    mMenuPrised.setSelected(data.getArticle().isLiked());
                } else {
                    mMenuPrised.setVisibility(View.GONE);
                }
            } else {//禁止评论，在左边显示
                mFyContainer.setVisibility(View.GONE);
                ly_comment_num.setVisibility(View.GONE);
                ivSetting.setVisibility(View.GONE);
                mMenuPrised.setVisibility(View.GONE);
                if (data.getArticle().isLike_enabled()) {
                    ivPrisedRelpace.setVisibility(View.VISIBLE);
                } else {
                    ivPrisedRelpace.setVisibility(View.GONE);
                }
                ivSettingReplace.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * WebView加载完毕
     */
    @Override
    public void onOptPageFinished() {
        mAdapter.showAll();
        float size = SettingBiz.get().getHtmlFontScale();
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
                    mMenuPrised.setSelected(true);
                    ZBToast.showShort(getBaseContext(), "已点赞成功");
                } else {
                    ZBToast.showShort(getBaseContext(), errMsg);
                }

            }

            @Override
            public void onSuccess(Void baseInnerData) {
                ZBToast.showShort(getBaseContext(), getString(R.string.module_detail_prise_success));
                mNewsDetail.getArticle().setLiked(true);
                mMenuPrised.setSelected(true);
            }
        }).setTag(this).exe(mArticleId, true, mNewsDetail.getArticle().getUrl());
    }

    @OnClick({R2.id.ly_comment_num, R2.id.menu_prised, R2.id.menu_prised_relpace, R2.id.menu_setting, R2.id.menu_setting_relpace,
            R2.id.fl_comment, R2.id.iv_top_share, /*R2.id.iv_type_video,*/ R2.id.iv_top_bar_back,
            R2.id.tv_top_bar_subscribe_text, R2.id.iv_top_subscribe_icon, R2.id.tv_top_bar_title/*, R2.id.ll_net_hint*/})
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
        } else if (view.getId() == R.id.menu_prised || view.getId() == R.id.menu_prised_relpace) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickPriseIcon(mNewsDetail);
            }
            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting || view.getId() == R.id.menu_setting_relpace) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickMoreIcon(mNewsDetail);
                MoreDialog.newInstance(mNewsDetail).setWebViewCallBack(mAdapter.getWebViewHolder(), mAdapter.getWebViewHolder()).show(getSupportFragmentManager(), "MoreDialog");
            }

            //评论框
        } else if (view.getId() == R.id.fl_comment) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                //进入评论编辑页面(不针对某条评论)
                DataAnalyticsUtils.get().ClickCommentBox(mNewsDetail);

                //评论发表成功
                Analytics analytics = DataAnalyticsUtils.get().CreateCommentAnalytics(mNewsDetail, false);
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
                        .setObjectType(ObjectType.C01)
                        .setUrl(mNewsDetail.getArticle().getUrl())
                        .setClassifyID(mNewsDetail.getArticle().getChannel_id() + "")
                        .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setColumn_id(String.valueOf(mNewsDetail.getArticle().getColumn_id()))
                        .setColumn_name(mNewsDetail.getArticle().getColumn_name())
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfobjectID(mNewsDetail.getArticle().getId() + "");
                //分享操作
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setNewsCard(true)
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
                new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.forward)
                        .setTargetID(mNewsDetail.getArticle().getId() + "")
                        .setUrl(mNewsDetail.getArticle().getUrl())
                        .build()
                        .send();

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
                        ZBToast.showShort(NewsDetailActivity.this, "取消订阅失败");
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
                            ZBToast.showShort(NewsDetailActivity.this, "订阅失败");
                        }

                    }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), true);
                }

            }
            //进入栏目
        } else if (view.getId() == R.id.tv_top_bar_title || view.getId() == R.id.iv_top_subscribe_icon) {
            DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "点击进入栏目详情页", "800031", "ToDetailColumn", "");
            if (!TextUtils.isEmpty(mNewsDetail.getArticle().getColumn_url())) {
                Nav.with(UIUtils.getContext()).to(mNewsDetail.getArticle().getColumn_url());
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (mAdapter != null) {
//            mAdapter.onWebViewResume();
//        }
        //新华智云
//        if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
//            new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.comeIn)
//                    .setTargetID(mNewsDetail.getArticle().getId() + "")
//                    .setUrl(mNewsDetail.getArticle().getUrl())
//                    .build()
//                    .send();
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        float size = SettingBiz.get().getHtmlFontScale();
        L.e("WLJ,onPause,size=" + size);
//        if (mAdapter != null) {
//            mAdapter.onWebViewPause();
//        }
        //新华智云
//        if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
//            new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.leave)
//                    .setTargetID(mNewsDetail.getArticle().getId() + "")
//                    .setUrl(mNewsDetail.getArticle().getUrl())
//                    .build()
//                    .send();
//        }

    }


    @Override
    protected void onDestroy() {
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        super.onDestroy();
        if (builder != null) {
            //阅读深度
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                builder.pagePercent(mScale + "");
            }
            builder.readPercent(mScale + "");
            if (mAnalytics != null) {
                mAnalytics.sendWithDuration();
            }
        }
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mView.setVisibility(View.VISIBLE);
        mFloorBar.setVisibility(View.GONE);
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
            }
        }
    }

}


