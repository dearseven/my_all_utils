package wc.c.libbase.okhttp

import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.*
import wc.c.libbase.configs.BaseConfig
import wc.c.libbase.utils.IDoSomeThing
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CoroutineHttpHelper {
    private val handler = Handler(Looper.getMainLooper())

    suspend fun getRequest(url: String): CoroutineHttpResult? {
        var url = url
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://$url"
        }
        Log.i("HttpHelper", "url=$url")
        val okHttpClient = OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        //
        val request = Request.Builder()
                .url(url).addHeader("mac", BaseConfig.OtherConfig.MAC_ADD)
                .get()
                .build()
        //
        val call = okHttpClient.newCall(request)

        return suspendCoroutine { conti ->
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.i("HttpHelper", e.message)
                    val failure = HttpFailure.create(call, e)
                    conti.resume(CoroutineHttpResult().also {
                        it.isSuccess = false
                        it.failure = failure
                    })
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    Log.i("HttpHelper", "response called,read data from body.string()")
                    val response1 = HttpResponse.create(call, response)
                    conti.resume(CoroutineHttpResult().also {
                        it.isSuccess = true
                        it.response = response1
                    })
                }
            })
        }
    }

}