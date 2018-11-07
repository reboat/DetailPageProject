package com.zjrb.zjxw.detailproject.nomaldetail.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliya.uimode.mode.Attr;
import com.aliya.uimode.utils.UiModeUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
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
        View view = UIUtils.inflate(R.layout.module_detail_share_item, parent, false);
        // 因为5个item和6个item都使用的该adapter,但五个item的LayoutManager是Grid,6个的是LinearLayoutManager,所以这里针对6个的情况设置宽度
        if (view != null && datas != null && datas.size() == 6) {
            view.getLayoutParams().width = (int) (UIUtils.getScreenW() * ((float) 2 / 11));
        }
        return new DetailShareViewHolder(view);
    }

    /**
     * 普通详情页中间分享holder
     */
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
                UiModeUtils.applySave(iv_img, Attr.NAME_SRC,R.mipmap.module_detail_me_friend_btn);
            } else if (mData.getPlatform() == SHARE_MEDIA.WEIXIN) {
                UiModeUtils.applySave(iv_img, Attr.NAME_SRC,R.mipmap.module_detail_me_wechat_btn);
            } else if (mData.getPlatform() == SHARE_MEDIA.QQ) {
                UiModeUtils.applySave(iv_img, Attr.NAME_SRC,R.mipmap.module_detail_me_qq_btn);
            } else if (mData.getPlatform() == SHARE_MEDIA.SINA) {
                UiModeUtils.applySave(iv_img, Attr.NAME_SRC,R.mipmap.module_detail_me_sina_btn);
            } else if (mData.getPlatform() == SHARE_MEDIA.QZONE) {
                UiModeUtils.applySave(iv_img, Attr.NAME_SRC,R.mipmap.module_detail_me_space_btn);
            } else if (mData.getPlatform() == SHARE_MEDIA.DINGTALK) {
                UiModeUtils.applySave(iv_img, Attr.NAME_SRC,R.mipmap.module_detail_me_dingding_btn);
            } else if (mData.getPlatform() == SHARE_MEDIA.MORE) { // 更多
                UiModeUtils.applySave(iv_img, Attr.NAME_SRC,R.mipmap.module_detail_me_more_btn);
            }

        }
    }
}
