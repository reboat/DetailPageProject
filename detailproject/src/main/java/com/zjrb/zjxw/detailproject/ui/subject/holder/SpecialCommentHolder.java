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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.nav.Nav;

/**
 * 群众之声标题holder
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
        if (mData.getComment_list() != null && mData.getComment_list().size() > 0 && !TextUtils.isEmpty(mData.getComment_list().get(0).getList_title())) {
            if (ryContainer.getVisibility() == View.GONE) {
                ryContainer.setVisibility(View.VISIBLE);
            }
            tvTitle.setText(mData.getComment_list().get(0).getList_title());
        } else {
            if (ryContainer.getVisibility() == View.VISIBLE) {
                ryContainer.setVisibility(View.GONE);
            }
        }
    }

    private Bundle bundle;

    @OnClick({R2.id.ry_title})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        //进入详情页
        if (view.getId() == R2.id.ry_title) {
            if (mData.getComment_list() != null && mData.getComment_list().size() > 0 && !TextUtils.isEmpty(mData.getComment_list().get(0).getList_title())) {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putString(IKey.ID, String.valueOf(mData.getComment_list().get(0).getChannel_article_id()));
                Nav.with(UIUtils.getActivity()).setExtras(bundle).to(mData.getComment_list().get(0).getUrl());
            }
        }
    }

}
