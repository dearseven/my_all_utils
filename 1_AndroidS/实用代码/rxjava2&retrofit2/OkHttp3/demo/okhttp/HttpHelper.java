package wc.c.libbase.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wc.c.libbase.configs.BaseConfig;
import wc.c.libbase.utils.IDoSomeThing;
import wc.c.libbase.utils.MacUtil;
import wc.c.libbase.utils.SimpleHttp;

import static wc.c.libbase.okhttp.HeadItem.WRITE_TO_BOTTH;
import static wc.c.libbase.okhttp.HeadItem.WRITE_TO_COOKIE;
import static wc.c.libbase.okhttp.HeadItem.WRITE_TO_HEAD;

public class HttpHelper {
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void getRequest(String url, final IDoSomeThing<HttpResponse> doOnResponse, final IDoSomeThing<HttpFailure> doOnFailure, List<HeadItem> headIfNeed) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Log.i("HttpHelper", "getRequest url=" + url);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        //
//        okhttp3.Request request = new okhttp3.Request.Builder()
//                .url(url).addHeader("mac", BaseConfig.OtherConfig.MAC_ADD)
//                .get()
//                .build();

        Request.Builder builder = new okhttp3.Request.Builder();
        builder.url(url);
        builder.addHeader("mac", BaseConfig.OtherConfig.MAC_ADD);
        if (headIfNeed != null) {
            StringBuilder ck = null;
            Iterator<HeadItem> itemIterator = headIfNeed.iterator();
            while (itemIterator.hasNext()) {
                HeadItem item = itemIterator.next();
                Log.i("HttpHelper", "head:key=" + item.key + ",value=" + item.value);
                if (item.writeTo == WRITE_TO_HEAD) {
                    builder.addHeader(item.key, item.value);
                } else if (item.writeTo == WRITE_TO_COOKIE) {
                    if (ck == null)
                        ck = new StringBuilder();
                    ck.append(";").append(item.key).append("=").append(item.value);
                } else if (item.writeTo == WRITE_TO_BOTTH) {
                    builder.addHeader(item.key, item.value);
                    if (ck == null)
                        ck = new StringBuilder();
                    ck.append(";").append(item.key).append("=").append(item.value);
                }
            }
            if (ck != null) {
                builder.addHeader("Cookie", ck.toString().substring(1));
                Log.i("HttpHelper", "head Cookie:" + ck.toString().substring(1));

            }
        }
        okhttp3.Request request = builder.get().build();
        //
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("HttpHelper", e.getMessage()+"");
                final HttpFailure failure = HttpFailure.create(call, e);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doOnFailure.doSomeThing(failure);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final HttpResponse response1 = HttpResponse.create(call, response);
                Log.i("HttpHelper", "response called,read data from body.string()");
                Log.i("HttpHelper", response1.getResponseString());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doOnResponse.doSomeThing(response1);
                    }
                });
            }
        });
    }

    public static void postRequest(String url, Map<String, String> formParam, final IDoSomeThing<HttpResponse> doOnResponse, final IDoSomeThing<HttpFailure> doOnFailure, List<HeadItem> headIfNeed) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Log.i("HttpHelper", "postRequest url=" + url);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<String> keyIt = formParam.keySet().iterator();
        while (keyIt.hasNext()) {
            String key = keyIt.next();
            if (formParam.get(key) != null) {
                builder.add(key, formParam.get(key));
            }
            Log.i("HttpHelper", "formParam key=" + key + ",formParam value=" + formParam.get(key));

        }
        RequestBody body = builder.build();

//        okhttp3.Request request = new okhttp3.Request.Builder()
//                .url(url).addHeader("mac", BaseConfig.OtherConfig.MAC_ADD)
//                .post(body)
//                .build();

        Request.Builder requestBuilder = new okhttp3.Request.Builder();
        requestBuilder.url(url);
        requestBuilder.addHeader("mac", BaseConfig.OtherConfig.MAC_ADD);
        if (headIfNeed != null) {
            StringBuilder ck = null;
            Iterator<HeadItem> itemIterator = headIfNeed.iterator();
            while (itemIterator.hasNext()) {
                HeadItem item = itemIterator.next();
                Log.i("HttpHelper", "head:key=" + item.key + ",value=" + item.value);
                if (item.writeTo == WRITE_TO_HEAD) {
                    requestBuilder.addHeader(item.key, item.value);
                } else if (item.writeTo == WRITE_TO_COOKIE) {
                    if (ck == null)
                        ck = new StringBuilder();
                    ck.append(";").append(item.key).append("=").append(item.value);
                } else if (item.writeTo == WRITE_TO_BOTTH) {
                    requestBuilder.addHeader(item.key, item.value);
                    if (ck == null)
                        ck = new StringBuilder();
                    ck.append(";").append(item.key).append("=").append(item.value);
                }
            }
            if (ck != null) {
                requestBuilder.addHeader("Cookie", ck.toString().substring(1));
                Log.i("HttpHelper", "head Cookie:" + ck.toString().substring(1));
            }
        }
        okhttp3.Request request = requestBuilder
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("HttpHelper", e.getMessage());
                final HttpFailure failure = HttpFailure.create(call, e);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doOnFailure.doSomeThing(failure);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final HttpResponse response1 = HttpResponse.create(call, response);
                Log.i("HttpHelper", "response called,read data from body.string()");
                Log.i("HttpHelper", response1.getResponseString());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doOnResponse.doSomeThing(response1);
                    }
                });
            }
        });

    }

    public static void postJson(String url, String json, final IDoSomeThing<HttpResponse> doOnResponse, final IDoSomeThing<HttpFailure> doOnFailure, List<HeadItem> headIfNeed) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Log.i("HttpHelper", "postJson url=" + url);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        //
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
//        okhttp3.Request request = new okhttp3.Request.Builder()
//                .url(url).addHeader("mac", BaseConfig.OtherConfig.MAC_ADD)
//                .post(body)
//                .build();

        Request.Builder requestBuilder = new okhttp3.Request.Builder();
        requestBuilder.url(url);
        requestBuilder.addHeader("mac", BaseConfig.OtherConfig.MAC_ADD);
        if (headIfNeed != null) {
            StringBuilder ck = null;
            Iterator<HeadItem> itemIterator = headIfNeed.iterator();
            while (itemIterator.hasNext()) {
                HeadItem item = itemIterator.next();
                Log.i("HttpHelper", "head:key=" + item.key + ",value=" + item.value);
                if (item.writeTo == WRITE_TO_HEAD) {
                    requestBuilder.addHeader(item.key, item.value);
                } else if (item.writeTo == WRITE_TO_COOKIE) {
                    if (ck == null)
                        ck = new StringBuilder();
                    ck.append(";").append(item.key).append("=").append(item.value);
                } else if (item.writeTo == WRITE_TO_BOTTH) {
                    requestBuilder.addHeader(item.key, item.value);
                    if (ck == null)
                        ck = new StringBuilder();
                    ck.append(";").append(item.key).append("=").append(item.value);
                }
            }
            if (ck != null) {
                requestBuilder.addHeader("Cookie", ck.toString().substring(1));
                Log.i("HttpHelper", "head Cookie:" + ck.toString().substring(1));
            }
        }
        okhttp3.Request request = requestBuilder.post(body)
                .build();

        //
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("HttpHelper", "postJson error"+e.getMessage());
                final HttpFailure failure = HttpFailure.create(call, e);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doOnFailure.doSomeThing(failure);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final HttpResponse response1 = HttpResponse.create(call, response);
                Log.i("HttpHelper", "postJson response called,read data from body.string()");
                Log.i("HttpHelper", response1.getResponseString());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doOnResponse.doSomeThing(response1);
                    }
                });
            }
        });
    }

    public final static String METHOD_GET = "get";
    public final static String METHOD_POST = "post";

    /**
     * 调用以前的SimpleHttp
     */
    public static void callSimpleHttp(String method, String url, Map<String, String> queries,IDoSomeThing<SimpleHttp.Result>doSomeThing) {
        SimpleHttp.Result result = null;
        if (method == METHOD_GET) {
            result = SimpleHttp.api2Get(url, queries);
        } else if (method == METHOD_POST) {
            result = SimpleHttp.api2Post(url, queries);
        }
        doSomeThing.doSomeThing(result);
    }
}
