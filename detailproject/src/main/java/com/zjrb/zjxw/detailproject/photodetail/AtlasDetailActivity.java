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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zjrb.core.api.callback.APICallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.global.IKey;
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
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.AlbumImageListBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.photodetail.adapter.ImagePrePagerAdapter;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.utils.BizUtils;
import com.zjrb.zjxw.detailproject.webjs.BottomSaveDialogFragment;

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
    @BindView(R2.id.tv_top_bar_title)
    TextView mTvTitleTop;
    @BindView(R2.id.iv_top_download)
    ImageView mIvDownLoad;
    @BindView(R2.id.ly_tip_contain)
    RelativeLayout mLyContainer;
    @BindView(R2.id.view_exise)
    LinearLayout mViewExise;


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
            if (data.getQueryParameter(IKey.ID) != null) {
                mArticleId = data.getQueryParameter(IKey.ID);
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
        mData = atlasDetailEntity;
        //设置数据
        if (atlasDetailEntity != null) {
            if (atlasDetailEntity.getArticle().getAlbum_image_list() != null && !atlasDetailEntity.getArticle().getAlbum_image_list().isEmpty()) {
                mAtlasList = atlasDetailEntity.getArticle().getAlbum_image_list();
            }
            mAtlasList = atlasDetailEntity.getArticle().getAlbum_image_list();
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
            mViewPager.addOnPageChangeListener(this);
            mViewPager.setPageTransformer(true, new DepthPageTransformer());
            mAtlasList.add(new AlbumImageListBean());
            //设置图片count
            atlasDetailEntity.getArticle().setAlbum_image_list(mAtlasList);
            atlasDetailEntity.getArticle().setAlbum_image_count(mAtlasList.size());
            atlasDetailEntity.getArticle().setRelated_news(mData.getArticle().getRelated_news());
            mViewPager.setAdapter(new ImagePrePagerAdapter(getSupportFragmentManager(), atlasDetailEntity));

            //设置图集标题和指示器
            mTvIndex.setText(String.valueOf(mIndex + 1));
            mTvTottleNum.setText(String.valueOf(mAtlasList.size()));
            mTvTitle.setText(atlasDetailEntity.getArticle().getList_title());
            AlbumImageListBean entity = mAtlasList.get(mIndex);
            mTvContent.setText(entity.getDescription());
        }

    }


    @OnClick({R2.id.iv_back, R2.id.iv_share, R2.id.tv_comment, R2.id.menu_comment, R2.id.menu_prised, R2.id
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
            finish();
            //分享
        } else if (id == R.id.iv_share) {
            //TODO WLJ 分享  默认图片地址？？？默认标题???分享地址??
            UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                    .setSingle(false)
                    .setImgUri(!TextUtils.isEmpty(mData.getArticle().getAlbum_image_list().get(0).getImage_url()) ? mData.getArticle().getAlbum_image_list().get(0).getImage_url() : "")
                    .setTextContent(!TextUtils.isEmpty(mData.getArticle().getAlbum_image_list().get(0).getDescription()) ? mData.getArticle().getAlbum_image_list().get(0).getDescription() :
                            getString(R.string.module_detail_share_content_from))
                    .setTitle(!TextUtils.isEmpty(mData.getArticle().getList_title()) ? mData.getArticle().getList_title() : getString(R.string.module_detail_share_content_from))
                    .setTargetUrl(!TextUtils.isEmpty(mData.getArticle().getUrl()) ? mData.getArticle().getUrl() : "")
            );
            //评论框
        } else if (id == R.id.tv_comment) {
            if (mData != null && BizUtils.isCanComment(this, mData.getArticle().getComment_level())) {
                CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(String.valueOf(mData.getArticle().getId())))).show(getSupportFragmentManager(), "CommentWindowDialog");
                return;
            }
            //评论列表
        } else if (id == R.id.menu_comment) {
            if (mData != null) {
                if (BizUtils.isCanComment(this, mData.getArticle().getComment_level())) {

                    if (bundle == null) {
                        bundle = new Bundle();
                    }
                    bundle.putInt(IKey.ID, mData.getArticle().getId());
                    bundle.putInt(IKey.MLF_ID, mData.getArticle().getMlf_id());
                    bundle.putInt(IKey.COMMENT_SET, mData.getArticle().getComment_level());
                    bundle.putString(IKey.TITLE, mData.getArticle().getList_title());
                    Nav.with(UIUtils.getContext()).setExtras(bundle).toPath("/detail/CommentActivity");
                }

            }
            //点赞
        } else if (id == R.id.menu_prised) {
            fabulous();
            //分享
        } else if (id == R.id.menu_setting) {
            //TODO WLJ 修改字体，夜间模式之类
//            share();
            //下载
        } else if (id == R.id.iv_top_download) {
            loadImage(mIndex);
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
            if (!TextUtils.isEmpty(mAtlasList.get(position).getImage_url())) {
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

    /**
     * 稿件点赞
     */
    private void fabulous() {
        if (mData == null) return;
        if (mData.getArticle().isLiked()) {
            T.showNow(this, "您已点赞", Toast.LENGTH_SHORT);
            return;
        }
        new DraftPraiseTask(new APICallBack<Void>() {
            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(UIUtils.getContext(), "点赞失败");
            }

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(UIUtils.getContext(), "点赞成功");
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
