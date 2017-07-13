package com.zjrb.zjxw.detailproject.photodetail;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.zjrb.coreprojectlibrary.api.APIManager;
import com.zjrb.coreprojectlibrary.api.callback.APICallBack;
import com.zjrb.coreprojectlibrary.api.callback.APIExpandCallBack;
import com.zjrb.coreprojectlibrary.common.base.BaseActivity;
import com.zjrb.coreprojectlibrary.common.global.IKey;
import com.zjrb.coreprojectlibrary.common.listener.IOnImageTapListener;
import com.zjrb.coreprojectlibrary.db.IntentHelper;
import com.zjrb.coreprojectlibrary.domain.base.BaseInnerData;
import com.zjrb.coreprojectlibrary.domain.base.ResultCode;
import com.zjrb.coreprojectlibrary.ui.UmengUtils.UmengShareBean;
import com.zjrb.coreprojectlibrary.ui.UmengUtils.UmengShareUtils;
import com.zjrb.coreprojectlibrary.ui.anim.viewpager.DepthPageTransformer;
import com.zjrb.coreprojectlibrary.ui.widget.AtlasIndexView;
import com.zjrb.coreprojectlibrary.ui.widget.photoview.HackyViewPager;
import com.zjrb.coreprojectlibrary.utils.T;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.coreprojectlibrary.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.comment.CommentActivity;
import com.zjrb.zjxw.detailproject.comment.CommentWindowActivity;
import com.zjrb.zjxw.detailproject.photodetail.adapter.ImagePrePagerAdapter;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图集详情页
 *
 * @author a_liYa
 * @date 16/11/2 下午2:15.
 */
public class AtlasDetailActivity extends BaseActivity implements IOnImageTapListener, ViewPager
        .OnPageChangeListener, View.OnTouchListener {

    @BindView(R2.id.view_pager)
    HackyViewPager mViewPager;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_content)
    TextView mTvContent;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.floor_bar)
    RelativeLayout mFloorBar;
    @BindView(R2.id.index_view)
    AtlasIndexView mIndexView;
    @BindView(R2.id.container_bottom)
    LinearLayout mContainerBottom;
    @BindView(R2.id.menu_collected)
    ImageView mMenuCollected;
    @BindView(R2.id.tv_comment)
    TextView mTvComment;
    @BindView(R2.id.fit_bottom_layout)
    FrameLayout mFitBottomLayout;
    @BindView(R2.id.fit_top_layout)
    FrameLayout mFitTopLayout;
    @BindView(R2.id.container_top)
    LinearLayout mContainerTop;
    @BindView(R2.id.tv_atlas_top_title)
    TextView tv_top_title;
    @BindView(R2.id.tv_atlas_top_subscribe)
    TextView tvAtlasTopSubscribe;
    @BindView(R2.id.tv_tag_original)
    TextView tvTagOriginal;

    private int mArticleId;
    private int mlfId;
    private int mIndex;
    private List<AtlasEntity> mAtlasList;
    private DraftDetailBean mData;
    private DraftDetailBean mAtlasEntity;
    public static final int REQUEST_CODE = 0x001;

    public static Intent getIntent(int articleId, int mlfId) {
        return IntentHelper.get(AtlasDetailActivity.class)
                .put(IKey.ARTICLE_ID, articleId)
                .put(IKey.MLF_ID, mlfId)
                .intent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSaveState(savedInstanceState);
        setContentView(R.layout.module_detail_photo_detail);
        ButterKnife.bind(this);
        mFloorBar.setOnTouchListener(this);
        UIUtils.dispatchApplyWindowInsets(mFitBottomLayout);
        UIUtils.dispatchApplyWindowInsets(mFitTopLayout);
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        loadData();
    }

    private void initSaveState(Bundle savedState) {
        if (savedState == null) {
            mArticleId = getIntent().getIntExtra(IKey.ARTICLE_ID, -1);
            mlfId = getIntent().getIntExtra(IKey.MLF_ID, -1);
        } else {
            mArticleId = savedState.getInt(IKey.ARTICLE_ID);
            mlfId = savedState.getInt(IKey.MLF_ID);
        }
    }

    private void loadData() {

        new AtlasDetailTask(new APICallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean atlasDetailEntity) {
                mAtlasEntity = atlasDetailEntity;
                fillData(atlasDetailEntity);
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad()).exe(mArticleId);

    }

    private void fillData(DraftDetailBean atlasDetailEntity) {
        mData = atlasDetailEntity;
        mAtlasList = atlasDetailEntity.getAttachInfo();
        tvTagOriginal.setVisibility(atlasDetailEntity.isOriginal() ? View.VISIBLE : View.GONE);
        mTvCommentsNum.setText(BizUtils.formatComments(atlasDetailEntity.getCommentNum()));
        mMenuCollected.setSelected(atlasDetailEntity.isCollected());
        tv_top_title.setText(atlasDetailEntity.getColumnName());
        if (!atlasDetailEntity.isSubscribed()) {
            tvAtlasTopSubscribe.setEnabled(true);
            tvAtlasTopSubscribe.setText("订阅");
        } else {
            tvAtlasTopSubscribe.setEnabled(false);
            tvAtlasTopSubscribe.setText("已订阅");
        }
        BizUtils.setCommentSet(mTvComment, mData.getCommentSet());
        if (mAtlasList != null && !mAtlasList.isEmpty()) {
            List<String> imgs = new ArrayList<>(mAtlasList.size());
            for (AtlasEntity entity : mAtlasList) {
                imgs.add(entity.getFilePath());
            }

            mViewPager.addOnPageChangeListener(this);
            mViewPager.setPageTransformer(true, new DepthPageTransformer());

            mViewPager.setAdapter(new ImagePrePagerAdapter(getSupportFragmentManager(), imgs));

            mIndexView.setIndexNum(String.valueOf(mIndex + 1));
            mIndexView.setTotalNum(String.valueOf(mAtlasList.size()));
            mTvTitle.setText(atlasDetailEntity.getTitle());
            AtlasEntity entity = mAtlasList.get(mIndex);
            mTvContent.setText(entity.getDescription());
        }

    }

    @Override
    public boolean isShowToolBar() {
        return false;
    }

    @OnClick({R2.id.tv_atlas_top_title, R2.id.tv_atlas_top_subscribe, R2.id.iv_back, R2.id.tv_comment, R2.id.menu_comment, R2.id.menu_collected, R2.id
            .menu_share})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        switch (view.getId()) {
            case R2.id.tv_atlas_top_subscribe:
                subscribeColumn();
                break;
            case R2.id.iv_back:
                finish();
                break;
            case R2.id.tv_comment: // 开启评论窗口
                if (mData != null && BizUtils.isCanComment(this, mData.getCommentSet())) {
                    startActivity(CommentWindowActivity.getIntent(mArticleId, false, mlfId));
                    overridePendingTransition(0, 0); // 关闭动画
                    return;
                }
                break;
            case R2.id.menu_comment: // mFloorBar - 查看评论
                if (mData != null) {
                    startActivity(CommentActivity.newIntent(mArticleId, 0, mData.getCommentSet(), mlfId));
                }
                break;
            case R2.id.menu_collected: // mFloorBar - 收藏
                collected();
                break;
            case R2.id.menu_share: // mFloorBar - 分享
                share();
                break;
            case R2.id.tv_atlas_top_title:
                if (null != mData) {
                    Intent intent = new Intent(getSelf(), ColumnDetailActivity.class);
                    intent.putExtra(IKey.COLUMN_ID, String.valueOf(mData.getColumnId()));
                    startActivityForResult(intent, REQUEST_CODE);
                }
                break;
        }
    }

    /**
     * 订阅栏目
     */
    private void subscribeColumn() {
        if (mAtlasEntity == null) return;
        new ColumnSubscribeTask(new APIExpandCallBack<BaseInnerData>() {
            @Override
            public void onSuccess(BaseInnerData baseInnerData) {
                if (baseInnerData.isSucceed()) {
                    tvAtlasTopSubscribe.setEnabled(false);
                    tvAtlasTopSubscribe.setText("已订阅");
                    showShortToast("订阅成功");
                }
            }
        }).exe(mAtlasEntity.getColumnId());
    }

    //友盟分享
    private UmengShareUtils shareUtils;

    private void share() {
        if (mData == null) return;

        String title, content;
        if (TextUtils.isEmpty(mData.getSummary())) {
            title = UIUtils.getAppName();
            content = mData.getTitle();
        } else {
            title = mData.getTitle();
            content = mData.getSummary();
        }
        String logoUrl = APIManager.endpoint.SHARE_24_LOGO_URL;
        String targetUrl = mData.getShareUrl();
        if (shareUtils == null)
            shareUtils = new UmengShareUtils();

        UmengShareBean instance = UmengShareBean.getInstance();
        instance.setArticleId(mArticleId);
        instance.setTitle(title);
        instance.setTextContent(content);
        instance.setImgUri(logoUrl);
        instance.setTargetUrl(targetUrl);
        instance.setColumnNum(3);
        instance.setIsScroll(true);
        instance.setIsShowUpvote(true);
        instance.setIsShowMoney(true);
        instance.setIsUpvote(mData.isPraised());
        shareUtils.startShare(instance, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (shareUtils != null) {
            shareUtils.initResult(requestCode, resultCode, data);
        }
        if (requestCode == REQUEST_CODE) {
            if (resultCode == ColumnDetailActivity.RESULT_CODE) {
                if (mData == null) return;
                boolean isSubscibed = data.getBooleanExtra(IKey.DATAS, mData.isSubscribed());
                tvAtlasTopSubscribe.setEnabled(!isSubscibed);
                if (isSubscibed) {
                    tvAtlasTopSubscribe.setText("已订阅");
                } else {
                    tvAtlasTopSubscribe.setText("订阅");
                }
            }
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(CommentResultEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        if (mData != null && event.getData() > 0) {
            mData.setCommentNum(mData.getCommentNum() + event.getData());
            mTvCommentsNum.setText(BizUtils.formatComments(mData.getCommentNum()));
        }
    }

    // 收藏
    private void collected() {
        if (mMenuCollected.isSelected()) {
            new CollectionCancelTask(new APICallBack<BaseInnerData>() {
                @Override
                public void onSuccess(BaseInnerData baseInnerData) {
                    if (baseInnerData.isSucceed()) {
                        showShortToast("取消收藏成功");
                        // 处理收藏状态切换动画
                        BizUtils.switchSelectorAnim(mMenuCollected, false);
                    } else {
                        showShortToast(baseInnerData.getResultMsg());
                        mMenuCollected.setSelected(false);
                    }
                }

                @Override
                public void onError(String errMsg, int errCode) {
                    showShortToast("取消收藏失败");
                }
            }).setTag(this).exe(mArticleId);
        } else {
            new CollectionArticleTask(new APICallBack<BaseInnerData>() {
                @Override
                public void onSuccess(BaseInnerData baseInnerData) {
                    if (baseInnerData.isSucceed()) {
                        showShortToast("收藏成功");
                        // 处理收藏状态切换动画
                        BizUtils.switchSelectorAnim(mMenuCollected, true);
                    } else {
                        showShortToast(baseInnerData.getResultMsg());
                        if (baseInnerData.getResultCode() == ResultCode.HAS_COLLECTION) {
                            mMenuCollected.setSelected(true);
                        }
                    }
                }

                @Override
                public void onError(String errMsg, int errCode) {
                    showShortToast("收藏失败");
                }
            }).setTag(this).exe(mArticleId);
        }
    }

    /**
     * 图片轻触回调
     */
    @Override
    public void onImageTap(View view) {
        mContainerBottom.clearAnimation();
        mContainerTop.clearAnimation();
        AnimatorSet animatorSet = new AnimatorSet();
        if (mContainerBottom.getAlpha() == 0) {
            animatorSet.playTogether(
                    createAnimator(mContainerBottom, 0, 1),
                    createAnimator(mContainerTop, 0, 1)
            );
        } else {
            animatorSet.playTogether(
                    createAnimator(mContainerBottom, 1, 0),
                    createAnimator(mContainerTop, 1, 0)
            );
        }
        animatorSet.setDuration(100);
        animatorSet.start();
    }

    private ObjectAnimator createAnimator(View view, int start, int end) {
        return ObjectAnimator.ofFloat(view, "alpha", start, end);
    }


    @Override
    public void onPageSelected(int position) {
        mIndex = position;
        mIndexView.setIndexNum(String.valueOf(mIndex + 1));
        setSwipeBackEnable(0 == position);
        AtlasEntity entity = mAtlasList.get(position);
        mTvContent.setText(entity.getDescription());
        mTvContent.scrollTo(0, 0);
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

    // 点赞
    private void fabulous() {
        if (mData == null) return;
        if (mData.isPraised()) {
            T.showNow(this, "您已点赞", Toast.LENGTH_SHORT);
            return;
        }
        new DraftPraiseTask(new APICallBack<BaseInnerData>() {
            @Override
            public void onError(String errMsg, int errCode) {
                showShortToast("点赞失败");
            }

            @Override
            public void onSuccess(BaseInnerData baseInnerData) {
                switch (baseInnerData.getResultCode()) {
                    case 0:
                        showShortToast("点赞成功");
                        mData.setPraised(1);
                        break;
                    case 1:
                        showShortToast("点赞失败");
                        break;
                    case 10004:
                        showShortToast("您已点赞");
                        break;
                }
            }
        }).setTag(this).exe(mArticleId);
    }


    @Subscribe
    public void handleAtlasShareEvent(UmengShareEventBean bean) {
        if (bean == null) return;
        if (mArticleId == bean.getmArticleID()) {
            if (!bean.isFromList()) {
                if (bean.isClickUpvot()) {
                    fabulous();
                }
            }
        }
    }
}
