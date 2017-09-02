package com.zjrb.zjxw.detailproject.subjectdetail.holder;

import android.support.percent.PercentFrameLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.page.PageItem;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.PH;
import com.zjrb.core.ui.widget.ExpandableTextView;
import com.zjrb.core.ui.widget.divider.GridSpaceDivider;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.subjectdetail.adapter.TopicDetailChannelAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanglinjie.
 * create time:2017/8/29  下午9:32
 */

public class HeaderTopicHolder extends PageItem implements OnItemClickListener {


    @BindView(R2.id.iv_news_topic_banner)
    ImageView ivNewsTopicBanner;
    @BindView(R2.id.banner_news_topic)
    PercentFrameLayout bannerNewsTopic;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_summary)
    ExpandableTextView tvSummary;
    @BindView(R2.id.tv_column_detail_description_indicator)
    TextView tvColumnDetailDescriptionIndicator;
    @BindView(R2.id.rv_head)
    RecyclerView rvHead;
    @BindView(R2.id.iv_topic_pic)
    ImageView ivTopicPic;
    @BindView(R2.id.tv_title2)
    TextView tvTitle2;
    @BindView(R2.id.pf_pic_container)
    PercentFrameLayout pfPicContainer;

    /**
     * 专题频道适配器
     */
    private TopicDetailChannelAdapter mChannelItemAdapter;

    public HeaderTopicHolder(ViewGroup parent) {
        super(inflate(R.layout.module_detail_header_topic, parent, false));
        ButterKnife.bind(this, itemView);
        //频道名称
        rvHead.addItemDecoration(new GridSpaceDivider(0));
        GridLayoutManager managerFollow = new GridLayoutManager(UIUtils.getContext(), 4);
        rvHead.setLayoutManager(managerFollow);
    }

    /**
     * @param draftTopicBean 初始化头数据
     */
    public void initData(DraftDetailBean draftTopicBean) {
        if (mChannelItemAdapter != null) {
            mChannelItemAdapter = new TopicDetailChannelAdapter(draftTopicBean.getSubject_groups());
            mChannelItemAdapter.setOnItemClickListener(this);
        }
//        mChannelItemAdapter.setData(draftTopicBean.getSubject_groups());
        rvHead.setAdapter(mChannelItemAdapter);

        tvTitle.setText(draftTopicBean.getList_title());
        String summary = draftTopicBean.getSubject_summary();
        String backPicUrl = draftTopicBean.getArticle_pic();
        String subjectFocusImg = draftTopicBean.getSubject_focus_image();
        if (TextUtils.isEmpty(summary)) {
            tvSummary.setVisibility(View.GONE);
        } else {
            tvSummary.setVisibility(View.VISIBLE);
            tvSummary.setText(summary);
        }
        if (TextUtils.isEmpty(backPicUrl)) {
            bannerNewsTopic.setVisibility(View.GONE);
        } else {
            bannerNewsTopic.setVisibility(View.VISIBLE);
            GlideApp.with(ivNewsTopicBanner).load(backPicUrl).centerCrop().into(ivNewsTopicBanner);
        }
        if (TextUtils.isEmpty(subjectFocusImg)) {
            pfPicContainer.setVisibility(View.GONE);
        } else {
            GlideApp.with(ivTopicPic).load(draftTopicBean.getSubject_focus_image())
                    .placeholder(PH.zheBig())
                    .centerCrop()
                    .into(ivTopicPic);
        }

        if (TextUtils.isEmpty(draftTopicBean.getSubject_summary())) {
            tvTitle2.setVisibility(View.GONE);
        } else {
            tvTitle2.setVisibility(View.VISIBLE);
            tvTitle2.setText(draftTopicBean.getSubject_summary());
        }
    }

    @Override
    public void onItemClick(View itemView, int position) {
        //TODO  WLJ  滚动到项管group

    }
}
