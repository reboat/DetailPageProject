package com.zjrb.zjxw.detailproject.redBoat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.holder.NewsDetailWebViewHolder;
import com.zjrb.zjxw.detailproject.holder.RedBoatDetailTitleHolder;
import com.zjrb.zjxw.detailproject.topic.holder.NewsPlaceHolder;

import java.util.List;

/**
 * 类描述：红船号新闻详情页Adapter
 * Created by wanglinjie.
 * create time:2017/7/18  上午09:14
 */

public class RedBoatAdapter extends BaseRecyclerAdapter {
    //顶部标题，视频等
    public static final int VIEW_TYPE_TOP = 1;
    //webview
    public static final int VIEW_TYPE_WEB_VIEW = 2;

    private NewsDetailWebViewHolder webviewHolder;
    private boolean mHasVideoUrl;
    public static final int NO_POSITION = -1;
    private int mWebViewHolderPosition = NO_POSITION;
    private boolean isShowAll;
    private DraftDetailBean detailBean;
    //恢复
    public static final String PAYLOADS_RESUME = "on_resume";
    //暂停
    public static final String PAYLOADS_PAUSE = "on_pause";

    public RedBoatAdapter(List data, boolean hasVideoUrl) {
        super(data);
        mHasVideoUrl = hasVideoUrl;
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TOP) {
            return new RedBoatDetailTitleHolder(parent);
        } else if (viewType == VIEW_TYPE_WEB_VIEW) {
            webviewHolder = new NewsDetailWebViewHolder(parent, mHasVideoUrl);
            return webviewHolder;
        }
        return new NewsPlaceHolder(parent);
    }

    @Override
    public int getAbsItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TOP;
        } else if (position == 1) {
            mWebViewHolderPosition = position;
            return VIEW_TYPE_WEB_VIEW;
        }
        return 0;
    }

    public NewsDetailWebViewHolder getWebViewHolder() {
        return webviewHolder;
    }

    /**
     * 显示全部
     */
    public void showAll() {
        //只加载一次
        if (isShowAll) return;
        isShowAll = true;
        int oldSize = datas.size();
        //添加标题头
        detailBean = (DraftDetailBean) datas.get(0);
        //中间
        datas.add(detailBean);

        datas.add("占位");
        notifyItemRangeChanged(oldSize, datas.size() - oldSize);
    }

    /**
     * webview恢复监听
     */
    public void onWebViewResume() {
        if (mWebViewHolderPosition != NO_POSITION) {
            notifyItemChanged(mWebViewHolderPosition, PAYLOADS_RESUME);
        }
    }

    /**
     * webview暂停监听
     */
    public void onWebViewPause() {
        if (mWebViewHolderPosition != NO_POSITION) {
            notifyItemChanged(mWebViewHolderPosition, PAYLOADS_PAUSE);
        }
    }

    /**
     * 视频生命周期监听
     */
    public interface ILifecycle {

        void onResume();

        void onPause();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        boolean isNeedSuperBind = true;
        if (payloads != null && payloads.size() > 0) {
            isNeedSuperBind = false;
            for (int i = 0; i < payloads.size(); i++) {
                Object payload = payloads.get(i);
                if (PAYLOADS_RESUME.equals(payload)) {
                    if (holder instanceof ILifecycle) {
                        ((ILifecycle) holder).onResume();
                    }
                } else if (PAYLOADS_PAUSE.equals(payload)) {
                    if (holder instanceof ILifecycle) {
                        ((ILifecycle) holder).onPause();
                    }
                } else {
                    isNeedSuperBind = true;
                }
            }
        }
        if (isNeedSuperBind) {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    /**
     * 公共操作回调
     *
     * @author a_liYa
     * @date 2017/5/15 下午8:53.
     */
    public interface CommonOptCallBack {
        /**
         * WebView加载完毕操作
         */
        void onOptPageFinished();

        /**
         * 稿件阅读百分比变化
         *
         * @param scale
         */
        void onReadingScaleChange(float scale);
    }
}
