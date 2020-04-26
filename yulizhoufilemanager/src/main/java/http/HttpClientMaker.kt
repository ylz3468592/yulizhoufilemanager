package http

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author yulizhou
 * @description:
 * @date :2020/4/17 10:23
 */
object HttpClientMaker {

    fun makePopularClient(): HttpService {
        val okHttpClient = OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build()
        val allUrl = "http://192.168.0.151:8090/mit-web/rest/userInfo/"
        val service = Retrofit.Builder().baseUrl(allUrl).client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(HttpService::class.java)
        return service
    }


}