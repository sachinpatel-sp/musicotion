package com.excavanger.musicotion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.excavanger.musicotion.R;
import com.excavanger.musicotion.models.MediaModel;
import com.excavanger.musicotion.models.SearchModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchResultAdaptor extends RecyclerView.Adapter<SearchResultAdaptor.ViewHolder> {
    private List<SearchModel> mediaModelList;
    public Context context;
    private final SearchResultSelectedListener searchResultSelectedListener;
    public SearchResultAdaptor(List<SearchModel> mediaModelList, Context context) {
        this.mediaModelList = mediaModelList;
        this.context = context;
        searchResultSelectedListener = (SearchResultSelectedListener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.queue_song_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(mediaModelList.get(position).getImage()).into(holder.mediaImg);
        holder.title.setText(mediaModelList.get(position).getTitle());
        holder.subtitle.setText(mediaModelList.get(position).getDesc());
    }

    public void setMediaModelList(List<SearchModel> mediaModelList){
        this.mediaModelList = mediaModelList;
    }

    @Override
    public int getItemCount() {
        return mediaModelList.size();
    }

    public interface SearchResultSelectedListener {
        void onSearchItemSelected(SearchModel media);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView mediaImg;
        public TextView title, subtitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mediaImg = itemView.findViewById(R.id.queue_song_image);
            title = itemView.findViewById(R.id.queue_song_title);
            subtitle = itemView.findViewById(R.id.queue_song_subtitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           searchResultSelectedListener.onSearchItemSelected(mediaModelList.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
