package com.zjrb.zjxw.detailproject.subjectdetail.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;
import com.zjrb.coreprojectlibrary.common.glide.GlideApp;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻列表 - 专题类型 - 上图、中标题、下阅读量
 *
 * Created by wanglinjie.
 * create time:2017/8/9  下午21:16
 */
public class NewsTopicHolder extends BaseRecyclerViewHolder<SubjectItemBean> {

    @BindView(R2.id.iv_picture)
    ImageView mIvPicture;
    @BindView(R2.id.tv_tag)
    TextView mTvTag;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_other)
    TextView mTvOther;

    public NewsTopicHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_item_news_topic, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (mData.getList_pics() != null && !mData.getList_pics().isEmpty()) {
            if (!mData.getList_pics().get(0).isEmpty()) {
                GlideApp.with(mIvPicture).load(mData.getList_pics().get(0)).placeholder(R.mipmap.ic_launcher).into(mIvPicture);
            } else {
                mIvPicture.setVisibility(View.GONE);
            }
        }
        mTvTag.setText(mData.getList_tag());
        mTvTitle.setText(mData.getList_title());
        mTvOther.setText(mData.getRead_count());

    }

}
