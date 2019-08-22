package netease.li.com.wangyiyun.news.bean;

public class HotDetail {
    String img;//图片
    String title;//标题
    String source;//来源
    int replyCount;//跟帖
    String id;//详情页面Id
    String specialID;  //专题栏详情页面
    public String getId() {
        return id;
    }

    public String getSpecialID() {
        return specialID;
    }

    public void setSpecialID(String specialID) {
        this.specialID = specialID;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    @Override
    public String toString() {
        return "HotDetail{" +
                "img='" + img + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", replyCount=" + replyCount +
                ", id='" + id + '\'' +
                ", specialID='" + specialID + '\'' +
                '}';
    }
}
