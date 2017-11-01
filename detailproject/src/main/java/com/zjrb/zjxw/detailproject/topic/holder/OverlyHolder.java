package com.zjrb.zjxw.detailproject.topic.holder;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.topic.utils.ArgbUtils;
import com.zjrb.zjxw.detailproject.topic.utils.AttrUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 悬浮布局 view holder
 *
 * @author a_liYa
 * @date 2017/11/1 13:31.
 */
public class OverlyHolder {

    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_host)
    TextView mTvHost;
    @BindView(R2.id.tv_guest)
    TextView mTvGuest;

    public View itemView;

    private static final int BC_START = Color.parseColor("#7f000000");
    private static final int ATTR_BC_END = R.attr.module_detail_color_ffffff_202124;

    // 标题 颜色
    private static final int ATTR_TTC_START = R.attr.module_detail_color_ffffff_7a7b7d;
    private static final int ATTR_TTC_END = R.attr.module_detail_color_000000_7a7b7d;

    // 主持人／嘉宾 颜色
    private static final int ATTR_HTC_START = R.attr.module_detail_color_d2d2d2_5c5c5c;
    private static final int ATTR_HTC_END = R.attr.module_detail_color_999999_5c5c5c;

    public OverlyHolder(View itemView) {
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public void setData(DraftDetailBean data) {
        DraftDetailBean.ArticleBean mArticle = data != null ? data.getArticle() : null;
        if (mArticle != null) {
            // 标题
            mTvTitle.setText(mArticle.getDoc_title());

            // 主持人
            List<String> hosts = mArticle.getTopic_hosts();
            if (hosts != null && !hosts.isEmpty()) {
                mTvHost.setText("主持人：");
                for (String host : hosts) {
                    mTvHost.append(host);
                }
                mTvHost.setVisibility(View.VISIBLE);
            } else {
                mTvHost.setVisibility(View.GONE);
            }

            // 嘉宾
            List<String> guests = mArticle.getTopic_guests();
            if (guests != null && !guests.isEmpty()) {
                mTvGuest.setVisibility(View.VISIBLE);
                mTvGuest.setText("嘉宾：");
                for (String guest : guests) {
                    mTvGuest.append(guest);
                }
            } else {
                mTvGuest.setVisibility(View.GONE);
            }
        }
    }

    public int getHeight() {
        return itemView.getHeight();
    }

    public void setVisible(boolean visible) {
        itemView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private Resources.Theme getTheme() {
        return itemView.getContext().getTheme();
    }

    public void setFraction(float fraction) {
        // 背景
        itemView.setBackgroundColor(
                ArgbUtils.evaluate(
                        fraction, BC_START, AttrUtils.getColor(getTheme(), ATTR_BC_END)));

        // 标题
        mTvTitle.setTextColor(ArgbUtils.evaluate(fraction,
                AttrUtils.getColor(getTheme(), ATTR_TTC_START),
                AttrUtils.getColor(getTheme(), ATTR_TTC_END)));

        // 主持人/嘉宾
        int hostColor = ArgbUtils.evaluate(fraction,
                AttrUtils.getColor(getTheme(), ATTR_HTC_START),
                AttrUtils.getColor(getTheme(), ATTR_HTC_END));
        mTvHost.setTextColor(hostColor);
        mTvGuest.setTextColor(hostColor);
    }

}
