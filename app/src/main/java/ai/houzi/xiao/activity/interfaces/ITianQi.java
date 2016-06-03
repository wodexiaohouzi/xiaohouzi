package ai.houzi.xiao.activity.interfaces;

import ai.houzi.xiao.entity.TianQi;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ITianQi {
    @GET("weather_mini")
    Call<TianQi> getTianQi(@Query("city") String city);


//    @GET("{city}")
//    Call<TianQi> getTianQiPath(@Path("city") String city);
}
