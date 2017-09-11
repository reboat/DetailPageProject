package com.zjrb.zjxw.detailproject.holder;

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

    private void initView() {
        mRecyleView.addItemDecoration(new ListSpaceDivider(32, 0, false));
        mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        List<HotCommentsBean> list = mockTest();
        mText.setText(itemView.getContext().getString(R.string.module_detail_hot_comment));
        mMore.setText(itemView.getContext().getString(R.string.module_detail_more_comment));
//        adapter = new CommentAdapter(mData.getArticle().getHot_comments(),String.valueOf(mData.getArticle().getId()));
        adapter = new CommentAdapter(mockTest(),"739652");
        mRecyleView.setAdapter(adapter);


//        mRecyleView.addItemDecoration(new ListSpaceDivider(32, 0, false));
//        mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
//                LinearLayoutManager.VERTICAL, false));
//        if (mData != null && mData.getArticle().getHot_comments() != null && mData.getArticle().getHot_comments().size() > 0) {
//            adapter = new CommentAdapter(mData.getArticle().getHot_comments());
//            adapter.setOnItemClickListener(this);
//            mRecyleView.setAdapter(adapter);
//        } else {
//            mLyHotContainer.setVisibility(View.GONE);
//        }
    }

    private List<HotCommentsBean> mockTest() {
        List<HotCommentsBean> list = new ArrayList<>();
        HotCommentsBean b = new HotCommentsBean();
        b.setContent("评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1");
        b.setLike_count(9999999);
        b.setLiked(true);
        b.setNick_name("周公");
        b.setParent_content("父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论");
        b.setParent_like_count(9999999);
        b.setParent_liked(true);
        b.setParent_nick_name("周公");
        b.setParent_content("父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论");
        b.setPortrait_url("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b.setParent_portrait_url("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b.setParent_like_count(9999999);
        b.setParent_liked(true);
        HotCommentsBean b1 = new HotCommentsBean();
        b1.setContent("评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1");
        b1.setLike_count(9999999);
        b1.setLiked(true);
        b1.setNick_name("周公");
        b1.setParent_content("父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论");
        b1.setParent_like_count(9999999);
        b1.setParent_liked(true);
        b1.setParent_nick_name("周公");
        b1.setParent_content("父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论");
        b1.setParent_like_count(9999999);
        b1.setParent_liked(true);
        b1.setPortrait_url("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b1.setParent_portrait_url("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        HotCommentsBean b2 = new HotCommentsBean();
        b2.setContent("评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1");
        b2.setLike_count(9999999);
        b2.setLiked(true);
        b2.setNick_name("周公");
        b2.setParent_content("父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论");
        b2.setParent_like_count(9999999);
        b2.setParent_liked(true);
        b2.setParent_nick_name("周公");
        b2.setParent_content("父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论");
        b2.setParent_like_count(9999999);
        b2.setParent_liked(true);
        b2.setPortrait_url("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b2.setParent_portrait_url("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        HotCommentsBean b3 = new HotCommentsBean();
        b3.setContent("评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1");
        b3.setLike_count(9999999);
        b3.setLiked(true);
        b3.setNick_name("周公");
        b3.setParent_content("父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论");
        b3.setParent_like_count(9999999);
        b3.setParent_liked(true);
        b3.setParent_nick_name("周公");
        b3.setParent_content("父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论");
        b3.setParent_like_count(9999999);
        b3.setParent_liked(true);
        b3.setPortrait_url("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b3.setParent_portrait_url("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        HotCommentsBean b4 = new HotCommentsBean();
        b4.setContent("评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1评论1");
        b4.setLike_count(9999999);
        b4.setLiked(true);
        b4.setNick_name("周公");
        b4.setParent_content("父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论");
        b4.setParent_like_count(9999999);
        b4.setParent_liked(true);
        b4.setParent_nick_name("周公");
        b4.setParent_content("父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论父评论");
        b4.setParent_like_count(9999999);
        b4.setParent_liked(true);
        b4.setPortrait_url("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b4.setParent_portrait_url("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        list.add(b);
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        return list;
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
                    .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(mData.getArticle().getId()))
                    .appendQueryParameter(Key.COMMENT_SET, String.valueOf(mData.getArticle().getComment_level()))
                    .appendQueryParameter(Key.TITLE, mData.getArticle().getList_title())
                    .build(), 0);
        }
    }

}
