package com.zjrb.zjxw.detailproject.nomaldetail.adapter;

import android.support.percent.PercentFrameLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerAdapter;
import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;
import com.zjrb.coreprojectlibrary.common.glide.GlideApp;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.RelatedSubjectsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 详情页相关专题列表
 * Created by wanglinjie.
 * create time:2017/7/10  下午5:39
 */
public class NewsRelatedSubjectAdapter extends BaseRecyclerAdapter {

    public NewsRelatedSubjectAdapter(List been) {
        super(been);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int
            viewType) {
        return new SubjectNewsHolder(UIUtils.inflate(R.layout.module_detail_related_subject,
                parent, false));

    }

    static class SubjectNewsHolder extends BaseRecyclerViewHolder<RelatedSubjectsBean> {

        @BindView(R2.id.pf_container)
        PercentFrameLayout mContainer;
        @BindView(R2.id.iv_subject)
        ImageView mImg;

        public SubjectNewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView() {
            if (mData.getPic().isEmpty()) {
                mContainer.setVisibility(View.GONE);
            } else {
                GlideApp.with(mImg).load(mData.getPic()).into(mImg);
            }
        }
    }

}
