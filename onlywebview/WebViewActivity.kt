
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * Created by Administrator on 2017/6/2.
 */
class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view);

        var lp=mainWebView.layoutParams as LinearLayout.LayoutParams
        lp.width=resources.displayMetrics.widthPixels
        lp.height=resources.displayMetrics.widthPixels
        mainWebView.layoutParams=lp

        handler.postDelayed({
            val webSettings: WebSettings = mainWebView.getSettings()
            webSettings.setJavaScriptEnabled(true)
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true)
            webSettings.setSupportMultipleWindows(true)
            mainWebView.setWebViewClient(WebViewClient())
            mainWebView.setWebChromeClient(WebChromeClient())
            mainWebView.loadUrl("http://192.168.1.12:8080/xx/test_2.action");
        },1000)
    }


    companion object{
        private var handler = object: Handler() {

            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
            }
        }

    }
}