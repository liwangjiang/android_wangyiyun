package netease.li.com.wangyiyun.splash.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import netease.li.com.wangyiyun.MainActivity;
import netease.li.com.wangyiyun.R;
import netease.li.com.wangyiyun.splash.OnTimeClickListener;
import netease.li.com.wangyiyun.splash.TimeView;
import netease.li.com.wangyiyun.splash.bean.Action;
import netease.li.com.wangyiyun.splash.bean.Ads;
import netease.li.com.wangyiyun.splash.bean.AdsDetail;
import netease.li.com.wangyiyun.splash.service.DownloadImageService;
import netease.li.com.wangyiyun.util.Constant;
import netease.li.com.wangyiyun.util.HttpResponse;
import netease.li.com.wangyiyun.util.HttpUtil;
import netease.li.com.wangyiyun.util.ImageUtil;
import netease.li.com.wangyiyun.util.JsonUtil;
import netease.li.com.wangyiyun.util.Md5Helper;
import netease.li.com.wangyiyun.util.SharePrenceUtil;


public class SplashActivity extends Activity{
    //缓存图片
    static final String JSON_CACHE="ads_json";
    static final String JSON_CACHE_TIME_OUT="ads_json_time_out";
    static final String JSON_CACHE_LAST_SUCCESS="ads_json_last";
    static final String LAST_IMAGE_INDEX="img_index";
    MyHandler mHandler ;//创建Handler对象
    //广告图片
    private ImageView topImageView;
    //设置圆
    TimeView timeView;

    //圆的转角
    int length = 2*1000;
    int space = 500;
    int total;
    int now;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //4.4以后android提供了一个叫沉浸试的概念就是设置全屏
        View mDecorView=getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        |View.SYSTEM_UI_FLAG_FULLSCREEN
                                        |View.SYSTEM_UI_FLAG_IMMERSIVE
                                         );
        setContentView(R.layout.activity_splash);
        timeView=findViewById(R.id.timeView);
        this.topImageView=findViewById(R.id.topImage);

        initHandler();//初始化Handler

        initOnClickListener();//初始化点击事件

        setAbs();//http请求

        showImage();//图片显示


    }
    public void initOnClickListener(){
        timeView.setOnClickListener(new OnTimeClickListener() {
            @Override
            public void onClickTime() {
                //直接跳转到MianActivity
                //我们选择定时器跳过按钮后,就应该吧定时器去除
                mHandler.removeCallbacks(reshRing);
                goToMainActivity();
            }
        });
    }
    private void goToMainActivity(){//页面的跳转
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        //我们选择定时器跳过按钮后,就应该吧定时器去除
        mHandler.removeCallbacks(reshRing);
    }
    private void initHandler(){
        total = length / space;

        mHandler = new MyHandler(this);//色块解决通过若应用

    }
    Runnable reshRing = new Runnable() {
        @Override
        public void run() {
            //会重新新建多次Handler对象
            //Message message = new Message();
            //消息池中复用
            Message message = mHandler.obtainMessage();
            message.arg1=now;
            mHandler.sendMessage(message);
            mHandler.postDelayed(this,space);
            now++;
        }
    };

    Runnable NoPhotoGotoMain = new Runnable() {
        @Override
        public void run() {
            goToMainActivity();
        }
    };
    @Override
    public void onBackPressed() {//该方法是点击返回键的时候调用
        mHandler.removeCallbacks(NoPhotoGotoMain);
        super.onBackPressed();

    }

    //显示图片
    public void showImage(){
        mHandler.post(reshRing);
        //读取缓存
        String cache=SharePrenceUtil.getString(this,JSON_CACHE);//读取json数据
        if(!TextUtils.isEmpty(cache)){//判断Json是否有数据如果有就不需要读

            //对出此次进入系统的图片
            int index=SharePrenceUtil.getInt(this,LAST_IMAGE_INDEX);
            if(TextUtils.isEmpty(cache)){
                return;
            }
            //转换成对象
            Ads ads=(Ads) JsonUtil.parseJson(cache,Ads.class);
            if(ads==null){
                return;
            }

            //获取图片路径链表
            List<AdsDetail> adsDetail=ads.getAds();

            //判断生怕图片出现溢出

            //判断索引是否越界
            if(index>adsDetail.size()-1){
                index=0;
            }
            if(adsDetail!=null && adsDetail.size()>0){
                //获取第一张图片

                AdsDetail detail=adsDetail.get(index);
                List<String> urls=detail.getRes_url();
                if(urls!=null && urls.size()>0){
                    //获取到URL
                    String url=urls.get(0);
                    //计算出文件名
                    String image_name=Md5Helper.toMD5(url);
                    //显示图片
                    File image=ImageUtil.getFileByName(image_name);
                    if(image.exists()){//判断是否存在
                        Bitmap bitmap = ImageUtil.getImageBitMapByFile(image);
                        topImageView.setImageBitmap(bitmap);
                        index++;
                        //存储图片索引的值
                        SharePrenceUtil.saveInt(this,LAST_IMAGE_INDEX,index);
                        //获取到图片的链接路径
                        final Action action=detail.getAction_params();

                        //将图片设为点击事件
                        topImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(action!=null&&!TextUtils.isEmpty(action.getLink_url())){
                                    Intent intent = new Intent();
                                    intent.setClass(SplashActivity.this,WebActivity.class);
                                    intent.putExtra(WebActivity.H5_PATH,action);
                                    startActivity(intent);
                                    finish();
                                    //我们选择定时器跳过按钮后,就应该吧定时器去除
                                    mHandler.removeCallbacks(reshRing);
                                }
                            }
                        });


                    }else{
                        //不存在
                    }
                }
            }
        }else{
            //没有缓存不显示图片  三秒后跳入注页面
            mHandler.postDelayed(NoPhotoGotoMain,space*total);
        }

    }

    public void setAbs(){
        String cache=SharePrenceUtil.getString(this,JSON_CACHE);
        if(TextUtils.isEmpty(cache)){
            //如果等于空就执行http操作
             httpRequest();

        }else{
            //不等于空的操作
            //判断超时
            int time_out = SharePrenceUtil.getInt(this,JSON_CACHE_TIME_OUT);//超时时间
            long last = SharePrenceUtil.getLong(this,JSON_CACHE_LAST_SUCCESS);//获取上次请求的时间
            long now = System.currentTimeMillis();//获取当前时间
            if((now-last)>time_out*60*1000){//判断是否超时
                httpRequest();
            }

        }
    }
    //网络请求
    public void httpRequest(){
        HttpUtil util = HttpUtil.getInstance();
        util.getData(Constant.SPLASH_URL, new HttpResponse<String>(String.class) {
            @Override
            public void onError(String msg) {
            Log.v("liwangjiang","onError msg="+msg);
            }

            @Override
            public void onSuccess(String json) {
                //Log.v("liwangjiang","json="+json);
                Ads ads=JsonUtil.parseJson(json, Ads.class);//Json对象返回
                //http成功后，缓存Json数据
                SharePrenceUtil.saveString(SplashActivity.this,JSON_CACHE,json);
                //http成功后，存储超时时间
                SharePrenceUtil.saveInt(SplashActivity.this,JSON_CACHE_TIME_OUT,ads.getNext_req());
                //http成功后，缓存上次请求的时间
                SharePrenceUtil.saveLong(SplashActivity.this,JSON_CACHE_LAST_SUCCESS,System.currentTimeMillis());



                //Gson请求成功
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, DownloadImageService.class);
                //如果要传输对象的话必须让该对象实现Serializable   序列化
                intent.putExtra(DownloadImageService.ADS_DATA,ads);
                startService(intent);
            }
        });
    }

//    //网络请求服务
//    public void httpRequest(){
//        OkHttpClient client = new OkHttpClient();
//        Request request=new Request.Builder()
//                .url(Constant.SPLASH_URL).build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.v("liwangjiang","访问失败");
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                if(!response.isSuccessful()){
//                    //请求失败
//                }
//                String json=response.body().string();//等到Json数据
//
//                Ads ads=JsonUtil.parseJson(json, Ads.class);//Json对象返回
//                if(ads!=null){
//
//
//                    //http成功后，缓存Json数据
//                    SharePrenceUtil.saveString(SplashActivity.this,JSON_CACHE,json);
//                      //http成功后，存储超时时间
//                    SharePrenceUtil.saveInt(SplashActivity.this,JSON_CACHE_TIME_OUT,ads.getNext_req());
//                      //http成功后，缓存上次请求的时间
//                    SharePrenceUtil.saveLong(SplashActivity.this,JSON_CACHE_LAST_SUCCESS,System.currentTimeMillis());
//
//
//
//                    //Gson请求成功
//                 Intent intent = new Intent();
//                 intent.setClass(SplashActivity.this, DownloadImageService.class);
//                 //如果要传输对象的话必须让该对象实现Serializable   序列化
//                 intent.putExtra(DownloadImageService.ADS_DATA,ads);
//                 startService(intent);
//                }else{
//                    //Gson请求失败
//                }
//
//            }
//        });
//
//    }



    static class MyHandler extends Handler{
        //java里面有三种应用
        //1.强应用
        //2.若应用  JVM是无法保证若应用的存活的
        //3.软应用
        //在解决Handler和Activity绑定的问题

        //2.使用若应用持有对象
        WeakReference<SplashActivity> activity;//创建一个若应用
        public MyHandler(SplashActivity activity){
            this.activity=new WeakReference<SplashActivity>(activity);
        }
        //1.使用静态内部类切断访问activity
        @Override
        public void handleMessage(Message msg) {
            //获取对象，如果对象呗回收就为null
            SplashActivity act = activity.get();
            if(act==null){
                return;
            }
            switch (msg.what){

                case 0:
                    int myNow=msg.arg1;
                    if(myNow<=act.total){
                        act.timeView.setProgess(act.total,myNow);
                    }else{
                        this.removeCallbacks(act.reshRing);
                        act.goToMainActivity();//表示圆弧转完后页面跳转
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
