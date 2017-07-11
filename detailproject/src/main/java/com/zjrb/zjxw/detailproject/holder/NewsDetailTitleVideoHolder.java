package com.zjrb.zjxw.detailproject.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zjrb.coreprojectlibrary.utils.TimeUtils;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新闻详情页视频类型title - ViewHolder
 *
 * @author a_liYa
 * @date 2017/5/17 16:23.
 */
public class NewsDetailTitleVideoHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        NewsDetailAdapter.IBindSubscribe {

    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_draft_other)
    TextView mTvDraftOther;
    @BindView(R2.id.tv_column_name)
    TextView mTvColumnName;
    @BindView(R2.id.tv_subscribe)
    TextView mTvSubscribe;

    public NewsDetailTitleVideoHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_top_video, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.setClickable(false);
        mTvTitle.setText(mData.getTitle());
        String other = BizUtils.formatPageViews(mData.getReadTotalNum(), mData.getDocType())
                + "  " + TimeUtils.getFriendlyTime(mData.getPublishTime());
        mTvDraftOther.setText(other);
        mTvColumnName.setText(mData.getColumnName());
        bindSubscribe();
    }

    @OnClick({R.id.tv_subscribe, R.id.tv_column_name})
    public void onViewClicked(View view) {
        NewsDetailAdapter.CommonOptCallBack callback;
        if (itemView.getContext() instanceof NewsDetailAdapter.CommonOptCallBack) {
            callback = (NewsDetailAdapter.CommonOptCallBack) itemView.getContext();
            switch (view.getId()) {
                case R.id.tv_subscribe:
                    if (mData.isSubscribed()) {
                        callback.onOptCancelSubscribe();
                    } else {
                        callback.onOptSubscribe();
                    }
                    break;
                case R.id.tv_column_name:
                    callback.onOptClickColumn();
                    break;
            }
        }
    }

    @Override
    public void bindSubscribe() {
        mTvSubscribe.setText(0 == mData.getSubscribed() ? "订阅" : "已订阅");
    }


}
