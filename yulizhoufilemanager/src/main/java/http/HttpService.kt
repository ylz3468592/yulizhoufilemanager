package http

import com.dscomm.mit.ga.android.http.bean.Weather
import com.google.gson.JsonObject
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url


interface HttpService {

    //获取天气接口
    @GET
    fun getWeather(@Url url: String): Flowable<Weather>

    //获取JsonObject对象
    @GET
    fun getPopular(@Url url: String): Flowable<JsonObject>

    //流，获取文件
    @GET
    @Streaming
    fun downloadFile(@Url imgUrl: String): Flowable<ResponseBody>

//    //获取在线辅警列表
//    @GET
//    fun getFjList(@Url url: String): Flowable<Any>
//
//
//    //发送转警请求，并获取返回值
//    @POST
//    @Headers("Content-Type:application/json")
//    fun forwardJq(@Url url: String, @Body vo: FjForwardBean): Flowable<FjResponseBean>
//
//    //发送转警撤回请求
//    @POST
//    @Headers("Content-Type:application/json")
//    fun cancelJq(@Url url: String, @Body vo: FjCancelBean): Flowable<FjResponseBean>
//
//
//    //发送转警撤回请求
//    @POST
//    @Headers("Content-Type:application/json")
//    fun arriveTime(@Url url: String, @Body vo: SignArriveTime): Flowable<FjResponseBean>
//
//
//    //发送辅警反馈单查询请求
//    @POST
//    @Headers("Content-Type:application/json")
//    fun fjFeedbackQuery(@Url url: String, @Body vo: FjFeedbackQuery): Flowable<FjFeedBackQueryResponseBean>
//
//
//    //发送查询当前民警或辅警24小时内已反馈警单列表
//    @POST
//    @Headers("Content-Type:application/json")
//    fun queryHasFeedbackIncidents(@Url url: String, @Body vo: QueryFeedbackInfo): Flowable<List<IncidentBean>>
//
//
//    //发送辅警反馈审核结果
//    @POST
//    @Headers("Content-Type:application/json")
//    fun fjFeedbackResult(@Url url: String, @Body vo: FjFeedbackResult): Flowable<FjResponseBean>
//
//
//    //拉警情列表结果
//    @POST
//    @FormUrlEncoded
//    @Headers("Content-Type:application/json")
//    fun queryIncients(@Url url: String, @Field("imei") imei: String): Flowable<FjResponseBean>
//
//
//    //查看案由地址分类等的版本
//    @GET
//    fun queryAyAndAddressVersion(@Url url: String): Flowable<List<Int>>
//
//    //查看案由
//    @GET
//    fun queryAy(@Url url: String): Flowable<String>
//
//    @GET
//    fun queryAddress(@Url url: String): Flowable<String>
//
//    @GET
//    fun queryCljg(@Url url: String): Flowable<String>

}