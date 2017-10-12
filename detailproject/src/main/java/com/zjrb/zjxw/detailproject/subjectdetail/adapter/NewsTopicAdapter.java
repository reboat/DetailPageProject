package com.zjrb.zjxw.detailproject.subjectdetail.adapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
import com.zjrb.zjxw.detailproject.bean.SubjectNewsBean;
import com.zjrb.zjxw.detailproject.holder.NewsDetailArticleGeneralViewHolder;
import com.zjrb.zjxw.detailproject.subjectdetail.holder.NewsActivityHolder;
import com.zjrb.zjxw.detailproject.subjectdetail.holder.NewsMultiPictureHolder;
import com.zjrb.zjxw.detailproject.subjectdetail.holder.NewsSubjectHolder;
import com.zjrb.zjxw.detailproject.subjectdetail.holder.NewsTextHolder;
import com.zjrb.zjxw.detailproject.subjectdetail.holder.NewsVideoHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新闻专题列表 Adapter(支持所有类型 图文，纯文字，多图，视频，话题，直播)
 * 2-9分别代表普通、链接、图集、专题、话题、活动、直播、视频
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsTopicAdapter extends BaseRecyclerAdapter {

    //group组名
    public static final int TYPE_GROUP = 0;
    //普通
    public static final int TYPE_NOMAL = 1;
    //活动/话题
    public static final int TYPE_ACTIVITY = 2;
    //视频/直播
    public static final int TYPE_VIDEO = 3;
    //文本
    public static final int TYPE_TEXT = 4;
    //多图
    public static final int TYPE_MULTI = 5;
    //专题
    public static final int TYPE_SUBJECT = 6;

    public NewsTopicAdapter() {
        super(new ArrayList<SubjectNewsBean.GroupArticlesBean>());
    }

    /**
     * @param groupList 设置专题详情页分组数据
     */
    public void setupData(List<SubjectNewsBean.GroupArticlesBean> groupList) {
        datas.clear();
        if (groupList != null) {
            for (int i = 0; i < groupList.size(); i++) {
                SubjectNewsBean.GroupArticlesBean group = groupList.get(i);
                SubjectItemBean bean = new SubjectItemBean();
                bean.setList_title(group.getGroupName());
                if (group.getArticleList() != null && !group.getArticleList().isEmpty()) {
                    bean.setSize(group.getArticleList().size());
                } else {
                    bean.setSize(0);
                }
                bean.setId(group.getGroupId());
                bean.setDoc_type(-1);
                datas.add(bean);
                datas.addAll(group.getArticleList());
            }

            for (int i = 0; i < datas.size(); i++) {
                ((SubjectItemBean) datas.get(i)).setPosition(i);
            }
            notifyDataSetChanged();
        }
    }


    /**
     * 理论上专题详情页不存在专题稿件，但是需要给媒立方容错，有传就显示
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int
            viewType) {
        if (TYPE_GROUP == viewType) {
            return new GroupViewHolder(parent);
        } else if (viewType == TYPE_ACTIVITY) {
            return new NewsActivityHolder(parent);
        } else if (viewType == TYPE_TEXT) {
            return new NewsTextHolder(parent);
        } else if (viewType == TYPE_VIDEO) {
            return new NewsVideoHolder(parent);
        } else if (viewType == TYPE_MULTI) {
            return new NewsMultiPictureHolder(parent);
        } else if (viewType == TYPE_SUBJECT) {
            return new NewsSubjectHolder(parent);
        }
        return new NewsDetailArticleGeneralViewHolder(parent);
    }

    @Override
    public int getAbsItemViewType(int position) {
        //组名
        SubjectItemBean b = (SubjectItemBean) datas.get(position);
        if (b.getDoc_type() == -1) {
            return TYPE_GROUP;
            //纯文字
        } else if (b.getList_style() == 1) {
            return TYPE_TEXT;
            //图文
        } else if (b.getList_style() == 2) {
            if (b.getDoc_type() == 2 || b.getDoc_type() == 3
                    || b.getDoc_type() == 4) {//普通稿件
                return TYPE_NOMAL;
            } else if (b.getDoc_type() == 6 || b.getDoc_type() == 7) {//专题/活动/话题
                return TYPE_ACTIVITY;

            } else if (b.getDoc_type() == 8 || b.getDoc_type() == 9) {//直播/视频
                return TYPE_VIDEO;

            }
            //专题
        } else if (b.getDoc_type() == 5) {
            return TYPE_SUBJECT;
            //多图
        } else if (b.getList_style() == 3) {
            return TYPE_MULTI;
        }
        return TYPE_NOMAL;
    }

    /**
     * 分组holder
     */
    static class GroupViewHolder extends BaseRecyclerViewHolder<SubjectItemBean> {

        @BindView(R2.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R2.id.tv_more)
        TextView tvMore;

        public GroupViewHolder(ViewGroup parent) {
            super(UIUtils.inflate(R.layout.moduel_detail_topic_group_name, parent, false));
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView() {
            //分组标签不可点击
            if (mData.getList_title() != null) {
                tvGroupName.setText(mData.getList_title());
            }
            //显示是否有更多相关专题
            if (mData.getSize() >= 3) {
                tvMore.setVisibility(View.VISIBLE);
                tvMore.setText(itemView.getContext().getString(R.string.module_detail_offical_more));
            } else {
                tvMore.setVisibility(View.GONE);
            }
        }

        private Bundle bundle;
        @OnClick({R2.id.tv_more})
        public void onClick(View v) {
            if (v.getId() == R.id.tv_more) {
                //进入专题列表
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putInt(IKey.GROUP_ID, mData.getId());
                bundle.putString(IKey.TITLE, mData.getList_title());
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.TOPIC_LIST);

            }

        }

    }

}
