package com.zjrb.zjxw.detailproject.topic.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DetailShareBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻详情页中间内容
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsActivityTopHolder extends BaseRecyclerViewHolder<DraftDetailBean> {

    @BindView(R2.id.iv_cover)
    ImageView mIvCover;
    @BindView(R2.id.fl_cover_alpha_place_holder)
    public View mFlCoverAlphaPlaceHolder;
    @BindView(R2.id.tv_cover_title)
    public TextView mTvCoverTitle;
    @BindView(R2.id.tv_host)
    TextView mTvHost;
    @BindView(R2.id.tv_guest)
    TextView mTvGuest;
    @BindView(R2.id.ll_cover_title)
    public LinearLayout mLlFixedTitle;
    @BindView(R2.id.ll_cover)
    FrameLayout mLlCover;
    /**
     * 分享数据列表
     */
    private List<DetailShareBean> mListData;

    public NewsActivityTopHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_activity_top, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);

        //题图
        ViewGroup.LayoutParams params = mLlCover.getLayoutParams();
        params.width = UIUtils.getScreenW();
        params.height = UIUtils.getScreenH();
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            params2.height -= getStatusBarHeight(itemView.getContext());
//        }
        mLlCover.setLayoutParams(params);



//        GlideApp.with(mIvCover).load(mData.getArticle().getArticle_pic()).placeholder(PH.zheSmall()).centerCrop().into(mIvCover);

        //标题
        if (mData.getArticle().getList_title() != null) {
            mTvCoverTitle.setText(mData.getArticle().getList_title());
        }

        //主持人
        if (mData.getArticle().getTopic_hosts() != null) {
            mTvHost.setText("主持人：");
            for (String host :
                    mData.getArticle().getTopic_hosts()) {
                mTvHost.append(host);
            }
        }

        //嘉宾
        if (mData.getArticle().getTopic_guests() != null) {
            mTvGuest.setText("嘉宾：");
            for (String guest :
                    mData.getArticle().getTopic_guests()) {
                mTvGuest.append(guest);
            }
        }

    }

}