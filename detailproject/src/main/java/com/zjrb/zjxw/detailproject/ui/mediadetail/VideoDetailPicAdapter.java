package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.glide.AppGlideOptions;
import cn.daily.news.biz.core.glide.PH;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.utils.RouteManager;
import cn.daily.news.biz.core.web.ImageBrowseActivity;
import port.SerializableHashMap;

/**
 * 视频直播间九宫格图片
 * Created by wanglinjie.
 * create time:2017/8/21  上午10:14
 */
public class VideoDetailPicAdapter extends BaseRecyclerAdapter implements OnItemClickListener {

    private Bundle bundle;
    //图片集合
    private SerializableHashMap map;

    public VideoDetailPicAdapter(List data) {
        super(data);
        //填充图片集合
        map = new SerializableHashMap();
        for (int i = 0; i < data.size(); i++) {
            map.getMap().put(i, false);
        }
        setOnItemClickListener(this);
    }


    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int
            viewType) {
        return new ImagePreviewHolder(parent);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putStringArray(ImageBrowseActivity.EXTRA_IMAGE_URLS, (String[]) datas.toArray());
        bundle.putInt(ImageBrowseActivity.EXTRA_IMAGE_INDEX, position);
        if (map != null && map.getMap() != null && map.getMap().size() > position && !map.getMap().get(position)) {
            map.getMap().put(position, true);
        }
        bundle.putSerializable(ImageBrowseActivity.EXTRA_IMAGE_SRCS, map);
        Nav.with(itemView.getContext()).setExtras(bundle).toPath(RouteManager.IMAGE_BROWSE_ACTIVITY);
    }

    /**
     * 直播间图片
     */
    static class ImagePreviewHolder extends BaseRecyclerViewHolder<String> {

        @BindView(R2.id.iv_image)
        ImageView mIvImage;

        public ImagePreviewHolder(ViewGroup parent) {
            super(UIUtils.inflate(R.layout.module_detail_video_img_preview, parent, false));
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView() {
            //无图片时用占位图
            GlideApp.with(mIvImage).load(mData).centerCrop().placeholder(PH.zheSmall()).apply(AppGlideOptions.smallOptions()).into(mIvImage);
        }

    }


}
