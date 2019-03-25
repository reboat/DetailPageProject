package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * //TODO WLJ 换个布局吧
 * 详情页文案holder
 * Created by wanglinjie.
 * create time:2017/10/26  上午8:58
 */

public class VideoDetailTextHolder extends BaseRecyclerViewHolder<String> {

    @BindView(R2.id.tv_hot)
    TextView mTvHot;
    //评论数
    private int mCommentNum;
    private boolean isHot;

    public VideoDetailTextHolder(ViewGroup parent, int commentNum, boolean isHot) {
        super(UIUtils.inflate(R.layout.module_detail_comment_head, parent, false));
        ButterKnife.bind(this, itemView);
        mCommentNum = commentNum;
        this.isHot = isHot;
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        if (isHot) {
            mTvHot.setText(mData.toString());
        } else {
            mTvHot.setText(mData.toString());
        }
    }

    //设置评论标签文案显示
    public void setText(String text) {
        mTvHot.setText(text);
    }
}
