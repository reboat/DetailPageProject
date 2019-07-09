package com.zjrb.zjxw.detailproject.ui.redBoat;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.zjrb.zjxw.detailproject.R;

import cn.daily.news.biz.core.ui.toolsbar.holder.TopBarViewHolder;

/**
 * 5.3.3版本红船号顶部栏
 *
 * @author wanglinjie
 * @date 2018/4/28 14:33.
 */
public class RedBoatDetailTopBarHolder extends TopBarViewHolder {
    public FitWindowsRelativeLayout mContainer;
    public TextView tvTitle;
    public ImageView ivmore, tvSubscribe, mIvIcon;
    public FitWindowsRelativeLayout mRelativeLayout;
    public TextView rankActionView;

    public RedBoatDetailTopBarHolder(ViewGroup view, Activity activity) {
        super(view, activity);
        mContainer = findViewById(R.id.layout_title_bar);
        tvTitle = findViewById(R.id.tv_top_bar_title);
        ivmore = findViewById(R.id.iv_top_more);
        mIvIcon = findViewById(R.id.iv_top_subscribe_icon);
        mRelativeLayout = findViewById(R.id.frl_title);
        tvSubscribe = findViewById(R.id.tv_top_bar_subscribe_text);
        rankActionView = findViewById(R.id.rank_action_view);
        setBackOnClickListener(R.id.iv_top_bar_back);
    }

    /**
     * @param title 动态设置title
     */
    public void setTopBarText(String title) {
        tvTitle.setText(title);
    }

//    /**
//     * @return 获取分享view
//     */
//    public ImageView getShareView() {
//        return mIvshare;
//    }

    public ImageView getIvmore() {
        return ivmore;
    }


    /**
     * @return 获取标题
     */
    public TextView getTitleView() {
        return tvTitle;
    }

    /**
     * @return 获取中间栏目布局
     */
    public FitWindowsRelativeLayout getFitRelativeLayout() {
        return mRelativeLayout;
    }

    /**
     * @return 获取栏目头像
     */
    public ImageView getIvIcon() {
        return mIvIcon;
    }

    /**
     * @return 订阅状态
     */
    public ImageView getSubscribe() {
        return tvSubscribe;
    }

    /**
     * @param v
     * @param visible View.VISIBLE/View.GONE
     *                设置控件显示/隐藏
     */
    public void setViewVisible(View v, int visible) {
        v.setVisibility(visible);
    }

    public FitWindowsRelativeLayout getContainerView() {
        return mContainer;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.module_detail_layout_top_redboat;
    }

}
