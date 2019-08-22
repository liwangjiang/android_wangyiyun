package netease.li.com.wangyiyun.news.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import netease.li.com.wangyiyun.R;
import netease.li.com.wangyiyun.backs.MyBacks;
import netease.li.com.wangyiyun.news.adapter.FeedBackAdapter;
import netease.li.com.wangyiyun.news.bean.FeedBack;
import netease.li.com.wangyiyun.news.bean.FeedBacks;
import netease.li.com.wangyiyun.util.Constant;
import netease.li.com.wangyiyun.util.HttpResponse;
import netease.li.com.wangyiyun.util.HttpUtil;
import netease.li.com.wangyiyun.util.JsonUtil;

public class FeedbackActivity extends Activity {

    private ListView listView;
    //页面的地址
    private String docid;

    //路径
    private String url;

    //表示全部的feedBack
    ArrayList<FeedBacks> backs;

    //创建Adapter
    private FeedBackAdapter feedBackAdapter;

    //创建一个弱引用
    InnerHandler innerHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        //退出页面
        findViewById(R.id.backsImageViewOnclikDetail).setOnClickListener(new MyBacks(this));
        //初始化ListView
        listView = findViewById(R.id.feedback_listView);
        //初始化弱应用
        innerHandler = new InnerHandler(this);
        docid = getIntent().getStringExtra(DetailActivity.DOCID);
        url = Constant.getFeedBackUrl(docid);
        //Log.v("liwangjiang","url="+ url);
        backs = new ArrayList<FeedBacks>();

        HttpUtil http=HttpUtil.getInstance();
        http.getData(url, new HttpResponse<String>(String.class) {
            @Override
            public void onError(String msg) {

            }

            @Override
            public void onSuccess(String json) {
                if(json!=null){
                    try {
                        //难道整个json对象  就是获取到所有的数据
                        JSONObject jsonObject = new JSONObject(json);
                        //在整个json对象里面拿到一个数组回复对象
                        JSONArray array=jsonObject.optJSONArray("hotPosts");

                        //生成一个标题的数据
                        FeedBacks title = new FeedBacks();
                        title.setTitle(true);
                        title.setTitleS("热门跟帖");
                        backs.add(title);


                        //array数组对象里面每个个数据多代表一个数组   逐条解析数据
                        for(int i=0;i<array.length();i++){
                            //表示每一项hotPosts
                            FeedBacks  feedBacks = new FeedBacks();
                            //解析hotPosts里面的数组
                            JSONObject tmp=array.optJSONObject(i);
                            //返回一个键对象
                            Iterator<String> keys=tmp.keys();
                            while(keys.hasNext()){
                                //拿到对应的数组名称键
                              String  key=keys.next();
                              //解析里面的对象
                              JSONObject everyJson=tmp.optJSONObject(key);
                              //获取每个json数据对应的对象
                              FeedBack feedBack=JsonUtil.parseJson(everyJson.toString(), FeedBack.class);

                              feedBacks.add(feedBack);
                              //强转要遍历
                              feedBack.setIndex(Integer.valueOf(key));
                            }
                            //进行排序
                            feedBacks.sort();
                            //添加到数据集里面
                            backs.add(feedBacks);
                        }

                        //Log.v("liwangjiang","backs="+backs.toString());
                        innerHandler.sendEmptyMessage(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            });
    }

    public void init(){
       // Log.v("liwangjiang","backs="+backs.get(0).getHot().size());
        feedBackAdapter = new FeedBackAdapter(backs,this);
        listView.setAdapter(feedBackAdapter);
    }

    //更新UI若应用
    static class InnerHandler extends Handler{
        WeakReference<FeedbackActivity> hand;
        public InnerHandler(FeedbackActivity activity){
            hand=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FeedbackActivity activity=hand.get();
            if(activity!=null){
                //初始化
                activity.init();
            }
            super.handleMessage(msg);
        }
    }

}
