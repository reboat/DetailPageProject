package com.zjrb.zjxw.detailproject.ui.nomaldetail.holder;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.utils.RouteManager;

/**
 * 点击更多文案holder
 * Created by wanglinjie.
 * create time:2017/10/26  上午8:58
 */

public class NewsTextMoreHolder extends BaseRecyclerViewHolder<String> {


    @BindView(R2.id.tv_related)
    TextView mTvRelated;
    @BindView(R2.id.tv_all)
    TextView mTvAll;

    private boolean isShowMore;
    private DraftDetailBean mBean;

    public NewsTextMoreHolder(ViewGroup parent, boolean isShowMore, DraftDetailBean bean) {
        super(UIUtils.inflate(R.layout.module_detail_text_more, parent, false));
        ButterKnife.bind(this, itemView);
        this.isShowMore = isShowMore;
        mBean = bean;
    }

    @Override
    public void bindView() {
        mTvRelated.setText(mData.toString());
        if (isShowMore) {
            mTvAll.setVisibility(View.VISIBLE);
        } else {
            mTvAll.setVisibility(View.GONE);
            itemView.setOnClickListener(null);
        }
    }

    private Bundle bundle;

    @OnClick({R2.id.tv_all})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        //点击精选更多
        if (view.getId() == R.id.tv_all) {
            if (mBean != null && mBean.getArticle() != null) {
                DataAnalyticsUtils.get().ClickCommentAll(mBean);
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(IKey.NEWS_DETAIL, mBean);
                bundle.putBoolean(IKey.IS_SELECT_LIST, true);
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.COMMENT_ACTIVITY_PATH);
            }
        }
    }

}
