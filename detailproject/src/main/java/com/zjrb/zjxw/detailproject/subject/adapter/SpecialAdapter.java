package com.zjrb.zjxw.detailproject.subject.adapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trs.tasdk.entity.ObjectType;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics;

/**
 * 专题详情页 - Adapter
 *
 * @author a_liYa
 * @date 2017/10/11 上午10:37.
 */
public class SpecialAdapter extends NewsBaseAdapter {

    // group组名
    public static final int TYPE_GROUP = 100;

    private DraftDetailBean mBean;

    public SpecialAdapter(DraftDetailBean data) {
        super(null);
        if (data != null && data.getArticle() != null
                && data.getArticle().getSubject_groups() != null) {
            mBean = data;
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
            return new GroupViewHolder(parent,mBean);
        }
        return super.onAbsCreateViewHolder(parent, viewType);
    }

    @Override
    public OverlayViewHolder onCreateOverlayViewHolder(ViewGroup parent, int viewType) {
        return new GroupViewHolder(parent,mBean);
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

    /**
     * 分组 ViewHolder
     *
     * @author a_liYa
     * @date 2017/10/21 下午4:13.
     */
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

        private DraftDetailBean mBean;
        /**
         * 网脉埋点专用构造器
         * @param parent
         */
        public GroupViewHolder(ViewGroup parent,DraftDetailBean bean) {
            super(parent, R.layout.module_detail_special_group_name);
            ButterKnife.bind(this, itemView);
            mBean = bean;
        }

        @Override
        public void bindView() {
            tvGroupName.setText(mData.getGroup_name());
            // 显示是否有更多
            tvMore.setVisibility(mData.isGroup_has_more() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void setData(SpecialGroupBean data) {
            super.setData(data);
            if (data != null) {
                itemView.setOnClickListener(data.isGroup_has_more() ? this : null);
            }
        }

        @Override
        public void onClick(View v) {
            if (itemView == v && mData != null) {
                if(mData != null){
                    Map map = new HashMap();
                    map.put("relatedColumn", "SubjectType");
                    map.put("subject", mBean.getArticle().getId());
                    new Analytics.AnalyticsBuilder(itemView.getContext(), "900002", "900002")
                            .setEvenName("专题详情页，更多按钮点击")
                            .setPageType("专题详情页")
                            .setClassifyID(mBean.getArticle().getMlf_id()+"")
                            .setClassifyName(mBean.getArticle().getDoc_title())
                            .setOtherInfo(map.toString())
                            .setSelfObjectID(mBean.getArticle().getId() + "")
                            .build()
                            .send();
                }
                //进入专题更多列表
                Bundle bundle = new Bundle();
                bundle.putString(IKey.GROUP_ID, mData.getGroup_id());
                bundle.putString(IKey.TITLE, mData.getGroup_name());
                Nav.with(v.getContext()).setExtras(bundle).toPath(RouteManager.TOPIC_LIST);
            }
        }
    }

}
