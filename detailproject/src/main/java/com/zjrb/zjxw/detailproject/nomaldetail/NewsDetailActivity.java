package com.zjrb.zjxw.detailproject.nomaldetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.aliya.view.fitsys.FitWindowsRecyclerView;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
import com.zjrb.core.common.biz.TouchSlopHelper;
import com.zjrb.core.domain.base.BaseInnerData;
import com.zjrb.core.domain.base.ResultCode;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.widget.load.LoadViewHolder;
import com.zjrb.core.utils.NetUtils;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
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

import static com.zjrb.core.utils.UIUtils.getContext;


/**
 * 普通详情页
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsDetailActivity extends BaseActivity implements TouchSlopHelper.OnTouchSlopListener,
        NewsDetailAdapter.CommonOptCallBack, View.OnClickListener {

    /**
     * 稿件ID
     */
    public String mArticleId;
    //视频地址
    public String mVideoPath = "";
    @BindView(R2.id.iv_image)
    ImageView mIvImage;
    @BindView(R2.id.iv_type_video)
    ImageView mIvTypeVideo;
    @BindView(R2.id.video_container)
    FrameLayout mVideoContainer;
    @BindView(R2.id.rv_content)
    FitWindowsRecyclerView mRvContent;
    @BindView(R2.id.tv_comment)
    EditText mTvComment;
    @BindView(R2.id.fl_comment)
    FrameLayout mFlComment;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.menu_setting)
    ImageView mMenuSetting;
    @BindView(R2.id.floor_bar)
    FitWindowsFrameLayout mFloorBar;
    @BindView(R2.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R2.id.ly_container)
    LinearLayout mContainer;
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
            if (data.getQueryParameter(Key.ID) != null) {
                mArticleId = data.getQueryParameter(Key.ID);
            }
            if (data.getQueryParameter(Key.VIDEO_PATH) != null) {
                mVideoPath = data.getQueryParameter(Key.VIDEO_PATH);
            }
        }
    }


    /**
     * topbar
     */
    private DefaultTopBarHolder1 topHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = TopBarFactory.createDefault1(view, this);
        return topHolder.getView();
    }

    /**
     * 初始化/拉取数据
     */
    private void init() {
        topHolder.setViewVisible(topHolder.getShareView(), View.VISIBLE);
        mTouchSlopHelper = new TouchSlopHelper();
        mTouchSlopHelper.setOnTouchSlopListener(this);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        if (!TextUtils.isEmpty(mVideoPath)) {
            initVideo();
        }
        loadData();
    }

    /**
     * 初始化视频
     */
    private void initVideo() {
        mVideoContainer.setVisibility(View.GONE);
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
     * 请求详情页数据
     */
    private void loadData() {
        if (mArticleId == null || mArticleId.isEmpty()) return;
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                mNewsDetail = draftDetailBean;
                showEmptyNewsDetail();
//                fillData(draftDetailBean);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }
        }).setTag(this).exe(mArticleId);
    }

    @Override
    public LoadViewHolder replaceLoad() {
        if (TextUtils.isEmpty(mVideoPath)) {
            return super.replaceLoad();
        } else { // 视频详情页
            return replaceLoad(mFlContent);
        }
    }

    /**
     * @param data 填充详情页数据
     */
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
        //TODO WLJ 添加6次数据
        datas.add(data);
        datas.add(data);
        datas.add(data);
        //TODO  WLJ 添加相关专题
        datas.add(data);
//        if (data.getArticle().getRelated_subjects() != null && data.getArticle().getRelated_subjects().size() > 0) {
//            datas.add(data);
//        }
        datas.add(data);
//        if (data.getArticle().getRelated_news() != null && data.getArticle().getRelated_news().size() > 0) {
//            datas.add(data);
//
//        }
        datas.add(data);
//        if (data.getArticle().getHot_comments() != null && data.getArticle().getHot_comments().size() > 0) {
//            datas.add(data);
//        }
        mRvContent.setAdapter(mAdapter = new NewsDetailAdapter(datas,
                mNewsDetail.getArticle().getDoc_type() == DraftDetailBean.ArticleBean.type.VIDEO));
//        mAdapter.setOnItemClickListener(this);

        mMenuPrised.setSelected(data.getArticle().isLiked());
        if (data.getArticle().getComment_count() <= 0) {
            mTvCommentsNum.setVisibility(View.GONE);
        } else {
            if (data.getArticle().getComment_count() < 9999) {
                mTvCommentsNum.setText(data.getArticle().getComment_count() + "");
            } else if (data.getArticle().getComment_count() > 9999) {
                mTvCommentsNum.setText(BizUtils.numFormat(data.getArticle().getComment_count(), 10000, 1) + "");
            }
        }
        BizUtils.setCommentSet(mTvComment, mNewsDetail.getArticle().getComment_level());
    }

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    /**
     * @param isUp 控制底部floorBar
     */
    @Override
    public void onTouchSlop(boolean isUp) {
        int translationY = !isUp ? 0 : mFloorBar.getHeight() + getFloorBarMarginBottom();
        mFloorBar.animate().setInterpolator(mInterpolator)
                .setDuration(200)
                .translationY(translationY);
    }

    /**
     * @return 获取底部栏间距
     */
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
        //如果频道未订阅
        new ColumnSubscribeTask(new APIExpandCallBack<BaseInnerData>() {

            @Override
            public void onSuccess(BaseInnerData baseInnerData) {
                //TODO WLJ 错误码处理
                switch (baseInnerData.getResultCode()) {
                    case ResultCode.SUCCEED:
                        T.showShort(getBaseContext(), "订阅成功");
                        mNewsDetail.getArticle().setColumn_subscribed(true);
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

        }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), true);

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
        //TODO  WLJ 进入栏目列表
        Nav.with(UIUtils.getContext()).to(Uri.parse("http://www.8531.cn/subscription/detail")
                .buildUpon()
                .appendQueryParameter(Key.ID, String.valueOf(mNewsDetail.getArticle().getColumn_id()))
                .build(), 0);
    }

    /**
     * 进入频道详情页
     */
    @Override
    public void onOptClickChannel() {
        //TODO WLJ 频道详情页
    }

    /**
     * 点赞操作
     */
    public void onOptFabulous() {
        // 点赞
        if (mNewsDetail.getArticle().isLiked()) {
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
                //TODO WLJ 错误码处理
                switch (baseInnerData.getResultCode()) {
                    case ResultCode.SUCCEED:
                        T.showShort(getBaseContext(), "点赞成功");
                        if (mNewsDetail != null) {
                            mNewsDetail.getArticle().setLiked(true);
                        }
                        break;
                    case ResultCode.FAILED:
                        T.showShort(getBaseContext(), "点赞失败");
                        break;
                    case ResultCode.HAS_PRAISED:
                        mNewsDetail.getArticle().setLiked(false);
                        T.showShort(getBaseContext(), "您已点赞");
                        break;
                }
            }
        }).setTag(this).exe(mArticleId);
    }


    @OnClick({R2.id.menu_comment, R2.id.menu_prised, R2.id.menu_setting,
            R2.id.tv_comment})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.menu_comment) {
            if (mNewsDetail != null) {
                //进入评论列表页面
                Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/CommentActivity")
                        .buildUpon()
                        .appendQueryParameter(Key.ID, String.valueOf(mNewsDetail.getArticle().getId()))
                        .appendQueryParameter(Key.MLF_ID, String.valueOf(mNewsDetail.getArticle().getMlf_id()))
                        .appendQueryParameter(Key.COMMENT_SET, String.valueOf(mNewsDetail.getArticle().getComment_level()))
                        .appendQueryParameter(Key.TITLE, mNewsDetail.getArticle().getList_title())
                        .build(), 0);
            }

        } else if (view.getId() == R.id.menu_prised) {
            onOptFabulous();
        } else if (view.getId() == R.id.menu_setting) {
            //TODO  WLJ  打开设置按钮
        } else if (view.getId() == R.id.tv_comment) {
            if (mNewsDetail != null &&
                    BizUtils.isCanComment(this, mNewsDetail.getArticle().getComment_level())) {
                //进入评论编辑页面(不针对某条评论)
                Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/CommentWindowActivity")
                        .buildUpon()
                        .appendQueryParameter(Key.ID, String.valueOf(mNewsDetail.getArticle().getId()))
                        .appendQueryParameter(Key.MLF_ID, String.valueOf(mNewsDetail.getArticle().getMlf_id()))
                        .build(), 0);
                return;
            }
        } else if (view.getId() == R.id.iv_share) {
            T.showShortNow(NewsDetailActivity.this, "分享");
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
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(CommentResultEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        if (mNewsDetail != null && event.getData() > 0) {
            mNewsDetail.getArticle().setComment_count(mNewsDetail.getArticle().getComment_count() + event.getData());
            mTvCommentsNum.setText(BizUtils.formatComments(mNewsDetail.getArticle().getComment_count()));
        }
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mContainer.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ly_container, EmptyStateFragment.newInstance(String.valueOf(mNewsDetail.getArticle().getColumn_id()))).commit();
    }
}


