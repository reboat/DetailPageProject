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
 * 详情页文案holder
 * Created by wanglinjie.
 * create time:2017/10/26  上午8:58
 */

public class VideoDetailTextHolder extends BaseRecyclerViewHolder<String> {

    @BindView(R2.id.tv_hot)
    TextView mTvHot;
    @BindView(R2.id.tv_comment_num)
    TextView mTvNum;

    private int mCommentNum;

    public VideoDetailTextHolder(ViewGroup parent, int commentNum) {
        super(UIUtils.inflate(R.layout.module_detail_video_comment, parent, false));
        ButterKnife.bind(this, itemView);
        mCommentNum = commentNum;
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        mTvHot.setText(mData.toString());
        if (mCommentNum > 99999) {
            mTvNum.setText("99999+");
        } else {
            mTvNum.setText(mCommentNum + "");
        }
    }

    //评论数
    public void setText(String text) {
        mTvNum.setText(text);
    }
}
