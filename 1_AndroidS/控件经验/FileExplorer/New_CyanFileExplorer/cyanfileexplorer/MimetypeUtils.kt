import android.webkit.MimeTypeMap
import com.nextcloud.talk.R
import java.io.File
import java.util.*

/**
 * 获取文件的mimetype
 */
class MimetypeUtils private constructor() {
    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = MimetypeUtils()
    }

    fun getMimeType(file: File): String {
        var suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        var type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null && !(type?.isEmpty() ?: true)) {
            return type;
        }
        return "file/*";
    }

    private fun getSuffix(file: File): String? {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        var fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        var index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    //======================================================
    fun fromMimeTypeGetIcon(mimeType: String) =
            mimeType.let {
                when {
                    it.contains("DIR") -> R.drawable.raptor_ic_folder
                    it.contains("image") -> R.drawable.raptor_ic_file_image
                    it.contains("video") -> R.drawable.raptor_ic_file_image
                    it.contains("text") -> R.drawable.raptor_ic_file_text
                    else -> R.drawable.raptor_ic_file
                }
            }


    fun isDirectory(mimeType: String): Boolean {
        return mimeType.contains("DIR")
    }
}