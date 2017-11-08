package com.zjrb.zjxw.detailproject.photodetail.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.AppGlideOptions;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.PH;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.RelatedNewsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
            }
        }
    }


}
