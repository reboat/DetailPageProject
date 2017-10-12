package com.zjrb.zjxw.detailproject.subject.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
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
    public int getAbsItemViewType(int position) {
        if (getData(position) instanceof SpecialGroupBean)
            return TYPE_GROUP;
        return super.getAbsItemViewType(position);
    }

    static class GroupViewHolder extends BaseRecyclerViewHolder<SpecialGroupBean> {

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
            itemView.setClickable(!mData.isGroup_has_more());
            // TODO: 2017/10/11 别忘记取消取反
        }

    }

}
