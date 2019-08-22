package netease.li.com.wangyiyun.news.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;

import netease.li.com.wangyiyun.R;
import netease.li.com.wangyiyun.news.bean.DetailWebImage;
import uk.co.senab.photoview.PhotoView;

public class DetailImageAdapter extends PagerAdapter {
    ArrayList<DetailWebImage> images;
    ArrayList<View> views;
    Context context;
    private DisplayImageOptions options;
    public DetailImageAdapter(ArrayList<DetailWebImage> images, ArrayList<View> views, Context context){
        this.images=images;
        this.views=views;
        this.context=context;

        ////表示保存到本地磁盘
        options =  new DisplayImageOptions
                .Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }
    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=views.get(position);
        PhotoView photoView=view.findViewById(R.id.item_detail_photoViewId);
        ImageLoader.getInstance().displayImage(images.get(position).getSrc(),photoView,options);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
