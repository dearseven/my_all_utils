package cc.m2u.lottery.activity.test

/**
 * 主页呗
 * Created by wx on 2017/6/1.
 */
class MainActivity : cc.m2u.lottery.activity.base.BaseCompatActivity() {
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
        h.sendEmptyMessageDelayed(cc.m2u.lottery.activity.test.MainActivity.Companion.MSG_GO, 1000);
        toolbarTitle.text = "MainActivityKt!"
    }

    companion object {
        private val MSG_GO: Int = 1;
    }

    private val h: android.os.Handler = object : android.os.Handler() {
        override fun handleMessage(msg: android.os.Message?) {
            when (msg!!.what) {
                cc.m2u.lottery.activity.test.MainActivity.Companion.MSG_GO -> { //startToMain2();
                    var intent: android.content.Intent = android.content.Intent(this@MainActivity, Main2Activity::class.java);
                    startActivity(intent);
                }
                else -> {
                }
            }
            super.handleMessage(msg)
        }
    }

}