package cc.m2u.lottery.utils.https;

import android.app.Activity;

/**
 * Created by Administrator on 2017/5/3.
 */

public class TestActivity extends Activity {
    /*
    private Button btn, btn1;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initData();

    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        btn1 = (Button) findViewById(R.id.btn1);
    }

    private void initListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                HttpsPostThread thread = new HttpsPostThread(mHandler,
                        "https://certs.cac.washington.edu/CAtest/", list, 200);
                thread.start();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpsGetThread thread = new HttpsGetThread(mHandler,
                        "https://certs.cac.washington.edu/CAtest/", 200);
                thread.start();
            }
        });
    }

    private void initData() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                switch (msg.what) {
                    case 200:
                        // 请求成功
                        Log.e("TAG", "返回参数===" + result);
                        break;
                    case 404:
                        // 请求失败
                        Log.e("TAG", "请求失败!");
                        break;
                }

            }
        };
    }
    */
}
