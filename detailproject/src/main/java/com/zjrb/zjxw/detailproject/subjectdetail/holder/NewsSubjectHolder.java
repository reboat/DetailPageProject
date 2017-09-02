package com.zjrb.zjxw.detailproject.subjectdetail.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻列表 - 专题类型
 *
 * Created by wanglinjie.
 * create time:2017/8/9  下午21:16
 */
public class NewsSubjectHolder extends BaseRecyclerViewHolder<SubjectItemBean> {


    @BindView(R2.id.iv_picture)
    ImageView mIvPicture;
    @BindView(R2.id.tv_tag)
    TextView mTvTag;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_read)
    TextView mTvRead;

    public NewsSubjectHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_item_news_subject, parent, false));
        ButterKnife.bind(this, itemView);
    }

    /* 阅读数规则
    * • 观看数/阅读数为0则不显示数字和图标
    * • 观看数/阅读数1~9999，直接显示具体数字
    * • 观看数/阅读数10000~99999999显示为：1万~9999.9万，例：109000将显示为10.9万，109001将显示为10.9万+
    * • 观看数/阅读数大于99999999显示为1.x亿
    * */

    @Override
    public void bindView() {
        //图片
        GlideApp.with(mIvPicture).load(mData.getList_pics().get(0)).placeholder(R.mipmap.ic_launcher).into(mIvPicture);
        //标题
        mTvTitle.setText(mData.getList_title());
        //状态标签
        if (mData.getList_tag() != null && !mData.getList_tag().isEmpty()) {
            mTvTag.setText(mData.getList_tag());
        } else {
            mTvTag.setVisibility(View.GONE);
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
    }
}
