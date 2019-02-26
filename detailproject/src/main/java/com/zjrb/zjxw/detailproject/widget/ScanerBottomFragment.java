package com.zjrb.zjxw.detailproject.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.R;
import com.zjrb.core.R2;
import com.zjrb.core.base.BaseActivity;
import com.zjrb.core.permission.IPermissionCallBack;
import com.zjrb.core.permission.Permission;
import com.zjrb.core.permission.PermissionManager;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.utils.PathUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.update.util.DownloadUtil;


/**
 * 长按识别二维码底部弹出框
 *
 * @author wanglinjie
 * @date 18/5/7 下午17:12.
 */

final public class ScanerBottomFragment extends BaseDialogFragment {

    protected Dialog dialog;
    private static final String TAG = "BottomDialogFragment";
    private static ScanerBottomFragment fragment = null;

    @BindView(R2.id.tv_scaner_img)
    TextView tvScanerImg;
    @BindView(R2.id.tv_save_img)
    TextView tvSaveImg;
    @BindView(R2.id.tv_cancel)
    TextView tvCancel;
    @BindView(R2.id.ly_scaner)
    LinearLayout lyScaner;

    private String imgUrl;
    private Activity act;
    private boolean isScanerImg;
    private int mlfid = 0;

    public static ScanerBottomFragment newInstance() {
        if (fragment == null) {
            synchronized (ScanerBottomFragment.class) {
                if (fragment == null) {
                    fragment = new ScanerBottomFragment();
                }
            }
        }
        return fragment;

    }

    /**
     * 是否是二维码图片
     *
     * @param isScanerImg
     * @return
     */
    public ScanerBottomFragment isScanerImg(boolean isScanerImg) {
        this.isScanerImg = isScanerImg;
        return this;
    }

    /**
     * 设置activity
     *
     * @param act
     * @return
     */
    public ScanerBottomFragment setActivity(Activity act) {
        this.act = act;
        return this;
    }

    /**
     * 设置二维码图片url
     *
     * @param imgUrl
     * @return
     */
    public ScanerBottomFragment setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    /**
     * 设置mlf_id
     *
     * @param mlfId
     * @return
     */
    public ScanerBottomFragment setMlfId(int mlfId) {
        this.mlfid = mlfid;
        return this;
    }

    /**
     * @param savedInstanceState
     * @return AlertDialog 在5.0及以下使用造成内存泄露
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.BottomDialog);
        View view = View.inflate(getContext(), R.layout.module_core_scaner_bottom_layout, null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        initWindow();
        showScanerLayout();
        return dialog;
    }

    /**
     * 显示dialog扫描布局
     */
    private void showScanerLayout() {
        if (!isScanerImg) {
            lyScaner.setVisibility(View.GONE);
        } else {
            lyScaner.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置底部弹出框的窗口样式
     */
    private void initWindow() {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        if (null != dialog) {
            dialog.getWindow().setLayout(-1, -2);
        }
    }

    /**
     * 显示分享弹出框
     *
     * @param appCompatActivity
     * @return
     */
    public ScanerBottomFragment showDialog(AppCompatActivity appCompatActivity) {
        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
        if (!appCompatActivity.isFinishing()
                && null != fragment
                && !fragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
        }

        return fragment;
    }

    @OnClick({R2.id.tv_scaner_img, R2.id.tv_save_img, R2.id.tv_cancel})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        //识别二维码
        if (v.getId() == R.id.tv_scaner_img) {
            if (!TextUtils.isEmpty(imgUrl)) {
                scanerImg(imgUrl);
            }
            //保存图片
        } else if (v.getId() == R.id.tv_save_img) {
            longPressAnalytics();
            if (!TextUtils.isEmpty(imgUrl)) {
                saveImg(imgUrl);
            }
            //取消
        } else if (v.getId() == R.id.tv_cancel) {
            dismissFragmentDialog();
        }
    }

    @Override
    public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }


    /**
     * 保存图片
     *
     * @param url
     */
    private void saveImg(final String url) {
        try {
            PermissionManager.get().request((BaseActivity) act, new
                    IPermissionCallBack() {
                        @Override
                        public void onGranted(boolean isAlreadyDef) {
                            download(url);
                        }

                        @Override
                        public void onDenied(List<String> neverAskPerms) {
                            PermissionManager.showAdvice(getContext(),
                                    "保存图片需要开启存储权限");
                        }

                        @Override
                        public void onElse(List<String> deniedPerms, List<String>
                                neverAskPerms) {

                        }
                    }, Permission.STORAGE_READE, Permission.STORAGE_WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO  取缓存，待优化，兼容性测试url为内部地址时会ANR，之前已下载，需要修改为保存到系统相册并在下载/保存时遍历是否已经存在该图片 保存在zjxw目录下5。4.0版本改

    /**
     * @param url 下载图片
     */
    private void download(String url) {
        //图片特殊处理
        if (!TextUtils.isEmpty(url) && url.contains("?w=")) {
            url = url.split("[?]")[0];
        }
        try {
            DownloadUtil.get()
                    .setDir(PathUtil.getImagePath())
                    .setListener(new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onLoading(int progress) {

                        }

                        @Override
                        public void onSuccess(String path) {
                            dismissFragmentDialog();
                            T.showShort(getContext(), "保存成功");
                        }

                        @Override
                        public void onFail(String err) {
                            dismissFragmentDialog();
                            T.showShort(getContext(), "保存失败");
                        }
                    })
                    .download(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 二维码跳转
     */
    private void scanerImg(String imgUrl) {
        if (imgUrl.startsWith("http") || imgUrl.startsWith("https")) {
            if (imgUrl.contains("weixin.qq.com")) {
                Nav.with(getContext()).toPath("http://weixin.qq.com/");//链接
            } else {
                Nav.with(getContext()).toPath(imgUrl);//链接
            }
        } else {
            //文本
            Bundle bundle = new Bundle();
            bundle.putString(IKey.SCANER_TEXT, imgUrl);
            Nav.with(getContext()).setExtras(bundle).toPath("/ui/ScanerResultActivity");
        }
        dismissFragmentDialog();
    }

    /**
     * 取消加载框
     */
    private void dismissFragmentDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        dismissFragmentDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 长按保存fuckWM
     */
    private void longPressAnalytics() {
        new Analytics.AnalyticsBuilder(getContext(), "A0025", "A0025","PictureRelatedOperation",false)
                .setEvenName("长按保存")
                .setObjectID(mlfid + "")
                .setObjectType(ObjectType.NewsType)
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("mediaURL", imgUrl)
                        .toString()).pageType("新闻详情页")
                .operationType("长按保存")
                .build()
                .send();
    }

}
