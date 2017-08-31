package com.zjrb.zjxw.detailproject.holder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;
import com.zjrb.coreprojectlibrary.utils.TimeUtils;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻详情页(带视频的标题) - ViewHolder
 * Created by wanglinjie.
 * create time:2017/7/21  上午10:14
 */
public class NewsDetailTitleVideoHolder extends BaseRecyclerViewHolder<DraftDetailBean> {
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_reporter)
    TextView mTvReporter;
    @BindView(R2.id.tv_column_name)
    TextView mTvColumnName;

    public NewsDetailTitleVideoHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_top_video, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        mTvTitle.setText(mData.getList_title());
        String other = BizUtils.formatPageViews(mData.getRead_count(), mData.getDoc_type())
                + "  " + TimeUtils.getFriendlyTime(mData.getPublished_at());
        mTvReporter.setText(other);
        mTvColumnName.setText(mData.getColumn_name());
    }

}
