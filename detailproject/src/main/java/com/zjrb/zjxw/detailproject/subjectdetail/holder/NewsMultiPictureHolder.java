package com.zjrb.zjxw.detailproject.subjectdetail.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliya.view.ratio.RatioLinearLayout;
import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;
import com.zjrb.coreprojectlibrary.common.glide.GlideApp;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻列表 - 多图类型 - 上标题、中三图、下阅读量
 *
 * @author a_liYa
 * @date 2017/7/7 15:33.
 */
public class NewsMultiPictureHolder extends BaseRecyclerViewHolder<SubjectItemBean> {

    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_tag)
    TextView mTvStatus;
    @BindView(R2.id.tv_flag)
    TextView mTvChannel;
    @BindView(R2.id.iv_1)
    ImageView mIv1;
    @BindView(R2.id.iv_2)
    ImageView mIv2;
    @BindView(R2.id.iv_3)
    ImageView mIv3;
    @BindView(R2.id.tv_prise)
    TextView mTvPrise;
    @BindView(R2.id.tv_read)
    TextView mTvRead;
    @BindView(R2.id.ry_container)
    RatioLinearLayout mContainer;

    public NewsMultiPictureHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_item_news_multi_picture, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        //标题
        mTvTitle.setText(mData.getList_title());
        //标签状态
        if (mData.getList_tag() != null && !mData.getList_tag().isEmpty()) {
            mTvStatus.setText(mData.getList_tag());
        } else {
            mTvStatus.setVisibility(View.GONE);
        }
        //频道名称
        if (mData.getChannel_name() != null && !mData.getChannel_name().isEmpty()) {
            mTvChannel.setText(mData.getChannel_name());
        } else {
            mTvChannel.setVisibility(View.GONE);
        }

        //多图
        if (mData.getList_pics() != null && !mData.getList_pics().isEmpty()) {
            if (!mData.getList_pics().get(0).isEmpty()) {
                GlideApp.with(mIv1).load(mData.getList_pics().get(0)).placeholder(R.mipmap.ic_launcher).into(mIv1);
            } else {
                mIv1.setVisibility(View.GONE);
            }
            if (!mData.getList_pics().get(1).isEmpty()) {
                GlideApp.with(mIv2).load(mData.getList_pics().get(1)).placeholder(R.mipmap.ic_launcher).into(mIv2);
            } else {
                mIv2.setVisibility(View.GONE);
            }
            if (!mData.getList_pics().get(2).isEmpty()) {
                GlideApp.with(mIv3).load(mData.getList_pics().get(2)).placeholder(R.mipmap.ic_launcher).into(mIv3);
            } else {
                mIv3.setVisibility(View.GONE);
            }
        } else {
            mContainer.setVisibility(View.GONE);
        }

        //阅读量
        if (mData.getRead_count() == 0) {
            mTvTitle.setVisibility(View.GONE);
        } else if (mData.getRead_count() > 0 && mData.getRead_count() <= 9999) {
            mTvRead.setText(mData.getRead_count() + "阅读");
        } else if (mData.getComment_count() > 9999 && mData.getComment_count() <= 99999999) {
            mTvRead.setText(BizUtils.numFormat(mData.getRead_count(), 10000, 1) + "万阅读");
        } else if (mData.getRead_count() > 99999999) {
            mTvRead.setText(BizUtils.numFormatSuper((long) mData.getRead_count(), 100000000, 1) + "亿阅读");
        }

        //点赞数
        if (mData.isLike_enabled()) {
            if (mData.getLike_count() == 0) {
                mTvPrise.setVisibility(View.GONE);
            } else if (mData.getLike_count() > 0 && mData.getLike_count() <= 9999) {
                mTvPrise.setText(String.valueOf(mData.getLike_count()) + "赞");
            } else if (mData.getLike_count() > 9999) {
                mTvPrise.setText("9999+赞");
            }
        } else {
            mTvPrise.setVisibility(View.GONE);
        }
    }

}
