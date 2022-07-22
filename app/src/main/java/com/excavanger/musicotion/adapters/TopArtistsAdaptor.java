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
import com.excavanger.musicotion.models.CircularModel;
import com.excavanger.musicotion.models.MediaModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopArtistsAdaptor extends RecyclerView.Adapter<TopArtistsAdaptor.TopArtistsHolder> {
    private List<CircularModel> artistModelList;
    public Context context;
    private final TopArtistsAdaptor.ArtistSelectedListener artistSelectedListener;
    public TopArtistsAdaptor(List<CircularModel> artistModelList, Context context) {
        this.artistModelList = artistModelList;
        this.context = context;
        this.artistSelectedListener = (ArtistSelectedListener) context;
    }

    @NonNull
    @Override
    public TopArtistsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.circular_layout, parent, false);
        return new TopArtistsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopArtistsHolder holder, int position) {
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(artistModelList.get(position).getImage()).into(holder.artistImg);
        holder.name.setText(artistModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return artistModelList.size();
    }

    public interface ArtistSelectedListener {
        void onArtistSelected(CircularModel media);
    }
    class TopArtistsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView artistImg;
        public TextView name;

        public TopArtistsHolder(@NonNull View itemView) {
            super(itemView);
            artistImg = itemView.findViewById(R.id.artist_image_holder);
            name= itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            artistSelectedListener.onArtistSelected(artistModelList.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
