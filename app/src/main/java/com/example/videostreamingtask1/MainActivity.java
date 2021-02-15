package com.example.videostreamingtask1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.exoplayer2.ExoPlayerFactory;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

import com.google.android.exoplayer2.source.MediaSource;

import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;

import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView btn_search;
    ImageView btn_clear;
    EditText edt_link;


    private SimpleExoPlayer simpleExoPlayer;
    private BandwidthMeter bandwidthMeter;
    private ExtractorsFactory extractorsFactory;
    private AdaptiveTrackSelection.Factory trackSelectionFactory;
    private TrackSelector trackSelector;
    private PlayerView playerView;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private boolean currPlayWhenReady = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        InitUI();
    }

    private void InitUI() {
        btn_search = findViewById(R.id.imag_serch);
        btn_clear = findViewById(R.id.search_clear);
        edt_link = findViewById(R.id.etd_videolink);
        playerView= findViewById(R.id.idExoPlayerVIew);
        playerView.setVisibility(View.GONE);
         btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_link.setText("");
            }

        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edt_link.getText().toString().equalsIgnoreCase("")) {
                    if (Constants.isNetworkAvailable(MainActivity.this)) {
                        setSource(edt_link.getText().toString());
                        playerView.setVisibility(View.VISIBLE);
                    } else {
                        Constants.displayLongToast(MainActivity.this, "No Network! Please try again");
                    }
                } else {
                    Constants.displayLongToast(MainActivity.this, "Link Cannot be empty!");
                }
            }
        });

        initializePlayer();

    }
    private void initializePlayer() {
        if (simpleExoPlayer == null) {
            bandwidthMeter = new DefaultBandwidthMeter();
            extractorsFactory = new DefaultExtractorsFactory();
            trackSelectionFactory = new AdaptiveTrackSelection.Factory();
            trackSelector = new DefaultTrackSelector();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(MainActivity.this, trackSelector);
            playerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.setPlayWhenReady(currPlayWhenReady);
            simpleExoPlayer.seekTo(currentWindow, playbackPosition);
        }
    }
    public void setSource(String source) {
        MediaSource mediaSource =Constants.buildMediaSource(source, null,MainActivity.this);
        if (mediaSource != null) {
            if (simpleExoPlayer != null) {
                simpleExoPlayer.prepare(mediaSource, true, true);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 24) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 24|| simpleExoPlayer == null)) {
              initializePlayer();
          }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 24) {
              releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 24) {
             releasePlayer();
        }
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            playerView.setVisibility(View.GONE);
            playbackPosition = simpleExoPlayer.getCurrentPosition();
            currentWindow = simpleExoPlayer.getCurrentWindowIndex();
             currPlayWhenReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }
    
}

