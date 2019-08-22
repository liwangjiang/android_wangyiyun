package netease.li.com.wangyiyun.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

public class JsonUtil {
    private static Gson mGson;
    //<T>代表声明一个泛型
    //T返回的类型是我们使用的类型
    //Class<T>  传入一个什么对象就返回一个什么对象
    public static <T> T parseJson(String json,Class<T> tClass){
        if(mGson==null){
            mGson = new Gson();
        }

        if(TextUtils.isEmpty(json)){//判断字符串是否为空
            return null;
        }
        return mGson.fromJson(json,tClass);
    }
}
