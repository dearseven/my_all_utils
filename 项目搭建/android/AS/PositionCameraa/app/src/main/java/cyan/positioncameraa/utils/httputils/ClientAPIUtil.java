package cyan.positioncameraa.utils.httputils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import io.reactivex.Observable;


/**
 * 终端的api工具类，封装RXJAVA2和Retrofit2
 * Created by wx on 2018/4/19.
 */

public class ClientAPIUtil {
    /**
     * 传入这么多参数，然后变成json 在post给服务端
     *
     * @return
     */
    @Deprecated
    public Observable<ResponseBody> playAuthorize(String cid, String tid, String supercid, String playType, String contentType, String businessType, String idFlag) {
        JSONObject param = new JSONObject();
        try {
            param.put("cid", cid);
            param.put("tid", tid);
            param.put("sc", supercid);
            param.put("pt", playType);
            param.put("ct", contentType);
            param.put("bt", businessType);
            param.put("of", idFlag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param.toString());
        return mAPIFunction.playAuthorize(requestBody);
    }

    /**
     * 一个Get请求的测试百度
     *
     * @return
     */
    public Observable<ResponseBody> seeBaidu(HashMap<String, Object> m) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("wd=").append(m.get("wd"));
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/text"), sb.toString());
        return mAPIFunction.seeBaidu(m);
    }

    //---------------------------------------------------------------------------------------------------------------------------------
    private static volatile ClientAPIUtil instance = null;
    private APIFunction mAPIFunction;

    public static ClientAPIUtil getInstance() {
        if (instance == null) {
            synchronized (ClientAPIUtil.class) {
                if (instance == null) {
                    instance = new ClientAPIUtil();
                }
            }
        }
        return instance;
    }

    private ClientAPIUtil() {
        /*1初始化一个OKHTTP*/
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)
                .readTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)
                .writeTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)
                .addInterceptor(InterceptorUtil.HeaderInterceptor())
                .addInterceptor(InterceptorUtil.LogInterceptor())//添加日志拦截器
                .build();
        //
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(HttpConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加rxjava转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                .client(mOkHttpClient)
                .build();
        mAPIFunction = mRetrofit.create(APIFunction.class);
    }
}
