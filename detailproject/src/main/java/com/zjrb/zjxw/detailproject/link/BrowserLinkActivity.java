package com.zjrb.zjxw.detailproject.link;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.commonwebview.webview.CommonWebView;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.callback.DetailWMHelperInterFace;
import com.zjrb.zjxw.detailproject.global.C;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.utils.MoreDialogLink;
import com.zjrb.zjxw.detailproject.utils.YiDunToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.ui.toolsbar.BIZTopBarFactory;
import cn.daily.news.biz.core.ui.toolsbar.holder.DefaultTopBarHolder4;
import cn.daily.news.biz.core.utils.RouteManager;
import cn.daily.news.biz.core.web.JsMultiInterfaceImp;
import cn.daily.news.biz.core.web.WebViewImpl;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * 普通链接稿 - Activity
 * <p>
 * Created by wanglinjie.
 * create time:2017/10/08  上午10:14
 */
public class BrowserLinkActivity extends DailyActivity implements DetailWMHelperInterFace.LinkDetailWM {

    @BindView(R2.id.web_view)
    CommonWebView mWebView;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.ry_container)
    FitWindowsRelativeLayout mContainer;
    @BindView(R2.id.v_container)
    FrameLayout mView;
    @BindView(R2.id.ly_bottom_comment)
    FitWindowsRelativeLayout mFloorBar;

    private String mArticleId;
    /**
     * 详情页数据
     */
    private DraftDetailBean mNewsDetail;

    /**
     * 网页地址
     */
    private String url;

    private String mFromChannel;

    private WebViewImpl webImpl;
    private JsMultiInterfaceImp jsInterfaceImp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_activity_browser);
        ButterKnife.bind(this);
        mFloorBar.setVisibility(View.GONE);
        getIntentData(getIntent());
        initWebview();
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
        mFloorBar.setVisibility(View.GONE);
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
     * 顶部标题
     */
    private DefaultTopBarHolder4 topBarHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topBarHolder = BIZTopBarFactory.createDefault4(view, this);
        topBarHolder.setViewVisible(topBarHolder.getTitleView(), View.GONE);
        return topBarHolder.getView();
    }

    Analytics.AnalyticsBuilder builder;

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
                builder = pageStayTime(draftDetailBean);
                //可能被重定向了
                if (mNewsDetail.getArticle().getDoc_type() == 3) {
                    url = mNewsDetail.getArticle().getWeb_link();
                } else {
                    url = mNewsDetail.getArticle().getUrl();
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
                    T.showShortNow(BrowserLinkActivity.this, errMsg);
                }
            }
        }).setTag(this)./*bindLoadViewHolder(replaceLoad(mContainer)).*/exe(mArticleId, mFromChannel);
    }//TODO WLJ 后面再调试

    /**
     * 顶部导航条在数据加载结束后再显示
     *
     * @param data 填充详情页数据
     */
    private void fillData(DraftDetailBean data) {
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


        //显示标题展示WebView内容等
        mWebView.loadUrl(url);
        if (topBarHolder != null) {
            topBarHolder.setViewVisible(topBarHolder.getSettingView(), View.VISIBLE);
        }
        //是否点赞
        if (data.getArticle().isLike_enabled()) {
            mMenuPrised.setVisibility(View.VISIBLE);
            mMenuPrised.setSelected(data.getArticle().isLiked());
        } else {
            mMenuPrised.setVisibility(View.GONE);
        }

        //禁止评论，隐藏评论框及评论按钮
        if (data.getArticle().getComment_level() == 0) {
            mMenuComment.setVisibility(View.GONE);
            mTvCommentsNum.setVisibility(View.GONE);
        } else {
            mMenuComment.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(data.getArticle().getComment_count_general())) {
                mTvCommentsNum.setVisibility(View.VISIBLE);
                mTvCommentsNum.setText(data.getArticle().getComment_count_general());
            }
        }
    }

    private Bundle bundle;

    @OnClick({R2.id.iv_back, R2.id.menu_comment, R2.id.menu_prised, R2.id.menu_setting})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;

        int id = view.getId();
        if (R.id.iv_back == id) {
            onBackPressed();
            //分享(无法获取链接稿第一张图，设置为浙江新闻LOGO)
        } else if (view.getId() == R.id.menu_comment) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                ClickInCommentList();
                //进入评论列表页面
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(IKey.NEWS_DETAIL, mNewsDetail);
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.COMMENT_ACTIVITY_PATH);
            }
            //点赞
        } else if (view.getId() == R.id.menu_prised) {
            ClickPriseIcon();

            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                ClickMoreIcon();
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

                UmengShareBean shareBean = UmengShareBean.getInstance()
                        .setSingle(false)
                        .setArticleId(mNewsDetail.getArticle().getId() + "")
                        .setImgUri(mNewsDetail.getArticle().getFirstPic())
                        .setTextContent(getString(R.string.module_detail_share_content_from))
                        .setTitle(mNewsDetail.getArticle().getDoc_title())
                        .setTargetUrl(url)
                        .setAnalyticsBean(bean).setEventName("NewsShare")
                        .setShareType("文章");

                MoreDialogLink.newInstance(mNewsDetail).setShareBean(shareBean).show(getSupportFragmentManager(), "MoreDialog");
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

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mView.setVisibility(View.VISIBLE);
        if (topBarHolder != null) {
            topBarHolder.setViewVisible(topBarHolder.getSettingView(), View.GONE);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.v_container, EmptyStateFragment.newInstance()).commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
        //统计时长
        if (builder != null) {
            Analytics mAnalytics = builder.build();
            if (mAnalytics != null) {
                mAnalytics.sendWithDuration();
            }
        }

        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
    }

    @Override
    public void ClickBack() {
        if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
            new Analytics.AnalyticsBuilder(getActivity(), "800001", "800001", "AppTabClick", false)
                    .setEvenName("点击返回")
                    .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                    .setObjectName(mNewsDetail.getArticle().getDoc_title())
                    .setObjectType(ObjectType.NewsType)
                    .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                    .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                    .setPageType("新闻详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                            .put("subject", "")
                            .toString())
                    .setSelfObjectID(mNewsDetail.getArticle().getId() + "").pageType("新闻详情页")
                    .clickTabName("返回")
                    .build()
                    .send();
        }
    }

    @Override
    public void ClickInCommentList() {
        new Analytics.AnalyticsBuilder(getActivity(), "800004", "800004", "AppTabClick", false)
                .setEvenName("点击评论，进入评论列表")
                .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                .setObjectName(mNewsDetail.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(mNewsDetail.getArticle().getId() + "")
                .pageType("新闻详情页")
                .clickTabName("评论按钮")
                .build()
                .send();
    }

    @Override
    public void ClickPriseIcon() {
        if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
            new Analytics.AnalyticsBuilder(getActivity(), "A0021", "A0021", "Support", false)
                    .setEvenName("点击点赞")
                    .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                    .setObjectName(mNewsDetail.getArticle().getDoc_title())
                    .setObjectType(ObjectType.NewsType)
                    .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                    .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                    .setPageType("新闻详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                            .put("subject", "")
                            .toString())
                    .setSelfObjectID(mNewsDetail.getArticle().getId() + "")
                    .newsID(mNewsDetail.getArticle().getMlf_id() + "")
                    .selfNewsID(mNewsDetail.getArticle().getId() + "")
                    .newsTitle(mNewsDetail.getArticle().getDoc_title())
                    .selfChannelID(mNewsDetail.getArticle().getChannel_id())
                    .channelName(mNewsDetail.getArticle().getChannel_name())
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
                .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                .setObjectName(mNewsDetail.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(mNewsDetail.getArticle().getId() + "")
                .pageType("新闻详情页")
                .clickTabName("更多")
                .build()
                .send();
    }


    public Analytics.AnalyticsBuilder pageStayTime(DraftDetailBean bean) {
        return new Analytics.AnalyticsBuilder(getContext(), "A0010", "800021", "ViewAppNewsDetail", true)
                .setEvenName("页面停留时长/阅读深度")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "").newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("新闻详情页")
                .pubUrl(bean.getArticle().getUrl());
    }

}