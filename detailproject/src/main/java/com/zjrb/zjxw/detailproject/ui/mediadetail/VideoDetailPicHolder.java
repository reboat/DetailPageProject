package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.StringUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.NativeLiveBean;
import com.zjrb.zjxw.detailproject.widget.VideoGridSpaceDivider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 直播间多图holder
 * Created by wanglinjie.
 * create time:2019/3/26  下午3:54
 */
public class VideoDetailPicHolder extends BaseRecyclerViewHolder<NativeLiveBean.ListBean> {

    @BindView(R2.id.tv_time)
    TextView tvTime;
    @BindView(R2.id.iv_top)
    ImageView ivTop;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.rv_imgs)
    RecyclerView rvImgs;

    private VideoDetailPicAdapter mAdapter;

    public VideoDetailPicHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_video_live_holder, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        //时间
        tvTime.setText(StringUtils.long2String(mData.getCreated_at(), "yyyy.MM.dd"));
        //置顶
        if (mData.isStick_top()) {
            ivTop.setVisibility(View.VISIBLE);
        } else {
            ivTop.setVisibility(View.GONE);
        }
        //标题
        if (!TextUtils.isEmpty(mData.getContent())) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mData.getContent());
        } else {
            tvTitle.setVisibility(View.GONE);
        }

        if (mData.getPics() != null && mData.getPics().size() > 0) {
            if(mAdapter == null){
                rvImgs.setLayoutManager(new GridLayoutManager(itemView.getContext(), 3));
                rvImgs.addItemDecoration(new VideoGridSpaceDivider(6));
                mAdapter = new VideoDetailPicAdapter(mData.getPics());
                rvImgs.setAdapter(mAdapter);
            }
        } else {
            rvImgs.setVisibility(View.GONE);
        }

    }

}
