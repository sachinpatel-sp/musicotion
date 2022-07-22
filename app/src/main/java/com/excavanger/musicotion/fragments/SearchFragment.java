package com.excavanger.musicotion.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.excavanger.musicotion.R;
import com.excavanger.musicotion.adapters.RadioAdaptor;
import com.excavanger.musicotion.adapters.SearchModuleAdaptor;
import com.excavanger.musicotion.adapters.SearchResultAdaptor;
import com.excavanger.musicotion.models.CircularModel;
import com.excavanger.musicotion.models.SearchModel;
import com.excavanger.musicotion.models.SearchModulesModel;
import com.excavanger.musicotion.networkcalls.NetworkInterface;
import com.excavanger.musicotion.utils.Common;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jnr.ffi.annotations.In;
import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ProgressBar searchProgress;
    private ImageView searchLogo;
    private EditText searchEdit;
    private Handler handler;
    private List<SearchModel> topQuery,searchArtist,searchAlbums,searchSongs,searchPlaylists,topSearches;
    private RecyclerView searchRecycler;
    private SearchModuleAdaptor searchModuleAdaptor;
    private List<SearchModulesModel> searchModulesModelList;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchProgress = view.findViewById(R.id.search_progress);
        searchLogo = view.findViewById(R.id.search_icon);
        searchEdit = view.findViewById(R.id.search_field);
        topQuery = new ArrayList<>();
        searchArtist = new ArrayList<>();
        searchAlbums = new ArrayList<>();
        searchSongs = new ArrayList<>();
        searchPlaylists = new ArrayList<>();
        topSearches = new ArrayList<>();
        searchModulesModelList = new ArrayList<>();
        searchModuleAdaptor = new SearchModuleAdaptor(searchModulesModelList,getContext(),getActivity());
        searchRecycler = view.findViewById(R.id.search_recycler);
        searchRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        searchRecycler.setAdapter(searchModuleAdaptor);
        searchModuleAdaptor.notifyDataSetChanged();
        LoadTopSearch();
        handler = new Handler();
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchLogo.setVisibility(View.INVISIBLE);
                searchProgress.setVisibility(View.VISIBLE);
                String query = charSequence.toString();
                searchModulesModelList.clear();
                if(query.length() > 0) {
                    topQuery.clear();
                    searchArtist.clear();
                    searchAlbums.clear();
                    searchSongs.clear();
                    searchPlaylists.clear();
                    LoadSearchResult(query);
                }else{
                    searchModulesModelList.add(new SearchModulesModel("Top Searches",topSearches));
                    searchModuleAdaptor.updateModules(searchModulesModelList,query);
                    searchModuleAdaptor.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                    Log.e("aa",editable.toString());
            }
        });

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){

        }else{
            //LoadTopSearch();
        }
    }

    private void LoadSearchResult(String query){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit= Common.getClient();
                Common.query = query;
                HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?__call=autocomplete.get&query="+query+"&_format=json&_marker=0&ctx=web6dot0");
                NetworkInterface api = retrofit.create(NetworkInterface.class);
                Call<JsonObject> call = api.getQueryResult(uri.toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        topQuery.clear();
                        searchArtist.clear();
                        searchSongs.clear();
                        searchAlbums.clear();
                        searchPlaylists.clear();
                        searchModulesModelList.clear();
                        JsonArray arr = response.body().get("topquery").getAsJsonObject().get("data").getAsJsonArray();
                        //Log.e("as",arr.toString());
                        JsonObject obj;
                        int n = arr.size();
                        for(int i=0;i<n;i++){
                            obj=arr.get(i).getAsJsonObject();
                                topQuery.add(new SearchModel(obj.get("id").getAsString(),obj.get("title").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),obj.get("url").getAsString(),obj.get("description").getAsString(),obj.get("type").getAsString()));
                        }
                        if(topQuery.size() > 0){
                            searchModulesModelList.add(new SearchModulesModel("Top Query",topQuery));
                        }
                        arr = response.body().get("artists").getAsJsonObject().get("data").getAsJsonArray();
                        n = arr.size();
                        for(int i=0;i<n;i++){
                            obj=arr.get(i).getAsJsonObject();
                            searchArtist.add(new SearchModel(obj.get("id").getAsString(),obj.get("title").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),obj.get("url").getAsString(),obj.get("description").getAsString(),obj.get("type").getAsString()));
                        }
                        if(searchArtist.size() > 0){
                            searchModulesModelList.add(new SearchModulesModel("Artists",searchArtist));
                        }

                        arr = response.body().get("albums").getAsJsonObject().get("data").getAsJsonArray();
                        n = arr.size();
                        for(int i=0;i<n;i++){
                            obj=arr.get(i).getAsJsonObject();
                            searchAlbums.add(new SearchModel(obj.get("id").getAsString(),obj.get("title").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),obj.get("url").getAsString(),obj.get("description").getAsString(),obj.get("type").getAsString()));
                        }
                        if(searchAlbums.size() > 0){
                            searchModulesModelList.add(new SearchModulesModel("Albums",searchAlbums));
                        }

                        arr = response.body().get("songs").getAsJsonObject().get("data").getAsJsonArray();
                        n = arr.size();
                        for(int i=0;i<n;i++){
                            obj=arr.get(i).getAsJsonObject();
                            searchSongs.add(new SearchModel(obj.get("id").getAsString(),obj.get("title").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),obj.get("url").getAsString(),obj.get("description").getAsString(),obj.get("type").getAsString()));
                        }
                        if(searchSongs.size() > 0){
                            searchModulesModelList.add(new SearchModulesModel("Songs",searchSongs));
                        }

                        arr = response.body().get("playlists").getAsJsonObject().get("data").getAsJsonArray();
                        n = arr.size();
                        for(int i=0;i<n;i++){
                            obj=arr.get(i).getAsJsonObject();
                            searchPlaylists.add(new SearchModel(obj.get("id").getAsString(),obj.get("title").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),obj.get("url").getAsString(),obj.get("description").getAsString(),obj.get("type").getAsString()));
                        }

                        if(searchPlaylists.size() > 0){
                            searchModulesModelList.add(new SearchModulesModel("Playlists",searchPlaylists));
                        }

                        searchProgress.setVisibility(View.INVISIBLE);
                        searchLogo.setVisibility(View.VISIBLE);
                        searchModuleAdaptor.updateModules(searchModulesModelList,query);
                        searchModuleAdaptor.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("a",call.request().toString());
                    }
                });
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void LoadTopSearch(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit= Common.getClient();
                NetworkInterface api=retrofit.create(NetworkInterface.class);
                Call<JsonArray> call=api.topSearch();
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        JsonArray arr=response.body().getAsJsonArray();
                        int n=arr.size();
                        JsonObject obj;
                        for(int i=0;i<n;i++){
                            obj=arr.get(i).getAsJsonObject();
                            topSearches.add(new SearchModel(obj.get("id").getAsString(),obj.get("title").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),obj.get("perma_url").getAsString(),obj.get("subtitle").getAsString(),obj.get("type").getAsString()));
                        }
                        searchModulesModelList.clear();
                        searchModulesModelList.add(new SearchModulesModel("Top Searches",topSearches));
                        searchModuleAdaptor.updateModules(searchModulesModelList,"");
                        searchModuleAdaptor.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

}