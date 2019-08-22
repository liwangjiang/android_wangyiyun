package netease.li.com.wangyiyun.news.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import netease.li.com.wangyiyun.R;

public class SpeicialTitleAdapter extends BaseAdapter {
    private ArrayList<String> title;
    private Context mContext;
    public SpeicialTitleAdapter(ArrayList<String> title, Context context){
        this.title = title;
        this.mContext = context;
    }
    public SpeicialTitleAdapter(String title[], Context context){
        this.title = new ArrayList<>();
        this.title.addAll(Arrays.asList(title));
        this.mContext = context;
    }
    public SpeicialTitleAdapter(Context context){
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

        return view;
    }
    class viewHandler{
        TextView title;
        ImageView imageView;
    }

}
