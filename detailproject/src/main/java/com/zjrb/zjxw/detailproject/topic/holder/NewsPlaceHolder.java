package com.zjrb.zjxw.detailproject.topic.holder;

import android.view.ViewGroup;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.zjxw.detailproject.R;

import butterknife.ButterKnife;

/**
 * 占位holder
 * Created by wanglinjie.
 * create time:2017/7/10  下午5:39
 */
public class NewsPlaceHolder extends BaseRecyclerViewHolder {


    public NewsPlaceHolder(ViewGroup parent) {
        super(inflate(R.layout.module_detail_article_place_item, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
    }
}
