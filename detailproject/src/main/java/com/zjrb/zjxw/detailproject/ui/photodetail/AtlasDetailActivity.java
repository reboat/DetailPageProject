package com.zjrb.zjxw.detailproject.ui.photodetail;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.permission.IPermissionCallBack;
import com.zjrb.core.permission.Permission;
import com.zjrb.core.permission.PermissionManager;
import com.zjrb.core.utils.StringUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.AlbumImageListBean;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.PromoteResponse;
import com.zjrb.zjxw.detailproject.apibean.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.apibean.task.PromoteTask;
import com.zjrb.zjxw.detailproject.apibean.task.RedBoatTask;
import com.zjrb.zjxw.detailproject.callback.DetailInterface;
import com.zjrb.zjxw.detailproject.ui.boardcast.SubscribeReceiver;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.ui.photodetail.adapter.ImagePrePagerAdapter;
import com.zjrb.zjxw.detailproject.ui.topbar.AtlasTopBarHolder;
import com.zjrb.zjxw.detailproject.utils.BizUtils;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;
import com.zjrb.zjxw.detailproject.utils.YiDunToken;
import com.zjrb.zjxw.detailproject.utils.global.C;
import com.zjrb.zjxw.detailproject.widget.AtlasLoad;

import java.util.List;

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
import cn.daily.news.biz.core.ui.dialog.BottomSaveDialogFragment;
import cn.daily.news.biz.core.ui.dialog.CommentWindowDialog;
import cn.daily.news.biz.core.ui.dialog.RankTipDialog;
import cn.daily.news.biz.core.ui.toast.ZBToast;
import cn.daily.news.biz.core.ui.widget.DepthPageTransformer;
import cn.daily.news.biz.core.ui.widget.photoview.HackyViewPager;
import cn.daily.news.biz.core.utils.DownloadUtil;
import cn.daily.news.biz.core.utils.PathUtil;
import cn.daily.news.biz.core.utils.RouteManager;

import static com.zjrb.core.utils.UIUtils.getContext;


/**
 * 图集详情页
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class AtlasDetailActivity extends DailyActivity implements ViewPager
        .OnPageChangeListener, View.OnTouchListener, CommentWindowDialog.LocationCallBack, DetailInterface.SubscribeSyncInterFace {

    @BindView(R2.id.ry_container)
    FitWindowsFrameLayout mContainer;
    @BindView(R2.id.view_pager)
    HackyViewPager mViewPager;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_index)
    TextView mTvIndex;
    @BindView(R2.id.tv_tottle_num)
    TextView mTvTottleNum;
    @BindView(R2.id.tv_content)
    TextView mTvContent;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.floor_bar)
    RelativeLayout mFloorBar;
    @BindView(R2.id.container_bottom)
    LinearLayout mContainerBottom;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.ly_tip_contain)
    RelativeLayout mLyContainer;
    @BindView(R2.id.fl_comment)
    RelativeLayout mFyContainer;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_source)
    TextView mTvSource;
    @BindView(R2.id.tv_name)
    TextView mTvName;
    @BindView(R2.id.v_container)
    FrameLayout mView;
    @BindView(R2.id.atlas_scrollView)
    ScrollView mScrollView;
    @BindView(R2.id.atlas_container)
    LinearLayout mBottomContainer;

    @BindView(R2.id.ly_comment_num)
    RelativeLayout ly_comment_num;

    /**
     * 稿件ID
     */
    public String mArticleId = "";

    private int mIndex;
    private List<AlbumImageListBean> mAtlasList;
    private DraftDetailBean mData;
    //订阅同步广播
    private SubscribeReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setOverly(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_photo_detail);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        initSubscribeReceiver();
        mFloorBar.setOnTouchListener(this);
        loadData();
        mScrollView.setOnTouchListener(new AtlasScrollListener());
    }

    private float y = 0;


    /**
     * topbar
     */
    private AtlasTopBarHolder topHolder;
    //    private TextView mTvMore;
    private ImageView mIvDownLoad;
    private ImageView mIvShare;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = new AtlasTopBarHolder(view, this);
        mIvDownLoad = topHolder.getDownView();
        mIvShare = topHolder.getShareView();
        topHolder.getView().setBackgroundColor(Color.TRANSPARENT);
        return topHolder.getView();
    }


    /**
     * 显示顶部栏显示/隐藏
     *
     * @param flag
     */
    private void setTopBarInOut(int flag) {
        topHolder.getView().setVisibility(flag);
    }


    private String mFromChannel;
    private boolean isRedAlbum = false;

    /**
     * 订阅广播
     */
    private void initSubscribeReceiver() {
        mReceiver = new SubscribeReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter("subscribe_success"));
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
                //红船号图集
                if (!TextUtils.isEmpty(data.getPath()) && data.getPath().equals("/red_boat_album.html")) {
                    isRedAlbum = true;
                } else {
                    isRedAlbum = false;
                }
            }
        }
    }

    /**
     * 重新初始化顶部栏和底部栏
     */
    private void reInitView() {
        mIndex = 0;
        topHolder.getView().setVisibility(View.GONE);
        mContainerBottom.setVisibility(View.VISIBLE);
        setTopBarInOut(View.VISIBLE);
        mScrollView.setVisibility(View.VISIBLE);
        mLyContainer.setVisibility(View.VISIBLE);
        mTvContent.setVisibility(View.VISIBLE);
        mTvSource.setVisibility(View.VISIBLE);
        mTvName.setVisibility(View.VISIBLE);
    }

    /**
     * singleTop启动模式下复用页面,普通图集与红船号图集复用图集
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
        loadData();
        reInitView();
    }

    /**
     * 获取图集数据
     */
    private void loadData() {
        AtlasLoad holder = new AtlasLoad(mViewPager, mContainer);
        //获取详情页
        if (!isRedAlbum) {
            new DraftDetailTask(new APICallBack<DraftDetailBean>() {
                @Override
                public void onSuccess(DraftDetailBean atlasDetailEntity) {
                    if (atlasDetailEntity == null || atlasDetailEntity.getArticle() == null
                            || atlasDetailEntity.getArticle().getAlbum_image_list() == null
                            || atlasDetailEntity.getArticle().getAlbum_image_list().isEmpty()) {
                        return;
                    }

                    //设置下载按钮
                    if (atlasDetailEntity.getArticle().getAlbum_image_list() != null &&
                            !atlasDetailEntity.getArticle().getAlbum_image_list().isEmpty()) {
                        mIvDownLoad.setVisibility(View.VISIBLE);
                    } else {
                        mIvDownLoad.setVisibility(View.GONE);
                    }
                    fillData(atlasDetailEntity);
                    YiDunToken.synYiDunToken(mArticleId);
                }

                @Override
                public void onError(String errMsg, int errCode) {
                    //图集撤稿
                    if (errCode == C.DRAFFT_IS_NOT_EXISE) {
                        showEmptyNewsDetail();
                    } else {
                        ZBToast.showShort(AtlasDetailActivity.this, errMsg);
                    }
                }
            }).setTag(this).bindLoadViewHolder(holder).exe(mArticleId, mFromChannel);
        } else {//红船号图集
            new RedBoatTask(new LoadingCallBack<DraftDetailBean>() {
                @Override
                public void onSuccess(DraftDetailBean atlasDetailEntity) {
                    if (atlasDetailEntity == null || atlasDetailEntity.getArticle() == null
                            || atlasDetailEntity.getArticle().getAlbum_image_list() == null
                            || atlasDetailEntity.getArticle().getAlbum_image_list().isEmpty()) {
                        return;
                    }

                    //设置下载按钮
                    if (atlasDetailEntity.getArticle().getAlbum_image_list() != null &&
                            !atlasDetailEntity.getArticle().getAlbum_image_list().isEmpty()) {
                        mIvDownLoad.setVisibility(View.VISIBLE);
                    } else {
                        mIvDownLoad.setVisibility(View.GONE);
                    }
                    fillData(atlasDetailEntity);
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
                        ZBToast.showShort(AtlasDetailActivity.this, errMsg);
                    }
                }
            }).setTag(this).bindLoadViewHolder(holder).exe(mArticleId);
        }

    }

    /**
     * @param data 获取图集详情页数据
     */
    private void fillData(DraftDetailBean data) {
        //阅读深度
        mReadingScale = (readedIndex + 1) * 1f / (data.getArticle().getAlbum_image_count());
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
        }

        mData = data;
        //设置数据
        if (data != null) {
            if (data.getArticle().getAlbum_image_list() != null &&
                    !data.getArticle().getAlbum_image_list().isEmpty()) {
                mAtlasList = data.getArticle().getAlbum_image_list();
            }
            mAtlasList = data.getArticle().getAlbum_image_list();
            initViewState(mData);
        }
        //设置图片列表
        if (mAtlasList != null && !mAtlasList.isEmpty()) {
            mViewPager.addOnPageChangeListener(this);
            mViewPager.setPageTransformer(true, new DepthPageTransformer());
            //设置图集标题和指示器
            mTvIndex.setText((mIndex + 1) + "/");
            mTvTottleNum.setText(String.valueOf(data.getArticle()
                    .getAlbum_image_count()));
            mTvTitle.setText(data.getArticle().getDoc_title());
            //新闻来源
            if (!TextUtils.isEmpty(mData.getArticle().getSource())) {
                mTvSource.setVisibility(View.VISIBLE);
                mTvSource.setText(mData.getArticle().getSource());
            } else {
                mTvSource.setVisibility(View.GONE);
            }

            //新闻作者
            if (!TextUtils.isEmpty(mData.getArticle().getAuthor())) {
                mTvName.setVisibility(View.VISIBLE);
                mTvName.setText(mData.getArticle().getAuthor());
            } else {
                mTvName.setVisibility(View.GONE);
            }

            //添加更多图集(假如有相关新闻)
            if (mData.getArticle().getRelated_news() != null && data.getArticle()
                    .getRelated_news().size() > 0) {
                mAtlasList.add(new AlbumImageListBean());
            }
            //设置图片count
            data.getArticle().setAlbum_image_list(mAtlasList);
            data.getArticle().setAlbum_image_count(mAtlasList.size());
            mViewPager.setAdapter(new ImagePrePagerAdapter(getSupportFragmentManager(),
                    data));

            AlbumImageListBean entity = mAtlasList.get(mIndex);
            mTvContent.setText(entity.getDescription());
        }

        calculationMaxHeight();

    }

    private int mMaxHeight = 0;
    private int mMinHeight = 0;

    /**
     * 计算底部栏最大高度
     */
    private void calculationMaxHeight() {

        mTvTitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int titleHeight = mLyContainer.getMeasuredHeight() + mLyContainer.getPaddingBottom();
                mMaxHeight = mBottomContainer.getMeasuredHeight();
                int defaultMin = (int) (titleHeight + (mTvContent.getLineHeight() + mTvContent.getLineSpacingExtra()) * 2.5);
                mMinHeight = (titleHeight + mTvContent.getHeight()) < defaultMin ? titleHeight + mTvContent.getHeight() : defaultMin;
                ViewGroup.LayoutParams paramsSc = mScrollView.getLayoutParams();
                if (paramsSc != null) {
                    paramsSc.height = mMinHeight;
                    mScrollView.setLayoutParams(paramsSc);
                }

                mTvTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * 刷新底部栏状态
     *
     * @param data
     */
    private void initViewState(DraftDetailBean data) {
        //中间栏目布局处理
        if (!TextUtils.isEmpty(data.getArticle().getColumn_name())) {
            //栏目名称
            topHolder.setViewVisible(topHolder.getFrlTitle(), View.VISIBLE);
            topHolder.getTitleView().setText(data.getArticle().getColumn_name());
            //栏目头像
            GlideApp.with(topHolder.getIvIcon()).load(data.getArticle().getColumn_logo()).placeholder(R.mipmap.ic_top_bar_redboat_icon)
                    .error(R.mipmap.ic_top_bar_redboat_icon).centerCrop().into(topHolder.getIvIcon());
            //订阅状态 采用select
            if (data.getArticle().isColumn_subscribed()) {
                topHolder.getSubscribe().setSelected(true);
                if (isRedAlbum) {
                    if (BizUtils.isRankEnable()) {
                        topHolder.getSubscribe().setVisibility(View.INVISIBLE);
                        topHolder.rankActionView.setVisibility(View.VISIBLE);
                        if (data.getArticle().rank_hited) {
                            topHolder.rankActionView.setText("拉票");
                        } else {
                            topHolder.rankActionView.setText("打榜");
                        }
                    } else {
                        topHolder.getSubscribe().setSelected(true);
                    }
                }
            } else {
                topHolder.getSubscribe().setSelected(false);
            }
        } else {
            topHolder.setViewVisible(topHolder.getFrlTitle(), View.GONE);
        }

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
            if (!TextUtils.isEmpty(data.getArticle().getComment_count_general()) && !isRedAlbum) {
                mTvCommentsNum.setVisibility(View.VISIBLE);
                mTvCommentsNum.setText(data.getArticle().getComment_count_general());
            } else {
                mTvCommentsNum.setVisibility(View.INVISIBLE);
            }
        }
    }

    @OnClick(R2.id.rank_action_view)
    public void onRankActionClick(final TextView view) {
        if (mData != null && mData.getArticle() != null) {
            final DraftDetailBean.ArticleBean bean = mData.getArticle();
            if (bean.rank_hited) {

                String shareName = String.format("我正在为起航号“%s”拉赞助力，快来和我一起为它加油！", bean.getColumn_name());
                String shareDes = String.format("点击查看起航号“%s”榜上排名", bean.getColumn_name());
                String shareUrl = "https://zj.zjol.com.cn/";

                OutSizeAnalyticsBean analyticsBean = OutSizeAnalyticsBean
                        .getInstance()
                        .setPageType("新闻详情页")
                        .setColumn_id(String.valueOf(bean.getColumn_id()))
                        .setColumn_name(bean.getColumn_name())
                        .setObjectType(ObjectType.C90);

                UmengShareBean shareBean = UmengShareBean.getInstance()
                        .setSingle(false)
                        .setTitle(shareName)
                        .setCardPageType("卡片详情页")
                        .setAnalyticsBean(analyticsBean)
                        .setTextContent(shareDes)
                        .setTargetUrl(TextUtils.isEmpty(bean.rank_share_url) ? shareUrl : bean.rank_share_url)
                        .setShareType("栏目")
                        .setNewsCard(false)
                        .setCardUrl(bean.rank_card_url);
                if (!StringUtils.isEmpty(bean.getColumn_logo())) {
                    shareBean.setImgUri(bean.getColumn_logo());
                } else {
                    shareBean.setPicId(R.mipmap.ic_launcher);
                }
                shareBean.setPicId(R.mipmap.ic_launcher);
                UmengShareUtils.getInstance().startShare(shareBean);


                new Analytics.AnalyticsBuilder(view.getContext(), "A0062", "", false)
                        .name("点击拉票")
                        .pageType("新闻详情页")
                        .columnID(String.valueOf(bean.getColumn_id()))
                        .columnName(bean.getColumn_name())
                        .seObjectType(ObjectType.C90)
                        .build()
                        .send();

            } else {
                new PromoteTask(new APICallBack<PromoteResponse>() {
                    @Override
                    public void onError(String errMsg, int errCode) {
                        super.onError(errMsg, errCode);
                        if (errCode == 53003) {
                            ZBToast.showShort(AtlasDetailActivity.this, errMsg);
                        } else {
                            ZBToast.showShort(AtlasDetailActivity.this, "打榜失败");
                        }
                    }

                    @Override
                    public void onSuccess(final PromoteResponse data) {
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                ZBToast.showShort(getContext(), data.toast);
                                bean.rank_hited = true;
                                view.setText("拉票");
                            }
                        });
                    }
                }).exe(mData.getArticle().getColumn_id());

                new Analytics.AnalyticsBuilder(view.getContext(), "A0061", "", false)
                        .name("点击打榜")
                        .pageType("新闻详情页")
                        .columnID(String.valueOf(bean.getColumn_id()))
                        .columnName(bean.getColumn_name())
                        .seObjectType(ObjectType.C90)
                        .build()
                        .send();
            }
        }
    }

    @OnClick({R2.id.iv_share, R2.id.fl_comment, R2.id.tv_comment, R2.id.menu_comment, R2.id.menu_prised, R2.id
            .menu_setting, R2.id.iv_top_download, R2.id.tv_top_bar_subscribe_text, R2.id.tv_top_bar_title_sub, R2.id.iv_top_subscribe_icon})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        click(view.getId());
    }

    private Bundle bundle;

    /**
     * 点击事件
     */
    private void click(int id) {
        //返回
        if (id == R.id.iv_back) {
            if (mData != null && mData.getArticle() != null) {
                DataAnalyticsUtils.get().ClickBack(mData);
            }

            finish();
            //分享
        } else if (id == R.id.iv_share) {
            if (mData != null && mData.getArticle() != null && !TextUtils.isEmpty(mData.getArticle().getUrl())) {
                new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.forward)
                        .setTargetID(mData.getArticle().getId() + "")
                        .setUrl(mData.getArticle().getUrl())
                        .build()
                        .send();

                //点击分享按钮埋点
                DataAnalyticsUtils.get().ClickShareTab(mData);
                //分享专用bean
                OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                        .setObjectID(mData.getArticle().getMlf_id() + "")
                        .setObjectName(mData.getArticle().getDoc_title())
                        .setObjectType(ObjectType.C01)
                        .setUrl(mData.getArticle().getUrl())
                        .setClassifyID(mData.getArticle().getChannel_id() + "")
                        .setClassifyName(mData.getArticle().getChannel_name())
                        .setColumn_id(String.valueOf(mData.getArticle().getColumn_id()))
                        .setColumn_name(mData.getArticle().getColumn_name())
                        .setPageType("图集详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mData.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfobjectID(mData.getArticle().getId() + "");

                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setNewsCard(true)
                        .setCardUrl(mData.getArticle().getCard_url())
                        .setArticleId(mData.getArticle().getId() + "")
                        .setImgUri(mData.getArticle().getAlbum_image_list().get(0).getImage_url())
                        .setTextContent(mData.getArticle().getAlbum_image_list().get(0)
                                .getDescription())
                        .setTitle(mData.getArticle().getDoc_title())
                        .setTargetUrl(mData.getArticle().getUrl())
                        .setAnalyticsBean(bean).setEventName("NewsShare")
                        .setShareType("文章")
                );
            }
            //评论框
        } else if (id == R.id.fl_comment || id == R.id.tv_comment) {
            if (mData != null && mData.getArticle() != null) {
                DataAnalyticsUtils.get().ClickCommentBox(mData);

                //评论发表成功
                Analytics analytics = DataAnalyticsUtils.get().CreateCommentAnalytics(mData, false);
                try {
                    CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(String
                            .valueOf(mData.getArticle().getId())))).setWMData(analytics).setLocationCallBack(this).show(getSupportFragmentManager(),
                            "CommentWindowDialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //评论列表
        } else if (id == R.id.menu_comment) {
            if (mData != null && mData.getArticle() != null) {

                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(IKey.NEWS_DETAIL, mData);
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager
                        .COMMENT_ACTIVITY_PATH);

            }
            //点赞
        } else if (id == R.id.menu_prised) {
            if (mData != null && mData.getArticle() != null) {
                DataAnalyticsUtils.get().ClickPriseIcon(mData);
            }
            fabulous();
            //设置
        } else if (id == R.id.menu_setting) {
            if (mData != null && mData.getArticle() != null) {
                DataAnalyticsUtils.get().ClickMoreIcon(mData);
                MoreDialog.newInstance(mData).show(getSupportFragmentManager(), "MoreDialog");
            }
            //下载
        } else if (id == R.id.iv_top_download) {
            if (mData != null && mData.getArticle() != null) {
                DataAnalyticsUtils.get().ClickDownLoad(mData);
            }
            loadImage(mIndex);
            //点击订阅
        } else if (id == R.id.tv_top_bar_subscribe_text) {
            //已订阅状态->取消订阅
            if (topHolder.getSubscribe().isSelected()) {
                DataAnalyticsUtils.get().SubscribeAnalytics(mData, "订阅号取消订阅", "A0114", "SubColumn", "取消订阅");
                new ColumnSubscribeTask(new LoadingCallBack<Void>() {

                    @Override
                    public void onSuccess(Void baseInnerData) {
                        topHolder.getSubscribe().setSelected(false);
                        ZBToast.showShort(getApplicationContext(), "操作成功");
                        syncSubscribeColumn(false, mData.getArticle().getColumn_id());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(String errMsg, int errCode) {
                        ZBToast.showShort(AtlasDetailActivity.this, "操作失败");
                    }

                }).setTag(this).exe(mData.getArticle().getColumn_id(), false);
            } else {//未订阅状态->订阅
                DataAnalyticsUtils.get().SubscribeAnalytics(mData, "订阅号订阅", "A0014", "SubColumn", "订阅");
                if (!topHolder.getSubscribe().isSelected()) {
                    new ColumnSubscribeTask(new LoadingCallBack<Void>() {

                        @Override
                        public void onSuccess(Void baseInnerData) {

                            if (isRedAlbum && BizUtils.isRankEnable()) {
                                hitRank();
                            }
                            ZBToast.showShort(getApplicationContext(), "操作成功");
                            topHolder.getSubscribe().setSelected(true);
                            syncSubscribeColumn(true, mData.getArticle().getColumn_id());

                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(String errMsg, int errCode) {
                            ZBToast.showShort(AtlasDetailActivity.this, "操作失败");
                        }

                    }).setTag(this).exe(mData.getArticle().getColumn_id(), true);
                }

            }
            //进入栏目
        } else if (id == R.id.tv_top_bar_title_sub || id == R.id.iv_top_subscribe_icon) {
            DataAnalyticsUtils.get().SubscribeAnalytics(mData, "点击进入栏目详情页", "800031", "ToDetailColumn", "");
            if (!TextUtils.isEmpty(mData.getArticle().getColumn_url())) {
                Nav.with(UIUtils.getContext()).to(mData.getArticle().getColumn_url());
            }
        }
    }

    /**
     * 打榜
     */
    private void hitRank() {
        if (!mData.getArticle().rank_hited) {
            RankTipDialog.Builder builder = new RankTipDialog.Builder()
                    .setLeftText("取消")
                    .setRightText("打榜")
                    .setMessage("关注成功，来为它打榜，助它荣登榜首吧！")
                    .setOnLeftClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Analytics.AnalyticsBuilder(getActivity(), "200037", "", false)
                                    .name("点击取消打榜")
                                    .pageType("弹框")
                                    .build()
                                    .send();
                        }
                    })
                    .setOnRightClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendActionRequest(mData.getArticle().getColumn_id());
                            new Analytics.AnalyticsBuilder(getActivity(), "200038", "", false)
                                    .name("点击继续打榜")
                                    .pageType("弹框")
                                    .build()
                                    .send();
                        }
                    });
            RankTipDialog dialog = new RankTipDialog(AtlasDetailActivity.this);
            dialog.setBuilder(builder);
            dialog.show();
        } else {
            topHolder.rankActionView.setText("拉票");
        }
        topHolder.getSubscribe().setVisibility(View.INVISIBLE);
        topHolder.rankActionView.setVisibility(View.VISIBLE);
    }

    private void sendActionRequest(final long column_id) {
        new PromoteTask(new APICallBack<PromoteResponse>() {
            @Override
            public void onError(String errMsg, int errCode) {
                super.onError(errMsg, errCode);
                if (errCode == 53003) {
                    ZBToast.showShort(AtlasDetailActivity.this, errMsg);
                }
            }

            @Override
            public void onSuccess(final PromoteResponse data) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ZBToast.showShort(AtlasDetailActivity.this, data.toast);
                        mData.getArticle().rank_hited = true;
                        topHolder.rankActionView.setText("拉票");
                        BizUtils.syncRankState(AtlasDetailActivity.this, column_id, data.delta_count);
                    }
                });
            }
        }).exe(column_id);
    }


    //图集阅读深度
    private float mReadingScale;
    //阅读数量
    private int readedIndex = 0;

    @Override
    public void onPageSelected(int position) {
        //只记录最大阅读
        if (readedIndex <= position) {
            readedIndex = position;
        }
        mIndex = position;
        mTvIndex.setText(String.valueOf(mIndex + 1) + "/");
        setSwipeBackEnable(0 == position);
        AlbumImageListBean entity = mAtlasList.get(position);
        //阅读深度
        mReadingScale = (readedIndex + 1) * 1f / (mData.getArticle().getAlbum_image_count());
        mTvContent.setText(entity.getDescription());
        mTvContent.scrollTo(0, 0);

        //更多图集
        if (mData.getArticle().getRelated_news() != null && mData.getArticle().getRelated_news()
                .size() > 0) {
            setTopTitle(position);
        }

        //文案显示
        if (mData.getArticle().getRelated_news() != null && mData.getArticle().getRelated_news()
                .size() > 0 && position == (mAtlasList.size() - 1)) {
            mLyContainer.setVisibility(View.GONE);
            mTvContent.setVisibility(View.GONE);
        } else {
            mLyContainer.setVisibility(View.VISIBLE);
            mTvContent.setVisibility(View.VISIBLE);
        }

        //下载按钮
        if (mAtlasList != null && !mAtlasList.isEmpty()) {
            if (!TextUtils.isEmpty(mAtlasList.get(position).getImage_url())) {
                if (mIvDownLoad.getVisibility() == View.GONE) {
                    mIvDownLoad.setVisibility(View.VISIBLE);
                }
                //更多图集不需要显示下载图标
                if (mData.getArticle().getRelated_news() != null && mData.getArticle()
                        .getRelated_news().size() > 0
                        && position == (mAtlasList.size() - 1) && mIvDownLoad.getVisibility() ==
                        View.VISIBLE) {
                    mIvDownLoad.setVisibility(View.GONE);
                }
            } else {
                if (mIvDownLoad.getVisibility() == View.VISIBLE) {
                    mIvDownLoad.setVisibility(View.GONE);
                }
            }
        } else {
            if (mIvDownLoad.getVisibility() == View.VISIBLE) {
                mIvDownLoad.setVisibility(View.GONE);
            }
        }

        if (position == mAtlasList.size() - 1 && mData.getArticle().getRelated_news() != null && mData.getArticle().getRelated_news().size() > 0) {
            mScrollView.setVisibility(View.GONE);
        } else {
            mScrollView.setVisibility(View.VISIBLE);
        }

        calculationMaxHeight();

    }

    /**
     * @param position 设置顶部topbar标题
     */
    private void setTopTitle(int position) {
        if (mAtlasList != null && !mAtlasList.isEmpty()) {
            //更多页面
            if (position == (mAtlasList.size() - 1)) {
//                mTvMore.setVisibility(View.VISIBLE);
//                topHolder.getFrlTitle().setVisibility(View.GONE);
                setTopBarInOut(View.GONE);
            } else {
//                mTvMore.setVisibility(View.GONE);
//                topHolder.getFrlTitle().setVisibility(View.VISIBLE);
                setTopBarInOut(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) { // 手指触摸到FloorBar上拦截
        return true;
    }

    /**
     * 稿件点赞
     */
    private void fabulous() {
        if (mData == null || mData.getArticle() == null) return;
        if (mData.getArticle().isLiked()) {
            ZBToast.showShort(this, getString(R.string.module_detail_you_have_liked));
            return;
        }
        new DraftPraiseTask(new APICallBack<Void>() {
            @Override
            public void onError(String errMsg, int errCode) {
                if (errCode == 50013) {
                    mData.getArticle().setLiked(true);
                    mMenuPrised.setSelected(true);
                    ZBToast.showShort(UIUtils.getContext(), "已点赞成功");
                } else {
                    ZBToast.showShort(UIUtils.getContext(), errMsg);
                }
            }

            @Override
            public void onSuccess(Void baseInnerData) {
                ZBToast.showShort(UIUtils.getContext(), getString(R.string.module_detail_prise_success));
                mData.getArticle().setLiked(true);
                mMenuPrised.setSelected(true);
            }
        }).setTag(this).exe(mArticleId, true, mData.getArticle().getUrl());
    }

    /**
     * 加载图片
     *
     * @param position
     */
    public void loadImage(final int position) {
        BottomSaveDialogFragment dialogFragment = new BottomSaveDialogFragment();
        dialogFragment.setSaveListener(new BottomSaveDialogFragment.OnSaveDialogClickListener() {
            @Override
            public void onSave() {
                try {
                    if (mAtlasList == null || mAtlasList.size() < position || mAtlasList.get
                            (position).equals(""))
                        return;
                    PermissionManager.get().request(AtlasDetailActivity.this, new
                            IPermissionCallBack() {
                                @Override
                                public void onGranted(boolean isAlreadyDef) {
                                    String url = mAtlasList.get(position).getImage_url();
                                    download(url);
                                }

                                @Override
                                public void onDenied(List<String> neverAskPerms) {
                                    PermissionManager.showAdvice(AtlasDetailActivity.this,
                                            "保存图片需要开启存储权限");
                                }

                                @Override
                                public void onElse(List<String> deniedPerms, List<String>
                                        neverAskPerms) {

                                }
                            }, Permission.STORAGE_READE, Permission.STORAGE_WRITE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "BottomSaveDialogFragment");
    }

    /**
     * @param url 下载图片
     */
    private void download(String url) {
        //图片特殊处理
        if (!TextUtils.isEmpty(url) && url.contains("?w=")) {
            url = url.split("[?]")[0];
        }
        DownloadUtil.get()
                .setDir(PathUtil.getImagePath())
                .setListener(new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onLoading(int progress) {

                    }

                    @Override
                    public void onSuccess(String path) {
                        ZBToast.showShort(AtlasDetailActivity.this, getString(R.string
                                .module_detail_save_success));
                    }

                    @Override
                    public void onFail(String err) {
                        ZBToast.showShort(AtlasDetailActivity.this, getString(R.string
                                .module_detail_save_failed));
                    }
                })
                .download(PathUtil.getSpliteUrl(url));
    }


    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mView.setVisibility(View.VISIBLE);
        mContainerBottom.setVisibility(View.GONE);
        topHolder.getView().setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.v_container, EmptyStateFragment.newInstance()).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);

    }

    /**
     * 同步订阅栏目
     */
    private void syncSubscribeColumn(boolean isSubscribe, int columnid) {
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

    @Override
    public void subscribeSync(Intent intent) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction()) && "subscribe_success".equals(intent.getAction())) {
            long id = intent.getLongExtra("id", 0);
            boolean subscribe = intent.getBooleanExtra("subscribe", false);
            //确定是该栏目需要同步
            if (id == mData.getArticle().getColumn_id()) {
                topHolder.getSubscribe().setSelected(subscribe);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mData != null && mData.getArticle() != null) {
//            //新华智云
            new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.comeIn)
                    .setTargetID(mData.getArticle().getId() + "")
                    .setUrl(mData.getArticle().getUrl())
                    .build()
                    .send();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //新华智云
        if (mData != null && mData.getArticle() != null) {
            new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.leave)
                    .setTargetID(mData.getArticle().getId() + "")
                    .setUrl(mData.getArticle().getUrl())
                    .build()
                    .send();
        }
    }


    private class AtlasScrollListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    y = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float duration = y - event.getRawY();
                    y = event.getRawY();
                    if (Math.abs(duration) < 10) {
                        return true;
                    }
                    ViewGroup.LayoutParams params = mScrollView.getLayoutParams();
                    if (params != null) {
                        params.height += duration;
                        if (params.height <= mMinHeight) {
                            params.height = mMinHeight;
                            mScrollView.setLayoutParams(params);
                            return false;
                        } else if (params.height >= mMaxHeight) {
                            params.height = mMaxHeight;
                            mScrollView.setLayoutParams(params);
                            return false;
                        } else {
                            mScrollView.setLayoutParams(params);
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    return false;
            }

            return true;
        }
    }
}
