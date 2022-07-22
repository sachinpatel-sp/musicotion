package com.excavanger.musicotion.utils;

import android.content.SharedPreferences;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.excavanger.musicotion.adapters.TopArtistsAdaptor;
import com.excavanger.musicotion.models.CircularModel;
import com.excavanger.musicotion.networkcalls.NetworkInterface;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Common {
    public static String query ="";
    public static String language;
    public static Retrofit retrofit =null;
    public static String url="https://www.jiosaavn.com/";
    public static String quality;
    public static Retrofit getClient(){
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl(Common.url)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

        }
        return retrofit;
    }
    public static String urlDecoder(String url){
        String decoded = "";
        for(int i = 0;i<url.length();i++){
            if(url.charAt(i) == '/'){
                decoded += "%2f";
            }else if(url.charAt(i) == '+'){
                decoded += "%2b";
            }else{
                decoded += url.charAt(i);
            }
        }
        return decoded;
    }
    public static String getToken(String perma_url){
        String token="";
        int i = perma_url.length()-1;
        while(perma_url.charAt(i) != '/'){
            token=perma_url.charAt(i)+token;
            i--;
        }
        return token;
    }
    public static String songUrl(String token){
        final String[] songUrl = {""};
        Retrofit retrofit= Common.getClient();
        NetworkInterface api=retrofit.create(NetworkInterface.class);
        Call<JsonObject> call=api.songUrl(token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                songUrl[0] = response.body().get("auth_url").getAsString();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
        return songUrl[0];
    }

    public static String getHighQuality(String url){
        String toReplace = "150x150.jpg";
        String toReplace2 = "50x50.jpg";
        String finalReplace = "500x500.jpg";
        return url.replace(toReplace,finalReplace).replace(toReplace2,finalReplace);
        //return url;
    }
    public static NetworkInterface getApiService(){
        return retrofit.create(NetworkInterface.class);
    }

    public static void setLanguage(String lang){
        language = lang;
    }
    public static void setQuality(String qual){quality = qual;}
}
