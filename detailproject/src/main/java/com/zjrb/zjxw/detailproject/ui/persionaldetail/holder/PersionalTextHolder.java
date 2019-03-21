package com.zjrb.zjxw.detailproject.ui.persionaldetail.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.OfficalArticlesBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.biz.core.nav.Nav;

/**
 * 官员holder2(纯文本)
 * Created by wanglinjie.
 * create time:2017/8/17  上午10:14
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
        if (mData.getTitle() != null) {
            mTvTitle.setText(mData.getTitle());
            mTvTitle.setSelected(ReadNewsDaoHelper.alreadyRead(mData.getId()));
        }
    }

    /**
     * 点击跳转到链接稿
     *
     * @param view
     */
    @OnClick({R2.id.tv_title})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.tv_title) {
            if (mTvTitle != null) {
                mTvTitle.setSelected(true);
                ReadNewsDaoHelper.addAlreadyRead(mData.getId());
            }
            Nav.with(UIUtils.getActivity()).to(mData.getUrl());
        }
    }
}
