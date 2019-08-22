package netease.li.com.wangyiyun.util;

import android.content.Context;
import android.icu.util.Measure;
import android.util.AttributeSet;
import android.widget.GridView;

public class NoScrollGridView extends GridView {
    public NoScrollGridView(Context context) {
        super(context);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE/2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, height);
    }
}