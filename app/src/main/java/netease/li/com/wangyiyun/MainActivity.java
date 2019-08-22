package netease.li.com.wangyiyun;

import android.content.Context;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import netease.li.com.wangyiyun.news.bean.ShowTabEvent;
import netease.li.com.wangyiyun.news.fragment.EmptyFragment;
import netease.li.com.wangyiyun.news.fragment.NewFragment;
import netease.li.com.wangyiyun.util.FragmentTabHost;

public class MainActivity extends AppCompatActivity  {

    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragmentTabHost();
        EventBus.getDefault().register(this);
        //判断版本执行
        int version = getSDKVersions();//获得SDK版本
        if(version>=19){
        //设置抗锯齿
        ImageView imageView=findViewById(R.id.status);
        int height=getStatusHeight(this);
        imageView.getLayoutParams().height=height;//设置高度
        imageView.setBackgroundColor(0XFFCC0000);
        }

    }
    //使用EventBus隐藏掉FragmentTabHost控件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showOrHideTab(ShowTabEvent isShow){
        if(isShow.getIsShow()){
            tabHost.setVisibility(View.GONE);
        }else{
            tabHost.setVisibility(View.VISIBLE);
        }
    }
    private void initFragmentTabHost(){
        //找到FragmentTabHost
        tabHost = findViewById(R.id.fragmentTabHostId);

        //对FragmentTabHost进行初始化
        tabHost.setup(this,getSupportFragmentManager(),R.id.fragmentContentId);
        String[] titles=getResources().getStringArray(R.array.tab_title);
        int[] imageViewResource = new  int[]{R.drawable.news_selector,R.drawable.reading_selector,R.drawable.video_selector,R.drawable.topic_selector,R.drawable.mine_selector};
        Class[] clz = new Class[]{NewFragment.class, EmptyFragment.class,EmptyFragment.class ,EmptyFragment.class, EmptyFragment.class};
        for (int i=0;i<titles.length;i++){
            TabHost.TabSpec tmp= tabHost.newTabSpec(""+i);
            tmp.setIndicator(getEmptyView(this,titles,imageViewResource,i));
            tabHost.addTab(tmp, clz[i],null);
        }
        //为每个Fragment添加选项
       // TabHost.TabSpec one=tabHost.newTabSpec("0");
        //one.setIndicator("one");//显示的内容
        //one.setIndicator(getEmptyView(this));

    }
    private View getEmptyView(Context context,String[] title,int[] imageViews,int index){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_title,null);
        //设置文字
        TextView textView=view.findViewById(R.id.tv_textViewId);
        textView.setText(title[index]);
        //设置图片
        ImageView imageView=view.findViewById(R.id.iv_ImageView);
        imageView.setImageResource(imageViews[index]);
        return view;
    }
    long lastBackTime;
    //返回键  点击两次返回
    @Override
    public void onBackPressed() {
        long now=System.currentTimeMillis();
        if(now-lastBackTime<1000){
           finish();
        }else {
            Toast.makeText(this,"请再次点击退出",Toast.LENGTH_SHORT).show();
        }
        lastBackTime=now;
    }

    /**
     * 获得状态栏的高度  就是获取抗锯齿的高度
     *
     * @param context
     * @return
     */
    public  int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
    //该方法是获取SDK版本的方法
    public int getSDKVersions(){
        return Build.VERSION.SDK_INT;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除EventBus解绑
        EventBus.getDefault().unregister(this);
    }
}
