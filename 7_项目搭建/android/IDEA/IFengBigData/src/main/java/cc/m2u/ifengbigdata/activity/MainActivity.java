package cc.m2u.ifengbigdata.activity;

import android.os.Bundle;
import android.view.MenuItem;
import cc.m2u.ifengbigdata.R;
import cc.m2u.ifengbigdata.activity.base.BaseCompatActivity;

/**
 * 主页
 */
public class MainActivity extends BaseCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getMenuId() {
        return 0;
    }

    @Override
    public void whenOptionsItemSelected(MenuItem item) {

    }
}
