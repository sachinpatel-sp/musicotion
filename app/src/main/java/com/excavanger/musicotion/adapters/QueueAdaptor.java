package com.excavanger.musicotion.adapters;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.excavanger.musicotion.R;
import com.excavanger.musicotion.models.MediaModel;
import com.excavanger.musicotion.models.MediasModel;
import com.excavanger.musicotion.utils.CustomItemTouchHelper;
import com.excavanger.musicotion.utils.ItemTouchHelperAdaptor;
import com.squareup.picasso.Picasso;

import java.util.List;

public class QueueAdaptor extends RecyclerView.Adapter<QueueAdaptor.ViewHolder> implements ItemTouchHelperAdaptor{

    private ItemTouchHelper mTouchHelper;
    private List<MediasModel> mediaModelList;
    public Context context;
    private final QueueMediaSelectedListener queueMediaSelectedListener;
    private final UpdateSongNumber updateSongNumber;
    public QueueAdaptor(List<MediasModel> mediaModelList, Context context) {
        this.mediaModelList = mediaModelList;
        this.context = context;
        queueMediaSelectedListener = (QueueMediaSelectedListener) context;
        updateSongNumber = (UpdateSongNumber) context;
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
        holder.subtitle.setText(mediaModelList.get(position).getSubtitle());
    }

    public void setMediaModelList(List<MediasModel> mediaModelList){
        this.mediaModelList = mediaModelList;
    }

    public interface UpdateSongNumber{
        void updateCurrSong(int from,int to,int type);
    }

    @Override
    public int getItemCount() {
        return mediaModelList.size();
    }

    public interface QueueMediaSelectedListener {
        void onQueueMediaSelected(MediasModel media,int position);
    }
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        MediasModel fromMedia = mediaModelList.get(fromPosition);
        mediaModelList.remove(fromMedia);
        mediaModelList.add(toPosition,fromMedia);
        notifyItemMoved(fromPosition,toPosition);
        updateSongNumber.updateCurrSong(fromPosition,toPosition,1);
    }

    @Override
    public void onItemSwiped(int position) {
        mediaModelList.remove(position);
        notifyItemRemoved(position);
        updateSongNumber.updateCurrSong(position,-1,0);
    }

    public void setmTouchHelper(ItemTouchHelper touchHelper){
        this.mTouchHelper = touchHelper;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener, GestureDetector.OnGestureListener {
        public ImageView mediaImg;
        public TextView title, subtitle;
        GestureDetector mGestureDetector;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mediaImg = itemView.findViewById(R.id.queue_song_image);
            title = itemView.findViewById(R.id.queue_song_title);
            subtitle = itemView.findViewById(R.id.queue_song_subtitle);
            mGestureDetector = new GestureDetector(itemView.getContext(),this);
            itemView.setOnClickListener(this);
            itemView.setOnTouchListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            queueMediaSelectedListener.onQueueMediaSelected(mediaModelList.get(getAdapterPosition()),getAdapterPosition());
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            mTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mGestureDetector.onTouchEvent(motionEvent);
            return true;
        }
    }

}

