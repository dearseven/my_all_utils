package cc.m2u.lottery.activity.test

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import cc.m2u.lottery.R
import cc.m2u.lottery.activity.base.BaseCompatActivity
import com.sun.jna.platform.win32.WinUser
import kotlinx.android.synthetic.main.activity_launch.*;

/**
 * 主页呗
 * Created by wx on 2017/6/1.
 */
class Main2Activity : cc.m2u.lottery.activity.base.BaseCompatActivity() {
    override fun getLayoutId(): Int {
        return cc.m2u.lottery.R.layout.activity_main
    }

    override fun getMenuId(): Int {
        //return 0
        return cc.m2u.lottery.R.menu.main
    }

    override fun whenOptionsItemSelected(item: android.view.MenuItem?) {
        if (item!!.itemId == cc.m2u.lottery.R.id.add_new_todo) {
            //startAddNewActivty();
            android.widget.Toast.makeText(this, "新建", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        //
        toolbarTitle.text = "MainActivityKt2!"
    }


}