package com.excavanger.musicotion.activites;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.excavanger.musicotion.R;
import com.excavanger.musicotion.adapters.TrendingAdaptor;
import com.excavanger.musicotion.models.MediasModel;
import com.excavanger.musicotion.models.SearchModel;
import com.excavanger.musicotion.models.SearchModulesModel;
import com.excavanger.musicotion.networkcalls.NetworkInterface;
import com.excavanger.musicotion.utils.Common;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchAllActivity extends AppCompatActivity {
    private List<MediasModel> allSongs;
    private RecyclerView recyclerView;
    private TrendingAdaptor allSongsAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all);
        allSongs = new ArrayList<>();
        recyclerView = findViewById(R.id.all_songs);
        Bundle bundle = getIntent().getExtras();
        String query = "";
        if(bundle.getString("query")!= null)
        {
            query = bundle.getString("query");
        }
        LoadAllSongs(query);
    }

    private void LoadAllSongs(String query){
        Retrofit retrofit= Common.getClient();
        HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?p=1&q="+query+"&_format=json&_marker=0&api_version=4&ctx=web6dot0&n=20&__call=search.getResults");
        NetworkInterface api = retrofit.create(NetworkInterface.class);
        Call<JsonObject> call = api.getSearchAll(uri.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray arr = response.body().getAsJsonObject().get("results").getAsJsonArray();
                JsonObject obj;
                int n = arr.size();
                for(int i=0;i<n;i++){
                    obj = arr.get(i).getAsJsonObject();
                    allSongs.add(new MediasModel(obj.get("id").getAsString(), Html.fromHtml(obj.get("title").getAsString(), FROM_HTML_MODE_LEGACY).toString(), Html.fromHtml(obj.get("subtitle").getAsString(), FROM_HTML_MODE_LEGACY).toString(), obj.get("type").getAsString(), obj.get("perma_url").getAsString(), Common.getHighQuality(obj.get("image").getAsString()), obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString(), "hindi"));
                }

                allSongsAdaptor = new TrendingAdaptor(allSongs,SearchAllActivity.this);
                GridLayoutManager layoutManager = new GridLayoutManager(SearchAllActivity.this,
                        2, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                allSongsAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("a",call.request().toString());
            }
        });
    }
}