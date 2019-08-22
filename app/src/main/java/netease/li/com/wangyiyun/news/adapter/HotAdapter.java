package netease.li.com.wangyiyun.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;

import netease.li.com.wangyiyun.R;
import netease.li.com.wangyiyun.news.bean.HotDetail;

public class HotAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<HotDetail> listHot;
    private DisplayImageOptions options;
    public HotAdapter(ArrayList<HotDetail> mList, Context context){
        inflater=LayoutInflater.from(context);
        this.listHot=mList;
        ////表示保存到本地磁盘
        options =  new DisplayImageOptions
                .Builder()
                .showImageOnLoading(R.drawable.biz_pc_main_promo)//表示图片如果没有加载完就显示默认图片
                .showImageForEmptyUri(R.drawable.biz_tie_user_avater_default)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.biz_pc_main_night)//设置图片加载或解码中发生错误显示的图片
                .displayer(new SimpleBitmapDisplayer())//默认
                    // - RoundedBitmapDisplayer（int roundPixels）设置圆角图片 如果是圆角的话就是表示图片创建了两张在三级内存里面
                    //- FakeBitmapDisplayer（）这个类什么都没做
                    //- FadeInBitmapDisplayer（int durationMillis）设置图片渐显的时间
                    //- SimpleBitmapDisplayer()正常显示一张图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }
    @Override
    public int getCount() {
        return listHot.size();
    }

    @Override
    public Object getItem(int i) {
        return listHot.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        viewHoder hoder;
        if(view==null){
            view = inflater.inflate(R.layout.item_hot,viewGroup,false);
            hoder = new viewHoder();
            hoder.icon=view.findViewById(R.id.item_hot_image);
            hoder.title=view.findViewById(R.id.item_hot_tex_view_title_id);
            hoder.source=view.findViewById(R.id.item_hot_text_view_source);
            hoder.replyCount=view.findViewById(R.id.item_hot_text_view_replyCount);
            hoder.special=view.findViewById(R.id.item_hot_text_view_special);
            view.setTag(hoder);
        }else{
            hoder=(viewHoder) view.getTag();
        }
        initView(hoder,listHot.get(position));
        return view;
    }
    private void initView(viewHoder hoder,HotDetail hot){

        //三个参数
        //1.表示显示图片的地址
        //2.表示显示到那个ImageView中
        //3.显示模式
         ImageLoader.getInstance()   //图片显示
                    .displayImage(hot.getImg(),hoder.icon,options)
                    ;
        hoder.title.setText(hot.getTitle()+"");
        hoder.source.setText(hot.getSource()+"");
        if(!TextUtils.isEmpty(hot.getSpecialID())){
            hoder.replyCount.setBackgroundColor(Color.RED);
            hoder.replyCount.setText("专题栏");
        }else{

        hoder.replyCount.setText(hot.getReplyCount()+"跟帖");
        }

    }
    //给外部返回一个对象去显示调用返回当前点击的对象
    public HotDetail getHotDetaByInde(int index){
        HotDetail detail=listHot.get(index);
        return detail;
    }
    public void addData(ArrayList<HotDetail> add){
        if(listHot==null){
            listHot=new ArrayList<>();
        }
        listHot.addAll(add);
        //刷新数据
        notifyDataSetChanged();
    }
    class viewHoder{
        ImageView icon;
        TextView  title;
        TextView  source;
        TextView  replyCount;
        TextView  special;
    }
}
