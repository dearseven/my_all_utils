
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.*;

/**
 * 拍照处理类
 * Created by wx on 2017/7/19.
 */
public class PictureTakerOnlyTakeAndResize {
    public interface OnTaked {
        /**
         * 图片设置成功，可以上传了
         *
         * @param f
         */
        void pictureTaked(File f);
    }

    /**
     * 要在对应的Activity.onActivityResult调用这个方法
     * if(requestCode==PictureTakerOnlyTakeAndResize.REQUEST_CODE_OPEN_ABLUM||
     * requestCode==PictureTakerOnlyTakeAndResize.REQUEST_CODE_TAKE_NEW_IMG||
     * requestCode==PictureTakerOnlyTakeAndResize.REQUEST_CODE_startPhotoZoom){
     * pictureTaker.handleActivityResult(requestCode,resultCode,data)
     * }
     * else{
     * <p>
     * }
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_TAKE_NEW_IMG) {
            //不裁剪
            // startPhotoZoom(Uri.fromFile(new File(tempFilePath)));
            Bitmap bm = BitmapFactory.decodeFile(tempFilePath);
            bm = zoomImage(bm, 720, 1280);
            setPicToView(bm);
        }
    }

    /**
     * 转换成指定大小
     *
     * @param bgimage
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
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
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        bgimage.recycle();
        return bitmap;
    }


//    public static int getBitmapSize(Bitmap bitmap) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {     //API 19
//            return bitmap.getAllocationByteCount();
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
//            return bitmap.getByteCount();
//        }
//        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
//    }

    public static final int REQUEST_CODE_OPEN_ABLUM = 5123;
    public static final int REQUEST_CODE_TAKE_NEW_IMG = 5124;
    public static final int REQUEST_CODE_startPhotoZoom = 5125;

    private Activity activity;
    private OnTaked onTaked;
    private ImageView imageView;
    /**
     * 临时放头像的地址,用于上传的
     **/
    private String imgUrlForUpload = null;
    /**
     * 头像零时文件的文件夹地址*
     */
    private String tempFileParentPath;
    /**
     * 头像零时文件地址，用于裁剪的
     **/
    private String tempFilePath;

    public PictureTakerOnlyTakeAndResize(Activity a, ImageView forShowImage, OnTaked onTaked) {
        this.onTaked = onTaked;
        this.imageView = forShowImage;
        this.activity = a;
        String basePath = Environment
                .getExternalStorageDirectory() + "/" + activity.getPackageName() + "/";
        imgUrlForUpload = basePath + "pic/for_upload/";

        // 要被编辑或上传的头像文件临时写在sd卡上的目录地址
        File folder = new File(basePath + "pic/for_temp");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        tempFileParentPath = folder.getAbsolutePath();
    }


    /**
     * 拍照
     */
    public void ShowPickDialog() {
        if (android.os.Environment
                .getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)) {
            File temp = new File(tempFileParentPath
                    + "/temp.jpg");
            tempFilePath = temp.getAbsolutePath();

            Intent intent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            // 下面这句指定调用相机拍照后的照片存储的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(temp));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            activity.startActivityForResult(intent, REQUEST_CODE_TAKE_NEW_IMG);
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 2448);
        intent.putExtra("aspectY", 3264);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 2448);
        intent.putExtra("outputY", 3264);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, REQUEST_CODE_startPhotoZoom);
        //overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    /**
     * 用户保存图片信息
     */
    private Bundle extras = null;

    /**
     * 用于上传的头像图片
     */
    private File readyForUpload = null;


    /**
     * 保存裁剪之后的图片数据
     *
     * @param photo
     */
    private void setPicToView(Bitmap photo) {
        try {
            File real = new File(imgUrlForUpload);
            if (!real.exists())
                real.mkdirs();
            readyForUpload = new File(imgUrlForUpload + "/"
                    + System.currentTimeMillis() + ".jpg");
            photo.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(readyForUpload));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 先直接想显示的图片切换成新的
        try {
            Drawable drable = new BitmapDrawable(activity.getResources(),
                    BitmapFactory.decodeStream(new FileInputStream(
                            readyForUpload)));
            imageView.setImageDrawable(drable);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //回调设置成功，可以上传图片到服务器了
        //Toast.makeText(activity, "上传图片地址:" + readyForUpload.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        onTaked.pictureTaked(readyForUpload);
    }
}
