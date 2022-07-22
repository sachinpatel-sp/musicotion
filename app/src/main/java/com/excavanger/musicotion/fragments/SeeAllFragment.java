package com.excavanger.musicotion.fragments;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.excavanger.musicotion.R;
import com.excavanger.musicotion.activites.SearchAllActivity;
import com.excavanger.musicotion.adapters.TrendingAdaptor;
import com.excavanger.musicotion.models.MediasModel;
import com.excavanger.musicotion.networkcalls.NetworkInterface;
import com.excavanger.musicotion.utils.Common;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeeAllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeeAllFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<MediasModel> allSongs;
    private RecyclerView recyclerView;
    private TrendingAdaptor allSongsAdaptor;
    private TextView searchedTitle;
    private ImageView backBtn;

    public SeeAllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeeAllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeeAllFragment newInstance(String param1, String param2) {
        SeeAllFragment fragment = new SeeAllFragment();
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
        View view = inflater.inflate(R.layout.fragment_see_all, container, false);
        allSongs = new ArrayList<>();
        recyclerView = view.findViewById(R.id.all_songs);
        searchedTitle = view.findViewById(R.id.searched_title);
        backBtn = view.findViewById(R.id.back_btn);
        allSongsAdaptor = new TrendingAdaptor(allSongs, getContext());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),
                2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(allSongsAdaptor);
        searchedTitle.setText(Common.query+" in songs");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment myFragment = getActivity().getSupportFragmentManager().getPrimaryNavigationFragment();
                getActivity().getSupportFragmentManager().beginTransaction().hide(myFragment);

            }
        });
        LoadAllSongs(Common.query);
        return view;
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
                allSongsAdaptor.updateList(allSongs);
                allSongsAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("a",call.request().toString());
            }
        });
    }
}