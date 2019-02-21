package com.zjrb.zjxw.detailproject.nomaldetail.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.ItemClickCallback;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftHotTopNewsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.glide.AppGlideOptions;
import cn.daily.news.biz.core.glide.PH;

/**
 * 撤稿页面列表适配器
 * Created by wanglinjie.
 * create time:2017/9/4  上午8:59
 */

public class EmptyStateListAdapter extends BaseRecyclerAdapter<DraftHotTopNewsBean.HotNewsBean> {
    public static int TYPE_TEXT = -1;
    public static int TYPE_NOMAL = 0;

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
        if (viewType == TYPE_NOMAL) {
            return new EmptyStateHolder(UIUtils.inflate(R.layout.module_detail_item_subject,
                    parent, false));
        } else {
            return new EmptyStateTextHolder(UIUtils.inflate(R.layout.module_detail_item_subject_text,
                    parent, false));
        }

    }

    @Override
    public int getAbsItemViewType(int position) {
        //纯文本
        if (((DraftHotTopNewsBean.HotNewsBean) datas.get(position)).isList_pics_empty()) {
            return TYPE_TEXT;
        } else {
            //图文/纯图
            return TYPE_NOMAL;
        }
    }

    /**
     * 撤稿适配器
     * 样式与相关新闻一致，图文/纯文
     */
    static class EmptyStateHolder extends BaseRecyclerViewHolder<DraftHotTopNewsBean.HotNewsBean> implements ItemClickCallback {

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
            GlideApp.with(mImg).load(mData.getList_pics().get(0)).centerCrop().placeholder(PH.zheSmall()).apply(AppGlideOptions.smallOptions()).into(mImg);
            //标题
            if (mData.getList_title() != null) {
                mTitle.setText(mData.getList_title());
                mTitle.setSelected(ReadNewsDaoHelper.alreadyRead(mData.getId()));
            }
        }

        @Override
        public void onItemClick(View itemView, int position) {
            if (ClickTracker.isDoubleClick()) return;
            if (mTitle != null) {
                mTitle.setSelected(true);
                ReadNewsDaoHelper.addAlreadyRead(mData.getId());
            }
        }
    }

    /**
     * 相关新闻holder/纯文本
     */
    static class EmptyStateTextHolder extends BaseRecyclerViewHolder<DraftHotTopNewsBean.HotNewsBean> implements ItemClickCallback {

        @BindView(R2.id.tv_title)
        TextView mTitle;

        public EmptyStateTextHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView() {
            if (mData.getList_title() != null) {
                mTitle.setText(mData.getList_title());
                mTitle.setSelected(ReadNewsDaoHelper.alreadyRead(mData.getId()));
            }
        }

        @Override
        public void onItemClick(View itemView, int position) {
            if (ClickTracker.isDoubleClick()) return;
            if (mTitle != null) {
                mTitle.setSelected(true);
                ReadNewsDaoHelper.addAlreadyRead(mData.getId());
            }
        }
    }
}
