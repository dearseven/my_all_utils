import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 拍照处理类
 * Created by wx on 2017/7/19.
 */
public class PictureTaker {
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
     * if(requestCode==PictureTaker.REQUEST_CODE_OPEN_ABLUM||
     * requestCode==PictureTaker.REQUEST_CODE_TAKE_NEW_IMG||
     * requestCode==PictureTaker.REQUEST_CODE_startPhotoZoom){
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
        if (requestCode == REQUEST_CODE_OPEN_ABLUM) {
            if (data != null)
                startPhotoZoom(data.getData());
        } else if (requestCode == REQUEST_CODE_TAKE_NEW_IMG) {
            startPhotoZoom(Uri.fromFile(new File(tempFilePath)));
        } else if (requestCode == REQUEST_CODE_startPhotoZoom) {
            if (data != null) {
                setPicToView(data);
            }
        }
    }

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

    public PictureTaker(Activity a, ImageView forShowImage, OnTaked onTaked) {
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
     * 选择提示对话框,拍照或者相册图片
     */
    public void ShowPickDialog() {
        new AlertDialog.Builder(activity)
                .setTitle("上传图片")
                .setNegativeButton("从相册挑选",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                                Intent intent = new Intent(Intent.ACTION_PICK,
                                        null);

                                intent.setDataAndType(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        "image/*");
                                activity.startActivityForResult(intent, REQUEST_CODE_OPEN_ABLUM);
//                                activityoverridePendingTransition(R.anim.left_in,
//                                        R.anim.right_out);
                            }
                        })
                .setPositiveButton("拍摄并上传",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
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
                                    activity.startActivityForResult(intent, REQUEST_CODE_TAKE_NEW_IMG);
//                                    overridePendingTransition(R.anim.left_in,
//                                            R.anim.right_out);

                                } else {
                                    Toast.makeText(
                                            activity,
                                            "请插入SD卡", 0)
                                            .show();
                                }
                            }
                        }).show();
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
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
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
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
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
            /**
             *
             */

            /**
             * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
             */

            // ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            // byte[] b = stream.toByteArray(); // 将图片流以字符串形式存储下来

            // tp = new String(Base64Coder.encodeLines(b));
            // 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了， 服务器处理的方法是服务器那边的事了，吼吼

            // 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换 为我们可以用的图片类型就OK啦...吼吼
            // Bitmap dBitmap = BitmapFactory.decodeFile(tp);
            // Drawable drawable = new BitmapDrawable(dBitmap);

        }
    }
}
