package com.zjrb.zjxw.detailproject.topic.holder;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.utils.ArgbUtils;

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

    private static final int BC_START = Color.parseColor("#cc000000");
    private static final int ATTR_BC_END = R.color._ffffff_202124;

    // 标题 颜色
    private static final int ATTR_TTC_START = R.color._ffffff_7a7b7d;
    private static final int ATTR_TTC_END = R.color._000000_7a7b7d;

    // 主持人／嘉宾 颜色
    private static final int ATTR_HTC_START = R.color._d2d2d2_5c5c5c;
    private static final int ATTR_HTC_END = R.color._999999_5c5c5c;

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
            String host_s = "";
            if (hosts != null && !hosts.isEmpty() && hosts.size() > 0) {
                mTvHost.setVisibility(View.VISIBLE);
                for (int i = 0; i < hosts.size(); i++) {
                    if (i == (hosts.size() - 1)) {
                        host_s += hosts.get(i);
                    } else {
                        host_s += hosts.get(i) + "  ";
                    }
                }
                mTvHost.setText("主持人: " + host_s);

            } else {
                mTvHost.setVisibility(View.GONE);
            }

            // 嘉宾
            List<String> guests = mArticle.getTopic_guests();
            String guest_s = "";
            if (guests != null && !guests.isEmpty() && guests.size() > 0) {
                mTvGuest.setVisibility(View.VISIBLE);
                for (int i = 0; i < guests.size(); i++) {
                    if (i == (guests.size() - 1)) {
                        guest_s += guests.get(i);
                    } else {
                        guest_s += guests.get(i) + "  ";
                    }
                }
                mTvGuest.setText("嘉宾: " + guest_s);
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

        GradientDrawable bg;
        if (itemView.getBackground() instanceof GradientDrawable) {
            bg = (GradientDrawable) itemView.getBackground();
            bg.mutate();
        } else {
            bg = new GradientDrawable();
            bg.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        }
        bg.setColors(new int[]{
                ArgbUtils.evaluate(fraction,
                        Color.TRANSPARENT, ContextCompat.getColor(itemView.getContext(), ATTR_BC_END)),
                ArgbUtils.evaluate(fraction,
                        BC_START, ContextCompat.getColor(itemView.getContext(), ATTR_BC_END))});
        // 背景
        itemView.setBackground(bg);

        // 标题
        mTvTitle.setTextColor(ArgbUtils.evaluate(fraction,
                ContextCompat.getColor(mTvTitle.getContext(), ATTR_TTC_START),
                ContextCompat.getColor(mTvTitle.getContext(), ATTR_TTC_END)));

        // 主持人/嘉宾
        int hostColor = ArgbUtils.evaluate(fraction,
                ContextCompat.getColor(mTvTitle.getContext(), ATTR_HTC_START),
                ContextCompat.getColor(mTvTitle.getContext(), ATTR_HTC_END));
        mTvHost.setTextColor(hostColor);
        mTvGuest.setTextColor(hostColor);
    }

}
