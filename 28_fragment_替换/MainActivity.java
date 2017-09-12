package cyan.intellicyan.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import cyan.intellicyan.R;
import cyan.intellicyan.activities.base.BaseCompatActivity;
import cyan.intellicyan.fragments.CalendarFragment;
import cyan.intellicyan.fragments.MyFavListFragment;
import cyan.intellicyan.fragments.RecommedFragment;
import cyan.intellicyan.fragments.father.SuperFragment;

public class MainActivity extends BaseCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FrameLayout mainContent = null;
    private String currentFragmentTag;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getToolbarTitle().setText("Calendar");
                    changeFragment(CalendarFragment.class,CalendarFragment.TAG);
                    return true;
                case R.id.navigation_dashboard:
                    getToolbarTitle().setText("Schedule");
                    changeFragment(RecommedFragment.class,RecommedFragment.TAG);
                    return true;
                case R.id.navigation_notifications:
                    getToolbarTitle().setText("Notification");
                    changeFragment(MyFavListFragment.class,MyFavListFragment.TAG);

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //
        mainContent = (FrameLayout) findViewById(R.id.main_content);

        //初始化的时候，加载日历的fragment
        currentFragmentTag = CalendarFragment.TAG;
        changeFragment(CalendarFragment.class,CalendarFragment.TAG);
        getToolbarTitle().setText("Calendar");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getMenuId() {
        return R.menu.todo_list_menu;
    }

    @Override
    public void whenOptionsItemSelected(MenuItem item) {

    }

    /**
     * 加载fragment
     * @param claz
     * @param toFragment
     */
    private void changeFragment(Class claz, String toFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment from = fragmentManager.findFragmentByTag(currentFragmentTag);
        if (from != null) {//如果找到了被替换的fragment，证明已经初始化了（其实后面的证明不重要）
            Fragment to = fragmentManager.findFragmentByTag(toFragment);
            if (to == null) {//如果找不到要显示的fragment，则初始化一个，
                Object obj = null;
                try {
                    obj = claz.newInstance();
                    to = (Fragment) obj;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                transaction.add(R.id.main_content,to, toFragment);//加载到transaction
            }
            transaction.hide(from).show(to).commit();//切换
        } else {//要被替换的fragment都找不到 就是第一次初始化啦，所以就直接初始化一个加进去
            Object obj = null;
            Fragment to = null;
            try {
                obj = claz.newInstance();
                to = (Fragment) obj;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            transaction.add(R.id.main_content,to, toFragment).show(to).commit();
        }
        currentFragmentTag=toFragment;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
