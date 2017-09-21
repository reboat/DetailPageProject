package com.zjrb.zjxw.detailproject.holder;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.domain.base.BaseInnerData;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.utils.StringUtils;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.CommentPriseBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.eventBus.CommentDeleteEvent;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.task.CommentDeleteTask;
import com.zjrb.zjxw.detailproject.task.CommentPraiseTask;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 详情页/评论列表item holder
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class DetailCommentHolder extends BaseRecyclerViewHolder<HotCommentsBean> {
    @BindView(R2.id.ly_replay)
    RelativeLayout mLayReplay;
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
    @BindView(R2.id.tv_delete_tip)
    TextView mTvDeleteTip;

    /**
     * 稿件id
     */
    private String articleId;

    public DetailCommentHolder(View itemView, String articleId) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.articleId = articleId;
    }

    @Override
    public void bindView() {
        //是否是自己发布的评论
        if (mData.isOwn()) {
            mDelete.setVisibility(View.VISIBLE);
        }

        //评论已删除
        if (mData.getStatus() == 3) {
            mTvDeleteTip.setVisibility(View.VISIBLE);
            mTvCommentContent.setVisibility(View.GONE);
            mTvCommentSrc.setVisibility(View.GONE);
            mTvDeleteTip.setText(itemView.getContext().getString(R.string.module_detail_comment_delete_tip));
        }

        //回复者的评论
        if (mData.getContent() != null) {
            mContent.setText(mData.getContent());
        }
        //回复者昵称
        if (mData.getNick_name() != null) {
            mName.setText(mData.getNick_name());
        }

        mTime.setText(StringUtils.long2String(mData.getCreated_at(), "MM-dd HH:mm:ss"));
        //我的评论
        if (mData.getParent_content() != null) {
            mTvCommentContent.setText(mData.getParent_content());
        }

        //点赞次数和是否已点赞
        mThumb.setText(mData.getLike_count() + "");
        mThumb.setSelected(mData.isLiked() == true);
        //回复者头像
        GlideApp.with(mImg).load(mData.getPortrait_url()).centerCrop().into(mImg);

        //我的昵称
        if (mData.getParent_nick_name() != null) {
            mTvCommentSrc.setText(mData.getParent_nick_name());
        }

    }

    @OnClick({R2.id.tv_thumb_up, R2.id.tv_delete, R2.id.ly_replay, R2.id.ly_comment_reply})
    public void onClick(View view) {
        //点赞
        if (view.getId() == R.id.tv_thumb_up) {
            praiseComment(mData.getId());
        } else if (view.getId() == R.id.tv_delete) {
            deleteComment(mData.getId());
            //回复评论者
        } else if (view.getId() == R.id.ly_replay) {
            Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/CommentWindowActivity")
                    .buildUpon()
                    .appendQueryParameter(Key.ARTICLE_ID, articleId + "")
                    .appendQueryParameter(Key.CONENT, mData.getContent() + "")
                    .appendQueryParameter(Key.PARENT_ID, mData.getId() + "")
                    .appendQueryParameter(Key.REPLAYER, mData.getNick_name() + "")
                    .build(), 0);
            //回复回复者
        } else {
            Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/CommentWindowActivity")
                    .buildUpon()
                    .appendQueryParameter(Key.ARTICLE_ID, articleId + "")
                    .appendQueryParameter(Key.MLF_ID, mData.getParent_content() + "")
                    .appendQueryParameter(Key.PARENT_ID, mData.getParent_id() + "")
                    .appendQueryParameter(Key.REPLAYER, mData.getParent_nick_name() + "")
                    .build(), 0);
        }
    }

    /**
     * @param comment_id 评论id
     *                   评论点赞
     */
    private void praiseComment(String comment_id) {
        new CommentPraiseTask(new APIExpandCallBack<CommentPriseBean>() {
            @Override
            public void onSuccess(CommentPriseBean stateBean) {
                if (stateBean == null) {
                    return;
                }
                BizUtils.switchSelectorAnim(mThumb, true);//设置点赞动画
                mThumb.setSelected(true);
                mData.setLike_count((mData.getLike_count() + 1));
                mThumb.setText(mData.getLike_count() + "");
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(itemView.getContext(), errMsg);
            }
        }).setTag(UIUtils.getActivity()).exe(comment_id);
    }

    /**
     * 删除评论
     *
     * @param comment_id
     */
    private void deleteComment(String comment_id) {
        new CommentDeleteTask(new APIExpandCallBack<BaseInnerData>() {
            @Override
            public void onSuccess(BaseInnerData stateBean) {
                if (stateBean == null) {
                    return;
                }
                if (stateBean.getResultCode() == 0) {
                    EventBus.getDefault().postSticky(new CommentDeleteEvent());
                } else {
                    T.showShort(itemView.getContext(), stateBean.getResultMsg());
                }
            }

            @Override
            public void onError(String errMsg, int errCode) {
                EventBus.getDefault().postSticky(new CommentDeleteEvent());
                T.showShort(itemView.getContext(), errMsg);
            }
        }).setTag(UIUtils.getActivity()).exe(comment_id);
    }

}