package com.zjrb.zjxw.detailproject.holder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.widget.divider.GridSpaceDivider;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DetailShareBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.DetailShareAdapter;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.webjs.WebJsInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新闻详情页中间内容
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsDetailMiddleHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        View.OnAttachStateChangeListener, OnItemClickListener {
    @BindView(R2.id.gridlist)
    RecyclerView mRecyleView;
    @BindView(R2.id.tv_channel_name)
    TextView mTvChannelName;
    @BindView(R2.id.tv_channel_subscribe)
    TextView mTvChannelSubscribe;
    @BindView(R2.id.tv_column_name)
    TextView mTvColumnName;
    @BindView(R2.id.tv_column_subscribe)
    TextView mTvColumnSubscribe;
    @BindView(R2.id.ry_channel)
    RelativeLayout mRyChannel;
    @BindView(R2.id.ry_subscribe)
    RelativeLayout mRySubscribe;

    /**
     * 分享数据列表
     */
    private List<DetailShareBean> mListData;
    /**
     * 分享适配器
     */
    private DetailShareAdapter mAdapter;

    public NewsDetailMiddleHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_middle, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        mRecyleView.addItemDecoration(new ListSpaceDivider(32, 0, false));
        mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        itemView.removeOnAttachStateChangeListener(this);
        itemView.addOnAttachStateChangeListener(this);

        //栏目已关联(栏目id>0即表示频道有关联内容)
        if (mData.getArticle().getColumn_id() > 0) {
            mRySubscribe.setVisibility(View.VISIBLE);
            if (mData.getArticle().getColumn_name() != null) {
                mTvColumnName.setText(mData.getArticle().getColumn_name());
            }
            mTvColumnSubscribe.setText(mData.getArticle().isColumn_subscribed() ? "已订阅" : "订阅");
        } else {
            mRySubscribe.setVisibility(View.GONE);
        }

        //频道名称
        if (mData.getArticle().getChannel_id() != null && !mData.getArticle().getChannel_id().isEmpty()) {
            mRyChannel.setVisibility(View.VISIBLE);
            mTvChannelName.setText(mData.getArticle().getChannel_name());
        } else {
            mRyChannel.setVisibility(View.GONE);
        }

        //初始化分享
        initShareBean();
    }

    /**
     * 初始化滚动列表数据
     */
    private void initShareBean() {

        mRecyleView.addItemDecoration(new GridSpaceDivider(0));
        GridLayoutManager managerFollow = new GridLayoutManager(UIUtils.getContext(), 5);
        mRecyleView.setLayoutManager(managerFollow);

        if (mListData == null) {
            mListData = new ArrayList<>();
            mListData.add(new DetailShareBean(R.mipmap.me_friend_btn, "朋友圈", SHARE_MEDIA.WEIXIN_CIRCLE));
            mListData.add(new DetailShareBean(R.mipmap.me_wechat_btn, "微信好友", SHARE_MEDIA.WEIXIN));
            mListData.add(new DetailShareBean(R.mipmap.me_qq_btn, "QQ好友", SHARE_MEDIA.QQ));
            mListData.add(new DetailShareBean(R.mipmap.me_space_btn, "QQ空间", SHARE_MEDIA.QZONE));
            mListData.add(new DetailShareBean(R.mipmap.me_sina_btn, "新浪微博", SHARE_MEDIA.SINA));
        }

        mAdapter = new DetailShareAdapter(mListData);
        mAdapter.setOnItemClickListener(this);
        mRecyleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * @param view 频道订阅/栏目  点击
     */
    @OnClick({R2.id.tv_column_subscribe, R2.id.ry_subscribe, R2.id.ry_channel})
    public void onViewClicked(View view) {
        if (ClickTracker.isDoubleClick()) return;
        NewsDetailAdapter.CommonOptCallBack callback;
        if (itemView.getContext() instanceof NewsDetailAdapter.CommonOptCallBack) {
            callback = (NewsDetailAdapter.CommonOptCallBack) itemView.getContext();
            //频道订阅
            if (view.getId() == R.id.tv_column_subscribe) {
                if (!mData.getArticle().isColumn_subscribed()) {
                    callback.onOptSubscribe();
                }
                //进入频道详情页
            } else if (view.getId() == R.id.ry_channel) {
                callback.onOptClickChannel();
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

    /**
     * 分享点击
     *
     * @param itemView
     * @param position
     */
    @Override
    public void onItemClick(View itemView, int position) {
        if (mData != null && mData.getArticle() != null) {
            UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                    .setSingle(true)
                    .setImgUri(WebJsInterface.getInstance(itemView.getContext()).getmImgSrcs()[0])
                    .setTextContent(WebJsInterface.getInstance(itemView.getContext()).getHtmlText())
                    .setTitle(mData.getArticle().getList_title())
                    .setPlatform(mListData.get(position).getPlatform())
                    .setTargetUrl(mData.getArticle().getUrl()));
        }
    }
}