package com.zjrb.zjxw.detailproject.nomaldetail;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
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

import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.aliya.view.fitsys.FitWindowsRecyclerView;
import com.zjrb.coreprojectlibrary.api.callback.APIExpandCallBack;
import com.zjrb.coreprojectlibrary.common.base.BaseActivity;
import com.zjrb.coreprojectlibrary.common.base.adapter.OnItemClickListener;
import com.zjrb.coreprojectlibrary.common.base.toolbar.TopBarFactory;
import com.zjrb.coreprojectlibrary.common.biz.TouchSlopHelper;
import com.zjrb.coreprojectlibrary.common.listener.AbsAnimatorListener;
import com.zjrb.coreprojectlibrary.domain.base.BaseInnerData;
import com.zjrb.coreprojectlibrary.domain.base.ResultCode;
import com.zjrb.coreprojectlibrary.nav.Nav;
import com.zjrb.coreprojectlibrary.ui.UmengUtils.UmengShareBean;
import com.zjrb.coreprojectlibrary.ui.UmengUtils.UmengShareUtils;
import com.zjrb.coreprojectlibrary.ui.widget.load.LoadViewHolder;
import com.zjrb.coreprojectlibrary.utils.NetUtils;
import com.zjrb.coreprojectlibrary.utils.T;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.coreprojectlibrary.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.diver.NewsDetailSpaceDivider;
import com.zjrb.zjxw.detailproject.eventBus.CommentResultEvent;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
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
 * 普通详情页
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsDetailActivity extends BaseActivity implements TouchSlopHelper.OnTouchSlopListener,
        NewsDetailAdapter.CommonOptCallBack, View.OnClickListener, OnItemClickListener {
    @BindView(R2.id.iv_image)
    ImageView mIvImage;
    @BindView(R2.id.iv_type_video)
    ImageView mIvTypeVideo;
    @BindView(R2.id.video_container)
    FrameLayout mVideoContainer;
    @BindView(R2.id.rv_content)
    FitWindowsRecyclerView mRvContent;
    @BindView(R2.id.tv_comment)
    TextView mTvComment;
    @BindView(R2.id.fl_comment)
    FrameLayout mFlComment;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.menu_share)
    ImageView mMenuShare;
    @BindView(R2.id.floor_bar)
    FitWindowsFrameLayout mFloorBar;
    @BindView(R2.id.fl_content)
    FrameLayout mFlContent;

    public int mArticleId = -1;
    public int mlfId = -1;
    public String mVideoPath = "";
    /**
     * 上下滑动超出范围处理
     */
    private TouchSlopHelper mTouchSlopHelper;
    /**
     * 详情页数据
     */
    private DraftDetailBean mNewsDetail;

    /**
     * 详情页适配器
     */
    private NewsDetailAdapter mAdapter;
    //    private VideoManager mVideoManager;
//    private NewsDetailVideoHolder mVideoHolder;

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
        ButterKnife.bind(this);
        getIntentData(getIntent());
        init();
    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            if (intent.hasExtra(Key.ARTICLE_ID)) {
                mArticleId = Integer.parseInt(data.getQueryParameter(Key.ARTICLE_ID));
            }
            if (intent.hasExtra(Key.MLF_ID)) {
                mlfId = Integer.parseInt(data.getQueryParameter(Key.MLF_ID));
            }
            if (intent.hasExtra(Key.VIDEO_PATH)) {
                mVideoPath = data.getQueryParameter(Key.VIDEO_PATH);
            }
        }
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, "").getView();
    }

    /**
     * 初始化/拉取数据
     */
    private void init() {
        mTouchSlopHelper = new TouchSlopHelper();
        mTouchSlopHelper.setOnTouchSlopListener(this);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new NewsDetailSpaceDivider());
        if (!TextUtils.isEmpty(mVideoPath)) {
            initVideo();
        }

        loadData();
    }

    /**
     * 初始化视频
     */
    private void initVideo() {
        mVideoContainer.setVisibility(View.VISIBLE);
//        mVideoManager = VideoManager.get();
        if (!NetUtils.isAvailable()) {
            T.showShort(getContext(), "网络不可用");
        } else {
//            if (mNewsDetail == null) {
//                mVideoManager.play(mVideoContainer, mVideoPath);
//            } else {
//                mVideoManager.play(mVideoContainer, mNewsDetail.getWeb_link(),
//                        ExtraEntity.createBuild()
//                                .setTitle(mNewsDetail.getList_title())
//                                .setDuration(mNewsDetail.getVideo_duration())
//                                .setSize(mNewsDetail.getVideoSize())
//                                .setShareUrl(mNewsDetail.getUri_scheme())
//                                .setId(mArticleId)
//                                .setSummary(mNewsDetail.getSummary())
//                                .build()
//                );
//            }
        }
//        mVideoHolder = new NewsDetailVideoHolder(mVideoContainer);
    }

    /**
     * 初始化过度动画
     */
    private void initTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setTransitionName(mVideoContainer, Key.VIDEO_SHARED_VIEW_NAME);
        }
    }

    /**
     * 请求详情页数据
     */
    private void loadData() {
        if (mArticleId == -1) return;
        //TODO  TEST
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                fillData(draftDetailBean);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }

            @Override
            public void onAfter() {
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

    private void fillData(DraftDetailBean data) {

        mNewsDetail = data;

//        if (!data.isSucceed()) {
//            if (data.getResultCode() == ResultCode.DRAFT_NOT_EXIST) { //  文章已删除
//                mFlContent.setVisibility(View.GONE);
//                if (mVideoContainer.getParent() != null
//                        && mVideoContainer.getParent() instanceof ViewGroup) {
//                    ((ViewGroup) mVideoContainer.getParent()).removeView(mVideoContainer);
//                }
//
//                View notExistView = mVsNoExist.inflate();
//                UIUtils.dispatchApplyWindowInsets(notExistView);
//                notExistView.findViewById(R.id.fl_finish).setOnClickListener(this);
//            }
//            return;
//        }

//        if (data.getDoc_type() == DraftDetailBean.type.VIDEO) {
//            if (TextUtils.isEmpty(mVideoPath)) {
//                initVideo();
//            } else {
//                mVideoManager.setExtra(ExtraEntity.createBuild()
//                        .setTitle(mNewsDetail.getList_title())
//                        .setDuration(mNewsDetail.getVideo_duration())
//                        .setSize(mNewsDetail.getVideoSize())
//                        .setShareUrl(mNewsDetail.getUri_scheme())
//                        .setId((int) mNewsDetail.getId())
//                        .setSummary(mNewsDetail.getSummary())
//                        .build());
//            }
//            mVideoHolder.bind(data);
//        }


        List datas = new ArrayList<>();
        //TODO WLJ 添加5次数据
        datas.add(data);
        datas.add(data);
        datas.add(data);
        datas.add(data);
        datas.add(data);
        if (mAdapter != null) {
            mAdapter = new NewsDetailAdapter(datas,
                    mNewsDetail.getDoc_type() == DraftDetailBean.type.VIDEO, this);
            mAdapter.setOnItemClickListener(this);
        }
        mRvContent.setAdapter(mAdapter);
        mMenuPrised.setSelected(data.isLiked());
        if (data.getComment_count() <= 0) {
            mTvCommentsNum.setVisibility(View.GONE);
        } else {
            if (data.getComment_count() < 9999) {
                mTvCommentsNum.setText(data.getComment_count() + "");
            } else if (data.getComment_count() > 9999) {
                mTvCommentsNum.setText(BizUtils.numFormat(data.getComment_count(), 10000, 1) + "");
            }
        }
        BizUtils.setCommentSet(mTvComment, mNewsDetail.getComment_level());

//        if (data.getPoints() > 0) {
//            showShallowRead(data.getPoints());
//        }

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

    /**
     * 订阅
     */
    @Override
    public void onOptSubscribe() {
        new ColumnSubscribeTask(new APIExpandCallBack<BaseInnerData>() {

            @Override
            public void onSuccess(BaseInnerData baseInnerData) {
                switch (baseInnerData.getResultCode()) {
                    case ResultCode.SUCCEED:
                        T.showShort(getBaseContext(), "订阅成功");
                        mNewsDetail.setColumn_subscribed(true);
                        break;
                    default:
                        T.showShort(getBaseContext(), baseInnerData.getResultMsg());
                        break;
                }
            }

            @Override
            public void onAfter() {
                mAdapter.updateSubscribeInfo();
            }

        }).setTag(this).exe(mNewsDetail.getColumn_id());
    }

    @Override
    public void onOptPageFinished() { // WebView页面加载完毕
        mAdapter.showAll();
    }

    /**
     * 进入栏目列表页
     */
    @Override
    public void onOptClickColumn() {
//        ARouter.getInstance().build("/module/detail/ColumnDetailActivity")
//                .withString(Key.COLUMN_ID, mNewsDetail.getColumn_id().toString())
//                .navigation();
    }

    /**
     * 点赞操作
     */
    public void onOptFabulous() {
        // 点赞
        if (mNewsDetail.isLiked()) {
            T.showNow(this, "您已点赞", Toast.LENGTH_SHORT);
            return;
        }
        new DraftPraiseTask(new APIExpandCallBack<BaseInnerData>() {

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), "点赞失败");
            }

            @Override
            public void onSuccess(BaseInnerData baseInnerData) {
                switch (baseInnerData.getResultCode()) {
                    case ResultCode.SUCCEED:
                        T.showShort(getBaseContext(), "点赞成功");
                        if (mNewsDetail != null) {
                            mNewsDetail.setLiked(true);
                        }
                        break;
                    case ResultCode.FAILED:
                        T.showShort(getBaseContext(), "点赞失败");
                        break;
                    case ResultCode.HAS_PRAISED:
                        mNewsDetail.setLiked(false);
                        T.showShort(getBaseContext(), "您已点赞");
                        break;
                }
            }

            @Override
            public void onAfter() {
//                mAdapter.updateFabulousInfo();
            }
        }).setTag(this).exe(mArticleId);
    }


    @OnClick({R2.id.menu_comment, R2.id.menu_prised, R2.id.menu_share,
            R2.id.tv_comment})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.menu_comment) {
            if (mNewsDetail != null) {
                //进入评论列表页面
                Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/CommentActivity")
                        .buildUpon()
                        .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(mNewsDetail.getId()))
                        .appendQueryParameter(Key.MLF_ID, String.valueOf(mNewsDetail.getMlf_id()))
                        .appendQueryParameter(Key.COMMENT_SET, String.valueOf(mNewsDetail.getComment_level()))
                        .appendQueryParameter(Key.TITLE, mNewsDetail.getList_title())
                        .build(), 0);
            }

        } else if (view.getId() == R.id.menu_prised) {
            onOptFabulous();
        } else if (view.getId() == R.id.menu_share) {
            share();
        } else if (view.getId() == R.id.tv_comment) {
            if (mNewsDetail != null &&
                    BizUtils.isCanComment(this, mNewsDetail.getComment_level())) {
                //进入评论编辑页面(不针对某条评论)
                Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/CommentWindowActivity")
                        .buildUpon()
                        .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(mNewsDetail.getId()))
                        .appendQueryParameter(Key.MLF_ID, String.valueOf(mNewsDetail.getMlf_id()))
                        .build(), 0);
                return;
            }
        }
    }

    //友盟分享
    private UmengShareUtils shareUtils;

    // 分享方法
    private void share() {
        if (mNewsDetail == null) return;

        String title, content, logoUrl, targetUrl;

        title = mNewsDetail.getList_title();
//        content = TextUtils.isEmpty(mNewsDetail.getSummary()) ? C.SHARE_SLOGAN : mNewsDetail
//                .getSummary();
        if (TextUtils.isEmpty(mNewsDetail.getArticle_pic())) {
            logoUrl = "";//APIManager.endpoint.SHARE_24_LOGO_URL;
        } else {
            logoUrl = mNewsDetail.getArticle_pic();
        }
        targetUrl = mNewsDetail.getUrl();
        if (shareUtils == null)
            shareUtils = new UmengShareUtils();

        shareUtils.startShare(
                UmengShareBean.getInstance()
                        .setTitle(title)
//                        .setTextContent(content)
                        .setImgUri(logoUrl)
                        .setTargetUrl(targetUrl)
                        .setPlatform(null)
                ,
                this
        );
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
            mNewsDetail.setComment_count(mNewsDetail.getComment_count() + event.getData());
            mTvCommentsNum.setText(BizUtils.formatComments(mNewsDetail.getComment_count()));
        }
    }

    @Override
    public void onItemClick(View itemView, int position) {
    }
}


