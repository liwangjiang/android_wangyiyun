package netease.li.com.wangyiyun.news.bean;

import java.io.Serializable;

public class DetailWebImage implements Serializable{
    private String ref;
    private String src;
    private String alt;
    private String pixel;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getPixel() {
        return pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }

    @Override
    public String toString() {
        return "DetailWebImage{" +
                "ref='" + ref + '\'' +
                ", src='" + src + '\'' +
                ", alt='" + alt + '\'' +
                ", pixel='" + pixel + '\'' +
                '}';
    }
}
