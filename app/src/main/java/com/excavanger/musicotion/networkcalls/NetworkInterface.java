package com.excavanger.musicotion.networkcalls;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface NetworkInterface {

    @GET("api.php?__call=webapi.getLaunchData&api_version=4&_format=json&_marker=0&ctx=web6dot0")
    Call<JsonObject> newTrending(@Header("Cookie") String language);

    @GET("api.php?__call=content.getCharts&api_version=4&_format=json&_marker=0&ctx=web6dot0")
    Call<JsonArray> getCharts(@Header("Cookie") String language);

    @GET("api.php?__call=social.getTopArtists&api_version=4&_format=json&_marker=0&ctx=web6dot0")
    Call<JsonObject> topArtists(@Header("Cookie") String language);

    @GET("api.php?__call=content.getAlbums&api_version=4&_format=json&_marker=0&n=500&p=1&ctx=web6dot0")
    Call<JsonObject> newAlbums(@Header("Cookie") String language);

    @GET("api.php?__call=webradio.getFeaturedStations&api_version=4&_format=json&_marker=0&ctx=web6dot0")
    Call<JsonArray> featRadio(@Header("Cookie") String language);

    @GET
    Call<JsonObject> songUrl(@Url String url);

    @GET("api.php?__call=playlist.getDetails&listid=49&api_version=4&_format=json&_marker=0&ctx=web6dot0")
    Call<JsonObject> weeklyTop(@Header("Cookie") String language);

    @GET
    Call<JsonArray> similarSongs(@Url String url);

    @GET
    Call<JsonObject> loadPlaylist(@Url String url);

    @GET
    Call<JsonObject> loadAlbum(@Url String url);

    @GET
    Call<JsonObject> loadArtist(@Url String url);

    @GET
    Call<JsonObject> stationID(@Url String url);

    @GET
    Call<JsonObject> getRadioSongs(@Url String url);

    @GET
    Call<JsonObject> getQueryResult(@Url String url);

    @GET("api.php?__call=content.getTopSearches&ctx=web6dot0&api_version=4&_format=json&_marker=0")
    Call<JsonArray> topSearch();

    @GET
    Call<JsonObject> getLyrics(@Url String url);

    @GET
    Call<JsonObject> getSearchAll(@Url String url);

    //https://www.jiosaavn.com/api.php?p=1&q=maa sherawaliye&_format=json&_marker=0&api_version=4&ctx=web6dot0&n=20&__call=search.getResults
}
