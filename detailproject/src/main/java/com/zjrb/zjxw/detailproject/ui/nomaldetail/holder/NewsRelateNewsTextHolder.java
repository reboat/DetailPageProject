package com.zjrb.zjxw.detailproject.ui.nomaldetail.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.ItemClickCallback;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedNewsBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 相关新闻纯文本holder
 * Created by wanglinjie.
 * create time:2017/10/26  上午8:58
 */

public class NewsRelateNewsTextHolder extends BaseRecyclerViewHolder<RelatedNewsBean> implements ItemClickCallback {
    @BindView(R2.id.tv_title)
    TextView mTitle;
    @BindView(R2.id.tv_channel)
    TextView mChannel;
    @BindView(R2.id.tv_prise_num)
    TextView mPriseNum;

    public NewsRelateNewsTextHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_item_relate_news_text, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        if (mData.getTitle() != null) {
            mTitle.setText(mData.getTitle());
            mTitle.setSelected(ReadNewsDaoHelper.alreadyRead(mData.getId()));
        }
        if (!TextUtils.isEmpty(mData.getColumn_name())) {
            mChannel.setVisibility(View.VISIBLE);
            mChannel.setText(mData.getColumn_name());
        }else{
            mChannel.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mData.getLike_count_general())) {
            mPriseNum.setVisibility(View.VISIBLE);
            mPriseNum.setText(mData.getLike_count_general());
        }else{
            mPriseNum.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (mData != null && mData.getTitle() != null) {
            mTitle.setSelected(true);
            ReadNewsDaoHelper.addAlreadyRead(mData.getId());
        }
    }
}
