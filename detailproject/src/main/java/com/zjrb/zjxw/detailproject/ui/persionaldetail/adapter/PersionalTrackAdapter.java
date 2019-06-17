package com.zjrb.zjxw.detailproject.ui.persionaldetail.adapter;

import android.view.ViewGroup;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.zjxw.detailproject.apibean.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.ui.persionaldetail.holder.OfficalTrackHolder;

import java.util.ArrayList;
import java.util.List;

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

}