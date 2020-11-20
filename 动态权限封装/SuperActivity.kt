
import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

/**
 *
动态权限
val permissions = arrayOf<String>(
android.Manifest.permission.READ_EXTERNAL_STORAGE,
android.Manifest.permission.WRITE_EXTERNAL_STORAGE
)
(activity as SuperActivity).askPermission(permissions, object : IPermissionCallBack {
override fun askPermissionResult(result: Boolean) {
Dlog.Toast(activity,"授权结果：$result")
}
}, "我们需要文件读取权限才能进行下一步")

actvityResult
val it = Intent(activity, CyanFileExplorerActivity::class.java)
(activity as SuperActivity).startActivityForResult(6001,it,object:IActivityResultCallBack{
override fun activityResultCallBack(requestCode: Int, resultCode: Int, data: Intent?) {
Dlog.Toast(activity,"selectDirectory activityResultCallBack")
}
})
 */
open class SuperActivity : AppCompatActivity() {
    companion object {
        val REQUEST_PEMISSION_CODE: Int = 2041
    }

    var permissionCallBack: IPermissionCallBack? = null
    var shouldShowRequestTip: String? = null

//    open lateinit var loading: LoadingDiaglog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        loading = LoadingDiaglog()
    }

    /**
     * permissionArr:权限数组
     * callBack:回调
     * shouldShowRequestTip：如果用户授权点了不再提醒，这是弹出狂的提示语
     */
    fun askPermission(permissionArr: Array<String>, callBack: IPermissionCallBack, shouldShowRequestTip: String) {
        permissionCallBack = callBack
        this.shouldShowRequestTip = shouldShowRequestTip
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArr, REQUEST_PEMISSION_CODE)
        } else {
            permissionCallBack?.askPermissionResult(true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PEMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) run {
                permissionCallBack?.askPermissionResult(true)
            } else {//如果用户点了不再提醒并且拒绝了授权
                permissionCallBack?.askPermissionResult(false)
                if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    //我们就弹出对话框告诉用户需要授权才能用,然后打开应用的权限页面,因为这个时候requestPermissions已经直接返回不成功了
                    showMessageOKCancel(shouldShowRequestTip!!,
                            DialogInterface.OnClickListener { dialog, which ->
                                //
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                //设置去向意图
                                val uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri            //发起跳转
                                startActivity(intent)
                            })
                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.go_pms_page), okListener)
                .setNegativeButton(getString(R.string.not_now), null)
                .create()
                .show()
    }


    //==============================startActivityForResult======================================
    var activityResultCallBack: IActivityResultCallBack? = null

    fun startActivityForResult(requestCode: Int, intent: Intent, callBack: IActivityResultCallBack) {
        activityResultCallBack = callBack
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultCallBack?.activityResultCallBack(requestCode, resultCode, data)
    }
}

interface IPermissionCallBack {
    open fun askPermissionResult(result: Boolean)
}

interface IActivityResultCallBack {
    open fun activityResultCallBack(requestCode: Int, resultCode: Int, data: Intent?)
}