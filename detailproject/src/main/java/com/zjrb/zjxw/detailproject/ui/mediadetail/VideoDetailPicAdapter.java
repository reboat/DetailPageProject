package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.zjxw.detailproject.apibean.bean.NativeLiveBean;

import java.util.ArrayList;
import java.util.List;

import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.utils.RouteManager;
import cn.daily.news.biz.core.web.ImageBrowseActivity;
import port.SerializableHashMap;

/**
 * 视频直播间九宫格图片
 * Created by wanglinjie.
 * create time:2017/8/21  上午10:14
 */
public class VideoDetailPicAdapter extends BaseRecyclerAdapter<NativeLiveBean.ListBean> implements OnItemClickListener {

    private Bundle bundle;
    //图片集合
    private SerializableHashMap map;

    public VideoDetailPicAdapter(NativeLiveBean.ListBean data) {
//        super(data);
        super(null);
        //填充图片集合
        map = new SerializableHashMap();
        for (int i = 0; i < data.getPics().size(); i++) {
            map.getMap().put(i, false);
        }
        setOnItemClickListener(this);
        setData(data);
    }

    public void setData(NativeLiveBean.ListBean data) {
        List list = new ArrayList<>();
        for (int i = 0; i < data.getPics().size(); i++) {
            if(!TextUtils.isEmpty(data.getPics().get(i))){
                list.add(data.getPics().get(i));
            }
        }
        setData(data != null ? list : null);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int
            viewType) {
        return new VideoDetailPicAdapterHolder(parent);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        //这里需要转换
        if (datas != null && datas.size() > 0) {
            bundle.putStringArray(ImageBrowseActivity.EXTRA_IMAGE_URLS, (String[]) datas.toArray(new String[datas.size()]));
        }
        bundle.putInt(ImageBrowseActivity.EXTRA_IMAGE_INDEX, position);
        if (map != null && map.getMap() != null && map.getMap().size() > position && !map.getMap().get(position)) {
            map.getMap().put(position, true);
        }
        bundle.putSerializable(ImageBrowseActivity.EXTRA_IMAGE_SRCS, map);
        Nav.with(itemView.getContext()).setExtras(bundle).toPath(RouteManager.IMAGE_BROWSE_ACTIVITY);
    }


}
