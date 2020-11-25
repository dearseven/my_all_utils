
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.SparseArray
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton

class OpenOtherMapUtil {


    val MAP_BD = "com.baidu.BaiduMap"
    val MAP_GD = "com.autonavi.minimap"
    val MAP_TX = "com.tencent.map"

    val KEY_BD = 0
    val KEY_GD = 1
    val KEY_TX = 2


    //显示一个选择地图的弹窗
    fun showMapSelector(
        context: SuperActivity,
        anchor: View,
        severalUtils: SeveralUtils,
        gps: Gps,
        ikisaki: String
    ) {
        //这里使用了superativity封装的方法来显示一个下面弹出的菜单
        context.bottomMenu(R.layout.layout_show_maps, anchor, SparseArray<Boolean>().also {
            val mapbd = severalUtils.isAppInstalled(context, MAP_BD);
            val mapgd = severalUtils.isAppInstalled(context, MAP_GD);
            val maptx = severalUtils.isAppInstalled(context, MAP_TX);

            it.put(KEY_BD, mapbd)
            it.put(KEY_GD, mapgd)
            it.put(KEY_TX, maptx)

        }, object : IPopMenuShownCallBack {
            override fun onPopMenuShownCallBack(
                view: View,
                callBackParam: Any?,
                popupWindow: PopupWindow
            ) {
                //根据severalUtils.isAppInstalled的判定来隐藏从xxx地图打开的按钮
                val sparseArray: SparseArray<Boolean> = callBackParam!! as SparseArray<Boolean>
                val areaBd = view.findViewById<RelativeLayout>(R.id.show_maps_area_baidu)
                val areaGd = view.findViewById<RelativeLayout>(R.id.show_maps_area_gaode)
                val areaTx = view.findViewById<RelativeLayout>(R.id.show_maps_area_tengxun)
                if (!sparseArray[KEY_BD]) {
                    areaBd.visibility = View.GONE
                }
                if (!sparseArray[KEY_GD]) {
                    areaGd.visibility = View.GONE
                }
                if (!sparseArray[KEY_TX]) {
                    areaTx.visibility = View.GONE
                }
                //
                val bt1 = view.findViewById<AppCompatButton>(R.id.file_item_more_pop_btn1)
                val bt2 = view.findViewById<AppCompatButton>(R.id.file_item_more_pop_btn2)
                val bt3 = view.findViewById<AppCompatButton>(R.id.file_item_more_pop_btn3)
                //打开地图
                bt1.setOnClickListener { openMapForDriving(MAP_BD, gps, context,ikisaki) }
                bt2.setOnClickListener { openMapForDriving(MAP_GD, gps, context,ikisaki) }
                bt3.setOnClickListener { openMapForDriving(MAP_TX, gps, context,ikisaki) }
            }
        })
    }

    /**调起第三方地图APP导航*/
    private fun openMapForDriving(
        packageName: String,
        toLatLng: Gps,
        context: Context,
        ikisaki: String?
    ) {

        val toWhere = ikisaki ?: "目的地"
        var showToastTxt: String = ""
        try {
            when (packageName) {
                MAP_BD -> {
//                    showToastTxt = "手机未安装百度地图APP"
                    val intent = Intent()
                    //导航界面
                    val gps = othersToBaidu(toLatLng)
                    intent.setData(Uri.parse("baidumap://map/direction?destination=latlng:${gps.wgLat},${gps.wgLon}|name:${toWhere}&coord_type=bd09ll&mode=driving"))
                    //由于没获取到目的地地址，所以跳到目的地界面
                    //intent.setData(Uri.parse("baidumap://map/geocoder?location=${item?.la},${item?.lg}&src=andr.baidu.openAPIdemo"))
                    context?.startActivity(intent)

                }
                MAP_GD -> {
//                    showToastTxt = "手机未安装高德地图APP"
                    val intent = Intent()
                    intent.setPackage("com.autonavi.minimap")
                    intent.setAction(Intent.ACTION_VIEW)
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
//                    val destination = convertBaiduToGPS(toLatLng);//转换坐标系
                    intent.setData(
                        Uri.parse(
                            "androidamap://route?sourceApplication=OpenOtherMapUtil&" +
                                    "dlat=" + toLatLng.wgLat + "&dlon=" + toLatLng.wgLon + "&dname=${toWhere}" + "&dev=0&t=0"
                        )
                    )
                    context?.startActivity(intent)
                }
                MAP_TX -> {
//                    showToastTxt = "手机未安装腾讯地图APP"
                    val intent = Intent()
                    intent.setData(Uri.parse("qqmap://map/routeplan?type=walk&to=${toWhere}&tocoord=${toLatLng.wgLat},${toLatLng.wgLon}&policy=1&referer=myapp"))
                    context?.startActivity(intent)
                }
            }
        } catch (ex: ActivityNotFoundException) {
            // Global.showToast(showToastTxt)
        }
    }

//    /**百度坐标系 (BD-09) 转 火星坐标系(GCJ-02)的转换*/
//    fun convertBaiduToGPS(latlng: Gps) = PositionUtil().(CoordinateConverter.CoordType.BD09LL).coord(latlng).convert()

    fun othersToBaidu(gps: Gps) = PositionUtil.gcj02_To_Bd09(gps.wgLat, gps.wgLon)
}