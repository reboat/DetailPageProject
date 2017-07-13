package com.zjrb.zjxw.detailproject.comment.adapter;

import android.app.Activity;
import android.support.v4.widget.CircleImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.coreprojectlibrary.ui.widget.ExpandableTextView;
import com.zjrb.coreprojectlibrary.utils.StringUtils;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 评论页Adapter
 *
 * @author a_liYaø
 * @date 16/10/19 08:31.
 */
public class CommentAdapter extends BaseLoadRecyclerAdapter<CommentItemBean, CommentRefreshBean,
        CommentAdapter.CommentViewHolder> {
    private int articleId;  //稿件id
    private static int mlfId;  //媒立方稿件id
    private long lastMinPublishTime;//最后刷新时间

    public CommentAdapter(List<CommentItemBean> datas) {
        super(datas);
        if (datas.size() > 0) {
            lastMinPublishTime = datas.get(datas.size() - 1).getPublishTime();
        }

    }

    @Override
    protected void onLoadMoreSuccess(CommentRefreshBean commentRefreshBean) {
        if (commentRefreshBean == null) {
            return;
        }
        List<CommentItemBean> commentList = commentRefreshBean.getCommentList();
        if (commentList != null && commentList.size() > 0) {
            lastMinPublishTime = getLastMinPublishTime(commentList);//获取最后的刷新时间
        }
        checkAddDatas(commentList);

    }

    private long getLastMinPublishTime(List<CommentItemBean> commentList) {
        return commentList.get(commentList.size() - 1).getPublishTime();
    }

    @Override
    protected void onLoadMore(LoadingCallBack<CommentRefreshBean> loadingCallBack) {
        new ArticleCommentLoadMoreTask(loadingCallBack).exe(articleId, lastMinPublishTime);
    }

    public void setparams(int articleId) {
        this.articleId = articleId;
    }

    public void setMlfparams(int mlfId) {
        this.mlfId = mlfId;
    }

    @Override
    public CommentViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(UIUtils.inflate(R.layout.module_detail_item_comment.xml, parent, false));
    }

    static class CommentViewHolder extends BaseRecyclerViewHolder<CommentItemBean> implements
            ExpandableTextView.OnLineCountListener {

        @BindView(R2.id.iv_avatar)
        CircleImageView mIvAvatar;
        @BindView(R2.id.tv_thumb_up)
        TextView mTvThumbUp;
        @BindView(R2.id.tv_name)
        TextView mTvName;
        @BindView(R2.id.tv_time)
        TextView mTvTime;
        @BindView(R2.id.tv_comment_content)
        ExpandableTextView mTvCommentContent;
        @BindView(R2.id.tv_open)
        TextView mTvOpen;
        private String[] openStrs = {"显示完整评论", "收起评论"};

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mTvCommentContent.setOnLineCountListener(this);
        }

        @Override
        public void bindView() {
            mTvName.setText(mData.getCommentUserNickName());
            mTvTime.setText(StringUtils.long2String(mData.getPublishTime(), "MM-dd HH:mm:ss"));
            mTvCommentContent.setText(mData.getContent());
            mTvCommentContent.setMaxLines(Integer.MAX_VALUE);
            mTvThumbUp.setText(mData.getPraiseSum() + "");
            mTvThumbUp.setSelected(mData.getIsPraised() == 1);

            Glide.with((Activity) itemView.getContext()).load(mData.getCommentUserIconUrl())
                    .placeholder(R.mipmap.default_avatar_icon).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource,
                                            GlideAnimation<? super GlideDrawable> glideAnimation) {
                    mIvAvatar.setImageDrawable(resource);
                }
            });

        }

        @OnClick({R.id.tv_thumb_up, R.id.tv_open})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_thumb_up://点赞评论
                    WmUtil.onLikeCommentList(mlfId, mData);
                    praiseComment(mData.getId());
                    break;
                case R.id.tv_open:
                    if (mData.isOpen()) {
                        mTvCommentContent.setMaxLines(3);
                        mTvOpen.setText(openStrs[0]);
                    } else {
                        mTvCommentContent.setMaxLines(Integer.MAX_VALUE);
                        mTvOpen.setText(openStrs[1]);
                    }
                    mData.setOpen(!mData.isOpen());
                    break;

            }
        }

        private void praiseComment(int id) {
            new ArticleCommentPraiseTask(new APIExpandCallBack<BaseInnerData>() {
                @Override
                public void onSuccess(BaseInnerData stateBean) {
                    if (stateBean == null) {
                        return;
                    }
                    if (stateBean.getResultCode() == 0) {
                        BizUtils.switchSelectorAnim(mTvThumbUp, true);//设置点赞动画
                        mTvThumbUp.setSelected(true);
                        mData.setPraiseSum((mData.getPraiseSum() + 1));
                        mTvThumbUp.setText(mData.getPraiseSum() + "");
                    } else {
                        T.showShort(itemView.getContext(), stateBean.getResultMsg());
                    }
                }

                @Override
                public void onError(String errMsg, int errCode) {
                    T.showShort(itemView.getContext(), errMsg);
                }
            }).exe(id);
        }

        @Override
        public void onLineCount(int lineCount, int maxLines) {
            if (mTvCommentContent == null || mTvOpen == null) return;

            if (lineCount > 3 && maxLines != 3) {
                if (!mData.isOpen()) {
                    mTvCommentContent.setMaxLines(3);
                    mTvOpen.setText(openStrs[0]);
                } else {
                    mTvOpen.setText(openStrs[1]);
                }
                mTvOpen.setVisibility(View.VISIBLE);
            } else if (lineCount < 3) {
                mTvOpen.setVisibility(View.GONE);
            }
        }
    }

}