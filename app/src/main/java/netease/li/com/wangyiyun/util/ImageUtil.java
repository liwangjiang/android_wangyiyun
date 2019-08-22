package netease.li.com.wangyiyun.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class ImageUtil {
    //判断文件是否存在
    public static boolean checkImageIsDownLoad(String imageName){
        File image = getFileByName(imageName);
        if(!image.exists()){//判断文件是否存在
            Bitmap bitmap= getImageBitMapByFile(image);
            if(bitmap!=null){//判断文件是否存在
                return true;
            }
        }
        return false;
    }
    public static File  getFileByName(String imageName){
        //先读一下SD卡文件
        File SD = Environment.getExternalStorageDirectory();
        File cacheFile =new File(SD,Constant.CACHE);
        File image= new File(cacheFile,imageName+".jpg");
        return image;
    }
    //Bitmap是否存在
    public static Bitmap getImageBitMapByFile(File image){
        return BitmapFactory.decodeFile(image.getAbsolutePath());
    }

    //判断文件是否
    public static boolean fileExists(){
        File SD= Environment.getExternalStorageDirectory();//得到SD卡的路径
        File cacheFile = new File(SD, Constant.CACHE);//Constant.CACHE表示是否隐藏后缀
        return cacheFile.exists();//如果存在返回ture
    }

}
