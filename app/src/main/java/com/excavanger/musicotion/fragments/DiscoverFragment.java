package com.excavanger.musicotion.fragments;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.excavanger.musicotion.R;
import com.excavanger.musicotion.adapters.ChartsAdaptor;
import com.excavanger.musicotion.adapters.ModulesAdaptor;
import com.excavanger.musicotion.adapters.RadioAdaptor;
import com.excavanger.musicotion.adapters.TopArtistsAdaptor;
import com.excavanger.musicotion.adapters.TrendingAdaptor;
import com.excavanger.musicotion.models.CircularModel;
import com.excavanger.musicotion.models.MediaModel;
import com.excavanger.musicotion.models.MediasModel;
import com.excavanger.musicotion.models.ModulesModel;
import com.excavanger.musicotion.networkcalls.NetworkInterface;
import com.excavanger.musicotion.utils.Common;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private String lang;
    private TextView greetingTextView;
    private List<ModulesModel> modulesList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ModulesAdaptor modulesAdaptor;
    private RecyclerView trendingRecycler,trendingPlaylistRecycler,chartsRecycler,topArtistRecycler,newAlbumsRecycler,featuredRadioRecycler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_discover, container, false);
        greetingTextView = view.findViewById(R.id.greeting);
        trendingRecycler = view.findViewById(R.id.home_scroll);
        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        lang = "L="+Common.language;
        modulesList = new ArrayList<>();
        modulesAdaptor = new ModulesAdaptor(modulesList,getContext());
        trendingRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        trendingRecycler.setAdapter(modulesAdaptor);
        loadLaunchData();
//        loadTrending();
//        loadCharts();
//        loadArtists();
//        loadNewAlbums();
//        loadRadio();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadLaunchData();
            }
        });
        return view;
    }
//    public void loadTrending(){
//        final String[] greetMsg = new String[1];
//        Retrofit retrofit= Common.getClient();
//        NetworkInterface api=retrofit.create(NetworkInterface.class);
//        Call<JsonObject> call=api.newTrending(lang);
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                JsonArray arr=response.body().get("new_trending").getAsJsonArray();
//                JsonArray arr2=response.body().get("top_playlists").getAsJsonArray();
//                int n=arr.size();
//                JsonObject obj;
//                greetMsg[0] = response.body().get("greeting").getAsString();
//                greetingTextView.setText(greetMsg[0]);
//                for(int i=0;i<n;i++){
//                    obj=arr.get(i).getAsJsonObject();
//                    String type = obj.get("type").getAsString();
//                    String encUrl = "";
//                    if(type.equalsIgnoreCase("song")){
//                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
//                    }
//                    trendingNowList.add(new MediaModel(obj.get("id").getAsString(),obj.get("title").getAsString().replace("&quot;","'"),obj.get("subtitle").getAsString(),obj.get("type").getAsString(),obj.get("perma_url").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),encUrl));
//                }
//
//                n=arr2.size();
//                for(int i=0;i<n;i++){
//                    obj=arr2.get(i).getAsJsonObject();
//                    String type = obj.get("type").getAsString();
//                    String encUrl = "";
//                    if(type.equalsIgnoreCase("song")){
//                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
//                    }
//                    topPlaylistList.add(new MediaModel(obj.get("id").getAsString(),obj.get("title").getAsString(),obj.get("subtitle").getAsString(),obj.get("type").getAsString(),obj.get("perma_url").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),encUrl));
//                }
//
//                TrendingAdaptor trendingAdaptor = new TrendingAdaptor(trendingNowList,getContext());
//                trendingRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
//                trendingRecycler.setAdapter(trendingAdaptor);
//                trendingAdaptor.notifyDataSetChanged();
//
//                TrendingAdaptor trendingPlaylistAdaptor = new TrendingAdaptor(topPlaylistList,getContext());
//                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
//                        2, GridLayoutManager.HORIZONTAL, false);
//                trendingPlaylistRecycler.setLayoutManager(layoutManager);
//                trendingPlaylistRecycler.setAdapter(trendingPlaylistAdaptor);
//                trendingPlaylistAdaptor.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//            }
//        });
//    }
//    public void loadCharts(){
//        Retrofit retrofit= Common.getClient();
//        NetworkInterface api=retrofit.create(NetworkInterface.class);
//        Call<JsonArray> call=api.getCharts(lang);
//        call.enqueue(new Callback<JsonArray>() {
//            @Override
//            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                JsonArray arr = response.body().getAsJsonArray();
//                int n = arr.size();
//                //Log.d("sz",response.body().getAsString());
//                JsonObject obj;
//                for (int i = 0; i < n; i++) {
//                    obj = arr.get(i).getAsJsonObject();
//                    String type = obj.get("type").getAsString();
//                    String encUrl = "";
//                    if (type.equalsIgnoreCase("song")) {
//                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
//                    }
//                    allChartsList.add(new MediaModel(obj.get("id").getAsString(), obj.get("title").getAsString(), obj.get("subtitle").getAsString(), obj.get("type").getAsString(), obj.get("perma_url").getAsString(), Common.getHighQuality(obj.get("image").getAsString()), encUrl));
//                }
//
//                ChartsAdaptor chartsAdaptor = new ChartsAdaptor(allChartsList, getContext());
//                chartsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//                SnapHelper snapHelper = new PagerSnapHelper();
//                snapHelper.attachToRecyclerView(chartsRecycler);
//                chartsRecycler.setAdapter(chartsAdaptor);
//                chartsAdaptor.notifyDataSetChanged();
//            }
//            @Override
//            public void onFailure(Call<JsonArray> call, Throwable t) {
//                Log.e("sz","failed");
//            }
//        });
//    }
//
//    public void loadArtists(){
//        Retrofit retrofit= Common.getClient();
//        NetworkInterface api=retrofit.create(NetworkInterface.class);
//        Call<JsonObject> call=api.topArtists(lang);
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                JsonArray arr=response.body().get("top_artists").getAsJsonArray();
//                int n=arr.size();
//                JsonObject obj;
//                for(int i=0;i<n;i++){
//                    obj=arr.get(i).getAsJsonObject();
////                  String type = obj.get("type").getAsString();
//                    String encUrl = "";
////                    if(type.equalsIgnoreCase("song")){
////                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
////                    }
//                    topArtistsList.add(new CircularModel(obj.get("artistid").getAsString(),obj.get("name").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),obj.get("perma_url").getAsString(),0,""));
//                }
//
//                TopArtistsAdaptor topArtistsAdaptor = new TopArtistsAdaptor(topArtistsList,getContext());
//                topArtistRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
//                topArtistRecycler.setAdapter(topArtistsAdaptor);
//                topArtistsAdaptor.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//            }
//        });
//    }
//
//    public void loadNewAlbums(){
//        Retrofit retrofit= Common.getClient();
//        NetworkInterface api=retrofit.create(NetworkInterface.class);
//        Call<JsonObject> call=api.newAlbums(lang);
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                JsonArray arr=response.body().get("data").getAsJsonArray();
//                int n=arr.size();
//                JsonObject obj;
//                for(int i=0;i<n;i++){
//                    obj=arr.get(i).getAsJsonObject();
//                    String type = obj.get("type").getAsString();
//                    String encUrl = "";
//                    if(type.equalsIgnoreCase("song")){
//                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
//                    }
//                    newAlbumsList.add(new MediaModel(obj.get("id").getAsString(),obj.get("title").getAsString(),obj.get("subtitle").getAsString(),obj.get("type").getAsString(),obj.get("perma_url").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),encUrl));
//                }
//
//                TrendingAdaptor newAlbumsAdaptor = new TrendingAdaptor(newAlbumsList,getContext());
//                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
//                        2, GridLayoutManager.HORIZONTAL, false);
//                newAlbumsRecycler.setLayoutManager(layoutManager);
//                newAlbumsRecycler.setAdapter(newAlbumsAdaptor);
//                newAlbumsAdaptor.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//            }
//        });
//    }
//
//    public void loadRadio(){
//        Retrofit retrofit= Common.getClient();
//        NetworkInterface api=retrofit.create(NetworkInterface.class);
//        Call<JsonArray> call=api.featRadio(lang);
//        call.enqueue(new Callback<JsonArray>() {
//            @Override
//            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                JsonArray arr=response.body().getAsJsonArray();
//                int n=arr.size();
//                JsonObject obj;
//                for(int i=0;i<n;i++){
//                    obj=arr.get(i).getAsJsonObject();
////                  String type = obj.get("type").getAsString();
//                    String encUrl = "";
////                    if(type.equalsIgnoreCase("song")){
////                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
////                    }
//                    featuredRadioList.add(new CircularModel(obj.get("more_info").getAsJsonObject().get("featured_station_type").getAsString(),obj.get("title").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),obj.get("perma_url").getAsString(),0,obj.get("more_info").getAsJsonObject().get("language").getAsString()));
//                }
//
//                RadioAdaptor featuredRadioAdaptor = new RadioAdaptor(featuredRadioList,getContext());
//                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
//                        2, GridLayoutManager.HORIZONTAL, false);
//                featuredRadioRecycler.setLayoutManager(layoutManager);
//                featuredRadioRecycler.setAdapter(featuredRadioAdaptor);
//                featuredRadioAdaptor.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<JsonArray> call, Throwable t) {
//
//            }
//        });
//    }

    public void loadLaunchData(){
        modulesList.clear();
        final String[] greetMsg = new String[1];
        Retrofit retrofit= Common.getClient();
        NetworkInterface api=retrofit.create(NetworkInterface.class);
        Call<JsonObject> call=api.newTrending(lang);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
               greetMsg[0] = response.body().get("greeting").getAsString();
                greetingTextView.setText(greetMsg[0]);
                JsonObject arr=response.body().get("modules").getAsJsonObject();
                Iterator x = arr.keySet().iterator();
                while(x.hasNext()) {
                    String key = (String)x.next();
                    JsonArray arr2 = response.body().get(arr.get(key).getAsJsonObject().get("source").getAsString()).getAsJsonArray();
                    int n = arr2.size();
                    List<MediasModel> medias = new ArrayList<>();
                    for (int j = 0; j < n; j++) {
                        JsonObject obj = arr2.get(j).getAsJsonObject();
                        Log.e("obj",obj.toString());
                        if(!obj.has("subtitle")){
                            continue;
                        }
                        String type = obj.get("type").getAsString();
                        String encUrl = "";
                        String lang = "hindi";
                        String id = obj.get("id").getAsString();
                        if (type.equalsIgnoreCase("song") && obj.get("more_info").getAsJsonObject().has("encrypted_media_url")) {
                            encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
                        }
                        if (type.equalsIgnoreCase("radio") || type.equalsIgnoreCase("radio_station")) {
                            if(obj.get("more_info").getAsJsonObject().has("language"))
                                lang = obj.get("more_info").getAsJsonObject().get("language").getAsString();
                            else
                                lang = "hindi";
                            id = obj.get("more_info").getAsJsonObject().get("featured_station_type").getAsString();
                        }
                        medias.add(new MediasModel(id, Html.fromHtml(obj.get("title").getAsString(), FROM_HTML_MODE_LEGACY).toString(), Html.fromHtml(obj.get("subtitle").getAsString(), FROM_HTML_MODE_LEGACY).toString(), obj.get("type").getAsString(), obj.get("perma_url").getAsString(), Common.getHighQuality(obj.get("image").getAsString()), encUrl, lang));
                    }
                    String src = arr.get(key).getAsJsonObject().get("source").getAsString();
                    int type;
                    if(src.equalsIgnoreCase("charts")){
                        type = 2;
                    }else if(src.equalsIgnoreCase("radio")){
                        type = 3;
                    }else if(src.equalsIgnoreCase("artist_recos")){
                        type = 3;
                    }else{
                        type = 1;
                    }
                    modulesList.add(new ModulesModel(arr.get(key).getAsJsonObject().get("title").getAsString(),arr.get(key).getAsJsonObject().get("subtitle").getAsString(),arr.get(key).getAsJsonObject().get("source").getAsString(),type,medias));
                }
                modulesAdaptor.updateModules(modulesList);
                modulesAdaptor.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}