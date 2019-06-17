package com.zjrb.zjxw.detailproject.apibean.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 直播列表
 * Created by wanglinjie.
 * create time:2019/3/26  上午11:11
 */
public class NativeLiveBean implements Serializable {
    private static final long serialVersionUID = -4153065686298325643L;

    /**
     * list : [{"id":0,"content":"乌镇西栅景区准备了七组水灯庆祝元宵佳节。","pics":["https://mzvideo.8531.cn/1550568486938微信截图_20190219172646.png","https://mzvideo.8531.cn/1550568565325微信截图_20190219172854.png","https://mzvideo.8531.cn/1550568914681微信截图_20190219173449.png"],"video_url":"","created_at":1550568492000,"created_at_general":"15分钟前","stick_top":false},{"id":1,"content":"荷花拱门。","pics":["https://mzvideo.8531.cn/1550566277566微信截图_20190219165049.png"],"video_url":"","created_at":1550566284000,"created_at_general":"15分钟前","stick_top":false}]
     * has_more : true
     */

    private boolean has_more;
    /**
     * id : 0
     * content : 乌镇西栅景区准备了七组水灯庆祝元宵佳节。
     * pics : ["https://mzvideo.8531.cn/1550568486938微信截图_20190219172646.png","https://mzvideo.8531.cn/1550568565325微信截图_20190219172854.png","https://mzvideo.8531.cn/1550568914681微信截图_20190219173449.png"]
     * video_url :
     * created_at : 1550568492000
     * created_at_general : 15分钟前
     * stick_top : false
     */

    private List<ListBean> list;

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private long id;
        private String content;
        private String video_url;
        private long created_at;
        private String created_at_general;
        private boolean stick_top;
        private List<String> pics;
        private String video_cover;
        private int video_duration;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public long getCreated_at() {
            return created_at;
        }

        public void setCreated_at(long created_at) {
            this.created_at = created_at;
        }

        public String getCreated_at_general() {
            return created_at_general;
        }

        public void setCreated_at_general(String created_at_general) {
            this.created_at_general = created_at_general;
        }

        public boolean isStick_top() {
            return stick_top;
        }

        public void setStick_top(boolean stick_top) {
            this.stick_top = stick_top;
        }

        public List<String> getPics() {
            return pics;
        }

        public void setPics(List<String> pics) {
            this.pics = pics;
        }

        public String getVideo_cover() {
            return video_cover;
        }

        public void setVideo_cover(String video_cover) {
            this.video_cover = video_cover;
        }

        public int getVideo_duration() {
            return video_duration;
        }

        public void setVideo_duration(int video_duration) {
            this.video_duration = video_duration;
        }
    }
}
