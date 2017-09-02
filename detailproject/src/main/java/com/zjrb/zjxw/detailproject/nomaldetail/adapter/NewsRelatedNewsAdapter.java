package com.zjrb.zjxw.detailproject.nomaldetail.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
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

    public NewsRelatedNewsAdapter(List been) {
        super(been);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int
            viewType) {
        return new SubjectNewsHolder(UIUtils.inflate(R.layout.module_detail_item_subject,
                parent, false));

    }

    static class SubjectNewsHolder extends BaseRecyclerViewHolder<RelatedNewsBean> {

        @BindView(R2.id.iv_pic)
        ImageView mImg;
        @BindView(R2.id.tv_title)
        TextView mTitle;

        public SubjectNewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView() {
            GlideApp.with(mImg).load(mData.getPic()).placeholder(R.mipmap.ic_launcher).into(mImg);
            mTitle.setText(mData.getTitle());
        }
    }

}
