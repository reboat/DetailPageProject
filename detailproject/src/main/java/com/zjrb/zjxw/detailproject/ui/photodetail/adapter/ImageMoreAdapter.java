package com.zjrb.zjxw.detailproject.ui.photodetail.adapter;

import android.view.ViewGroup;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedNewsBean;

import java.util.List;

/**
 * 更多图集
 * Created by wanglinjie.
 * create time:2017/8/21  上午10:14
 */
public class ImageMoreAdapter extends BaseRecyclerAdapter {

    public ImageMoreAdapter(List data) {
        super(data);
    }

    private DraftDetailBean bean;

    //FUCK WM
    public void setDetailBean(DraftDetailBean bean) {
        this.bean = bean;
    }


    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int
            viewType) {
        return new ImageMoreHolder(parent, bean);
    }

}
