package com.zjrb.zjxw.detailproject.persionaldetail.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人官员履历适配器
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class PersionalTrackAdapter extends BaseRecyclerAdapter {


    public PersionalTrackAdapter() {
        super(new ArrayList<OfficalDetailBean.OfficerBean.ResumesBean>());
    }

    public void setupData(List<OfficalDetailBean.OfficerBean.ResumesBean> groupList) {
        datas.clear();
        if (groupList != null) {
            for (int i = 0; i < groupList.size(); i++) {
                OfficalDetailBean.OfficerBean.ResumesBean bean = groupList.get(i);
                if (i == 0) {
                    bean.setSameYear(false);
                }
                //如果没有到最后一位
                if (groupList.size() > 1 && i < (groupList.size() - 1)) {
                    if (bean.getYear() == groupList.get(i + 1).getYear()) {
                        groupList.get(i + 1).setSameYear(true);
                    } else {
                        groupList.get(i + 1).setSameYear(false);
                    }
                }
                datas.add(bean);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int
            viewType) {
        return new OfficalTrackHolder(parent);
    }


    /**
     * 官员履历Holder
     */
    static class OfficalTrackHolder extends BaseRecyclerViewHolder<OfficalDetailBean.OfficerBean.ResumesBean> {

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
                mTvYear.setText(mData.getYear() + "年");
            } else {
                mTvYear.setVisibility(View.GONE);
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

}