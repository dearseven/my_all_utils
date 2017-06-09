package com.teetaa.testwh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.teetaa.Debug;
import com.teetaa.testwh.activity.GridViewTestActivity;
import com.teetaa.testwh.activity.fragment.Fragment2;
import com.teetaa.testwh.activity.fragment.Fragment_zhuanti;
import com.teetaa.testwh.eventbus_param.ChangeFragmentShowHide;
import com.teetaa.testwh.eventbus_param.Down;
import com.teetaa.testwh.eventbus_param.MainViewPageItenSelected;
import com.teetaa.testwh.eventbus_param.Up;
import com.teetaa.testwh.focus_anim.FocusFollowImageView;
import com.teetaa.testwh.focus_anim.FocusView;
import com.teetaa.testwh.vertical_cursor_imageview.VerticalCursorView;
import com.teetaa.testwh.verticalviewpager.VerticalViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private VerticalViewPager viewPager = null;
    private List<Fragment> fragmentList = null;
    private VViewPagerAdatper adatper = null;
    private VerticalCursorView vcv = null;

    public FocusView focusLayer = null;
    /**
     * activity的根视图
     */
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (VerticalViewPager) findViewById(R.id.v_vp);
        fragmentList = new ArrayList<Fragment>(4);
        fragmentList.add(new Fragment_zhuanti());
        fragmentList.add(new Fragment2());
        fragmentList.add(new Fragment_zhuanti());//Fragment_zhuanti());
        fragmentList.add(new Fragment2());//Fragment_fenlei());
        fragmentList.add(new Fragment_zhuanti());
        adatper = new VViewPagerAdatper(getSupportFragmentManager());
        viewPager.setAdapter(adatper);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(this);

        focusLayer = (FocusView) findViewById(R.id.focus_layer);

        vcv = (VerticalCursorView) findViewById(R.id.v_c_v);
        vcv.setCurIndex(1);//初始化的时候必须放到第一个，然后在跳到需要的位置
        vcv.setTotalPage(5);
        vcv.reDrawCursor();
        vcv.setCurIndex(2);
        vcv.reDrawCursor();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        /*ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        StringBuffer sb = new StringBuffer();
        sb.append("总内存大小:").append(mi.totalMem / 1024 / 1024);
        sb.append("\n");
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        sb.append("cpu info:0=").append(cpuInfo[0]).append(",1=").append(cpuInfo[1]);

        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
        Debug.i(Main2Activity.class, sb.toString());

        //DBClient.getInstance(this).insert("test_table", new String[]{"a", "b", "c"}, new String[]{"1", "2", "3"});
        List<Map<String,String>>lm=DBClient.getInstance(this).retrieve("test_table","select * from test_table",new String[]{},null);
        Debug.i(Main2Activity.class,lm.toString());
        */
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        MainViewPageItenSelected param = new MainViewPageItenSelected();
        param.index = position;
        param.focusLayer = focusLayer;
        EventBus.getDefault().post(param);

        vcv.setCurIndex(position + 1);
        vcv.reDrawCursor();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class VViewPagerAdatper extends FragmentPagerAdapter {

        public VViewPagerAdatper(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeFragmentShowHide param) {
        viewPager.setCurrentItem(param.id - 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Up param) {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Down up) {
        if (viewPager.getCurrentItem() != fragmentList.size() - 1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        rootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);

        MainViewPageItenSelected param = new MainViewPageItenSelected();
        param.index = 1;
        param.needAnim = false;
        param.focusLayer = focusLayer;
        EventBus.getDefault().post(param);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            //Debug.i(MainActivity.class, event.getAction() + "," + event.getKeyCode());

            View v = rootView.findFocus();
            Debug.i(Main2Activity.class, v.toString());
            if (v instanceof FocusFollowImageView) {//这里是拦截FocusFollowImageView就行了，当然以后代码越多判断的类型越多
                if (v.getTag() != null) {
                    String tag = (String) v.getTag();
                    if (tag.equals("left_to_item_title") && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT /*|| event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP*/)) {
                        //最左上图
                        MainViewPageItenSelected param = new MainViewPageItenSelected();
                        param.index = viewPager.getCurrentItem();
                        param.needAnim = true;
                        param.focusLayer = focusLayer;
                        param.forcedHasFocus = true;
                        EventBus.getDefault().post(param);//这个事件在Fragment1处理
                        return true;
                    } else if (tag.equals("left_to_item_title1") && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                        //最左下图
                        MainViewPageItenSelected param = new MainViewPageItenSelected();
                        param.index = viewPager.getCurrentItem();
                        param.needAnim = true;
                        param.focusLayer = focusLayer;
                        param.forcedHasFocus = true;
                        EventBus.getDefault().post(param);//这个事件在Fragment1处理
                        return true;
                    } else if (tag.equals("end_of_hscroll") && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        //最右侧图
                        return true;//
                    } else {//有其他的FocusFollowImageView，就直接不处理
                        return super.dispatchKeyEvent(event);
                    }
                }
            } else {
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
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER://确定
                        Debug.i(MainActivity.class, "－－－－－确定－－－－－");
                        if (v.getId() == R.id.item_ico_seach) {
                            Intent i=new Intent(Main2Activity.this, GridViewTestActivity.class);
                            startActivity(i);
                            //finish();
                        }
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
            }
            return super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }
}
