package com.zjrb.zjxw.detailproject.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trs.tasdk.entity.ObjectType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DetailShareBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.RouteManager;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.DetailShareAdapter;
import com.zjrb.zjxw.detailproject.task.DraftCollectTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.constant.C;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.db.ThemeMode;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.share.BaseDialogFragment;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;

/**
 * Created by wanglinjie on 2017/9/21.
 */

public class MoreDialog extends BaseDialogFragment implements RadioGroup.OnCheckedChangeListener, OnItemClickListener {

    protected Dialog dialog;
    @BindView(R2.id.iv_module_core_more_collect)
    ImageView ivCollect;
    @BindView(R2.id.rb_module_core_more_set_font_size_small)
    RadioButton rbSmall;
    @BindView(R2.id.rb_module_core_more_set_font_size_normal)
    RadioButton rbNormal;
    @BindView(R2.id.rb_module_core_more_set_font_size_big)
    RadioButton rbBig;
    @BindView(R2.id.rg_module_core_more_set_font_size)
    RadioGroup rgSetFontSize;
    @BindView(R2.id.tv_module_core_more_set_font_size_preview)
    TextView tvPreview;
    @BindView(R2.id.gridlist)
    RecyclerView mRecyleView;
    @BindView(R2.id.ry_font)
    RelativeLayout fontRelativeLayout;
    @BindView(R2.id.ly_preview)
    LinearLayout previewLineLayout;

    private DraftDetailBean mBean;
    /**
     * @return
     */
    private static MoreDialog fragment = null;

    private IWebViewDN callback;
    private IWebViewTextSize callback_2;

    /**
     * 分享适配器
     */
    private DetailShareAdapter mAdapter;
    /**
     * 分享数据列表
     */
    private List<DetailShareBean> mListData;

    /**
     * js夜间模式回调
     */
    public interface IWebViewDN {

        void onChangeTheme();

    }

    /**
     * webview文字颜色
     */
    public interface IWebViewTextSize {
        void onChangeTextSize(final float textSize);
    }

    /**
     * 设置webview回调
     *
     * @param callback
     * @param callback_2
     * @return
     */
    public MoreDialog setWebViewCallBack(IWebViewDN callback, IWebViewTextSize callback_2) {
        this.callback = callback;
        this.callback_2 = callback_2;
        return fragment;
    }


    public static MoreDialog newInstance(DraftDetailBean bean) {
        fragment = new MoreDialog();
        Bundle args = new Bundle();
        args.putSerializable(IKey.FRAGMENT_ARGS, bean);
        fragment.setArguments(args);
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
        View view = View.inflate(getContext(), R.layout.module_core_dialog_more_layout, null);
        ButterKnife.bind(this, view);

        //红船号稿件
        LinearLayoutManager managerFollow = new LinearLayoutManager(UIUtils.getContext());
        managerFollow.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyleView.setLayoutManager(managerFollow);
        if (mBean != null && mBean.getArticle() != null && mBean.getArticle().getDoc_type() == 10) {
            fontRelativeLayout.setVisibility(View.GONE);
            previewLineLayout.setVisibility(View.GONE);
            mRecyleView.setVisibility(View.VISIBLE);
            //初始化分享
            initShareBean();
        } else {
            fontRelativeLayout.setVisibility(View.VISIBLE);
            previewLineLayout.setVisibility(View.VISIBLE);
            mRecyleView.setVisibility(View.GONE);
        }

        rgSetFontSize.setOnCheckedChangeListener(this);
        if (mBean.getArticle().isFollowed()) {
            ivCollect.getDrawable().setLevel(UIUtils.getApp().getResources().getInteger(R.integer.level_collect_on));
        } else {
            ivCollect.getDrawable().setLevel(UIUtils.getApp().getResources().getInteger(R.integer.level_collect_off));
        }
        initPreview();
        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        initWindow();
        return dialog;
    }


    @Override
    public void onItemClick(View itemView, int position) {
        if (ClickTracker.isDoubleClick()) return;
        if (mBean != null && mBean.getArticle() != null && !TextUtils.isEmpty(mBean.getArticle().getUrl())) {
            //红船号分享专用bean
            DraftDetailBean.ArticleBean article = mBean.getArticle();
            OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                    .setObjectID(article.getGuid() + "")
                    .setObjectName(article.getDoc_title())
                    .setObjectType(ObjectType.NewsType)
                    .setClassifyID(article.getChannel_id() + "")
                    .setClassifyName(article.getChannel_name())
                    .setPageType("红船号详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", article.getColumn_id() + "")
                            .put("subject", "")
                            .toString())
                    .setSelfobjectID(article.getId() + "");

            UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                    .setSingle(true)
                    .setAnalyticsBean(bean)
                    .setArticleId(article.getId() + "")
                    .setImgUri(article.getFirstPic())
                    .setTextContent(article.getSummary())
                    .setTitle(article.getDoc_title())
                    .setPlatform(mListData.get(position).getPlatform())
                    .setTargetUrl(article.getUrl()).setEventName("NewsShare")
                    .setShareType("文章"));

        }
    }

    /**
     * 初始化滚动列表数据
     */
    private void initShareBean() {
        if (mListData == null) {
            mListData = new ArrayList<>();
            mListData.add(new DetailShareBean("微信", SHARE_MEDIA.WEIXIN));
            mListData.add(new DetailShareBean("朋友圈", SHARE_MEDIA.WEIXIN_CIRCLE));
            mListData.add(new DetailShareBean("钉钉", SHARE_MEDIA.DINGTALK));
            mListData.add(new DetailShareBean("QQ", SHARE_MEDIA.QQ));
            mListData.add(new DetailShareBean("微博", SHARE_MEDIA.SINA));
            mListData.add(new DetailShareBean("QQ空间", SHARE_MEDIA.QZONE));
        }

        mAdapter = new DetailShareAdapter(mListData);
        mAdapter.setOnItemClickListener(this);
        mRecyleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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


    public void dismissFragmentDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 弹框预览效果
     */
    private void initPreview() {
        float size = SettingBiz.get().getHtmlFontScale();
        initArticleTextSize(size);
    }

    /**
     * 初始化文章字体大小
     *
     * @param textSize
     */
    private void initArticleTextSize(float textSize) {
        if (textSize == C.FONT_SCALE_LARGE) {
            rbBig.setChecked(true);
            tvPreview.setScaleX(C.FONT_SCALE_LARGE);
            tvPreview.setScaleY(C.FONT_SCALE_LARGE);
        } else if (textSize == C.FONT_SCALE_STANDARD) {
            rbNormal.setChecked(true);
            tvPreview.setScaleX(C.FONT_SCALE_STANDARD);
            tvPreview.setScaleY(C.FONT_SCALE_STANDARD);
        } else {
            rbSmall.setChecked(true);
            tvPreview.setScaleX(C.FONT_SCALE_SMALL);
            tvPreview.setScaleY(C.FONT_SCALE_SMALL);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dismissFragmentDialog();
    }

    @OnClick({R2.id.ll_module_core_more_collect, R2.id.ll_module_core_more_night, R2.id.ll_module_core_more_feed_back, R2.id.btn_dialog_close})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        int i = v.getId();
        //收藏按钮
        if (i == R.id.ll_module_core_more_collect) {
            if (mBean != null && mBean.getArticle() != null) {
                DraftDetailBean.ArticleBean article = mBean.getArticle();
                if (!mBean.getArticle().isFollowed()) {
                    new Analytics.AnalyticsBuilder(getContext(), "A0024", "A0024", "Collect", false)
                            .setEvenName("点击收藏")
                            .setObjectID(getMlfID(article))
                            .setObjectName(article.getDoc_title())
                            .setObjectType(ObjectType.NewsType)
                            .setClassifyID(article.getChannel_id())
                            .setClassifyName(article.getChannel_name())
                            .setPageType("新闻详情页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("relatedColumn", article.getColumn_id() + "")
                                    .put("subject", "")
                                    .toString())
                            .setSelfObjectID(article.getId() + "").newsID(mBean.getArticle().getMlf_id() + "")
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
                            .setObjectID(getMlfID(article))
                            .setObjectName(article.getDoc_title())
                            .setObjectType(ObjectType.NewsType)
                            .setClassifyID(article.getChannel_id())
                            .setClassifyName(article.getChannel_name())
                            .setPageType("新闻详情页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("relatedColumn", article.getColumn_id() + "")
                                    .put("subject", "")
                                    .toString())
                            .setSelfObjectID(article.getId() + "").newsID(mBean.getArticle().getMlf_id() + "")
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
            //夜间模式
        } else if (i == R.id.ll_module_core_more_night) {
            ThemeMode.setUiMode(!ThemeMode.isNightMode());
            if (callback != null) {
                callback.onChangeTheme();
            }
            //点击开启夜间模式
            if (!ThemeMode.isNightMode()) {
                if (mBean != null & mBean.getArticle() != null) {
                    DraftDetailBean.ArticleBean article = mBean.getArticle();
                    new Analytics.AnalyticsBuilder(getContext(), "700020", "700020", "WithStatusElementClick", false)
                            .setEvenName("夜间模式设置")
                            .setEventDetail("关")
                            .setObjectID(getMlfID(article))
                            .setObjectName(article.getDoc_title())
                            .setObjectType(ObjectType.NewsType)
                            .setClassifyID(article.getChannel_id())
                            .setClassifyName(article.getChannel_name())
                            .setPageType("新闻详情页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("relatedColumn", article.getColumn_id() + "")
                                    .put("subject", "")
                                    .toString())
                            .setSelfObjectID(article.getId() + "").pageType("新闻详情页")
                            .clickTabName("夜间模式设置")
                            .elementStatus("关")
                            .build()
                            .send();
                }
            } else {//关闭夜间模式
                if (mBean != null & mBean.getArticle() != null) {
                    DraftDetailBean.ArticleBean article = mBean.getArticle();
                    new Analytics.AnalyticsBuilder(getContext(), "700020", "700020", "WithStatusElementClick", false)
                            .setEvenName("夜间模式设置")
                            .setEventDetail("开")
                            .setObjectID(getMlfID(article))
                            .setObjectName(article.getDoc_title())
                            .setObjectType(ObjectType.NewsType)
                            .setClassifyID(article.getChannel_id())
                            .setClassifyName(article.getChannel_name())
                            .setPageType("新闻详情页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("relatedColumn", article.getColumn_id() + "")
                                    .put("subject", "")
                                    .toString())
                            .setSelfObjectID(article.getId() + "").pageType("新闻详情页")
                            .clickTabName("夜间模式设置")
                            .elementStatus("开")
                            .build()
                            .send();
                }
            }
            dismissFragmentDialog();
            //返回键
        } else if (i == R.id.ll_module_core_more_feed_back) {
            if (mBean != null & mBean.getArticle() != null) {
                DraftDetailBean.ArticleBean article = mBean.getArticle();
                new Analytics.AnalyticsBuilder(getContext(), "800007", "800007", "AppTabClick", false)
                        .setEvenName("点击反馈问题")
                        .setObjectID(getMlfID(article))
                        .setObjectName(article.getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(article.getChannel_id())
                        .setClassifyName(article.getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", article.getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(article.getId() + "").pageType("新闻详情页")
                        .clickTabName("反馈问题")
                        .build()
                        .send();
            }
            Nav.with(UIUtils.getContext()).toPath(RouteManager.FEED_BACK);
            dismissFragmentDialog();

        } else if (i == R.id.btn_dialog_close) {
            dismissFragmentDialog();
        }
    }

    /**
     * 稿件收藏
     */
    private void newsTopicCollect() {
        new DraftCollectTask(new LoadingCallBack<Void>() {

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

                dismissFragmentDialog();

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
                dismissFragmentDialog();
            }

        }).setTag(this).exe(mBean.getArticle().getId(), !mBean.getArticle().isFollowed(), mBean.getArticle().getUrl());
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.rb_module_core_more_set_font_size_small) {
            tvPreview.setScaleX(C.FONT_SCALE_SMALL);
            tvPreview.setScaleY(C.FONT_SCALE_SMALL);
            SettingBiz.get().setHtmlFontScale(C.FONT_SCALE_SMALL);
            if (callback_2 != null) {
                callback_2.onChangeTextSize(C.FONT_SCALE_SMALL);
            }
            if (mBean != null && mBean.getArticle() != null) {
                DraftDetailBean.ArticleBean article = mBean.getArticle();
                new Analytics.AnalyticsBuilder(getContext(), "700022", "700022", "FontSizeSet", false)
                        .setEvenName("点击字体调节")
                        .setObjectID(getMlfID(article))
                        .setObjectName(article.getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(article.getChannel_id())
                        .setClassifyName(article.getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", article.getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(article.getId() + "")
                        .setEventDetail("小")
                        .pageType("新闻详情页")
                        .clickTabName("字体大小设置")
                        .elementStatus("小")
                        .build()
                        .send();
            }

        } else if (checkedId == R.id.rb_module_core_more_set_font_size_normal) {
            tvPreview.setScaleX(C.FONT_SCALE_STANDARD);
            tvPreview.setScaleY(C.FONT_SCALE_STANDARD);
            SettingBiz.get().setHtmlFontScale(C.FONT_SCALE_STANDARD);
            if (callback_2 != null) {
                callback_2.onChangeTextSize(C.FONT_SCALE_STANDARD);
            }
            if (mBean != null && mBean.getArticle() != null) {
                DraftDetailBean.ArticleBean article = mBean.getArticle();
                new Analytics.AnalyticsBuilder(getContext(), "700022", "700022", "FontSizeSet", false)
                        .setEvenName("点击字体调节")
                        .setObjectID(getMlfID(article))
                        .setObjectName(article.getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(article.getChannel_id())
                        .setClassifyName(article.getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", article.getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(article.getId() + "")
                        .setEventDetail("中").pageType("新闻详情页")
                        .clickTabName("字体大小设置")
                        .elementStatus("中")
                        .build()
                        .send();
            }

        } else if (checkedId == R.id.rb_module_core_more_set_font_size_big) {
            tvPreview.setScaleX(C.FONT_SCALE_LARGE);
            tvPreview.setScaleY(C.FONT_SCALE_LARGE);
            SettingBiz.get().setHtmlFontScale(C.FONT_SCALE_LARGE);
            if (callback_2 != null) {
                callback_2.onChangeTextSize(C.FONT_SCALE_LARGE);
            }
            if (mBean != null && mBean.getArticle() != null) {
                DraftDetailBean.ArticleBean article = mBean.getArticle();
                new Analytics.AnalyticsBuilder(getContext(), "700022", "700022", "FontSizeSet", false)
                        .setEvenName("点击字体调节")
                        .setObjectID(getMlfID(article))
                        .setObjectName(article.getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(article.getChannel_id())
                        .setClassifyName(article.getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", article.getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(article.getId() + "")
                        .setEventDetail("大").pageType("新闻详情页")
                        .clickTabName("字体大小设置")
                        .elementStatus("大")
                        .build()
                        .send();
            }
        }
    }

    /**
     * 封装方法，获取mlf_id
     *
     * @param article
     * @return
     */
    private String getMlfID(DraftDetailBean.ArticleBean article) {
        String mlfId = String.valueOf(article.getMlf_id());
        if (article.getDoc_type() == 10) {
            mlfId = String.valueOf(article.getGuid());
        }
        return mlfId;
    }

}
