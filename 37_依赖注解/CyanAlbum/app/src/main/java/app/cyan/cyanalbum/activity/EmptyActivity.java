package app.cyan.cyanalbum.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import app.cyan.cyanalbum.R;
import app.cyan.cyanalbum.beans.Welcome;
import app.cyan.cyanalbum.databinding.ActivityEmptyBinding;
import app.cyan.cyanalbum.utils.SoftAny;

public class EmptyActivity extends AppCompatActivity {
    public static final Class GO_ACTIVITY_PERMISSION_CHECK = PermissionAskActivity.class;
    public static final Class GO_ACTIVITY_MAIN = PhotoBoardActivity.class;

    private static SoftAny _this = null;
    private Welcome welcomeData = null;

    private TextView annotation_tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_empty);
        _this = new SoftAny(this);
        //1获取绑定
        ActivityEmptyBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_empty);
        welcomeData = new Welcome();
        welcomeData.setWelcome("Hello");
        welcomeData.setName("cyan");
        //把对象放到binding的变量上
        binding.setWelcome(welcomeData);
        h.sendEmptyMessageDelayed(1, 1000);
        //annotation_tv1.setText("annotation_tv1!!");
    }

    @Override
    protected void onDestroy() {
        _this = null;
        super.onDestroy();
    }

    public static Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                EmptyActivity activity = _this.get(EmptyActivity.class);
                h.sendEmptyMessageDelayed(2, 1000);
                //更新已经绑定的对象，会更新ui，当然要对数据bean做修改
                activity.welcomeData.setWelcome("Now,");
                activity.welcomeData.setName("here we go!");
            } else if (msg.what == 2) {
                EmptyActivity activity = _this.get(EmptyActivity.class);
                Intent i = new Intent(activity, GO_ACTIVITY_PERMISSION_CHECK);
                activity.startActivityForResult(i, 7);
            } else
                super.handleMessage(msg);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7) {
            if (resultCode == PermissionAskActivity.RESULT_FOR_REQ_PERMISSION_SUC_ALL) {
                Toast.makeText(EmptyActivity.this, "全部权限申请成功", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PermissionAskActivity.RESULT_FOR_REQ_PERMISSION_SUC_ALL) {
                Toast.makeText(EmptyActivity.this, "部分权限申请失败，可能影响使用", Toast.LENGTH_SHORT).show();
            }
            Intent i = new Intent(EmptyActivity.this, GO_ACTIVITY_MAIN);
            startActivity(i);
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
