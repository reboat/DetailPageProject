package com.zjrb.zjxw.detailproject.webjs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.permission.IPermissionCallBack;
import com.zjrb.core.common.permission.Permission;
import com.zjrb.core.common.permission.PermissionManager;
import com.zjrb.core.ui.anim.viewpager.DepthPageTransformer;
import com.zjrb.core.ui.fragment.ImagePreviewFragment;
import com.zjrb.core.ui.widget.photoview.HackyViewPager;
import com.zjrb.core.utils.DownloadUtil;
import com.zjrb.core.utils.PathUtil;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片浏览 Activity
 * <p>
 * 使用方式:
 * Intent ikey = new Intent(getActivity(), ImageBrowseActivity.class);
 * ikey.putExtra(ImageBrowseActivity.EXTRA_IMAGE_URLS, new String[]{});
 * ikey.putExtra(ImageBrowseActivity.EXTRA_IMAGE_INDEX, 0); // 从0开始
 * <p>
 * Created by wanglinjie.
 * create time:2017/9/19  上午11:34
 */
public class ImageBrowseActivity extends BaseActivity {

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    @BindView(R2.id.pager)
    HackyViewPager mPager;
    @BindView(R2.id.indicator)
    TextView indicator;

    private String viewpager_indicator = "%1$d/%2$d";
    private int pagerPosition;
    private ImagePagerAdapter mAdapter;

    public static Intent newIntent(Context ctx, String[] urls, int index) {
        Intent intent = new Intent(ctx, ImageBrowseActivity.class);
        intent.putExtra(ImageBrowseActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImageBrowseActivity.EXTRA_IMAGE_INDEX, index);
        return intent;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_image_browse);
        ButterKnife.bind(this);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        String[] urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
        initPage(savedInstanceState, urls);
    }

    /**
     * 保存当前页面position
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    /**
     * 初始化页面适配器
     *
     * @param savedInstanceState
     * @param urls
     */
    private void initPage(Bundle savedInstanceState, String[] urls) {
        mAdapter = new ImagePagerAdapter(
                getSupportFragmentManager(), urls);
        mPager.setPageTransformer(true, new DepthPageTransformer());
        mPager.setAdapter(mAdapter);

        CharSequence text = String.format(viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        mPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = String.format(viewpager_indicator,
                        arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }


    /**
     * 图片适配器
     */
    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public String[] fileList;

        public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.length;
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList[position];
            return ImagePreviewFragment.newInstance(url);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R2.id.iv_download})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        if (v.getId() == R.id.iv_download) {
            loadImage(pagerPosition);
        }
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
                    if (mAdapter.fileList == null || mAdapter.fileList.length < position || TextUtils.isEmpty(mAdapter.fileList[position]))
                        return;
                    PermissionManager.get().request(ImageBrowseActivity.this, new IPermissionCallBack() {
                        @Override
                        public void onGranted(boolean isAlreadyDef) {
                            T.showShort(ImageBrowseActivity.this, "当前下载第 " + position + " 张图片");
                            String url = mAdapter.fileList[position];
                            download(url);
                        }

                        @Override
                        public void onDenied(List<String> neverAskPerms) {
                            PermissionManager.showAdvice(ImageBrowseActivity.this, "保存图片需要开启存储权限");
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
                        T.showShort(ImageBrowseActivity.this, "保存成功");
                    }

                    @Override
                    public void onFail(String err) {
                        T.showShort(ImageBrowseActivity.this, "保存失败");
                    }
                })
                .download(url);
    }
}