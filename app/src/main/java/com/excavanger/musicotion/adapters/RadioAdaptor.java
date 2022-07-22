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
import com.excavanger.musicotion.models.MediasModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RadioAdaptor extends RecyclerView.Adapter<RadioAdaptor.RadioHolder> {
    private List<MediasModel> artistModelList;
    public Context context;
    private final RadioAdaptor.RadioSelectedListener radioSelectedListener;
    public RadioAdaptor(List<MediasModel> artistModelList, Context context) {
        this.artistModelList = artistModelList;
        this.context = context;
        this.radioSelectedListener = (RadioSelectedListener) context;
    }

    @NonNull
    @Override
    public RadioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.circular_layout, parent, false);
        return new RadioHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadioHolder holder, int position) {
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(artistModelList.get(position).getImage()).into(holder.artistImg);
        holder.name.setText(artistModelList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return artistModelList.size();
    }

    public interface RadioSelectedListener {
        void onRadioSelected(MediasModel media);
    }
    class RadioHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView artistImg;
        public TextView name;

        public RadioHolder(@NonNull View itemView) {
            super(itemView);
            artistImg = itemView.findViewById(R.id.artist_image_holder);
            name= itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            radioSelectedListener.onRadioSelected(artistModelList.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
