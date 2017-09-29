package com.zjrb.zjxw.detailproject.holder;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsLinearLayout;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.C;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.utils.TimeUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.Key;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新闻详情页(带视频的标题) - ViewHolder
 * Created by wanglinjie.
 * create time:2017/7/21  上午10:14
 */
public class NewsDetailTitleVideoHolder extends BaseRecyclerViewHolder<DraftDetailBean> {
    @BindView(R2.id.iv_top_bg)
    ImageView mIvTopBg;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_reporter)
    TextView mTvReporter;
    @BindView(R2.id.tv_time)
    TextView mTvTime;
    @BindView(R2.id.tv_column_name)
    TextView mTvColumnName;

    public NewsDetailTitleVideoHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_top, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);

        //顶部焦点图
        if (TextUtils.isEmpty(mData.getArticle().getArticle_pic())) {
            mIvTopBg.setVisibility(View.GONE);
        } else {
            mIvTopBg.setVisibility(View.VISIBLE);
            GlideApp.with(mIvTopBg).load(mData.getArticle().getArticle_pic()).centerCrop().into(mIvTopBg);
        }

        //标题
        if (mData.getArticle().getList_title() != null) {
            mTvTitle.setText(mData.getArticle().getList_title());
        }

        //记者
        if (!TextUtils.isEmpty(mData.getArticle().getAuthor())) {
            mTvReporter.setVisibility(View.VISIBLE);
            mTvReporter.setText(mData.getArticle().getAuthor());
        } else {
            mTvReporter.setVisibility(View.GONE);
        }

        //稿件发布时间/栏目名称
        if (!TextUtils.isEmpty(mData.getArticle().getColumn_name())) {
            mTvColumnName.setVisibility(View.VISIBLE);
            mTvTime.setText(TimeUtils.getTime(mData.getArticle().getPublished_at(), C.DATE_FORMAT_1) + "|");
            mTvColumnName.setText(mData.getArticle().getColumn_name());
        } else {
            mTvTime.setText(TimeUtils.getTime(mData.getArticle().getPublished_at(), C.DATE_FORMAT_1));
            mTvColumnName.setVisibility(View.GONE);
        }
    }

    @OnClick({R2.id.tv_column_name})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.tv_column_name) {
            //TODO  WLJ  进入频道列表页面
            Nav.with(UIUtils.getContext()).to(Uri.parse("http://www.8531.cn/subscription/subscribe")
                    .buildUpon()
                    .appendQueryParameter(Key.CHANNEL_NAME, mData.getArticle().getChannel_name())
                    .appendQueryParameter(Key.CHANNEL_ID, mData.getArticle().getChannel_id())
                    .build(), 0);
        }
    }

}
