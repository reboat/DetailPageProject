package com.zjrb.zjxw.detailproject.holder;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.uimode.utils.UiModeUtils;
import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.api.callback.LocationCallBack;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.ui.widget.dialog.ConfirmDialog;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.comment.CommentActivity;
import com.zjrb.zjxw.detailproject.comment.CommentSelectActivity;
import com.zjrb.zjxw.detailproject.nomaldetail.NewsDetailActivity;
import com.zjrb.zjxw.detailproject.task.CommentDeleteTask;
import com.zjrb.zjxw.detailproject.task.CommentPraiseTask;
import com.zjrb.zjxw.detailproject.topic.ActivityTopicActivity;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import java.util.regex.Pattern;

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
public class DetailCommentHolder extends BaseRecyclerViewHolder<HotCommentsBean> implements ConfirmDialog.OnConfirmListener, LocationCallBack {
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

    /**
     * 稿件id
     */
    private String articleId;
    //弹框
    private ConfirmDialog dialog;

    private DraftDetailBean mBean;

    private String pageType = "新闻详情页";
    private String scPageType = "新闻详情页";

    /**
     * 评论标签
     */
    private String comment_tag = "";
    /**
     * 剩余内容
     */
    private String no_comment_tag = "";

    /**
     * 评论标签正则表达式
     */
    private final String COMMENT_TAG = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";

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

    //TODO  WLJ 可以减少id的参数，直接传bean

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
            mTvCommentLocation.setVisibility(View.GONE);
            mTvCommentSrc.setVisibility(View.GONE);
        } else {//显示正常评论
            mTvDeleteTip.setVisibility(View.GONE);
            mTvCommentContent.setVisibility(View.VISIBLE);
            mLyComment.setVisibility(View.VISIBLE);
            //回复者评论
            if (mData.getContent() != null) {
                doCommentTag(mData.getContent());
                if (ThemeMode.isNightMode()) {
                    mContent.setText(Html.fromHtml("<font color=#4b7aae>" + comment_tag + "</font>" + "<font color=#5c5c5c>" + no_comment_tag + "</font>"));
                } else {
                    mContent.setText(Html.fromHtml("<font color=#036ce2>" + comment_tag + "</font>" + "<font color=#000000>" + no_comment_tag + "</font>"));
                }
//                mContent.setText(mData.getContent());
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
                mTvCommentLocation.setVisibility(View.GONE);
            } else {
                mTvCommentContent.setVisibility(View.VISIBLE);
                mLyComment.setVisibility(View.VISIBLE);
                mReply.setVisibility(View.VISIBLE);

                //父评论内容
                if (!TextUtils.isEmpty(mData.getParent_content())) {
                    doCommentTag(mData.getParent_content());
                    if (ThemeMode.isNightMode()) {
                        mTvCommentContent.setText(Html.fromHtml("<font color=#4b7aae>" + comment_tag + "</font>" + "<font color=#5c5c5c>" + no_comment_tag + "</font>"));
                    } else {
                        mTvCommentContent.setText(Html.fromHtml("<font color=#036ce2>" + comment_tag + "</font>" + "<font color=#000000>" + no_comment_tag + "</font>"));
                    }
                }

//              mTvCommentContent.setText(mData.getParent_content());

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
            mThumb.setText(mData.getLike_count() + "");
        } else {
            mThumb.setText("");
        }
        //是否已点赞
        mThumb.setSelected(mData.isLiked() == true);

        if (!TextUtils.isEmpty(mData.getLocation())) { // 评论位置
            mLocation.setText(mData.getLocation());
            // 计算最大location最大展示宽度
            // 左侧宽度 11+36+11   右侧留白 12  中间: 时间宽度+10+位置宽度+5+点赞宽度+删除按钮宽度
            int maxWidth = UIUtils.getScreenW() - UIUtils.dip2px(85) - measureTextWidth(mThumb) - measureTextWidth(mThumb) - measureTextWidth(mDelete);
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

    @OnClick({R2.id.tv_thumb_up, R2.id.tv_delete, R2.id.ly_replay, R2.id.ly_comment_reply})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
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
                new Analytics.AnalyticsBuilder(itemView.getContext(), "A0123", "A0123", "CommentDeleted", false)
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
                        .newsID(mBean.getArticle().getMlf_id() + "")
                        .selfNewsID(mBean.getArticle().getId() + "")
                        .newsTitle(mBean.getArticle().getDoc_title())
                        .selfChannelID(mBean.getArticle().getChannel_id())
                        .channelName(mBean.getArticle().getChannel_name())
                        .pageType(scPageType)
                        .build()
                        .send();
            }
            dialog.show();
            //回复评论者
        } else if (view.getId() == R.id.ly_replay) {
            if (mBean != null && mBean.getArticle() != null) {
                new Analytics.AnalyticsBuilder(itemView.getContext(), "800003", "800003", "Comment", false)
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
                        .newsID(mBean.getArticle().getMlf_id() + "")
                        .selfNewsID(mBean.getArticle().getId() + "")
                        .newsTitle(mBean.getArticle().getDoc_title())
                        .selfChannelID(mBean.getArticle().getChannel_id())
                        .channelName(mBean.getArticle().getChannel_name())
                        .pageType(scPageType)
                        .commentType("评论")
                        .build()
                        .send();


                Analytics analytics = new Analytics.AnalyticsBuilder(getContext(), "800003", "800003", "Comment", false)
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
                        .setAttachObjectId(mData.getId()).newsID(mBean.getArticle().getMlf_id() + "")
                        .selfNewsID(mBean.getArticle().getId() + "")
                        .newsTitle(mBean.getArticle().getDoc_title())
                        .selfChannelID(mBean.getArticle().getChannel_id())
                        .channelName(mBean.getArticle().getChannel_name())
                        .pageType(scPageType)
                        .commentType("文章")
                        .build();
                try {
                    CommentWindowDialog.newInstance(new CommentDialogBean(articleId, mData.getId(), mData.getNick_name()))
                            .setListen(new RefreshComment())
                            .setLocationCallBack(this)
                            .setWMData(analytics)
                            .show(((FragmentActivity) UIUtils.getActivity()).getSupportFragmentManager(), "CommentWindowDialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    CommentWindowDialog.newInstance(new CommentDialogBean(articleId, mData.getId(), mData.getNick_name()))
                            .setListen(new RefreshComment())
                            .setLocationCallBack(this)
                            .show(((FragmentActivity) UIUtils.getActivity()).getSupportFragmentManager(), "CommentWindowDialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //回复回复者
        } else {
            if (mBean != null && mBean.getArticle() != null) {
                new Analytics.AnalyticsBuilder(itemView.getContext(), "800003", "800003", "Comment", false)
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
                        .setAttachObjectId(mData.getId()).newsID(mBean.getArticle().getMlf_id() + "")
                        .selfNewsID(mBean.getArticle().getId() + "")
                        .newsTitle(mBean.getArticle().getDoc_title())
                        .selfChannelID(mBean.getArticle().getChannel_id())
                        .channelName(mBean.getArticle().getChannel_name())
                        .pageType(scPageType)
                        .commentType("评论")
                        .build()
                        .send();

                Analytics analytics = new Analytics.AnalyticsBuilder(getContext(), "800003", "800003", "Comment", false)
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
                        .setAttachObjectId(mData.getId()).newsID(mBean.getArticle().getMlf_id() + "")
                        .selfNewsID(mBean.getArticle().getId() + "")
                        .newsTitle(mBean.getArticle().getDoc_title())
                        .selfChannelID(mBean.getArticle().getChannel_id())
                        .channelName(mBean.getArticle().getChannel_name())
                        .pageType(scPageType)
                        .commentType("文章")
                        .build();
                CommentWindowDialog.newInstance(new CommentDialogBean(articleId, mData.getParent_id(), mData.getParent_nick_name())).setListen(new RefreshComment()).setLocationCallBack(this).setWMData(analytics).show(((FragmentActivity) UIUtils.getActivity()).getSupportFragmentManager(), "CommentWindowDialog");
            } else {
                CommentWindowDialog.newInstance(new CommentDialogBean(articleId, mData.getParent_id(), mData.getParent_nick_name())).setListen(new RefreshComment()).setLocationCallBack(this).show(((FragmentActivity) UIUtils.getActivity()).getSupportFragmentManager(), "CommentWindowDialog");
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
                T.showShort(itemView.getContext(), "点赞成功");
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(itemView.getContext(), errMsg);
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

    /**
     * 解析标签
     *
     * @param s
     */
    private void doCommentTag(String s) {
        //如果标签有内容，则进行清除
        comment_tag = "";
        no_comment_tag = s;

        if (!TextUtils.isEmpty(s)) {
            for (int i = 0; i < s.length(); i++) {
                if (s.length() == 1 || s.charAt(0) != '#') {
                    break;
                } else {
                    //如果第二个也是#，跳出
                    if (i == 1 && s.charAt(1) == '#') {
                        break;
                    }
                    //循环遍历，找到即退出
                    if (i > 1 && i <= 101 && s.charAt(i) == '#') {
                        //如果匹配
                        if (Pattern.compile(COMMENT_TAG).matcher(s.substring(1, i)).matches()) {
                            //最后一个字符是'#'
                            if (s.length() == i + 1) {
                                comment_tag = s;
                                no_comment_tag = "";
                            } else {
                                comment_tag = s.substring(0, i + 1);
                                no_comment_tag = s.substring(i + 1);
                            }
                            break;
                        }
                    }
                    //超过101位则不再进行判定
                    if (i > 101) {
                        break;
                    }
                }
            }
        }
    }
}