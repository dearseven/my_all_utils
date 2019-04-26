

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * API的接口定义,全是按照json格式提交
 * Created by wx on 2018/4/19.
 */

public interface APIFunction {
    @POST("/user/default/authorize")
    Observable<ResponseBody> playAuthorize(@Body RequestBody requestBody);
}
