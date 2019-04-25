package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zjrb.core.recycleView.PageItem;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 视频详情页直播间专用头部
 * Created by wanglinjie.
 * create time:2017/8/27 下午2:31.
 */
public class VideoDetailHeaderHolder extends PageItem {

    @BindView(R2.id.tv_read_num)
    TextView tvReadNum;
    @BindView(R2.id.tv_read_sort)
    TextView tvReadSort;

    private boolean isReverse;

    //头部动效
    public VideoDetailHeaderHolder(RecyclerView parent, boolean isReverse) {
        super(parent, R.layout.module_detail_video_live_head);
        ButterKnife.bind(this, itemView);
        this.isReverse = isReverse;
    }

    public void setData(DraftDetailBean data) {
        if (data == null || data.getArticle() == null) {
            return;
        }
        if (isReverse) {
            tvReadSort.setText("正序浏览");
        } else {
            tvReadSort.setText("倒序浏览");
        }
        tvReadNum.setText(data.getArticle().getRead_count_general());
    }

    @OnClick({R2.id.tv_read_sort})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        if (v.getId() == R.id.tv_read_sort) {
            if (!isReverse) {
                isReverse = true;
                tvReadSort.setText("正序浏览");
            } else {
                isReverse = false;
                tvReadSort.setText("倒序浏览");
            }
            //刷新数据
            Intent intent = new Intent("refresh_head");
            intent.putExtra("isReverse", isReverse);
            intent.putExtra("isClick",true);
            LocalBroadcastManager.getInstance(getItemView().getContext()).sendBroadcast(intent);
        }
    }
}
