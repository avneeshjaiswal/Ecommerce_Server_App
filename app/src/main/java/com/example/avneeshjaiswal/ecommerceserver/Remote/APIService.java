package com.example.avneeshjaiswal.ecommerceserver.Remote;

import com.example.avneeshjaiswal.ecommerceserver.Model.MyResponse;
import com.example.avneeshjaiswal.ecommerceserver.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by avneesh jaiswal on 15-Mar-18.
 */

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA3jCmTxg:APA91bG8m8AuAHdg7FYtNBw5B_YNqsOSMx0t3TuICpJAYsa-19ofhpsKPMdSJ6w_YscBisl2kJdycRq_u4k4EmDaca4V0N-DTz82SWv0LM2WCexmvlOR85Yqckermgb8GJuFVz0KF7WN"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
