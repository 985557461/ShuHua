package com.xy.shuhua.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2016/6/13.
 */
public class CompressUtil {
    //Ñ¹ËõÍ¼Æ¬µ½Í¼Æ¬Ð¡ÓÚ150kÎªÖ¹
    public static String getCompressBmp(String filepath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        int height = options.outHeight * 200 / options.outWidth;
        options.outWidth = 200;
        options.outHeight = height;
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(filepath, options);
        String path = PathManager.getCropPhotoPath().getAbsolutePath();
        FileUtil.writeImage(bmp,path,100);
        return path;
    }
}
