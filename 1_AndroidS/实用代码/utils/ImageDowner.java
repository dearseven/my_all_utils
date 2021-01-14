
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wx on 2017/7/29.
 */
public class ImageDowner {
    private Context ctx;
    private OnDownload od;
    private String downLoadKey;

    public ImageDowner(Context ctx, String downLoadKey, OnDownload od) {
        this.ctx = ctx;
        this.od = od;
        this.downLoadKey = downLoadKey;
    }

    public File down(String _url) {
        try {
            URL url = new URL(_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //写一个文件
                return compressFile(bitmap);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private File compressFile(Bitmap photo) {
        String basePath = Environment
                .getExternalStorageDirectory() + "/" + ctx.getPackageName() + "/";
        String imgUrlForUpload = basePath + "pic/for_upload/";
        try {
            File real = new File(imgUrlForUpload);
            if (!real.exists())
                real.mkdirs();
            File readyForUpload = new File(imgUrlForUpload + "/"
                    + System.currentTimeMillis() + ".jpg");
            photo.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(readyForUpload));

//            // 先直接想显示的图片切换成新的
//            Drawable drable = new BitmapDrawable(ctx.getResources(),
//                    BitmapFactory.decodeStream(new FileInputStream(
//                            readyForUpload)));
//            forShow.setImageDrawable(drable);

            //回调设置成功，可以上传图片到服务器了
            //Toast.makeText(activity, "上传图片地址:" + readyForUpload.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            od.picDownloaded(readyForUpload, downLoadKey);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface OnDownload {

        void picDownloaded(File f, String key);
    }
}
