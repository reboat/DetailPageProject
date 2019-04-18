package com.zjrb.zjxw.detailproject.ui.subject.holder;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.SubjectVoiceMassBean;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.nav.Nav;

/**
 * 群众之声评论列表holder
 * Created by wanglinjie.
 * create time:2019/3/7  上午9:07
 */
public class SpecialCommentHolder extends BaseRecyclerViewHolder<SubjectVoiceMassBean> {

    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.ry_container)
    RelativeLayout ryContainer;

    public SpecialCommentHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_subject_comment, parent, false));
        ButterKnife.bind(this, itemView);
    }

    //交互绑定
    @Override
    public void bindView() {
        if (mData.getComment_list() != null && mData.getComment_list().size() > 0 && !TextUtils.isEmpty(mData.getComment_list().get(0).getTitle())) {
            tvTitle.setText(mData.getComment_list().get(0).getTitle());
        } else {
            itemView.setVisibility(View.GONE);
        }
    }

    private Bundle bundle;

    @OnClick({R2.id.ry_title})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        //进入评论文章详情页
        if (view.getId() == R.id.ry_title) {
            if (mData.getComment_list() != null && mData.getComment_list().size() > 0 && !TextUtils.isEmpty(mData.getComment_list().get(0).getTitle())) {
                DataAnalyticsUtils.get().SpecialCommentNewsClick(mData.getComment_list().get(0));
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putString(IKey.ID, String.valueOf(mData.getComment_list().get(0).getChannel_article_id()));
                Nav.with(UIUtils.getActivity()).setExtras(bundle).to(mData.getComment_list().get(0).getUrl());
            }
        }
    }

}
