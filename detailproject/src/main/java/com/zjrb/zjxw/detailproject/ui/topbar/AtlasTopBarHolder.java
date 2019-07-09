package com.zjrb.zjxw.detailproject.ui.topbar;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.zjrb.zjxw.detailproject.R;

import cn.daily.news.biz.core.ui.toolsbar.holder.TopBarViewHolder;

/**
 * 详情页TopBar：左边返回 - 中间文字-右边文字
 *
 * @author a_liYa
 * @date 2017/7/25 17:15.
 */
public class AtlasTopBarHolder extends TopBarViewHolder {
    public FitWindowsRelativeLayout mContainer;
    public TextView tvTitle;
    public ImageView ivShare, ivBack, ivDownLoad, tvSubscribe, mIvIcon;
    public FitWindowsRelativeLayout frlTitle;
    public TextView rankActionView;

    public AtlasTopBarHolder(ViewGroup view, Activity activity) {
        super(view, activity);
        mContainer = findViewById(R.id.layout_title_bar);
        tvTitle = findViewById(R.id.tv_top_bar_title_sub);
        ivShare = findViewById(R.id.iv_share);
        ivBack = findViewById(R.id.iv_back);
        ivDownLoad = findViewById(R.id.iv_top_download);
        frlTitle = findViewById(R.id.frl_title);
        mIvIcon = findViewById(R.id.iv_top_subscribe_icon);
        tvSubscribe = findViewById(R.id.tv_top_bar_subscribe_text);
        rankActionView = findViewById(R.id.rank_action_view);
        setBackOnClickListener(R.id.iv_back);
    }


    /**
     * @return 获取返回控件
     */
    public ImageView getBackView() {
        return ivBack;
    }

    /**
     * 设置标题字体颜色
     *
     * @param color
     */
    public void setTopBarTextColor(int color) {
        tvTitle.setTextColor(color);
    }

    /**
     * @param title 动态设置title
     */
    public void setTopBarText(String title) {
        tvTitle.setText(title);
    }

    /**
     * @return 获取topbar容器
     */
    public FitWindowsRelativeLayout getContainerView() {
        return mContainer;
    }

    /**
     * @return 获取订阅名称
     */
    public TextView getTitleView() {
        return tvTitle;
    }

    /**
     * @return 获取下载控件
     */
    public ImageView getDownView() {
        return ivDownLoad;
    }

    /**
     * @return 获取分享控件
     */
    public ImageView getShareView() {
        return ivShare;
    }

    /**
     * @param v
     * @param visible View.VISIBLE/View.GONE
     *                设置控件显示/隐藏
     */
    public void setViewVisible(View v, int visible) {
        v.setVisibility(visible);
    }

    /**
     * @return 订阅状态
     */
    public ImageView getSubscribe() {
        return tvSubscribe;
    }

    /**
     * @return 获取栏目头像
     */
    public ImageView getIvIcon() {
        return mIvIcon;
    }

    public FitWindowsRelativeLayout getFrlTitle() {
        return frlTitle;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.module_detail_layout_atlas_top_bar;
    }

}
