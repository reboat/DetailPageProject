package com.zjrb.zjxw.detailproject.persionaldetail.holder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalArticlesBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 官员holder2(纯文本)
 *
 * @author a_liYa
 * @date 2017/7/7 15:33.
 */
public class PersionalTextHolder extends BaseRecyclerViewHolder<OfficalArticlesBean> {


    @BindView(R2.id.tv_title)
    TextView mTvTitle;

    public PersionalTextHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_persional_holder2, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        mTvTitle.setText(mData.getTitle());

    }
}
