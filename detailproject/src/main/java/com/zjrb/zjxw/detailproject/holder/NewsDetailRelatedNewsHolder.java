package com.zjrb.zjxw.detailproject.holder;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;
import com.zjrb.coreprojectlibrary.common.base.adapter.OnItemClickListener;
import com.zjrb.coreprojectlibrary.nav.Nav;
import com.zjrb.coreprojectlibrary.ui.widget.divider.ListSpaceDivider;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsRelatedNewsAdapter;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 详情页相关新闻Holder
 * Created by wanglinjie.
 * create time:2017/7/28  下午12:28
 */

public class NewsDetailRelatedNewsHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements OnItemClickListener {
    @BindView(R2.id.rv_content)
    RecyclerView mRecyleView;
    @BindView(R2.id.tv_related)
    TextView tvRelated;
    @BindView(R2.id.ly_container)
    LinearLayout lyContainer;

    private NewsRelatedNewsAdapter adapter;

    public NewsDetailRelatedNewsHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_subject_news, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        if (mData == null || mData.getRelated_news() == null || mData.getRelated_news().isEmpty()) {
            lyContainer.setVisibility(View.GONE);
        } else {
            mRecyleView.addItemDecoration(new ListSpaceDivider(32, 0, false));
            mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                    LinearLayoutManager.VERTICAL, false));
            adapter = new NewsRelatedNewsAdapter(mData.getRelated_news());
            adapter.setOnItemClickListener(this);
            mRecyleView.setAdapter(adapter);
        }
    }

    /**
     * 详情页相关新闻点击进入详情页
     * 普通详情页/专题/图集/话题/链接
     * 2-9分别代表普通、链接、图集、专题、话题、活动、直播、视频
     *
     * @param itemView
     * @param position
     */
    @Override
    public void onItemClick(View itemView, int position) {
        BizUtils.jumpToDetailActivity(mData, position);
    }
}
