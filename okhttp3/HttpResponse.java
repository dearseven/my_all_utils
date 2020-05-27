package wc.c.libbase.okhttp;

import okhttp3.Call;
import okhttp3.Response;

public class HttpResponse {
    private Call call;
    private Response response;

    private HttpResponse() {
    }

    public static HttpResponse create(Call c, Response r) {
        HttpResponse hr = new HttpResponse();
        hr.call = c;
        hr.response = r;
        return hr;
    }

    public Call getCall() {
        return call;
    }

    public Response getResponse() {
        return response;
    }
}
