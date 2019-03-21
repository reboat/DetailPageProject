package com.zjrb.zjxw.detailproject.ui.subject.holder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 群众之声Tab holder
 * Created by wanglinjie.
 * create time:2017/10/26  上午8:58
 */

public class SpecialCommentTabHolder extends BaseRecyclerViewHolder<String> {
    @BindView(R2.id.tv_tab)
    TextView tvTab;

    public SpecialCommentTabHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_special_tab, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        tvTab.setText(mData.toString());
    }
}
