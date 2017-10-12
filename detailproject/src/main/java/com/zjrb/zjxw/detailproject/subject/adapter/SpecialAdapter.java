package com.zjrb.zjxw.detailproject.subject.adapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.base.OverlayViewHolder;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.nav.Nav;
import com.zjrb.daily.news.ui.adapter.NewsBaseAdapter;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.SpecialGroupBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 专题详情页 - Adapter
 *
 * @author a_liYa
 * @date 2017/10/11 上午10:37.
 */
public class SpecialAdapter extends NewsBaseAdapter {

    // group组名
    public static final int TYPE_GROUP = 100;

    public SpecialAdapter(DraftDetailBean data) {
        super(null);
        if (data != null && data.getArticle() != null
                && data.getArticle().getSubject_groups() != null) {
            List<SpecialGroupBean> groups = data.getArticle().getSubject_groups();
            List list = new ArrayList();
            for (SpecialGroupBean group : groups) {
                list.add(group);
                if (group.getGroup_articles() != null) {
                    list.addAll(group.getGroup_articles());
                }
            }
            setData(list);
        }
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int
            viewType) {
        if (TYPE_GROUP == viewType) {
            return new GroupViewHolder(parent);
        }
        return super.onAbsCreateViewHolder(parent, viewType);
    }

    @Override
    public OverlayViewHolder onCreateOverlayViewHolder(ViewGroup parent, int viewType) {
        return new GroupViewHolder(parent);
    }

    @Override
    public int getAbsItemViewType(int position) {
        if (getData(position) instanceof SpecialGroupBean)
            return TYPE_GROUP;
        return super.getAbsItemViewType(position);
    }

    @Override
    public boolean isOverlayViewType(int position) {
        return getAbsItemViewType(position) == TYPE_GROUP;
    }

    static class GroupViewHolder extends OverlayViewHolder<SpecialGroupBean> implements View
            .OnClickListener {

        @BindView(R2.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R2.id.tv_more)
        TextView tvMore;

        public GroupViewHolder(ViewGroup parent) {
            super(parent, R.layout.module_detail_special_group_name);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView() {
            tvGroupName.setText(mData.getGroup_name());
            // 显示是否有更多
            tvMore.setVisibility(!mData.isGroup_has_more() ? View.VISIBLE : View.GONE);
            itemView.setOnClickListener(!mData.isGroup_has_more() ? this : null);
        }

        @Override
        public void onClick(View v) {
            if (itemView == v && mData != null) {
                //进入专题更多列表
                Bundle bundle = new Bundle();
                bundle.putInt(IKey.GROUP_ID, mData.getGroup_id());
                bundle.putString(IKey.TITLE, mData.getGroup_name());
                Nav.with(v.getContext()).setExtras(bundle).toPath(RouteManager.TOPIC_LIST);
            }
        }
    }

}
