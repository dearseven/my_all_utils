
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import com.cyan.Dlog
import com.cyaninject.main.CyanInjector

import java.io.File
import java.net.URLConnection
import javax.inject.Inject

class StorageFilePreviewActivity : AppCompatActivity(), IFileStorageResultListener {
    override fun getListenerId() = "StorageFilePreviewActivity"

    override fun onTransferProgress(listenerId: String, progressRate: Long, totalTransferredSoFar: Long, totalToTransfer: Long, fileAbsoluteName: String) {
    }

    override fun onRemoteOperationFinish(optEnum: OptEnum, result: Result) {
        when (optEnum) {
            OptEnum.DownloadFile -> {
                val isSuc = result.isSuccess()
                if (isSuc) {
                    Dlog.log3(javaClass, "下载成功")
                    openFile();
                } else {
                    Dlog.log3(javaClass, "下载失败")
                }
            }
        }
    }

    //当前访问的存储目录
    var tempPath = ""
    //要下载的文件的存放目录
    var downloadFolder = ""
    //要下载的文件的全路径
    var downloadFilePath = ""

    companion object {
        val REMOTE_FILE = "1"
    }

    lateinit var remoteFile: RaptorRemoteFile
    @Inject
    lateinit var mFileStorageClient: FileStorageClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage_file_preview)

        CyanInjector.injectActivity(this)
        NextcloudTalkApplication.getSharedApplication().intellivComponent.inject(this)
        mFileStorageClient.addListener(this)

        remoteFile = intent.extras.getParcelable(REMOTE_FILE)
        remoteFile?.also {
            val basePath = getFilesDir().absolutePath
            var previewDownloadPath = "${basePath}${File.separator}raptor_cache_download/in_storage"
            val file = File(previewDownloadPath)
            var flag = false;
            if (!file.exists()) {
                flag = file.mkdirs()
            } else {
                flag = true
            }
            if (flag) {
                downloadFolder = previewDownloadPath
                downloadFilePath = previewDownloadPath + remoteFile.remotePath//folderPath + filename
                Dlog.log3(javaClass, "downloadFolder=${downloadFolder}")
                if (File(downloadFilePath).exists()) {
                    //如果存在
                    //--调用对应的页面打开--
                    Dlog.log3(javaClass, "文件已经存在啊")
                    openFile();
                } else {
                    //如果不存在
                    mFileStorageClient.downloadFile(PDownloadFile(remoteFile.remotePath!!, downloadFolder, getHandler(), getListenerId()))
                    //--小于一定量的直接下载，大于一定量的要用户进入存储下载
                    Dlog.log3(javaClass, "文件不存在啊")
                }
            }
        }
    }



    private fun openFile() {
        //-------------

        try {
            val localFile = File(downloadFilePath)
            //获取文件的mimetype，然后唤起不同的页面打开
            var mime = MimetypeUtils.instance.getMimeType(localFile)
            if (TextUtils.isEmpty(mime)) {
                val fileNameMap = URLConnection.getFileNameMap()
                mime = fileNameMap.getContentTypeFor(localFile.absolutePath)
//                  解决部分三星手机无法获取到类型的问题
                if (TextUtils.isEmpty(mime)) {
                    val extension = MimeTypeMap.getFileExtensionFromUrl(localFile.absolutePath);
                    mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                }
            }
            Dlog.log3(javaClass, "mime=${mime}")
            if (TextUtils.isEmpty(mime)) {
                Toast.makeText(this, getString(R.string.failure_to_read_file_type_correctly), Toast.LENGTH_SHORT).show();
                finish()
                return
            }

            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            var uri: Uri? = null
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                uri = Uri.fromFile(localFile)
            } else {
                uri = FileProvider.getUriForFile(this, "com.teetaa.intellivusdemo.FileProvider", localFile)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
            }
            intent.setDataAndType(uri, mime)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.no_program_to_open_this_file_is_installed_on_the_phone), Toast.LENGTH_SHORT).show();
            finish()
        }
        //-------------
    }

    fun getHandler(): WeakHandler {
//        return (activity as BottomNavMainActivity).mHandler
        return NextcloudTalkApplication.mHandler
    }

    override fun onDestroy() {
        mFileStorageClient.removeListener(this)
        super.onDestroy()
    }
}
