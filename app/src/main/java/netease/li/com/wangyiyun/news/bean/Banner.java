package netease.li.com.wangyiyun.news.bean;

public class Banner {
    private String img;
    private String title;

    public Banner(String img, String title) {
        this.img = img;
        this.title = title;
    }

    public Banner() {
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

    @Override
    public String toString() {
        return "Banner{" +
                "img='" + img + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
