
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.text.Layout
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TableLayout
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_my_orders.*
import kotlinx.android.synthetic.main.base_toolbar_for_index.*
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager


/**
 * 订单管理
 */
class MyOrdersActivity : BaseCompatActivity() {
    override fun getLayoutId(): Int = R.layout.activity_my_orders

    override fun getMenuId(): Int = 0

    override fun whenOptionsItemSelected(item: MenuItem?) {
    }

    private val fragments by lazy {
        ArrayList<ILazyLoadFragment>()
    }
    private var inflate: LayoutInflater? = null
    private var vpAdapter: MyViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar_subtitle.text = ""
        //
        inflate = LayoutInflater.from(this@MyOrdersActivity)
        //
        initViewPager()
        initTabLayout()
    }

    private fun initViewPager() {
        val f1 = SampleLazyFragmentImpl.newInstance(0, Bundle(), SampleLazyFragmentImpl::class.java, R.layout.fragment_sample_lazy_fragment_impl)
        val f2 = SampleLazyFragmentImpl.newInstance(1, Bundle(), SampleLazyFragmentImpl::class.java, R.layout.fragment_sample_lazy_fragment_impl)
        val f3 = SampleLazyFragmentImpl.newInstance(2, Bundle(), SampleLazyFragmentImpl::class.java, R.layout.fragment_sample_lazy_fragment_impl)
        val f4 = SampleLazyFragmentImpl.newInstance(3, Bundle(), SampleLazyFragmentImpl::class.java, R.layout.fragment_sample_lazy_fragment_impl)
        fragments.add(f1)
        fragments.add(f2)
        fragments.add(f3)
        fragments.add(f4)
        vpAdapter = MyViewPagerAdapter(supportFragmentManager)
        aOrderVP.setOffscreenPageLimit(1);
        aOrderVP.adapter = vpAdapter
        aOrderVP.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                aOrderTabLayout.getTabAt(position)!!.select()
            }
        })
    }

    private inner class MyViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            return if (fragments == null || fragments.size < resources.getStringArray(R.array.order_manage_tabs).size) null else fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

    }

    /**
     * 初始化tablayout
     */
    @SuppressLint("ResourceType")
    private fun initTabLayout() {
        //设置不滚动
        aOrderTabLayout.tabMode = TabLayout.MODE_FIXED
        //tab显示方式
        aOrderTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        /* ####!!!!!!!!!!!!##### 因为我用了setCustomView，所以就不定义这一段了
        //tab的字体选择器,默认黑色,选择时红色
        aOrderTabLayout.setTabTextColors(Color.BLUE, Color.WHITE);
        //tab的下划线颜色,默认是粉红色
        aOrderTabLayout.setSelectedTabIndicatorColor(Color.BLUE);
        */
        //隐藏那方的那个游标
        aOrderTabLayout.setSelectedTabIndicatorHeight(0)
        //初始化tabitem
        var first = true
        resources.getStringArray(R.array.order_manage_tabs).forEach {
            val tab = aOrderTabLayout.newTab()
            //要用这个来inflate，不然customview的高度会不对哦
            val view = inflate!!.inflate(R.layout.my_order_tab_item, aOrderTabLayout, false)
            val tv = view.findViewById<TextView>(R.id.myOrderTabItemTv)
            tv.text = it
            //####!!!!!!!!!!!!#####这个变色也是因为我用了customview只能自己处理
            if (first) {
                first = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tv.setBackgroundColor(getColor(R.color.tab_blue))
                } else {
                    tv.setBackgroundColor(resources.getColor(R.color.tab_blue)
                    )
                }
                tv.setTextColor(Color.WHITE)
            } else {
                tv.setBackgroundColor(Color.WHITE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tv.setTextColor(getColor(R.color.tab_blue))
                } else {
                    tv.setTextColor(resources.getColor(R.color.tab_blue)
                    )
                }
            }
            view.setTag(R.id.view_content_id, it)
            tab.setCustomView(view)
            aOrderTabLayout.addTab(tab)
        }
        //设置监听器
        aOrderTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                //####!!!!!!!!!!!!#####这个变色也是因为我用了customview只能自己处理
                (0..aOrderTabLayout.tabCount - 1).forEach {
                    val t = aOrderTabLayout.getTabAt(it)
                    val view = t!!.customView
                    val tv = view!!.findViewById<TextView>(R.id.myOrderTabItemTv)
                    //DLog.log(MyOrdersActivity::class.java, "${view.getTag(R.id.view_content_id)}=?=${tab!!.customView!!.getTag(R.id.view_content_id)}")
                    if (tab!!.customView!!.getTag(R.id.view_content_id).equals(view.getTag(R.id.view_content_id))) {
                        aOrderVP.setCurrentItem(it)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            tv.setBackgroundColor(getColor(R.color.tab_blue))
                        } else {
                            tv.setBackgroundColor(resources.getColor(R.color.tab_blue)
                            )
                        }
                        tv.setTextColor(Color.WHITE)
                    } else {
                        tv.setBackgroundColor(Color.WHITE)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            tv.setTextColor(getColor(R.color.tab_blue))
                        } else {
                            tv.setTextColor(resources.getColor(R.color.tab_blue)
                            )
                        }
                    }
                }
            }
        })
        //
    }

}
