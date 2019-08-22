package netease.li.com.wangyiyun.splash.bean;

import java.io.Serializable;

public class Action implements Serializable{
    private String link_url;

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    @Override
    public String toString() {
        return "Action{" +
                "link_url='" + link_url + '\'' +
                '}';
    }
}
