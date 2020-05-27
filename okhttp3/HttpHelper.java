package wc.c.libbase.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import wc.c.libbase.utils.IDoSomeThing;

public class HttpHelper {
    private static Handler handler=new Handler(Looper.getMainLooper());

    public static void getRequest(String url, final IDoSomeThing<HttpResponse> doOnResponse, final IDoSomeThing<HttpFailure> doOnFailure) {
        if(!url.startsWith("http://")&&!url.startsWith("https://")){
            url="http://"+url;
        }
        Log.i("HttpHelper","url="+url);
        OkHttpClient okHttpClient = new OkHttpClient();
        //
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();
        //
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("HttpHelper",e.getMessage());
                final HttpFailure failure=HttpFailure.create(call, e);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doOnFailure.doSomeThing(failure);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("HttpHelper","response called,read data from body.string()");
                final HttpResponse response1=HttpResponse.create(call, response);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doOnResponse.doSomeThing(response1);
                    }
                });
            }
        });
    }

    public static void postRequest(String url, Map<String, String> formParam, final IDoSomeThing<HttpResponse> doOnResponse, final IDoSomeThing<HttpFailure> doOnFailure) {
        if(!url.startsWith("http://")&&!url.startsWith("https://")){
            url="http://"+url;
        }
        Log.i("HttpHelper","url="+url);
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<String> keyIt = formParam.keySet().iterator();
        while (keyIt.hasNext()) {
            String key = keyIt.next();
            builder.add(key, formParam.get(key));
        }
        RequestBody body = builder.build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("HttpHelper",e.getMessage());
                final HttpFailure failure=HttpFailure.create(call, e);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doOnFailure.doSomeThing(failure);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("HttpHelper","response called,read data from body.string()");
                final HttpResponse response1=HttpResponse.create(call, response);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doOnResponse.doSomeThing(response1);
                    }
                });
            }
        });

    }

    public static void postJson(String url, String json, final IDoSomeThing<HttpResponse> doOnResponse, final IDoSomeThing<HttpFailure> doOnFailure) {
        if(!url.startsWith("http://")&&!url.startsWith("https://")){
            url="http://"+url;
        }
        Log.i("HttpHelper","url="+url);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        //
        OkHttpClient okHttpClient = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();
        //
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("HttpHelper",e.getMessage());
                final HttpFailure failure=HttpFailure.create(call, e);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doOnFailure.doSomeThing(failure);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("HttpHelper","response called,read data from body.string()");
                final HttpResponse response1=HttpResponse.create(call, response);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doOnResponse.doSomeThing(response1);
                    }
                });
            }
        });
    }
}
