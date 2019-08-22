package netease.li.com.wangyiyun.backs;

import android.app.Activity;
import android.view.View;

public class MyBacks implements View.OnClickListener {
    Activity activity;
    public MyBacks(Activity activity){
        this.activity=activity;
    }
    @Override
    public void onClick(View view) {
        activity.finish();
    }
}
