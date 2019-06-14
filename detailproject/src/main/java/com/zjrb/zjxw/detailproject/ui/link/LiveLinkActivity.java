package com.zjrb.zjxw.detailproject.ui.link;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.commonwebview.webview.CommonWebView;
import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadingCallBack;
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
import com.zjrb.zjxw.detailproject.ui.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;
import com.zjrb.zjxw.detailproject.utils.YiDunToken;
import com.zjrb.zjxw.detailproject.utils.global.C;

import bean.ZBJTOpenAppShareMenuBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.model.CommentDialogBean;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;
import cn.daily.news.biz.core.ui.dialog.CommentWindowDialog;
import cn.daily.news.biz.core.ui.toast.ZBToast;
import cn.daily.news.biz.core.ui.toolsbar.BIZTopBarFactory;
import cn.daily.news.biz.core.ui.toolsbar.holder.CommonTopBarHolder;
import cn.daily.news.biz.core.utils.RouteManager;
import cn.daily.news.biz.core.web.AndroidBug5497Workaround;
import cn.daily.news.biz.core.web.JsMultiInterfaceImp;
import cn.daily.news.biz.core.web.WebViewImpl;
import port.JsInterfaceCallBack;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * 直播链接稿 - Activity
 * <p>
 * Created by wanglinjie.
 * create time:2017/10/08  上午10:14
 */
public class LiveLinkActivity extends DailyActivity implements CommentWindowDialog.LocationCallBack,
        DetailInterface.SubscribeSyncInterFace {

    @BindView(R2.id.web_view)
    CommonWebView mWebView;
    @BindView(R2.id.fl_comment)
    RelativeLayout mFyContainer;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.ly_bottom_comment)
    FitWindowsRelativeLayout mFloorBar;
    @BindView(R2.id.ry_container)
    FitWindowsRelativeLayout mContainer;
    @BindView(R2.id.v_container)
    FrameLayout mView;

    @BindView(R2.id.ly_comment_num)
    RelativeLayout ly_comment_num;
    @BindView(R2.id.menu_setting_relpace)
    ImageView ivSettingReplace;
    @BindView(R2.id.menu_setting)
    ImageView ivSetting;
    @BindView(R2.id.menu_prised_relpace)
    ImageView ivPrisedRelpace;

    private String mArticleId;
    /**
     * 详情页数据
     */
    private DraftDetailBean mNewsDetail;
    private String mFromChannel;
    /**
     * 网页地址
     */
    private String url;
    //订阅同步广播
    private SubscribeReceiver mReceiver;
    private WebViewImpl webImpl;
    private JsMultiInterfaceImp jsInterfaceImp;
    private Analytics mAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_activity_browser);
        AndroidBug5497Workaround.assistActivity(this);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        mFloorBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        initWebview();
        mReceiver = new SubscribeReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter("subscribe_success"));
        loadData();
    }

    //初始化webview相关设置
    private void initWebview() {
        webImpl = new WebViewImpl();
        webImpl.setWebViewJsObject(C.JS_OBJ_NAME);
        jsInterfaceImp = new JsMultiInterfaceImp(mWebView, webImpl.getWebViewJsObject(), getContext());
        webImpl.setJsObject(jsInterfaceImp);
        mWebView.setHelper(webImpl);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
        loadData();
        if (topBarHolder != null) {
            topBarHolder.getShareView().setVisibility(View.VISIBLE);
        }
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
     * 顶部标题
     */
    private CommonTopBarHolder topBarHolder;
    Analytics.AnalyticsBuilder builder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topBarHolder = BIZTopBarFactory.createCommonTopBar(view, this);
        topBarHolder.setViewVisible(topBarHolder.getShareView(), View.VISIBLE);
        return topBarHolder.getView();
    }

    /**
     * 请求详情页数据
     */
    private void loadData() {
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        if (mArticleId == null || mArticleId.isEmpty()) return;
        new DraftDetailTask(new APICallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;
                mNewsDetail = draftDetailBean;
                builder = DataAnalyticsUtils.get().pageStayTime(draftDetailBean);
                mAnalytics = builder.build();
                if (mNewsDetail.getArticle().getDoc_type() == 8) {
                    url = mNewsDetail.getArticle().getLive_url();
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
                    showEmptyNewsDetail();
                } else {
                    ZBToast.showShort(LiveLinkActivity.this, errMsg);
                }
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(mArticleId, mFromChannel);
    }

    /**
     * @param data 填充详情页数据
     */
    private void fillData(DraftDetailBean data) {
        mFloorBar.setVisibility(View.VISIBLE);
        mView.setVisibility(View.GONE);
        // 记录阅读记录
        if (data != null && data.getArticle() != null) {
            DraftDetailBean.ArticleBean article = data.getArticle();
            ReadNewsDaoHelper.get().asyncRecord(
                    ReadNewsBean.newBuilder().id(article.getId())
                            .mlfId(article.getMlf_id())
                            .tag(article.getList_tag())
                            .title(article.getList_title())
                            .url(article.getUrl()));
        }
        //中间栏目布局处理
        if (!TextUtils.isEmpty(mNewsDetail.getArticle().getColumn_name())) {
            //栏目名称
            topBarHolder.setViewVisible(topBarHolder.getFitRelativeLayout(), View.VISIBLE);
            topBarHolder.setViewVisible(topBarHolder.getSubscribe(), View.VISIBLE);
            topBarHolder.getTitleView().setText(mNewsDetail.getArticle().getColumn_name());
            //栏目头像
            GlideApp.with(topBarHolder.getIvIcon()).load(mNewsDetail.getArticle().getColumn_logo()).placeholder(R.mipmap.ic_top_bar_redboat_icon)
                    .error(R.mipmap.ic_top_bar_redboat_icon).centerCrop().into(topBarHolder.getIvIcon());
            //订阅状态 采用select
            if (mNewsDetail.getArticle().isColumn_subscribed()) {
                topBarHolder.getSubscribe().setSelected(true);
            } else {
                topBarHolder.getSubscribe().setSelected(false);
            }
        } else {
            topBarHolder.setViewVisible(topBarHolder.getFitRelativeLayout(), View.INVISIBLE);
            topBarHolder.setViewVisible(topBarHolder.getSubscribe(), View.INVISIBLE);
        }
        webImpl.setLinkUrl(url);
        loadUrlScheme(url);
        initViewState(data);
    }


    private void loadUrlScheme(final String url) {
        //链接稿单独逻辑
        if (!TextUtils.isEmpty(url)) {
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(url);
                }
            });
        }
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
                    ivPrisedRelpace.setSelected(data.getArticle().isLiked());
                } else {
                    ivPrisedRelpace.setVisibility(View.GONE);
                }
                ivSettingReplace.setVisibility(View.VISIBLE);
            }
        }
    }


    private Bundle bundle;

    @OnClick({R2.id.iv_top_bar_back, R2.id.iv_top_share, R2.id.menu_comment,
            R2.id.menu_prised, R2.id.menu_prised_relpace, R2.id.menu_setting, R2.id.fl_comment,
            R2.id.tv_top_bar_subscribe_text, R2.id.tv_top_bar_title, R2.id.iv_top_subscribe_icon, R2.id.menu_setting_relpace})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;

        int id = view.getId();
        if (R.id.iv_top_bar_back == id) {
            onBackPressed();
            DataAnalyticsUtils.get().ClickBack(mNewsDetail);
            //分享(无法获取链接稿第一张图，设置为浙江新闻LOGO)
        } else if (id == R.id.iv_top_share) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null && !TextUtils.isEmpty(url)) {
                //分享专用bean
                OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                        .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                        .setObjectName(mNewsDetail.getArticle().getDoc_title())
                        .setUrl(mNewsDetail.getArticle().getUrl())
                        .setObjectType(ObjectType.C01)
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
                JsInterfaceCallBack jsCallBack = null;
                boolean isUpdateShare = false;
                if (mJsShareBean != null && jsInterfaceImp != null) {
                    menuBean = mJsShareBean.getBean();
                    isUpdateShare = true;
                    jsCallBack = jsInterfaceImp.getmCallback();

                }
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setNewsCard(true)
                        .setJsCallback(jsCallBack)
                        .setBean(menuBean)
                        .setShareUpdate(isUpdateShare)
                        .setCardUrl(mNewsDetail.getArticle().getCard_url())
                        .setArticleId(mNewsDetail.getArticle().getId() + "")
                        .setImgUri(mNewsDetail.getArticle().getFirstPic())
                        .setTextContent(mNewsDetail.getArticle().getSummary())
                        .setTitle(mNewsDetail.getArticle().getDoc_title())
                        .setTargetUrl(url)
                        .setAnalyticsBean(bean).setEventName("NewsShare")
                        .setShareType("文章")
                );
                DataAnalyticsUtils.get().ClickShare(mNewsDetail);
                new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.forward)
                        .setTargetID(mNewsDetail.getArticle().getId() + "")
                        .setUrl(mNewsDetail.getArticle().getUrl())
                        .build()
                        .send();

            }
        } else if (view.getId() == R.id.menu_comment) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
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
            DataAnalyticsUtils.get().ClickPriseIcon(mNewsDetail);
            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting || view.getId() == R.id.menu_setting_relpace) {
            DataAnalyticsUtils.get().ClickMoreIcon(mNewsDetail);
            MoreDialog.newInstance(mNewsDetail).show(getSupportFragmentManager(), "MoreDialog");
            //评论框
        } else if (view.getId() == R.id.fl_comment) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                //进入评论编辑页面(不针对某条评论)
                DataAnalyticsUtils.get().ClickCommentBox(mNewsDetail);
                //评论发表成功
                Analytics analytics = DataAnalyticsUtils.get().CreateCommentAnalytics(mNewsDetail, false);
                //进入评论编辑页面(不针对某条评论)
                try {
                    CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(mNewsDetail.getArticle().getId()))).setWMData(analytics).setLocationCallBack(this).show(getSupportFragmentManager(), "CommentWindowDialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (view.getId() == R.id.tv_top_bar_subscribe_text) {
            //已订阅状态->取消订阅
            if (topBarHolder.getSubscribe().isSelected()) {
                DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "订阅号取消订阅", "A0114", "SubColumn", "取消订阅");
                new ColumnSubscribeTask(new LoadingCallBack<Void>() {

                    @Override
                    public void onSuccess(Void baseInnerData) {
                        topBarHolder.getSubscribe().setSelected(false);
                        ZBToast.showShort(getApplicationContext(), "取消订阅成功");
                        SyncSubscribeColumn(false, mNewsDetail.getArticle().getColumn_id());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(String errMsg, int errCode) {
                        ZBToast.showShort(LiveLinkActivity.this, "取消订阅失败");
                    }

                }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), false);
            } else {//未订阅状态->订阅
                DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "订阅号订阅", "A0014", "SubColumn", "订阅");
                if (!topBarHolder.getSubscribe().isSelected()) {
                    new ColumnSubscribeTask(new LoadingCallBack<Void>() {

                        @Override
                        public void onSuccess(Void baseInnerData) {
                            topBarHolder.getSubscribe().setSelected(true);
                            ZBToast.showShort(getApplicationContext(), "订阅成功");
                            SyncSubscribeColumn(true, mNewsDetail.getArticle().getColumn_id());
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(String errMsg, int errCode) {
                            ZBToast.showShort(LiveLinkActivity.this, "订阅失败");
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

    /**
     * 点赞操作
     */
    private void onOptFabulous() {
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
                    if(mMenuPrised.getVisibility() == View.VISIBLE){
                        mMenuPrised.setSelected(true);
                    }
                    if(ivPrisedRelpace.getVisibility() == View.VISIBLE){
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
                if(mMenuPrised.getVisibility() == View.VISIBLE){
                    mMenuPrised.setSelected(true);
                }
                if(ivPrisedRelpace.getVisibility() == View.VISIBLE){
                    ivPrisedRelpace.setSelected(true);
                }
            }
        }).setTag(this).exe(mArticleId, true, mNewsDetail.getArticle().getUrl());
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mFloorBar.setVisibility(View.GONE);
        mView.setVisibility(View.VISIBLE);
        topBarHolder.getShareView().setVisibility(View.GONE);
        topBarHolder.setViewVisible(topBarHolder.getSubscribe(), View.INVISIBLE);
        topBarHolder.setViewVisible(topBarHolder.getFitRelativeLayout(), View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.v_container, EmptyStateFragment.newInstance()).commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
        //新华智云
        if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
            new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.leave)
                    .setTargetID(mNewsDetail.getArticle().getId() + "")
                    .setUrl(mNewsDetail.getArticle().getUrl())
                    .build()
                    .send();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        //新华智云
        if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
            new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.comeIn)
                    .setTargetID(mNewsDetail.getArticle().getId() + "")
                    .setUrl(mNewsDetail.getArticle().getUrl())
                    .build()
                    .send();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        mWebView.destroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
        if (builder != null) {
            if (mAnalytics != null) {
                mAnalytics.sendWithDuration();
            }
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

    /**
     * 同步订阅栏目
     */
    private void SyncSubscribeColumn(boolean isSubscribe, int columnid) {
        Intent intent = new Intent("subscribe_success");
        intent.putExtra("subscribe", isSubscribe);
        intent.putExtra("id", (long) columnid);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void subscribeSync(Intent intent) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction()) && "subscribe_success".equals(intent.getAction())) {
            long id = intent.getLongExtra("id", 0);
            boolean subscribe = intent.getBooleanExtra("subscribe", false);
            //确定是该栏目需要同步
            if (id == mNewsDetail.getArticle().getColumn_id()) {
                topBarHolder.getSubscribe().setSelected(subscribe);
            }
        }
    }

}