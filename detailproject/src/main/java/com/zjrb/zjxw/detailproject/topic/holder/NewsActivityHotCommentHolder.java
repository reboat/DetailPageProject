package com.zjrb.zjxw.detailproject.topic.holder;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.comment.adapter.CommentAdapter;
import com.zjrb.zjxw.detailproject.global.Key;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 话题详情页精选评论Holder
 * Created by wanglinjie.
 * create time:2017/7/28  下午12:28
 */

public class NewsActivityHotCommentHolder extends BaseRecyclerViewHolder<DraftDetailBean> {

    @BindView(R2.id.ly_hot_comment)
    LinearLayout mLyHotContainer;
    @BindView(R2.id.tv_related)
    TextView mText;
    @BindView(R2.id.rv_content)
    RecyclerView mRecyleView;
    @BindView(R2.id.tv_more)
    TextView mMore;
    @BindView(R2.id.tv_all)
    TextView mTvAll;

    private CommentAdapter adapter;

    public NewsActivityHotCommentHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_hot_comment, parent, false));
        ButterKnife.bind(this, itemView);
        initView();
    }

    private void initView() {
        mRecyleView.addItemDecoration(new ListSpaceDivider(32, 0, false));
        mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        //精选
        mText.setText(itemView.getContext().getString(R.string.module_detail_useful_comment));
        mMore.setVisibility(View.GONE);

        mRecyleView.addItemDecoration(new ListSpaceDivider(32, 0, false));
        mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                LinearLayoutManager.VERTICAL, false));
        if (mData != null && mData.getArticle().getHot_comments() != null && mData.getArticle().getHot_comments().size() > 0) {
            adapter = new CommentAdapter(mData.getArticle().getHot_comments());
            mRecyleView.setAdapter(adapter);
        } else {
            mLyHotContainer.setVisibility(View.GONE);
        }
    }


    /**
     * @param view 点击进入评论列表
     */
    @OnClick({R2.id.tv_all})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.tv_all) {
            //进入精选列表
            Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/CommentActivity")
                    .buildUpon()
                    .appendQueryParameter(Key.ID, String.valueOf(mData.getArticle().getId()))
                    .appendQueryParameter(Key.MLF_ID, String.valueOf(mData.getArticle().getMlf_id()))
                    .appendQueryParameter(Key.COMMENT_SET, String.valueOf(mData.getArticle().getComment_level()))
                    .appendQueryParameter(Key.TITLE, mData.getArticle().getList_title())
                    .build(), 0);
        }
    }

}
