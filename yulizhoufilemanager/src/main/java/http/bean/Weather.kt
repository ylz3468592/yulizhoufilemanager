package com.dscomm.mit.ga.android.http.bean

/**
 * @author yulizhou
 * @description:
 * @date :2020/3/26 10:24
 */
class Weather {
    var message = ""
    var status = ""
    var date = ""
    var time = ""
    var cityInfo: CityInfo = CityInfo()
    var data:WeatherData =WeatherData()
    var yesterday:YesterdayWeather=YesterdayWeather()
}

class WeatherData{
    var shidu=""
    var pm25=""
    var pm10=""
    var quality=""
    var wendu=""
    var ganmao=""
    var forecast = ArrayList<WeatherDetail>()
}

class YesterdayWeather{
    var date = ""
    var high = ""
    var low = ""
    var ymd = ""
    var week = ""
    var sunrise = ""
    var sunset = ""
    var aqi = ""
    var fx = ""
    var fl = ""
    var type = ""
    var notice = ""
}

class WeatherDetail {
    var date = ""
    var high = ""
    var low = ""
    var ymd = ""
    var week = ""
    var sunrise = ""
    var sunset = ""
    var aqi = ""
    var fx = ""
    var fl = ""
    var type = ""
    var notice = ""
}

class CityInfo {
    var city = ""
    var citykey = ""
    var parent = ""
    var updateTime = ""
}