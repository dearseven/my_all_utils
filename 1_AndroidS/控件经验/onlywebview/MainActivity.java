
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toast.makeText(this, "3秒后跳转", Toast.LENGTH_SHORT).show();
        h.sendEmptyMessageDelayed(1, 3000);
    }

    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String pkg = "....";
                String cls = "...";
                Intent mIntent = new Intent();
                mIntent.setAction("...");
                mIntent.putExtra("pid", 1024);
                mIntent.putExtra("vid", 2048);
                mIntent.putExtra("title", "1标题啊");
                mIntent.putExtra("img", "1图片地址");
                ComponentName mComp = new ComponentName(pkg, cls);//注意AcitivityName(目标应用程序)要完整的，带包名的PackageName的
                mIntent.setComponent(mComp);
                startActivity(mIntent);
            } else
                super.handleMessage(msg);
        }
    };
}
