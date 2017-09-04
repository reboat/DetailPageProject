package com.zjrb.zjxw.detailproject.photodetail.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

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

    static class ImageMoreHolder extends BaseRecyclerViewHolder {

        @BindView(R2.id.iv_image)
        TextView mIvImage;
        @BindView(R2.id.tv_title)
        TextView mTvTitle;

        public ImageMoreHolder(ViewGroup parent) {
            super(UIUtils.inflate(R.layout.module_detail_image_more_item, parent, false));
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView() {

        }
    }


}
