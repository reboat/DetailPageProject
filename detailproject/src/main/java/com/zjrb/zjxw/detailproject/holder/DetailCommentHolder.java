package com.zjrb.zjxw.detailproject.holder;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.uimode.utils.UiModeUtils;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.AppGlideOptions;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.ui.widget.dialog.ConfirmDialog;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.comment.CommentActivity;
import com.zjrb.zjxw.detailproject.comment.CommentSelectActivity;
import com.zjrb.zjxw.detailproject.nomaldetail.NewsDetailActivity;
import com.zjrb.zjxw.detailproject.task.CommentDeleteTask;
import com.zjrb.zjxw.detailproject.task.CommentListTask;
import com.zjrb.zjxw.detailproject.task.CommentPraiseTask;
import com.zjrb.zjxw.detailproject.topic.ActivityTopicActivity;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * 详情页/评论列表item holder
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class DetailCommentHolder extends BaseRecyclerViewHolder<HotCommentsBean> implements ConfirmDialog.OnConfirmListener {
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
    RelativeLayout mReply;
    //原评论内容
    @BindView(R2.id.tv_comment_content)
    TextView mTvCommentContent;
    //原评论者
    @BindView(R2.id.tv_comment_src)
    TextView mTvCommentSrc;
    @BindView(R2.id.tv_delete_tip)
    TextView mTvDeleteTip;
    @BindView(R2.id.iv_host)
    ImageView mIvHost;
    @BindView(R2.id.iv_guest)
    ImageView mIvGuest;
    @BindView(R2.id.ly_comment)
    RelativeLayout mLyComment;

    /**
     * 稿件id
     */
    private String articleId;
    //弹框
    private ConfirmDialog dialog;

    private DraftDetailBean mBean;

    private String pageType = "新闻详情页";

    /**
     * 话题稿专用构造器
     *
     * @param parent
     * @param articleId
     */
    public DetailCommentHolder(ViewGroup parent, String articleId) {
        super(UIUtils.inflate(R.layout.module_detail_item_comment, parent, false));
        ButterKnife.bind(this, itemView);
        this.articleId = articleId;
    }

    /**
     * 评论列表专用构造器
     *
     * @param view
     * @param articleId
     * @param s
     * @param bean
     */
    public DetailCommentHolder(View view, String articleId, String s, DraftDetailBean bean) {
        super(view);
        ButterKnife.bind(this, itemView);
        this.articleId = articleId;
        if (!TextUtils.isEmpty(s)) {
            pageType = s;
        }
        mBean = bean;
    }

    /**
     * 网脉专用构造器
     *
     * @param parent
     * @param articleId
     * @param bean
     */
    public DetailCommentHolder(ViewGroup parent, String articleId, DraftDetailBean bean) {
        super(UIUtils.inflate(R.layout.module_detail_item_comment, parent, false));
        ButterKnife.bind(this, itemView);
        this.articleId = articleId;
        mBean = bean;
    }

    @Override
    public void bindView() {
        //是否是自己发布的评论
        dialog = new ConfirmDialog(itemView.getContext());
        dialog.setOnConfirmListener(this);
        if (mData.isOwn()) {
            mDelete.setVisibility(View.VISIBLE);
        } else {
            mDelete.setVisibility(View.GONE);
        }

        //评论已删除
        if (mData.getStatus() == 3) {
            mTvDeleteTip.setVisibility(View.VISIBLE);
            mTvDeleteTip.setText(itemView.getContext().getString(R.string.module_detail_comment_delete_tip));
            mTvCommentContent.setVisibility(View.GONE);
            mLyComment.setVisibility(View.GONE);
        } else {//显示正常评论
            mTvDeleteTip.setVisibility(View.GONE);
            mTvCommentContent.setVisibility(View.VISIBLE);
            mLyComment.setVisibility(View.VISIBLE);
            //回复者评论
            if (mData.getContent() != null) {
                mContent.setText(mData.getContent());
            }
            //回复者昵称
            if (mData.getAccount_type() == 1) {//主持人
                mIvHost.setVisibility(View.VISIBLE);
                UiModeUtils.applyImageSrc(mIvHost, R.attr.module_detail_activity_host);
            } else if (mData.getAccount_type() == 2) {//嘉宾
                mIvHost.setVisibility(View.VISIBLE);
                UiModeUtils.applyImageSrc(mIvHost, R.attr.module_detail_activity_guest);
            } else if (mData.getAccount_type() == 3) {
                if (mData.getNick_name() != null) {
                    mIvHost.setVisibility(View.GONE);
                }
            }
            mName.setText(mData.getNick_name());

        }


        //父评论
        if (!TextUtils.isEmpty(mData.getParent_content())) {
            if (mData.getParent_status() == 3) {
                mReply.setVisibility(View.VISIBLE);
                mTvDeleteTip.setVisibility(View.VISIBLE);
                mTvDeleteTip.setText(itemView.getContext().getString(R.string.module_detail_comment_delete_tip));
                mTvCommentContent.setVisibility(View.GONE);
                mLyComment.setVisibility(View.GONE);
            } else {
                mTvCommentContent.setVisibility(View.VISIBLE);
                mLyComment.setVisibility(View.VISIBLE);
                mReply.setVisibility(View.VISIBLE);
                mTvCommentContent.setText(mData.getParent_content());
                //父评论昵称
                if (mData.getParent_account_type() == 1) {//主持人
                    mIvGuest.setVisibility(View.VISIBLE);
                    UiModeUtils.applyImageSrc(mIvGuest, R.attr.module_detail_activity_host);
                } else if (mData.getParent_account_type() == 2) {//嘉宾
                    mIvGuest.setVisibility(View.VISIBLE);
                    UiModeUtils.applyImageSrc(mIvGuest, R.attr.module_detail_activity_guest);
                } else if (mData.getAccount_type() == 3) {
                    if (mData.getParent_nick_name() != null) {
                        mIvGuest.setVisibility(View.GONE);
                    }
                }
                mTvCommentSrc.setText(mData.getParent_nick_name());
            }
        } else {
            mReply.setVisibility(View.GONE);
        }

        //时间显示
        mTime.setText(mData.getCommentTime(mData.getCreated_at()));

        //点赞次数
        if (mData.getLike_count() != 0) {
            mThumb.setText(mData.getLike_count() + "");
        } else {
            mThumb.setText("");
        }
        //是否已点赞
        mThumb.setSelected(mData.isLiked() == true);
        //回复者头像(显示默认头像)
        if(mData != null && !TextUtils.isEmpty(mData.getPortrait_url())){
            GlideApp.with(mImg).load(mData.getPortrait_url()).centerCrop().into(mImg);
        }

    }

    @OnClick({R2.id.tv_thumb_up, R2.id.tv_delete, R2.id.ly_replay, R2.id.ly_comment_reply})
    public void onClick(View view) {
        //点赞
        if (view.getId() == R.id.tv_thumb_up) {
            if (!mData.isLiked()) {
                praiseComment(mData.getId());
            } else {
                //已点赞
                T.showShortNow(itemView.getContext(), itemView.getContext().getString(R.string.module_detail_you_have_liked));
            }
        } else if (view.getId() == R.id.tv_delete) {
            //弹框
            if (mBean != null && mBean.getArticle() != null && mData != null) {
                new Analytics.AnalyticsBuilder(itemView.getContext(), "A0123", "A0123")
                        .setEvenName("删除评论")
                        .setObjectID(mBean.getArticle().getMlf_id())
                        .setObjectName(mBean.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mBean.getArticle().getChannel_id())
                        .setClassifyName(mBean.getArticle().getChannel_name())
                        .setPageType(pageType)
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mBean.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(mBean.getArticle().getId() + "")
                        .setAttachObjectId(mData.getId())
                        .build()
                        .send();
            }
            dialog.show();
            //回复评论者
        } else if (view.getId() == R.id.ly_replay) {
            if (mBean != null && mBean.getArticle() != null) {
                new Analytics.AnalyticsBuilder(itemView.getContext(), "800003", "800003")
                        .setEvenName("热门评论点击回复")
                        .setObjectID(mBean.getArticle().getMlf_id())
                        .setObjectName(mBean.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mBean.getArticle().getChannel_id())
                        .setClassifyName(mBean.getArticle().getChannel_name())
                        .setPageType(pageType)
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mBean.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(mBean.getArticle().getId() + "")
                        .setAttachObjectId(mData.getId())
                        .build()
                        .send();


                Analytics analytics = new Analytics.AnalyticsBuilder(getContext(), "800003", "800003")
                        .setEvenName("回复评论，且发送成功")
                        .setObjectID(mBean.getArticle().getMlf_id() + "")
                        .setObjectName(mBean.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mBean.getArticle().getChannel_id())
                        .setClassifyName(mBean.getArticle().getChannel_name())
                        .setPageType(pageType)
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mBean.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(mBean.getArticle().getId() + "")
                        .setAttachObjectId(mData.getId())
                        .build();
                CommentWindowDialog.newInstance(new CommentDialogBean(articleId, mData.getId(), mData.getNick_name())).setWMData(analytics).show(((FragmentActivity) UIUtils.getActivity()).getSupportFragmentManager(), "CommentWindowDialog");
            } else {
                CommentWindowDialog.newInstance(new CommentDialogBean(articleId, mData.getId(), mData.getNick_name())).show(((FragmentActivity) UIUtils.getActivity()).getSupportFragmentManager(), "CommentWindowDialog");
            }

            //回复回复者
        } else {
            if (mBean != null && mBean.getArticle() != null) {
                new Analytics.AnalyticsBuilder(itemView.getContext(), "800003", "800003")
                        .setEvenName("热门评论点击回复")
                        .setObjectID(mBean.getArticle().getMlf_id() + "")
                        .setObjectName(mBean.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mBean.getArticle().getChannel_id())
                        .setClassifyName(mBean.getArticle().getChannel_name())
                        .setPageType(pageType)
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mBean.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(mBean.getArticle().getId() + "")
                        .setAttachObjectId(mData.getId())
                        .build()
                        .send();

                Analytics analytics = new Analytics.AnalyticsBuilder(getContext(), "800003", "800003")
                        .setEvenName("回复评论，且发送成功")
                        .setObjectID(mBean.getArticle().getMlf_id() + "")
                        .setObjectName(mBean.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mBean.getArticle().getChannel_id())
                        .setClassifyName(mBean.getArticle().getChannel_name())
                        .setPageType(pageType)
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mBean.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(mBean.getArticle().getId() + "")
                        .setAttachObjectId(mData.getId())
                        .build();
                CommentWindowDialog.newInstance(new CommentDialogBean(articleId, mData.getParent_id(), mData.getParent_nick_name())).setWMData(analytics).show(((FragmentActivity) UIUtils.getActivity()).getSupportFragmentManager(), "CommentWindowDialog");
            } else {
                CommentWindowDialog.newInstance(new CommentDialogBean(articleId, mData.getParent_id(), mData.getParent_nick_name())).show(((FragmentActivity) UIUtils.getActivity()).getSupportFragmentManager(), "CommentWindowDialog");
            }
        }
    }

    /**
     * @param comment_id 评论id
     *                   评论点赞
     */
    private void praiseComment(String comment_id) {
        new CommentPraiseTask(new APIExpandCallBack<Void>() {
            @Override
            public void onSuccess(Void stateBean) {
                BizUtils.switchSelectorAnim(mThumb, true);//设置点赞动画
                mThumb.setSelected(true);
                mData.setLike_count((mData.getLike_count() + 1));
                mData.setLiked(true);
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
    private void deleteComment(final String comment_id, final int position) {
        new CommentDeleteTask(new APIExpandCallBack<Void>() {
            @Override
            public void onSuccess(Void stateBean) {
                if (itemView.getContext() instanceof CommentActivity) {
                    ((CommentActivity) itemView.getContext()).onDeleteComment(position);
                } else if (itemView.getContext() instanceof NewsDetailActivity) {
                    ((NewsDetailActivity) itemView.getContext()).onDeleteComment(position);
                } else if (itemView.getContext() instanceof ActivityTopicActivity) {
                    ((ActivityTopicActivity) itemView.getContext()).onDeleteComment(position);
                } else if (itemView.getContext() instanceof CommentSelectActivity) {
                    ((CommentSelectActivity) itemView.getContext()).onDeleteComment(position);
                }
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(itemView.getContext(), errMsg);
            }
        }).setTag(UIUtils.getActivity()).exe(comment_id);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onOK() {
        deleteComment(mData.getId(), getAdapterPosition());
    }

    /**
     * 删除评论回调
     */
    public interface deleteCommentListener {

        void onDeleteComment(int position);
    }
}