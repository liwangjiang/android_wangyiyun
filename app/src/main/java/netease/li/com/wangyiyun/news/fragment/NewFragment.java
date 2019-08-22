package netease.li.com.wangyiyun.news.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Annotation;
import java.lang.invoke.VolatileCallSite;
import java.util.ArrayList;

import me.hz89.swipeback.Utils;
import netease.li.com.wangyiyun.R;
import netease.li.com.wangyiyun.news.adapter.NewsAdapter;
import netease.li.com.wangyiyun.news.adapter.ShowAdaptr;
import netease.li.com.wangyiyun.news.bean.FragmentInfo;
import netease.li.com.wangyiyun.news.bean.ShowTabEvent;
import netease.li.com.wangyiyun.util.NoScrollGridView;
import netease.li.com.wangyiyun.util.SharePrenceUtil;


public class NewFragment extends Fragment {
    ArrayList<FragmentInfo> pages;
    private ImageView imageView;
    private boolean isAnimation=false;
    private RelativeLayout layout;
    private FrameLayout menu;

    //菜单栏的显示于关闭
    private NoScrollGridView show,not_show;

    private ShowAdaptr showAdapter;
    private ShowAdaptr not_showAdapter;
    private NewsAdapter adapter;
    private NewsAdapter newAdapter1;
    private SmartTabLayout smartTabLayout;//数据绑定...
    private ViewPager viewPager;//

    //缓存的名称
    private static String SHOW_CONTENT="show";
    private static String NOT_SHOW_CONTENT="not_show_content";

    //用于缓存当前的内容
    private String lastTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_news,container,false);
        imageView = view.findViewById(R.id.add);
        //初始添加按钮
        layout = view.findViewById(R.id.menu_title);
        //初始化清单
        menu = view.findViewById(R.id.menu);

        //初始化网格GridView
        show=view.findViewById(R.id.show);
        not_show=view.findViewById(R.id.not_show);

        //设置网格的点击事件
        show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //点击某一条
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    //判断网格状态
                if(showAdapter.getIsShow()){
                    if (position==0){
                        Toast.makeText(getContext(), "对不起 `"+showAdapter.getItem(position)+"`不能删除", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //是显示的
                    String title=showAdapter.deleteItem(position);
                    not_showAdapter.addItem(title);
                }
            }
        });
        not_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        if(not_showAdapter.getIsShow()){
                            String title = not_showAdapter.deleteItem(position);
                            showAdapter.addItem(title);
                        }
            }
        });
        //设置删除按钮
       final Button onClickIsShowDeleteImage=view.findViewById(R.id.deleteButton);
        onClickIsShowDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(showAdapter.getIsShow()){
                 onClickIsShowDeleteImage.setText("排序删除");
             }else{
                 onClickIsShowDeleteImage.setText("完成");
             }
                showAdapter.setShow();
                not_showAdapter.setShow();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {//图片设置点击事件
            @Override
            public void onClick(View view) {
                Animation animation;
                if(!isAnimation){
                     animation=AnimationUtils.loadAnimation(getContext(),R.anim.add_up);
                    //Animation动画的使用他一般是控件只是有效果但是没有真正意义上改变
                    animation.setFillAfter(true);
                    imageView.startAnimation(animation);
                    //显示menu
                    layout.setVisibility(View.VISIBLE);
                    //显示清单
                    menu.setVisibility(View.VISIBLE);
                    Animation mian_menu_show=AnimationUtils.loadAnimation(getContext(),R.anim.mian_menu_show);
                    menu.startAnimation(mian_menu_show);
                    Animation menu_show=AnimationUtils.loadAnimation(getContext(),R.anim.top_menu_show);
                    layout.startAnimation(menu_show);
                    EventBus.getDefault().post(new ShowTabEvent(true));
                    isAnimation=true;
                }else{
                    //设置按钮
                    if(showAdapter.getIsShow()){
                        showAdapter.setShow();
                        not_showAdapter.setShow();
                        onClickIsShowDeleteImage.setText("排序删除");
                    }


                    animation=AnimationUtils.loadAnimation(getContext(),R.anim.add_down);
                    //Animation动画的使用他一般是控件只是有效果但是没有真正意义上改变
                    EventBus.getDefault().post(new ShowTabEvent(false));
                    animation.setFillAfter(true);
                    imageView.startAnimation(animation);
                    //影藏menu
                    layout.setVisibility(View.GONE);
                    //隐藏清单
                    menu.setVisibility(View.GONE);
                    Animation mian_menu_hide=AnimationUtils.loadAnimation(getContext(),R.anim.mian_menu_hide);
                    menu.startAnimation(mian_menu_hide);
                    Animation menu_hide=AnimationUtils.loadAnimation(getContext(),R.anim.top_menu_hide);
                    menu_hide.setAnimationListener(new Animation.AnimationListener() {
                        //动画开始
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }
                        //动画结束
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layout.setVisibility(View.GONE);
                            menu.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    layout.startAnimation(menu_hide);

                    isAnimation=false;


                    //修改后的内容
                    String show_content=showAdapter.getContent();
                    String not_show_content=not_showAdapter.getContent();


                    SharePrenceUtil.saveString(getContext(),SHOW_CONTENT,show_content);
                    SharePrenceUtil.saveString(getContext(),NOT_SHOW_CONTENT,not_show_content);
                    if (lastTitle.equals(show_content)){
                        return;
                    }
                    String[] newTitle=show_content.split("-");
                    //pages清空
                    pages.clear();
                    for (int i=0;i<newTitle.length;i++) {
                        FragmentInfo titleBean;
                        if (i == 0) {
                            titleBean = new FragmentInfo(new HotFragment(), newTitle[i]);
                        } else {
                            titleBean = new FragmentInfo(new EmptyFragment(), newTitle[i]);
                        }
                        pages.add(titleBean);
                    }
                    newAdapter1.setData(pages);
                    //!!!!关键代码,自动绑定数据
                    smartTabLayout.setViewPager(viewPager);
                    smartTabLayout.setDividerColors(Color.TRANSPARENT);
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FrameLayout frameLayou = getActivity().findViewById(R.id.tabs);
        pages=new ArrayList<>();
        frameLayou.addView(View.inflate(getActivity(),R.layout.include_tab,null));//绑定数据
        //找到SmartTabLayout类
        smartTabLayout = getActivity().findViewById(R.id.smart_tab);

        viewPager = getActivity().findViewById(R.id.viewPageId);

        smartTabLayout.setViewPager(viewPager);//viewPager和SmartTabLayout进行绑定
        String title_content=SharePrenceUtil.getString(getContext(),SHOW_CONTENT);
        String[] title;
        if(TextUtils.isEmpty(title_content)){
             title=getResources().getStringArray(R.array.title_name);
             StringBuilder sb = new StringBuilder();
             for (int i=0;i<title.length;i++){
                 sb.append(title[i]);
                 if(i!=title.length){
                     sb.append("-");
                 }
             }
            lastTitle=sb.toString();
        }else{
            title=title_content.split("-");
            lastTitle=title_content;
        }
        //全部标题
        for (int i=0;i<title.length;i++){
            FragmentInfo titleBean;
            if (i==0){
                titleBean=new FragmentInfo(new HotFragment(),title[i]);
            }else{
                titleBean=new FragmentInfo(new EmptyFragment(),title[i]);
            }
            pages.add(titleBean);
        }

        showAdapter = new ShowAdaptr(title,getContext());
        show.setAdapter(showAdapter);

        String not_show_content=SharePrenceUtil.getString(getContext(),NOT_SHOW_CONTENT);
        if(TextUtils.isEmpty(not_show_content)){
            not_showAdapter = new ShowAdaptr(getContext());
        }else{
            String[] not_show=not_show_content.split("-");
            not_showAdapter = new ShowAdaptr(not_show,getContext());
        }
        not_show.setAdapter(not_showAdapter);


        newAdapter1 = new NewsAdapter(getActivity().getSupportFragmentManager(),pages);

        viewPager.setAdapter(newAdapter1);

        //!!!!关键代码,自动绑定数据
        smartTabLayout.setViewPager(viewPager);
        smartTabLayout.setDividerColors(Color.TRANSPARENT);
    }
}
