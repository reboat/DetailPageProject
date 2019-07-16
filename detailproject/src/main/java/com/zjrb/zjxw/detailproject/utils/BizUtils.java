package com.zjrb.zjxw.detailproject.utils;

import android.animation.Animator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.zjrb.core.db.SPHelper;

import cn.daily.news.biz.core.model.ResourceBiz;

/**
 * 业务相关的逻辑处理工具
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class BizUtils {
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
     * 榜单是否开启,默认开启
     *
     * @return
     */
    public static boolean isRankEnable() {
        boolean isRankEnable = true;
        ResourceBiz resourceBiz = SPHelper.get().getObject(cn.daily.news.biz.core.constant.Constants.Key.INITIALIZATION_RESOURCES);
        if (resourceBiz != null && resourceBiz.feature_list != null && resourceBiz.feature_list.size() > 0) {
            for (ResourceBiz.FeatureListBean bean : resourceBiz.feature_list) {
                if ("columns_rank".equals(bean.name)) {
                    isRankEnable = bean.enabled;
                    break;
                }
            }
        }
        return isRankEnable;
    }

}