package com.zjrb.zjxw.detailproject.photodetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zjrb.core.api.callback.APICallBack;
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
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.anim.viewpager.DepthPageTransformer;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.ui.widget.photoview.HackyViewPager;
import com.zjrb.core.utils.DownloadUtil;
import com.zjrb.core.utils.PathUtil;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.core.utils.webjs.BottomSaveDialogFragment;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.AlbumImageListBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.photodetail.adapter.ImagePrePagerAdapter;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 图集详情页
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */

public class AtlasDetailActivity extends BaseActivity implements ViewPager
        .OnPageChangeListener, View.OnTouchListener {

    @BindView(R2.id.ry_container)
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
    @BindView(R2.id.ly_tip_contain)
    RelativeLayout mLyContainer;
    @BindView(R2.id.view_exise)
    RelativeLayout mViewExise;
    @BindView(R2.id.fl_comment)
    FrameLayout mFyContainer;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;


    /**
     * 稿件IDid
     */
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
     * topbar
     */
    private DefaultTopBarHolder3 topHolder;
    private TextView mTvTitleTop;
    private ImageView mIvDownLoad;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = TopBarFactory.createDefault3(view, this);
        mTvTitleTop = topHolder.getTitleView();
        mIvDownLoad = topHolder.getDownView();
        return topHolder.getView();
    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            if (data.getQueryParameter(IKey.ID) != null) {
                mArticleId = data.getQueryParameter(IKey.ID);
            }
        }
    }

    /**
     * 重新初始化顶部栏和底部栏
     */
    private void reInitView() {
        mIndex = 0;
        mTvTitleTop.setVisibility(View.GONE);
    }

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
        //获取详情页
        new DraftDetailTask(new APICallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean atlasDetailEntity) {
                if (atlasDetailEntity == null || atlasDetailEntity.getArticle() == null
                        || atlasDetailEntity.getArticle().getAlbum_image_list() == null
                        || atlasDetailEntity.getArticle().getAlbum_image_list().isEmpty()) return;

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
                //图集撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                    showEmptyNewsDetail();
                } else {
                    mViewExise.setVisibility(View.VISIBLE);
                    mViewPager.setVisibility(View.GONE);
                    mContainerBottom.setVisibility(View.GONE);
                }
            }
        }).setTag(this).exe(mArticleId);
    }

    /**
     * @param atlasDetailEntity 获取图集详情页数据
     */
    private void fillData(DraftDetailBean atlasDetailEntity) {
        //显示UI
        mViewExise.setVisibility(View.GONE);
        mViewPager.setVisibility(View.VISIBLE);
        mContainerBottom.setVisibility(View.VISIBLE);

        mData = atlasDetailEntity;
        //设置数据
        if (atlasDetailEntity != null) {
            if (atlasDetailEntity.getArticle().getAlbum_image_list() != null && !atlasDetailEntity.getArticle().getAlbum_image_list().isEmpty()) {
                mAtlasList = atlasDetailEntity.getArticle().getAlbum_image_list();
            }
            mAtlasList = atlasDetailEntity.getArticle().getAlbum_image_list();
            initViewState(mData);
        }
        //设置图片列表
        if (mAtlasList != null && !mAtlasList.isEmpty()) {
            mViewPager.addOnPageChangeListener(this);
            mViewPager.setPageTransformer(true, new DepthPageTransformer());
            //设置图集标题和指示器
            mTvIndex.setText(String.valueOf(mIndex + 1) + "/");
            mTvTottleNum.setText(String.valueOf(atlasDetailEntity.getArticle().getAlbum_image_count()));
            mTvTitle.setText(atlasDetailEntity.getArticle().getDoc_title());
            //添加更多图集(假如有相关新闻)
            if (mData.getArticle().getRelated_news() != null && atlasDetailEntity.getArticle().getRelated_news().size() > 0) {
                mAtlasList.add(new AlbumImageListBean());
            }
            //设置图片count
            atlasDetailEntity.getArticle().setAlbum_image_list(mAtlasList);
            atlasDetailEntity.getArticle().setAlbum_image_count(mAtlasList.size());
            mViewPager.setAdapter(new ImagePrePagerAdapter(getSupportFragmentManager(), atlasDetailEntity));

            AlbumImageListBean entity = mAtlasList.get(mIndex);
            mTvContent.setText(entity.getDescription());
        }

    }

    /**
     * 刷新底部栏状态
     *
     * @param data
     */
    private void initViewState(DraftDetailBean data) {
        //评论数量
        if (!TextUtils.isEmpty(data.getArticle().getComment_count_general())) {
            mTvCommentsNum.setVisibility(View.VISIBLE);
            mTvCommentsNum.setText(data.getArticle().getComment_count_general());
        } else {
            mTvCommentsNum.setVisibility(View.GONE);
        }

        //是否已点赞
        if (data.getArticle().isLike_enabled()) {
            mMenuPrised.setVisibility(View.VISIBLE);
            mMenuPrised.setSelected(data.getArticle().isLiked());
        } else {
            mMenuPrised.setVisibility(View.GONE);
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


    @OnClick({R2.id.iv_share, R2.id.tv_comment, R2.id.menu_comment, R2.id.menu_prised, R2.id
            .menu_setting, R2.id.iv_top_download, R2.id.view_exise})
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
            finish();
            //分享
        } else if (id == R.id.iv_share) {
            UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                    .setSingle(false)
                    .setImgUri(mData.getArticle().getAlbum_image_list().get(0).getImage_url())
                    .setTextContent(mData.getArticle().getAlbum_image_list().get(0).getDescription())
                    .setTitle(mData.getArticle().getDoc_title())
                    .setTargetUrl(mData.getArticle().getUrl())
            );
            //评论框
        } else if (id == R.id.tv_comment) {
            if (mData != null) {
                CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(String.valueOf(mData.getArticle().getId())))).show(getSupportFragmentManager(), "CommentWindowDialog");
            }
            //评论列表
        } else if (id == R.id.menu_comment) {
            if (mData != null) {

                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(IKey.NEWS_DETAIL, mData);
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.COMMENT_ACTIVITY_PATH);

            }
            //点赞
        } else if (id == R.id.menu_prised) {
            fabulous();
            //设置
        } else if (id == R.id.menu_setting) {
            MoreDialog.newInstance(mData).show(getSupportFragmentManager(), "MoreDialog");
            //下载
        } else if (id == R.id.iv_top_download) {
            loadImage(mIndex);
        } else if (id == R.id.view_exise) {
            loadData();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengShareUtils.getInstance().initResult(requestCode, resultCode, data);
    }


    @Override
    public void onPageSelected(int position) {
        mIndex = position;
        mTvIndex.setText(String.valueOf(mIndex + 1) + "/");
        setSwipeBackEnable(0 == position);
        AlbumImageListBean entity = mAtlasList.get(position);
        mTvContent.setText(entity.getDescription());
        mTvContent.scrollTo(0, 0);

        //更多图集
        if (mData.getArticle().getRelated_news() != null && mData.getArticle().getRelated_news().size() > 0) {
            setTopTitle(position);
        }

        //文案显示
        if (mData.getArticle().getRelated_news() != null && mData.getArticle().getRelated_news().size() > 0 && position == (mAtlasList.size() - 1)) {
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
                if (mData.getArticle().getRelated_news() != null && mData.getArticle().getRelated_news().size() > 0
                        && position == (mAtlasList.size() - 1) && mIvDownLoad.getVisibility() == View.VISIBLE) {
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

    /**
     * 稿件点赞
     */
    private void fabulous() {
        if (mData == null) return;
        if (mData.getArticle().isLiked()) {
            T.showNow(this, getString(R.string.module_detail_you_have_liked), Toast.LENGTH_SHORT);
            return;
        }
        new DraftPraiseTask(new APICallBack<Void>() {
            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(UIUtils.getContext(), getString(R.string.module_detail_prise_failed));
            }

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(UIUtils.getContext(), getString(R.string.module_detail_prise_success));
                mData.getArticle().setLiked(true);
                mMenuPrised.setSelected(true);
            }
        }).setTag(this).exe(mArticleId, true);
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
                        T.showShort(AtlasDetailActivity.this, getString(R.string.module_detail_save_success));
                    }

                    @Override
                    public void onFail(String err) {
                        T.showShort(AtlasDetailActivity.this, getString(R.string.module_detail_save_failed));
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
        ft.add(R.id.ry_container, EmptyStateFragment.newInstance()).commit();
    }

}
