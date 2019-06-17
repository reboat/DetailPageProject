package com.zjrb.zjxw.detailproject.ui.nomaldetail.holder;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.uimode.mode.Attr;
import com.aliya.uimode.utils.UiModeUtils;
import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.apibean.task.CommentDeleteTask;
import com.zjrb.zjxw.detailproject.apibean.task.CommentPraiseTask;
import com.zjrb.zjxw.detailproject.ui.comment.CommentActivity;
import com.zjrb.zjxw.detailproject.ui.comment.CommentSelectActivity;
import com.zjrb.zjxw.detailproject.ui.mediadetail.VideoDetailActivity;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.NewsDetailActivity;
import com.zjrb.zjxw.detailproject.ui.subject.SpecialActivity;
import com.zjrb.zjxw.detailproject.ui.topic.ActivityTopicActivity;
import com.zjrb.zjxw.detailproject.utils.BizUtils;
import com.zjrb.zjxw.detailproject.utils.CommentTagMathUtils;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;
import com.zjrb.zjxw.detailproject.widget.ConfirmDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.model.CommentDialogBean;
import cn.daily.news.biz.core.ui.dialog.CommentWindowDialog;
import cn.daily.news.biz.core.ui.toast.ZBToast;
import cn.daily.news.biz.core.utils.TypeFaceUtils;

/**
 * 详情页/评论列表item holder
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class DetailCommentHolder extends BaseRecyclerViewHolder<HotCommentsBean> implements ConfirmDialog.OnConfirmListener, CommentWindowDialog.LocationCallBack {
    @BindView(R2.id.iv_avatar)
    ImageView mImg;
    @BindView(R2.id.iv_prised)
    ImageView mThumb;
    @BindView(R2.id.tv_prise_num)
    TextView mPriseNum;
    @BindView(R2.id.tv_name)
    TextView mName;
    @BindView(R2.id.tv_content)
    TextView mContent;
    @BindView(R2.id.tv_time)
    TextView mTime;
    @BindView(R2.id.tv_location)
    TextView mLocation;
    @BindView(R2.id.tv_comment_location)
    TextView mTvCommentLocation;
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
    @BindView(R2.id.tv_show_all)
    TextView tvShowAll;
    @BindView(R2.id.tv_parent_show_all)
    TextView tvParentShowAll;
    @BindView(R2.id.tv_reply)
    TextView tvReply;

    /**
     * 稿件id
     */
    private String articleId;
    //弹框
    private ConfirmDialog dialog;

    private DraftDetailBean mBean;

    private String pageType = "新闻详情页";
    private String scPageType = "新闻详情页";
    public static final int MAX_DEFAULT_LINES = 5;
    //最新评论还是热门评论
    private String commentType = "";
    private boolean isVoiceOfMass = false;

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
            if (s.equals("评论页")) {
                scPageType = "评论列表页";
            }
        }
        mBean = bean;
    }

    //是否是群众之声
    public void setVoiceOfMass(boolean voiceOfMass) {
        isVoiceOfMass = voiceOfMass;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
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

    /**
     * 继承专用构造器
     */
    protected DetailCommentHolder(View view) {
        super(view);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        //是否是自己发布的评论
        dialog = new ConfirmDialog(itemView.getContext());
        dialog.setOnConfirmListener(this);
        if (mData.isOwn()) {
            tvReply.setVisibility(View.GONE);
            mDelete.setVisibility(View.VISIBLE);
        } else {
            tvReply.setVisibility(View.VISIBLE);
            mDelete.setVisibility(View.GONE);
        }

        //评论已删除
        if (mData.getStatus() == 3) {
            mTvDeleteTip.setVisibility(View.VISIBLE);
            mTvDeleteTip.setText(itemView.getContext().getString(R.string.module_detail_comment_delete_tip));
            mTvCommentContent.setVisibility(View.GONE);
            mLyComment.setVisibility(View.GONE);
            mTvCommentLocation.setVisibility(View.GONE);
            mTvCommentSrc.setVisibility(View.GONE);
        } else {//显示正常评论
            mTvDeleteTip.setVisibility(View.GONE);
            mTvCommentContent.setVisibility(View.VISIBLE);
            mLyComment.setVisibility(View.VISIBLE);

            //回复者评论
            if (mData.getContent() != null) {
                mContent.setText(CommentTagMathUtils.newInstance().doCommentTag(mData.getContent()) != null ? CommentTagMathUtils.newInstance().doCommentTag(mData.getContent()) : mData.getContent());
                if (mData.getContent().equals("当前评论正在审核中")) {
                    mContent.setTextColor(Color.parseColor("#bfbfbf"));
                    mContent.setTextSize(18);
                } else {
                    mContent.setTextColor(Color.parseColor("#222222"));
                    mContent.setTextSize(16);
                }

            }
            //超过5行
            mContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mContent.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (mContent.getLineCount() >= 5) {
                        mContent.setMaxLines(MAX_DEFAULT_LINES);
                        tvShowAll.setVisibility(View.VISIBLE);
                        tvShowAll.setText("展开全部");
                    } else {
                        tvShowAll.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
            //回复者昵称
            if (!TextUtils.isEmpty(mData.getNick_name())) {
                if (mData.getAccount_type() == 1) {//主持人
                    mIvHost.setVisibility(View.VISIBLE);
                    UiModeUtils.applySave(mIvHost, Attr.NAME_SRC, R.mipmap.module_detail_activity_host);
                } else if (mData.getAccount_type() == 2) {//嘉宾
                    mIvHost.setVisibility(View.VISIBLE);
                    UiModeUtils.applySave(mIvHost, Attr.NAME_SRC, R.mipmap.module_detail_activity_guest);
                } else if (mData.getAccount_type() == 3) {
                    if (mData.getNick_name() != null) {
                        mIvHost.setVisibility(View.GONE);
                    }
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
                mTvCommentLocation.setVisibility(View.GONE);
            } else {
                mTvCommentContent.setVisibility(View.VISIBLE);
                mLyComment.setVisibility(View.VISIBLE);
                mReply.setVisibility(View.VISIBLE);

                //父评论内容
                if (!TextUtils.isEmpty(mData.getParent_content())) {
                    mTvCommentContent.setText(CommentTagMathUtils.newInstance().doCommentTag(mData.getParent_content()) != null ? CommentTagMathUtils.newInstance().doCommentTag(mData.getParent_content()) : mData.getParent_content());
                }

                //父评论超过5行
                mTvCommentContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mTvCommentContent.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (mTvCommentContent.getLineCount() >= 5) {
                            mTvCommentContent.setMaxLines(MAX_DEFAULT_LINES);
                            tvParentShowAll.setVisibility(View.VISIBLE);
                            tvParentShowAll.setText("展开全部");
                        } else {
                            tvParentShowAll.setVisibility(View.GONE);
                        }
                        return false;
                    }
                });
                //父评论昵称
                if (!TextUtils.isEmpty(mData.getParent_nick_name())) {
                    if (mData.getParent_account_type() == 1) {//主持人
                        mIvGuest.setVisibility(View.VISIBLE);
                        UiModeUtils.applySave(mIvGuest, Attr.NAME_SRC, R.mipmap.module_detail_activity_host);
                    } else if (mData.getParent_account_type() == 2) {//嘉宾
                        mIvGuest.setVisibility(View.VISIBLE);
                        UiModeUtils.applySave(mIvGuest, Attr.NAME_SRC, R.mipmap.module_detail_activity_guest);
                    } else if (mData.getAccount_type() == 3) {
                        if (mData.getParent_nick_name() != null) {
                            mIvGuest.setVisibility(View.GONE);
                        }
                    }
                }
                mTvCommentSrc.setText(mData.getParent_nick_name());
                if (!TextUtils.isEmpty(mData.getParent_location())) {
                    mTvCommentLocation.setText(mData.getParent_location());
                    // 计算location最大宽度  左侧宽度 11+36+11   右侧留白 12  中间: 左侧7dp 右侧12dp
                    int maxWidth = UIUtils.getScreenW() - UIUtils.dip2px(89);
                    mTvCommentLocation.setMaxWidth(maxWidth);
                    mTvCommentLocation.setVisibility(View.VISIBLE);
                } else {
                    mTvCommentLocation.setVisibility(View.GONE);
                }
            }
        } else {
            mReply.setVisibility(View.GONE);
        }

        //时间显示
        mTime.setText(mData.getCommentTime(mData.getCreated_at()));

        //点赞次数
        if (mData.getLike_count() != 0) {
            mPriseNum.setVisibility(View.VISIBLE);
            mPriseNum.setText(mData.getLike_count() + "赞");
        } else {
            mPriseNum.setVisibility(View.GONE);
            mPriseNum.setText("");
        }
        TypeFaceUtils.formatNumToDin(mPriseNum);
        //是否已点赞
        mThumb.setSelected(mData.isLiked() == true);
        //标红
        if(mData.isLiked()){
            mPriseNum.setTextColor(Color.parseColor("#d12324"));
        }

        if (!TextUtils.isEmpty(mData.getLocation())) { // 评论位置
            mLocation.setText(mData.getLocation());
            // 计算最大location最大展示宽度
            // 左侧宽度 11+36+11   右侧留白 12  中间: 时间宽度+10+位置宽度+5+点赞宽度+删除按钮宽度
            int maxWidth = UIUtils.getScreenW() - UIUtils.dip2px(85) - measureTextWidth(mPriseNum) - measureTextWidth(mPriseNum) - measureTextWidth(mDelete);
            mLocation.setMaxWidth(maxWidth);
            mLocation.setVisibility(View.VISIBLE);
        } else {
            mLocation.setVisibility(View.GONE);
        }

        //回复者头像(显示默认头像)
        if (mData != null && !TextUtils.isEmpty(mData.getPortrait_url())) {
            GlideApp.with(mImg).load(mData.getPortrait_url()).centerCrop().into(mImg);
        }

    }

    @OnClick({R2.id.iv_prised, R2.id.ly_prise, R2.id.tv_delete, R2.id.ly_replay, R2.id.ly_comment_reply, R2.id.tv_reply, R2.id.tv_show_all, R2.id.tv_parent_show_all})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        //评论点赞
        if (view.getId() == R.id.iv_prised || view.getId() == R.id.ly_prise) {
            if (!mData.isLiked()) {
                DataAnalyticsUtils.get().CommentPrise(mBean, pageType, scPageType, mData.getId());
                praiseComment(mData.getId());
            } else {
                //已点赞
                ZBToast.showShort(itemView.getContext(), itemView.getContext().getString(R.string.module_detail_you_have_liked));
            }
        } else if (view.getId() == R.id.tv_delete) {
            //弹框
            if (mBean != null && mBean.getArticle() != null && mData != null) {
                DataAnalyticsUtils.get().DeletedComment(mBean, pageType, scPageType, mData.getId());
            }
            dialog.show();
            //回复评论者
        } else if (view.getId() == R.id.tv_reply || view.getId() == R.id.ly_replay) {
            if (!mData.isOwn()) {
                if (mBean != null && mBean.getArticle() != null) {
                    if (!TextUtils.isEmpty(commentType)) {
                        if (commentType.equals("热门评论")) {
                            DataAnalyticsUtils.get().HotCommentClick(mBean, pageType, scPageType, mData.getId());
                        } else {
                            DataAnalyticsUtils.get().NewCommentClick(mBean, pageType, scPageType, mData.getId());
                        }
                    } else {
                        DataAnalyticsUtils.get().HotCommentClick(mBean, pageType, scPageType, mData.getId());
                    }
                    Analytics analytics = DataAnalyticsUtils.get().CreateCommentSend(mBean, pageType, scPageType, mData.getId());
                    try {
                        //群众之声评论传递稿件id
                        String id;
                        if (isVoiceOfMass) {
                            id = mData.getChannel_article_id() + "";
                        } else {
                            id = articleId;
                        }
                        CommentWindowDialog.newInstance(new CommentDialogBean(id, mData.getId(), mData.getNick_name()))
                                .setListen(new RefreshComment())
                                .setLocationCallBack(this)
                                .setWMData(analytics)
                                .show(((FragmentActivity) UIUtils.getActivity()).getSupportFragmentManager(), "CommentWindowDialog");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String id;
                    if (isVoiceOfMass) {
                        id = mData.getChannel_article_id() + "";
                    } else {
                        id = articleId;
                    }
                    try {
                        CommentWindowDialog.newInstance(new CommentDialogBean(id, mData.getId(), mData.getNick_name()))
                                .setListen(new RefreshComment())
                                .setLocationCallBack(this)
                                .show(((FragmentActivity) UIUtils.getActivity()).getSupportFragmentManager(), "CommentWindowDialog");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //评论展开全部
        } else if (view.getId() == R.id.tv_show_all) {
            if (tvShowAll.getText().equals("展开全部")) {
                mContent.setMaxLines(Integer.MAX_VALUE);
                tvShowAll.setText("收起");
            } else {
                mContent.setMaxLines(MAX_DEFAULT_LINES);
                tvShowAll.setText("展开全部");
            }
            //评论回复展开全部
        } else if (view.getId() == R.id.tv_parent_show_all) {
            if (tvParentShowAll.getText().equals("展开全部")) {
                mTvCommentContent.setMaxLines(Integer.MAX_VALUE);
                tvParentShowAll.setText("收起");
            } else {
                mTvCommentContent.setMaxLines(MAX_DEFAULT_LINES);
                tvParentShowAll.setText("展开全部");
            }
        } else {//回复回复者 不可以对自己回复
            if (!mData.isParent_own()) {
                if (mBean != null && mBean.getArticle() != null) {
                    if (!TextUtils.isEmpty(commentType)) {
                        if (commentType.equals("热门评论")) {
                            DataAnalyticsUtils.get().HotCommentClick(mBean, pageType, scPageType, mData.getId());
                        } else {
                            DataAnalyticsUtils.get().NewCommentClick(mBean, pageType, scPageType, mData.getId());
                        }
                    } else {
                        DataAnalyticsUtils.get().HotCommentClick(mBean, pageType, scPageType, mData.getId());
                    }
                    Analytics analytics = DataAnalyticsUtils.get().CreateCommentSend(mBean, pageType, scPageType, mData.getId());
                    CommentWindowDialog.newInstance(new CommentDialogBean(articleId, mData.getParent_id(), mData.getParent_nick_name())).setListen(new RefreshComment()).setLocationCallBack(this).setWMData(analytics).show(((FragmentActivity) UIUtils.getActivity()).getSupportFragmentManager(), "CommentWindowDialog");
                } else {
                    CommentWindowDialog.newInstance(new CommentDialogBean(articleId, mData.getParent_id(), mData.getParent_nick_name())).setListen(new RefreshComment()).setLocationCallBack(this).show(((FragmentActivity) UIUtils.getActivity()).getSupportFragmentManager(), "CommentWindowDialog");
                }
            }
        }
    }

    /**
     * @param comment_id 评论id
     *                   评论点赞
     */
    private void praiseComment(String comment_id) {
        new CommentPraiseTask(new LoadingCallBack<Void>() {
            @Override
            public void onSuccess(Void stateBean) {
                BizUtils.switchSelectorAnim(mThumb, true);//设置点赞动画
                mThumb.setSelected(true);
                mData.setLike_count((mData.getLike_count() + 1));
                mData.setLiked(true);
                if (mPriseNum.getVisibility() == View.GONE) {
                    mPriseNum.setVisibility(View.VISIBLE);
                }
                mPriseNum.setText(mData.getLike_count() + "赞");
                mPriseNum.setTextColor(Color.parseColor("#d12324"));
                ZBToast.showShort(itemView.getContext(), "点赞成功");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                ZBToast.showShort(itemView.getContext(), errMsg);
            }
        }).setTag(UIUtils.getActivity()).exe(comment_id);
    }

    /**
     * 测量textview宽度
     *
     * @param tv
     * @return
     */
    private int measureTextWidth(TextView tv) {
        if (tv == null) return 0;
        tv.measure(0, 0);
        return tv.getMeasuredWidth();
    }

    /**
     * 删除评论
     *
     * @param comment_id
     */
    private void deleteComment(final String comment_id, final int position) {
        new CommentDeleteTask(new LoadingCallBack<Void>() {
            @Override
            public void onSuccess(Void stateBean) {
                if (itemView.getContext() instanceof CommentActivity) {//评论列表
                    ((CommentActivity) itemView.getContext()).onDeleteComment(position);
                } else if (itemView.getContext() instanceof NewsDetailActivity) {//详情评论
                    ((NewsDetailActivity) itemView.getContext()).onDeleteComment(position);
                } else if (itemView.getContext() instanceof ActivityTopicActivity) {//话题评论
                    ((ActivityTopicActivity) itemView.getContext()).onDeleteComment(position);
                } else if (itemView.getContext() instanceof CommentSelectActivity) {//精选评论
                    ((CommentSelectActivity) itemView.getContext()).onDeleteComment(position);
                } else if (itemView.getContext() instanceof VideoDetailActivity) {//视频/直播稿件删除
                    ((VideoDetailActivity) itemView.getContext()).onDeleteComment(position);
                } else if (itemView.getContext() instanceof SpecialActivity) {//专题稿评论删除
                    ((SpecialActivity) itemView.getContext()).onDeleteComment(position);
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                ZBToast.showShort(itemView.getContext(), "删除失败");
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

    static class RefreshComment implements CommentWindowDialog.updateCommentListener {

        @Override
        public void onUpdateComment() {
            LocalBroadcastManager.getInstance(UIUtils.getApp()).sendBroadcast(new Intent("refresh_comment"));
        }
    }

    /**
     * 点击评论时,获取用户所在位置
     */
    @Override
    public String onGetLocation() {
        if (LocationManager.getInstance().getLocation() != null) {
            DataLocation.Address address = LocationManager.getInstance().getLocation().getAddress();
            if (address != null) {
                return address.getCountry() + "," + address.getProvince() + "," + address.getCity();
            } else {
                return "" + "," + "" + "," + "";
            }
        } else {
            return "" + "," + "" + "," + "";
        }
    }
}