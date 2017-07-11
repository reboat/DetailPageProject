package com.zjrb.zjxw.detailproject;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zjrb.coreprojectlibrary.api.APIManager;
import com.zjrb.coreprojectlibrary.api.callback.APICallBack;
import com.zjrb.coreprojectlibrary.api.callback.APIExpandCallBack;
import com.zjrb.coreprojectlibrary.common.base.BaseActivity;
import com.zjrb.coreprojectlibrary.common.biz.TouchSlopHelper;
import com.zjrb.coreprojectlibrary.common.global.C;
import com.zjrb.coreprojectlibrary.common.global.IKey;
import com.zjrb.coreprojectlibrary.common.listener.AbsAnimatorListener;
import com.zjrb.coreprojectlibrary.common.listener.IOnItemClickListener;
import com.zjrb.coreprojectlibrary.db.IntentHelper;
import com.zjrb.coreprojectlibrary.domain.base.BaseInnerData;
import com.zjrb.coreprojectlibrary.domain.base.ResultCode;
import com.zjrb.coreprojectlibrary.ui.UmengUtils.UmengShareBean;
import com.zjrb.coreprojectlibrary.ui.UmengUtils.UmengShareUtils;
import com.zjrb.coreprojectlibrary.ui.widget.fitsys.FitSysWinFrameLayout;
import com.zjrb.coreprojectlibrary.ui.widget.fitsys.FitSysWinRecyclerView;
import com.zjrb.coreprojectlibrary.ui.widget.load.LoadViewHolder;
import com.zjrb.coreprojectlibrary.utils.NetUtils;
import com.zjrb.coreprojectlibrary.utils.T;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.coreprojectlibrary.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.holder.NewsDetailVideoHolder;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zjrb.coreprojectlibrary.utils.UIUtils.getContext;

/**
 * 新闻、日子、资讯详情 - 页面
 *
 * @author a_liYa
 */
public class NewsDetailActivity extends BaseActivity implements TouchSlopHelper.OnTouchSlopListener,
        NewsDetailAdapter.CommonOptCallBack, View.OnClickListener, IOnItemClickListener<Object> {


    @BindView(R2.id.video_container)
    FrameLayout mVideoContainer;
    @BindView(R2.id.rv_content)
    FitSysWinRecyclerView mRvContent;
    @BindView(R2.id.tv_comment)
    TextView mTvComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.menu_collected)
    ImageView mMenuCollected;
    @BindView(R2.id.floor_bar)
    FitSysWinFrameLayout mFloorBar;
    @BindView(R2.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R2.id.vs_no_exist)
    private int mlfId;
    private String mVideoPath;

    /**
     * 上下滑动超出范围处理
     */
    private TouchSlopHelper mTouchSlopHelper;
    private DraftDetailBean mNewsDetail;

    private NewsDetailAdapter mAdapter;
    private VideoManager mVideoManager;
    private NewsDetailVideoHolder mVideoHolder;

    public static Intent getIntent(int articleId, int mlfId) {
        return IntentHelper.get(NewsDetailActivity.class)
                .put(IKey.ARTICLE_ID, articleId)
                .put(IKey.MLF_ID, mlfId)
                .intent();
    }

    public static Intent getIntent(int articleId, String videoPath, int mlfId) {
        return IntentHelper.get(NewsDetailActivity.class)
                .put(IKey.ARTICLE_ID, articleId)
                .put(IKey.VIDEO_PATH, videoPath)
                .put(IKey.MLF_ID, mlfId)
                .intent();
    }

    @Override
    public boolean isShowToolBar() {
        return false;
    }

    /**
     * 处理上下移动监听
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mTouchSlopHelper != null)
            mTouchSlopHelper.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucenceStatusBarBg();
        setContentView(R.layout.module_detail_activity_detail);
        ButterKnife.bind(this, View.inflate(getContext(), R.layout.module_detail_activity_detail, null));

        initSavedState(savedInstanceState);

        init();

    }

    private void init() {
        UIUtils.dispatchApplyWindowInsets(mFloorBar);
        UIUtils.dispatchApplyWindowInsets(mRvContent);
        mTouchSlopHelper = new TouchSlopHelper();
        mTouchSlopHelper.setOnTouchSlopListener(this);
        if (!TextUtils.isEmpty(mVideoPath)) {
            initVideo();
        }

        loadData();
    }

    private void initVideo() {
        mVideoContainer.setVisibility(View.VISIBLE);
        mVideoManager = VideoManager.get();
        if (!NetUtils.isAvailable()) {
            T.showShort(getContext(), "网络不可用");
        } else {
            if (mNewsDetail == null) {
                mVideoManager.play(mVideoContainer, mVideoPath);
            } else {
                mVideoManager.play(mVideoContainer, mNewsDetail.getLinkUrl(),
                        ExtraEntity.createBuild()
                                .setTitle(mNewsDetail.getTitle())
                                .setDuration(mNewsDetail.getVideoDuration())
                                .setSize(mNewsDetail.getVideoSize())
                                .setShareUrl(mNewsDetail.getShareUrl())
                                .setId(mArticleId)
                                .setSummary(mNewsDetail.getSummary())
                                .build()
                );
            }
        }
        mVideoHolder = new NewsDetailVideoHolder(mVideoContainer);
    }

    /**
     * 初始化过度动画
     */
    private void initTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setTransitionName(mVideoContainer, C.VIDEO_SHARED_VIEW_NAME);
        }
    }

    private void loadData() {
        if (mArticleId == -1) return;

        new DraftDetailTask(new APICallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                fillData(draftDetailBean);
            }

        }).setTag(this).bindLoadViewHolder(replaceLoad()).exe(mArticleId);
    }

    @Override
    public LoadViewHolder replaceLoad() {
        if (TextUtils.isEmpty(mVideoPath)) {
            return super.replaceLoad();
        } else { // 视频详情页
            return replaceLoad(mFlContent);
        }
    }

    private void initSavedState(Bundle savedState) {
        if (savedState == null) {
            mArticleId = getIntent().getIntExtra(IKey.ARTICLE_ID, -1);
            mlfId = getIntent().getIntExtra(IKey.MLF_ID, -1);
            mVideoPath = getIntent().getStringExtra(IKey.VIDEO_PATH);
        } else {
            mArticleId = savedState.getInt(IKey.ARTICLE_ID);
            mlfId = savedState.getInt(IKey.MLF_ID);
            mVideoPath = savedState.getString(IKey.VIDEO_PATH);
        }
    }

    private void fillData(DraftDetailBean data) {

        mNewsDetail = data;

        if (!data.isSucceed()) {
            if (data.getResultCode() == ResultCode.DRAFT_NOT_EXIST) { //  文章已删除
                mFlContent.setVisibility(View.GONE);
                if (mVideoContainer.getParent() != null
                        && mVideoContainer.getParent() instanceof ViewGroup) {
                    ((ViewGroup) mVideoContainer.getParent()).removeView(mVideoContainer);
                }

                View notExistView = mVsNoExist.inflate();
                UIUtils.dispatchApplyWindowInsets(notExistView);
                notExistView.findViewById(R.id.fl_finish).setOnClickListener(this);
            }
            return;
        }

        if (data.getDocType() == ArticleItemBean.type.VIDEO) {
            if (TextUtils.isEmpty(mVideoPath)) {
                initVideo();
            } else {
                mVideoManager.setExtra(ExtraEntity.createBuild()
                        .setTitle(mNewsDetail.getTitle())
                        .setDuration(mNewsDetail.getVideoDuration())
                        .setSize(mNewsDetail.getVideoSize())
                        .setShareUrl(mNewsDetail.getShareUrl())
                        .setId((int) mNewsDetail.getId())
                        .setSummary(mNewsDetail.getSummary())
                        .build());
            }
            mVideoHolder.bind(data);
        }


        List datas = new ArrayList<>();
        datas.add(data);
        datas.add(data);
        mRvContent.setAdapter(mAdapter = new NewsDetailAdapter(datas,
                mNewsDetail.getDocType() == ArticleItemBean.type.VIDEO));
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickListener(this);
        mRvContent.addItemDecoration(new NewsDetailSpaceDivider());

        // mFloorBar
        mMenuCollected.setSelected(data.isCollected());
        mTvCommentsNum.setText(BizUtils.formatComments(data.getCommentNum()));

        BizUtils.setCommentSet(mTvComment, mNewsDetail.getCommentSet());

        if (data.getPoints() > 0) {
            showShallowRead(data.getPoints());
        }

    }

    // 浅读领取积分TextView
    private TextView mTvShallow;

    /**
     * 展示获取浅读积分
     */
    private void showShallowRead(int integral) {

        if (isDestroyed()) {
            return;
        }

        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        //获取的是LocalWindowManager对象
        WindowManager windowManager = getWindowManager();
        wmParams.gravity = Gravity.TOP;
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = UIUtils.dip2px(30);

        mTvShallow = (TextView) getLayoutInflater().inflate(R.layout
                .module_detail_layout_integral_float, null);
        windowManager.addView(mTvShallow, wmParams);
        BizUtils.setGetIntegralText(mTvShallow, integral);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                perShallowAnim();
            }
        });
    }

    /**
     * 执行浅读积分动画
     */
    private void perShallowAnim() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int radius = mTvShallow.getWidth() / 2;
            Animator showAnimator = ViewAnimationUtils.createCircularReveal(
                    mTvShallow, mTvShallow.getWidth() / 2, 0,
                    0, radius);
            showAnimator.setDuration(300);
            Animator fadeAnimator = ViewAnimationUtils.createCircularReveal(
                    mTvShallow, mTvShallow.getWidth() / 2, 0,
                    radius, 0);
            fadeAnimator.setDuration(300);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(fadeAnimator).after(3000).after(showAnimator);
            animatorSet.addListener(new AbsAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    try {
                        mTvShallow.setVisibility(View.GONE);
                        getWindowManager().removeView(mTvShallow);
                        mTvShallow = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            animatorSet.start();
        } else {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet
                    .play(ObjectAnimator.ofFloat(mTvShallow, View.TRANSLATION_Y, 0,
                            -mTvShallow.getHeight()).setDuration(300))
                    .after(3000)
                    .after(ObjectAnimator.ofFloat(mTvShallow, View.TRANSLATION_Y,
                            -mTvShallow.getHeight(), 0).setDuration(300));
            animatorSet.addListener(new AbsAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    try {
                        mTvShallow.setVisibility(View.GONE);
                        getWindowManager().removeView(mTvShallow);
                        mTvShallow = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            animatorSet.start();
        }
    }

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    @Override
    public void onTouchSlop(boolean isUp) { // 控制底部floorBar
        int translationY = !isUp ? 0 : mFloorBar.getHeight() + getFloorBarMarginBottom();
        mFloorBar.animate().setInterpolator(mInterpolator)
                .setDuration(200)
                .translationY(translationY);
    }

    private int getFloorBarMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = mFloorBar.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

    @Override
    public void onOptCancelSubscribe() {
        new ColumnCancelSubscribeTask(new APIExpandCallBack<BaseInnerData>() {

            @Override
            public void onSuccess(BaseInnerData baseInnerData) {
                switch (baseInnerData.getResultCode()) {
                    case ResultCode.SUCCEED:
                        showShortToast("取消订阅成功");
                        mNewsDetail.setSubscribed(0);
                        break;
                    default:
                        showShortToast(baseInnerData.getResultMsg());
                        break;
                }
            }

            @Override
            public void onAfter() {
                mAdapter.updateSubscribeInfo();
            }

        }).setTag(this).exe(mNewsDetail.getColumnId());
    }

    @Override
    public void onOptSubscribe() {
        new ColumnSubscribeTask(new APIExpandCallBack<BaseInnerData>() {

            @Override
            public void onSuccess(BaseInnerData baseInnerData) {
                switch (baseInnerData.getResultCode()) {
                    case ResultCode.SUCCEED:
                        showShortToast("订阅成功");
                        mNewsDetail.setSubscribed(1);
                        break;
                    default:
                        showShortToast(baseInnerData.getResultMsg());
                        break;
                }
            }

            @Override
            public void onAfter() {
                mAdapter.updateSubscribeInfo();
            }

        }).setTag(this).exe(mNewsDetail.getColumnId());
    }

    @Override
    public void onOptPageFinished() { // WebView页面加载完毕
        mAdapter.showAll();
    }

    @Override
    public void onOptClickColumn() {
        startActivity(IntentHelper.get(ColumnDetailActivity.class)
                .put(IKey.COLUMN_ID, String.valueOf(mNewsDetail.getColumnId()))
                .intent());
    }

    @Override
    public void onOptFabulous() {
        // 点赞
        if (mNewsDetail.isPraised()) {
            T.showNow(this, "您已点赞", Toast.LENGTH_SHORT);
            return;
        }
        new DraftPraiseTask(new APIExpandCallBack<BaseInnerData>() {

            @Override
            public void onError(String errMsg, int errCode) {
                showShortToast("点赞失败");
            }

            @Override
            public void onSuccess(BaseInnerData baseInnerData) {
                switch (baseInnerData.getResultCode()) {
                    case ResultCode.SUCCEED:
                        showShortToast("点赞成功");
                        if (mNewsDetail != null) {
                            mNewsDetail.setPraised(1);
                        }
                        break;
                    case ResultCode.FAILED:
                        showShortToast("点赞失败");
                        break;
                    case ResultCode.HAS_PRAISED:
                        mNewsDetail.setPraised(1);
                        showShortToast("您已点赞");
                        break;
                }
            }

            @Override
            public void onAfter() {
                mAdapter.updateFabulousInfo();
            }
        }).setTag(this).exe(mArticleId);
    }


    @OnClick({R2.id.btn_back, R2.id.menu_comment, R2.id.menu_collected, R2.id.menu_share,
            R2.id.tv_comment})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        switch (view.getId()) {
            case R2.id.btn_back:
                finish();
                break;
            case R2.id.menu_comment:
                if (mNewsDetail != null) {
                    startActivity(CommentActivity
                            .newIntent(mArticleId, 0, mNewsDetail.getCommentSet(), mlfId));
                }
                break;
            case R2.id.menu_collected:
                collected();
                break;
            case R2.id.menu_share:
                share();
                break;
            case R2.id.tv_comment:
                if (mNewsDetail != null &&
                        BizUtils.isCanComment(this, mNewsDetail.getCommentSet())) {
                    startActivity(CommentWindowActivity.getIntent(mNewsDetail.getColumnId(),
                            false, mlfId));
                    overridePendingTransition(0, 0); // 关闭动画
                    return;
                }
                break;
            case R2.id.fl_finish: // 稿件失效
                finish();
                break;
        }
    }

    //友盟分享
    private UmengShareUtils shareUtils;

    // 分享方法
    private void share() {
        if (mNewsDetail == null) return;

        String title, content, logoUrl, targetUrl;

        title = mNewsDetail.getTitle();
        content = TextUtils.isEmpty(mNewsDetail.getSummary()) ? C.SHARE_SLOGAN : mNewsDetail
                .getSummary();
        if (TextUtils.isEmpty(mNewsDetail.getTitleBackgroundImage())) {
            logoUrl = APIManager.endpoint.SHARE_24_LOGO_URL;
        } else {
            logoUrl = mNewsDetail.getTitleBackgroundImage();
        }
        targetUrl = mNewsDetail.getShareUrl();
        if (shareUtils == null)
            shareUtils = new UmengShareUtils();

        shareUtils.startShare(
                UmengShareBean.getInstance()
                        .setArticleId(mArticleId)
                        .setTitle(title)
                        .setTextContent(content)
                        .setImgUri(logoUrl)
                        .setTargetUrl(targetUrl)
                        .setPlatform(null)
                        .setColumnNum(3),
                this
        );
    }

    // 收藏
    private void collected() {
        if (mNewsDetail == null) return;
        if (mNewsDetail.isCollected()) {
            new CollectionCancelTask(new APICallBack<BaseInnerData>() {
                @Override
                public void onSuccess(BaseInnerData baseInnerData) {
                    if (baseInnerData.isSucceed()) {
                        showShortToast("取消收藏成功");
                        mNewsDetail.setCollected(0);
                        mMenuCollected.setSelected(false);
                        // 处理收藏状态切换动画
                        BizUtils.switchSelectorAnim(mMenuCollected, false);
                    } else {
                        mMenuCollected.setSelected(false);
                    }
                }

                @Override
                public void onError(String errMsg, int errCode) {
                    showShortToast("取消收藏失败");
                }
            }).setTag(this).exe(mNewsDetail.getId());
        } else {
            new CollectionArticleTask(new APICallBack<BaseInnerData>() {
                @Override
                public void onSuccess(BaseInnerData baseInnerData) {
                    if (baseInnerData.isSucceed()) {
                        showShortToast("收藏成功");
                        mNewsDetail.setCollected(1);
                        mMenuCollected.setSelected(true);
                    } else {
                        mMenuCollected.setSelected(true);
                    }
                }

                @Override
                public void onError(String errMsg, int errCode) {
                    showShortToast("收藏失败");
                }
            }).setTag(this).exe(mNewsDetail.getId());
        }
    }


    private long startActivityTime;
    //记录进入页面的时间
    private long inTime = System.currentTimeMillis();

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.onWebViewResume();
        }
        startActivityTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.onWebViewPause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (shareUtils != null) {
            shareUtils.initResult(requestCode, resultCode, data);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(CommentResultEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        if (mNewsDetail != null && event.getData() > 0) {
            mNewsDetail.setCommentNum(mNewsDetail.getCommentNum() + event.getData());
            mTvCommentsNum.setText(BizUtils.formatComments(mNewsDetail.getCommentNum()));
        }
    }

    @Override
    public void onItemClick(View view, int position, Object data) {
        if (data instanceof ArticleItemBean) {
            BizUtils.articleItemClickJump(getSelf(), (ArticleItemBean) data);
        }
    }

}


