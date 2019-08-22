package netease.li.com.wangyiyun.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import netease.li.com.wangyiyun.news.bean.FragmentInfo;

public class NewsAdapter extends FragmentStatePagerAdapter{
    //FragmentStatePagerAdapter
    // FragmentPagerAdapter
    //两者的区别在于一个状态FragmentStatePagerAdapter表示只创建一个PageView
    // FragmentPagerAdapter一次性全部创建
    ArrayList<FragmentInfo> mFragments;
    public NewsAdapter(FragmentManager fm, ArrayList<FragmentInfo> fragments) {
        super(fm);
        this.mFragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position).getmFragment();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    //返回页面标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getmTitle();
    }
    //更新数据
    public void setData(ArrayList<FragmentInfo> fragments){
        this.mFragments=fragments;
        notifyDataSetChanged();

    }
    //如果ViewPage不重写getItemPosition话重写刷新不了的
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
