package com.zjrb.zjxw.detailproject.holder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;
import com.zjrb.coreprojectlibrary.common.base.adapter.OnItemClickListener;
import com.zjrb.coreprojectlibrary.common.permission.IPermissionOperate;
import com.zjrb.coreprojectlibrary.ui.UmengUtils.UmengShareBean;
import com.zjrb.coreprojectlibrary.ui.UmengUtils.UmengShareUtils;
import com.zjrb.coreprojectlibrary.ui.widget.divider.GridSpaceDivider;
import com.zjrb.coreprojectlibrary.ui.widget.divider.ListSpaceDivider;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.coreprojectlibrary.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DetailShareBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.DetailShareAdapter;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;

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
        View.OnAttachStateChangeListener,
        NewsDetailAdapter.IBindSubscribe, OnItemClickListener {
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
    private DetailShareAdapter mAdapter;

    /**
     * 分享需要写入权限
     */
    private IPermissionOperate permissionOp;
    private UmengShareUtils umengShareUtils;


    public NewsDetailMiddleHolder(ViewGroup parent, IPermissionOperate permissionOp) {
        super(UIUtils.inflate(R.layout.module_detail_layout_middle, parent, false));
        ButterKnife.bind(this, itemView);
        this.permissionOp = permissionOp;
    }

    @Override
    public void bindView() {
        mRecyleView.addItemDecoration(new ListSpaceDivider(32, 0, false));
        mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        itemView.removeOnAttachStateChangeListener(this);
        itemView.addOnAttachStateChangeListener(this);
        if (!mData.getColumn_id().isEmpty()) {
            mRySubscribe.setVisibility(View.VISIBLE);
            mTvColumnName.setText(mData.getColumn_name());
        }

        if (!mData.getChannel_id().isEmpty()) {
            mRyChannel.setVisibility(View.VISIBLE);
            mTvChannelName.setText(mData.getChannel_name());
        }
        initShareBean();
        bindSubscribe();
        if (umengShareUtils == null)
            umengShareUtils = new UmengShareUtils();

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
            mListData.add(new DetailShareBean(R.mipmap.me_wechat_btn, "微信好友", SHARE_MEDIA.WEIXIN));
            mListData.add(new DetailShareBean(R.mipmap.me_friend_btn, "朋友圈", SHARE_MEDIA.WEIXIN_CIRCLE));
            mListData.add(new DetailShareBean(R.mipmap.me_qq_btn, "QQ好友", SHARE_MEDIA.QQ));
            mListData.add(new DetailShareBean(R.mipmap.me_space_btn, "QQ空间", SHARE_MEDIA.QZONE));
            mListData.add(new DetailShareBean(R.mipmap.me_sina_btn, "新浪微博", SHARE_MEDIA.SINA));
        }


        mAdapter = new DetailShareAdapter(mListData);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setData(mListData);
        mRecyleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    @OnClick({R2.id.tv_column_subscribe, R2.id.tv_channel_subscribe})
    public void onViewClicked(View view) {
        if (ClickTracker.isDoubleClick()) return;
        NewsDetailAdapter.CommonOptCallBack callback;
        if (itemView.getContext() instanceof NewsDetailAdapter.CommonOptCallBack) {
            callback = (NewsDetailAdapter.CommonOptCallBack) itemView.getContext();
            if (view.getId() == R.id.tv_column_subscribe) {
                if (!mData.isColumn_subscribed()) {
                    callback.onOptSubscribe();
                }
            } else {
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
    public void bindSubscribe() {
        mTvColumnSubscribe.setText(mData.isColumn_subscribed() ? "已订阅" : "订阅");
    }

    @Override
    public void onItemClick(View itemView, int position) {
        umengShareUtils.startShare(
                UmengShareBean.getInstance()
                        .setTitle(mData.getList_title())
                        .setTextContent(mData.getContent())
                        .setImgUri(mData.getArticle_pic())
                        .setTargetUrl(mData.getUrl())
                        .setPlatform(mListData.get(position).getPlatform())
                ,
                permissionOp
        );
    }
}