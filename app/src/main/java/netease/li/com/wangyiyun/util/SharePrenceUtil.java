package netease.li.com.wangyiyun.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrenceUtil {
    public static final String XML_FILE_NAME="cache";

    //该方法用来保存Json数据格式
    public static void saveString(Context context,String title,String content){
        //第一个参数表示存储的该文件的名字    第二个参数表示访问文件的权限
        SharedPreferences shared=context.getSharedPreferences(XML_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(title,content);
        //表示立马写入XML文件中
        editor.commit();
        //一般介意是使用apply方法   空闲的时候写入
        //editor.apply();
    }
    //该方法表示获取XML里面的内容
    public static String getString(Context context,String title){
        SharedPreferences shared=context.getSharedPreferences(XML_FILE_NAME,Context.MODE_PRIVATE);
        //第一个参数表示要键值对里面的键   第二个参数表示默认值
       return shared.getString(title,"");
    }


    //用于缓存超时时间
    public static void saveInt(Context context,String title,int content){
        SharedPreferences shared=context.getSharedPreferences(XML_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=shared.edit();
        editor.putInt(title,content);
        editor.apply();
    }
    public static int getInt(Context context,String title){
        SharedPreferences shared=context.getSharedPreferences(XML_FILE_NAME,Context.MODE_PRIVATE);
        //第一个参数表示要键值对里面的键   第二个参数表示默认值
        return shared.getInt(title,0);
    }


    //用于当前时间
    public static void saveLong(Context context,String title,long content){
        SharedPreferences shared=context.getSharedPreferences(XML_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=shared.edit();
        editor.putLong(title,content);
        editor.apply();
    }
    public static long getLong(Context context,String title){
        SharedPreferences shared=context.getSharedPreferences(XML_FILE_NAME,Context.MODE_PRIVATE);
        //第一个参数表示要键值对里面的键   第二个参数表示默认值
        return shared.getLong(title,0);
    }
}
