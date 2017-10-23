package com.zjrb.zjxw.detailproject.topic.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.PH;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.core.utils.webjs.WebJsInterface;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.topic.adapter.ActivityTopicAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新闻详情页中间内容
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsActivityMiddleHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        View.OnAttachStateChangeListener, OnItemClickListener, ActivityTopicAdapter.IBindSubscribe {
    @BindView(R2.id.tv_column_name)
    TextView mTvColumnName;
    @BindView(R2.id.tv_column_subscribe)
    TextView mTvColumnSubscribe;
    @BindView(R2.id.iv_column_logo)
    ImageView mIvColumnLogo;

    public NewsActivityMiddleHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_activity_middle_holder_layout, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.removeOnAttachStateChangeListener(this);
        itemView.addOnAttachStateChangeListener(this);
        //栏目LOGO
        GlideApp.with(mIvColumnLogo).load(mData.getArticle().getColumn_logo()).placeholder(PH.zheSmall()).centerCrop().into(mIvColumnLogo);

        //栏目名
        if (mData.getArticle().getColumn_name() != null) {
            mTvColumnName.setText(mData.getArticle().getColumn_name());
        }

        //是否已订阅
        if (!mData.getArticle().isColumn_subscribed()) {
            mTvColumnSubscribe.setVisibility(View.VISIBLE);
            mTvColumnSubscribe.setText(itemView.getContext().getString(R.string.module_detail_subscribe));
        }
    }

    /**
     * @param view 频道订阅/栏目  点击
     */
    @OnClick({R2.id.ry_container, R2.id.tv_column_subscribe})
    public void onViewClicked(View view) {
        if (ClickTracker.isDoubleClick()) return;
        ActivityTopicAdapter.CommonOptCallBack callback;
        if (itemView.getContext() instanceof ActivityTopicAdapter.CommonOptCallBack) {
            callback = (ActivityTopicAdapter.CommonOptCallBack) itemView.getContext();
            //栏目订阅
            if (view.getId() == R.id.tv_column_subscribe) {
                if (!mData.getArticle().isColumn_subscribed()) {
                    callback.onOptSubscribe();
                }
            } else {
                //进入栏目详情页
                callback.onOptClickColumn();
            }
        }
    }


    @Override
    public void onViewAttachedToWindow(View v) {
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (ClickTracker.isDoubleClick()) return;
        UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                .setSingle(true)
                .setImgUri(new WebJsInterface(itemView.getContext()).getFirstSrc())
                .setTextContent(mData.getArticle().getSummary())
                .setTitle(mData.getArticle().getList_title())
                .setTargetUrl(mData.getArticle().getUrl()));

    }

    /**
     * 局部刷新订阅状态
     */
    @Override
    public void bindSubscribe() {
        mTvColumnSubscribe.setVisibility(View.GONE);
    }
}