package com.zjrb.zjxw.detailproject.utils;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;

import java.math.BigDecimal;

/**
 * 业务相关的逻辑处理工具
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class BizUtils {

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

//    /**
//     * 是否可以评论
//     *
//     * @return true 可以评论
//     */
//    public static boolean isCanComment(Context context, int set) {
//        switch (set) {
//            case comment.JY: // 评论未关闭，但用户被禁言
//                T.showShort(context, UIUtils.getString(R.string.module_detail_no_speaking));
//                return false;
//            default:
//                return true;
//        }
//    }

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

    /**
     * 评论权限相关信息
     * Created by wanglinjie.
     * create time:2017/7/28  上午11:18
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

}