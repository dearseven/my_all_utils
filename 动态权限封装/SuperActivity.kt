open class SuperActivity : AppCompatActivity() {
    companion object {
        val REQUEST_PEMISSION_CODE: Int = 2041
    }

    var permissionCallBack: IPermissionCallBack? = null
    var shouldShowRequestTip: String? = null

    open lateinit var loading: LoadingDiaglog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loading = LoadingDiaglog()
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
}

interface IPermissionCallBack {
    open fun askPermissionResult(result: Boolean)
}