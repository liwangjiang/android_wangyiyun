package netease.li.com.wangyiyun.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import netease.li.com.wangyiyun.R;

public class ShowAdaptr extends BaseAdapter {
    private ArrayList<String> title;
    private Context mContext;
    private boolean isShow=false;
    public ShowAdaptr(ArrayList<String> title, Context context){
        this.title = title;
        this.mContext = context;
    }
    public ShowAdaptr(String title[], Context context){
        this.title = new ArrayList<>();
        this.title.addAll(Arrays.asList(title));
        this.mContext = context;
    }
    public ShowAdaptr(Context context){
        if(title==null){
            this.title = new ArrayList<>();
        }
        this.mContext=context;
    }
    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public Object getItem(int i) {
        return title.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public void setShow(){
        this.isShow=!this.isShow;
        notifyDataSetChanged();
    }
    public boolean getIsShow(){
        return this.isShow;
    }
    //删除选项
    public String deleteItem(int index){
        String itme=this.title.get(index);
        this.title.remove(index);
        notifyDataSetChanged();
        return itme;
    }
    //添加一个选项
    public void addItem(String title){
        if(this.title==null){
            this.title = new ArrayList<>();
        }else{
        this.title.add(title);
        }
        notifyDataSetChanged();
    }
    //返回当前的全部字符串返回去用来缓存
    public String getContent(){
        StringBuilder content = new StringBuilder();
        for (int i=0;i<this.title.size();i++){
            content.append(title.get(i));
            if(i!=this.title.size()-1){
               content.append("-");
            }
        }
        return content.toString();
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        viewHandler handler;
        if(view==null){
            handler = new viewHandler();
            view = View.inflate(mContext,R.layout.item_show,null);
            handler.title=view.findViewById(R.id.title_muen_id);
            handler.imageView=view.findViewById(R.id.showImage);
            view.setTag(handler);
        }else{
            handler = (viewHandler) view.getTag();
        }
        handler.title.setText(title.get(position));
        if(isShow){
            handler.imageView.setVisibility(View.VISIBLE);
        }else{
            handler.imageView.setVisibility(View.GONE);
        }
        return view;
    }
    class viewHandler{
        TextView title;
        ImageView imageView;
    }

}
