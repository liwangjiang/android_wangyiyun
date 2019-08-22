package netease.li.com.wangyiyun.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import netease.li.com.wangyiyun.R;
import netease.li.com.wangyiyun.news.activity.DetailActivity;
import netease.li.com.wangyiyun.news.activity.SpecialActivity;
import netease.li.com.wangyiyun.news.adapter.BannerAdapter;
import netease.li.com.wangyiyun.news.adapter.HotAdapter;
import netease.li.com.wangyiyun.news.bean.Hot;
import netease.li.com.wangyiyun.news.bean.HotDetail;
import netease.li.com.wangyiyun.util.Constant;
import netease.li.com.wangyiyun.util.HttpResponse;
import netease.li.com.wangyiyun.util.HttpUtil;

public class HotFragment extends Fragment implements ViewPager.OnPageChangeListener{

    private ListView mListView;
    private MyHandler handler;
    //初始化成功
    public final static int INIT_SUCCESS=0;
    //初始化失败
    public final static int INIT_ERROR=1;
    //第一次取数据
    public final static int ONE_DATA=2;
    //上拉刷新控件
    public final static int PULL_REFRESH=3;
    //轮番图
    private ViewPager viewPager;
    //初始化PageView里面的图片显示个数
    private ArrayList<View> views;
    //图片资源
    private Hot o;
    private ArrayList<HotDetail> mHotDetails;

    private LayoutInflater inflater;

    //初始化BannerAdapter
    private BannerAdapter bannerAdapter;

    //初始化标题
    private TextView bannerTitle;
    //初始化白点
    private LinearLayout bannerDost;

    private ArrayList<ImageView> dot_img;
    private int mPosition;
    //使用Hander实现轮番图滚动
    private Handler mHandler;
    private View view;


    //使用刷洗效果
    int startIndex = 0;
    int endIndex = 20;
    int pageSize=20;
    //表示刷新是否到最后一条
    boolean isToEnd;
    int count=0;
    private HotAdapter adapter;

    //设置开关变量
    boolean isHttpRequestSuccess=false;

    //下拉刷新设置控件
    private PtrClassicFrameLayout ptr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.hot_fragment,container,false);

        mListView = view.findViewById(R.id.newListViewId);
        RelativeLayout layout = view.findViewById(R.id.include_loading);
        //判断ListView是否显示如果不显示就显示Layout
        mListView.setEmptyView(layout);


        //下拉刷新设置
        ptr = view.findViewById(R.id.ptr_Classic);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {//表示刷新成功


                initHTTP(true);//更新数据
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, mListView, header);
            }
        });
        //mListView.setOnScrollListener(new PauseOnScrollListener(imageLoader,false,true));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化轮播图参数
        initBannerParas();

        //HTTP初始化
        initHTTP(true);


    }

    //初始化轮播图参数
    private void initBannerParas() {

        handler=new MyHandler(this);
        //初始化Hnadler循环领轮播图
        mHandler = new Handler();


        //ListView添加滚动接听
        mListView.setOnScrollListener(new MyListView());
        //轮番图的使用
        inflater = LayoutInflater.from(getActivity());
        view = inflater.inflate(R.layout.include_banner,null);
        //将轮番图控件加入ListView中
        mListView.addHeaderView(view);

        //初始化ViewPage
        viewPager= view.findViewById(R.id.viewPageId);

        //设置监听事件
        viewPager.addOnPageChangeListener(this);
        //初始化Vies
        views = new ArrayList<>();

        //初始化标题
        bannerTitle = view.findViewById(R.id.bannerImageTextTitle_id);
        bannerDost = view.findViewById(R.id.dost);

        //设置mListView  的监听在点击的时候传输一个Id
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                HotDetail detail = adapter.getHotDetaByInde(position-mListView.getHeaderViewsCount());
                Intent intent = new Intent();
                if(TextUtils.isEmpty(detail.getSpecialID())){ //判断是否是专题栏
                    intent.setClass(getActivity(), DetailActivity.class);
                    intent.putExtra(DetailActivity.DOCID,detail.getId());
               }else{
                    intent.setClass(getActivity(), SpecialActivity.class);
                    intent.putExtra(SpecialActivity.SPECIAL_ID,detail.getSpecialID());
               }



                startActivity(intent);
                //页面动画从进来到出去
                getActivity().overridePendingTransition(R.anim.activity_in,R.anim.activity_out);
            }
        });


        //初始化白点
        dot_img = new ArrayList<>();
    }
    public void calIndex(){
        if(count==0){
            startIndex=0;
            endIndex=startIndex+pageSize;
        }else{
            startIndex=endIndex;
            endIndex=startIndex+pageSize;
        }
    }
    //HTTP初始化
    private void initHTTP(final boolean isInit) {


        //如果正在访问就不执行该方法
        if(isHttpRequestSuccess){
            return;
        }
        isHttpRequestSuccess=true;
        HttpUtil http = HttpUtil.getInstance();
        //第一次进来调用一次
        if(isInit){
        count=0;//如果是第一次吧count设置为零
        calIndex();
        }
      //Log.v("liwangjiang","Constant.getHotUrl(startIndex,endIndex)="+Constant.getHotUrl(startIndex,endIndex));
        http.getData(Constant.getHotUrl(startIndex,endIndex), new HttpResponse<Hot>(Hot.class) {
            @Override
            public void onError(String msg) {
                isHttpRequestSuccess=false;
                Message message = new Message();
                message.what=1;
                handler.sendMessage(message);
                handler.sendEmptyMessage(PULL_REFRESH);

            }

            @Override
            public void onSuccess(Hot o) {
                //更新数据
                handler.sendEmptyMessage(PULL_REFRESH);
              //在这里是有一个异步线程不能在这里更新UI

            //mListView.setAdapter(new HotAdapter(o.getT1348647909107(),getActivity()));
                //第一次去数据
                isHttpRequestSuccess=false;

                if (mListView!=null&&o!=null){
                    count++;
                    //取的第一页的数据-->取出轮播图数据-->第二次显示更新的数据
                    if(isInit){
                        if(views!=null){
                            views.clear();
                            dot_img.clear();//清除白点
                            Message message = new Message();
                            message.what=ONE_DATA;
                            message.obj=o;
                            handler.sendMessage(message);
                        }

                    }else{
                    Message message = new Message();
                    message.what=0;
                    message.obj=o;
                    handler.sendMessage(message);
                    }
                }
            }
        });

    }
    public void update(){
        ptr.refreshComplete();
    }
    //处理listView的数据
    private void initHotAdapter(){

        adapter = new HotAdapter(o.getT1348647909107(),getActivity());
        mListView.setAdapter(adapter);
    }
    private void update(ArrayList<HotDetail> newDate){
        if(adapter==null){
            mHotDetails = new ArrayList<>();
            mHotDetails.addAll(newDate);
            adapter=new HotAdapter(mHotDetails,getActivity());
            mListView.setAdapter(adapter);
        }else{
            adapter.addData(newDate);
        }
    }
    //初始化轮番图
    private void initBanner(){

        //判断轮番图是否为空
        if(o.getT1348647909107()!=null&&o.getT1348647909107().size()>0){
                for (int i=0 ;i < o.getT1348647909107().size();i++){
                    View view = inflater.inflate(R.layout.item_banner,null);
                    views.add(view);
                    ImageView dot=new ImageView(getActivity());
                    dot.setImageResource(R.drawable.gray_dot);
                    //LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    dot.setPadding(0,0,15,10);
                    bannerDost.addView(dot);
                    //吧白点添加到列表中
                    dot_img.add(dot);
                }
        }
        //启动设置轮番图
        bannerAdapter = new BannerAdapter(views,o.getT1348647909107());
        viewPager.setAdapter(bannerAdapter);
        int half = (Integer.MAX_VALUE/2)-(Integer.MAX_VALUE/2)%o.getT1348647909107().size();
        viewPager.setCurrentItem(half);//显示在第几页
        //设置白点和标题
        setImage(0);
        mHandler.post(runnBannerImage);

    }

    //设置白色的点
    private void setImage(int index){
        ImageView img;
        int size=dot_img.size();
        int realPosition=index%size;
        for(int i=0;i<size;i++){
            img = dot_img.get(i);
            if(realPosition==i){
                bannerTitle.setText(o.getT1348647909107().get(realPosition).getTitle()+"");
                img.setImageResource(R.drawable.white_dot);
            }else{
                img.setImageResource(R.drawable.gray_dot);
            }
        }
    }
    Runnable runnBannerImage = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(runnBannerImage,2000);
            int p=mPosition+1;
            viewPager.setCurrentItem(p);
            setImage(p);

        }
    };
    //轮播图接听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    //当前选中的页面
    @Override
    public void onPageSelected(int position) {
        this.mPosition=position;
        setImage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MyHandler extends Handler{
        WeakReference<HotFragment> fragment;//设置弱应用
        public MyHandler(HotFragment fragment){
            this.fragment=new WeakReference<>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HotFragment hot=fragment.get();
            if (hot==null){
                return;
            }
            switch (msg.what){
                case INIT_SUCCESS:
                    //初始化条目加载新的条目
                    o=(Hot) msg.obj;
                    update(o.getT1348647909107());
                    break;

                case INIT_ERROR:
                    Toast.makeText(getContext(),"对不起当前没有网络",Toast.LENGTH_SHORT).show();
                    break;
                case ONE_DATA:
                    //初始化轮播图
                    if(views!=null){
                        views.clear();
                        dot_img.clear();
                    }
                    o=(Hot) msg.obj;
                    initHotAdapter();
                    initBanner();
                    break;
                case PULL_REFRESH://更新刷新
                    update();
                    break;
                    default:

                        break;
            }
        }
    }
    class MyListView implements AbsListView.OnScrollListener{
        //接听滑动的状态回调
        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            if(scrollState==SCROLL_STATE_IDLE&&isToEnd){
                //获取更多数据
                calIndex();
                initHTTP(false);
            }

            switch (scrollState){
                case SCROLL_STATE_IDLE://休闲状态  就是处于禁止状态

                    break;
                case SCROLL_STATE_TOUCH_SCROLL: //当前正在滑动
                    break;
                case SCROLL_STATE_FLING://惯性滑动  就是滑动时手松开了还在动
                    break;
                default:
                        break;

            }
        }
        //absListView所有类的父类  该方法就是当滚动时调用
        //三个int类型的数据分别代表
        //1.firstVisibleItem   ：表示  最顶端  显示的是第几项   就是当前头部显示的第几条
        //2.visibleItemCount   : 表示  统计  当前页面显示的 条目
        //3.totalItemCount     : 表示  统计ListView中所有的条目
        //absListView.getLastVisiblePosition()表示当前页面显示的最后一项
        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(absListView.getLastVisiblePosition()==totalItemCount-1){
                isToEnd=true;
            }else{
                isToEnd=false;
            }

        }
    }

}
