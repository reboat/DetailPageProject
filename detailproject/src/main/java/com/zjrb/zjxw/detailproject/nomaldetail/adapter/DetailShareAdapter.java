package com.zjrb.zjxw.detailproject.nomaldetail.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliya.uimode.utils.UiModeUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.PH;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.bean.DetailShareBean;

import java.util.List;


/**
 * 新闻详情页分享适配器
 * Created by wanglinjie.
 * create time:2017/7/10  下午5:39
 */

public final class DetailShareAdapter extends BaseRecyclerAdapter {


    /**
     * 构造方法
     *
     * @param datas 传入集合数据
     */
    public DetailShareAdapter(List datas) {
        super(datas);
    }


    @Override
    public DetailShareViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        return new DetailShareViewHolder(UIUtils.inflate(R.layout.module_detail_share_item, parent, false));
    }

    public class DetailShareViewHolder extends BaseRecyclerViewHolder<DetailShareBean> {
        private TextView tv_title;
        private ImageView iv_img;

        public DetailShareViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_share_name);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_share_image);
        }

        @Override
        public void bindView() {
            if (mData.getContent() != null) {
                tv_title.setText(mData.getContent());
            }
            if (mData.getPlatform() == SHARE_MEDIA.WEIXIN_CIRCLE) {
                UiModeUtils.applyImageSrc(iv_img, R.attr.module_detail_share_friend);
            } else if (mData.getPlatform() == SHARE_MEDIA.WEIXIN) {
                UiModeUtils.applyImageSrc(iv_img, R.attr.module_detail_share_wx);
            } else if (mData.getPlatform() == SHARE_MEDIA.QQ) {
                UiModeUtils.applyImageSrc(iv_img, R.attr.module_detail_share_qq);
            } else if (mData.getPlatform() == SHARE_MEDIA.QZONE) {
                UiModeUtils.applyImageSrc(iv_img, R.attr.module_detail_share_qzone);
            } else if (mData.getPlatform() == SHARE_MEDIA.SINA) {
                UiModeUtils.applyImageSrc(iv_img, R.attr.module_detail_share_wb);
            }

        }
    }
}
