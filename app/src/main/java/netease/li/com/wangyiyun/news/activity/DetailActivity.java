package netease.li.com.wangyiyun.news.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import me.hz89.swipeback.SwipeBackLayout;
import me.hz89.swipeback.app.SwipeBackActivity;
import netease.li.com.wangyiyun.R;
import netease.li.com.wangyiyun.backs.MyBacks;
import netease.li.com.wangyiyun.news.bean.DetailWeb;
import netease.li.com.wangyiyun.news.bean.DetailWebImage;
import netease.li.com.wangyiyun.util.Constant;
import netease.li.com.wangyiyun.util.HttpResponse;
import netease.li.com.wangyiyun.util.HttpUtil;
import netease.li.com.wangyiyun.util.JsonUtil;

public class DetailActivity extends SwipeBackActivity {
    public static final String DOCID="docid";
    private String doc_id;
    private DetailWeb web;
    private String body;
    private MyHandler mHandler;
    private int INITSUCCESS=0;//成功
    private WebView mWebView;
    private TextView replayCountTextView;
    int replyCount ;
    private EditText feedback;
    private LinearLayout share_out;
    private TextView sendText;
    private boolean isFocus=false;
    private RelativeLayout parent;

    //滑动退出的控件
    private SwipeBackLayout mSwipeBackLayout;

    //图片的路径
    ArrayList<DetailWebImage> images;
    @JavascriptInterface
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_detail);
        findViewById(R.id.backsImageViewOnclick).setOnClickListener(new MyBacks(this));
        mSwipeBackLayout=getSwipeBackLayout();
        //初始化View
        initView();
        //设置文本框操作
        setEditHandler();
        //网络请求操作
        initHttp();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }


    private void initView(){
        mHandler=new MyHandler(this);
        mWebView = (WebView) findViewById(R.id.webViewId);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this,"demo");
        replayCountTextView = (TextView) findViewById(R.id.replayCount);
        parent = (RelativeLayout) findViewById(R.id.parent);
        //点击跳转到回复页面
        replayCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(DetailActivity.this,FeedbackActivity.class);
                intent.putExtra(DOCID,doc_id);
                startActivity(intent);
            }
        });
        //回复/
        feedback = (EditText) findViewById(R.id.feedback);
        share_out = (LinearLayout) findViewById(R.id.share_out);
        sendText = (TextView) findViewById(R.id.sendText);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    private void  setEditHandler(){
      final  InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        final Drawable left=getResources().getDrawable(R.drawable.biz_pc_main_tie_icon);
        left.setBounds(0,0,50,50);
        feedback.setCompoundDrawables(left,null ,null,null);
        feedback.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isfocus) {
                isFocus=isfocus;
                if(isfocus){
                    //有焦点
                    feedback.setCompoundDrawables(null,null ,null,null);//隐藏
                    feedback.setHint("");
                    sendText.setVisibility(View.VISIBLE);
                    share_out.setVisibility(View.INVISIBLE);
                }else{
                    // 获取软键盘的显示状态
                    boolean isOpen=imm.isActive();
                    // 如果软键盘已经显示，则隐藏，反之则显示
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                    //没有焦点
                    feedback.setCompoundDrawables(left,null ,null,null);
                    share_out.setVisibility(View.VISIBLE);
                    sendText.setVisibility(View.INVISIBLE);
                    feedback.setHint("写跟帖");
                }
            }
        });
    }

    private void initHttp(){
        Intent intent=getIntent();
        doc_id = intent.getStringExtra(DOCID);

        HttpUtil http=HttpUtil.getInstance();
        String url=Constant.getDetailUrl(doc_id);
        http.getData(url,new HttpResponse<String>(String.class){
            @Override
            public void onError(String msg) {

            }

            @Override
            public void onSuccess(String json) {
                if(json==null){
                    return;
                }
                try {
                    JSONObject js=new JSONObject(json);
                    JSONObject jsonObj=js.optJSONObject(doc_id);//吧数据转成正确的json对象
                    web=JsonUtil.parseJson(jsonObj.toString(), DetailWeb.class);
                    //Log.v("liwangjiang","web = "+web);
                    if(web!=null){
                        body = web.getBody();
                        images = web.getImg();
                        for (int i=0 ; i<images.size() ; i++){
                            String imageTag="<img onClick=\"show()\"  src='"+images.get(i).getSrc()+"'/>";
                            String Tag="<!--IMG#"+i+"-->";
                            body=body.replace(Tag,imageTag);
                        }
                        //点击量
                        replyCount=web.getReplyCount();
                        //p 标签代表一个段落
                        String titleHTML = "<p><span style='font-size:18px;'><strong>" + web.getTitle() + "</strong></span></p>";// 标题

                        titleHTML = titleHTML+ "<p><span style='color:#666666;'>"+web.getSource()+"&nbsp&nbsp"+web.getPtime()+"</span></p>";//来源与时间

                        body =titleHTML+body;
                        body="<html><head><style>img{width:100%}</style><script type='text/javascript'>function show(){window.demo.show()}</script></head><body>"+body+"</body></html>";
                        mHandler.sendEmptyMessage(INITSUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(isFocus){
            //让另外一个控件获取焦点
            parent.requestFocus();        //让其他控件获取焦点
        }else{
            finish();
        }
    }

    //该方法是提供给javaScript的方法
    @JavascriptInterface
    public void show(){
        Intent intent = new Intent();
        intent.setClass(this,DetailImageActiviry.class);
        intent.putExtra(DetailImageActiviry.intentName,images);
        startActivity(intent);
    }
    private void initWebView(){
        mWebView.loadDataWithBaseURL(null,body,"text/html","utf-8",null);
        replayCountTextView.setText(""+replyCount);
    }


    //弱应用
    class MyHandler extends Handler{
        WeakReference<DetailActivity> activity;
        public MyHandler(DetailActivity activity) {
            this.activity = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            DetailActivity detail= activity.get();
            if(detail==null){
                return;
            }
            detail.initWebView();

        }
    }
}
