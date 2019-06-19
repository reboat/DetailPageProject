package com.zhejiangdaily;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.ui.divider.ListSpaceDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.db.SettingManager;
import cn.daily.news.biz.core.nav.Nav;

public class MainActivity extends DailyActivity implements OnItemClickListener {


    private RecyclerView mRecyclerView;
    private TypeAdapter mTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SettingManager.getInstance().setAutoPlayVideoWithWifi(true);

        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity("普通稿件", "https://zjbeta.8531.cn/news.html?id=1158613"));
        entities.add(new Entity("链接稿件","https://zjbeta.8531.cn/link.html?id=1156948"));
        entities.add(new Entity("活动稿件", "https://zjbeta.8531.cn/activity.html?id=1158579"));
        entities.add(new Entity("视频稿件", "https://zjbeta.8531.cn/video.html?id=1156732"));
        entities.add(new Entity("直播稿件", "https://zjbeta.8531.cn/live.html?id=1156923&native=1"));
        entities.add(new Entity("图集稿件", "https://zjbeta.8531.cn/album.html?id=1156729"));
        entities.add(new Entity("专题稿件","https://zj.zjol.com.cn/subject.html?id=1146710"));
        entities.add(new Entity("起航号稿件", "https://zj.zjol.com.cn/red_boat.html?id=100035879"));
        entities.add(new Entity("起航号图集稿件", "https://zjbeta.8531.cn/red_boat_album.html?id=100041077"));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ListSpaceDivider(1, R.color._f5f5f5, true));
        mTypeAdapter = new TypeAdapter(entities);
        mRecyclerView.setAdapter(mTypeAdapter);
        mTypeAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Entity entity = mTypeAdapter.getData(position);
        Nav.with(this).to(entity.url);
    }

    static class TypeAdapter extends BaseRecyclerAdapter<Entity> {
        public TypeAdapter(List<Entity> data) {
            super(data);
        }

        @Override
        public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
            return new TypeViewHolder(parent, R.layout.item_type);
        }
    }

    static class TypeViewHolder extends BaseRecyclerViewHolder<Entity> {
        TextView mTypeNameView;

        public TypeViewHolder(@NonNull ViewGroup parent, int layoutRes) {
            super(parent, layoutRes);
            mTypeNameView=itemView.findViewById(R.id.type_name);
        }

        @Override
        public void bindView() {
            mTypeNameView.setText(getData().type);
        }
    }

    static class Entity {
        public String type;
        public String url;

        public Entity(String type, String url) {
            this.type = type;
            this.url = url;
        }
    }

}
