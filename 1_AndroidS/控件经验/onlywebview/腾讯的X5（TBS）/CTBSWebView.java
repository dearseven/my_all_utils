
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.RequiresApi;

import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;



/**
 * 记得调用setCWebViewEventHandler`~~~
 */
public class CTBSWebView extends com.tencent.smtt.sdk.WebView {
    private String currentUrl = null;
    private boolean loadError = false;
    /**
     * 用于表示当前是谁
     */
    private String tag;
    /**
     * 事件接口的实现
     */
    private CWebViewEventHandler cWebViewEventHandler = null;

    public void setCWebViewEventHandler(CWebViewEventHandler cWebViewEventHandler) {
        this.cWebViewEventHandler = cWebViewEventHandler;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void loadUrl(String url) {
        DLog.log(getClass(), "url:" + url);
        currentUrl = url;
        loadError = false;
        super.loadUrl(url);
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public interface CWebViewEventHandler {
        void onPageFinshed(CTBSWebView cWebView, boolean loadError);

        void onStartLoad(CTBSWebView cWebView, String url);

        void loadProgress(CTBSWebView view, int newProgress);
    }

    public CTBSWebView(Context context) {
        super(context);
        init(context);
    }

    public CTBSWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CTBSWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //Toast.makeText(context,getX5WebViewExtension()+"",Toast.LENGTH_SHORT).show();
        setBackgroundColor(0);

        //竖直快速滑块，设置null可去除
        getX5WebViewExtension().setVerticalTrackDrawable (null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        clearCache(true);//就可清除webview缓存。
        WebSettings webSetting = getSettings();
//        webSetting.setJavaScriptEnabled(true);
//        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSetting.setSupportMultipleWindows(true);
//        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);


        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //getWindow().setFormat(PixelFormat.TRANSLUCENT);

        getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                setEnabled(false);// 当加载网页的时候将网页进行隐藏
                super.onPageStarted(view, url, favicon);
                cWebViewEventHandler.onStartLoad(CTBSWebView.this, url);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // 断网或者网络连接超时
                if (error.getErrorCode() == ERROR_HOST_LOOKUP || error.getErrorCode() == ERROR_CONNECT || error.getErrorCode() == ERROR_TIMEOUT) {
                    view.loadUrl("about:blank 4"); // 避免出现默认的错误界面
//                    view.loadUrl(mErrorUrl);
                    loadError = true;
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 断网或者网络连接超时
                if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                    view.loadUrl("about:blank 3"); // 避免出现默认的错误界面
                    loadError = true;
                }
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
//                // 这个方法在6.0才出现
//                int statusCode = errorResponse.getStatusCode();
//                System.out.println("onReceivedHttpError code = " + statusCode);
//                if (404 == statusCode || 500 == statusCode) {
//                    view.loadUrl("about:blank 2");// 避免出现默认的错误界面
//                    //view.loadUrl(mErrorUrl);
//                    loadError = true;
//                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (NetWorkTester.getNetState(getContext()) == NetWorkTester.NO_INTERNET) {
                    loadError = true;
                }
                if (!loadError) {//当网页加载成功的时候判断是否加载成功
                    setEnabled(true);
                    cWebViewEventHandler.onPageFinshed(CTBSWebView.this, false);
                } else { //加载失败的话，初始化页面加载失败的图，然后替换正在加载的视图页面
                    cWebViewEventHandler.onPageFinshed(CTBSWebView.this, true);
                }
            }
        });

        setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                cWebViewEventHandler.loadProgress(CTBSWebView.this, newProgress);
            }

            /**
             * 当WebView加载之后，返回 HTML 页面的标题 Title
             * @param view
             * @param title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                //DLog.log(getClass(),getTag()+",onReceivedTitle ~"+title);
                //判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
//                if (!TextUtils.isEmpty(title) && title.toLowerCase().contains("error")) {
//                    loadError = true;
//                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    if (title.contains("404") || title.contains("500") || title.contains("Error")) {
                        view.loadUrl("about:blank 1");// 避免出现默认的错误界面
                        loadError = true;
                    }
                }
            }
        });
    }

}
