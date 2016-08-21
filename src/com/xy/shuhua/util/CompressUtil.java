package com.xy.shuhua.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2016/6/13.
 */
public class CompressUtil {
    //ѹ��ͼƬ��ͼƬС��150kΪֹ
    public static String getCompressBmp(String filepath){
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filepath, options);
//        int height = options.outHeight * 200 / options.outWidth;
//        options.outWidth = 200;
//        options.outHeight = height;
//        options.inJustDecodeBounds = false;
//        Bitmap bmp = BitmapFactory.decodeFile(filepath, options);
//        String path = PathManager.getCropPhotoPath().getAbsolutePath();
//        FileUtil.writeImage(bmp,path,50);
//        return path;


        //ͼƬ�������ռ�   ��λ��KB
        double maxSize =120.00;
        //��bitmap���������У�����bitmap�Ĵ�С����ʵ�ʶ�ȡ��ԭ�ļ�Ҫ��
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitMap = BitmapFactory.decodeFile(filepath);
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //���ֽڻ���KB
        double mid = b.length/1024;
        //�ж�bitmapռ�ÿռ��Ƿ�����������ռ�  ���������ѹ�� С����ѹ��
        if (mid > maxSize) {
            //��ȡbitmap��С ����������С�Ķ��ٱ�
            double i = mid / maxSize;
            //��ʼѹ��  �˴��õ�ƽ���� ������͸߶�ѹ������Ӧ��ƽ������ ��1.���̶ֿȺ͸߶Ⱥ�ԭbitmap����һ�£�ѹ����Ҳ�ﵽ������Сռ�ÿռ�Ĵ�С��
            String path = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
                    bitMap.getHeight() / Math.sqrt(i));
            return path;
        }else{
            return filepath;
        }
    }

    /***
     * ͼƬ�����ŷ���
     *
     * @param bgimage
     *            ��ԴͼƬ��Դ
     * @param newWidth
     *            �����ź���
     * @param newHeight
     *            �����ź�߶�
     * @return
     */
    public static String zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // ��ȡ���ͼƬ�Ŀ�͸�
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // ��������ͼƬ�õ�matrix����
        Matrix matrix = new Matrix();
        // ������������
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // ����ͼƬ����
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        String path = PathManager.getCropPhotoPath().getAbsolutePath();
        FileUtil.writeImage(bitmap,path,80);
        return path;
    }
}
