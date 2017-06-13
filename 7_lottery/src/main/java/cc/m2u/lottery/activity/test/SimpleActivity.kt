package cc.m2u.lottery.activity.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message

import cc.m2u.lottery.R

/**
 * Created by Administrator on 2017/6/6.
 */

class SimpleActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        h.sendEmptyMessageDelayed(MSG1, 1000)
    }

    private val h = object : Handler() {
        override fun handleMessage(msg: Message) {
            // super.handleMessage(msg);
            when (msg.what) {
                MSG1 -> {
                    val intent = Intent(this@SimpleActivity, Main2Activity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    companion object {

        private val MSG1 = 1
    }
}
