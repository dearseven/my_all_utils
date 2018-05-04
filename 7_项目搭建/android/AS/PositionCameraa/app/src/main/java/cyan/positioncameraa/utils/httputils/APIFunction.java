package cyan.positioncameraa.utils.httputils;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * API的接口定义,全是按照json格式提交
 * Created by wx on 2018/4/19.
 */

public interface APIFunction {
    @POST("/user/default/authorize")
    @Deprecated
    Observable<ResponseBody> playAuthorize(@Body RequestBody requestBody);

    @GET("/s")
    Observable<ResponseBody> seeBaidu(@QueryMap HashMap<String, Object> params);

}
