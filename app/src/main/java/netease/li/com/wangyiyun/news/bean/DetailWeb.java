package netease.li.com.wangyiyun.news.bean;

import java.util.ArrayList;

public class DetailWeb {
    String body;
    ArrayList<DetailWebImage> img;
    String ptime;
    String title;
    String source;
    int replyCount;

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    @Override
    public String toString() {
        return "DetailWeb{" +
                "body='" + body + '\'' +
                ", img=" + img +
                ", ptime='" + ptime + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ArrayList<DetailWebImage> getImg() {
        return img;
    }

    public void setImg(ArrayList<DetailWebImage> img) {
        this.img = img;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
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
}
