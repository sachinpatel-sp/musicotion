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

import org.web3j.abi.datatypes.primitive.Char;

import java.util.List;

public class ChartsAdaptor extends RecyclerView.Adapter<ChartsAdaptor.ChartsHolder> {
    private List<MediasModel> chartModelList;
    public Context context;
    private final ChartsAdaptor.ChartsSelectedListener chartsSelectedListener;
    public ChartsAdaptor(List<MediasModel> mediaModelList, Context context) {
        this.chartModelList = mediaModelList;
        this.context = context;
        chartsSelectedListener = (ChartsSelectedListener) context;
    }

    @NonNull
    @Override
    public ChartsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.charts_layout, parent, false);
        return new ChartsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartsHolder holder, int position) {
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(chartModelList.get(position).getImage()).into(holder.mediaImg);
        holder.title.setText(chartModelList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return chartModelList.size();
    }

    public interface ChartsSelectedListener {
        void onChartsSelected(MediasModel media);
    }

    class ChartsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView mediaImg;
        public TextView title;

        public ChartsHolder(@NonNull View itemView) {
            super(itemView);
            mediaImg = itemView.findViewById(R.id.media_image);
            title = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            chartsSelectedListener.onChartsSelected(chartModelList.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }

    }
}
