package netease.li.com.wangyiyun.service;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

public class NetEaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        File sdFile= Environment.getExternalStorageDirectory();//sd卡的路径
        File file=new File(sdFile,"xImageLoader");//图片加载路径
        if(!file.exists()){
            file.mkdirs();//创建文件夹
        }
        //ImageLoaderConfiguration 就是ImageLoader的配置类   createDefault创建一个默认的显示配置
        //ImageLoaderConfiguration imageLoader=ImageLoaderConfiguration.createDefault(this);
        ImageLoaderConfiguration configuration=new
                ImageLoaderConfiguration
                .Builder(this)
                .discCache(new UnlimitedDiskCache(sdFile))//表示保存到本地磁盘
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//缓存到本地创建文件时的命名规范
                .build();
        //初始化
        ImageLoader.getInstance().init(configuration);
        //初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
