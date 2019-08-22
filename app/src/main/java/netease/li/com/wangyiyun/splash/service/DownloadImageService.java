package netease.li.com.wangyiyun.splash.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import netease.li.com.wangyiyun.splash.bean.Ads;
import netease.li.com.wangyiyun.splash.bean.AdsDetail;
import netease.li.com.wangyiyun.util.Constant;
import netease.li.com.wangyiyun.util.ImageUtil;
import netease.li.com.wangyiyun.util.Md5Helper;

public class DownloadImageService extends IntentService {
    public static final String ADS_DATA="ads";
    //如果要使用IntentService一定要写这个构造器
    public DownloadImageService(){
        super("DownloadImageService");
    }
    //服务下载图片
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.v("liwangjiang","启动服务");
        Ads ads=(Ads) intent.getSerializableExtra(ADS_DATA);
        List<AdsDetail> list = ads.getAds();
        for (int i=0;i<list.size();i++) {
            AdsDetail adsDetail = list.get(i);
            List<String> res_url =adsDetail.getRes_url();
            if(res_url!=null){
               String img_url=res_url.get(0);//等到图片路径

               if (!TextUtils.isEmpty(img_url)){

                   //开始图片下载
                   String catch_name=Md5Helper.toMD5(img_url);
                   //先判断文件是否存在 , 如果存在就不执行
                   //ImageUtil.checkImageIsDownLoad(catch_name)
                   if(ImageUtil.checkImageIsDownLoad(catch_name)||!ImageUtil.fileExists()){
                       Log.v("liwangjiang","图片开始下载");
                       //下载图片
                        downloadImage(img_url,catch_name);

                   }
                   downloadImage(img_url,catch_name);

               }
            }
        }
    }


    //加载图片
    public void downloadImage(String url_name,String MD5_name){
        try {
            URL url = new URL(url_name);
            URLConnection connection=url.openConnection();
            Bitmap bitmap=BitmapFactory.decodeStream(connection.getInputStream());//成功保存一张位图
            //保存吧图片保存到SD卡里面
            saveToSD(bitmap,MD5_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //保存到SD卡里面
    public void saveToSD(Bitmap bitmap,String MD5_name){
        if(bitmap==null){
            return;
        }
        //判断SD卡是否插入
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
           //插入状态
           File SD= Environment.getExternalStorageDirectory();//得到SD卡的路径
           File cacheFile = new File(SD, Constant.CACHE);//Constant.CACHE表示是否隐藏后缀
           if(!cacheFile.exists()){//判断文件是否存在  不存在的时候创建
                cacheFile.mkdirs();//创建文件
             }
           File image = new File(cacheFile,MD5_name+".jpg");//保存图片
           //如果图片存在返回null
           if(image.exists()){
               return;
           }
           //图片不存在
            try {
                FileOutputStream image_out = new FileOutputStream(image);//吧文件给位图去处理
                //format该参数是图片的格式    quality该参数是图片的压缩大小如果是10压缩百分之90   stream压缩流就是把图片保存到哪里
                //compress(Bitmap.CompressFormat format, int quality, OutputStream stream)
                bitmap.compress(Bitmap.CompressFormat.JPEG,60,image_out);//就是把图片压缩到SD卡里面
                image_out.flush();//刷新
                image_out.close();//关闭流
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            //没有插入
        }

    }

}
