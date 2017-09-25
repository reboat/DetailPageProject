package com.zjrb.zjxw.detailproject.topic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.zjrb.core.common.biz.TouchSlopHelper;
import com.zjrb.core.domain.base.BaseInnerData;
import com.zjrb.core.domain.base.ResultCode;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.widget.load.LoadViewHolder;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.topic.adapter.ActivityTopicAdapter;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 话题详情页
 * Created by wanglinjie.
 * create time:2017/9/13  上午9:08
 */

public class ActivityTopicActivity extends BaseActivity implements TouchSlopHelper.OnTouchSlopListener,
        ActivityTopicAdapter.CommonOptCallBack {
    @BindView(R2.id.recyclerView)
    FitWindowsRecyclerView mRecyclerView;
    @BindView(R2.id.tv_comment)
    EditText mTvComment;
    @BindView(R2.id.fl_comment)
    FrameLayout mFlComment;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.menu_setting)
    ImageView mMenuSetting;
    @BindView(R2.id.floor_bar)
    FitWindowsFrameLayout mFloorBar;
    @BindView(R2.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.iv_share)
    ImageView mIvShare;
    @BindView(R2.id.v_toolbar_divider)
    View mVToolbarDivider;
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;
    @BindView(R2.id.tv_cover_title)
    TextView mTvCoverTitle;
    @BindView(R2.id.tv_host)
    TextView mTvHost;
    @BindView(R2.id.tv_guest)
    TextView mTvGuest;
    @BindView(R2.id.ll_fixed_title)
    LinearLayout mLlFixedTitle;

    private ActivityTopicAdapter adapter;
    /**
     * 上下滑动超出范围处理
     */
    private TouchSlopHelper mTouchSlopHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_topic_activity);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        initView();
        loadData();
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


    /**
     * 文章ID
     */
    private String mArticleId;
    /**
     * 详情页数据
     */
    private DraftDetailBean mNewsDetail;

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            if (data.getQueryParameter(Key.ID) != null) {
                mArticleId = data.getQueryParameter(Key.ID);
            }
        }
    }

    /**
     * head监听滚动 渐变
     */
    private void initView() {
        mTouchSlopHelper = new TouchSlopHelper();
        mTouchSlopHelper.setOnTouchSlopListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                HeadScrollChild();

            }
        });
    }

//    int mStatusBarHeight;
//    int[] mCurCoverTitleYAxis = new int[2];
//    int mLastCoverTitleYAxis;
//    private float mGradientRadio;
//    private boolean isInitCriticalCoverTitleY = false;
//    private int mCriticalCoverTitleY;
//    private int mGradientRange;
//    private int mGradientY;
//    private int mGradientAlpha;
//
//    private NewsActivityTopHolder mCoverVH;
//
//    private void HeadScrollChild() {
//        mStatusBarHeight = UIUtils.getStatusBarHeight();
//        if (mToolbar.getHeight() == 0) {
//            return;
//        }
//
//        if (mCoverVH == null) {
//            mCoverVH = (NewsActivityTopHolder) mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(0));
//        }
//
//        if (!isInitCriticalCoverTitleY) {
//            mCriticalCoverTitleY = mToolbar.getHeight() + mStatusBarHeight;
//            mGradientY = mToolbar.getHeight() + mCoverVH.mTvCoverTitle.getHeight() * 2 + mStatusBarHeight;
//            mGradientRange = mCoverVH.mTvCoverTitle.getHeight() * 2;
//            isInitCriticalCoverTitleY = true;
//        }
//
//        mCoverVH.mTvCoverTitle.getLocationInWindow(mCurCoverTitleYAxis);
//
//        if (mCurCoverTitleYAxis[1] < mLastCoverTitleYAxis) {//Up Scroll
//            //Cover Title Fixed Title
//            if (mCurCoverTitleYAxis[1] <= mCriticalCoverTitleY && mCriticalCoverTitleY <= mLastCoverTitleYAxis) {
//                mLlFixedTitle.setVisibility(View.VISIBLE);
//                mCoverVH.mTvCoverTitle.setVisibility(View.INVISIBLE);
//                mToolbar.setBackgroundColor(Color.argb(255, 255, 255, 255));
//                mVToolbarDivider.setVisibility(View.VISIBLE);
//            }
//
//            //Gradient
//            if (mCurCoverTitleYAxis[1] >= mCriticalCoverTitleY) {
//                if (mCurCoverTitleYAxis[1] < mGradientY) {
//                    mGradientRadio = (mGradientY - mCurCoverTitleYAxis[1]) * 1.0f / mGradientRange;
//                    mGradientAlpha = Math.round(mGradientRadio * 255.0f);
//                    mCoverVH.setCoverTxtColor(mGradientRadio);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        setStatusBarColor(getStatusColor(mGradientRadio));
//                    }
//                    mCoverVH.mFlCoverAlphaPlaceHolder.setVisibility(View.VISIBLE);
//                    mCoverVH.mFlCoverAlphaPlaceHolder.setBackgroundColor(Color.argb(mGradientAlpha, 255, 255, 255));
//
//                }
//            }
//        }
//
//        if (mCurCoverTitleYAxis[1] > mLastCoverTitleYAxis) {//Down Scroll
//            //Cover Title FixEd Title
//            if (mCurCoverTitleYAxis[1] >= mCriticalCoverTitleY && mLastCoverTitleYAxis <= mCriticalCoverTitleY) {
//                mLlFixedTitle.setVisibility(View.INVISIBLE);
//                mCoverVH.mTvCoverTitle.setVisibility(View.VISIBLE);
//                mToolbar.setBackgroundColor(Color.argb(0, 255, 255, 255));
//                mVToolbarDivider.setVisibility(View.INVISIBLE);
//            }
//            //Gradient
//            if (mCurCoverTitleYAxis[1] <= mGradientY) {
//                mGradientRadio = (mGradientY - mCurCoverTitleYAxis[1]) * 1.0f / mGradientRange;
//                mGradientAlpha = Math.round(mGradientRadio * 255.0f);
//                mCoverVH.setCoverTxtColor(mGradientRadio);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    setStatusBarColor(getStatusColor(mGradientRadio));
//                }
//                mCoverVH.mFlCoverAlphaPlaceHolder.setVisibility(View.VISIBLE);
//                mCoverVH.mFlCoverAlphaPlaceHolder.setBackgroundColor(Color.argb(mGradientAlpha, 255, 255, 255));
//            } else {
//                if (mGradientRadio > 0) {
//                    mGradientRadio = 0.0f;
//                    mCoverVH.setCoverTxtColor(mGradientRadio);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        setStatusBarColor(getStatusColor(mGradientRadio));
//                    }
//                    mCoverVH.mFlCoverAlphaPlaceHolder.setBackgroundColor(Color.argb(0, 255, 255, 255));
//                }
//            }
//        }
//
//        mLastCoverTitleYAxis = mCurCoverTitleYAxis[1];
//    }

    /**
     * 初始化/拉取数据
     */
    private void loadData() {
        if (mArticleId == null || mArticleId.isEmpty()) return;
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                mNewsDetail = draftDetailBean;
                fillData(draftDetailBean);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }
        }).setTag(this).exe(mArticleId);
    }

//    /**
//     * 状态栏颜色渐变
//     */
//    private boolean isInitStatusColor = false;
//    private int mStatusRedStart;
//    private int mStatusBlueStart;
//    private int mStatusGreenStart;
//    private int mStatusAlpha;
//    private int mStatusColorEnd;
//
//    public int getStatusColor(float radio) {
//        if (!isInitStatusColor) {
//            mStatusRedStart = Color.red(mStatusColorEnd);
//            mStatusBlueStart = Color.blue(mStatusColorEnd);
//            mStatusGreenStart = Color.green(mStatusColorEnd);
//            isInitStatusColor = true;
//        }
//        mStatusAlpha = (int) (((255) * radio + 0.5));
//        if (mStatusAlpha < 0) {
//            mStatusAlpha = 0;
//        }
//        if (mStatusAlpha > 255) {
//            mStatusAlpha = 255;
//        }
//        return Color.argb(mStatusAlpha, mStatusRedStart, mStatusGreenStart, mStatusBlueStart);
//    }

    /**
     * 填充数据
     *
     * @param data
     */
    private void fillData(DraftDetailBean data) {
        mNewsDetail = data;
        List datas = new ArrayList<>();
        //头
        datas.add(data);
        //webview
        datas.add(data);
        //订阅
        datas.add(data);
        //相关专题
        datas.add(data);
        //精选
        datas.add(data);
        //互动评论
        datas.add(data);
        mRecyclerView.setAdapter(adapter = new ActivityTopicAdapter(datas));

        mMenuPrised.setSelected(data.getArticle().isLiked());
        BizUtils.setCommentSet(mTvComment, mNewsDetail.getArticle().getComment_level());
    }

    @Override
    public LoadViewHolder replaceLoad() {
        return replaceLoad(mFlContent);
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

    @Override
    public void onOptSubscribe() {
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
                adapter.updateSubscribeInfo();
            }

        }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), true);

    }

    @Override
    public void onOptPageFinished() {
        adapter.showAll();
    }

    @Override
    public void onOptClickColumn() {
        Nav.with(UIUtils.getContext()).to(Uri.parse("http://www.8531.cn/subscription/detail")
                .buildUpon()
                .appendQueryParameter(Key.ID, String.valueOf(mNewsDetail.getArticle().getColumn_id()))
                .build(), 0);
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

    @OnClick({R2.id.menu_prised, R2.id.menu_setting,
            R2.id.tv_comment})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;

        if (view.getId() == R.id.menu_prised) {
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
            T.showShortNow(ActivityTopicActivity.this, "分享");
        }
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mFlContent.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ly_container, EmptyStateFragment.newInstance(String.valueOf(mNewsDetail.getArticle().getColumn_id()))).commit();
    }
}
