package netease.li.com.wangyiyun.news.bean;

import android.support.v4.app.Fragment;

public class FragmentInfo {
    Fragment mFragment;
    String mTitle;

    public Fragment getmFragment() {
        return mFragment;
    }

    public void setmFragment(Fragment mFragment) {
        this.mFragment = mFragment;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public FragmentInfo(Fragment mFragment, String mTitle) {
        this.mFragment = mFragment;
        this.mTitle = mTitle;
    }

    @Override
    public String toString() {
        return "FragmentInfo{" +
                "mFragment=" + mFragment +
                ", mTitle='" + mTitle + '\'' +
                '}';
    }
}
