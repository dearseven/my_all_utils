package wang.cyan.mvvm.activities;
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import wang.cyan.mvvm.presenters.IPresenter

abstract class BaseParentActivity<V : ViewDataBinding, P : IPresenter> : AppCompatActivity {
    var vdBing: V? = null
    var presenter: P? = null

    constructor() {
       // presenter!!.getTheActivity<MainActivity>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vdBing = DataBindingUtil.setContentView(this, get_LayoutId())
        //接下来还可以做一些处理，比如设置菜单，初始化一些数据等等
        presenter = get_Presenter();
        //接下来回调回去到presenter层
        presenter!!.onCreateDone()
    }

    abstract fun get_Presenter(): P?

    abstract fun get_LayoutId(): Int;

}
