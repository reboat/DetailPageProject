package com.zjrb.zjxw.detailproject.subject.holder;

import android.view.ViewGroup;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;

import butterknife.ButterKnife;

/**
 * 群众之声评论占位
 * Created by wanglinjie.
 * create time:2019/3/7  上午9:07
 */
public class SpecialCommentPlaceHolder extends BaseRecyclerViewHolder<String> {

    public SpecialCommentPlaceHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_subject_place, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {

    }
}
