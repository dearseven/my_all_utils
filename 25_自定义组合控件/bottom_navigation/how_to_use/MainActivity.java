
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 */
public class MainActivity extends BaseCompatActivity implements CustomBottomNavigation.BottomNavigationItemClick {
    private CustomBottomNavigation bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNavigation = (CustomBottomNavigation) findViewById(R.id.act_main_cbn);
        List<CustomBottomNavigation.NavItem> items = new ArrayList<CustomBottomNavigation.NavItem>(5);
        items.add(new CustomBottomNavigation.NavItem("tag1", "j哥", R.mipmap.ic_launcher, R.drawable.white_devide,
                Color.parseColor("#222222"), Color.parseColor("#CC5588")));
        items.add(new CustomBottomNavigation.NavItem("tag2", "j哥", R.mipmap.ic_launcher, R.drawable.white_devide,
                Color.parseColor("#222222"), Color.parseColor("#CC5588")));
        items.add(new CustomBottomNavigation.NavItem("tag3", "j哥", R.mipmap.ic_launcher, R.drawable.white_devide,
                Color.parseColor("#222222"), Color.parseColor("#CC5588")));
        items.add(new CustomBottomNavigation.NavItem("tag4", "JJ哥", R.mipmap.ic_launcher, R.drawable.white_devide,
                Color.parseColor("#222222"), Color.parseColor("#CC5588")));
        items.add(new CustomBottomNavigation.NavItem("tag5", "j哥", R.mipmap.ic_launcher, R.drawable.white_devide,
                Color.parseColor("#222222"), Color.parseColor("#CC5588")));
        bottomNavigation.initViews(items,this);
		bottomNavigation.setCurrentItem(items.get(0).tag)
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

    /**
     * 下面的按钮点击事件
     *
     * @param tag
     */
    @Override
    public void onBottomNavigationItemClick(String tag) {
        Toast.makeText(this, tag, Toast.LENGTH_SHORT).show();
		bottomNavigation.setCurrentItem(tag);

    }
}
