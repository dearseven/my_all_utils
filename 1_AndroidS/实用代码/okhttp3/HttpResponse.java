package wc.c.libbase.okhttp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class HttpResponse {
    private Call call;
    private Response response;
    private String bodyString;

    private HttpResponse() {
    }

    public static HttpResponse create(Call c, Response r) {
        HttpResponse hr = new HttpResponse();
        hr.call = c;
        hr.response = r;
        try {
            hr.bodyString=r.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hr;
    }

    public Call getCall() {
        return call;
    }

    public Response getResponse() {
        return response;
    }

    public String getResponseString() {
        return bodyString;
    }
}
