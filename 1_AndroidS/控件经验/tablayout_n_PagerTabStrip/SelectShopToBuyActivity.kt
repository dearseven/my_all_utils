
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.Toast

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import kotlinx.android.synthetic.main.activity_select_shop_to_buy.*
import kotlinx.android.synthetic.main.base_toolbar.*

class SelectShopToBuyActivity : BaseCompatActivity() {
    private var one: TabLayout.Tab? = null;
    private var two: TabLayout.Tab? = null;

    /**
     * 是否让用户去打开GPS
     */
    val isToOpenGpsSetting = false
    private var locationClient: AMapLocationClient? = null
    private var locationOption: AMapLocationClientOption? = null


    /**
     *fraglist
     */
    val fragList by lazy {
        ArrayList<Fragment>()
    }

    var adapter: VPAdapter? = null

    companion object {
        val REQUEST_OPEN_GPS = 1
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_select_shop_to_buy
    }

    override fun getMenuId(): Int {
        return 0
    }

    override fun whenOptionsItemSelected(item: MenuItem?) {
    }

    override fun isShowBacking(): Boolean {
        return super.isShowBacking()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar_subtitle.text = ""
        tryOpenGPS()

        initViews()
    }

    /**
     * 对控件做一些初始化
     */
    private fun initViews() {
        fragList.add(SwitchBettingShopFragment.newInstance(mapOf(SwitchBettingShopFragment.PAR_INITTYPE to SwitchBettingShopFragment.INIT_TYPE.DISTANCE)))
        fragList.add(SwitchBettingShopFragment.newInstance(mapOf(SwitchBettingShopFragment.PAR_INITTYPE to SwitchBettingShopFragment.INIT_TYPE.NONE)))

        adapter = VPAdapter(supportFragmentManager)
        asstb_viewPager.adapter = adapter!!
        adapter!!.notifyDataSetChanged()
        asstb_viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                //
            }
        })

        asstb_tabLayout.setupWithViewPager(asstb_viewPager);
        one = asstb_tabLayout.getTabAt(0)
        one!!.text = "选项卡11"
        two = asstb_tabLayout.getTabAt(1)
        two!!.text = "选项卡22"
        //添加一个间隔线
        var linearLayout = asstb_tabLayout.getChildAt(0) as LinearLayout
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
                R.drawable.tablayout_devider));
        linearLayout.setDividerPadding(SizeUtil.dip2px(this, 10.toFloat())); //设置分割线间隔
        asstb_tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == asstb_tabLayout.getTabAt(0)) {
                    asstb_viewPager.setCurrentItem(0);
                } else if (tab == asstb_tabLayout.getTabAt(1)) {
                    asstb_viewPager.setCurrentItem(1);
                }
            }
        })

    }

    inner class VPAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment = fragList[position]

        override fun getCount(): Int = fragList.size

        // override fun getPageTitle(position: Int): CharSequence = fragList[position]

    }

    override fun onDestroy() {
        destroyLocation()
        super.onDestroy()
    }


    private var h = object : Handler() {
        override fun handleMessage(msg: Message?) {
        }
    }

    @SuppressLint("MissingPermission")
    fun tryOpenGPS() {
        var lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //检查gps等状态
        val gps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!gps && isToOpenGpsSetting) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            Toast.makeText(this@SelectShopToBuyActivity, "请打开GPS", Toast.LENGTH_SHORT).show()
            startActivityForResult(intent, REQUEST_OPEN_GPS) // 设置完成后返回到原来的界面
        } else {
            initLocation()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_OPEN_GPS -> {
                var lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                //检查gps等状态
                val gps: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                Toast.makeText(this@SelectShopToBuyActivity, (if (gps) "已打开" else "未打开"), Toast.LENGTH_SHORT).show();
                initLocation()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //-----start 初始化定位(设置参数，定位回调实现等)----
    /**
     * 初始化定位
     */
    private fun initLocation() {
        //初始化client
        locationClient = AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient!!.setLocationOption(locationOption);
        // 设置定位监听
        locationClient!!.setLocationListener(locationListener);

        startLocation()
    }

    /**
     * 定位监听
     */
    var locationListener = object : AMapLocationListener {

        override fun onLocationChanged(location: AMapLocation) {
            var fd = Flood.getInstance();
            fd.run() {
                DLog.log(javaClass, "just test run at Thread ,need not use thread at here")
                var sb = StringBuffer();
                if (null != location) {
                    //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                    if (location.getErrorCode() == 0) {
                        sb.append("定位成功" + "\n");
                        sb.append("定位类型: " + location.getLocationType() + "\n");
                        sb.append("经    度    : " + location.getLongitude() + "\n");
                        sb.append("纬    度    : " + location.getLatitude() + "\n");
                        sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                        sb.append("提供者    : " + location.getProvider() + "\n");
                        sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                        sb.append("角    度    : " + location.getBearing() + "\n");
                        // 获取当前提供定位服务的卫星个数
                        sb.append("星    数    : " + location.getSatellites() + "\n");
                        sb.append("国    家    : " + location.getCountry() + "\n");
                        sb.append("省            : " + location.getProvince() + "\n");
                        sb.append("市            : " + location.getCity() + "\n");
                        sb.append("城市编码 : " + location.getCityCode() + "\n");
                        sb.append("区            : " + location.getDistrict() + "\n");
                        sb.append("区域 码   : " + location.getAdCode() + "\n");
                        sb.append("地    址    : " + location.getAddress() + "\n");
                        sb.append("兴趣点    : " + location.getPoiName() + "\n");
                        //定位完成的时间
                        sb.append("定位时间: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
                    } else {
                        //定位失败
                        sb.append("定位失败" + "\n");
                        sb.append("错误码:" + location.getErrorCode() + "\n");
                        sb.append("错误信息:" + location.getErrorInfo() + "\n");
                        sb.append("错误描述:" + location.getLocationDetail() + "\n");
                    }
                } else {
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                //定位之后的回调时间
                sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
                sb.toString()
            }.runAtUI(h, fd._get()) {
                x ->
                var str = x.toString()
                DLog.log(javaClass, str)
                str
            }
        }
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    fun getDefaultOption(): AMapLocationClientOption {
        var mOption = AMapLocationClientOption();
        mOption.setLocationMode(com.amap.api.location.AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }
//-----end 初始化定位----

//----start 下面是配置用于使用的开始，停止和销毁定位 ----
    /**
     * 开始定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    fun startLocation() {
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient!!.setLocationOption(locationOption);
        // 启动定位
        locationClient!!.startLocation();
    }

    /**
     * 停止定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    fun stopLocation() {
        // 停止定位
        locationClient!!.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    fun destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient!!.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    // 根据控件的选择，重新设置定位参数
    fun resetOption() {
        // 设置是否需要显示地址信息
        locationOption!!.setNeedAddress(true)
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption!!.setGpsFirst(true)
        // 设置是否开启缓存
        locationOption!!.setLocationCacheEnable(true)
        // 设置是否单次定位
        locationOption!!.setOnceLocation(false)
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption!!.setOnceLocationLatest(false)
        //设置是否使用传感器
        locationOption!!.setSensorEnable(true)
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
        locationOption!!.setInterval(10000)
        // 设置网络请求超时时间
        locationOption!!.setHttpTimeOut(5000)
    }
//----end 下面是配置用于使用的开始，停止和销毁定位 ----

}
