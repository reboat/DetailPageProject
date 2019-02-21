package com.zjrb.zjxw.detailproject.holder;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.RelatedSubjectsBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.glide.AppGlideOptions;
import cn.daily.news.biz.core.glide.PH;

/**
 * 相关专题holder
 * Created by wanglinjie.
 * create time:2017/10/26  上午9:02
 */

public class NewsRelateSubjectHolder extends BaseRecyclerViewHolder<RelatedSubjectsBean> {

    @BindView(R2.id.iv_subject)
    ImageView mImg;

    public NewsRelateSubjectHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_related_subject, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        GlideApp.with(mImg).load(mData.getPic()).placeholder(PH.zheBig()).centerCrop().apply(AppGlideOptions.bigOptions()).into(mImg);
    }

}
