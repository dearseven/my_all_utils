package cc.m2u.ifengbigdata.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.*;

public class CWebView extends WebView {
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
    public String getTag() {
        return tag;
    }
	
	@Override
    public void loadUrl(String url) {
        currentUrl = url;
        super.loadUrl(url);
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public interface CWebViewEventHandler {
        void onPageFinshed(CWebView cWebView, boolean loadError);

        void onStartLoad(CWebView cWebView, String url);

        void loadProgress(CWebView view, int newProgress);
    }

    public CWebView(Context context) {
        super(context);
        init(context);
    }

    public CWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                setEnabled(false);// 当加载网页的时候将网页进行隐藏
                super.onPageStarted(view, url, favicon);
                cWebViewEventHandler.onStartLoad(CWebView.this, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                loadError = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!loadError) {//当网页加载成功的时候判断是否加载成功
				    setEnabled(true);
                    cWebViewEventHandler.onPageFinshed(CWebView.this, false);
                } else { //加载失败的话，初始化页面加载失败的图，然后替换正在加载的视图页面
                    cWebViewEventHandler.onPageFinshed(CWebView.this, true);
                }
            }
        });

        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                cWebViewEventHandler.loadProgress(CWebView.this, newProgress);
            }
        });
    }

}
