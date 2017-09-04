package com.zjrb.zjxw.detailproject.photodetail;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.zjrb.core.api.callback.APICallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.listener.IOnImageTapListener;
import com.zjrb.core.domain.base.BaseInnerData;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.anim.viewpager.DepthPageTransformer;
import com.zjrb.core.ui.widget.photoview.HackyViewPager;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.AlbumImageListBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.eventBus.CommentResultEvent;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.photodetail.adapter.ImagePrePagerAdapter;
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


/**
 * 图集详情页
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */

public class AtlasDetailActivity extends BaseActivity implements IOnImageTapListener, ViewPager
        .OnPageChangeListener, View.OnTouchListener {

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
    @BindView(R2.id.fit_bottom_layout)
    FitWindowsFrameLayout mFitBottomLayout;
    @BindView(R2.id.fit_top_layout)
    FitWindowsFrameLayout mFitTopLayout;
    @BindView(R2.id.container_top)
    LinearLayout mContainerTop;
    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.iv_share)
    ImageView mIvShare;
    @BindView(R2.id.menu_comment)
    ImageView mIvComment;

    public int mArticleId = -1;
    public int mlfId = -1;

    private int mIndex;
    private List<AlbumImageListBean> mAtlasList;
    private DraftDetailBean mData;
    public static final int REQUEST_CODE = 0x001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_photo_detail);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        mFloorBar.setOnTouchListener(this);
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        loadData();
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
        }
    }


    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, "").getView();
    }


    private void loadData() {
        //获取详情页
        new DraftDetailTask(new APICallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean atlasDetailEntity) {
                fillData(atlasDetailEntity);
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad()).exe(mArticleId);

    }

    /**
     * @param atlasDetailEntity 获取图集详情页数据
     */
    private void fillData(DraftDetailBean atlasDetailEntity) {
        mData = atlasDetailEntity;
        //设置数据
        if (atlasDetailEntity != null) {
            if (atlasDetailEntity.getAlbum_image_list() != null && !atlasDetailEntity.getAlbum_image_list().isEmpty()) {
                mAtlasList = atlasDetailEntity.getAlbum_image_list();
            }
            mTvCommentsNum.setText(BizUtils.formatComments(atlasDetailEntity.getComment_count()));
            mMenuPrised.setSelected(atlasDetailEntity.isFollowed());
            BizUtils.setCommentSet(mTvComment, mData.getComment_level());
        }
        //设置图片列表
        if (mAtlasList != null && !mAtlasList.isEmpty()) {
            List<String> imgs = new ArrayList<>(mAtlasList.size());
            for (AlbumImageListBean entity : mAtlasList) {
                imgs.add(entity.getImage_url());
            }
            //添加更多图集索引
            if (imgs.size() > 0) {
                imgs.add("");
            }

            mViewPager.addOnPageChangeListener(this);
            mViewPager.setPageTransformer(true, new DepthPageTransformer());

            mViewPager.setAdapter(new ImagePrePagerAdapter(getSupportFragmentManager(), imgs));

            mTvIndex.setText(String.valueOf(mIndex + 1));
            mTvTottleNum.setText(String.valueOf(mAtlasList.size()));
            mTvTitle.setText(atlasDetailEntity.getList_title());
            AlbumImageListBean entity = mAtlasList.get(mIndex);
            mTvContent.setText(entity.getDescription());
        }

    }


    @OnClick({R2.id.iv_back, R2.id.iv_share, R2.id.tv_comment, R2.id.menu_comment, R2.id.menu_prised, R2.id
            .menu_share})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        click(view.getId());
    }

    /**
     * 点击事件
     */
    private void click(int id) {
        //返回
        if (id == R.id.iv_back) {
            finish();
            //分享
        } else if (id == R.id.iv_share) {
            share();
            //评论框
        } else if (id == R.id.tv_comment) {
            if (mData != null && BizUtils.isCanComment(this, mData.getComment_level())) {
                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/CommentActivity")
                        .buildUpon()
                        .build(), 0);
                return;
            }
            //评论列表
        } else if (id == R.id.menu_comment) {
            if (mData != null) {
                if (BizUtils.isCanComment(this, mData.getComment_level())) {
                    Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/CommentActivity")
                            .buildUpon()
                            .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(mData.getId()))
                            .appendQueryParameter(Key.MLF_ID, String.valueOf(mData.getMlf_id()))
                            .appendQueryParameter(Key.COMMENT_SET, String.valueOf(mData.getComment_level()))
                            .appendQueryParameter(Key.TITLE, mData.getList_title())
                            .build(), 0);
                }

            }
            //点赞
        } else if (id == R.id.menu_prised) {
            fabulous();
            //分享
        } else if (id == R.id.menu_share) {
            share();
        }
    }

    //友盟分享
    private UmengShareUtils shareUtils;

    private void share() {
        if (mData == null) return;

        String title, content;
//        if (TextUtils.isEmpty(mData.getSummary())) {
//            title = UIUtils.getAppName();
//            content = mData.getList_title();
//        } else {
        title = mData.getList_title();
//            content = mData.getSummary();
//        }
        //TODO WLJ logo
        String logoUrl = "http://10.200.76.17/images/24hlogo.png";//APIManager.endpoint.SHARE_24_LOGO_URL;
        String targetUrl = mData.getUrl();
        if (shareUtils == null)
            shareUtils = new UmengShareUtils();

        UmengShareBean instance = UmengShareBean.getInstance();
//        instance.setArticleId(mArticleId);
        instance.setTitle(title);
//        instance.setTextContent(content);
        instance.setImgUri(logoUrl);
        instance.setTargetUrl(targetUrl);
        shareUtils.startShare(instance, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (shareUtils != null) {
            shareUtils.initResult(requestCode, resultCode, data);
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
            mData.setComment_count(mData.getComment_count() + event.getData());
            mTvCommentsNum.setText(BizUtils.formatComments(mData.getComment_count()));
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
        mTvIndex.setText(String.valueOf(mIndex + 1));
        setSwipeBackEnable(0 == position);
        AlbumImageListBean entity = mAtlasList.get(position);
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
        if (mData.isLiked()) {
            T.showNow(this, "您已点赞", Toast.LENGTH_SHORT);
            return;
        }
        new DraftPraiseTask(new APICallBack<BaseInnerData>() {
            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(UIUtils.getContext(), "点赞失败");
            }

            @Override
            public void onSuccess(BaseInnerData baseInnerData) {
                switch (baseInnerData.getResultCode()) {
                    case 0:
                        T.showShort(UIUtils.getContext(), "点赞成功");
                        mData.setLiked(true);
                        break;
                    case 1:
                        T.showShort(UIUtils.getContext(), "点赞失败");
                        break;
                    case 10004:
                        T.showShort(UIUtils.getContext(), "您已点赞");
                        break;
                }
            }
        }).setTag(this).exe(mArticleId);
    }

}
