package com.zjrb.zjxw.detailproject.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.trs.tasdk.entity.ObjectType;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zjrb.core.base.LifecycleActivity;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.load.LoadingIndicatorDialog;
import com.zjrb.core.permission.IPermissionCallBack;
import com.zjrb.core.permission.IPermissionOperate;
import com.zjrb.core.permission.Permission;
import com.zjrb.core.permission.PermissionManager;
import com.zjrb.core.utils.ImageUtils;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.model.BaseData;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.network.compatible.APIExpandCallBack;
import cn.daily.news.biz.core.network.task.ArticShareTask;
import cn.daily.news.biz.core.network.task.DraftCollectTask;
import cn.daily.news.biz.core.share.BaseDialogFragment;
import cn.daily.news.biz.core.share.ShareOnResultCallback;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.utils.RouteManager;
import cn.daily.news.biz.core.web.JsMultiInterfaceImp;

/**
 * Created by wanglinjie on 2017/9/21.
 */

public class MoreDialogLink extends BaseDialogFragment {

    protected Dialog dialog;
    @BindView(R2.id.iv_module_core_more_collect)
    ImageView ivCollect;
    @BindView(R2.id.ll_module_core_me_friend)
    LinearLayout llModuleCoreMeFriend;
    @BindView(R2.id.ll_module_core_me_wechat)
    LinearLayout llModuleCoreMeWechat;
    @BindView(R2.id.ll_module_core_me_qq)
    LinearLayout llModuleCoreMeQq;
    @BindView(R2.id.ll_module_core_me_space)
    LinearLayout llModuleCoreMeSpace;
    @BindView(R2.id.ll_module_core_me_sina)
    LinearLayout llModuleCoreMeSina;
    @BindView(R2.id.ll_module_core_me_dingding)
    LinearLayout llModuleCoreMeDingDing;

    private DraftDetailBean mBean;
    private UmengShareBean mBeanShare;

    private MoreDialog.IWebViewDN callback;

    /**
     * @return
     */
    private static MoreDialogLink fragment = null;


    public MoreDialogLink setShareBean(UmengShareBean mBeanShare) {
        this.mBeanShare = mBeanShare;
        return this;

    }

    public static MoreDialogLink newInstance(DraftDetailBean bean) {
        fragment = new MoreDialogLink();
        Bundle args = new Bundle();
        args.putSerializable(IKey.FRAGMENT_ARGS, bean);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 设置webview回调
     *
     * @param callback
     * @return
     */
    public MoreDialogLink setWebViewCallBack(MoreDialog.IWebViewDN callback) {
        this.callback = callback;
        return fragment;
    }

    @Override
    public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mBean = (DraftDetailBean) getArguments().getSerializable(IKey.FRAGMENT_ARGS);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.BottomDialog);
        View view = View.inflate(getContext(), R.layout.module_detail_dialog_more_link_layout, null);
        ButterKnife.bind(this, view);
        if (mBean.getArticle().isFollowed()) {
            ivCollect.getDrawable().setLevel(UIUtils.getApp().getResources().getInteger(R.integer.level_collect_on));
        } else {
            ivCollect.getDrawable().setLevel(UIUtils.getApp().getResources().getInteger(R.integer.level_collect_off));
        }
        initShareView();
        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        initWindow();
        return dialog;
    }

    private void initShareView() {
        setShareItemWidth(llModuleCoreMeWechat);
        setShareItemWidth(llModuleCoreMeFriend);
        setShareItemWidth(llModuleCoreMeQq);
        setShareItemWidth(llModuleCoreMeSina);
        setShareItemWidth(llModuleCoreMeSpace);
        setShareItemWidth(llModuleCoreMeDingDing);
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

    private void setShareItemWidth(LinearLayout layout) {
        if (layout == null) {
            return;
        }
        LinearLayout.LayoutParams weParam = (LinearLayout.LayoutParams) layout.getLayoutParams();
        weParam.width = (int) (UIUtils.getScreenW() * ((float) 2 / 11));
        layout.setLayoutParams(weParam);
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        if (null != dialog) {
            dialog.getWindow().setLayout(-1, -2);
        }
    }


    public void dismissFragmentDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dismissAllDialog();
    }

    @OnClick({R2.id.ll_module_core_more_collect, R2.id.ll_module_core_more_feed_back, R2.id.btn_dialog_close
            , R2.id.ll_module_core_me_friend, R2.id.ll_module_core_me_wechat, R2.id.ll_module_core_me_qq, R2.id.ll_module_core_me_space, R2.id.ll_module_core_me_sina, R2.id.ll_module_core_me_dingding})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        int i = v.getId();
        if (i == R.id.ll_module_core_more_collect) {
            if (mBean != null && mBean.getArticle() != null) {
                if (!mBean.getArticle().isFollowed()) {
                    new Analytics.AnalyticsBuilder(getContext(), "A0024", "A0024", "Collect", false)
                            .setEvenName("点击收藏")
                            .setObjectID(mBean.getArticle().getMlf_id() + "")
                            .setObjectName(mBean.getArticle().getDoc_title())
                            .setObjectType(ObjectType.NewsType)
                            .setClassifyID(mBean.getArticle().getChannel_id())
                            .setClassifyName(mBean.getArticle().getChannel_name())
                            .setPageType("新闻详情页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("relatedColumn", mBean.getArticle().getColumn_id() + "")
                                    .put("subject", "")
                                    .toString())
                            .setSelfObjectID(mBean.getArticle().getId() + "")
                            .newsID(mBean.getArticle().getMlf_id() + "")
                            .selfNewsID(mBean.getArticle().getId() + "")
                            .newsTitle(mBean.getArticle().getDoc_title())
                            .selfChannelID(mBean.getArticle().getChannel_id())
                            .channelName(mBean.getArticle().getChannel_name())
                            .pageType("新闻详情页")
                            .operationType("收藏")
                            .build()
                            .send();
                } else {
                    new Analytics.AnalyticsBuilder(getContext(), "A0124", "A0124", "Collect", false)
                            .setEvenName("取消收藏")
                            .setObjectID(mBean.getArticle().getMlf_id() + "")
                            .setObjectName(mBean.getArticle().getDoc_title())
                            .setObjectType(ObjectType.NewsType)
                            .setClassifyID(mBean.getArticle().getChannel_id())
                            .setClassifyName(mBean.getArticle().getChannel_name())
                            .setPageType("新闻详情页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("relatedColumn", mBean.getArticle().getColumn_id() + "")
                                    .put("subject", "")
                                    .toString())
                            .setSelfObjectID(mBean.getArticle().getId() + "").newsID(mBean.getArticle().getMlf_id() + "")
                            .selfNewsID(mBean.getArticle().getId() + "")
                            .newsTitle(mBean.getArticle().getDoc_title())
                            .selfChannelID(mBean.getArticle().getChannel_id())
                            .channelName(mBean.getArticle().getChannel_name())
                            .pageType("新闻详情页")
                            .operationType("取消收藏")
                            .build()
                            .send();
                }
            }

            newsTopicCollect();
        } else if (i == R.id.ll_module_core_more_feed_back) {
            if (mBean != null & mBean.getArticle() != null) {
                new Analytics.AnalyticsBuilder(getContext(), "800007", "800007", "AppTabClick", false)
                        .setEvenName("点击反馈问题")
                        .setObjectID(mBean.getArticle().getMlf_id() + "")
                        .setObjectName(mBean.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mBean.getArticle().getChannel_id())
                        .setClassifyName(mBean.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mBean.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(mBean.getArticle().getId() + "")
                        .pageType("新闻详情页")
                        .clickTabName("反馈问题")
                        .build()
                        .send();
            }
            Nav.with(UIUtils.getContext()).toPath(RouteManager.FEED_BACK);
            dismissAllDialog();

        } else if (i == R.id.btn_dialog_close) {
            dismissAllDialog();

        } else if (i == R.id.ll_module_core_me_friend) {
            checkShare(SHARE_MEDIA.WEIXIN_CIRCLE);

        } else if (i == R.id.ll_module_core_me_wechat) {
            checkShare(SHARE_MEDIA.WEIXIN);

        } else if (i == R.id.ll_module_core_me_qq) {
            checkShare(SHARE_MEDIA.QQ);

        } else if (i == R.id.ll_module_core_me_space) {
            checkShare(SHARE_MEDIA.QZONE);

        } else if (i == R.id.ll_module_core_me_sina) {
            checkShare(SHARE_MEDIA.SINA);

        } else if (i == R.id.ll_module_core_me_dingding) {
            checkShare(SHARE_MEDIA.DINGTALK);
        }
    }

    /**
     * 稿件收藏
     */
    private void newsTopicCollect() {
        new DraftCollectTask(new APIExpandCallBack<Void>() {

            @Override
            public void onSuccess(Void baseInnerData) {
                if (!mBean.getArticle().isFollowed()) {
                    ivCollect.getDrawable().setLevel(UIUtils.getApp().getResources().getInteger(R.integer.level_collect_on));
                    mBean.getArticle().setFollowed(true);
                    T.showShort(UIUtils.getApp(), "已收藏成功");
                } else {
                    ivCollect.getDrawable().setLevel(UIUtils.getApp().getResources().getInteger(R.integer.level_collect_off));
                    mBean.getArticle().setFollowed(false);
                    T.showShort(UIUtils.getApp(), "已取消收藏");
                }

                dismissAllDialog();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                //已收藏成功
                if (errCode == 50013) {
                    ivCollect.getDrawable().setLevel(UIUtils.getApp().getResources().getInteger(R.integer.level_collect_on));
                    mBean.getArticle().setFollowed(true);
                    T.showShort(UIUtils.getApp(), "已收藏成功");
                } else {
                    T.showShort(UIUtils.getApp(), errMsg);
                }
                dismissAllDialog();
            }

        }).setTag(this).exe(mBean.getArticle().getId(), !mBean.getArticle().isFollowed(), mBean.getArticle().getUrl());
    }

    private UMShareAPI mShareAPI;

    /**
     * 正在加载
     */
    private LoadingIndicatorDialog shareDialog;

    /**
     * 分享加载框，分享成功后关闭
     */
    public void getShareDialog() {
        Activity activity = UIUtils.getActivity();
        shareDialog = new LoadingIndicatorDialog(activity);
        if (!activity.isDestroyed()) {
            shareDialog.show();
        }
    }

    /**
     * 关闭dialog
     */
    public void dismissLoadingDialog() {
        if (shareDialog != null && shareDialog.isShowing()) {
            shareDialog.finish();
        }
    }


    /**
     * 关闭所有dialog
     */
    public void dismissAllDialog() {
        dismissFragmentDialog();
        dismissLoadingDialog();
    }

    /**
     * 校验分享
     *
     * @param platform
     */
    private void checkShare(final SHARE_MEDIA platform) {
        if (!checkInstall(platform)) {
            return;
        }
        if (!ClickTracker.isDoubleClick()) {
            if (EMUIUtils.isEMUI() || platform == SHARE_MEDIA.QZONE || platform == SHARE_MEDIA.QQ) {
                PermissionManager.get().request((IPermissionOperate) UIUtils.getActivity(), new IPermissionCallBack() {
                    @Override
                    public void onGranted(boolean isAlreadyDef) {
                        if (mBeanShare != null) {
                            umengShare(platform, mBeanShare);
                        }
                    }

                    @Override
                    public void onDenied(List<String> neverAskPerms) {
                        T.showShortNow(UIUtils.getApp(), "权限被拒绝");
                    }

                    @Override
                    public void onElse(List<String> deniedPerms, List<String> neverAskPerms) {
                    }
                }, Permission.STORAGE_READE, Permission.STORAGE_WRITE);
            } else {
                if (mBeanShare != null) {
                    umengShare(platform, mBeanShare);
                }
            }
        }
    }

    /**
     * 设置网脉埋点
     */
    private void setAnalytics(SHARE_MEDIA share_media, boolean isTrue) {
        if (mBeanShare != null && mBeanShare.getAnalyticsBean() != null) {
            String eventName = "";
            String WMCode = "";
            String UMCode = "";
            String eventDetail = "";
            String shareClass = "文章";
            if (share_media == SHARE_MEDIA.WEIXIN) {
                WMCode = "A0022";
                UMCode = "60003";
                eventName = "微信分享；微信分享成功";
                eventDetail = "微信";
            } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                WMCode = "A0022";
                UMCode = "60004";
                eventName = "朋友圈分享；朋友圈分享成功";
                eventDetail = "朋友圈";
            } else if (share_media == SHARE_MEDIA.QQ) {
                WMCode = "A0022";
                UMCode = "800020";
                eventName = "QQ分享；QQ分享成功";
                eventDetail = "QQ";
            } else if (share_media == SHARE_MEDIA.SINA) {
                WMCode = "A0022";
                UMCode = "60001";
                eventName = "微博分享；微博分享成功";
                eventDetail = "微博";
            } else if (share_media == SHARE_MEDIA.QZONE) {
                WMCode = "A0022";
                UMCode = "800019";
                eventName = "QQ空间分享;QQ空间分享成功";
                eventDetail = "QQ空间";
            } else if (share_media == SHARE_MEDIA.DINGTALK) { // 钉钉
                WMCode = "A0022";
                UMCode = "800032";
                eventName = "钉钉分享;钉钉分享成功";
                eventDetail = "钉钉";
            }
            if (!TextUtils.isEmpty(mBeanShare.getAnalyticsBean().getPageType()) && mBeanShare.getAnalyticsBean().getPageType().equals("官员页面")) {
                shareClass = "官员";
            }
            new Analytics.AnalyticsBuilder(getContext(), WMCode, UMCode, "NewsShare", false)
                    .setEvenName(eventName)
                    .setObjectID(mBeanShare.getAnalyticsBean().getObjectID())
                    .setObjectName(mBeanShare.getAnalyticsBean().getObjectName())
                    .setObjectType(mBeanShare.getAnalyticsBean().getObjectType())
                    .setClassifyID(mBeanShare.getAnalyticsBean().getClassifyID())
                    .setClassifyName(mBeanShare.getAnalyticsBean().getClassifyName())
                    .setPageType(mBeanShare.getAnalyticsBean().getPageType())
                    .setOtherInfo(mBeanShare.getAnalyticsBean().getOtherInfo())
                    .setSelfObjectID(mBeanShare.getAnalyticsBean().getSelfobjectID())
                    .setIscuccesee(isTrue)
                    .setEventDetail(eventDetail)
                    .newsID(mBeanShare.getAnalyticsBean().getObjectID())
                    .selfNewsID(mBeanShare.getArticleId())
                    .newsTitle(mBeanShare.getTitle())
                    .selfChannelID(mBeanShare.getAnalyticsBean().getClassifyID())
                    .channelName(mBeanShare.getAnalyticsBean().getClassifyName())
                    .pageType(mBeanShare.getAnalyticsBean().getPageType())
                    .shareType(eventDetail)
                    .shareClass(shareClass)
                    .build()
                    .send();
        }
    }

    /**
     * 分享回调
     */
    private UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            dismissAllDialog();
            T.showShortNow(UIUtils.getApp(), "分享成功");
            setAnalytics(share_media, true);
            //稿件分享成功后，登录用户获取积分
            new ArticShareTask(new LoadingCallBack<BaseData>() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onError(String errMsg, int errCode) {

                }

                @Override
                public void onSuccess(BaseData data) {

                }
            }).setTag(this).exe(mBeanShare.getArticleId() != null ?
                    mBeanShare.getArticleId() : "", mBeanShare.getTargetUrl());
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            setAnalytics(share_media, false);
            T.showShortNow(UIUtils.getApp(), "分享失败");
            dismissAllDialog();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            dismissAllDialog();
        }
    };


    /**
     * 是否使用js分享  当js下发非图片分享时,只要标题和链接有一个为空,就用原生分享;当下发的是图片分享时且图片链接不为空,不需要判断标题和链接是否为空
     *
     * @param mJsShareBean
     * @return
     */
    private boolean isUseJsShare(UmengShareBean mJsShareBean) {
        if (mJsShareBean == null) {
            return false;
        }
        if (!mJsShareBean.isPicShare()) { // 非图片分享,标题及链接都不为空时使用js分享,当有一个为空时使用原生分享
            return !TextUtils.isEmpty(mJsShareBean.getTargetUrl()) && !TextUtils.isEmpty(mJsShareBean.getTitle());
        } else { // 图片分享,当下发的图片链接不为空时,使用js分享
            return !TextUtils.isEmpty(mJsShareBean.getImgUri());
        }
    }

    /**
     * @param bean 分享的信息
     */
    public void umengShare(final SHARE_MEDIA platform, @NonNull final UmengShareBean bean) {
        UmengShareBean mJsShareBean = SPHelper.get().getObject(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN); // 获取js下发的分享信息
        if (isUseJsShare(mJsShareBean)) {
            bean.setImgUri(mJsShareBean.getImgUri())
                    .setPicShare(mJsShareBean.isPicShare())
                    .setTextContent(mJsShareBean.getTextContent())
                    .setTitle(mJsShareBean.getTitle())
                    .setTargetUrl(mJsShareBean.getTargetUrl());
        }
        if (!bean.isPicShare()) { // 非图片分享
            if (TextUtils.isEmpty(bean.getTargetUrl())) { // 跟ios统一,targetUrl为空,默认跳官网
                bean.setTargetUrl("http://zj.zjol.com.cn");
            }
            UMWeb web = new UMWeb(bean.getTargetUrl());
            if (!TextUtils.isEmpty(bean.getTitle())) {
                web.setTitle(bean.getTitle());//标题
            } else {
                web.setTitle("看浙江新闻，拿积分好礼");//标题
            }
            //分享图片
            if (!TextUtils.isEmpty(bean.getImgUri())) {
                String url;
                if (bean.getImgUri().contains("?w=") || bean.getImgUri().contains("?width=")) {
                    url = bean.getImgUri().split("[?]")[0];
                } else {
                    url = bean.getImgUri();
                }
                web.setThumb(new UMImage(UIUtils.getContext(), url));  //缩略图
            } else {
                web.setThumb(new UMImage(UIUtils.getContext(), R.mipmap.ic_share));
            }
            //分享描述
            if (!TextUtils.isEmpty(bean.getTextContent())) {
                web.setDescription(bean.getTextContent());
            } else {
                web.setDescription(UIUtils.getContext().getString(R.string.module_core_share_content_from));
            }

            if (umShareListener != null) {
                getShareDialog();
                if (platform == SHARE_MEDIA.QQ || platform == SHARE_MEDIA.QZONE) {
                    if (UIUtils.getActivity() instanceof LifecycleActivity) {
                        ((LifecycleActivity) UIUtils.getActivity())
                                .registerActivityCallbacks(ShareOnResultCallback.get());
                    }
                }
                new ShareAction(UIUtils.getActivity())
                        .setPlatform(platform)
                        .withText(bean.getTextContent())
                        .withMedia(web)
                        .setCallback(umShareListener)
                        .share();
            }
        } else { // 图片分享
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UMImage image = setUMImage(bean);
                    if (image != null && umShareListener != null) {
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getShareDialog();
                                }
                            });
                        }
                        if (platform == SHARE_MEDIA.QQ || platform == SHARE_MEDIA.QZONE) {
                            if (UIUtils.getActivity() instanceof LifecycleActivity) {
                                ((LifecycleActivity) UIUtils.getActivity())
                                        .registerActivityCallbacks(ShareOnResultCallback.get());
                            }
                        }
                        new ShareAction(UIUtils.getActivity())
                                .setPlatform(platform)
                                .withMedia(image)
                                .setCallback(umShareListener)
                                .share();
                    }
                }
            }).start();
        }

    }

    private UMImage setUMImage(UmengShareBean bean) {
        UMImage umImage = null;
        if (bean.getBimtap() != null) { // bitmap图片分享
            umImage = new UMImage(UIUtils.getContext(), bean.getBimtap());
            umImage.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
            return umImage;
        } else if (!TextUtils.isEmpty(bean.getImgUri())) { // 网络图片分享
            umImage = new UMImage(UIUtils.getContext(), bean.getImgUri());
            umImage.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
            return umImage;
        } else if (bean.getPicId() != 0) { // 资源文件图片分享
            umImage = new UMImage(UIUtils.getContext(), ImageUtils.getBitmapById(bean.getPicId()));
            umImage.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
            return umImage;
        }
        return umImage;
    }


    /**
     * 检测是否安装微信等
     */
    private boolean checkInstall(SHARE_MEDIA platform) {
        if (mShareAPI == null) {
            mShareAPI = UMShareAPI.get(UIUtils.getContext());
        }
        if (platform == SHARE_MEDIA.WEIXIN || platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
            if (mShareAPI != null && !mShareAPI.isInstall(UIUtils.getActivity(), SHARE_MEDIA.WEIXIN)) {
                T.showShortNow(UIUtils.getApp(), "未安装微信客户端");
                return false;
            }
        } else if (platform == SHARE_MEDIA.QQ || platform == SHARE_MEDIA.QZONE) {
            if (mShareAPI != null && !mShareAPI.isInstall(UIUtils.getActivity(), SHARE_MEDIA.QQ)) {
                T.showShortNow(UIUtils.getApp(), "未安装QQ客户端");
                return false;
            }
        } else if (platform == SHARE_MEDIA.DINGTALK) {
            if (mShareAPI != null && !mShareAPI.isInstall(UIUtils.getActivity(), SHARE_MEDIA.DINGTALK)) {
                T.showShortNow(UIUtils.getApp(), "未安装钉钉客户端");
                return false;
            }
        }
        dismissAllDialog();
        return true;
    }
}
