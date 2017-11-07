package app.cyan.cyanalbum.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;


import java.util.ArrayList;

import app.cyan.cyanalbum.R;

/**
 * Created by wx on 2017/10/24.
 */

public class PermissionAskActivity extends AppCompatActivity {
    /**
     * 大于M（6.0）动态权限申请
     */
    private static final int REQUEST_FINE_LOCATION_GTE_ANDROID_M = 1;


    /**
     * 返回到调用者，全部成功
     */
    public static final int RESULT_FOR_REQ_PERMISSION_SUC_ALL = 0;
    /**
     * 返回到调用者，没有全部成功
     */
    public static final int RESULT_FOR_REQ_PERMISSION_SUC_PART = 1;


    public static  boolean requestAllSuccess = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        //隐藏ActionBar
        getSupportActionBar().hide();
        //
        handler.sendEmptyMessageDelayed(MSG_PERMISSION_CHECK, 500);
    }

    /**
     * 动态权限申请
     */
    public void permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M) {

            ArrayList<String> permissionList = new ArrayList<String>();

            //存储
            int checkResult = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            checkResult = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            //麦克风
            checkResult = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.RECORD_AUDIO);
            }
            //相机
            checkResult = checkSelfPermission(Manifest.permission.CAMERA);
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CAMERA);
            }
            //
            checkResult = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_PHONE_STATE);
            }

            //
            checkResult = checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS);
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_PHONE_NUMBERS);
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
            checkResult = checkSelfPermission(Manifest.permission.CAMERA);
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CAMERA);
            }

            //位置
            checkResult = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            checkResult = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }


            //开始申请权限
            if (permissionList.size() > 0) {
                //requestPermissions(arrayOf(Manifest.permission_group.STORAGE, Manifest.permission_group.STORAGE), REQUEST_FINE_LOCATION_GTE_ANDROID_M)

                //var permissonArr = arrayOfNulls<String>(permissionList.size)
                //permissionList.toArray(permissonArr) java可以这样写，但是这里用kotlin的写法
                String [] permissionArr = permissionList.toArray(new String[permissionList.size()]);

                requestPermissions(permissionArr, REQUEST_FINE_LOCATION_GTE_ANDROID_M);
            } else {
                setResult(RESULT_FOR_REQ_PERMISSION_SUC_ALL);
                finish();
            }
        } else {
            setResult(RESULT_FOR_REQ_PERMISSION_SUC_ALL);
            finish();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_FINE_LOCATION_GTE_ANDROID_M) {
//                for (item in grantResults) {
//                }
//                for (i in 0..grantResults.size - 1) {
//                }
            for (int i=0;i<grantResults.length;i++) {//如果遍历list或者array可以通过索引进行迭代
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    requestAllSuccess = false;
                }
            }
            //关闭自己
            if (requestAllSuccess) {
                setResult(RESULT_FOR_REQ_PERMISSION_SUC_ALL);
            } else {
                setResult(RESULT_FOR_REQ_PERMISSION_SUC_PART);
            }
            finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public int MSG_PERMISSION_CHECK = 1;

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_PERMISSION_CHECK) {
                PermissionAskActivity.this.permissionCheck();
            }
            super.handleMessage(msg);
        }
    };
}
