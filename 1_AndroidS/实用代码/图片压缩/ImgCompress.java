package com.teetaa.outsourcing.carinspection.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ImgCompress {


//    /**
//     * 质量压缩方法 用这个
//     *
//     * @param image
//     * @return
//     */
//    private static Bitmap compressImageLess1M(Bitmap image) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = baos.size() / (153600 * 2); //40;
//        //while (baos.size() > 153600 * 2) { // 循环判断如果压缩后图片是否大于300kb,大于继续压缩
//        baos.reset(); // 重置baos即清空baos
//        image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//        options = (options / 2);// 每次都减少一部分
//        //}
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
//        return bitmap;
//    }


    //    public static File compressImage(File file) {
//        Log.i("compressImage", "-------=-------=----------=----------=--------=-------");
//
//        File newFile = null;
//        String name = file.getName();
//        String newName = name + "_" + name;
//        String newAbsoluteFath = file.getAbsolutePath().replace(name, "") + newName;
//        Log.i("compressImage", "name=" + name + ",newName=" + newName);
////        Log.i("compressImage", "newAbsoluteFath=" + newAbsoluteFath);
//
//        try {
//            Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(file));
//            bm = compressImageLess1M(bm);
//            //
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            // 把压缩后的数据存放到baos中
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            try {
//                newFile = new File(newAbsoluteFath);
//                if (newFile.exists()) {
//                    newFile.delete();
//                }
//                newFile.createNewFile();
//                FileOutputStream fos = new FileOutputStream(newFile);
//                fos.write(baos.toByteArray());
//                fos.flush();
//                fos.close();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return newFile;
//    }
//图片允许最大空间 单位：KB
    static double maxSize = 256.00;

    public static File compressImage(File file) {
        Log.i("compressImage", "-------=-------=----------=----------=--------=-------");

        File newFile = null;
        String name = file.getName();
        String newName = name + "_" + name;
        String newAbsoluteFath = file.getAbsolutePath().replace(name, "") + newName;
        Log.i("compressImage", "name=" + name + ",newName=" + newName);
//        Log.i("compressImage", "newAbsoluteFath=" + newAbsoluteFath);

        try {
            Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(file));
            bm = imageZoom(bm);
            //
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 把压缩后的数据存放到baos中
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            Log.i("ImgCompress", "after imageZoom 1=" + (baos.size() / 1024));
            while (baos.size() / 1024.0 > maxSize) {
                bm = imageZoom(bm);
                //
                baos = new ByteArrayOutputStream();
                // 把压缩后的数据存放到baos中
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            }
            Log.i("ImgCompress", "after imageZoom 2=" + (baos.size() / 1024));

            try {
                newFile = new File(newAbsoluteFath);
                if (newFile.exists()) {
                    newFile.delete();
                }
                newFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(newFile);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile;
    }
    //----------------------------------------------------------------------------

    private static Bitmap imageZoom(Bitmap bitMap) {

        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] b = baos.toByteArray();
//        //将字节换成KB
//        double mid = b.length / 1024;
        double mid = baos.size() / 1024;
        Log.i("ImgCompress", "mid=" + mid);
        //判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
                    bitMap.getHeight() / Math.sqrt(i));
            //imageZoom(bitMap);
        }
        return bitMap;
    }


    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
// 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
// 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
// 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
// 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    //----------------------
}
