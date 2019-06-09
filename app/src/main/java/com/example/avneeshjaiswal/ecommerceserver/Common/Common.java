package com.example.avneeshjaiswal.ecommerceserver.Common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.avneeshjaiswal.ecommerceserver.Model.Request;
import com.example.avneeshjaiswal.ecommerceserver.Model.User;
import com.example.avneeshjaiswal.ecommerceserver.Remote.APIService;
import com.example.avneeshjaiswal.ecommerceserver.Remote.FCMRetrofitClient;
import com.example.avneeshjaiswal.ecommerceserver.Remote.IGeoCoordinates;
import com.example.avneeshjaiswal.ecommerceserver.Remote.RetrofitClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by avneesh jaiswal on 09-Feb-18.
 */

public class Common {
    public static User currentUser;
    public static Request currentRequest;
    public static final String baseUrl = "https://maps.googleapis.com";
    public static final String fcmUrl = "https://fcm.googleapis.com/";

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final int PICK_IMAGE_REQUEST = 1;


    public static String convertCodeToStatus(String code){
        if(code.equals("0")){
            return "Placed";
        }else if(code.equals("1")){
            return "On my way";
        }else{
            return "Shipped";
        }
    }

    public static IGeoCoordinates getGeoCodeService(){
        return RetrofitClient.getClient(baseUrl).create(IGeoCoordinates.class);
    }

    public static APIService getFCMClient(){
        return FCMRetrofitClient.getClient(fcmUrl).create(APIService.class);
    }


    public static Bitmap scaleBitmap(Bitmap bitmap,int newWidth,int newHeight){
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth,newHeight,Bitmap.Config.ARGB_8888);

        float scaleX = newWidth/(float)bitmap.getWidth();
        float scaleY = newHeight/(float)bitmap.getHeight();
        float pivotX=0,pivotY=0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX,scaleY,pivotX,pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;
    }

    public static String getDate(long time){
        /*Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date = new StringBuilder(new SimpleDateFormat("yyyy-MM-dd HH:mm")).toString();*/
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        return date.toString();

    }


}
