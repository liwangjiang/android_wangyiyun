package netease.li.com.wangyiyun.news.adapter;

import android.content.Context;
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
import java.util.concurrent.ForkJoinTask;

import netease.li.com.wangyiyun.R;
import netease.li.com.wangyiyun.news.bean.SpecialItemBean;

public class SpecialAdapter extends BaseAdapter {
    ArrayList<SpecialItemBean> datas;
    LayoutInflater inflater;
    int title=0;
    int content=1;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    public SpecialAdapter(Context content, ArrayList<SpecialItemBean> datas,ImageLoader imageLoader){
        this.datas=datas;
        inflater = LayoutInflater.from(content);
        this.imageLoader=imageLoader;
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
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(getItemViewType(position)==title){  //是标题
            titleHandler titleObj;
            if (view==null){
                titleObj = new titleHandler();
                view = inflater.inflate(R.layout.item_special_title,null);
                titleObj.textView=view.findViewById(R.id.item_text_view_title_id);

                view.setTag(titleObj);
            }else{
                titleObj= (titleHandler) view.getTag();
            }
            titleObj.textView.setText(datas.get(position).getIndex()+"   "+datas.get(position).getTitle_name());
        }else{  //不是标题  n内容
            contentHandler content;
            if(view == null){
                content = new contentHandler();
                view=inflater.inflate(R.layout.item_special,viewGroup,false);
                content.imageView=view.findViewById(R.id.item_special_image);
                content.title=view.findViewById(R.id.item_special_tex_view_title_id);
                content.source=view.findViewById(R.id.item_special_text_view_source);
                content.replyCount=view.findViewById(R.id.item_special_text_view_replyCount);
                view.setTag(content);
            }else{
                content = (contentHandler) view.getTag();
            }
            initView(content,datas.get(position));
        }
        return view;
    }
    public void initView(contentHandler content,SpecialItemBean bean){
        imageLoader.getInstance().displayImage(bean.getImgsrc(),content.imageView,options);
        content.title.setText(bean.getLtitle());
        content.source.setText(bean.getSource());
        content.replyCount.setText(bean.getReplyCount()+"跟帖");
    }
    //标题Handler
    class titleHandler{
        TextView textView;
    }
    //内容Handler
    class contentHandler{
        ImageView imageView;
        TextView title;
        TextView source;
        TextView replyCount;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        SpecialItemBean bean=datas.get(position);
        if(bean.isTitle()){
            return title;
        }else{
            return content;
        }

    }
}
