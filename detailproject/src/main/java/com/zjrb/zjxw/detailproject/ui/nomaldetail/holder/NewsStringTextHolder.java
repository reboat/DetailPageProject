package com.zjrb.zjxw.detailproject.ui.nomaldetail.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 详情页文案holder
 * Created by wanglinjie.
 * create time:2017/10/26  上午8:58
 */

public class NewsStringTextHolder extends BaseRecyclerViewHolder<String> {

    @BindView(R2.id.tv_related)
    TextView mTvRelated;
    @BindView(R2.id.v_line)
    View mLine;

    public NewsStringTextHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_string_text, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        mTvRelated.setText(mData.toString());
        if(mData.equals("热门评论")){
            mLine.setVisibility(View.GONE);
        }else{
            mLine.setVisibility(View.VISIBLE);
        }
    }

}
