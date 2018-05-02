package com.zjrb.zjxw.detailproject.nomaldetail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliya.player.PlayerManager;
import com.aliya.view.fitsys.FitWindowsRecyclerView;
import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.aliya.view.ratio.RatioFrameLayout;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.CommonTopBarHolder;
import com.zjrb.core.common.glide.AppGlideOptions;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.PH;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.OutSizeAnalyticsBean;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.holder.EmptyPageHolder;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.utils.NetUtils;
import com.zjrb.core.utils.SettingManager;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.daily.news.global.biz.Format;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.global.PlayerAnalytics;
import com.zjrb.zjxw.detailproject.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;

import static com.zjrb.core.utils.UIUtils.getContext;


/**
 * 普通详情页
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsDetailActivity extends BaseActivity implements
        NewsDetailAdapter.CommonOptCallBack, View.OnClickListener, DetailCommentHolder.deleteCommentListener {

    /**
     * 稿件ID
     */
    public String mArticleId;
    private String mFromChannel;

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
    FrameLayout mFyContainer;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.v_container)
    FrameLayout mView;
    @BindView(R2.id.tv_duration)
    TextView mTvDuration;
    /**
     * 详情页数据
     */
    private DraftDetailBean mNewsDetail;
    /**
     * 详情页适配器
     */
    private NewsDetailAdapter mAdapter;

    private Analytics mAnalytics;


    private class VideoBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UIUtils.getString(com.zjrb.core.R.string.intent_action_close_video).equals(action)) {
                mVideoContainer.setVisibility(View.GONE);
            } else if (UIUtils.getString(com.zjrb.core.R.string.intent_action_open_video).equals(action)) {
                mVideoContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    private VideoBroadcastReceiver receive;
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucenceStatusBarBg();
        setContentView(R.layout.module_detail_activity_detail);
        ButterKnife.bind(this);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        receive = new VideoBroadcastReceiver();
        intentFilter = new IntentFilter(UIUtils.getString(com.zjrb.core.R.string.intent_action_close_video));
        intentFilter.addAction(UIUtils.getString(com.zjrb.core.R.string.intent_action_open_video));
        localBroadcastManager.registerReceiver(receive, intentFilter);
        getIntentData(getIntent());
        loadData();
    }

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
        topHolder = TopBarFactory.createCommonTopBar(view, this);
        return topHolder.getView();
    }

    /**
     * 初始化视频
     */
    private void initVideo(DraftDetailBean.ArticleBean bean) {
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
            if (SettingManager.getInstance().isAutoPlayVideoWithWifi() && NetUtils.isWifi()) {
                PlayerManager.get().play(mVideoContainer, bean.getVideo_url(), bean);
                PlayerManager.setPlayerCallback(mVideoContainer, PlayerAnalytics.get());
            }
        } else {
            mVideoContainer.setVisibility(View.GONE);
        }

        if (!NetUtils.isAvailable()) {
            T.showShort(getContext(), getString(R.string.module_detail_no_network));
        }
    }

    Analytics.AnalyticsBuilder builder;

    /**
     * 请求详情页数据
     */
    private void loadData() {
        if (mArticleId == null || mArticleId.isEmpty()) return;
        DraftDetailTask task = new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;
                Map map = new HashMap();
                map.put("relatedColumn", draftDetailBean.getArticle().getColumn_id());
                map.put("subject", "");
                builder = new Analytics.AnalyticsBuilder(getContext(), "A0010", "800021")
                        .setEvenName("页面停留时长/阅读深度")
                        .setObjectID(draftDetailBean.getArticle().getMlf_id() + "")
                        .setObjectName(draftDetailBean.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(draftDetailBean.getArticle().getChannel_id())
                        .setClassifyName(draftDetailBean.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setSelfObjectID(draftDetailBean.getArticle().getId() + "");
                if (mView.getVisibility() == View.VISIBLE) {
                    mView.setVisibility(View.GONE);
                }
                mNewsDetail = draftDetailBean;
                if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                    initVideo(mNewsDetail.getArticle());
                }
                fillData(mNewsDetail);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
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
            if (!TextUtils.isEmpty(article.getColumn_logo())) {
                GlideApp.with(topHolder.getIvIcon()).load(article.getColumn_logo()).centerCrop().into(topHolder.getIvIcon());
            }
            //订阅状态 采用select
            if (article.isColumn_subscribed()) {
                topHolder.getSubscribe().setText("已订阅");
                topHolder.getSubscribe().setSelected(true);
            } else {
                topHolder.getSubscribe().setText("+订阅");
                topHolder.getSubscribe().setSelected(false);
            }
        } else {
            topHolder.setViewVisible(topHolder.getFitRelativeLayout(), View.GONE);
        }

        mNewsDetail = data;
        initViewState(mNewsDetail);
        List datas = new ArrayList<>();
        //头
        datas.add(data);
        //web
        datas.add(data);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new NewsDetailSpaceDivider(0.5f, R.attr.dc_dddddd));
        mAdapter = new NewsDetailAdapter(datas, !TextUtils.isEmpty(mNewsDetail.getArticle().getVideo_url()) ? true : false);
        initAdapter(mAdapter);
        mAdapter.setEmptyView(
                new EmptyPageHolder(mRvContent,
                        EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据")
                ).itemView);
        mRvContent.setAdapter(mAdapter);
    }

    /**
     * 配置Adapter
     *
     * @param adapter 适配器
     */
    public void initAdapter(NewsDetailAdapter adapter) {
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
            mFyContainer.setVisibility(View.GONE);
            mMenuComment.setVisibility(View.GONE);
            mTvCommentsNum.setVisibility(View.GONE);
        } else {
            mFyContainer.setVisibility(View.VISIBLE);
            mMenuComment.setVisibility(View.VISIBLE);
        }
    }


//    /**
//     * 订阅
//     */
//    @Override
//    public void onOptSubscribe() {
//        //如果栏目未订阅
//        new ColumnSubscribeTask(new APIExpandCallBack<Void>() {
//
//            @Override
//            public void onSuccess(Void baseInnerData) {
//                T.showShort(getBaseContext(), getString(R.string.module_detail_subscribe_success));
//            }
//
//            @Override
//            public void onAfter() {
//                mAdapter.updateSubscribeInfo();
//            }
//
//            @Override
//            public void onError(String errMsg, int errCode) {
//                T.showShortNow(NewsDetailActivity.this, errMsg);
//            }
//
//        }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), true);
//
//    }

    /**
     * WebView加载完毕
     */
    @Override
    public void onOptPageFinished() {
        mAdapter.showAll();
    }

    private Bundle bundle;

//    /**
//     * 进入栏目列表页
//     */
//    @Override
//    public void onOptClickColumn() {
//        Nav.with(this).to(Uri.parse("http://www.8531.cn/subscription/detail").buildUpon().appendQueryParameter("id", String.valueOf(mNewsDetail.getArticle().getColumn_id())).build().toString());
//    }

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
        new DraftPraiseTask(new APIExpandCallBack<Void>() {

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
        }).setTag(this).exe(mArticleId, true);
    }

    @OnClick({R2.id.menu_comment, R2.id.menu_prised, R2.id.menu_setting,
            R2.id.tv_comment, R2.id.iv_top_share, R2.id.iv_type_video, R2.id.iv_top_bar_back,
            R2.id.tv_top_bar_subscribe_text, R2.id.tv_top_bar_title})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        //评论列表
        if (view.getId() == R.id.menu_comment) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                //埋点，进入评论列表
                new Analytics.AnalyticsBuilder(getContext(), "800004", "800004")
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
                        .build()
                        .send();

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
                new Analytics.AnalyticsBuilder(getContext(), "A0021", "A0021")
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
                        .build()
                        .send();
            }
            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                new Analytics.AnalyticsBuilder(getContext(), "800005", "800005")
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
                        .build()
                        .send();
                MoreDialog.newInstance(mNewsDetail).setWebViewCallBack(mAdapter.getWebViewHolder(), mAdapter.getWebViewHolder()).show(getSupportFragmentManager(), "MoreDialog");
            }

            //评论框
        } else if (view.getId() == R.id.tv_comment) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                //进入评论编辑页面(不针对某条评论)
                new Analytics.AnalyticsBuilder(getContext(), "800002", "800002")
                        .setEvenName("点击评论输入框")
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
                        .build()
                        .send();

                //评论发表成功
                Analytics analytics = new Analytics.AnalyticsBuilder(getContext(), "A0023", "A0023")
                        .setEvenName("发表评论，且发送成功")
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
                        .build();
                CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(mNewsDetail.getArticle().getId()))).setWMData(analytics).show(getSupportFragmentManager(), "CommentWindowDialog");

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
                        .setTargetUrl(mNewsDetail.getArticle().getUrl()));
                //点击分享操作
                new Analytics.AnalyticsBuilder(getContext(), "800018", "800018")
                        .setEvenName("点击分享")
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
                        .build()
                        .send();
            }

            //重新加载
        } else if (view.getId() == R.id.iv_type_video) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null && !TextUtils.isEmpty(mNewsDetail.getArticle().getVideo_url())) {
                PlayerManager.get().play(mVideoContainer, mNewsDetail.getArticle().getVideo_url(), mNewsDetail.getArticle());
                PlayerManager.setPlayerCallback(mVideoContainer, PlayerAnalytics.get());
            }
        } else if (view.getId() == R.id.iv_top_bar_back) {
            //点击返回操作
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                new Analytics.AnalyticsBuilder(getContext(), "800001", "800001")
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
                        .setSelfObjectID(mNewsDetail.getArticle().getId() + "")
                        .build()
                        .send();
            }
            finish();
        } else if (view.getId() == R.id.tv_top_bar_subscribe_text) {
            //已订阅状态->取消订阅
            if (topHolder.getSubscribe().isSelected()) {
                new ColumnSubscribeTask(new APIExpandCallBack<Void>() {

                    @Override
                    public void onSuccess(Void baseInnerData) {
                        topHolder.getSubscribe().setSelected(false);
                        topHolder.getSubscribe().setText("+订阅");
                    }

                    @Override
                    public void onError(String errMsg, int errCode) {
                        T.showShortNow(NewsDetailActivity.this, "取消订阅失败");
                    }

                }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), false);
            } else {//未订阅状态->订阅
                if (topHolder.getSubscribe().isSelected()) {
                    new ColumnSubscribeTask(new APIExpandCallBack<Void>() {

                        @Override
                        public void onSuccess(Void baseInnerData) {
                            topHolder.getSubscribe().setSelected(true);
                            topHolder.getSubscribe().setText("已订阅");
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
            Bundle bundle = new Bundle();
            bundle.putString(IKey.ID, String.valueOf(mNewsDetail.getArticle().getColumn_id()));
            Nav.with(UIUtils.getContext()).setExtras(bundle)
                    .toPath("/subscription/detail");
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
        super.onDestroy();
        if (builder != null) {
            //阅读深度
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                builder.setPercentage(mScale + "");
            }
            mAnalytics = builder.build();
            if (mAnalytics != null) {
                mAnalytics.sendWithDuration();
            }
        }
        localBroadcastManager.unregisterReceiver(receive);
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

}


