
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class LocalThumbHelper {
    private constructor() {}

    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = LocalThumbHelper()
    }

    /**
     * 加载本地图片(文件流转换成图片)
     * @param url
     * @return
     */
    fun getLocalBitmap(pathName: String): Bitmap? {
        return try {
            var fis: FileInputStream = FileInputStream(pathName);
            BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片
        } catch (e: FileNotFoundException) {
            e.printStackTrace();
            null;
        }
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图
     *
     * @param path      图像的路径
     * @param maxWidth  指定输出图像的宽度
     * @param maxHeight 指定输出图像的高度
     * @return 生成的缩略图
     */
    fun getImageWithSize(path: String, maxWidth: Int, maxHeight: Int): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            var `in` = BufferedInputStream(FileInputStream(
                    File(path)))
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(`in`, null, options)
            `in`.close()
            var i = 0
            while (true) {
                if (options.outWidth shr i <= maxWidth && options.outHeight shr i <= maxHeight) {
                    `in` = BufferedInputStream(
                            FileInputStream(File(path)))
                    options.inSampleSize = Math.pow(2.0, i.toDouble()).toInt()
                    options.inJustDecodeBounds = false
                    bitmap = BitmapFactory.decodeStream(`in`, null, options)
                    break
                }
                i += 1
            }
        } catch (e: Exception) {
            return null
        }

        return bitmap
    }

    /**
     * 根据路径获取图片
     */
    fun getBitmap(pathName: String): Bitmap? {
        var file: File = File(pathName)
        var bm: Bitmap? = null
        if (file.exists()) {
            bm = BitmapFactory.decodeFile(pathName)
        }
        return bm
    }

    /**
     * 根据(视屏)路径获取缩略图
     */
    fun getVideoThumb(path: String): Bitmap? {
        var b: Bitmap? = null
        var media: MediaMetadataRetriever? = null
        try {
            media = MediaMetadataRetriever()
            media.setDataSource(path)
            b = media.frameAtTime
        } catch (ex: Exception) {

        } finally {
            try {
                media?.release()
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }

        }
        return b

    }
}