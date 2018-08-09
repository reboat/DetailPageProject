package com.zjrb.zjxw.detailproject.holder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trs.tasdk.entity.ObjectType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.ui.UmengUtils.OutSizeAnalyticsBean;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.widget.divider.GridSpaceDivider;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
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
import cn.daily.news.analytics.Analytics;

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
    //    @BindView(R2.id.tv_column_name)
//    TextView mTvColumnName;
//    @BindView(R2.id.tv_column_subscribe)
//    TextView mTvColumnSubscribe;
    @BindView(R2.id.ry_channel)
    RelativeLayout mRyChannel;
//    @BindView(R2.id.ry_subscribe)
//    RelativeLayout mRySubscribe;

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
        mRecyleView.addItemDecoration(new GridSpaceDivider(34));
        GridLayoutManager managerFollow = new GridLayoutManager(UIUtils.getContext(), 5);
        mRecyleView.setLayoutManager(managerFollow);
    }

    @Override
    public void bindView() {
        itemView.removeOnAttachStateChangeListener(this);
        itemView.addOnAttachStateChangeListener(this);

        //栏目已关联(栏目id>0即表示频道有关联内容)
//        if (mData.getArticle().getColumn_id() > 0
//                && !TextUtils.isEmpty(mData.getArticle().getColumn_name())) {
//            mRySubscribe.setVisibility(View.VISIBLE);
//            mTvColumnName.setText(mData.getArticle().getColumn_name());
//            //已订阅不显示入口
//            if (!mData.getArticle().isColumn_subscribed()) {
//                mTvColumnSubscribe.setVisibility(View.VISIBLE);
//                mTvColumnSubscribe.setText(itemView.getContext().getString(R.string.module_detail_subscribe));
//            }
//
//        } else {
//            mRySubscribe.setVisibility(View.GONE);
//        }

        //频道名称
        if (!TextUtils.isEmpty(mData.getArticle().getSource_channel_name()) && !TextUtils.isEmpty(mData.getArticle().getSource_channel_id())) {
            mRyChannel.setVisibility(View.VISIBLE);
            mTvChannelName.setText(mData.getArticle().getSource_channel_name());
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
        if (mListData == null) {
            mListData = new ArrayList<>();
            mListData.add(new DetailShareBean("微信", SHARE_MEDIA.WEIXIN));
            mListData.add(new DetailShareBean("朋友圈", SHARE_MEDIA.WEIXIN_CIRCLE));
            mListData.add(new DetailShareBean("钉钉", SHARE_MEDIA.DINGTALK));
            mListData.add(new DetailShareBean("QQ", SHARE_MEDIA.QQ));
            mListData.add(new DetailShareBean("更多", SHARE_MEDIA.MORE));
        }

        mAdapter = new DetailShareAdapter(mListData);
        mAdapter.setOnItemClickListener(this);
        mRecyleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * @param view 频道订阅/栏目  点击
     */
    @OnClick({R2.id.ry_channel})
    public void onViewClicked(View view) {
        if (ClickTracker.isDoubleClick()) return;
        NewsDetailAdapter.CommonOptCallBack callback;
        if (itemView.getContext() instanceof NewsDetailAdapter.CommonOptCallBack) {
            callback = (NewsDetailAdapter.CommonOptCallBack) itemView.getContext();
            //频道订阅
//            if (view.getId() == R.id.tv_column_subscribe) {
//                if (mData != null && mData.getArticle() != null && !mData.getArticle().isColumn_subscribed()) {
//                    new Analytics.AnalyticsBuilder(itemView.getContext(), "A0014", "A0014")
//                            .setEvenName("点击订阅")
//                            .setObjectID(mData.getArticle().getColumn_id() + "")
//                            .setObjectName(mData.getArticle().getColumn_name())
//                            .setPageType("新闻详情页")
//                            .setOtherInfo(Analytics.newOtherInfo()
//                                    .put("customObjectType", "RelatedColumnType")
//                                    .toString())
//                            .build()
//                            .send();
//                    callback.onOptSubscribe();
//                }
//                //进入频道详情页
//            } else
            if (view.getId() == R.id.ry_channel) {
                if (mData != null && mData.getArticle() != null) {
                    new Analytics.AnalyticsBuilder(itemView.getContext(), "800012", "800012", "RelatedContentClick", false)
                            .setEvenName("点击正文底部频道名称")
                            .setObjectID(mData.getArticle().getChannel_id())
                            .setObjectName(mData.getArticle().getChannel_name())
                            .setObjectType(ObjectType.ColumnType)
                            .setClassifyID(mData.getArticle().getSource_channel_id())
                            .setClassifyName(mData.getArticle().getSource_channel_name())
                            .setPageType("新闻详情页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("relatedColumn", mData.getArticle().getColumn_id() + "")
                                    .put("subject", "")
                                    .toString())
                            .setSelfObjectID(mData.getArticle().getId() + "").newsID(mData.getArticle().getMlf_id() + "")
                            .selfNewsID(mData.getArticle().getId() + "")
                            .newsTitle(mData.getArticle().getDoc_title())
                            .selfChannelID(mData.getArticle().getChannel_id())
                            .channelName(mData.getArticle().getChannel_name())
                            .pageType("新闻详情页")
                            .relatedContentClick("所属频道")
                            .build()
                            .send();
                }
                callback.onOptClickChannel();
            }
//            else {
//                if (mData != null && mData.getArticle() != null) {
//                    new Analytics.AnalyticsBuilder(itemView.getContext(), "800031", "800031")
//                            .setEvenName("点击进入栏目详情页")
//                            .setObjectID(mData.getArticle().getColumn_id() + "")
//                            .setObjectName(mData.getArticle().getColumn_name())
//                            .setPageType("新闻详情页")
//                            .setOtherInfo(Analytics.newOtherInfo()
//                                    .put("customObjectType", "RelatedColumnType")
//                                    .toString())
//                            .build()
//                            .send();
//                }
//                //进入栏目详情页
//                callback.onOptClickColumn();
//            }
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
        if (ClickTracker.isDoubleClick()) return;
        if (mData != null && mData.getArticle() != null && !TextUtils.isEmpty(mData.getArticle().getUrl())) {
            if (position == 4) {
                new Analytics.AnalyticsBuilder(itemView.getContext(), "800005", "800005", "AppTabClick", false)
                        .setEvenName("点击更多")
                        .setObjectID(mData.getArticle().getMlf_id() + "")
                        .setObjectName(mData.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mData.getArticle().getChannel_id())
                        .setClassifyName(mData.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mData.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(mData.getArticle().getId() + "")
                        .setEventDetail("更多")
                        .pageType("新闻详情页")
                        .clickTabName("更多")
                        .build()
                        .send();
            } else {
                //分享专用bean
                OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                        .setObjectID(mData.getArticle().getMlf_id() + "")
                        .setObjectName(mData.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mData.getArticle().getChannel_id() + "")
                        .setClassifyName(mData.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mData.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfobjectID(mData.getArticle().getId() + "");

                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(true)
                        .setAnalyticsBean(bean)
                        .setArticleId(mData.getArticle().getId() + "")
                        .setImgUri(mData.getArticle().getFirstPic())
                        .setTextContent(mData.getArticle().getSummary())
                        .setTitle(mData.getArticle().getDoc_title())
                        .setPlatform(mListData.get(position).getPlatform())
                        .setTargetUrl(mData.getArticle().getUrl())
                        .setEventName("NewsShare")
                        .setShareType("文章"));
            }

        }
    }

}