package com.excavanger.musicotion.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.health.SystemHealthManager;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.excavanger.musicotion.PlaybackInfoListener;
import com.excavanger.musicotion.R;
import com.excavanger.musicotion.Song;
import com.excavanger.musicotion.adapters.ChartsAdaptor;
import com.excavanger.musicotion.adapters.QueueAdaptor;
import com.excavanger.musicotion.adapters.RadioAdaptor;
import com.excavanger.musicotion.adapters.SearchResultAdaptor;
import com.excavanger.musicotion.adapters.TopArtistsAdaptor;
import com.excavanger.musicotion.adapters.TrendingAdaptor;
import com.excavanger.musicotion.fragments.DiscoverFragment;
import com.excavanger.musicotion.fragments.MoreFragment;
import com.excavanger.musicotion.fragments.SearchFragment;
import com.excavanger.musicotion.models.CircularModel;
import com.excavanger.musicotion.models.MediaModel;
import com.excavanger.musicotion.models.MediasModel;
import com.excavanger.musicotion.models.SearchModel;
import com.excavanger.musicotion.networkcalls.NetworkInterface;
import com.excavanger.musicotion.utils.Common;
import com.excavanger.musicotion.utils.CustomItemTouchHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jnr.ffi.Struct;
import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity implements TrendingAdaptor.MediaSelectedListener, ChartsAdaptor.ChartsSelectedListener,TopArtistsAdaptor.ArtistSelectedListener, RadioAdaptor.RadioSelectedListener, SearchResultAdaptor.SearchResultSelectedListener, QueueAdaptor.QueueMediaSelectedListener,QueueAdaptor.UpdateSongNumber {
    final Fragment discoverFragment = new DiscoverFragment();
    final Fragment searchFragment = new SearchFragment();
    final Fragment moreFragment = new MoreFragment();
    private TabLayout tabLayout;
    private ChipNavigationBar chipNavigationBar;
    private RelativeLayout upperControlLayout;
    private SlidingUpPanelLayout slidingPaneLayout;
    private MediaPlayer mediaPlayer;
    private ImageView mediaImg,playPause,songImg,songPlayPause,prevBtn,nextBtn,repeatView,downloadView;
    private TextView mediaTitle,mediaSubTitle,songTitle,songSubtitle,currDuration,totalDuration,lyricsTv;
    private ProgressBar songProgressTop;
    private SeekBar songSeekBar;
    private View topControl;
    private Boolean isPlaying;
    private LinearLayout playerTitle;
    private QueueAdaptor queueAdaptor;
    private List<MediasModel> songQueue;
    private RecyclerView songQueueRecycler;
    private Handler handler;
    private String currentSongId;
    private Integer currSongNumber = -1;
    private ProgressBar progressBar,progressBar2;
    private int bottomBarHeight,topControlHeight;
    private String lang,quality;
    private LinearLayoutManager linearLayoutManager;
    private ProgressDialog progressDialog;
    final DownloadTask downloadTask = new DownloadTask(HomeActivity.this);
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private int repeat = 0;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(slidingPaneLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
            slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return;
        }
//        if(mediaPlayer != null){
//            handler.removeCallbacksAndMessages(null);
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
        this.moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            handler.removeCallbacksAndMessages(null);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    Fragment active = discoverFragment;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        lang = Common.language;
        quality = Common.quality;
        chipNavigationBar = (ChipNavigationBar) findViewById(R.id.nav_view);
        upperControlLayout = findViewById(R.id.top_control);
        upperControlLayout.setBackgroundColor(ColorUtils.setAlphaComponent(getResources().getColor(R.color.red_A400), 10));
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_main,moreFragment,"3").hide(moreFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_main,searchFragment,"2").hide(searchFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_main,discoverFragment,"1").commit();
        getViews();
        chipNavigationBar.setItemSelected(R.id.discover,true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.discover:
                        if(active!=discoverFragment)
                            setFragment(discoverFragment);
                        break;
                    case R.id.search:
                        if(active!=searchFragment)
                            setFragment(searchFragment);
                        break;
                    case R.id.more:
                        if(active!=moreFragment)
                            setFragment(moreFragment);
                        break;
                }
            }
        });
    }

    private void getViews(){
        mediaPlayer = null;
        isPlaying = false;
        mediaImg = findViewById(R.id.track_image);
        mediaTitle = findViewById(R.id.track_title);
        mediaSubTitle = findViewById(R.id.track_subtitle);
        playPause = findViewById(R.id.play_pause);
        songProgressTop = findViewById(R.id.song_progress_top);
        topControl = findViewById(R.id.top_control_include);
        songImg =findViewById(R.id.song_image);
        songTitle = findViewById(R.id.song_title);
        songSubtitle = findViewById(R.id.song_subtitle);
        songPlayPause = findViewById(R.id.song_play_pause);
        songSeekBar = findViewById(R.id.seekBar);
        currDuration = findViewById(R.id.current_duration);
        totalDuration = findViewById(R.id.total_duration);
        songQueueRecycler = findViewById(R.id.queue_recycler);
        prevBtn = findViewById(R.id.queue_prev_btn);
        nextBtn = findViewById(R.id.queue_next_btn);
        playerTitle = findViewById(R.id.player_title);
        progressBar = findViewById(R.id.progress);
        progressBar2 = findViewById(R.id.progress2);
        tabLayout = findViewById(R.id.tab);
        lyricsTv = findViewById(R.id.lyrics_tv);
        repeatView = findViewById(R.id.repeat);
        downloadView = findViewById(R.id.download);
        mediaSubTitle.setSelected(true);
        songSubtitle.setSelected(true);
        bottomBarHeight = 179;
        topControlHeight = 170;
        slidingPaneLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        handler = new Handler();
        currentSongId = "";
        songQueue = new ArrayList<>();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.e("hg",String.valueOf(position));
                switch (position) {
                    case 0:
                        songQueueRecycler.setVisibility(View.VISIBLE);
                        lyricsTv.setVisibility(View.GONE);
                        break;
                    case 1:
                        songQueueRecycler.setVisibility(View.GONE);
                        lyricsTv.setVisibility(View.VISIBLE);
                        showLyricsDialog();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        repeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeat = (repeat+1)%3;
                if(repeat == 0){
                    repeatView.setImageResource(R.drawable.ic_no_repeat);
                }else if(repeat == 1){
                    repeatView.setImageResource(R.drawable.ic_repeat_one);
                }else{
                    repeatView.setImageResource(R.drawable.ic_repeat);
                }
            }
        });

        downloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer == null){
                    Toast.makeText(getApplicationContext(),"Please select a song!",Toast.LENGTH_SHORT).show();
                }else{
                    downloadFile(songQueue.get(currSongNumber));
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlaying = false;
                playPause.setImageResource(R.drawable.ic_play);
                songPlayPause.setImageResource(R.drawable.ic_play_circle_filled);
                PauseMusic();
                if(currSongNumber >= songQueue.size()){
                    Toast.makeText(getApplicationContext(),"Queue Ended!",Toast.LENGTH_SHORT).show();
                }
                if(currSongNumber < songQueue.size()-1){
                        View prevView = linearLayoutManager.getChildAt(currSongNumber);
                        if(prevView != null)
                             ((TextView)prevView.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));

                    currSongNumber++;
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar2.setVisibility(View.VISIBLE);
                    PlayNext();
                }
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlaying = false;
                playPause.setImageResource(R.drawable.ic_play);
                songPlayPause.setImageResource(R.drawable.ic_play_circle_filled);
                if(mediaPlayer != null)
                    PauseMusic();
                if(currSongNumber > 0){
                    if(currSongNumber < songQueue.size()) {
                        View prevView = linearLayoutManager.getChildAt(currSongNumber);
                        if(prevView != null)
                            ((TextView) prevView.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    }
                    currSongNumber--;
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar2.setProgress(View.VISIBLE);
                    PlayNext();
                }
            }
        });
        songPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    PauseMusic();
                }else{
                    ResumeMusic();
                }
            }
        });
        slidingPaneLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                topControl.setAlpha(1-slideOffset);
                playerTitle.setAlpha(slideOffset);
                ViewGroup.LayoutParams layoutParams = chipNavigationBar.getLayoutParams();
                layoutParams.height = (int)((1-slideOffset)*bottomBarHeight);
                chipNavigationBar.setLayoutParams(layoutParams);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState == SlidingUpPanelLayout.PanelState.EXPANDED ){
                        playPause.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                }else if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    playPause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(isPlaying){
                                PauseMusic();
                            }else{
                                ResumeMusic();
                            }
                        }
                    });
                }

            }
        });

        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer != null && b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Downloading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true); //cancel the task
            }
        });

        slidingPaneLayout.setPanelHeight(0);
        queueAdaptor = new QueueAdaptor(songQueue,this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        songQueueRecycler.setLayoutManager(linearLayoutManager);
        ItemTouchHelper.Callback callback = new CustomItemTouchHelper(queueAdaptor);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        queueAdaptor.setmTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(songQueueRecycler);
        songQueueRecycler.setAdapter(queueAdaptor);
        loadWeeklyTop();

    }

    private void setFragment(Fragment fragment){
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).hide(active).show(fragment).commit();
        active = fragment;
    }

    @Override
    public void onQueueMediaSelected(MediasModel media, int position) {
        PauseMusic();
        songQueueRecycler.smoothScrollToPosition(position);
        if(currSongNumber>=0 && currSongNumber<songQueue.size()) {
            View prevView = linearLayoutManager.getChildAt(currSongNumber);
            if(prevView != null)
                ((TextView)prevView.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        }
        View view = linearLayoutManager.getChildAt(position);
        if(view!=null)
            ((TextView)view.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red_A400));
            if(!currentSongId.equalsIgnoreCase(media.getId())) {
                progressBar2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                currentSongId = media.getId();
                //Log.e("wda",String.valueOf(queueAdaptor.getItemCount()));
                quality = Common.quality;
                String token = Common.urlDecoder(media.getMedia_url());
                Retrofit retrofit = Common.getClient();
                HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?__call=song.generateAuthToken&url=" + token + "&bitrate="+quality+"&api_version=4&_format=json&ctx=web6dot0&_marker=0");
                NetworkInterface api = retrofit.create(NetworkInterface.class);
                Call<JsonObject> call = api.songUrl(uri.toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        String uri = response.body().get("auth_url").getAsString();
                        Log.e("url",uri);
                        currSongNumber = position;
                        if (mediaPlayer == null) {
                            handler.removeCallbacksAndMessages(null);
                            StartMusic(uri, media);
                        } else {
                            handler.removeCallbacksAndMessages(null);
                            mediaPlayer.release();
                            mediaPlayer = null;
                            //mediaPlayer = new MediaPlayer();
                            StartMusic(uri, media);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("error", call.request().url().toString());
                    }
                });
            }
    }

    @Override
    public void onMediaSelected(MediasModel media) {
        //currSongNumber = 0;
        String type = media.getType();
        quality = Common.quality;
        if(type.equalsIgnoreCase("song")) {
            if(!currentSongId.equalsIgnoreCase(media.getId())){
                progressBar2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                songQueue.clear();
                if(media.getMedia_url().length() == 0){
                    loadSongWithToken(media);
                }else {
                    songQueue.add(media);
                    loadSimilarSong(media);
                    currentSongId = media.getId();
                    //Log.e("wda",String.valueOf(queueAdaptor.getItemCount()));
                    String token = Common.urlDecoder(media.getMedia_url());
                    Retrofit retrofit = Common.getClient();
                    HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?__call=song.generateAuthToken&url=" + token + "&bitrate="+quality+"&api_version=4&_format=json&ctx=web6dot0&_marker=0");
                    NetworkInterface api = retrofit.create(NetworkInterface.class);
                    Call<JsonObject> call = api.songUrl(uri.toString());
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            String uri = response.body().get("auth_url").getAsString();
                            currSongNumber = 0;
                            View prevView = linearLayoutManager.getChildAt(currSongNumber);
                            if(prevView != null)
                                ((TextView)prevView.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red_A400));

                            if (mediaPlayer == null) {
                                handler.removeCallbacksAndMessages(null);
                                StartMusic(uri, media);
                            } else {
                                handler.removeCallbacksAndMessages(null);
                                mediaPlayer.release();
                                mediaPlayer = null;
                                //mediaPlayer = new MediaPlayer();
                                StartMusic(uri, media);
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("error", call.request().url().toString());
                        }
                    });
                }
            }
        }else if(type.equalsIgnoreCase("playlist")){
            Toast.makeText(getApplicationContext(),"Playing "+media.getTitle(),Toast.LENGTH_SHORT).show();
            if(isPlaying)
                PauseMusic();
            progressBar.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
            songQueue.clear();
            LoadPlaylist(media);
        }else if(type.equalsIgnoreCase("album")){
            Toast.makeText(getApplicationContext(),"Playing "+media.getTitle(),Toast.LENGTH_SHORT).show();
            if(isPlaying)
                PauseMusic();
            progressBar.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
            songQueue.clear();
            LoadAlbum(media);
        }else {
            Toast.makeText(getApplicationContext(),"Starting Radio "+media.getTitle(),Toast.LENGTH_SHORT).show();
            if(isPlaying)
                PauseMusic();
            progressBar.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
            songQueue.clear();
            LoadRadioSongs(media);
        }
    }

    @Override
    public void onChartsSelected(MediasModel media) {
        Toast.makeText(getApplicationContext(),"Playing "+media.getTitle(),Toast.LENGTH_SHORT).show();
        if(isPlaying)
            PauseMusic();
        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        songQueue.clear();
        LoadPlaylist(media);
    }

    @Override
    public void onRadioSelected(MediasModel media) {
        Toast.makeText(getApplicationContext(),"Starting Radio "+media.getTitle(),Toast.LENGTH_SHORT).show();
        if(isPlaying)
            PauseMusic();
        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        songQueue.clear();
        LoadRadioSongs(media);
    }

    @Override
    public void onArtistSelected(CircularModel artist) {
        Toast.makeText(getApplicationContext(),"Playing Top Songs Of "+artist.getName(),Toast.LENGTH_SHORT).show();
        if(isPlaying)
            PauseMusic();
        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        songQueue.clear();
        LoadArtistSong(artist);
    }

    @Override
    public void onSearchItemSelected(SearchModel media) {
        String type = media.getType();
        if(type.equalsIgnoreCase("playlist")){
            Toast.makeText(getApplicationContext(),"Playing "+media.getTitle(),Toast.LENGTH_SHORT).show();
            if(isPlaying)
                PauseMusic();
            progressBar.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
            songQueue.clear();
            LoadPlaylist(new MediasModel(media.getId(),media.getTitle(),media.getDesc(),media.getType(),media.getUrl(),media.getImage(),"","hindi"));
        }else if(type.equalsIgnoreCase("album")){
            Toast.makeText(getApplicationContext(),"Playing "+media.getTitle(),Toast.LENGTH_SHORT).show();
            if(isPlaying)
                PauseMusic();
            progressBar.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
            songQueue.clear();
            LoadAlbum(new MediasModel(media.getId(),media.getTitle(),media.getDesc(),media.getType(),media.getUrl(),media.getImage(),"","hindi"));
        }else if(type.equalsIgnoreCase("artist")){
            Toast.makeText(getApplicationContext(),"Playing Top Songs Of "+media.getTitle(),Toast.LENGTH_SHORT).show();
            if(isPlaying)
                PauseMusic();
            progressBar.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
            songQueue.clear();
            LoadArtistSong(new CircularModel(media.getId(),media.getTitle(),media.getImage(),media.getUrl(),0,""));
        }else if(type.equalsIgnoreCase("song")){
            PlaySearchedSong(media);
        }
    }

    private void PlaySearchedSong(SearchModel media){
        Retrofit retrofit= Common.getClient();
        //Log.e("ad",media.getPerma_url());
        String token = Common.getToken(media.getUrl());
        HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?__call=webapi.get&token="+token+"&type=song&p=1&n=50&includeMetaTags=0&ctx=web6dot0&api_version=4&_format=json&_marker=0");
        NetworkInterface api = retrofit.create(NetworkInterface.class);
        Call<JsonObject> call = api.loadPlaylist(uri.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray arr=response.body().get("songs").getAsJsonArray();
                int n=arr.size();
                JsonObject obj;
                for(int i=0;i<n;i++){
                    obj=arr.get(i).getAsJsonObject();
                    String type = obj.get("type").getAsString();
                    String encUrl = "";
                    if(type.equalsIgnoreCase("song")){
                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
                    }
                    onMediaSelected(new MediasModel(obj.get("id").getAsString(),obj.get("title").getAsString().replaceAll("&quot;","'"),obj.get("subtitle").getAsString().replaceAll("&quot;","'"),obj.get("type").getAsString(),obj.get("perma_url").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),encUrl,"hidni"));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("a",call.request().toString());
            }
        });
    }

    private void PlayNext(){
        if(currSongNumber >= songQueue.size()){
            Toast.makeText(getApplicationContext(),"Queue Ended!",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            progressBar2.setVisibility(View.INVISIBLE);
            return;
        }
        slidingPaneLayout.setPanelHeight(358);
        View view = linearLayoutManager.getChildAt(currSongNumber);
        if(view != null)
            ((TextView)view.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red_A400));
        songQueueRecycler.scrollToPosition(currSongNumber);
        MediasModel media = songQueue.get(currSongNumber);
        currentSongId = media.getId();
        quality = Common.quality;
        String token = Common.urlDecoder(media.getMedia_url());
        Retrofit retrofit = Common.getClient();
        HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?__call=song.generateAuthToken&url=" + token + "&bitrate="+quality+"&api_version=4&_format=json&ctx=web6dot0&_marker=0");
        NetworkInterface api = retrofit.create(NetworkInterface.class);
        Call<JsonObject> call = api.songUrl(uri.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String uri = response.body().get("auth_url").getAsString();
                Log.e("url",uri);
                progressBar.setVisibility(View.INVISIBLE);
                if (mediaPlayer == null) {
                    handler.removeCallbacksAndMessages(null);
                    StartMusic(uri, media);
                } else {
                    handler.removeCallbacksAndMessages(null);
                    mediaPlayer.release();
                    mediaPlayer = null;
                    //mediaPlayer = new MediaPlayer();
                    StartMusic(uri, media);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("error", call.request().url().toString());
            }
        });
    }

    private void StartMusic(String url, MediasModel media){
        songQueueRecycler.scrollToPosition(currSongNumber);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                isPlaying = false;
                playPause.setImageResource(R.drawable.ic_play);
                songPlayPause.setImageResource(R.drawable.ic_play_circle_filled);
                View view = linearLayoutManager.getChildAt(currSongNumber);
                ((TextView)view.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                if(currSongNumber < songQueue.size()){
                    if(repeat == 0) {
                        currSongNumber++;
                    }
                    else if(repeat == 2){
                        currSongNumber = (currSongNumber+1)%songQueue.size();
                    }else{
                        mediaPlayer.seekTo(0);
                        ResumeMusic();
                        return;
                    }
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar2.setVisibility(View.VISIBLE);
                        PlayNext();
                }
            }
        });
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Picasso.get().load(media.getImage()).into(mediaImg);
                Picasso.get().load(media.getImage()).into(songImg);
                mediaTitle.setText(media.getTitle());
                mediaSubTitle.setText(media.getSubtitle());
                songTitle.setText(media.getTitle());
                songSubtitle.setText(media.getSubtitle());
                playPause.setImageResource(R.drawable.ic_pause);
                songPlayPause.setImageResource(R.drawable.ic_pause_circle_filled);
                isPlaying = true;
                progressBar.setVisibility(View.INVISIBLE);
                progressBar2.setVisibility(View.INVISIBLE);
                if(slidingPaneLayout.getPanelHeight()!=358)
                    slidingPaneLayout.setPanelHeight(358);
                mediaPlayer.start();
                int millSecond = mediaPlayer.getDuration();
                songProgressTop.setMax(millSecond);
                songSeekBar.setMax(millSecond);
                totalDuration.setText(Song.formatDuration(millSecond));
                Log.e("hg",String.valueOf(millSecond));
                mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                        if (i<songSeekBar.getMax()) {
                           songSeekBar.setSecondaryProgress((i*millSecond)/100);
                            Log.e("hg",String.valueOf(i));
                        }

                    }
                });


                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                            songProgressTop.setProgress(mediaPlayer.getCurrentPosition());
                            songSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                            currDuration.setText(Song.formatDuration(mediaPlayer.getCurrentPosition()));
                            handler.postDelayed(this, 1000);
                    }
                };
                handler.postDelayed(runnable,0);
                //Toast.makeText(getApplicationContext(), String.valueOf(millSecond),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void PauseMusic(){
            playPause.setImageResource(R.drawable.ic_play);
            songPlayPause.setImageResource(R.drawable.ic_play_circle_filled);
            if(mediaPlayer != null)
              mediaPlayer.pause();
            isPlaying = false;
    }
    private void ResumeMusic(){
        if(mediaPlayer == null){
            if(songQueue.size() > 0){
                currSongNumber = 0;
                View prevView = linearLayoutManager.getChildAt(currSongNumber);
//                if(prevView != null)
//                    ((TextView)prevView.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red_A400));

                PlayNext();
            }
        }else{
            playPause.setImageResource(R.drawable.ic_pause);
            songPlayPause.setImageResource(R.drawable.ic_pause_circle_filled);
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    private void loadWeeklyTop(){
        Retrofit retrofit= Common.getClient();
        NetworkInterface api=retrofit.create(NetworkInterface.class);
        songQueue.clear();
        Call<JsonObject> call=api.weeklyTop(lang);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
               JsonArray arr=response.body().get("list").getAsJsonArray();
                int n=arr.size();
                JsonObject obj;
                for(int i=0;i<n;i++){
                    obj=arr.get(i).getAsJsonObject();
                    String type = obj.get("type").getAsString();
                    String encUrl = "";
                    if(type.equalsIgnoreCase("song")){
                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
                    }
                    songQueue.add(new MediasModel(obj.get("id").getAsString(),obj.get("title").getAsString().replaceAll("&quot;","'"),obj.get("subtitle").getAsString().replaceAll("&quot;","'"),obj.get("type").getAsString(),obj.get("perma_url").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),encUrl,"hindi"));
                }
                Picasso.get().load(response.body().get("image").getAsString()).into(mediaImg);
                Picasso.get().load(response.body().get("image").getAsString()).into(songImg);
                songTitle.setText(response.body().get("title").getAsString());
                mediaTitle.setText(response.body().get("title").getAsString());
                songSubtitle.setText(response.body().get("header_desc").getAsString());
                mediaSubTitle.setText(response.body().get("header_desc").getAsString());
                queueAdaptor.setMediaModelList(songQueue);
                queueAdaptor.notifyDataSetChanged();
                slidingPaneLayout.setPanelHeight(358);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }
    private void loadSimilarSong(MediasModel media){
        Retrofit retrofit= Common.getClient();
        Log.e("ad",media.getPerma_url());
        String token = media.getId();
        HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?__call=reco.getreco&api_version=4&_format=json&_marker=0&ctx=web6dot0&pid="+token);
        NetworkInterface api = retrofit.create(NetworkInterface.class);
        Call<JsonArray> call = api.similarSongs(uri.toString());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray arr=response.body().getAsJsonArray();
                int n=arr.size();
                JsonObject obj;
                for(int i=0;i<n;i++){
                    obj=arr.get(i).getAsJsonObject();
                    String type = obj.get("type").getAsString();
                    String encUrl = "";
                    if(type.equalsIgnoreCase("song")){
                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
                    }
                    songQueue.add(new MediasModel(obj.get("id").getAsString(),obj.get("title").getAsString().replaceAll("&quot;","'"),obj.get("subtitle").getAsString().replaceAll("&quot;","'"),obj.get("type").getAsString(),obj.get("perma_url").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),encUrl,"hindi"));
                }
                queueAdaptor.setMediaModelList(songQueue);
                queueAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("a",call.request().toString());
            }
        });
    }

    private void LoadPlaylist(MediasModel media){
        Retrofit retrofit= Common.getClient();
        Log.e("ad",media.getPerma_url());
        String token = Common.getToken(media.getPerma_url());
        HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?__call=webapi.get&token="+token+"&type=playlist&p=1&n=50&includeMetaTags=0&ctx=web6dot0&api_version=4&_format=json&_marker=0");
        NetworkInterface api = retrofit.create(NetworkInterface.class);
        Call<JsonObject> call = api.loadPlaylist(uri.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray arr=response.body().get("list").getAsJsonArray();
                int n=arr.size();
                JsonObject obj;
                for(int i=0;i<n;i++){
                    obj=arr.get(i).getAsJsonObject();
                    String type = obj.get("type").getAsString();
                    String encUrl = "";
                    if(type.equalsIgnoreCase("song")){
                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
                    }
                    songQueue.add(new MediasModel(obj.get("id").getAsString(),obj.get("title").getAsString().replaceAll("&quot;","'"),obj.get("subtitle").getAsString().replaceAll("&quot;","'"),obj.get("type").getAsString(),obj.get("perma_url").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),encUrl,"hindi"));
                }
                queueAdaptor.setMediaModelList(songQueue);
                queueAdaptor.notifyDataSetChanged();
                currSongNumber = 0;
                View prevView = linearLayoutManager.getChildAt(currSongNumber);
                if(prevView != null)
                    ((TextView)prevView.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red_A400));

                PlayNext();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("a",call.request().toString());
            }
        });
    }
    private void LoadAlbum(MediasModel media){
        Retrofit retrofit= Common.getClient();
        Log.e("ad",media.getPerma_url());
        String token = media.getId();
        HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?__call=content.getAlbumDetails&albumid="+token+"&api_version=4&_format=json&_marker=0&ctx=web6dot0");
        NetworkInterface api = retrofit.create(NetworkInterface.class);
        Call<JsonObject> call = api.loadAlbum(uri.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray arr=response.body().get("list").getAsJsonArray();
                int n=arr.size();
                JsonObject obj;
                for(int i=0;i<n;i++){
                    obj=arr.get(i).getAsJsonObject();
                    String type = obj.get("type").getAsString();
                    String encUrl = "";
                    if(type.equalsIgnoreCase("song")){
                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
                    }
                    songQueue.add(new MediasModel(obj.get("id").getAsString(),obj.get("title").getAsString().replaceAll("&quot;","'"),obj.get("subtitle").getAsString().replaceAll("&quot;","'"),obj.get("type").getAsString(),obj.get("perma_url").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),encUrl,"hindi"));
                }
                queueAdaptor.setMediaModelList(songQueue);
                queueAdaptor.notifyDataSetChanged();
                currSongNumber = 0;
                View prevView = linearLayoutManager.getChildAt(currSongNumber);
                if(prevView != null)
                    ((TextView)prevView.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red_A400));

                PlayNext();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("a",call.request().toString());
            }
        });
    }

    private void LoadArtistSong(CircularModel artist){
        Retrofit retrofit= Common.getClient();
        //Log.e("ad",media.getPerma_url());
        String token = Common.getToken(artist.getPerma_url());
        HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?__call=webapi.get&token="+token+"&type=artist&p=&n_song=50&n_album=0&sub_type=songs&category=&sort_order=&includeMetaTags=0&ctx=web6dot0&api_version=4&_format=json&_marker=0");
        NetworkInterface api = retrofit.create(NetworkInterface.class);
        Call<JsonObject> call = api.loadArtist(uri.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray arr=response.body().get("topSongs").getAsJsonArray();
                int n=arr.size();
                JsonObject obj;
                for(int i=0;i<n;i++){
                    obj=arr.get(i).getAsJsonObject();
                    String type = obj.get("type").getAsString();
                    String encUrl = "";
                    if(type.equalsIgnoreCase("song")){
                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
                    }
                    songQueue.add(new MediasModel(obj.get("id").getAsString(),obj.get("title").getAsString().replaceAll("&quot;","'"),obj.get("subtitle").getAsString().replaceAll("&quot;","'"),obj.get("type").getAsString(),obj.get("perma_url").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),encUrl,"hindi"));
                }
                queueAdaptor.setMediaModelList(songQueue);
                queueAdaptor.notifyDataSetChanged();
                currSongNumber = 0;
                View prevView = linearLayoutManager.getChildAt(currSongNumber);
                if(prevView != null)
                    ((TextView)prevView.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red_A400));

                PlayNext();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("a",call.request().toString());
            }
        });
    }

    private void LoadRadioSongs(MediasModel radio){
        Retrofit retrofit= Common.getClient();
        //Log.e("ad",media.getPerma_url());
        String type = (radio.getId().equalsIgnoreCase("artist") ? "createArtistStation":"createFeaturedStation");
        HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?language="+radio.getLanguage()+"&pid=&query="+radio.getTitle()+"&name="+radio.getTitle()+"&mode=&artistid=&api_version=4&_format=json&_marker=0&ctx=web6dot0&__call=webradio."+type);
        NetworkInterface api = retrofit.create(NetworkInterface.class);
        Call<JsonObject> call = api.stationID(uri.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String token = response.body().get("stationid").getAsString();
                LoadRadioSongs(token);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("a",call.request().toString());
            }
        });

    }

    private void LoadRadioSongs(String token){
        Retrofit retrofit= Common.getClient();
        //Log.e("ad",media.getPerma_url());
        HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?__call=webradio.getSong&stationid="+token+"&k=15&next=1&api_version=4&_format=json&_marker=0&ctx=web6dot0");
        NetworkInterface api = retrofit.create(NetworkInterface.class);
        Call<JsonObject> call = api.getRadioSongs(uri.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject arr=response.body().getAsJsonObject();
                Log.e("radio",arr.toString()+String.valueOf(arr.size()));
                int n=arr.size();
                JsonObject obj;
                for(int i=0;i<n-1;i++){
                    obj=arr.get(String.valueOf(i)).getAsJsonObject().get("song").getAsJsonObject();
                    String type = obj.get("type").getAsString();
                    String encUrl = "";
                    if(type.equalsIgnoreCase("song")){
                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
                    }
                    songQueue.add(new MediasModel(obj.get("id").getAsString(),obj.get("title").getAsString().replaceAll("&quot;","'"),obj.get("subtitle").getAsString().replaceAll("&quot;","'"),obj.get("type").getAsString(),obj.get("perma_url").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),encUrl,"hindi"));
                }
                queueAdaptor.setMediaModelList(songQueue);
                queueAdaptor.notifyDataSetChanged();
                currSongNumber = 0;
                View prevView = linearLayoutManager.getChildAt(currSongNumber);
                if(prevView != null)
                    ((TextView)prevView.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red_A400));

                PlayNext();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("a",call.request().toString());
            }
        });
    }

    private void loadSongWithToken(MediasModel media){
        Retrofit retrofit= Common.getClient();
        Log.e("ad",media.getPerma_url());
        String token = Common.getToken(media.getPerma_url());
        HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?__call=webapi.get&token="+token+"&type=song&p=1&n=50&includeMetaTags=0&ctx=web6dot0&api_version=4&_format=json&_marker=0");
        NetworkInterface api = retrofit.create(NetworkInterface.class);
        Call<JsonObject> call = api.loadPlaylist(uri.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray arr=response.body().get("songs").getAsJsonArray();
                int n=arr.size();
                JsonObject obj;
                for(int i=0;i<n;i++){
                    obj=arr.get(i).getAsJsonObject();
                    String type = obj.get("type").getAsString();
                    String encUrl = "";
                    if(type.equalsIgnoreCase("song")){
                        encUrl = obj.get("more_info").getAsJsonObject().get("encrypted_media_url").getAsString();
                    }
                    songQueue.add(new MediasModel(obj.get("id").getAsString(),obj.get("title").getAsString().replaceAll("&quot;","'"),obj.get("subtitle").getAsString().replaceAll("&quot;","'"),obj.get("type").getAsString(),obj.get("perma_url").getAsString(),Common.getHighQuality(obj.get("image").getAsString()),encUrl,"hindi"));
                }
                queueAdaptor.setMediaModelList(songQueue);
                queueAdaptor.notifyDataSetChanged();
                currSongNumber = 0;
                View prevView = linearLayoutManager.getChildAt(currSongNumber);
                if(prevView != null)
                    ((TextView)prevView.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red_A400));

                PlayNext();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("a",call.request().toString());
            }
        });
    }

    private void showLyricsDialog(){
        Retrofit retrofit= Common.getClient();
        String id = "";
        if(currSongNumber >= 0 && currSongNumber<songQueue.size()){
            id = songQueue.get(currSongNumber).getId();
        }else{
            Toast.makeText(getApplicationContext(),"No Song Playing",Toast.LENGTH_SHORT).show();
            return;
        }
        HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?__call=lyrics.getLyrics&lyrics_id="+id+"&ctx=web6dot0&api_version=4&_format=json&_marker=0");
        NetworkInterface api = retrofit.create(NetworkInterface.class);
        Call<JsonObject> call = api.getLyrics(uri.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String lyrics = "";
                if(response.body().has("status")){
                    lyrics = "No lyrics found for this song!";
                }else{
                    lyrics = response.body().get("lyrics").getAsString();
                }
                lyricsTv.setText(Html.fromHtml(lyrics));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

    }


    @Override
    public void updateCurrSong(int position,int toPosition, int type) {
        if(type == 0){
            if(position == currSongNumber) {
                isPlaying = false;
                playPause.setImageResource(R.drawable.ic_play);
                songPlayPause.setImageResource(R.drawable.ic_play_circle_filled);
                if (currSongNumber >= songQueue.size()) {
                    Toast.makeText(getApplicationContext(), "Queue Ended!", Toast.LENGTH_SHORT).show();
                    PauseMusic();
                } else {
                    View currView = linearLayoutManager.getChildAt(position + 1);
                    if (currView != null)
                        ((TextView) currView.findViewById(R.id.queue_song_title)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_A400));
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar2.setVisibility(View.VISIBLE);
                    PlayNext();
                }
            }else if(position < currSongNumber){
                currSongNumber--;
            }
        }else if(type == 1){
            if(position == currSongNumber) {
                currSongNumber = toPosition;
            }else if(position < currSongNumber){
                if(toPosition >= currSongNumber)
                    currSongNumber--;
            }else{
                if(toPosition<=currSongNumber)
                    currSongNumber--;
            }
        }
    }

    private void downloadFile(MediasModel media){
        int permission = ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    HomeActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );

        }else {
            String token = Common.urlDecoder(media.getMedia_url());
            Retrofit retrofit = Common.getClient();
            HttpUrl uri = HttpUrl.parse("https://www.jiosaavn.com/api.php?__call=song.generateAuthToken&url=" + token + "&bitrate=" + quality + "&api_version=4&_format=json&ctx=web6dot0&_marker=0");
            NetworkInterface api = retrofit.create(NetworkInterface.class);
            Call<JsonObject> call = api.songUrl(uri.toString());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    String url = response.body().get("auth_url").getAsString();
                    //Toast.makeText(getApplicationContext(),url,Toast.LENGTH_SHORT).show();
                    new DownloadTask(getApplicationContext()).execute(url,media.getTitle());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("error", call.request().url().toString());
                }
            });
        }
    }


    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString()+"/"+sUrl[1]+".mp3");
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            progressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "Song downloaded", Toast.LENGTH_SHORT).show();
        }
    }

}