package app.cyan.cyanalbum.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.jap.myprocess.annotations.BindView;
import com.jap.myprocess.annotations.OnClick;

import app.cyan.cyanalbum.R;
import app.cyan.cyanalbum.annotation_processor_test.helper.BindHelper;

public class AnnotationActivity extends AppCompatActivity {
    @BindView(R.id.annotation_tv1)
    TextView initTv;


    @OnClick({R.id.annotation_tv1})
    public void clickInitTv() {
        Toast.makeText(this, "show", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
        BindHelper.bind(this);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                initTv.setText("click me!");
            }
        },500);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BindHelper.unbind(this);
    }

    private static Handler h=new Handler(){};
}
