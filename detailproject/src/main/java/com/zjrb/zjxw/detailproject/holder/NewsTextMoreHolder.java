package com.zjrb.zjxw.detailproject.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 相关新闻holder
 * Created by wanglinjie.
 * create time:2017/10/26  上午8:58
 */

public class NewsTextMoreHolder extends BaseRecyclerViewHolder<String> {


    @BindView(R2.id.tv_related)
    TextView mTvRelated;
    @BindView(R2.id.tv_all)
    TextView mTvAll;

    private boolean isShowMore = false;

    public NewsTextMoreHolder(ViewGroup parent, boolean isShowMore) {
        super(UIUtils.inflate(R.layout.module_detail_text_more, parent, false));
        ButterKnife.bind(this, itemView);
        this.isShowMore = isShowMore;
    }

    @Override
    public void bindView() {
        mTvRelated.setText(mData.toString());
        if (isShowMore) {
            mTvAll.setVisibility(View.VISIBLE);
        } else {
            mTvAll.setVisibility(View.GONE);
        }
    }

}
