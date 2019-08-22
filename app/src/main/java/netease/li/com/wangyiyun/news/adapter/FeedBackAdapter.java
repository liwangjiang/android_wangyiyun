package netease.li.com.wangyiyun.news.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import netease.li.com.wangyiyun.R;
import netease.li.com.wangyiyun.news.bean.FeedBack;
import netease.li.com.wangyiyun.news.bean.FeedBacks;

public class FeedBackAdapter extends BaseAdapter {
    int type_title=0;
    int type_content=1;

    //数据
    ArrayList<FeedBacks> data;
    LayoutInflater inflater;
    private DisplayImageOptions options;
    public FeedBackAdapter(ArrayList<FeedBacks> data, Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        ////表示保存到本地磁盘
        options =  new DisplayImageOptions
                .Builder()
                .showImageOnLoading(R.drawable.biz_tie_user_avater_default)//表示图片如果没有加载完就显示默认图片
                .showImageForEmptyUri(R.drawable.biz_tie_user_avater_default)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.biz_tie_user_avater_default)//设置图片加载或解码中发生错误显示的图片
                .cacheInMemory(true)//表示保存到本地磁盘里面
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
       //返回类型是否有热门标题
       int type=getItemViewType(position);
       if(type == type_title){
           //返回标题和内容
           TitleViewHolder viewHolder;
           if(view==null){
               view = inflater.inflate(R.layout.item_feed_title,null);
               viewHolder = new TitleViewHolder();
               viewHolder.title=view.findViewById(R.id.title);
               view.setTag(viewHolder);
           }else{
                viewHolder=(TitleViewHolder) view.getTag();
           }
           //viewHolder.title.setText();
       }else{
           //返回内容
           //取出feedbacks
           FeedBacks bakcs=data.get(position);
           ContentViewHodler viewHodler;
           if(view==null){
               view=inflater.inflate(R.layout.item_feedback,null);
               viewHodler = new ContentViewHodler();
               viewHodler.icon=view.findViewById(R.id.profile_image);
               viewHodler.name=view.findViewById(R.id.net_name);
               viewHodler.from=view.findViewById(R.id.net_from);
               viewHodler.vote=view.findViewById(R.id.like);
               viewHodler.content=view.findViewById(R.id.content);
               viewHodler.vip_b=view.findViewById(R.id.vip_b);
               viewHodler.vip_f=view.findViewById(R.id.vip_f);
               view.setTag(viewHodler);
           }else {
               viewHodler=(ContentViewHodler) view.getTag();
           }
           //Log.v("liwangjiang","feedBacks="+bakcs.getHot().size());
           init(viewHodler,bakcs);
       }

        return view;
    }
    private void init(ContentViewHodler viewHodler,FeedBacks feedBacks){

        FeedBack feedBack=feedBacks.lastFeedBakc();
        viewHodler.name.setText(feedBack.getN());
        viewHodler.vote.setText(feedBack.getV());
        viewHodler.from.setText(feedBack.getF());
        viewHodler.content.setText(feedBack.getB());
        ImageLoader.getInstance().displayImage(feedBack.getTimg(),viewHodler.icon,options);
        //判断是否是vip
        if(!TextUtils.isEmpty(feedBack.getVip())){
            viewHodler.vip_f.setVisibility(View.VISIBLE);
            viewHodler.vip_b.setVisibility(View.VISIBLE);
        }else{
            viewHodler.vip_f.setVisibility(View.GONE);
            viewHodler.vip_b.setVisibility(View.GONE);
        }
    }


    //返回页面数据的类型个数
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    //返回的类型
    @Override
    public int getItemViewType(int position) {
        //根据每一条数据的isTitle()进行判断,如过是true
        FeedBacks feedBack=data.get(position);
        if (feedBack.isTitle()){
            return type_title;
        }else{
            return type_content;
        }
    }
    //标题ViewHolder
    class TitleViewHolder{
        TextView title;
    }
    //内容ViewHodler
    class ContentViewHodler{
        CircleImageView icon;//头像
        TextView name;//网易云网名
        TextView from;//来源
        TextView vote;//点赞
        TextView content;//内容
        //vie的判断
        ImageView vip_b;
        ImageView vip_f;
    }
}
