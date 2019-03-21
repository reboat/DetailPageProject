package com.zjrb.zjxw.detailproject.ui.nomaldetail.holder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 点击查看更多 文案
 * Created by wanglinjie.
 * create time:2017/10/26  上午8:58
 */

public class NewsStringClickMoreHolder extends BaseRecyclerViewHolder<String> {

    @BindView(R2.id.tv_related)
    TextView mTvRelated;

    public NewsStringClickMoreHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_string_click_more, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        mTvRelated.setText(mData.toString());
    }
}
