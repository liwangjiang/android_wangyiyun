package netease.li.com.wangyiyun.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import me.hz89.swipeback.SwipeBackLayout;
import me.hz89.swipeback.app.SwipeBackActivity;
import netease.li.com.wangyiyun.R;
import netease.li.com.wangyiyun.backs.MyBacks;
import netease.li.com.wangyiyun.news.adapter.SpecialAdapter;
import netease.li.com.wangyiyun.news.adapter.SpeicialTitleAdapter;
import netease.li.com.wangyiyun.news.bean.SpecialItemBean;
import netease.li.com.wangyiyun.util.Constant;
import netease.li.com.wangyiyun.util.HttpResponse;
import netease.li.com.wangyiyun.util.HttpUtil;
import netease.li.com.wangyiyun.util.JsonUtil;
import netease.li.com.wangyiyun.util.NoScrollGridView;

public class SpecialActivity extends SwipeBackActivity {
    public final  static String SPECIAL_ID="special_id";
    private SwipeBackLayout mSwipeBackLayout;
    private ListView specialListView;
    private String special_id;
    //图片路径
    private String banner;
    private ImageView banner_image;
    private NoScrollGridView gridView;
    private  DisplayImageOptions options;
    //数据集合
    ArrayList<SpecialItemBean> items;
    ArrayList<String> titles;
    SpecialAdapter adapter;
    //gridView的跳转
    ArrayList<Integer> indexs;
    ImageLoader imageLoader;
    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        //初始化ListView
        specialListView = (ListView) findViewById(R.id.specialListView);
        findViewById(R.id.backsImageView).setOnClickListener(new MyBacks(this));

        //ImageOptions();
        titles=new ArrayList<>();
        indexs = new ArrayList<>();
        //滑动退出页面
        mSwipeBackLayout = getSwipeBackLayout();

        initHttp();//初始化



        //设置专题栏页面点击
        specialListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position==0){
                    return;
                }
                int head =  specialListView.getHeaderViewsCount();
                SpecialItemBean itemBean= (SpecialItemBean) adapter.getItem(position-head);

                if(itemBean==null){
                    return;
                }
                if (TextUtils.isEmpty(itemBean.getDocid())){
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(SpecialActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.DOCID,itemBean.getDocid());
                startActivity(intent);
            }
        });

        view = View.inflate(this, R.layout.include_special_head,null);
        banner_image= view.findViewById(R.id.include_special_head_image);
        gridView= view.findViewById(R.id.include_scroll_grid_view);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int head =  specialListView.getHeaderViewsCount();
                int index = indexs.get(position);
                setSelection(index,head);
            }
        });
        //ListView的优化如果写就会报空指针异常
        //PauseOnScrollListener(imageLoader,false,true)
        //ImageLoader对象
        //pauseOnScroll  ->拖动暂停加载图片
        //pauseOnFling   ->飞一下暂停图片加载
        //specialListView.setOnScrollListener(new PauseOnScrollListener(imageLoader,false,true));

        myHanderCreate hander = new myHanderCreate(this);
        hander.sendEmptyMessage(0);


        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }
    public void setSelection(final  int index,final int head){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                specialListView.setSelection(index+head);
            }
        });
    }

    private void initTopAdd(){

        View view = View.inflate(this,R.layout.include_special_head,null);
        banner_image=view.findViewById(R.id.include_special_head_image);
        gridView=view.findViewById(R.id.include_scroll_grid_view);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int head =  specialListView.getHeaderViewsCount();
                int index = indexs.get(position);
                setSelection(index,head);
            }
        });
        //初始化选择按钮
        SpeicialTitleAdapter speicialTitleAdapter = new SpeicialTitleAdapter(titles,this);
        gridView.setAdapter(speicialTitleAdapter);
        //设置点击事件


        //显示图片
        imageLoader.getInstance().displayImage(banner,banner_image);


        specialListView.addHeaderView(view);

    }
    private void ImageOptions(){
        ////表示保存到本地磁盘
        options =  new DisplayImageOptions
                .Builder()
                .showImageOnLoading(R.drawable.biz_pc_main_promo)//表示图片如果没有加载完就显示默认图片
                .showImageForEmptyUri(R.drawable.biz_tie_user_avater_default)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.biz_pc_main_night)//设置图片加载或解码中发生错误显示的图片
                .displayer(new SimpleBitmapDisplayer())//默认
                // - RoundedBitmapDisplayer（int roundPixels）设置圆角图片 如果是圆角的话就是表示图片创建了两张在三级内存里面
                //- FakeBitmapDisplayer（）这个类什么都没做
                //- FadeInBitmapDisplayer（int durationMillis）设置图片渐显的时间
                //- SimpleBitmapDisplayer()正常显示一张图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }
    public void initHttp(){
        Intent  intent=getIntent();
        //初始化集合数据
        items = new ArrayList<>();
        special_id = intent.getStringExtra(SPECIAL_ID);


        String url=Constant.getSpecialUrl(special_id);
        //网络请求解析Gson数据
        HttpUtil.getInstance().getData(url, new HttpResponse<String>(String.class) {
            @Override
            public void onError(String msg) {
                //Toast.makeText(SpecialActivity.this, ""+msg+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String content) {
                //判断是否有数据
                if(TextUtils.isEmpty(content)){
                    return;
                }
                try {
                    JSONObject js = new JSONObject(content);
                    //拿到固定的对象
                    JSONObject jsonObject=js.optJSONObject(special_id);
                    banner = jsonObject.optString("banner");



                    String  sname = jsonObject.optString("sname");
                    JSONArray topics = jsonObject.getJSONArray("topics");
                    for (int i=0;i<topics.length();i++){
                        JSONObject tmp=topics.getJSONObject(i);
                        int index=tmp.getInt("index");//拿到索引
                        String tname=tmp.getString("tname");
                        titles.add(tname);
                        JSONArray docs=tmp.getJSONArray("docs");

                        //表示创建一个标题栏
                        SpecialItemBean itmeTitle=new SpecialItemBean();
                        itmeTitle.setTitle(true);
                        itmeTitle.setTitle_name(tname);
                        //全部选项
                        itmeTitle.setIndex(index+"/"+docs.length());
                        items.add(itmeTitle);
                        indexs.add(items.size()-1);
                        for (int j=0;j<docs.length();j++){
                            JSONObject doc_itme=docs.getJSONObject(j);
                            //每个栏目对应的数据
                            SpecialItemBean special=JsonUtil.parseJson(doc_itme.toString(), SpecialItemBean.class);
                            special.setTitle(false);
                            items.add(special);
                        }
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
    //创建弱引用
    class myHanderCreate extends Handler{
        WeakReference<SpecialActivity> weak;
        public myHanderCreate(SpecialActivity activity){
            weak = new WeakReference<SpecialActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            SpecialActivity activity= weak.get();
            if(activity==null){
                return;
            }
            //initTopAdd();

            //初始化选择按钮
            SpeicialTitleAdapter speicialTitleAdapter = new SpeicialTitleAdapter(titles,activity);
            gridView.setAdapter(speicialTitleAdapter);
            //显示图片
            imageLoader.getInstance().displayImage(banner,banner_image);

            specialListView.addHeaderView(view);



            //初始化遍历器
            adapter = new SpecialAdapter(activity,items,imageLoader);
            specialListView.setAdapter(adapter);






        }
    }
}
