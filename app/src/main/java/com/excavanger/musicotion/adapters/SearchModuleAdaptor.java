package com.excavanger.musicotion.adapters;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.excavanger.musicotion.R;
import com.excavanger.musicotion.activites.LanguageActivity;
import com.excavanger.musicotion.activites.MainActivity;
import com.excavanger.musicotion.activites.SearchAllActivity;
import com.excavanger.musicotion.fragments.MoreFragment;
import com.excavanger.musicotion.fragments.SeeAllFragment;
import com.excavanger.musicotion.models.SearchModulesModel;

import java.util.List;

public class SearchModuleAdaptor extends RecyclerView.Adapter<SearchModuleAdaptor.SearchModuleHolder> {
    private List<SearchModulesModel> searchModelList;
    public String query;
    public Context context;
    public Activity activity;
    public SearchModuleAdaptor(List<SearchModulesModel> searchSodulesList, Context context,Activity activity) {
        this.searchModelList = searchSodulesList;
        this.context = context;
        this.activity = activity;
        this.query = "";
    }

    @NonNull
    @Override
    public SearchModuleAdaptor.SearchModuleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_songs, parent, false);
        return new SearchModuleAdaptor.SearchModuleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  SearchModuleAdaptor.SearchModuleHolder holder, int position) {
        holder.title.setText(searchModelList.get(position).getTitle());
        if(searchModelList.get(position).getTitle().equalsIgnoreCase("songs")){
            holder.viewAll.setVisibility(View.VISIBLE);
        }else{
            holder.viewAll.setVisibility(View.GONE);
        }
        SearchResultAdaptor searchResultAdaptor = new SearchResultAdaptor(searchModelList.get(position).getSearchModelList(), context);
        holder.searchResultRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.searchResultRecycler.setAdapter(searchResultAdaptor);
        searchResultAdaptor.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return searchModelList.size();
    }

    public void updateModules(List<SearchModulesModel> modulesModels,String query){
        this.searchModelList = modulesModels;
        this.query = query;
    }

    public interface ViewAllSelectListener{
        void viewAllSelected();
    }


    class SearchModuleHolder extends RecyclerView.ViewHolder {
        public TextView title,viewAll;
        public RecyclerView searchResultRecycler;

        public SearchModuleHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.search_title);
            viewAll = itemView.findViewById(R.id.search_all);
            searchResultRecycler = itemView.findViewById(R.id.search_result_recycler);
            viewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new SeeAllFragment();
                    Fragment fragmentTemp = activity.getSupportFragmentManager().findFragmentByTag("4");
                    Fragment currentragment = activity.getSupportFragmentManager().getPrimaryNavigationFragment();
                    activity.getSupportFragmentManager().beginTransaction().hide(currentragment);
                    if (fragmentTemp == null) {
                        activity.getSupportFragmentManager().beginTransaction().add(R.id.content_main, myFragment, "4").commit();
                    } else {
                        activity.getSupportFragmentManager().beginTransaction().show(fragmentTemp);
                    }

                    //activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment).addToBackStack("2").commit();
                }
            });
        }


    }
}
