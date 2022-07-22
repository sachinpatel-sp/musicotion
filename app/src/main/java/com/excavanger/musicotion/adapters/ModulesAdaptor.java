package com.excavanger.musicotion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.excavanger.musicotion.R;
import com.excavanger.musicotion.models.MediaModel;
import com.excavanger.musicotion.models.ModulesModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ModulesAdaptor extends RecyclerView.Adapter<ModulesAdaptor.ModulesHolder> {
    private List<ModulesModel> modulesModelList;
    public Context context;
    public ModulesAdaptor(List<ModulesModel> modulesModelList, Context context) {
        this.modulesModelList = modulesModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ModulesAdaptor.ModulesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.trending_now_layout, parent, false);
        return new ModulesAdaptor.ModulesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ModulesAdaptor.ModulesHolder holder, int position) {
        holder.title.setText(modulesModelList.get(position).getTitle());
        holder.subtitle.setText(modulesModelList.get(position).getSubTitle());
        if(modulesModelList.get(position).getType() == 1){
            TrendingAdaptor newAlbumsAdaptor = new TrendingAdaptor(modulesModelList.get(position).getMedialModelList(),context);
                GridLayoutManager layoutManager = new GridLayoutManager(context,
                        2, GridLayoutManager.HORIZONTAL, false);
                if(modulesModelList.get(position).getMedialModelList().size() >20)
                    holder.mediaRecycler.setLayoutManager(layoutManager);
                else
                    holder.mediaRecycler.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
                holder.mediaRecycler.setAdapter(newAlbumsAdaptor);
                newAlbumsAdaptor.notifyDataSetChanged();

        }else if(modulesModelList.get(position).getType() == 2){
            ChartsAdaptor chartsAdaptor = new ChartsAdaptor(modulesModelList.get(position).getMedialModelList(), context);
            holder.mediaRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            SnapHelper snapHelper = new PagerSnapHelper();
            //snapHelper.attachToRecyclerView(holder.mediaRecycler);
            holder.mediaRecycler.setAdapter(chartsAdaptor);
            chartsAdaptor.notifyDataSetChanged();
        }else{
            RadioAdaptor featuredRadioAdaptor = new RadioAdaptor(modulesModelList.get(position).getMedialModelList(),context);
                GridLayoutManager layoutManager = new GridLayoutManager(context,
                        2, GridLayoutManager.HORIZONTAL, false);
                if(modulesModelList.get(position).getMedialModelList().size() > 20)
                    holder.mediaRecycler.setLayoutManager(layoutManager);
                else{
                    holder.mediaRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                }
                holder.mediaRecycler.setAdapter(featuredRadioAdaptor);
                featuredRadioAdaptor.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return modulesModelList.size();
    }

    public void updateModules(List<ModulesModel> modulesModels){
        this.modulesModelList = modulesModels;
    }

    public interface ChartsSelectedListener {
        void onChartsSelected(MediaModel media);
    }

    class ModulesHolder extends RecyclerView.ViewHolder {
        public TextView title,subtitle;
        public RecyclerView mediaRecycler;

        public ModulesHolder(@NonNull View itemView) {
            super(itemView);
            subtitle = itemView.findViewById(R.id.module_subtitle);
            title = itemView.findViewById(R.id.module_title);
            mediaRecycler = itemView.findViewById(R.id.trending_now_recycler);
        }


    }
}
