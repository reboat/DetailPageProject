package com.zjrb.zjxw.detailproject.ui.subject.holder;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.db.SPHelper;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.constant.Constants;
import cn.daily.news.biz.core.model.ResourceBiz;

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
        ResourceBiz sp = SPHelper.get().getObject(Constants.Key.INITIALIZATION_RESOURCES);
        if (sp != null && !TextUtils.isEmpty(sp.subject_comment_tag)) {
            tvTab.setText(sp.subject_comment_tag);
        } else {
            tvTab.setText(mData.toString());
        }
    }
}
