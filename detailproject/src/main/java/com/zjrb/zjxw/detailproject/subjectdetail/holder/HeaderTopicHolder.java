package com.zjrb.zjxw.detailproject.subjectdetail.holder;

import android.support.percent.PercentFrameLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.zjrb.zjxw.detailproject.eventBus.ChannelItemClickEvent;
import com.zjrb.zjxw.detailproject.subjectdetail.adapter.TopicDetailChannelAdapter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanglinjie.
 * create time:2017/8/29  下午9:32
 */

public class HeaderTopicHolder extends PageItem implements OnItemClickListener {


    @BindView(R2.id.iv_news_topic_banner)
    ImageView ivNewsTopicBanner;
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

    private boolean isOpen = false;
    /**
     * 专题频道适配器
     */
    private TopicDetailChannelAdapter mChannelItemAdapter;

    private DraftDetailBean bean;

    public HeaderTopicHolder(ViewGroup parent) {
        super(inflate(R.layout.module_detail_header_topic, parent, false));
        ButterKnife.bind(this, itemView);
        //频道名称
        rvHead.addItemDecoration(new GridSpaceDivider(13));
        GridLayoutManager managerFollow = new GridLayoutManager(UIUtils.getContext(), 4);
        rvHead.setLayoutManager(managerFollow);
    }

    private void initView(final DraftDetailBean draftTopicBean) {
        if (draftTopicBean != null) {
            isOpen = draftTopicBean.getArticle().isOpen();
        }
        tvColumnDetailDescriptionIndicator.setText(itemView.getContext().getString(R.string.module_detail_text_down));
        tvColumnDetailDescriptionIndicator.setCompoundDrawables(itemView.getContext().getResources().getDrawable(R.drawable.module_detail_text_down), null, null, null);
        tvSummary.setText(draftTopicBean.getArticle().getSummary());
        tvSummary.setOnLineCountListener(new ExpandableTextView.OnLineCountListener() {
            @Override
            public void onLineCount(int lineCount, int maxLines) {
                if (lineCount > 3) {
                    if (isOpen) {
                        tvColumnDetailDescriptionIndicator.setCompoundDrawables(itemView.getContext().getResources().getDrawable(R.drawable.module_detail_text_down), null, null, null);
                    } else {
                        tvColumnDetailDescriptionIndicator.setCompoundDrawables(itemView.getContext().getResources().getDrawable(R.drawable.module_detail_text_up), null, null, null);
                    }
                } else if (lineCount <= 3 && maxLines != 3) {
                    if (isOpen) {
                        tvColumnDetailDescriptionIndicator.setCompoundDrawables(itemView.getContext().getResources().getDrawable(R.drawable.module_detail_text_down), null, null, null);
                    } else {
                        tvColumnDetailDescriptionIndicator.setCompoundDrawables(itemView.getContext().getResources().getDrawable(R.drawable.module_detail_text_up), null, null, null);
                    }
                }
            }
        });
        tvColumnDetailDescriptionIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = !isOpen;
                if (isOpen) {
                    tvColumnDetailDescriptionIndicator.setText(itemView.getContext().getString(R.string.module_detail_text_up));
                    tvSummary.setMaxLines(Integer.MAX_VALUE);
                } else {
                    tvColumnDetailDescriptionIndicator.setText(itemView.getContext().getString(R.string.module_detail_text_down));
                    tvSummary.setMaxLines(3);
                }
            }
        });
    }

    /**
     * @param draftTopicBean 初始化头数据
     */
    public void initData(DraftDetailBean draftTopicBean) {
        bean = draftTopicBean;
        initView(draftTopicBean);
        if (mChannelItemAdapter == null) {
            mChannelItemAdapter = new TopicDetailChannelAdapter(draftTopicBean.getArticle().getSubject_groups());
            mChannelItemAdapter.setOnItemClickListener(this);
        }
//        mChannelItemAdapter.setData(draftTopicBean.getSubject_groups());
        rvHead.setAdapter(mChannelItemAdapter);

        tvTitle.setText(draftTopicBean.getArticle().getList_title());
        String summary = draftTopicBean.getArticle().getSummary();
        String backPicUrl = draftTopicBean.getArticle().getArticle_pic();
        String subjectFocusImg = draftTopicBean.getArticle().getSubject_focus_image();
        if (TextUtils.isEmpty(summary)) {
            tvSummary.setVisibility(View.GONE);
        } else {
            tvSummary.setVisibility(View.VISIBLE);
            tvSummary.setText(summary);
        }
        if (TextUtils.isEmpty(backPicUrl)) {
            ivNewsTopicBanner.setVisibility(View.GONE);
        } else {
            GlideApp.with(ivNewsTopicBanner).load(backPicUrl).centerCrop().into(ivNewsTopicBanner);
        }
        if (TextUtils.isEmpty(subjectFocusImg)) {
            pfPicContainer.setVisibility(View.GONE);
        } else {
            GlideApp.with(ivTopicPic).load(draftTopicBean.getArticle().getSubject_focus_image())
                    .placeholder(PH.zheBig())
                    .centerCrop()
                    .into(ivTopicPic);
        }

        if (TextUtils.isEmpty(draftTopicBean.getArticle().getSummary())) {
            tvTitle2.setVisibility(View.GONE);
        } else {
            tvTitle2.setVisibility(View.VISIBLE);
            tvTitle2.setText(draftTopicBean.getArticle().getSummary());
        }
    }

    @Override
    public void onItemClick(View itemView, int position) {
        //TODO  WLJ  滚动到相关group
        EventBus.getDefault().postSticky(new ChannelItemClickEvent(bean.getArticle().getSubject_groups().get(position).getGroupId()));
    }
}
