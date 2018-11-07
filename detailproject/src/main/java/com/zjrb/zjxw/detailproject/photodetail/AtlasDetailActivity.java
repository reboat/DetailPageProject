package com.zjrb.zjxw.detailproject.photodetail;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.api.callback.APICallBack;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.api.callback.LocationCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder3;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.common.permission.IPermissionCallBack;
import com.zjrb.core.common.permission.Permission;
import com.zjrb.core.common.permission.PermissionManager;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.OutSizeAnalyticsBean;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.anim.viewpager.DepthPageTransformer;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.ui.widget.load.AtlasLoad;
import com.zjrb.core.ui.widget.photoview.HackyViewPager;
import com.zjrb.core.utils.DownloadUtil;
import com.zjrb.core.utils.PathUtil;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.core.utils.webjs.BottomSaveDialogFragment;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.AlbumImageListBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.interFace.DetailWMHelperInterFace;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.photodetail.adapter.ImagePrePagerAdapter;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.task.RedBoatTask;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;

import static com.zjrb.core.utils.UIUtils.getContext;


/**
 * 图集详情页
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class AtlasDetailActivity extends BaseActivity implements ViewPager
        .OnPageChangeListener, View.OnTouchListener, LocationCallBack, DetailWMHelperInterFace.AtlasDetailWM {

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
    @BindView(R2.id.tv_comment)
    TextView mTvComment;
    @BindView(R2.id.ly_tip_contain)
    RelativeLayout mLyContainer;
    @BindView(R2.id.fl_comment)
    FrameLayout mFyContainer;
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


    /**
     * 稿件ID
     */
    public String mArticleId = "";

    private int mIndex;
    private List<AlbumImageListBean> mAtlasList;
    private DraftDetailBean mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setOverly(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_photo_detail);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        mFloorBar.setOnTouchListener(this);
        loadData();


        mScrollView.setOnTouchListener(new View.OnTouchListener() {
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
        });
    }

    private float y = 0;


    /**
     * topbar
     */
    private DefaultTopBarHolder3 topHolder;
    private TextView mTvTitleTop;
    private ImageView mIvDownLoad;
    private ImageView mIvShare;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = TopBarFactory.createDefault3(view, this);
        mTvTitleTop = topHolder.getTitleView();
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
                if (!TextUtils.isEmpty(data.getPath()) && data.getPath().contains("/red_boat_album.html")) {
                    isRedAlbum = true;
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
        mTvTitleTop.setVisibility(View.GONE);
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

    private Analytics.AnalyticsBuilder builder;

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
                }

                @Override
                public void onError(String errMsg, int errCode) {
                    //图集撤稿
                    if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                        showEmptyNewsDetail();
                    } else {
                        T.showShortNow(AtlasDetailActivity.this, errMsg);
                    }
                }
            }).setTag(this).bindLoadViewHolder(holder).exe(mArticleId, mFromChannel);
        } else {//红船号图集
            new RedBoatTask(new APIExpandCallBack<DraftDetailBean>() {
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

                }

                @Override
                public void onError(String errMsg, int errCode) {
                    //撤稿
                    if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                        showEmptyNewsDetail();
                    } else {
                        T.showShortNow(AtlasDetailActivity.this, errMsg);
                    }
                }
            }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(mArticleId, mFromChannel);
        }

    }

    private Analytics mAnalytics;

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
            builder = pageStayTime(data);
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
            mTvIndex.setText(String.valueOf(mIndex + 1) + "/");
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
        //评论数量 红船号图集不显示
        if (!TextUtils.isEmpty(data.getArticle().getComment_count_general()) && !isRedAlbum) {
            mTvCommentsNum.setVisibility(View.VISIBLE);
            mTvCommentsNum.setText(data.getArticle().getComment_count_general());
        } else {
            mTvCommentsNum.setVisibility(View.GONE);
        }

        //是否已点赞
        if (data.getArticle().isLike_enabled() && !isRedAlbum) {
            mMenuPrised.setVisibility(View.VISIBLE);
            mMenuPrised.setSelected(data.getArticle().isLiked());
        } else {
            mMenuPrised.setVisibility(View.GONE);
        }

        //禁止评论，隐藏评论框及评论按钮
        if (data.getArticle().getComment_level() == 0 || isRedAlbum) {
            mFyContainer.setVisibility(View.GONE);
            mMenuComment.setVisibility(View.GONE);
            mTvCommentsNum.setVisibility(View.GONE);
        } else {
            mFyContainer.setVisibility(View.VISIBLE);
            mMenuComment.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R2.id.iv_share, R2.id.tv_comment, R2.id.menu_comment, R2.id.menu_prised, R2.id
            .menu_setting, R2.id.iv_top_download})
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
                ClickBack(mData);
            }

            finish();
            //分享
        } else if (id == R.id.iv_share) {
            if (mData != null && mData.getArticle() != null && !TextUtils.isEmpty(mData.getArticle().getUrl())) {
                //分享专用bean
                OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                        .setObjectID(mData.getArticle().getMlf_id() + "")
                        .setObjectName(mData.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mData.getArticle().getChannel_id() + "")
                        .setClassifyName(mData.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mData.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfobjectID(mData.getArticle().getId() + "");

                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
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
        } else if (id == R.id.tv_comment) {
            if (mData != null && mData.getArticle() != null) {
                ClickCommentBox(mData);

                //评论发表成功
                Analytics analytics = new Analytics.AnalyticsBuilder(getActivity(), "A0023", "A0023", "Comment", false)
                        .setEvenName("发表评论，且发送成功")
                        .setObjectID(mData.getArticle().getMlf_id() + "")
                        .setObjectName(mData.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mData.getArticle().getChannel_id())
                        .setClassifyName(mData.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mData.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(mData.getArticle().getId() + "").newsID(mData.getArticle().getMlf_id() + "")
                        .selfNewsID(mData.getArticle().getId() + "")
                        .newsTitle(mData.getArticle().getDoc_title())
                        .selfChannelID(mData.getArticle().getChannel_id())
                        .channelName(mData.getArticle().getChannel_name())
                        .pageType("新闻详情页")
                        .commentType("文章")
                        .build();

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
                ClickPriseIcon(mData);
            }
            fabulous();
            //设置
        } else if (id == R.id.menu_setting) {
            if (mData != null && mData.getArticle() != null) {
                ClickMoreIcon(mData);
                MoreDialog.newInstance(mData).show(getSupportFragmentManager(), "MoreDialog");
            }
            //下载
        } else if (id == R.id.iv_top_download) {
            if (mData != null && mData.getArticle() != null) {
                ClickDownLoad(mData);
            }
            loadImage(mIndex);
        }
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

        if (mData != null && mData.getArticle() != null) {
            AtlasSlide(mData);
        }

    }

    /**
     * @param position 设置顶部topbar标题
     */
    private void setTopTitle(int position) {
        if (mAtlasList != null && !mAtlasList.isEmpty()) {
            if (position == (mAtlasList.size() - 1)) {
                mTvTitleTop.setVisibility(View.VISIBLE);
                mTvTitleTop.setTextColor(getResources().getColor(R.color.tc_ffffff));
                mTvTitleTop.setText(getString(R.string.module_detail_more_image));
                setTopBarInOut(View.GONE);
                if (mData != null && mData.getArticle() != null) {
                    ClickMoreImage(mData);
                }


            } else {
                mTvTitleTop.setVisibility(View.GONE);
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
            T.showNow(this, getString(R.string.module_detail_you_have_liked), Toast.LENGTH_SHORT);
            return;
        }
        new DraftPraiseTask(new APICallBack<Void>() {
            @Override
            public void onError(String errMsg, int errCode) {
                if (errCode == 50013) {
                    mData.getArticle().setLiked(true);
                    mMenuPrised.setSelected(true);
                    T.showShort(UIUtils.getContext(), "已点赞成功");
                } else {
                    T.showShort(UIUtils.getContext(), errMsg);
                }
            }

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(UIUtils.getContext(), getString(R.string.module_detail_prise_success));
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
                        T.showShort(AtlasDetailActivity.this, getString(R.string
                                .module_detail_save_success));
                    }

                    @Override
                    public void onFail(String err) {
                        T.showShort(AtlasDetailActivity.this, getString(R.string
                                .module_detail_save_failed));
                    }
                })
                .download(url);
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
        if (mData != null && mData.getArticle() != null) {
            if (builder != null) {
                builder.setPercentage(mReadingScale + "");
                builder.readPercent(mReadingScale + "");
                mAnalytics = builder.build();
                if (mAnalytics != null) {
                    mAnalytics.sendWithDuration();
                }
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

    @Override
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

    @Override
    public void ClickBack(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(getActivity(), "800001", "800001", "AppTabClick", false)
                .setEvenName("点击返回")
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
                .setSelfObjectID(bean.getArticle().getId() + "").pageType("新闻详情页").clickTabName("返回")
                .build()
                .send();
    }

    @Override
    public void ClickCommentBox(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(getActivity(), "800002", "800002", "AppTabClick", false)
                .setEvenName("点击评论输入框")
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
                .setSelfObjectID(bean.getArticle().getId() + "").pageType("新闻详情页").clickTabName("评论输入框")
                .build()
                .send();

    }

    @Override
    public void ClickPriseIcon(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(getActivity(), "A0021", "A0021", "Support", false)
                .setEvenName("点击点赞")
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
                .supportType("文章")
                .build()
                .send();
    }

    @Override
    public void ClickMoreIcon(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(getActivity(), "800005", "800005", "AppTabClick", false)
                .setEvenName("点击更多")
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
                .setSelfObjectID(bean.getArticle().getId() + "").pageType("新闻详情页")
                .clickTabName("更多")
                .build()
                .send();
    }

    @Override
    public void ClickDownLoad(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(this, "A0025", "A0025", "PictureRelatedOperation", false)
                .setEvenName("点击下载按钮")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("图集详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("图集详情页")
                .operationType("点击图片下载")
                .build()
                .send();
    }

    @Override
    public void AtlasSlide(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(this, "A0010", "A0010", "PictureRelatedOperation", false)
                .setEvenName("图片浏览(左右滑动)")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("图集详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("图集详情页")
                .operationType("图片浏览(左右滑动)")
                .build()
                .send();
    }

    @Override
    public void ClickMoreImage(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(this, "800011", "800011", "AppContentClick", false)
                .setEvenName("打开更多图集页面)")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("更多图集页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("图集列表")
                .objectType("图集新闻列表")
                .pubUrl(bean.getArticle().getUrl())
                .build()
                .send();
    }
}
