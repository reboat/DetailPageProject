package com.zjrb.zjxw.detailproject.persionaldetail.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人官员履历适配器
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class PersionalTrackAdapter extends BaseRecyclerAdapter {

    public PersionalTrackAdapter(List data) {
        super(data);
    }

    public void setupData(List<OfficalDetailBean.OfficerBean.ResumesBean> groupList) {
        if (groupList != null) {
            for (int i = 0; i < groupList.size(); i++) {
                OfficalDetailBean.OfficerBean.ResumesBean bean = new OfficalDetailBean.OfficerBean.ResumesBean();
                if (i == 0) {
                    bean.setSameYear(false);
                }
//                与下一个元素比较是否是同一年
                if (groupList.size() > (++i)) {
                    if (String.valueOf(bean.getYear()).equals(String.valueOf(groupList.get(i + 1).getYear()))) {
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
            mTvYear.setText(mData.getYear()+"");
            mTvMonth.setText(mData.getMonth()+"");
            mTvPersionalInfo.setText("[" + mData.getLocation() + "] " + mData.getTitle());

        }
    }

}