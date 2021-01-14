package wc.c.libbase.okhttp;

import okhttp3.Call;
import okhttp3.Response;

public class HttpFailure {
    private Call call;
    private Exception exception;

    private HttpFailure() {
    }

    public static HttpFailure create(Call c, Exception e) {
        HttpFailure hr = new HttpFailure();
        hr.call = c;
        hr.exception=e;
        return hr;
    }

    public Call getCall() {
        return call;
    }

    public Exception getException() {
        return exception;
    }
}
