package com.zjrb.zjxw.detailproject.nomaldetail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerAdapter;
import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;
import com.zjrb.zjxw.detailproject.holder.ArticleGeneralViewHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailEndHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailMiddleHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailTitleHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailTitleVideoHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailWebViewHolder;

import java.util.List;

/**
 * 新闻详情页Adapter
 * Created by wanglinjie.
 * create time:2017/7/10  下午5:39
 */
public class NewsDetailAdapter extends BaseRecyclerAdapter<Object,
        BaseRecyclerViewHolder<Object>> {
    private static final int MAX_RELATED_SIZE = 3;

    public static final int VIEW_TYPE_TOP = 1;
    public static final int VIEW_TYPE_WEB_VIEW = 2;
    public static final int VIEW_TYPE_MIDDLE = 3;
    public static final int VIEW_TYPE_END = 4;

    public static final String PAYLOADS_SUBSCRIBE = "update_subscribe";
    public static final String PAYLOADS_FABULOUS = "update_fabulous";
    public static final String PAYLOADS_RESUME = "on_resume";
    public static final String PAYLOADS_PAUSE = "on_pause";

    public static final int NO_POSITION = -1;
    private int mMiddleHolderPosition = NO_POSITION;
    private int mWebViewHolderPosition = NO_POSITION;

    private boolean isVideoType; // true表示视频类型

    public NewsDetailAdapter(List<Object> datas, boolean isVideoType) {
        super(datas);
        this.isVideoType = isVideoType;
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TOP) {
            return isVideoType ?
                    new NewsDetailTitleVideoHolder(parent) : new NewsDetailTitleHolder(parent);
        } else if (viewType == VIEW_TYPE_WEB_VIEW) {
            return new NewsDetailWebViewHolder(parent);
        } else if (viewType == VIEW_TYPE_MIDDLE) {
            return new NewsDetailMiddleHolder(parent);
        } else if (viewType == VIEW_TYPE_END) {
            return new NewsDetailEndHolder(parent);
        }
        return new ArticleGeneralViewHolder(parent);
    }

    @Override
    protected int getInnerItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TOP;
        } else if (position == 1) {
            mWebViewHolderPosition = position;
            return VIEW_TYPE_WEB_VIEW;
        } else if (mDatas.get(position) instanceof DraftDetailBean) {
            mMiddleHolderPosition = position;
            return VIEW_TYPE_MIDDLE;
        } else if (mDatas.get(position) == null) {
            return VIEW_TYPE_END;
        }
        return super.getInnerItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object>
            payloads) {
        boolean isNeedSuperBind = true;
        if (payloads != null && !payloads.isEmpty()) {
            isNeedSuperBind = false;
            for (int i = 0; i < payloads.size(); i++) {
                Object payload = payloads.get(i);
                if (PAYLOADS_SUBSCRIBE.equals(payload)) {
                    if (holder instanceof IBindSubscribe) {
                        ((IBindSubscribe) holder).bindSubscribe();
                    }
                } else if (PAYLOADS_FABULOUS.equals(payload)) {
                    if (holder instanceof IBindFabulous) {
                        ((IBindFabulous) holder).bindFabulous();
                    }
                } else if (PAYLOADS_RESUME.equals(payload)) {
                    if (holder instanceof ILifecycle) {
                        ((ILifecycle)holder).onResume();
                    }
                }else if (PAYLOADS_PAUSE.equals(payload)) {
                    if (holder instanceof ILifecycle) {
                        ((ILifecycle)holder).onPause();
                    }
                } else {
                    isNeedSuperBind = true;
                }
            }
        }
        if (isNeedSuperBind)
            super.onBindViewHolder(holder, position, payloads);
    }

    /**
     * 显示全部
     */
    public void showAll() {
        int oldSize = mDatas.size();
        DraftDetailBean detailBean = (DraftDetailBean) mDatas.get(0);
        mDatas.add(detailBean);
        List<ArticleItemBean> articles = detailBean.getRecommendedReading();
        if (articles != null) {
            for (int i = 0; i < MAX_RELATED_SIZE && i < articles.size(); i++) {
                mDatas.add(articles.get(i));
            }
        }
        mDatas.add(null);
        notifyItemRangeChanged(oldSize, mDatas.size() - oldSize);
    }

    @Override
    public int getDataSize() {
        return super.getDataSize();
    }

    public void updateSubscribeInfo() {
        notifyItemChanged(0, PAYLOADS_SUBSCRIBE);
        if (mMiddleHolderPosition != NO_POSITION) {
            notifyItemChanged(mMiddleHolderPosition, PAYLOADS_SUBSCRIBE);
        }

    }

    public void updateFabulousInfo() {
        if (mMiddleHolderPosition != NO_POSITION) {
            notifyItemChanged(mMiddleHolderPosition, PAYLOADS_FABULOUS);
        }

    }

    public void onWebViewResume() {
        if (mWebViewHolderPosition != NO_POSITION) {
            notifyItemChanged(mWebViewHolderPosition, PAYLOADS_RESUME);
        }
    }

    public void onWebViewPause() {
        if (mWebViewHolderPosition != NO_POSITION) {
            notifyItemChanged(mWebViewHolderPosition, PAYLOADS_PAUSE);
        }
    }

    public interface IBindSubscribe {

        void bindSubscribe();
    }

    public interface IBindFabulous {

        void bindFabulous();

    }

    public interface ILifecycle {

        void onResume();

        void onPause();
    }

    /**
     * 公共操作回调
     *
     * @author a_liYa
     * @date 2017/5/15 下午8:53.
     */
    public interface CommonOptCallBack {

        /**
         * 订阅操作
         */
        void onOptCancelSubscribe();

        /**
         * 订阅操作
         */
        void onOptSubscribe();

        /**
         * WebView加载完毕操作
         */
        void onOptPageFinished();

        /**
         * 点击栏目操作
         */
        void onOptClickColumn();

        /**
         * 点赞操作
         */
        void onOptFabulous();

    }

}