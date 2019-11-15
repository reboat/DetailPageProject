package com.zjrb.zjxw.detailproject.ui.link;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.commonwebview.webview.CommonWebView;
import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.task.CommonDraftDetailTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;
import com.zjrb.zjxw.detailproject.utils.MoreDialogLink;
import com.zjrb.zjxw.detailproject.utils.YiDunToken;
import com.zjrb.zjxw.detailproject.utils.global.C;

import java.util.Stack;

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
import cn.daily.news.biz.core.web.BizCoreInterface;
import cn.daily.news.biz.core.web.CallbackReceiver;
import cn.daily.news.biz.core.web.JsMultiInterfaceImp;
import cn.daily.news.biz.core.web.LinkStackPush;
import cn.daily.news.biz.core.web.WebViewImpl;
import port.JsInterfaceCallBack;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * 普通链接稿 - Activity
 * <p>
 * Created by wanglinjie.
 * create time:2017/10/08  上午10:14
 */
public class CommonDetailActivity extends DailyActivity implements LinkStackPush, BizCoreInterface, CommentWindowDialog.LocationCallBack {

    @BindView(R2.id.web_view)
    CommonWebView mWebView;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;

    @BindView(R2.id.ry_container)
    FitWindowsRelativeLayout mContainer;
    @BindView(R2.id.v_container)
    FrameLayout mView;
    @BindView(R2.id.ly_bottom_comment)
    FitWindowsRelativeLayout mFloorBar;
//    @BindView(R2.id.iv_close)
//    ImageView mClose;

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
    @BindView(R2.id.menu_prised_relpace)
    ImageView ivPrisedRelpace;

    private String mArticleId;
    /**
     * 详情页数据
     */
    private DraftDetailBean mNewsDetail;

    private String mFromChannel;

    private WebViewImpl webImpl;
    private JsMultiInterfaceImp jsInterfaceImp;
    //是否是链接稿
    private boolean isBrowserLink = false;
    //不识别scheme地址
    private String browserUri = "";
    //链接稿url堆栈管理
    private Stack<WebStack> mWebStacks = new Stack<>();
    private WebStack mWebStack;
    private Analytics mAnalytics;
    private CallbackReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_activity_common);
        ButterKnife.bind(this);
        AndroidBug5497Workaround.assistActivity(this);
        receiver = new CallbackReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter("js_call_back"));
        getIntentData(getIntent());
        initArgs(savedInstanceState);
        initWebview(mWebStack.urlLink);
//        addWebStack(mWebStack);
        loadData();
    }

    //初始化webview相关设置
    private void initWebview(String linkUrl) {
        webImpl = new WebViewImpl();
        webImpl.setSupportZoom(false);
        webImpl.setWebViewJsObject(C.JS_OBJ_NAME);
        webImpl.setLinkUrl(linkUrl);
        jsInterfaceImp = new JsMultiInterfaceImp(mWebView, webImpl.getWebViewJsObject(), getContext());
        webImpl.setJsObject(jsInterfaceImp);
        mWebView.setHelper(webImpl);
    }

    //链接稿复用
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //复用压栈
        WebStack webStack = new WebStack();
        webStack.urlLink = intent.getStringExtra(IKey.DATA);
        Uri data = intent.getData();
        if (data != null) {
            webStack.urlLink = data.toString();
        }
        //复用链接稿入栈
        if (!TextUtils.isEmpty(webStack.urlLink)) {
            mWebStack = webStack;
            addWebStack(mWebStack);
        }
        getIntentData(intent);
        loadData();
    }

    //初始化堆栈
    private void initArgs(Bundle savedInstanceState) {
        mWebStack = new WebStack();
        if (savedInstanceState != null) {
            mWebStack.urlLink = savedInstanceState.getString(IKey.DATA);
        }
        if (TextUtils.isEmpty(mWebStack.urlLink)) {
            Intent intent = getIntent();
            if (intent != null) {
                mWebStack.urlLink = intent.getStringExtra(IKey.DATA);
                //路由取值
                if (TextUtils.isEmpty(mWebStack.urlLink)) {
                    Uri data = getIntent().getData();
                    if (data != null) {
                        mWebStack.urlLink = data.toString();
                    }
                }
            }
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
                //判断是否为链接稿
                if (data.getPath().equals("/link.html")) {
                    isBrowserLink = true;
                }
                String uri_scheme = data.getQueryParameter(IKey.URI_SCHEME);
                if (!TextUtils.isEmpty(uri_scheme)) {
                    isBrowserLink = false;
                    browserUri = Uri.decode(uri_scheme);
                }
            }

        }
    }

    /**
     * 顶部标题
     */
    private CommonTopBarHolder topBarHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topBarHolder = BIZTopBarFactory.createCommonTopBar(view, this);
        return topBarHolder.getView();
    }

    Analytics.AnalyticsBuilder builder;

    /**
     * 请求详情页数据
     */
    private void loadData() {
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        if (isBrowserLink) {
            if (mArticleId == null || mArticleId.isEmpty()) return;
            new DraftDetailTask(new APICallBack<DraftDetailBean>() {
                @Override
                public void onSuccess(DraftDetailBean draftDetailBean) {
                    if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;
                    mNewsDetail = draftDetailBean;
                    builder = DataAnalyticsUtils.get().pageStayTime(draftDetailBean);
                    mAnalytics = builder.build();
                    if (mNewsDetail.getArticle().getDoc_type() == 3) {
                        mWebStack.urlLink = mNewsDetail.getArticle().getWeb_link();
                    } else {
                        mWebStack.urlLink = mNewsDetail.getArticle().getUrl();
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
                        topBarHolder.getShareView().setVisibility(View.INVISIBLE);
                        showEmptyNewsDetail();
                    } else {
                        ZBToast.showShort(CommonDetailActivity.this, errMsg);
                    }
                }
            }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(mArticleId, mFromChannel);
        } else {
            //非原生内部稿件
            new CommonDraftDetailTask(new APICallBack<DraftDetailBean>() {
                @Override
                public void onSuccess(DraftDetailBean draftDetailBean) {
                    if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;
                    mNewsDetail = draftDetailBean;
                    builder = DataAnalyticsUtils.get().pageStayTime(draftDetailBean);
                    if (mNewsDetail.getArticle().getDoc_type() == 3) {
                        mWebStack.urlLink = mNewsDetail.getArticle().getWeb_link();
                    } else {
                        mWebStack.urlLink = mNewsDetail.getArticle().getUrl();
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
                        ZBToast.showShort(CommonDetailActivity.this, errMsg);
                    }
                }
            }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(browserUri);
        }

    }

    /**
     * 顶部导航条在数据加载结束后再显示
     *
     * @param data 填充详情页数据
     */
    private void fillData(DraftDetailBean data) {
        mView.setVisibility(View.GONE);
        topBarHolder.setViewVisible(topBarHolder.getShareView(), View.VISIBLE);
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
        initViewState(data);
        loadUrlScheme(mWebStack.urlLink);
    }

    //通用详情页直接加载
    private void loadUrlScheme(final String url) {
        if (!TextUtils.isEmpty(url)) {
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(mWebStack.urlLink);
                    addWebStack(mWebStack);
                }
            });
        }
    }

    //顶部栏状态
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
                    if (data.getArticle().isLike_enabled()) {
                        ivPrisedRelpace.setSelected(data.getArticle().isLiked());
                    }
                } else {
                    ivPrisedRelpace.setVisibility(View.GONE);
                }
                ivSettingReplace.setVisibility(View.VISIBLE);
            }
        }
    }


    private Bundle bundle;

    @OnClick({R2.id.iv_top_bar_back, R2.id.menu_comment, R2.id.menu_prised, R2.id.menu_prised_relpace, R2.id.menu_setting, R2.id.menu_setting_relpace, R2.id.iv_top_share, R2.id.fl_comment})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        int id = view.getId();
        if (R.id.iv_top_bar_back == id) {
            //堆栈为空则直接返回
            if (mWebStacks.isEmpty()) {
                finish();
                closeActivity();
            } else {
                //返回删除以后栈顶对象
                mWebStacks.pop();
                if (mWebStacks.isEmpty()) {
                    finish();
                    closeActivity();
                } else {
                    bindWebStack(mWebStacks.peek());
                }
            }
        } else if (view.getId() == R.id.menu_comment) { //分享(无法获取链接稿第一张图，设置为浙江新闻LOGO)
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
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickMoreIcon(mNewsDetail);
                //分享专用bean
                OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                        .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                        .setObjectName(mNewsDetail.getArticle().getDoc_title())
                        .setObjectType(ObjectType.C01)
                        .setClassifyID(mNewsDetail.getArticle().getChannel_id() + "")
                        .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                        .setColumn_id(String.valueOf(mNewsDetail.getArticle().getColumn_id()))
                        .setColumn_name(mNewsDetail.getArticle().getColumn_name())
                        .setUrl(mNewsDetail.getArticle().getUrl())
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

                UmengShareBean shareBean = UmengShareBean.getInstance()
                        .setSingle(false)
                        .setNewsCard(true)
                        .setBean(menuBean)
                        .setShareUpdate(isUpdateShare)
                        .setJsCallback(jsCallBack)
                        .setCardUrl(mNewsDetail.getArticle().getCard_url())
                        .setArticleId(mNewsDetail.getArticle().getId() + "")
                        .setImgUri(mNewsDetail.getArticle().getFirstPic())
                        .setTextContent(getString(R.string.module_detail_share_content_from))
                        .setTitle(mNewsDetail.getArticle().getDoc_title())
                        .setTargetUrl(mWebStack.urlLink)
                        .setAnalyticsBean(bean).setEventName("NewsShare")
                        .setShareType("文章");

                MoreDialogLink.newInstance(mNewsDetail).setShareBean(shareBean).show(getSupportFragmentManager(), "MoreDialog");
            }
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
                //分享操作
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setNewsCard(true)
                        .setBean(menuBean)
                        .setJsCallback(jsCallBack)
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
                new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.forward)
                        .setTargetID(mNewsDetail.getArticle().getId() + "")
                        .setUrl(mNewsDetail.getArticle().getUrl())
                        .build()
                        .send();

            }

        }
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
                    if (mMenuPrised.getVisibility() == View.VISIBLE) {
                        mMenuPrised.setSelected(true);
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
                    mMenuPrised.setSelected(true);
                }
                if (ivPrisedRelpace.getVisibility() == View.VISIBLE) {
                    ivPrisedRelpace.setSelected(true);
                }
            }
        }).setTag(this).exe(mArticleId, true, mNewsDetail.getArticle().getUrl());
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mView.setVisibility(View.VISIBLE);
        if (topBarHolder != null) {
            topBarHolder.getShareView().setVisibility(View.GONE);
        }
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
        closeActivity();
        if (builder != null) {
            if (mAnalytics != null) {
                mAnalytics.sendWithDuration();
            }
        }
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    //关闭页面
    private void closeActivity() {
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        //清空webview依赖
        if (mWebView.getParent() != null && mWebView.getParent() instanceof ViewGroup) {
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
        }
        mWebView.removeAllViews();
        mWebView.destroy();
    }

    //异常销毁保存数据
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(mWebStack.urlLink)) {
            outState.putString(IKey.DATA, mWebStack.urlLink);
        }
    }

    //添加堆栈
    public void addWebStack(WebStack webStack) {
        if (webStack != null) {
            if (mWebStack != null) { // 上一个加入栈
                mWebStacks.push(mWebStack);
            }
            bindWebStack(webStack);
        }
    }

    //渲染链接
    private void bindWebStack(WebStack webStack) {
        mWebStack = webStack;
        mWebView.clearHistory();
        mWebView.loadUrl(mWebStack.urlLink);
    }

    @Override
    public void onBackPressed() {
        if (mWebStacks.isEmpty()) {
            finish();
            closeActivity();
        } else {
            //返回删除以后栈顶对象
            mWebStacks.pop();
            if (mWebStacks.isEmpty()) {
                finish();
                closeActivity();
            } else {
                bindWebStack(mWebStacks.peek());
            }
        }
    }

    //重定向入栈,每次会新建一个webStack
    @Override
    public void linkStackPush(WebView view, String url) {
        WebStack webStack = new WebStack();
        webStack.urlLink = url;
        //复用链接稿入栈
        if (!TextUtils.isEmpty(webStack.urlLink)) {
            mWebStack = webStack;
            addWebStack(mWebStack);
        }
    }

    //JS专用回退
    @Override
    public void jsCallBack(Intent intent) {
        //堆栈为空则直接返回
        if (mWebStacks.isEmpty()) {
            finish();
            closeActivity();
        } else {
            //返回删除以后栈顶对象
            mWebStacks.pop();
            if (mWebStacks.isEmpty()) {
                finish();
                closeActivity();
            } else {
                bindWebStack(mWebStacks.peek());
            }
        }
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

    private static class WebStack {
        private String urlLink;
    }
}