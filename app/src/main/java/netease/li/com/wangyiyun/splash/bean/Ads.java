package netease.li.com.wangyiyun.splash.bean;

import java.io.Serializable;
import java.util.List;

public class Ads implements Serializable{
    private int next_req;//超时时间
    private List<AdsDetail> ads;

    public int getNext_req() {
        return next_req;
    }

    public List<AdsDetail> getAds() {
        return ads;
    }

    public void setNext_req(int next_req) {
        this.next_req = next_req;
    }

    public void setAds(List<AdsDetail> ads) {
        this.ads = ads;
    }

    @Override
    public String toString() {
        return "Ads{" +
                "next_req=" + next_req +
                ", ads=" + ads +
                '}';
    }
}
