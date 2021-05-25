
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import java.lang.ref.WeakReference


/**
 * 其实他不是一个activity
 */
class DialogActivity : Dialog {
    private var layoutId: Int
    private lateinit var weakContext: WeakReference<Context>

//    interface Life {
//        fun whenShown()
//        fun whenHidden()
//    }

    companion object {
        @JvmStatic
        fun createOneWithFull(ctx: Context, layoutId: Int): DialogActivity {
            return DialogActivity(ctx, layoutId, R.style.dialog_activity_full_style)
        }

        @JvmStatic
        fun createOne(ctx: Context, layoutId: Int): DialogActivity {
            return DialogActivity(ctx, layoutId, R.style.dialog_activity_style)
        }
    }

    private constructor(ctx: Context, layoutId: Int, styleId: Int) : super(ctx, styleId) {
        weakContext = WeakReference(ctx)
        this.layoutId = layoutId
        //setContentView(R.layout.dialog_activity)
    }

    fun start(): View {
        val mView = weakContext?.get()?.let { context ->
            val inflater = LayoutInflater.from(context)
            val _view: View = inflater.inflate(layoutId, null)
            addContentView(_view, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            show()
            _view
        }!!
        return mView
    }

    fun startWithFullScreen(window: Window): View {
        val mView = weakContext?.get()?.let { context ->
            setCancelable(false)
//            setContentView(layoutId)

            val inflater = LayoutInflater.from(context)
            val _view: View = inflater.inflate(layoutId, null, true)
            addContentView(_view, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

            show()
            _view
        }!!

        val layoutParams = window.getAttributes()
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        window.decorView.setPadding(0, 0, 0, 0)
        window.attributes = layoutParams

        return mView
    }

    override fun dismiss() {
        super.dismiss()
    }

    override fun show() {
        super.show()
    }

    override fun cancel() {
        super.cancel()
    }


}