package com.zjrb.zjxw.detailproject.holder;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;

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

    private boolean isShowMore = false;
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
        if (view.getId() == R.id.menu_comment) {
            if (mBean != null && mBean.getArticle() != null) {
                new Analytics.AnalyticsBuilder(itemView.getContext(), "800013", "800013")
                        .setEvenName("点击精选的全部按钮")
                        .setObjectID(mBean.getArticle().getChannel_id())
                        .setObjectName(mBean.getArticle().getChannel_name())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mBean.getArticle().getSource_channel_id())
                        .setClassifyName(mBean.getArticle().getSource_channel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mBean.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(mBean.getArticle().getId() + "")
                        .build()
                        .send();

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
