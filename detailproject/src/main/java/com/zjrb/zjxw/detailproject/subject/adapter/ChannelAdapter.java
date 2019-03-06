package com.zjrb.zjxw.detailproject.subject.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.bean.SpecialGroupBean;

import java.util.List;

/**
 * 专题 - 频道 - Adapter
 * Created by wanglinjie.
 * create time:2017/8/27  上午10:14
 */
public class ChannelAdapter extends BaseRecyclerAdapter<SpecialGroupBean> {

    private SpecialGroupBean mSelectedData;

    public ChannelAdapter(List<SpecialGroupBean> data) {
        super(data);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    public void setSelectedData(SpecialGroupBean data) {
        mSelectedData = data;
    }

    class ViewHolder extends BaseRecyclerViewHolder<SpecialGroupBean> {

        private TextView tv_channel_name;

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.module_detail_special_channel_item);
            tv_channel_name = itemView.findViewById(R.id.tv_channel_name);
        }

        @Override
        public void bindView() {
            //最多显示6个字
            String name = mData.getGroup_name();
            if (name != null && name.length() > 6) {
                name = name.substring(0, 6);
                name += "…";
            }
            tv_channel_name.setText(name);
            tv_channel_name.setSelected(mSelectedData == mData);
        }
    }

}
