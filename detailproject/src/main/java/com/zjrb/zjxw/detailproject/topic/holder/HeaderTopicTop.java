package com.zjrb.zjxw.detailproject.topic.holder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.zjrb.core.common.base.page.PageItem;
import com.zjrb.core.common.glide.AppGlideOptions;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 话题 - top
 *
 * @author a_liYa
 * @date 2017/10/31 19:14.
 */
public class HeaderTopicTop extends PageItem {

    @BindView(R2.id.iv_cover)
    ImageView mIvCover;

    private TopBarHolder mTopBarHolder;
    private OverlyHolder mInnerOverlyHolder;
    private OverlyHolder mOverlyHolder;

    private DraftDetailBean.ArticleBean mArticle;

    public HeaderTopicTop(RecyclerView parent) {
        super(parent, R.layout.module_detail_activity_top);
        ButterKnife.bind(this, itemView);
        mInnerOverlyHolder = new OverlyHolder(findViewById(R.id.layout_fixed));
        parent.addOnScrollListener(new RecyclerView.OnScrollListener() {

            float fraction = -1;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int maxRange = itemView.getHeight()
                        - mInnerOverlyHolder.getHeight() - mTopBarHolder.getHeight();
                float scale;
                if (maxRange > 0) {
                    scale = (-1f * itemView.getTop()) / maxRange;
                } else {
                    scale = 1;
                    Log.e("TAG", "macRange 小于0 ");
                }
                if (scale < 0) {
                    scale = 0;
                } else if (scale > 1) {
                    scale = 1;
                }
                if (fraction != scale) {
                    fraction = scale;
                    mTopBarHolder.setFraction(fraction);
                    mInnerOverlyHolder.setFraction(fraction);
                    mOverlyHolder.setVisible(scale == 1);
//                    Log.e("TAG", "mScale " + fraction);
                }
            }
        });
    }

    public void setData(DraftDetailBean data) {

        mTopBarHolder.setData(data);
        mOverlyHolder.setData(data);
        mInnerOverlyHolder.setData(data);

        if (data != null && data.getArticle() != null) {
            mArticle = data.getArticle();
            // TODO: 2017/10/31 mock data
            mArticle.setArticle_pic("https://timgsa.baidu" +
                    ".com/timg?image&quality=80&size=b9999_10000&sec=1509470921516&di" +
                    "=ca91966181b58da9112f03db0f56152e&imgtype=0&src=http%3A%2F%2Fww2.sinaimg" +
                    ".cn%2Fbmiddle%2F005HScoIjw1erfxjbtzuoj30p018gaks.jpg");
            if (TextUtils.isEmpty(mArticle.getArticle_pic())) {

            } else {
                GlideApp.with(mIvCover)
                        .load(mArticle.getArticle_pic())
                        .apply(AppGlideOptions.bigOptions())
                        .into(mIvCover);
            }

        }
    }

    public void setTopBar(TopBarHolder topBarHolder) {
        mTopBarHolder = topBarHolder;
    }

    public void setOverlayHolder(OverlyHolder overlyHolder) {
        mOverlyHolder = overlyHolder;
    }

}
