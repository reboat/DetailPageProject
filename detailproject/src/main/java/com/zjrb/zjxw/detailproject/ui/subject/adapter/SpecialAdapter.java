package com.zjrb.zjxw.detailproject.ui.subject.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.OverlayViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.daily.news.ui.adapter.NewsBaseAdapter;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.SpecialGroupBean;
import com.zjrb.zjxw.detailproject.apibean.bean.SubjectVoiceMassBean;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.ui.subject.SpecialActivity;
import com.zjrb.zjxw.detailproject.ui.subject.holder.SpecialCommentHolder;
import com.zjrb.zjxw.detailproject.ui.subject.holder.SpecialCommentPlaceHolder;
import com.zjrb.zjxw.detailproject.ui.subject.holder.SpecialCommentTabHolder;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.nav.Nav;

/**
 * 专题详情页 - Adapter
 * Created by wanglinjie.
 * create time:2017/8/27  上午10:37.
 */
public class SpecialAdapter extends NewsBaseAdapter {

    // group组名
    public static final int TYPE_GROUP = 100;
    //群众之声
    private final int TYPE_COMMENT_TAB = 101;
    //评论
    private final int TYPE_COMMENT = 102;
    //文章标题
    private final int TYPE_TITLE = 103;
    //占位
    private final int TYPE_PLACE = 104;

    private DraftDetailBean mBean;

    public SpecialAdapter(DraftDetailBean data) {
        super(null);
        setData(data);
    }

    //设置专题数据
    public void setData(DraftDetailBean data) {
        if (data != null && data.getArticle() != null
                && data.getArticle().getSubject_groups() != null) {
            mBean = data;
            List<SpecialGroupBean> groups = data.getArticle().getSubject_groups();
            List list = new ArrayList();
            for (SpecialGroupBean group : groups) {
                list.add(group);
                if (group.getGroup_articles() != null) {
                    //稿件
                    list.addAll(group.getGroup_articles());
                }
            }
            //添加评论
            if (mBean.getArticle().getSubject_comment_list() != null && mBean.getArticle().getSubject_comment_list().size() > 0) {
                //只有有评论数据才添加群众之声
                list.add("群众之声");
                //遍历群众之声评论列表
                for (SubjectVoiceMassBean bean : mBean.getArticle().getSubject_comment_list()) {
                    if (bean.getComment_list() != null && bean.getComment_list().size() > 0) {
                        //只有1条评论的情况
                        if (bean.getComment_list().size() == 1) {
                            //HotCommentsBean类型
                            list.add(bean.getComment_list().get(0));
                            //标题类型
                            if (!TextUtils.isEmpty(bean.getComment_list().get(0).getTitle()) &&
                                    !TextUtils.isEmpty(bean.getComment_list().get(0).getUrl())) {
                                list.add(bean);
                            }
                            //空行
                            list.add("占位");
                            //多条评论的情况
                        } else if (bean.getComment_list().size() > 1) {
                            //评论
                            for (HotCommentsBean hotBean : bean.getComment_list()) {
                                list.add(hotBean);
                            }
                            //标题
                            if (!TextUtils.isEmpty(bean.getComment_list().get(0).getTitle()) &&
                                    !TextUtils.isEmpty(bean.getComment_list().get(0).getUrl())) {
                                list.add(bean);
                            }
                            //空行
                            list.add("占位");
                        }
                    }
                }
            }
            setData(list);
        }
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int
            viewType) {
        //专题分组标签
        if (TYPE_GROUP == viewType) {
            return new GroupViewHolder(parent, mBean);
            //群众之声TAB
        } else if (TYPE_COMMENT_TAB == viewType) {
            return new SpecialCommentTabHolder(parent);
            //评论item
        } else if (TYPE_COMMENT == viewType) {
            //专题稿件id
            DetailCommentHolder holder = new DetailCommentHolder(UIUtils.inflate(R.layout.module_detail_item_comment_subject, parent, false), String.valueOf(mBean.getArticle().getId()), "", mBean);
            holder.setVoiceOfMass(true);
            return holder;
            //评论列表文章
        } else if (TYPE_TITLE == viewType) {
            SpecialCommentHolder holder = new SpecialCommentHolder(parent);
            if (mBean != null && mBean.getArticle() != null) {
                holder.setDetailBean(mBean);
            }
            return holder;
            //占位
        } else if (TYPE_PLACE == viewType) {
            return new SpecialCommentPlaceHolder(parent);
        }
        return super.onAbsCreateViewHolder(parent, viewType);
    }

    @Override
    public OverlayViewHolder onCreateOverlayViewHolder(ViewGroup parent, int viewType) {
        return new GroupViewHolder(parent, mBean);
    }

    //判定item类型
    @Override
    public int getAbsItemViewType(int position) {
        if (getData(position) instanceof SpecialGroupBean) {
            return TYPE_GROUP;
        } else if (getData(position).equals("群众之声")) {
            return TYPE_COMMENT_TAB;
            //评论
        } else if (getData(position) instanceof HotCommentsBean) {
            return TYPE_COMMENT;
            //文章标题
        } else if (getData(position) instanceof SubjectVoiceMassBean) {
            return TYPE_TITLE;
        } else if (getData(position).equals("占位")) {
            return TYPE_PLACE;
        }
        return super.getAbsItemViewType(position);
    }

    @Override
    public boolean isOverlayViewType(int position) {
        return getAbsItemViewType(position) == TYPE_GROUP;
    }

    @Override
    public boolean isVoiceOfMassType(int position) {
        return getAbsItemViewType(position) == TYPE_COMMENT_TAB;
    }

    /**
     * 删除评论
     *
     * @param position
     */
    public void remove(int position) {
        getData().remove(cleanPosition(position));
        notifyItemRemoved(position);
    }

    /**
     * 分组标签名 ViewHolder
     *
     * @author a_liYa
     * @date 2017/10/21 下午4:13.
     */
    static class GroupViewHolder extends OverlayViewHolder<SpecialGroupBean> implements View
            .OnClickListener {

        @BindView(R2.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R2.id.tv_more)
        TextView tvMore;

        private DraftDetailBean mBean;

        /**
         * 网脉埋点专用构造器
         *
         * @param parent
         */
        public GroupViewHolder(ViewGroup parent, DraftDetailBean bean) {
            super(parent, R.layout.module_detail_special_group_name);
            ButterKnife.bind(this, itemView);
            mBean = bean;
        }

        @Override
        public void bindView() {
            tvGroupName.setText(mData.getGroup_name());
            // 显示是否有更多
            tvMore.setVisibility(mData.isGroup_has_more() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void setData(SpecialGroupBean data) {
            super.setData(data);
            if (data != null) {
                itemView.setOnClickListener(data.isGroup_has_more() ? this : null);
            }
        }

        @Override
        public void onClick(View v) {
            if (itemView == v && mData != null) {
                if (mBean != null && mBean.getArticle() != null) {
                    DataAnalyticsUtils.get().SpecialClickMore(mBean);
                }
                //进入专题更多列表
                Bundle bundle = new Bundle();
                bundle.putSerializable(IKey.NEWS_DETAIL, mBean);
                bundle.putSerializable(IKey.DATA, mData);
                Nav.with(v.getContext()).setExtras(bundle).toPath("/news/SpecialMoreActivity", SpecialActivity.REQUEST_CODE_MORE);
            }
        }
    }

}
