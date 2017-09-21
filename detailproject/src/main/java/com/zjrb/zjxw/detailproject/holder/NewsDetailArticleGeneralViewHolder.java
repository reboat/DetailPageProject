package com.zjrb.zjxw.detailproject.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.PH;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文章普通 item - ViewHolder （左边图片 - 右边文字）不可左滑删除
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsDetailArticleGeneralViewHolder extends BaseRecyclerViewHolder<SubjectItemBean> {
    @BindView(R2.id.iv_picture)
    ImageView mIvPicture;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_flag)
    TextView mTvFlag;
    @BindView(R2.id.tv_prise)
    TextView mTvPrise;
    @BindView(R2.id.tv_read)
    TextView mTvRead;

    public NewsDetailArticleGeneralViewHolder(@NonNull ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_article_general_item, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        //标题
        if (mData.getList_title() != null) {
            mTvTitle.setText(mData.getList_title());
        }
        //标签状态
        if (mData.getList_tag() != null && !mData.getList_tag().isEmpty()) {
            mTvFlag.setText(mData.getList_tag());
        } else {
            mTvFlag.setVisibility(View.GONE);
        }
        //TODO  WLJ 默认图片
        //多图
        GlideApp.with(mIvPicture).load(mData.getList_pics().get(0)).placeholder(PH.zheSmall()).centerCrop().into(mIvPicture);

        //阅读量
        if (mData.getRead_count() == 0) {
            mTvRead.setVisibility(View.GONE);
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