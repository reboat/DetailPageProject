package com.zjrb.zjxw.detailproject.holder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zjrb.coreprojectlibrary.utils.TimeUtils;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文章普通 item - ViewHolder （左边文字 - 右边图片）不可左滑删除
 * Created by wanglinjie.
 * create time:2017/7/10  下午5:39
 */
public class ArticleGeneralViewHolder extends BaseRecyclerViewHolder<ArticleItemBean> {

    @BindView(R2.id.iv_pic)
    public ImageView mIvPic;
    @BindView(R2.id.tv_title)
    public TextView mTvTitle;
    @BindView(R2.id.tv_tag)
    public TextView mTvTag;
    @BindView(R2.id.tv_draft_other)
    public TextView mTvDraftOther;
    @BindView(R2.id.tv_type_live)
    public TextView mTvTypeLive;
    @BindView(R2.id.tv_type_atlas)
    public TextView mTvTypeAtlas;
    @BindView(R2.id.tv_type_video)
    public TextView mTvTypeVideo;

    public ArticleGeneralViewHolder(@NonNull ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_item_article_general, parent, false));
        ButterKnife.bind(this, itemView);
    }

    /**
     * 留给子类继承调用，protected修饰
     *
     * @param itemView 条目根布局
     */
    protected ArticleGeneralViewHolder(@NonNull View itemView) {
        super(itemView);
        if (itemView != null) {
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void bindView() {
        Glide.with((Activity) itemView.getContext()).load(mData.getPicUrl())
                .placeholder(R.drawable.ic_load_error)
                .error(R.drawable.ic_load_error).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) // 缓存原始资源，解决Gif加载慢
                .into(mIvPic);
        mTvTitle.setText(mData.getListTitle());
        String other = "";
        if (!TextUtils.isEmpty(mData.getColumnName())) {
            other += mData.getColumnName() + "  ";
        }
        other += BizUtils.formatPageViews(mData.getReadTotalNum(), mData.getDocType()) +
                "  " + TimeUtils.getFriendlyTime(mData.getPublishTime());
        mTvDraftOther.setText(other);
        BizUtils.setDocType(mData.getDocType(), mTvTag, mData.getTag());

        // 图集
        if (mData.getDocType() == ArticleItemBean.type.ATLAS) {
            mTvTypeAtlas.setVisibility(View.VISIBLE);
            mTvTypeAtlas.setText(mData.getAttachImageNum() + "图");
        } else {
            mTvTypeAtlas.setVisibility(View.GONE);
        }

        // 视频
        if (mData.getDocType() == ArticleItemBean.type.VIDEO) {
            mTvTypeVideo.setVisibility(View.VISIBLE);
            mTvTypeVideo.setText(TimeUtils.formateVideoDuration(mData.getVideoDuration()));
        } else {
            mTvTypeVideo.setVisibility(View.GONE);
        }

        // 直播
        mTvTypeLive.setVisibility(mData.getDocType() == ArticleItemBean.type.LIVE ?
                View.VISIBLE : View.GONE);

    }
}