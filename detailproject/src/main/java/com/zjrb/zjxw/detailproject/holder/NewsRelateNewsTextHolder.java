package com.zjrb.zjxw.detailproject.holder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.RelatedNewsBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 相关新闻纯文本holder
 * Created by wanglinjie.
 * create time:2017/10/26  上午8:58
 */

public class NewsRelateNewsTextHolder extends BaseRecyclerViewHolder<RelatedNewsBean> {
    @BindView(R2.id.tv_title)
    TextView mTitle;

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
    }

}
