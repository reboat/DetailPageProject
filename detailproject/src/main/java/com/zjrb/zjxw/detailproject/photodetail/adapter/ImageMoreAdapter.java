package com.zjrb.zjxw.detailproject.photodetail.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.RelatedNewsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.glide.AppGlideOptions;
import cn.daily.news.biz.core.glide.PH;
import cn.daily.news.biz.core.nav.Nav;

/**
 * 更多图集
 * Created by wanglinjie.
 * create time:2017/8/21  上午10:14
 */
public class ImageMoreAdapter extends BaseRecyclerAdapter {

    public ImageMoreAdapter(List data) {
        super(data);
    }


    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int
            viewType) {
        return new ImageMoreHolder(parent);
    }

    /**
     * 更多图集使用相关新闻bean
     */
    static class ImageMoreHolder extends BaseRecyclerViewHolder<RelatedNewsBean> {

        @BindView(R2.id.iv_image)
        ImageView mIvImage;
        @BindView(R2.id.tv_title)
        TextView mTvTitle;

        public ImageMoreHolder(ViewGroup parent) {
            super(UIUtils.inflate(R.layout.module_detail_image_more_item, parent, false));
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView() {
            //无图片时用占位图
            GlideApp.with(mIvImage).load(mData.getPic()).centerCrop().placeholder(PH.zheSmall()).apply(AppGlideOptions.smallOptions()).into(mIvImage);
            //文案
            if (mData.getTitle() != null) {
                mTvTitle.setText(mData.getTitle());
                mTvTitle.setSelected(ReadNewsDaoHelper.alreadyRead(mData.getId()));
            }
        }


        @OnClick({R2.id.ry_container})
        public void onClick(View view) {
            if (ClickTracker.isDoubleClick()) return;
            if (view.getId() == R.id.ry_container && mData != null && !TextUtils.isEmpty(mData.getUri_scheme())) {
                new Analytics.AnalyticsBuilder(itemView.getContext(), "800011", "800011", "AppContentClick", false)
                        .setEvenName("更多图集页面，点击单个图集稿件)")
                        .setObjectID(mData.getMlf_id() + "")
                        .setObjectName(mData.getTitle())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID("")
                        .setClassifyName("")
                        .setPageType("更多图集页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(mData.getId() + "")
                        .newsID(mData.getMlf_id() + "")
                        .selfNewsID(mData.getId() + "")
                        .newsTitle(mData.getTitle())
                        .pageType("图集列表页")
                        .objectType("图集新闻列表")
                        .pubUrl(mData.getUri_scheme())
                        .build()
                        .send();
            }
            if (mTvTitle != null) {
                mTvTitle.setSelected(true);
                ReadNewsDaoHelper.addAlreadyRead(mData.getId());
            }
            Nav.with(UIUtils.getActivity()).to(mData.getUri_scheme());
        }

    }


}
