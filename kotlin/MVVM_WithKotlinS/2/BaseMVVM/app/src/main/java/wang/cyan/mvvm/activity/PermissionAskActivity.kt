package wang.cyan.mvvm.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import wang.cyan.mvvm.R
import wang.cyan.mvvm.utils.DLog

/**
 * 这个activity是一个透明的activity，请在Launch打开(startActivityForResult)，然后等待它finish返回结果
 * Created by wx on 2017/6/13.
 */
class PermissionAskActivity : AppCompatActivity() {
    /**
     * 大于M（6.0）动态权限申请
     */
    private val REQUEST_FINE_LOCATION_GTE_ANDROID_M = 1

    companion object {
        /**
         * 返回到调用者，全部成功
         */
        val RESULT_FOR_REQ_PERMISSION_SUC_ALL = 0;
        /**
         * 返回到调用者，没有全部成功
         */
        val RESULT_FOR_REQ_PERMISSION_SUC_PART = 1;
    }


    private var requestAllSuccess = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_ask)

        //隐藏ActionBar
        supportActionBar!!.hide()
        //
        handler.sendEmptyMessageDelayed(MSG_PERMISSION_CHECK, 500)
    }

    /**
     * 动态权限申请
     */
    fun permissionCheck(): Unit {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && applicationInfo.targetSdkVersion >= Build.VERSION_CODES.M) {
            Toast.makeText(this@PermissionAskActivity, "PermissionCkeck!", Toast.LENGTH_SHORT).show();
            var permissionList = ArrayList<String>();

            //存储
            var checkResult = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            checkResult = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            //麦克风
            checkResult = checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.RECORD_AUDIO)
            }

            //电话
            /*checkResult = checkSelfPermission(Manifest.permission_group.PHONE)
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission_group.PHONE)
            }
            checkResult = checkSelfPermission(Manifest.permission.CALL_PHONE)
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CALL_PHONE)
            }*/

            //相机
            checkResult = checkSelfPermission(Manifest.permission.CAMERA)
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CAMERA)
            }

            //位置
            checkResult = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            checkResult = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }


            //开始申请权限
            if (permissionList.size > 0) {
                //requestPermissions(arrayOf(Manifest.permission_group.STORAGE, Manifest.permission_group.STORAGE), REQUEST_FINE_LOCATION_GTE_ANDROID_M)

                //var permissonArr = arrayOfNulls<String>(permissionList.size)
                //permissionList.toArray(permissonArr) java可以这样写，但是这里用kotlin的写法
                var permissionArr = permissionList.toTypedArray()

                requestPermissions(permissionArr, REQUEST_FINE_LOCATION_GTE_ANDROID_M)
            }else{
                setResult(RESULT_FOR_REQ_PERMISSION_SUC_ALL)
                finish()
            }
        }else{
            setResult(RESULT_FOR_REQ_PERMISSION_SUC_ALL)
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_FINE_LOCATION_GTE_ANDROID_M -> {
//                for (item in grantResults) {
//                }
//                for (i in 0..grantResults.size - 1) {
//                }
                for (i in grantResults.indices) {//如果遍历list或者array可以通过索引进行迭代
                    DLog.log(PermissionAskActivity::class.java, "${permissions[i]} result-> ${grantResults[i] == PackageManager.PERMISSION_GRANTED}")
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        requestAllSuccess = false;
                    }
                }
                //关闭自己
                if (requestAllSuccess) {
                    setResult(RESULT_FOR_REQ_PERMISSION_SUC_ALL)
                } else {
                    setResult(RESULT_FOR_REQ_PERMISSION_SUC_PART)
                }
                finish()
            }
            else -> {
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private val MSG_PERMISSION_CHECK: Int = 1;

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg!!.what) {
                MSG_PERMISSION_CHECK -> {
                    this@PermissionAskActivity.permissionCheck()
                }
                else -> {
                    //do ...
                }
            }
            super.handleMessage(msg)
        }
    }
}