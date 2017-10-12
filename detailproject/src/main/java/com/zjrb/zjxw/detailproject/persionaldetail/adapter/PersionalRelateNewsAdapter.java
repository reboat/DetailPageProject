package com.zjrb.zjxw.detailproject.persionaldetail.adapter;

import android.view.ViewGroup;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.zjxw.detailproject.bean.ArticleItemBean;
import com.zjrb.zjxw.detailproject.holder.NewsDetailArticleGeneralViewHolder;
import com.zjrb.zjxw.detailproject.subject.holder.NewsActivityHolder;
import com.zjrb.zjxw.detailproject.subject.holder.NewsMultiPictureHolder;
import com.zjrb.zjxw.detailproject.subject.holder.NewsSubjectHolder;
import com.zjrb.zjxw.detailproject.subject.holder.NewsTextHolder;
import com.zjrb.zjxw.detailproject.subject.holder.NewsVideoHolder;

import java.util.List;

/**
 * 个人官员相关新闻列表适配器
 * 支持类型:图文，文字，多图，直播
 * 稿件类型普通新闻、链接新闻、直播
 * 注意:以上是理论支持了类型，实际上根据媒立方，会回传所有类型的稿件
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
//TODO WLJ 支持所有的类型
public class PersionalRelateNewsAdapter extends BaseRecyclerAdapter {

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

    public PersionalRelateNewsAdapter(List data) {
        super(data);
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
        if (viewType == TYPE_ACTIVITY) {
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
        //纯文字
        ArticleItemBean b = (ArticleItemBean) datas.get(position);
        if (b.getList_style() == 1) {
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
        //正常稿件
        return TYPE_NOMAL;
    }

}