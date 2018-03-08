
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Surface;
import android.widget.ImageView;
import android.widget.Toast;
import kotlin.Deprecated;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.*;

import static android.view.OrientationEventListener.ORIENTATION_UNKNOWN;

/**
 * Created by wx on 2017/7/28.
 */
public class PicTakeNotClip {
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
     * else{;
     * <p>
     * }
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_OPEN_ABLUM) {
//            if (data != null)
//                startPhotoZoom(data.getData());
//        } else if (requestCode == REQUEST_CODE_TAKE_NEW_IMG) {
//            startPhotoZoom(Uri.fromFile(new File(tempFilePath)));
//        } else if (requestCode == REQUEST_CODE_startPhotoZoom) {
//            if (data != null) {
//                setPicToView(data);
//            }
//        }

        if (requestCode == REQUEST_CODE_TAKE_NEW_IMG && resultCode == Activity.RESULT_OK) {
            DLog.log(getClass(), "resultCoderesultCode:" + resultCode);
            //不裁剪
            // startPhotoZoom(Uri.fromFile(new File(tempFilePath)));
            Bitmap bm = BitmapFactory.decodeFile(tempFilePath);
            bm = zoomImage(bm);
            setPicToView(bm);
        } else if (requestCode == REQUEST_CODE_OPEN_ABLUM && resultCode == Activity.RESULT_OK) {
            //判断手机系统版本号
            if (Build.VERSION.SDK_INT > 19) {
                //4.4及以上系统使用这个方法处理图片
                handleImgeOnKitKat(data);
            } else {
                handleImageBeforeKitKat(data);
            }

            //setPicToView(data);
        }
    }

//    /**
//     * 转换成指定大小
//     *
//     * @param bgimage
//     * @param newWidth
//     * @param newHeight
//     * @return
//     */
//    public Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
//        // 获取这个图片的宽和高
//        float width = bgimage.getWidth();
//        float height = bgimage.getHeight();
//        DLog.log(getClass(), "w:" + width + ",h:" + height);
//        //获取图片翻转
//        int degress = readPictureDegree(tempFilePath);
//        // 创建操作图片用的matrix对象
//        Matrix matrix = new Matrix();
//        //吧图片翻转回去
//        matrix.setRotate(degress);
//        // 计算宽高缩放率
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        DLog.log(getClass(), "sw:" + scaleWidth + ",sh:" + scaleHeight);
//
//        // 缩放图片动作
//        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
//        //bgimage.recycle();
//        return bitmap;
//    }


    /**
     * 转换成指定大小
     *
     * @param bgimage
     * @return
     */
    public Bitmap zoomImage(Bitmap bgimage) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        DLog.log(getClass(), "w:" + width + ",h:" + height);
        //获取图片翻转
        int degress = readPictureDegree(tempFilePath);
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        //吧图片翻转回去
        matrix.setRotate(degress);
        // 计算宽高缩放率
        float scaleWidth = ((float) width / 10) / width;
        float scaleHeight = ((float) height / 10) / height;
        DLog.log(getClass(), "sw:" + scaleWidth + ",sh:" + scaleHeight);

        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        //bgimage.recycle();
        return bitmap;
    }

    public static final int REQUEST_CODE_OPEN_ABLUM = 5123;
    public static final int REQUEST_CODE_TAKE_NEW_IMG = 5124;
    public static final int REQUEST_CODE_startPhotoZoom = 5125;

    private Activity activity;
    private OnTaked onTaked;
    private ImageView imageView;
    private int justIndex;
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

    public PicTakeNotClip(Activity a, ImageView forShowImage, OnTaked onTaked/*,int justIndex*/) {
        this.onTaked = onTaked;
        this.imageView = forShowImage;
        this.activity = a;
        //this.justIndex=justIndex;
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

                                if (Build.VERSION.SDK_INT >= 24) {//N
                                    File temp = new File(tempFileParentPath
                                            + "/temp.jpg");
                                    tempFilePath = temp.getAbsolutePath();
                                    //Android 7.0调用系统相册
                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.setType("image/*");
                                    Uri uriForFile = FileProvider.getUriForFile(activity, "cc.m2u.lotterymerchantside.fileprovider", temp);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    activity.startActivityForResult(intent, REQUEST_CODE_OPEN_ABLUM);
                                } else {
                                    Intent intent = new Intent(Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setDataAndType(
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            "image/*");
                                    activity.startActivityForResult(intent, REQUEST_CODE_OPEN_ABLUM);
                                }

                            }
                        })
                .setPositiveButton("拍摄并上传",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                //Android 7.0调用系统直接拍照额外在Appliction的OnCreate做了处理，所以这类不处理了
                                if (android.os.Environment
                                        .getExternalStorageState()
                                        .equals(android.os.Environment.MEDIA_MOUNTED)) {
                                    File temp = new File(tempFileParentPath
                                            + "/temp.jpg");
                                    tempFilePath = temp.getAbsolutePath();
                                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                                    Intent intent = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);
                                    //intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                                    // 下面这句指定调用相机拍照后的照片存储的路径
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(temp));
                                    activity.startActivityForResult(intent, REQUEST_CODE_TAKE_NEW_IMG);
                                } else {
                                    Toast.makeText(
                                            activity,
                                            "请插入SD卡", 0)
                                            .show();
                                }
                            }
                        }).show();
    }

//    /**
//     * 裁剪图片方法实现
//     *
//     * @param uri
//     */
//    private void startPhotoZoom(Uri uri) {
//
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);
//        activity.startActivityForResult(intent, REQUEST_CODE_startPhotoZoom);
//        //overridePendingTransition(R.anim.left_in, R.anim.right_out);
//    }

    /**
     * 用户保存图片信息
     */
    private Bundle extras = null;

    /**
     * 用于上传的头像图片
     */
    private File readyForUpload = null;

    /**
     * 4.4以下系统处理图片的方法
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }


    /**
     * 4.4及以上系统处理图片的方法
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImgeOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(activity, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                DLog.log(getClass(), "1");
                //解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                DLog.log(getClass(), "2");
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                DLog.log(getClass(), "3");
                //如果是content类型的uri，则使用普通方式处理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                DLog.log(getClass(), "4");
                //如果是file类型的uri，直接获取图片路径即可
                imagePath = uri.getPath();
            }
            //根据图片路径显示图片
            displayImage(imagePath);
        }
    }

    /**
     * 根据图片路径显示图片的方法
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            bitmap = zoomImage(bitmap);
            setPicToView(bitmap);
        } else {
//            ToastUtil.showShort(this,"failed to get image");
        }
    }

    /**
     * 通过uri和selection来获取真实的图片路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = activity.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

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

    /**
     * 获取图片的旋转角度
     */
    private int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

//    /**
//     * 将图片的旋转角度置为0
//     */
//    private void setPictureDegreeZero(String path) {
//        try {
//            ExifInterface exifInterface = new ExifInterface(path);
//            //修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
//            //例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
//            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
//            exifInterface.saveAttributes();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private void setPictrait(String bitmapUrl) {
//        try {
//            int degree = 0;
//            ExifInterface exifInterface = new ExifInterface(bitmapUrl);
//            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    degree = 90;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    degree = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    degree = 270;
//                    break;
//                default:
//                    degree = 0;
//            }

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
