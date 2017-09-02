package com.zjrb.zjxw.detailproject.subjectdetail.adapter;

import android.view.ViewGroup;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.zjxw.detailproject.bean.SubjectListBean;
import com.zjrb.zjxw.detailproject.holder.NewsDetailArticleGeneralViewHolder;
import com.zjrb.zjxw.detailproject.subjectdetail.holder.NewsActivityHolder;
import com.zjrb.zjxw.detailproject.subjectdetail.holder.NewsMultiPictureHolder;
import com.zjrb.zjxw.detailproject.subjectdetail.holder.NewsTextHolder;
import com.zjrb.zjxw.detailproject.subjectdetail.holder.NewsTopicHolder;
import com.zjrb.zjxw.detailproject.subjectdetail.holder.NewsVideoHolder;

import java.util.List;


/**
 * 专题列表适配器
 *
 * Created by wanglinjie.
 * create time:2017/8/26  上午9:14
 */
public class TopicListAdapter extends BaseRecyclerAdapter {

    //普通
    private static final int TYPE_NOMAL = 0;
    //专题
    private static final int TYPE_TOPIC = 1;
    //活动
    private static final int TYPE_ACTIVITY = 2;
    //视频
    private static final int TYPE_VIDEO = 3;
    //文本
    private static final int TYPE_TEXT = 4;
    //多图
    private static final int TYPE_MULTI = 5;
    //专题
    public static final int TYPE_SUBJECT = 6;

    public TopicListAdapter(List datas) {
        super(datas);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_TOPIC) {
            return new NewsTopicHolder(viewGroup);
        } else if (viewType == TYPE_ACTIVITY) {
            return new NewsActivityHolder(viewGroup);
        } else if (viewType == TYPE_TEXT) {
            return new NewsTextHolder(viewGroup);
        } else if (viewType == TYPE_VIDEO) {
            return new NewsVideoHolder(viewGroup);
        } else if (viewType == TYPE_MULTI) {
            return new NewsMultiPictureHolder(viewGroup);
        }

        return new NewsDetailArticleGeneralViewHolder(viewGroup);
    }

    @Override
    public int getAbsItemViewType(int position) {
        //组名
        SubjectListBean.ArticleListBean b = (SubjectListBean.ArticleListBean) datas.get(position);
        if (b.getDoc_type() == 2) {
            return TYPE_NOMAL;
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

        return super.getAbsItemViewType(position);
    }

}
