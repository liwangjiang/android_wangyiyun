package netease.li.com.wangyiyun.news.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import netease.li.com.wangyiyun.R;
import netease.li.com.wangyiyun.news.adapter.DetailImageAdapter;
import netease.li.com.wangyiyun.news.bean.DetailWebImage;

public class DetailImageActiviry extends Activity{

    private ViewPager viewPager;
    public static String intentName="image";
    private ArrayList<View> views;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiry_detail_image);
        viewPager = findViewById(R.id.detail_image_load_activity);
        views=new ArrayList<>();
        //传过来的值
        ArrayList<DetailWebImage> images = (ArrayList<DetailWebImage>)getIntent().getSerializableExtra(intentName);
        if(images!=null){
            for (DetailWebImage item:images){
                View view=View.inflate(this,R.layout.item_detail_image,null);
                views.add(view);
            }
        }
        DetailImageAdapter  adapter = new DetailImageAdapter(images,views,this);
        viewPager.setAdapter(adapter);
        }
}
