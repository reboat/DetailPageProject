package com.zjrb.zjxw.detailproject.persionaldetail.adapter;

import android.view.ViewGroup;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.zjxw.detailproject.bean.OfficalArticlesBean;
import com.zjrb.zjxw.detailproject.bean.OfficalListBean;
import com.zjrb.zjxw.detailproject.persionaldetail.holder.PersionalListDetailHolder;
import com.zjrb.zjxw.detailproject.persionaldetail.holder.PersionalTextHolder;

import java.util.List;

/**
 * 所有官员列表适配器
 * Created by wanglinjie.
 * create time:2017/8/21  上午10:14
 */
public class PersionalListAdapter extends BaseRecyclerAdapter {

    public static int TYPE_PERSIONAL_DETAIL = -1;
    public static int TYPE_NOMAL = 0;

    public PersionalListAdapter(List data) {
        super(data);
    }

    /**
     * @param groupList 设置官员列表数据
     */
    public void setupData(List<OfficalListBean.OfficerListBean> groupList) {
        datas.clear();
        if (groupList != null) {
            for (OfficalListBean.OfficerListBean group : groupList) {
                OfficalArticlesBean bean = new OfficalArticlesBean();
                bean.setOfficalId(group.getId());
                bean.setPhoto(group.getPhoto());
                bean.setName(group.getName());
                bean.setJob(group.getTitle());
                bean.setType(TYPE_PERSIONAL_DETAIL);
                datas.add(bean);
                datas.addAll(group.getArticles());
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int
            viewType) {
        if (TYPE_PERSIONAL_DETAIL == viewType) {
            //官员详情
            return new PersionalListDetailHolder(parent);
        }
        //链接稿
        return new PersionalTextHolder(parent);
    }


    @Override
    public int getAbsItemViewType(int position) {
        if (((OfficalArticlesBean) datas.get(position)).getType() == -1) {
            return TYPE_PERSIONAL_DETAIL;
        } else {
            return TYPE_NOMAL;
        }
    }

}
