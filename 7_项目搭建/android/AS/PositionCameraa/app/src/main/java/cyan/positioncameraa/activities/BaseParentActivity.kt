package cyan.positioncameraa.activities
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import cyan.positioncameraa.R
import cyan.positioncameraa.presenters.IPresenter

abstract class BaseParentActivity<V : ViewDataBinding, P : IPresenter> : AppCompatActivity {
    var vdBing: V? = null
    var presenter: P? = null

    private val TAG = BaseParentActivity::class.java.simpleName
    private var mToolbarTitle: TextView? = null
    private var mToolbarSubTitle: TextView? = null
    private var mToolbar: Toolbar? = null

    constructor() {
       // presenter!!.getTheActivity<MainActivity>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vdBing = DataBindingUtil.setContentView(this, get_LayoutId())
        //接下来还可以做一些处理，比如设置菜单，初始化一些数据等等
        presenter = get_Presenter();
        //
        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        /*
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Title");
        toolbar.setSubtitle("Sub Title");
        */
        mToolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        mToolbarSubTitle = findViewById<TextView>(R.id.toolbar_subtitle)

        //Log.d("intelliCyan",mToolbarTitle+" "+mToolbarSubTitle);
        if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar)
        }
        if (mToolbarTitle != null) {
            //getTitle()的值是activity的android:lable属性值
            mToolbarTitle!!.setText(title)
            //设置默认的标题不显示
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        //
        //接下来回调回去到presenter层
        presenter!!.onCreateDone()
    }

    abstract fun get_Presenter(): P?

    abstract fun get_LayoutId(): Int;


    override fun onStart() {
        super.onStart()
        /**
         * 判断是否有Toolbar,并默认显示返回按钮
         */
        if (null != getToolbar() && isShowBacking()) {
            showBack()
        }
    }

    /**
     * 获取头部标题的TextView
     *
     * @return
     */
    fun getToolbarTitle(): TextView? {
        return mToolbarTitle
    }

    /**
     * 获取头部标题的TextView
     *
     * @return
     */
    fun getSubTitle(): TextView? {
        return mToolbarSubTitle
    }

    /**
     * 设置头部标题
     *
     * @param title
     */
    fun setToolBarTitle(title: CharSequence) {
        if (mToolbarTitle != null) {
            mToolbarTitle!!.setText(title)
        } else {
            getToolbar()!!.title = title
            setSupportActionBar(getToolbar())
        }
    }

    /**
     * this Activity of tool bar.
     * 获取头部.
     *
     * @return support.v7.widget.Toolbar.
     */
    fun getToolbar(): Toolbar? {
        return findViewById<View>(R.id.toolbar) as Toolbar
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    private fun showBack() {
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
        getToolbar()!!.setNavigationIcon(android.R.drawable.ic_menu_revert)
        getToolbar()!!.setNavigationOnClickListener { onBackPressed() }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (getMenuId() != 0) {
            menuInflater.inflate(getMenuId(), menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        whenOptionsItemSelected(item)
        return true
    }

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     *
     * @return
     */
    protected fun isShowBacking(): Boolean {
        return true
    }

    /**
     * 返回菜单布局的id
     *
     * @return
     */
    protected abstract fun getMenuId(): Int

    abstract fun whenOptionsItemSelected(item: MenuItem)

    override fun onDestroy() {
        super.onDestroy()


    }

}
