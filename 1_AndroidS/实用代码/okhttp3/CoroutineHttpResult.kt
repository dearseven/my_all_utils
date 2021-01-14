package wc.c.libbase.okhttp

class CoroutineHttpResult {
    var isSuccess:Boolean=false;
    var response:HttpResponse?=null;
    var failure:HttpFailure?=null;
}