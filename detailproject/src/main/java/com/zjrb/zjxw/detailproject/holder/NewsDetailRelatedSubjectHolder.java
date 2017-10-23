package com.zjrb.zjxw.detailproject.holder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsRelatedSubjectAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 详情页相关专题Holder
 * Created by wanglinjie.
 * create time:2017/7/28  下午12:28
 */

public class NewsDetailRelatedSubjectHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements OnItemClickListener {
    @BindView(R2.id.rv_content)
    RecyclerView mRecyleView;
    @BindView(R2.id.tv_related)
    TextView tvRelated;
    @BindView(R2.id.ly_container)
    LinearLayout lyContainer;

    private NewsRelatedSubjectAdapter adapter;

    public NewsDetailRelatedSubjectHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_subject_news, parent, false));
        ButterKnife.bind(this, itemView);
        initView();
    }

    private void initView() {
        mRecyleView.addItemDecoration(new ListSpaceDivider(10, 0, false));
        mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                LinearLayoutManager.VERTICAL, false));
    }


    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        if (mData == null || mData.getArticle().getRelated_subjects() == null || mData.getArticle().getRelated_subjects().isEmpty()) {
            lyContainer.setVisibility(View.GONE);
        } else {
            tvRelated.setText("推荐专题");
            adapter = new NewsRelatedSubjectAdapter(mData.getArticle().getRelated_subjects());
            adapter.setOnItemClickListener(this);
            mRecyleView.setAdapter(adapter);
        }
    }

    /**
     * @param itemView
     * @param position 跳转详情页
     */
    @Override
    public void onItemClick(View itemView, int position) {
        if (ClickTracker.isDoubleClick()) return;
        if (mData != null && !TextUtils.isEmpty(mData.getArticle().getRelated_subjects().get(position).getUri_scheme())) {
            Nav.with(UIUtils.getActivity()).to(mData.getArticle().getRelated_subjects().get(position).getUri_scheme());
        }
    }
}
