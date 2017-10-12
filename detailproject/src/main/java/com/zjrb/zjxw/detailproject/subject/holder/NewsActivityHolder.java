package com.zjrb.zjxw.detailproject.subject.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliya.view.ratio.RatioFrameLayout;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.PH;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.ArticleItemBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻列表 - 活动/话题 类型 - 上图中间文字下时间
 * <p>
 * Created by wanglinjie.
 * create time:2017/8/9  下午21:16
 */
public class NewsActivityHolder extends BaseRecyclerViewHolder<ArticleItemBean> {
    @BindView(R2.id.rf_img_container)
    RatioFrameLayout mContainer;
    @BindView(R2.id.iv_picture)
    ImageView mIvPicture;
    @BindView(R2.id.iv_activity_status)
    ImageView mActStatus;
    @BindView(R2.id.tv_content)
    TextView mTvContent;
    @BindView(R2.id.tv_time)
    TextView mTvTime;
    @BindView(R2.id.tv_num)
    TextView mTvNum;

    public NewsActivityHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_item_news_activity, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        if (mData.getDoc_type() == 6) {
            initActivity();
        } else {
            initTopic();
        }

    }

    /**
     * 初始化活动
     */
    private void initActivity() {
        //活动图片
        GlideApp.with(mIvPicture).load(mData.getList_pics().get(0)).centerCrop().placeholder(PH.zheBig()).into(mIvPicture);
        //活动状态 0未开始，1进行中，2已结束
        //TODO  WLJ
        if (mData.getActivity_status() == 0) {
            mActStatus.setImageResource(R.mipmap.default_avatar_icon);
        } else if (mData.getActivity_status() == 1) {
            mActStatus.setImageResource(R.mipmap.me_friend_btn);
        } else {
            mActStatus.setImageResource(R.mipmap.me_qq_btn);
        }

        //活动标题
        if (mData.getList_title() != null && !mData.getList_title().isEmpty()) {
            mTvContent.setText(mData.getList_title());
        } else {
            mTvContent.setVisibility(View.GONE);
        }

        //活动时间
        mTvTime.setText(mData.getActivity_date_begin() + "-" + mData.getActivity_date_end());

        //活动人数
        mTvNum.setVisibility(View.VISIBLE);
        mTvNum.setText(String.valueOf(mData.getActivity_register_count()));
    }

    /**
     * 初始化话题
     */
    private void initTopic() {
        //话题图片
        GlideApp.with(mIvPicture).load(mData.getList_pics().get(0)).centerCrop().placeholder(PH.zheBig()).into(mIvPicture);

        //话题状态
        if (mData.getTopic_status() == 0) {
            mActStatus.setImageResource(R.mipmap.default_avatar_icon);
        } else if (mData.getTopic_status() == 1) {
            mActStatus.setImageResource(R.mipmap.me_friend_btn);
        } else {
            mActStatus.setImageResource(R.mipmap.me_qq_btn);
        }

        //话题标题
        if (mData.getList_title() != null && !mData.getList_title().isEmpty()) {
            mTvContent.setText(mData.getList_title());
        } else {
            mTvContent.setVisibility(View.GONE);
        }

        //话题时间
        mTvTime.setText(mData.getActivity_date_begin() + "-" + mData.getActivity_date_end());

        //无话题人数
        mTvNum.setVisibility(View.GONE);
    }

}
