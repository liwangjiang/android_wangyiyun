package netease.li.com.wangyiyun.splash;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import netease.li.com.wangyiyun.R;

public class TimeView extends View {
    private TextPaint mTextPaint;
    private String  mContent="跳过";
    //文字的间距
    int padding=5;
    //内圆的直径
    int inner;
    //外圆的直径
    int all;
    //创建圆的画笔
    Paint mCircle;
    //定义绘制弧线区域
    RectF outerRect;
    //绘制弧线的画笔
    Paint outerP;

    //外圈的角度
    int dgree;

    //设置点击事件
    private OnTimeClickListener onClick;
    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context,attrs);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context,attrs);
    }
    private  void initData(Context context, @Nullable AttributeSet attrs){
        //获取到xml定义的属性
        TypedArray arry= context.obtainStyledAttributes(attrs, R.styleable.TimeView);
            //初始化画笔
        mTextPaint = new TextPaint();
        //抗锯齿
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(30);
        mTextPaint.setColor(Color.WHITE);//设置文字的颜色
        //文字的宽度
        float text_Width=mTextPaint.measureText(mContent);
        //内圆圈的直径
        inner = (int)text_Width+2*padding;
        //外圆圈的直径
        all=inner+2*padding;
        //内圆
        int innerColor=arry.getColor(R.styleable.TimeView_innerColor,Color.BLACK);
        mCircle = new Paint();
        mCircle.setFlags(Paint.ANTI_ALIAS_FLAG);
        mCircle.setColor(innerColor);

        //绘制外边的弧线
        int ringColor=arry.getColor(R.styleable.TimeView_ringColor,Color.GRAY);
        outerRect = new RectF(padding/2,padding/2,all-padding/2,all-padding/2);
        //设置外面弧线的画笔
        outerP = new Paint();
        outerP.setFlags(Paint.ANTI_ALIAS_FLAG);
        outerP.setColor(ringColor);
        outerP.setStyle(Paint.Style.STROKE);//将风格设置为空心
        outerP.setStrokeWidth(padding);//设置弧线的宽度

        //用完后回收定义属性
        arry.recycle();
    }
    //测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //文字的宽度+内圆的边距*2+画笔的宽度*2
        setMeasuredDimension(all,all);
    }
    public void setD(int d){//动态设置弧线的长度
        this.dgree=d;
    }
    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
       // canvas.drawColor(Color.RED);
        canvas.drawCircle(all/2,all/2,inner/2,mCircle);

        //画布旋转因为画笔的角度不正确
        canvas.save();//保存
        canvas.rotate(-90,all/2,all/2);//旋转
        canvas.drawArc(outerRect,0f,dgree,false,outerP);
        canvas.restore();

        //设置文字跳过
        float y = (canvas.getHeight()/2);
        //文字圆中居中
        float de = mTextPaint.descent();// +  两个值的正负
        float a = mTextPaint.ascent(); //  -
        canvas.drawText(mContent,2*padding,y-((de+a)/2),mTextPaint);

    }
    public void setProgess(int tatal,int now){
        if(tatal==0){
            return;
        }
        int space = 360 / tatal;
        dgree= space * now;
        //主线程中刷新
        invalidate();
        //子线程中刷新
        //postInvalidate();
    }
    //设置一个触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setAlpha(0.3f);//设置透明度
                break;
            case MotionEvent.ACTION_UP:
                setAlpha(1.0f);
                if(onClick!=null){
                    onClick.onClickTime();
                }
                break;

        }
        return true;
    }
    public void setOnClickListener(OnTimeClickListener onClick){
        this.onClick=onClick;
    }
}
