package com.zjrb.zjxw.detailproject.nomaldetail.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.PH;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.RelatedNewsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 详情页相关新闻列表
 * Created by wanglinjie.
 * create time:2017/7/10  下午5:39
 */
public class NewsRelatedNewsAdapter extends BaseRecyclerAdapter {

    public static int TYPE_TEXT = -1;
    public static int TYPE_NOMAL = 0;

    public NewsRelatedNewsAdapter(List list) {
        super(list);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int
            viewType) {
        if (viewType == TYPE_NOMAL) {
            return new DetailNewsNomalHolder(UIUtils.inflate(R.layout.module_detail_item_subject,
                    parent, false));
        } else {
            return new DetailNewsTextHolder(UIUtils.inflate(R.layout.module_detail_item_subject_text,
                    parent, false));
        }


    }

    @Override
    public int getAbsItemViewType(int position) {
        //纯文本
        if (TextUtils.isEmpty(((RelatedNewsBean) datas.get(position)).getPic())) {
            return TYPE_TEXT;
        } else {
            //图文/纯图
            return TYPE_NOMAL;
        }
    }

    /**
     * 相关新闻holder
     */
    static class DetailNewsNomalHolder extends BaseRecyclerViewHolder<RelatedNewsBean> {

        @BindView(R2.id.iv_pic)
        ImageView mImg;
        @BindView(R2.id.tv_title)
        TextView mTitle;

        public DetailNewsNomalHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView() {
            if (!TextUtils.isEmpty(mData.getPic())) {
                mImg.setVisibility(View.VISIBLE);
                GlideApp.with(mImg).load(mData.getPic()).placeholder(PH.zheSmall()).centerCrop().into(mImg);
            } else {
                mImg.setVisibility(View.GONE);
            }

            if (mData.getTitle() != null) {
                mTitle.setText(mData.getTitle());
            }
        }
    }

    /**
     * 相关新闻holder/纯文本
     */
    static class DetailNewsTextHolder extends BaseRecyclerViewHolder<RelatedNewsBean> {

        @BindView(R2.id.tv_title)
        TextView mTitle;

        public DetailNewsTextHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView() {
            if (mData.getTitle() != null) {
                mTitle.setText(mData.getTitle());
            }
        }
    }


}
