package com.zjrb.zjxw.detailproject.holder;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.comment.adapter.CommentAdapter;
import com.zjrb.zjxw.detailproject.global.RouteManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 详情页热门评论Holder
 * Created by wanglinjie.
 * create time:2017/7/28  下午12:28
 */

public class NewsDetailCommentHolder extends BaseRecyclerViewHolder<DraftDetailBean> {

    @BindView(R2.id.ly_hot_comment)
    LinearLayout mLyHotContainer;
    @BindView(R2.id.tv_related)
    TextView mText;
    @BindView(R2.id.rv_content)
    RecyclerView mRecyleView;
    @BindView(R2.id.tv_more)
    TextView mMore;
    private CommentAdapter adapter;

    public NewsDetailCommentHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_hot_comment, parent, false));
        ButterKnife.bind(this, itemView);
        initView();
    }

    /**
     * 初始化recyleView
     */
    private void initView() {
        mRecyleView.addItemDecoration(new ListSpaceDivider(32, 0, false));
        mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        mRecyleView.addItemDecoration(new ListSpaceDivider(32, 0, false));
        mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                LinearLayoutManager.VERTICAL, false));
        if (mData != null && mData.getArticle().getHot_comments() != null && mData.getArticle().getHot_comments().size() > 0) {
            mText.setText(itemView.getContext().getString(R.string.module_detail_hot_comment));
            mMore.setText(itemView.getContext().getString(R.string.module_detail_more_comment));
            adapter = new CommentAdapter(mData.getArticle().getHot_comments());
            mRecyleView.setAdapter(adapter);
        } else {
            mLyHotContainer.setVisibility(View.GONE);
        }
    }


    /**
     * 路由传参
     */
    private Bundle bundle;

    /**
     * @param view 点击进入评论列表
     */
    @OnClick({R2.id.tv_more})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.tv_more) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSerializable(IKey.NEWS_DETAIL,mData);
            Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.COMMENT_ACTIVITY_PATH);

        }
    }

}
