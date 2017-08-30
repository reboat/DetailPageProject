package com.zjrb.zjxw.detailproject.utils;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.zjrb.coreprojectlibrary.utils.T;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import java.math.BigDecimal;

/**
 * 业务相关的逻辑处理工具
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class BizUtils {

    public static void setDocType(int docType, TextView tv, String tag) {
        if (tv == null) return;
        if (TextUtils.isEmpty(tag) || "无".equals(tag.trim())) {
            tv.setVisibility(View.INVISIBLE);
            return;
        }
        String tagName = tag.trim();
        tv.setText(tagName);
        tv.setVisibility(View.VISIBLE);
//        if ("突发".equals(tagName)) {
//            tv.setTextColor(UIUtils.getColor(R.color.border_ed2e0f));
//            tv.setBackgroundResource(R.drawable.bg_border_corners_50dp_ed2e0f);
//        } else {
//            tv.setTextColor(UIUtils.getColor(R.color.border_ea925d));
//            tv.setBackgroundResource(R.drawable.bg_border_corners_50dp_ea925d);
//        }
    }

    /**
     * 设置评论开关
     */
    public static void setCommentSet(TextView tvComment, int set) {
        if (tvComment == null) return;
        switch (set) {
            case comment.XSHF: // 先审后发
            case comment.XFHS: // 先发后审
                tvComment.setText("我说两句...");
                break;
            case comment.JY: // 评论未关闭，但用户被禁言
                tvComment.setText("已禁言");
                break;
        }
    }

    /**
     * 是否可以评论
     *
     * @return true 可以评论
     */
    public static boolean isCanComment(Context context, int set) {
        switch (set) {
            case comment.JY: // 评论未关闭，但用户被禁言
                T.showShort(context, "您已被禁言");
                return false;
            default:
                return true;
        }
    }

    /**
     * 文章条目点击跳转
     */
    public static void articleItemClickJump(Fragment fragment, DraftDetailBean bean) {
//        if (ClickTracker.isDoubleClick()) return;
//        Intent intent = null;
//        if (bean != null) {
//            intent = getArticleIntent(bean.getDoc_type(), bean.getId(), bean.getMetaDataId(),
//                    bean.getList_title(), bean.getWeb_link());
//        }
//        if (intent != null) {
//            fragment.startActivity(intent);
//        }
    }

    /**
     * 文章条目点击跳转
     */
    public static void articleItemClickJump(Activity activity, DraftDetailBean bean) {
//        if (ClickTracker.isDoubleClick()) return;
//        Intent intent = null;
//        if (bean != null) {
//            intent = getArticleIntent(bean.getDoc_type(), bean.getId(), bean.getMetaDataId(),
//                    bean.getList_title(), bean.getWeb_link());
//        }
//        if (intent != null) {
//            activity.startActivity(intent);
//        }
    }


    /**
     * 获取文章跳转 Intent
     */
    public static Intent getArticleIntent(int docType, int id, int mlfId, String title, String
            linkUrl) {
        Intent intent = null;

//        switch (docType) {
//            case type.NONE: // 外链 此时docType字段后台没给，默认为0
//                intent = BrowserActivity.getIntent(linkUrl, title);
//                break;
//            case type.NEWS: // 新闻
//                intent = NewsDetailActivity.getIntent(id, mlfId);
//                break;
//            case type.ATLAS: // 图集
//                intent = AtlasDetailActivity.getIntent(id, mlfId);
//                break;
//            case type.TOPIC: // 专题
//                intent = NewsTopicActivity.getIntent(id, NewsTopicAdapter.FROM_TYPE_NEWS, mlfId);
//                break;
//            case type.LINK: // 链接稿
//            case type.LIVE: // 直播稿
//                intent = LinkDraftActivity.getIntent(id, linkUrl, title, mlfId);
//                break;
//            case type.VIDEO://视频稿
//                intent = NewsDetailActivity.getIntent(id, linkUrl, mlfId);
//                break;
//        }
        return intent;
    }

    /**
     * 设置文章浅读积分获取文字
     */
    public static TextView setGetIntegralText(TextView tvIntegral, int integral) {
        String string = tvIntegral.getContext().getString(R.string.module_detail_get_integral, integral);
        String valueOf = String.valueOf(integral);
        String[] split = string.split(valueOf);
        SpannableString styledText = new SpannableString(string);
        styledText.setSpan(new TextAppearanceSpan(tvIntegral.getContext(), R.style.TextStyle14sp),
                0, split[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(tvIntegral.getContext(), R.style.TextStyle24sp),
                split[0].length(), split[0].length() + valueOf.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(tvIntegral.getContext(), R.style.TextStyle14sp),
                split[0].length() + valueOf.length(), string.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvIntegral.setText(styledText, TextView.BufferType.SPANNABLE);
        return tvIntegral;
    }


    /**
     * 统一执行选择器切换动画
     *
     * @param opView     操作的View
     * @param isSelected 执行结果的状态
     */
    public static void switchSelectorAnim(final View opView, final boolean isSelected) {
        opView.animate().cancel();
        opView.animate()
                .scaleX(0.5f).scaleY(0.5f)
                .alpha(0).setInterpolator(null)
                .setListener(new SelectorAnimatorListener(opView, isSelected));
    }

    /**
     * 动画监听器
     */
    private static class SelectorAnimatorListener implements Animator.AnimatorListener {
        private boolean isSelected;
        private View opView;

        public SelectorAnimatorListener(View opView, boolean isSelected) {
            this.opView = opView;
            this.isSelected = isSelected;
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (opView == null) return;
            opView.setSelected(isSelected);
            opView.animate().scaleX(1).scaleY(1)
                    .alpha(1)
                    .setInterpolator(new OvershootInterpolator(2));
            opView = null;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            opView = null;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }

    /**
     * 格式化浏览量
     *
     * @param pv      浏览量
     * @param docType docType
     * @return 返回格式化后的字符串 (xx阅读/播放)
     */
    public static String formatPageViews(int pv, int docType) {
        String pvValue = String.valueOf(pv);
        if (docType < 0) {
            return pvValue;
        }
        switch (docType) {
            case DraftDetailBean.type.VIDEO:
            case DraftDetailBean.type.LIVE:
                pvValue += "播放";
                break;
            default:
                pvValue += "阅读";

        }
        return pvValue;
    }

    /**
     * 格式化浏览量
     *
     * @param pv 浏览量
     * @return 返回格式化后的字符串
     */
    public static String formatPageViews(int pv) {
        return formatPageViews(pv, -1);
    }

    /**
     * 格式化评论量
     *
     * @param comments 评论次数
     * @return 返回格式化后的字符串
     */
    public static String formatComments(int comments) {
        if (comments <= 0) {
            return "";
        }
        return String.valueOf(comments);
    }

    /**
     * 四舍五入评论数
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static float numFormat(int v1, int v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Integer.toString(v1));
        BigDecimal b2 = new BigDecimal(Integer.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 超大数据四舍五入
     *
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    public static float numFormatSuper(long v1, int v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Long.toString(v1));
        BigDecimal b2 = new BigDecimal(Integer.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }


//    /**
//     * 处理Splash数据
//     *
//     * @param context Context
//     * @param data    SplashBean
//     */
//    public static void handleSplashData(Context context, SplashBean data) {
//        if (data == null || context == null) {
//            return;
//        }
//        switch (data.getType()) {
//            case 0: // 稿件
//                Intent intent = BizUtils.getArticleIntent(data.getDocType(),
//                        data.getArticleId(), data.getMetaDataId(), "", data.getLinkUrl());
//                if (intent != null) {
//                    context.startActivity(intent);
//                }
//                break;
//            case 1:
//                if (!TextUtils.isEmpty(data.getLinkUrl())) {
//                    //外部链接
//                    context.startActivity(BrowserActivity.getIntent(data.getLinkUrl(), ""));
//                }
//                break;
//        }
//
//    }

    /**
     * 处理外部信息跳转
     *
     * @param context Context
     * @param uri     Uri
     */
    public static void handleOuterArticle(Context context, Uri uri) {
        if (context == null || uri == null) return;

        int docType = 0;
        int id = -1;
        try {
            docType = Integer.parseInt(uri.getQueryParameter(article.DOC_TYPE));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            id = Integer.parseInt(uri.getQueryParameter(article.ID));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        int mlfId = -1;
        try {
            mlfId = Integer.parseInt(uri.getQueryParameter(article.MLFID));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (-1 != id && -1 != mlfId) {
            Intent intent = BizUtils.getArticleIntent(docType, id, mlfId,
                    uri.getQueryParameter(article.TITLE),
                    uri.getQueryParameter(article.LINK_URL));
            if (intent != null) {
                context.startActivity(intent);
            }
        }
    }

    /**
     * 评论权限相关信息
     *
     * @author a_liYa
     * @date 2017/1/4 上午10:13.
     */
    //0 禁止评论 1 先审后发 2 先发后审
    public static final class comment {
        /**
         * 禁言
         */
        public static final int JY = 0;
        /**
         * 先审后发
         */
        public static final int XSHF = 1;
        /**
         * 先发后审
         */
        public static final int XFHS = 2;
    }

    private static final class article {

        public static final String DOC_TYPE = "doc_type";

        public static final String ID = "id";

        public static final String MLFID = "mlfid";

        public static final String TITLE = "title";

        public static final String LINK_URL = "link_url";

        public static final String BLUR_URL = "blur_url";

    }
}