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
import com.zjrb.coreprojectlibrary.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.comment.adapter.CommentAdapter;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 详情页热门评论Holder
 * Created by wanglinjie.
 * create time:2017/7/28  下午12:28
 */

public class NewsDetailCommentHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements OnItemClickListener {

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
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        mRecyleView.addItemDecoration(new ListSpaceDivider(32, 0, false));
        mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                LinearLayoutManager.VERTICAL, false));
        if (mData != null && mData.getHot_comments() != null && mData.getHot_comments().size() > 0) {
            adapter = new CommentAdapter(mData.getHot_comments());
            adapter.setOnItemClickListener(this);
            mRecyleView.setAdapter(adapter);
        } else {
            mLyHotContainer.setVisibility(View.GONE);
        }
    }

    /**
     * @param view 点击进入评论列表
     */
    @OnClick({R2.id.tv_more})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.tv_more) {
            Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/CommentActivity")
                    .buildUpon()
                    .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(mData.getId()))
                    .appendQueryParameter(Key.MLF_ID, String.valueOf(mData.getMlf_id()))
                    .appendQueryParameter(Key.COMMENT_SET, String.valueOf(mData.getComment_level()))
                    .appendQueryParameter(Key.TITLE, mData.getList_title())
                    .build(), 0);
        }
    }

    /**
     * @param itemView
     * @param position 点击开始评论
     */
    @Override
    public void onItemClick(View itemView, int position) {
        if (BizUtils.isCanComment(UIUtils.getActivity(), mData.getComment_level())) {
            Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/CommentWindowActivity")
                    .buildUpon()
                    .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(mData.getId()))
                    .appendQueryParameter(Key.MLF_ID, String.valueOf(mData.getMlf_id()))
                    .appendQueryParameter(Key.PARENT_ID, mData.getHot_comments().get(position).getParent_id())
                    .build(), 0);
        }
    }
}
