package cc.m2u.denwatyou.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cc.m2u.denwatyou.R;
import cc.m2u.denwatyou.fragments.lazyfragments.ILazy.ILazyLoadFragment;
import cc.m2u.denwatyou.fragments.lazyfragments.ILazyImpl.ContactsLazyFragment;
import cc.m2u.denwatyou.fragments.lazyfragments.ILazyImpl.HistoryLazyFragment;
import cc.m2u.denwatyou.fragments.lazyfragments.ILazyImpl.MsgLazyFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPager vp1;
    private PagerTabStrip pts1;
    private MyViewPagerAdapter vp1Adpter;

    private int TAB_COUNT = 3;
    private String[] titles = {"历史记录", "联系人", "短信"};
    private List<ILazyLoadFragment> fragments = new ArrayList<ILazyLoadFragment>(TAB_COUNT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        initViewPagerAndPagerTabStrip();
    }

    /**
     * 初始化ViewPager和它的导航栏PagerTabStrip（其实也可以用toolbar的tab，但是我觉得pager_table_strip比较好看）
     */
    private void initViewPagerAndPagerTabStrip() {
        vp1 = (ViewPager) findViewById(R.id.ViewPager);
        pts1 = (PagerTabStrip) findViewById(R.id.PagerTab);

        ILazyLoadFragment history = HistoryLazyFragment.newInstance(0, new Bundle(), HistoryLazyFragment.class, R.layout.fragment_history_lazy);
        ILazyLoadFragment contacts = ContactsLazyFragment.newInstance(1, new Bundle(), ContactsLazyFragment.class, R.layout.fragment_contacts_lazy);
        ILazyLoadFragment msg = MsgLazyFragment.newInstance(2, new Bundle(), MsgLazyFragment.class, R.layout.fragment_msg_lazy);

        fragments.add(history);
        fragments.add(contacts);
        fragments.add(msg);

        vp1Adpter = new MyViewPagerAdapter(getSupportFragmentManager());
        vp1.setOffscreenPageLimit(1);
        vp1.setAdapter(vp1Adpter);
        vp1.addOnPageChangeListener(new MyPageChangeListener());
        showSnackbarMsg("初始化完毕!");
    }

    private class MyPageChangeListener extends ViewPager.SimpleOnPageChangeListener{
        @Override
        public void onPageSelected(int position) {
            Log.i("denwatyou",position+"");
        }
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return (fragments == null || fragments.size() < TAB_COUNT) ? null : fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    /**
     * 获取到根布局以后，通过Snackbar来显示消息
     *
     * @param msg
     */
    public void showSnackbarMsg(String msg) {
        View view = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        final Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        snackbar.setAction("知道啦", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                //doSomething
            }
        });
        snackbar.show();
    }

    //-------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (item.getItemId() == android.R.id.home)//setDisplayHomeAsUpEnabled的按钮
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
