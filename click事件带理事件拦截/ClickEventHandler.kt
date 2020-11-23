
import android.Manifest
import android.util.Log
import android.view.View
import android.widget.Toast


/**
 * 把按钮事件绑定在这里可以通过统一设置tag等进行拦截，id进行拦截行为
 */
class ClickEventHandler {
    //通过TAG_XXXX的传递的值 1 就是需要判断拦截
    //打开相册
    val TAG_CAMERA_ALBUM = R.id.TAG_CAMERA_ALBUM

    companion object {
        val instance = Holder.ClickEventHandler
    }

    private constructor()

    private object Holder {
        val ClickEventHandler = ClickEventHandler()
    }


    /**
     * 某些情况下是需要回调才走下一步所以这里把方法（clicked: (View) -> Unit）传过来 ，但是不一定会调用
     * 如果会调用，那么next一定要是false，不然会执行2次啊。
     */
    fun beforeClick(view: View, clicked: (View) -> Unit): Boolean {
        var next = true//设置为false就拦截啊
        var flag = 1//1 不拦截 0拦截 2 进入回调流程
        //可以拦截
        if (view.id == R.id.MainTxt1) {
            Toast.makeText(view.context, "setClickEventHandler--MainTxt1", Toast.LENGTH_SHORT)
                .show()
            //next = true
        } else if (view.id == R.id.MainTxt3) {
            Toast.makeText(view.context, "setClickEventHandler--MainTxt3", Toast.LENGTH_SHORT)
                .show()
            //next = false
        } else {
            //------- 通过TAG_XXXX的传递的值 1 就是需要判断拦截
            //--看看是不是打开相机相册的按钮
            view.getTag(TAG_CAMERA_ALBUM)?.let {
                if (it == 1) {
                    next = false
                    flag = 2
                    val superActivity = view.context as SuperActivity
                    superActivity.askPermission(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ), object : IPermissionCallBack {
                            override fun askPermissionResult(result: Boolean) {
                                if (result)
                                    clicked(view)
                            }
                        }, "需要权限才能访问您的相机和相册"
                    )
                }
            }
            //--
        }
        //
        when (flag) {
            1 -> {
                endClick(view, "不拦截")
            }
            0 -> {
                endClick(view, "拦截")
            }
            2 -> {
                endClick(view, "拦截，进入回调流程")
            }
        }
        return next
    }

    fun endClick(view: View, desc: String) {
        Log.i("ClickEventHandler", "${view} endClick--$desc")
    }
}


/**
MainTxt1.setClickEventHandler { v ->
startFrontServiceAndLocate()
}
MainTxt3.setClickEventHandler {
openOtherMap()
}
//设定需要判定动态权限
MainTxt4.setTag(ClickEventHandler.instance.TAG_CAMERA_ALBUM, 1)
MainTxt4.setClickEventHandler {
Log.i("MainTxt4", "打开相册相机")
}
 */
fun View.setClickEventHandler(clicked: (View) -> Unit) {
    this.setOnClickListener {
        if (ClickEventHandler.instance.beforeClick(it, clicked)) {
            clicked(this)
        }
    }
}