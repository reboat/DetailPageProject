package com.zjrb.zjxw.detailproject.holder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsRelatedNewsAdapter;

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
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        mRecyleView.addItemDecoration(new ListSpaceDivider(1f, UIUtils.getActivity().getResources().getColor(R.color.dc_f5f5f5), true, true));
        mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        if (mData == null || mData.getArticle().getRelated_news() == null || mData.getArticle().getRelated_news().isEmpty()) {
            lyContainer.setVisibility(View.GONE);
        } else {
            tvRelated.setText(itemView.getContext().getString(R.string.module_detail_realated_news_tip));
            adapter = new NewsRelatedNewsAdapter(mData.getArticle().getRelated_news());
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
        if (mData != null) {
            Nav.with(UIUtils.getActivity()).to(mData.getArticle().getUrl());
        }
    }
}
