package com.teetaa.testwh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.teetaa.Debug;
import com.teetaa.testwh.eventbus_param.ChangeFragmentShowHide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    LinearLayout ly1, ly2, ly3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ly1 = (LinearLayout) findViewById(R.id.ll_1);
        ly2 = (LinearLayout) findViewById(R.id.ll_2);
        ly3 = (LinearLayout) findViewById(R.id.ll_3);
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        Toast.makeText(this, "获取宽度" + getResources().getDimension(R.dimen.x1280) + ";高度" + getResources().getDimension(R.dimen.y720), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public static final String ACTION_NAME = "com.teetaa.testwh.change";
    /**
     * 用于接受 页面刷新
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME)) {
                int type = intent.getIntExtra("index", 1);
                if (type == 1) {
                    ly1.setVisibility(View.VISIBLE);
                    ly2.setVisibility(View.GONE);
                    ly3.setVisibility(View.GONE);
                } else if (type == 2) {
                    ly2.setVisibility(View.VISIBLE);
                    ly1.setVisibility(View.GONE);
                    ly3.setVisibility(View.GONE);
                } else if (type == 3) {
                    ly3.setVisibility(View.VISIBLE);
                    ly1.setVisibility(View.GONE);
                    ly2.setVisibility(View.GONE);
                } else {
                    ly1.setVisibility(View.VISIBLE);
                    ly2.setVisibility(View.GONE);
                    ly3.setVisibility(View.GONE);
                }
                //刷新数据
                // title.setText(intent.getStringExtra("title"));
            }
        }

    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            Debug.i(MainActivity.class, event.getAction() + "," + event.getKeyCode());
        }
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_UP://向上
                Debug.i(MainActivity.class, "－－－－－向上－－－－－");
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN://向下
                Debug.i(MainActivity.class, "－－－－－向下－－－－－");
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT://向左
                Debug.i(MainActivity.class, "－－－－－向左－－－－－");
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT://向右
                Debug.i(MainActivity.class, "－－－－－向右－－－－－");
                break;
            case KeyEvent.KEYCODE_ENTER://确定
                Debug.i(MainActivity.class, "－－－－－确定－－－－－");
                break;
            case KeyEvent.KEYCODE_BACK://返回
                Debug.i(MainActivity.class, "－－－－－返回－－－－－");
                break;
            case KeyEvent.KEYCODE_HOME://房子
                Debug.i(MainActivity.class, "－－－－－房子－－－－－");
                break;
            case KeyEvent.KEYCODE_MENU://菜单
                Debug.i(MainActivity.class, "－－－－－菜单－－－－－");
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeFragmentShowHide param) {
        if (param.id == 1) {
            ly1.setVisibility(View.VISIBLE);
            ly2.setVisibility(View.GONE);
            ly3.setVisibility(View.GONE);
        } else if (param.id == 2) {
            ly2.setVisibility(View.VISIBLE);
            ly1.setVisibility(View.GONE);
            ly3.setVisibility(View.GONE);
        } else if (param.id == 3) {
            ly3.setVisibility(View.VISIBLE);
            ly1.setVisibility(View.GONE);
            ly2.setVisibility(View.GONE);
        } else {
            ly1.setVisibility(View.VISIBLE);
            ly2.setVisibility(View.GONE);
            ly3.setVisibility(View.GONE);
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Integer i) {
        if (i == 1) {
            ly1.setVisibility(View.VISIBLE);
            ly2.setVisibility(View.GONE);
            ly3.setVisibility(View.GONE);
        } else if (i == 2) {
            ly2.setVisibility(View.VISIBLE);
            ly1.setVisibility(View.GONE);
            ly3.setVisibility(View.GONE);
        } else if (i == 3) {
            ly3.setVisibility(View.VISIBLE);
            ly1.setVisibility(View.GONE);
            ly2.setVisibility(View.GONE);
        } else {
            ly1.setVisibility(View.VISIBLE);
            ly2.setVisibility(View.GONE);
            ly3.setVisibility(View.GONE);
        }
    };
}
