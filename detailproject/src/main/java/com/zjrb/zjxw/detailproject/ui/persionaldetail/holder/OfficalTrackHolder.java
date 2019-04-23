package com.zjrb.zjxw.detailproject.ui.persionaldetail.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.OfficalDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 直播间多图holder
 * Created by wanglinjie.
 * create time:2019/3/26  下午3:54
 */
public class OfficalTrackHolder extends BaseRecyclerViewHolder<OfficalDetailBean.OfficerBean.ResumesBean> {

    /**
     * 如果年份一样 则只显示一次
     */
    @BindView(R2.id.tv_year)
    TextView mTvYear;
    @BindView(R2.id.tv_month)
    TextView mTvMonth;
    @BindView(R2.id.tv_persional_info)
    TextView mTvPersionalInfo;

    public OfficalTrackHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_persional_info_item, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        //任职年份
        if (!mData.isSameYear()) {
            mTvYear.setVisibility(View.VISIBLE);
            mTvYear.setText(mData.getYear() + "年");
        } else {
            mTvYear.setVisibility(View.INVISIBLE);
        }

        //任职月份
        if (mData.getMonth() < 10) {
            mTvMonth.setText("0" + mData.getMonth() + "月");
        } else {
            mTvMonth.setText(mData.getMonth() + "月");
        }

        //官员地点
        if (mData.getLocation() != null) {
            mTvPersionalInfo.setText("[" + mData.getLocation() + "] ");
        }

        //官员职务
        if (mData.getTitle() != null) {
            mTvPersionalInfo.append(mData.getTitle());
        }

    }
}
