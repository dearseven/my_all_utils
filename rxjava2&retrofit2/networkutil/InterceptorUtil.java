
/**
 * retrofit2的拦截器
 * Created by wx on 2018/4/19.
 */

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * retrofit2
 * 拦截器工具类
 */
public class InterceptorUtil {
    public static String TAG = "----";

    //日志拦截器
    public static HttpLoggingInterceptor LogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.w(TAG, "log: "+message );
                //Debug.log4op(InterceptorUtil.class, message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);//设置打印数据的级别
    }

    public static Interceptor HeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //在这里你可以做一些想做的事,比如token失效时,重新获取token
                //或者添加header等等,
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json;charset=UTF-8")
                        .addHeader("Authorization", HttpConfig.TOKEN)
                        .build();
                return chain.proceed(request);
            }
        };
    }
}
