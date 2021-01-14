
import android.webkit.MimeTypeMap
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
}