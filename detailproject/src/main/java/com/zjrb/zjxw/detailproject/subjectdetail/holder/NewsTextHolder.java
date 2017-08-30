package com.zjrb.zjxw.detailproject.subjectdetail.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;
import com.zjrb.coreprojectlibrary.common.listener.IOnItemClickListener;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻列表 - 纯文字类型 - 上标题，下阅读、赞
 *
 * @author a_liYa
 * @date 2017/7/7 15:33.
 */
public class NewsTextHolder extends BaseRecyclerViewHolder<SubjectItemBean> {

    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_praise)
    TextView mTvPrise;
    @BindView(R2.id.tv_read)
    TextView mTvRead;

    public NewsTextHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_item_news_text, parent, false));
        ButterKnife.bind(this, itemView);
    }

   /* 阅读数规则
    * • 观看数/阅读数为0则不显示数字和图标
    * • 观看数/阅读数1~9999，直接显示具体数字
    * • 观看数/阅读数10000~99999999显示为：1万~9999.9万，例：109000将显示为10.9万，109001将显示为10.9万+
    * • 观看数/阅读数大于99999999显示为1.x亿
    * */

    /* 点赞数规则
     * • 点赞数为0则不显示数字和图标
     * • 点赞数1~9999，直接显示
     * • 点赞数>9999，显示9999+
     * • 点赞功能被关闭，则不显示数字和图标。
     * */
    @Override
    public void bindView() {
        //标题
        if (mData.getList_title() != null && !mData.getList_title().isEmpty()) {
            mTvTitle.setText(mData.getList_title());
        } else {
            mTvTitle.setVisibility(View.GONE);
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
