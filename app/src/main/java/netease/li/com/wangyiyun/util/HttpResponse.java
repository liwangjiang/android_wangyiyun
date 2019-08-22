package netease.li.com.wangyiyun.util;

import android.text.TextUtils;

public abstract class HttpResponse<T> {
    //http返回的类型泛型
    Class<T> t;
    public HttpResponse(Class<T> t){
        this.t=t;
    }
    //请求失败
    public abstract void onError(String msg);
    //请求成功
    public abstract void onSuccess(T t);

    public void parse(String json){
        //判断Json是否为空
        if (TextUtils.isEmpty(json)){
            //请求失败
            onError("网络连接失败");
            return;
        }
        //如果我只需要获取json对象，那么老老实实返回json
        if (t == String.class){
            onSuccess((T)json);
            return;
        }

        if(TextUtils.isEmpty(json)){
            return;
        }
        //尝试转换json-->需要的类型
        T result=JsonUtil.parseJson(json,t);


        if (result!=null){
            //请求成功
            onSuccess(result);
        }else{
            //json解析失败
            onError("json解析失败");
        }

    }

}
