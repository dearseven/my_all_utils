

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.nextcloud.talk.R
import com.nextcloud.talk.activities.BaseActivity

open class SuperActivity : BaseActivity() {
    //=============================底部弹出菜单======================================

    fun bottomMenu(layoutid: Int, parent: View,callBackParam:Any?,popMenuShownCallBack:IPopMenuShownCallBack) {
        var popupWindow: PopupWindow? = null
        if (popupWindow?.isShowing == true) {
            return
        }
        //跟布局记得用LinearLayout
        val linearLayout = layoutInflater.inflate(layoutid, null) as LinearLayout
        popupWindow = PopupWindow(linearLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        //点击空白处隐藏
        popupWindow?.also {
            it.isFocusable = true
            it.setBackgroundDrawable(BitmapDrawable())
            //val location=IntArray(2)
            it.setAnimationStyle(R.style.pop_animation);
            it.showAtLocation(parent, Gravity.LEFT or Gravity.BOTTOM, 0, 0)
            popMenuShownCallBack.onPopMenuShownCallBack(linearLayout,callBackParam,popupWindow)
        }
    }
}


interface IPopMenuShownCallBack {
    open fun onPopMenuShownCallBack(view:View,callBackParam:Any?, popupWindow: PopupWindow)
}