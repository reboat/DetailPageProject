package com.zjrb.zjxw.detailproject.nomaldetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.permission.IPermissionCallBack;
import com.zjrb.core.permission.Permission;
import com.zjrb.core.permission.PermissionManager;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.photodetail.ImagePreviewFragment;
import com.zjrb.zjxw.detailproject.widget.BottomSaveDialogFragment;
import com.zjrb.zjxw.detailproject.widget.DepthPageTransformer;
import com.zjrb.zjxw.detailproject.widget.photoview.HackyViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.utils.PathUtil;
import cn.daily.news.update.util.DownloadUtil;
import port.SerializableHashMap;

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
public class ImageBrowseActivity extends DailyActivity {

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String EXTRA_IMAGE_SRCS = "image_srcs";

    @BindView(R2.id.pager)
    HackyViewPager mPager;
    @BindView(R2.id.indicator)
    TextView indicator;

    /**
     * 图片页码指示器
     */
    private String viewpager_indicator = "%1$d/%2$d";
    private int pagerPosition;
    private String[] urls;
    private ImagePagerAdapter mAdapter;
    private SerializableHashMap map;
    private boolean isFromAtlas = false;

    /**
     * @param intent
     */
    private void getIntentData(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(EXTRA_IMAGE_URLS)) {
                urls = intent.getStringArrayExtra(EXTRA_IMAGE_URLS);
            }
            if (intent.hasExtra(EXTRA_IMAGE_INDEX)) {
                pagerPosition = intent.getIntExtra(EXTRA_IMAGE_INDEX, 0);
            }
            if (intent.hasExtra(EXTRA_IMAGE_SRCS)) {
                map = (SerializableHashMap) intent.getSerializableExtra(EXTRA_IMAGE_SRCS);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_image_browse);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        initPage(savedInstanceState, urls);
    }

    /**
     * 保存当前页面position
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }


    /**
     * 初始化页面适配器
     *
     * @param savedInstanceState
     * @param urls
     */
    private void initPage(Bundle savedInstanceState, String[] urls) {
        if (urls == null || urls.length == 0) return;
        mAdapter = new ImagePagerAdapter(
                getSupportFragmentManager(), urls);
        mPager.setPageTransformer(true, new DepthPageTransformer());
        mPager.setAdapter(mAdapter);

        CharSequence text = String.format(viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }
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
                pagerPosition = arg0;
                map.getMap().put(arg0, true);
            }

        });

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
            return ImagePreviewFragment.newInstance(url, isFromAtlas, 0);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("map", map);
        setResult(1, intent);
        super.onBackPressed();
    }

    @OnClick({R2.id.iv_download, R2.id.iv_back})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        if (v.getId() == R.id.iv_download) {
            clickDownLoadWM();
            loadImage(pagerPosition);
        } else if (v.getId() == R.id.iv_back) {
            finish();
        }
    }

    private void clickDownLoadWM() {
        new Analytics.AnalyticsBuilder(this, "A0025", "A0025", "PictureRelatedOperation", false)
                .setEvenName("点击图片下载按钮")
                .setObjectID("")
                .setObjectType(ObjectType.PictureType)
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("mediaURL", urls.length > 0 ? urls[0] : "")
                        .toString())
                .pageType("新闻详情页")
                .operationType("点击图片下载")
                .build()
                .send();
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
                    if (mAdapter.fileList == null || mAdapter.fileList.length < (position) || TextUtils.isEmpty(mAdapter.fileList[position]))
                        return;
                    PermissionManager.get().request(ImageBrowseActivity.this, new IPermissionCallBack() {
                        @Override
                        public void onGranted(boolean isAlreadyDef) {
                            T.showShort(ImageBrowseActivity.this, "当前下载第 " + (position + 1) + " 张图片");
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
                    }, Permission.STORAGE_READE, Permission.STORAGE_WRITE);
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
        if (!TextUtils.isEmpty(url) && (url.contains("?w=") || url.contains("?width="))) {
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
                        T.showShort(ImageBrowseActivity.this, getString(R.string.module_core_download_success));
                        // 扫描图片到系统相册
                        AppUtils.scanFile(path);
                    }

                    @Override
                    public void onFail(String err) {
                        T.showShort(ImageBrowseActivity.this, getString(R.string.module_core_download_failed));
                    }
                })
                .download(url);
    }

}