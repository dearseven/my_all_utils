package wang.cyan.mvvm.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import wang.cyan.mvvm.R;


/**
 * Created by wx on 2017/3/19.
 * <br/>
 * 1 记得新建一个theme给application，用nocationbar主题，参见styles.xml
 * <br/>
 * 2 子类的layout记得include layout/base_toolbar.xml
 * 3.这个类是为了让左侧的nav按钮有自己的实现
 */

public abstract class BeImplNavIconBaseCompatActivity extends AppCompatActivity {

    private static final String TAG = BeImplNavIconBaseCompatActivity.class.getSimpleName();
    private TextView mToolbarTitle;
    private TextView mToolbarSubTitle;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
       /*
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Title");
        toolbar.setSubtitle("Sub Title");
        */
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarSubTitle = (TextView) findViewById(R.id.toolbar_subtitle);

        //Log.d("intelliCyan",mToolbarTitle+" "+mToolbarSubTitle);
        if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
        }
        if (mToolbarTitle != null) {
            //getTitle()的值是activity的android:lable属性值
            mToolbarTitle.setText(getTitle());
            //设置默认的标题不显示
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * 判断是否有Toolbar,并默认显示返回按钮
         */
        if (null != getToolbar() && isShowBacking()) {
            showBack();
        }
    }

    /**
     * 获取头部标题的TextView
     *
     * @return
     */
    public TextView getToolbarTitle() {
        return mToolbarTitle;
    }

    /**
     * 获取头部标题的TextView
     *
     * @return
     */
    public TextView getSubTitle() {
        return mToolbarSubTitle;
    }

    /**
     * 设置头部标题
     *
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        } else {
            getToolbar().setTitle(title);
            setSupportActionBar(getToolbar());
        }
    }

    /**
     * this Activity of tool bar.
     * 获取头部.
     *
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    private void showBack() {
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
        getToolbar().setNavigationIcon(/*android.R.drawable.ic_menu_revert*/getNavIconImageId());
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                onNavIconClick();
            }
        });

    }

    /**
     * 导航栏左侧按钮的图片id
     *
     * @return
     */
    public abstract int getNavIconImageId();

    /**
     * 导航栏左侧按钮的事件实现
     */
    public abstract void onNavIconClick();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuId() != 0) {
            getMenuInflater().inflate(getMenuId(), menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        whenOptionsItemSelected(item);
        return true;
    }

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     *
     * @return
     */
    protected boolean isShowBacking() {
        return true;
    }

    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     *
     * @return res layout xml id
     */
    protected abstract int getLayoutId();

    /**
     * 返回菜单布局的id
     *
     * @return
     */
    protected abstract int getMenuId();

    public abstract void whenOptionsItemSelected(MenuItem item);

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
