package netease.li.com.wangyiyun.util;

import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import netease.li.com.wangyiyun.splash.bean.Ads;

public class HttpUtil {
    static  HttpUtil util;
    static OkHttpClient client;
    private HttpUtil(){
        client = new OkHttpClient();
    }
    //使用单利设定模式 OkHttpClient对象创建很费性能 使用单利就只需要创建一次
    public static HttpUtil getInstance(){
        if (util==null){
            synchronized(HttpUtil.class){
                // synchronized(HttpUtil.class)一段时间内只能有一个线程被执行，另一个线程必须等待当前线程执行完这个代码块后才能执行该代码块。
                if (util==null){
                    util=new HttpUtil();
                }
            }
        }
        return util;
    }
    public void getData(String url,final HttpResponse respon){
        Request request=new Request.Builder()
                .url(url).build();
        client.newCall(request).enqueue(new Callback() {
            //连接服务器失败
            @Override
            public void onFailure(Request request, IOException e) {
                respon.onError("连接服务器失败");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    respon.onError("连接服务器失败");
                    return ;
                }
                String json = response.body().string();//得到Json数据
                if(TextUtils.isEmpty(json)){
                    return;
                }
                respon.parse(json);


            }
        });
    }
}
