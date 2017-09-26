package com.zjrb.zjxw.detailproject.nomaldetail.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.PH;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftHotTopNewsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 撤稿页面列表适配器
 * Created by wanglinjie.
 * create time:2017/9/4  上午8:59
 */

public class EmptyStateListAdapter extends BaseRecyclerAdapter {
    /**
     * 构造方法
     *
     * @param datas 传入集合数据
     */
    public EmptyStateListAdapter(List datas) {
        super(datas);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int
            viewType) {
        return new EmptyStateHolder(UIUtils.inflate(R.layout.module_detail_item_subject,
                parent, false));

    }

    /**
     * 撤稿适配器
     * 样式与相关新闻一致，图文/纯文
     */
    static class EmptyStateHolder extends BaseRecyclerViewHolder<DraftHotTopNewsBean.HotNewsBean> {

        @BindView(R2.id.tv_title)
        TextView mTitle;
        @BindView(R2.id.iv_pic)
        ImageView mImg;

        public EmptyStateHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView() {
            //占位图，如无图片，则显示占位图
            GlideApp.with(mImg).load(mData.getList_pics().get(0)).centerCrop().placeholder(PH.zheSmall()).into(mImg);
            //标题
            if (mData.getList_title() != null) {
                mTitle.setText(mData.getList_title());
            }
        }
    }
}
