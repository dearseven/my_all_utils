
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 主页
 */
public class MainActivity extends BaseCompatActivity implements CustomBottomNavigation.BottomNavigationItemClick, BaseLazyFragment.OnFragmentCreatedListener {
    private CustomBottomNavigation bottomNavigation;
    private BaseLazyFragment blf = new BaseLazyFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blf.setOnFragmentCreatedListener(this);

        bottomNavigation = (CustomBottomNavigation) findViewById(R.id.act_main_cbn);
        List<CustomBottomNavigation.NavItem> items = new ArrayList<CustomBottomNavigation.NavItem>(5);
        //下面这个数据，需要对应上Configs.Companion.main_fragments的数据
        items.add(new CustomBottomNavigation.NavItem("tag1", getString(R.string.yonghu), R.mipmap.ic_launcher, R.drawable.white_devide,
                ValuesGetter.getColor(this, R.color.bottom_nav_text_common_color), ValuesGetter.getColor(this, R.color.bottom_nav_text_press_color)));
        items.add(new CustomBottomNavigation.NavItem("tag2", getString(R.string.dinggou), R.mipmap.ic_launcher, R.drawable.white_devide,
                ValuesGetter.getColor(this, R.color.bottom_nav_text_common_color), ValuesGetter.getColor(this, R.color.bottom_nav_text_press_color)));
        items.add(new CustomBottomNavigation.NavItem("tag3", getString(R.string.shoushi), R.mipmap.ic_launcher, R.drawable.white_devide,
                ValuesGetter.getColor(this, R.color.bottom_nav_text_common_color), ValuesGetter.getColor(this, R.color.bottom_nav_text_press_color)));
        items.add(new CustomBottomNavigation.NavItem("tag4", getString(R.string.huizong), R.mipmap.ic_launcher, R.drawable.white_devide,
                ValuesGetter.getColor(this, R.color.bottom_nav_text_common_color), ValuesGetter.getColor(this, R.color.bottom_nav_text_press_color)));
        items.add(new CustomBottomNavigation.NavItem("tag5", getString(R.string.wode), R.mipmap.ic_launcher, R.drawable.white_devide,
                ValuesGetter.getColor(this, R.color.bottom_nav_text_common_color), ValuesGetter.getColor(this, R.color.bottom_nav_text_press_color)));
        bottomNavigation.initViews(items, this);

//        AutoUpdater autoUpdater = null;
//        if (autoUpdater == null) {
//            autoUpdater = new AutoUpdater.UpdateHelper("更新", "1,***\n2,***  ", "http://app.mi.com/download/2").builder();
//            autoUpdater.startUpdate(this);
//        }

//        //
//        WebView wv1 = (WebView) findViewById(R.id.wv1);
//        WebSettings webSettings = wv1.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setSupportMultipleWindows(true);
//        wv1.setWebViewClient(new WebViewClient());
//        wv1.setWebChromeClient(new WebChromeClient());
//        wv1.loadUrl("http://192.168.3.202:8080/fh/test_1.action");
//        //
//        WebView wv2 = (WebView) findViewById(R.id.wv2);
//        webSettings = wv2.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setSupportMultipleWindows(true);
//        wv2.setWebViewClient(new WebViewClient());
//        wv2.setWebChromeClient(new WebChromeClient());
//        wv2.loadUrl("http://192.168.3.202:8080/fh/test_3.action");

        onBottomNavigationItemClick("tag1");
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
//        if (tag.equals("tag2")) {
//            Intent it = new Intent(this, CalendarTestActivity.class);
//            startActivity(it);
//        }
        Map<String, Object> m = Configs.Companion.getMain_fragments().get(tag);
        BaseLazyFragment to = BaseLazyFragment.changeFragment(blf, this, R.id.am_fl, (Class) m.get("class"), (String) m.get("tag"));
        //getFragmentManager().executePendingTransactions();
        // to.intoFragment(m);
    }

    @Override
    public void OnFragmentCreated(BaseLazyFragment fragment) {
        if (fragment.getTag().equals(blf.getCurrentFragmentTag())) {
            Map<String, Object> m = Configs.Companion.getMain_fragments().get(fragment.getTag());
            fragment.intoFragment(m);
        }
    }
}
