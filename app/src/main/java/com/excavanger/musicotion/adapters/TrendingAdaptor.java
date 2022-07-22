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
import com.excavanger.musicotion.models.MediasModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrendingAdaptor extends RecyclerView.Adapter<TrendingAdaptor.ViewHolder> {
    private List<MediasModel> mediaModelList;
    public Context context;
    private final MediaSelectedListener mediaSelectedListener;
    public TrendingAdaptor(List<MediasModel> mediaModelList, Context context) {
        this.mediaModelList = mediaModelList;
        this.context = context;
        mediaSelectedListener = (MediaSelectedListener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.playlist_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().setLoggingEnabled(true);
        if(mediaModelList.get(position).getImage().length() > 0) {
            Picasso.get().load(mediaModelList.get(position).getImage()).into(holder.mediaImg);
        }
        holder.title.setText(mediaModelList.get(position).getTitle());
        holder.subtitle.setText(mediaModelList.get(position).getSubtitle());
    }

    @Override
    public int getItemCount() {
        return mediaModelList.size();
    }

    public interface MediaSelectedListener {
        void onMediaSelected(MediasModel media);
    }

    public void updateList(List<MediasModel> mediaModelList){
        this.mediaModelList = mediaModelList;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView mediaImg;
        public TextView title, subtitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mediaImg = itemView.findViewById(R.id.media_image);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mediaSelectedListener.onMediaSelected(mediaModelList.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}

