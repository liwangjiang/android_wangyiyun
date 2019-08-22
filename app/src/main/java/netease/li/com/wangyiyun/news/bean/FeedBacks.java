package netease.li.com.wangyiyun.news.bean;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.ConsoleHandler;

public class FeedBacks {
    ArrayList<FeedBack> hot;
    boolean isTitle =false;
    String titleS;

    public String getTitleS() {
        return titleS;
    }

    public void setTitleS(String titleS) {
        this.titleS = titleS;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }
    public FeedBack lastFeedBakc(){

        return hot.get(hot.size()-1);
    }


    public FeedBacks() {
        hot=new ArrayList<FeedBack>();
    }
    public void add(FeedBack feedBack){
        hot.add(feedBack);
    }
    //排序调用
    public void sort(){
        //排序
        Collections.sort(hot,new FeedBakcsSort());
    }
    //比较器
    class FeedBakcsSort implements Comparator{

        @Override
        public int compare(Object o, Object t1) {

            if(((FeedBack)o).getIndex()>((FeedBack)t1).getIndex()){
                //大于
                return 1;
            }else if(((FeedBack)o).getIndex()==((FeedBack)t1).getIndex()){
                //相等
                return 0;
            }else{
                //不小于
                return -1;
            }
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }


    }

    public ArrayList<FeedBack> getHot() {
        return hot;
    }

    public void setHot(ArrayList<FeedBack> hot) {
        this.hot = hot;
    }

    @Override
    public String toString() {
        return "FeedBacks{" +
                "hot=" + hot +
                '}';
    }
}
