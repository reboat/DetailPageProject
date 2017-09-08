package com.zjrb.zjxw.detailproject.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.domain.base.BaseInnerData;
import com.zjrb.core.utils.StringUtils;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.task.CommentPraiseTask;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 详情页/评论列表item holder
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class DetailCommentHolder extends BaseRecyclerViewHolder<HotCommentsBean> {
    @BindView(R2.id.ry_container)
    RelativeLayout mLaycontainer;
    @BindView(R2.id.iv_avatar)
    ImageView mImg;
    @BindView(R2.id.tv_thumb_up)
    TextView mThumb;
    @BindView(R2.id.tv_name)
    TextView mName;
    @BindView(R2.id.tv_content)
    TextView mContent;
    @BindView(R2.id.tv_time)
    TextView mTime;
    //删除评论
    @BindView(R2.id.tv_delete)
    TextView mDelete;
    //回复评论
    @BindView(R2.id.ly_comment_reply)
    LinearLayout mReply;
    //原评论内容
    @BindView(R2.id.tv_comment_content)
    TextView mTvCommentContent;
    //原评论者
    @BindView(R2.id.tv_comment_src)
    TextView mTvCommentSrc;
    //收起/打开评论
//    @BindView(R2.id.tv_open)
//    TextView mTvOpen;

    private String[] openStrs = {"显示完整评论", "收起评论"};

    public DetailCommentHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
//        mTvCommentContent.setOnLineCountListener(this);
    }

    @Override
    public void bindView() {
        mContent.setText(mData.getContent());
        mName.setText(mData.getNick_name());
        mTime.setText(StringUtils.long2String(mData.getCreated_at(), "MM-dd HH:mm:ss"));
        mTvCommentContent.setText(mData.getParent_content());
        mThumb.setText(mData.getLike_count() + "");
        mThumb.setSelected(mData.isLiked() == true);
        GlideApp.with(mImg).load(mData.getPortrait_url()).centerCrop().into(mImg);
        mTvCommentSrc.setText(mData.getParent_nick_name());

    }

    @OnClick({R2.id.tv_thumb_up})
    public void onClick(View view) {
        //点赞
        if (view.getId() == R.id.tv_thumb_up) {
            praiseComment(mData.getId());
        }
//        else {
//            //点击打开评论
//            mTvCommentContent.setMaxLines(Integer.MAX_VALUE);
//            mTvCommentContent.setText(openStrs[1]);
//        }
    }

    /**
     * @param id 评论id
     *           评论点赞
     */
    private void praiseComment(String id) {
        new CommentPraiseTask(new APIExpandCallBack<BaseInnerData>() {
            @Override
            public void onSuccess(BaseInnerData stateBean) {
                if (stateBean == null) {
                    return;
                }
                if (stateBean.getResultCode() == 0) {
                    BizUtils.switchSelectorAnim(mThumb, true);//设置点赞动画
                    mThumb.setSelected(true);
                    mData.setLike_count((mData.getLike_count() + 1));
                    mThumb.setText(mData.getLike_count() + "");
                } else {
                    T.showShort(itemView.getContext(), stateBean.getResultMsg());
                }
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(itemView.getContext(), errMsg);
            }
        }).setTag(UIUtils.getActivity()).exe(id);
    }

//    @Override
//    public void onLineCount(int lineCount, int maxLines) {
//        if (mTvCommentContent == null || mTvCommentContent == null) return;
//
//        if (lineCount > 3 && maxLines != 3) {
//            mTvCommentContent.setText(openStrs[1]);
//            mTvCommentContent.setVisibility(View.VISIBLE);
//        } else if (lineCount < 3) {
//            mTvCommentContent.setVisibility(View.GONE);
//        }
//    }
}