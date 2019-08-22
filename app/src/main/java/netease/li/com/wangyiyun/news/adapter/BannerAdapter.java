package netease.li.com.wangyiyun.news.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import netease.li.com.wangyiyun.R;
import netease.li.com.wangyiyun.news.bean.HotDetail;

//广告轮番图Adapter
public class BannerAdapter extends PagerAdapter {
    ArrayList<View> view;
    ArrayList<HotDetail> mList;
    private final DisplayImageOptions options;
    int size;//表示


    public BannerAdapter(ArrayList<View> view, ArrayList<HotDetail> mList){
         this.view=view;
         this.mList=mList;
         this.size=view.size();
         options = new DisplayImageOptions
                .Builder()
                .showImageOnLoading(R.drawable.biz_pc_main_promo)//表示图片如果没有加载完就显示默认图片
                .showImageForEmptyUri(R.drawable.biz_tie_user_avater_default)//设置图片Uri为空或是错误的时候显示的图片.cacheInMemory(true)
                .showImageOnFail(R.drawable.biz_pc_main_night)//设置图片加载或解码中发生错误显示的图片.cacheOnDisk(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build()
                ;
    }
   //要显示的页数
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
     //viewPage的过程 表示当前页面和滑进是否一致
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
     //返回当前显示的页面
     // 做了两件事，第一：将当前视图添加到container中，当重新返回这个view是就不要重复上网查找
     // 第二：返回当前View
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int viewPosition = position%size;
        View itemView=view.get(viewPosition);
        container.addView(itemView);
        ImageView img=itemView.findViewById(R.id.bannerImageViewId);

        ImageLoader.getInstance().displayImage(mList.get(viewPosition).getImg(), img,options);
       return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
