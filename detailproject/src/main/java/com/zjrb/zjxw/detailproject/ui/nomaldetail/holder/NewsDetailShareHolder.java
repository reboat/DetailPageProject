package com.zjrb.zjxw.detailproject.ui.nomaldetail.holder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.ui.divider.GridSpaceDivider;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DetailShareBean;
import com.zjrb.zjxw.detailproject.apibean.bean.DetailShareItemBean;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.adapter.DetailShareAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;

/**
 * 新闻详情页中间内容
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsDetailShareHolder extends BaseRecyclerViewHolder<DetailShareItemBean> implements
        View.OnAttachStateChangeListener, OnItemClickListener {
    @BindView(R2.id.gridlist)
    RecyclerView mRecyleView;
    /**
     * 分享数据列表
     */
    private List<DetailShareBean> mListData;
    /**
     * 分享适配器
     */
    private DetailShareAdapter mAdapter;

    public NewsDetailShareHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_share, parent, false));
        ButterKnife.bind(this, itemView);
        mRecyleView.addItemDecoration(new GridSpaceDivider(10));
        GridLayoutManager managerFollow = new GridLayoutManager(UIUtils.getContext(), 5);
        mRecyleView.setLayoutManager(managerFollow);
    }

    @Override
    public void bindView() {
        itemView.removeOnAttachStateChangeListener(this);
        itemView.addOnAttachStateChangeListener(this);
        //初始化分享
        initShareBean();
    }

    /**
     * 初始化滚动列表数据
     */
    private void initShareBean() {
        if (mListData == null) {
            mListData = new ArrayList<>();
            //使用FACEBOOK作为新闻卡片的ID
            mListData.add(new DetailShareBean("新闻卡片", SHARE_MEDIA.FACEBOOK));
            mListData.add(new DetailShareBean("微信", SHARE_MEDIA.WEIXIN));
            mListData.add(new DetailShareBean("朋友圈", SHARE_MEDIA.WEIXIN_CIRCLE));
            mListData.add(new DetailShareBean("钉钉", SHARE_MEDIA.DINGTALK));
            mListData.add(new DetailShareBean("更多", SHARE_MEDIA.MORE));
        }

        mAdapter = new DetailShareAdapter(mListData);
        mAdapter.setOnItemClickListener(this);
        mRecyleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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
        if (mData != null && mData.draftDetailBean !=null && mData.draftDetailBean.getArticle() != null && !TextUtils.isEmpty(mData.draftDetailBean.getArticle().getUrl())) {
            if (position == 4) {
                new Analytics.AnalyticsBuilder(itemView.getContext(), "800005", "AppTabClick", false)
                        .name("点击更多")
                        .objectID(mData.draftDetailBean.getArticle().getMlf_id() + "")
                        .objectShortName(mData.draftDetailBean.getArticle().getDoc_title())
                        .seObjectType(ObjectType.C01)
                        .classID(mData.draftDetailBean.getArticle().getChannel_id())
                        .className(mData.draftDetailBean.getArticle().getChannel_name())
                        .pageType("新闻详情页")
                        .selfObjectID(mData.draftDetailBean.getArticle().getId() + "")
                        .clickTabName("更多")
                        .build()
                        .send();

            }
            //分享专用bean
            OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                    .setObjectID(mData.draftDetailBean.getArticle().getMlf_id() + "")
                    .setObjectName(mData.draftDetailBean.getArticle().getDoc_title())
                    .setObjectType(ObjectType.C01)
                    .setClassifyID(mData.draftDetailBean.getArticle().getChannel_id() + "")
                    .setClassifyName(mData.draftDetailBean.getArticle().getChannel_name())
                    .setPageType("新闻详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", mData.draftDetailBean.getArticle().getColumn_id() + "")
                            .put("subject", "")
                            .toString())
                    .setSelfobjectID(mData.draftDetailBean.getArticle().getId() + "");

            UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                    .setSingle(true)
                    .setAnalyticsBean(bean)
                    .setArticleId(mData.draftDetailBean.getArticle().getId() + "")
                    .setImgUri(mData.draftDetailBean.getArticle().getFirstPic())
                    .setTextContent(mData.draftDetailBean.getArticle().getSummary())
                    .setTitle(mData.draftDetailBean.getArticle().getDoc_title())
                    .setPlatform(mListData.get(position).getPlatform())
                    .setTargetUrl(mData.draftDetailBean.getArticle().getUrl())
                    .setCardUrl(getData().draftDetailBean.getArticle().getCard_url())
                    .setShareType("文章"));

        }
    }

}