package com.zjrb.zjxw.detailproject.photodetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.zjrb.core.api.callback.APICallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.permission.IPermissionCallBack;
import com.zjrb.core.common.permission.Permission;
import com.zjrb.core.common.permission.PermissionManager;
import com.zjrb.core.domain.base.BaseInnerData;
import com.zjrb.core.domain.eventbus.EventBase;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.anim.viewpager.DepthPageTransformer;
import com.zjrb.core.ui.widget.photoview.HackyViewPager;
import com.zjrb.core.utils.DownloadUtil;
import com.zjrb.core.utils.PathUtil;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.AlbumImageListBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.RelatedNewsBean;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.photodetail.adapter.ImagePrePagerAdapter;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.utils.BizUtils;
import com.zjrb.zjxw.detailproject.webjs.BottomSaveDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zjrb.zjxw.detailproject.utils.BizUtils.comment.JY;


/**
 * 图集详情页
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */

public class AtlasDetailActivity extends BaseActivity implements ViewPager
        .OnPageChangeListener, View.OnTouchListener {

    @BindView(R2.id.activity_atlas_detail)
    FrameLayout mContainer;
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
    @BindView(R2.id.tv_top_bar_title)
    TextView mTvTitleTop;
    @BindView(R2.id.iv_top_download)
    ImageView mIvDownLoad;
    @BindView(R2.id.ly_tip_contain)
    RelativeLayout mLyContainer;


    public String mArticleId = "";

    private int mIndex;
    private List<AlbumImageListBean> mAtlasList;
    private DraftDetailBean mData;

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
            if (data.getQueryParameter(Key.ARTICLE_ID) != null) {
                mArticleId = data.getQueryParameter(Key.ARTICLE_ID);
            }
        }
    }

    /**
     * 获取图集数据
     */
    private void loadData() {
        //获取详情页
        new DraftDetailTask(new APICallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean atlasDetailEntity) {
                //设置下载按钮
                if (atlasDetailEntity.getArticle().getAlbum_image_list() != null && !atlasDetailEntity.getArticle().getAlbum_image_list().isEmpty()) {
                    mIvDownLoad.setVisibility(View.VISIBLE);
                } else {
                    mIvDownLoad.setVisibility(View.GONE);
                }

                fillData(atlasDetailEntity);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //TODO WLJ 撤稿处理
                T.showShort(getBaseContext(), errMsg);
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad()).exe(mArticleId);

    }

    private List<AlbumImageListBean> mockTest() {
        List<AlbumImageListBean> list = new ArrayList<>();
        AlbumImageListBean b = new AlbumImageListBean();
        b.setDescription("结婚的就好的骄傲和是的空间啊圣诞节拉黑啊我挂号费就好发酵素发sjaf jks dfJ fjhsd fdsh  开始看老大说的奥迪");
        b.setImage_url("http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg");
        AlbumImageListBean b1 = new AlbumImageListBean();
        b1.setDescription("结婚的就好的骄傲和是的空间啊圣诞节拉黑啊我挂号费就好发酵素发sjaf jks dfJ fjhsd fdsh  开始看老大说的奥迪");
        b1.setImage_url("http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg");
        AlbumImageListBean b2 = new AlbumImageListBean();
        b2.setDescription("结婚的就好的骄傲和是的空间啊圣诞节拉黑啊我挂号费就好发酵素发sjaf jks dfJ fjhsd fdsh  开始看老大说的奥迪");
        b2.setImage_url("http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg");
        AlbumImageListBean b3 = new AlbumImageListBean();
        b3.setDescription("结婚的就好的骄傲和是的空间啊圣诞节拉黑啊我挂号费就好发酵素发sjaf jks dfJ fjhsd fdsh  开始看老大说的奥迪");
        b3.setImage_url("http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg");
        AlbumImageListBean b4 = new AlbumImageListBean();
        b4.setDescription("结婚的就好的骄傲和是的空间啊圣诞节拉黑啊我挂号费就好发酵素发sjaf jks dfJ fjhsd fdsh  开始看老大说的奥迪");
        b4.setImage_url("http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg");
        AlbumImageListBean b5 = new AlbumImageListBean();
        b5.setDescription("结婚的就好的骄傲和是的空间啊圣诞节拉黑啊我挂号费就好发酵素发sjaf jks dfJ fjhsd fdsh  开始看老大说的奥迪");
        b5.setImage_url("http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg");
        list.add(b);
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        list.add(b5);
        return list;
    }

    private List<RelatedNewsBean> MockTest2() {
        List<RelatedNewsBean> list = new ArrayList<>();
        RelatedNewsBean b = new RelatedNewsBean();
        b.setPic("http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg");
        RelatedNewsBean b1 = new RelatedNewsBean();
        b1.setPic("http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg");
        RelatedNewsBean b2 = new RelatedNewsBean();
        b2.setPic("http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg");
        RelatedNewsBean b3 = new RelatedNewsBean();
        b3.setPic("http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg");
        RelatedNewsBean b4 = new RelatedNewsBean();
        b4.setPic("http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg");
        list.add(b);
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        return list;
    }

    /**
     * @param atlasDetailEntity 获取图集详情页数据
     */
    private void fillData(DraftDetailBean atlasDetailEntity) {
        mData = atlasDetailEntity;
        //设置数据
        if (atlasDetailEntity != null) {
            if (atlasDetailEntity.getArticle().getAlbum_image_list() != null && !atlasDetailEntity.getArticle().getAlbum_image_list().isEmpty()) {
                mAtlasList = atlasDetailEntity.getArticle().getAlbum_image_list();
            }
            //TODO  WLJ TEST
            mAtlasList = mockTest();
            //评论数量
            if (BizUtils.isCanComment(this, mData.getArticle().getComment_level())) {
                mTvCommentsNum.setText(BizUtils.formatComments(atlasDetailEntity.getArticle().getComment_count()));
            } else {
                mTvCommentsNum.setVisibility(View.GONE);
            }

            //是否已点赞
            if (atlasDetailEntity.getArticle().isLike_enabled()) {
                mMenuPrised.setSelected(atlasDetailEntity.getArticle().isLiked());
            } else {
                mMenuPrised.setVisibility(View.GONE);
            }
            BizUtils.setCommentSet(mTvComment, mData.getArticle().getComment_level());
        }
        //设置图片列表
        if (mAtlasList != null && !mAtlasList.isEmpty()) {
//            List<String> imgs = new ArrayList<>(mAtlasList.size());
//            for (AlbumImageListBean entity : mAtlasList) {
//                imgs.add(entity.getImage_url());
//            }
//            //添加更多图集索引
//            if (imgs.size() > 0) {
//                imgs.add("");
//            }

            mViewPager.addOnPageChangeListener(this);
            mViewPager.setPageTransformer(true, new DepthPageTransformer());
            //TODO  WLJ test
            mAtlasList.add(new AlbumImageListBean());
            atlasDetailEntity.getArticle().setAlbum_image_list(mAtlasList);
            atlasDetailEntity.getArticle().setAlbum_image_count(mAtlasList.size());
            atlasDetailEntity.getArticle().setRelated_news(MockTest2());

            mViewPager.setAdapter(new ImagePrePagerAdapter(getSupportFragmentManager(), atlasDetailEntity));

            mTvIndex.setText(String.valueOf(mIndex + 1));
            mTvTottleNum.setText(String.valueOf(mAtlasList.size()));
            mTvTitle.setText(atlasDetailEntity.getArticle().getList_title());
            AlbumImageListBean entity = mAtlasList.get(mIndex);
            mTvContent.setText(entity.getDescription());
        }

    }


    @OnClick({R2.id.iv_back, R2.id.iv_share, R2.id.tv_comment, R2.id.menu_comment, R2.id.menu_prised, R2.id
            .menu_share, R2.id.iv_top_download})
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
//            share();
            //评论框
        } else if (id == R.id.tv_comment) {
            if (mData != null && BizUtils.isCanComment(this, mData.getArticle().getComment_level())) {
                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/CommentActivity")
                        .buildUpon()
                        .build(), 0);
                return;
            }
            //评论列表
        } else if (id == R.id.menu_comment) {
            if (mData != null) {
                if (BizUtils.isCanComment(this, mData.getArticle().getComment_level())) {
                    Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/CommentActivity")
                            .buildUpon()
                            .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(mData.getArticle().getId()))
                            .appendQueryParameter(Key.MLF_ID, String.valueOf(mData.getArticle().getMlf_id()))
                            .appendQueryParameter(Key.COMMENT_SET, String.valueOf(mData.getArticle().getComment_level()))
                            .appendQueryParameter(Key.TITLE, mData.getArticle().getList_title())
                            .build(), 0);
                }

            }
            //点赞
        } else if (id == R.id.menu_prised) {
            fabulous();
            //分享
        } else if (id == R.id.menu_share) {
//            share();
            //下载
        } else if (id == R.id.iv_top_download) {
            loadImage(mIndex);
        }
    }

    //友盟分享
//    private UmengShareUtils shareUtils;
//
//    private void share() {
//        if (mData == null) return;
//
//        String title, content;
////        if (TextUtils.isEmpty(mData.getSummary())) {
////            title = UIUtils.getAppName();
////            content = mData.getList_title();
////        } else {
//        title = mData.getArticle().getList_title();
////            content = mData.getSummary();
////        }
//        //TODO WLJ logo
//        String logoUrl = "http://10.200.76.17/images/24hlogo.png";//APIManager.endpoint.SHARE_24_LOGO_URL;
//        String targetUrl = mData.getArticle().getUrl();
//        if (shareUtils == null)
//            shareUtils = new UmengShareUtils();
//
//        UmengShareBean instance = UmengShareBean.getInstance();
////        instance.setArticleId(mArticleId);
//        instance.setTitle(title);
////        instance.setTextContent(content);
//        instance.setImgUri(logoUrl);
//        instance.setTargetUrl(targetUrl);
//        shareUtils.startShare(instance, this);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (shareUtils != null) {
//            shareUtils.initResult(requestCode, resultCode, data);
//        }
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
    public void onEvent(EventBase event) {
        EventBus.getDefault().removeStickyEvent(event);
//        if(event instanceof AtlasDetailLastPageEvent){
//            if(((AtlasDetailLastPageEvent) event).getData().equals("true")){
//                topBar.setTopBarText(getString(R.string.module_detail_more_image));
//            }else{
//                topBar.setTopBarText("");
//            }
//        }
//        if (mData != null && event.getData() > 0) {
//            mData.getArticle().setComment_count(mData.getArticle().getComment_count() + event.getData());
//            mTvCommentsNum.setText(BizUtils.formatComments(mData.getArticle().getComment_count()));
//        }
    }

//    /**
//     * 图片轻触回调
//     */
//    @Override
//    public void onImageTap(View view) {
//        mContainerBottom.clearAnimation();
//        mContainerTop.clearAnimation();
//        AnimatorSet animatorSet = new AnimatorSet();
//        if (mContainerBottom.getAlpha() == 0) {
//            animatorSet.playTogether(
//                    createAnimator(mContainerBottom, 0, 1),
//                    createAnimator(mContainerTop, 0, 1)
//            );
//        } else {
//            animatorSet.playTogether(
//                    createAnimator(mContainerBottom, 1, 0),
//                    createAnimator(mContainerTop, 1, 0)
//            );
//        }
//        animatorSet.setDuration(100);
//        animatorSet.start();
//    }

//    private ObjectAnimator createAnimator(View view, int start, int end) {
//        return ObjectAnimator.ofFloat(view, "alpha", start, end);
//    }


    @Override
    public void onPageSelected(int position) {
        mIndex = position;
        mTvIndex.setText(String.valueOf(mIndex + 1));
        setSwipeBackEnable(0 == position);
        AlbumImageListBean entity = mAtlasList.get(position);
        mTvContent.setText(entity.getDescription());
        mTvContent.scrollTo(0, 0);

        //更多图集
        setTopTitle(position);

        //文案显示
        if (position == (mAtlasList.size() - 1)) {
            mLyContainer.setVisibility(View.GONE);
            mTvContent.setVisibility(View.GONE);
        } else {
            mLyContainer.setVisibility(View.VISIBLE);
            mTvContent.setVisibility(View.VISIBLE);
        }

        //下载按钮
        if (mAtlasList != null && !mAtlasList.isEmpty()) {
            if (mAtlasList.get(position) != null && !mAtlasList.get(position).equals("")) {
                if (mIvDownLoad.getVisibility() == View.GONE) {
                    mIvDownLoad.setVisibility(View.VISIBLE);
                }
                //更多图集不需要显示下载图标
                if (position == (mAtlasList.size() - 1) && mIvDownLoad.getVisibility() == View.VISIBLE) {
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
            } else {
                mTvTitleTop.setVisibility(View.GONE);
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

    //稿件点赞
    private void fabulous() {
        if (mData == null) return;
        if (mData.getArticle().isLiked()) {
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
                        mData.getArticle().setLiked(true);
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
                    if (mAtlasList == null || mAtlasList.size() < position || mAtlasList.get(position).equals(""))
                        return;
                    PermissionManager.get().request(AtlasDetailActivity.this, new IPermissionCallBack() {
                        @Override
                        public void onGranted(boolean isAlreadyDef) {
                            T.showShort(AtlasDetailActivity.this, "当前下载第 " + position + " 张图片");
                            String url = mAtlasList.get(position).getImage_url();
                            download(url);
                        }

                        @Override
                        public void onDenied(List<String> neverAskPerms) {
                            PermissionManager.showAdvice(AtlasDetailActivity.this, "保存图片需要开启存储权限");
                        }

                        @Override
                        public void onElse(List<String> deniedPerms, List<String> neverAskPerms) {

                        }
                    }, Permission.STORAGE_WRITE);
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
        DownloadUtil.get()
                .setDir(PathUtil.getImagePath())
                .setListener(new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onLoading(int progress) {

                    }

                    @Override
                    public void onSuccess(String path) {
                        T.showShort(AtlasDetailActivity.this, "保存成功");
                    }

                    @Override
                    public void onFail(String err) {
                        T.showShort(AtlasDetailActivity.this, "保存失败");
                    }
                })
                .download(url);
    }


    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mContainer.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ly_container, EmptyStateFragment.newInstance(String.valueOf(mData.getArticle().getColumn_id()))).commit();
    }

}
