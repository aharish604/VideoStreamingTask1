package com.example.videostreamingtask1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.google.android.exoplayer2.extractor.ogg.OggExtractor;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final String KEY_MP3 = "mp3";
    public static final String KEY_OGG = "ogg";
    public static final String KEY_MP4 = "mp4";
    public static final String KEY_MP4_CAPS = "MP4";
    public static final String KEY_HLS = "m3u8";
    public static final String KEY_USER_AGENT = "exoplayer-codelab";

    public static MediaSource buildMediaSource(String source, HashMap<String, String> extraHeaders,Context context) {


        if (source == null) {
            displayLongToast(context,"Input Is Invalid.");

            return null;
        }
        boolean validUrl = URLUtil.isValidUrl(source);
        if(!validUrl)
        {
            displayLongToast(context,"Not a Valid URL.");

        }

        Uri uri = Uri.parse(source);
        if (uri == null || uri.getLastPathSegment() == null) {
            displayLongToast(context,"Uri Converter Failed, Input Is Invalid.");
            return null;
        }
        if (validUrl && (uri.getLastPathSegment().contains(Constants.KEY_MP4) || uri.getLastPathSegment().contains(Constants.KEY_MP4_CAPS))) {

            DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory(Constants.KEY_USER_AGENT);
            if (extraHeaders != null) {
                for (Map.Entry<String, String> entry : extraHeaders.entrySet())
                    sourceFactory.getDefaultRequestProperties().set(entry.getKey(), entry.getValue());
            }

            return new ProgressiveMediaSource.Factory(sourceFactory)
                    .createMediaSource(uri);

        } else if (validUrl && (uri.getLastPathSegment().contains(Constants.KEY_MP4)) || uri.getLastPathSegment().contains(Constants.KEY_MP4_CAPS)) {
            return new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(context, Constants.KEY_USER_AGENT))
                    .createMediaSource(uri);

        } else if (validUrl && (uri.getLastPathSegment().contains(Constants.KEY_HLS))) {

          /*  // Create a data source factory.
            DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory();
// Create a HLS media source pointing to a playlist uri.
            HlsMediaSource hlsMediaSource =
                    new HlsMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(MediaItem.fromUri(hlsUri));
// Create a player instance.
            SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
// Set the media source to be played.
            player.setMediaSource(hlsMediaSource);
// Prepare the player.
            player.prepare();*/
            DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory(Constants.KEY_USER_AGENT);
            if (extraHeaders != null) {
                for (Map.Entry<String, String> entry : extraHeaders.entrySet())
                    sourceFactory.getDefaultRequestProperties().set(entry.getKey(), entry.getValue());
            }

            return new HlsMediaSource.Factory(sourceFactory).createMediaSource(uri);



        } else if (uri.getLastPathSegment().contains(Constants.KEY_MP3)){
            DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory(Constants.KEY_USER_AGENT);
            if (extraHeaders != null) {
                for (Map.Entry<String, String> entry : extraHeaders.entrySet())
                    sourceFactory.getDefaultRequestProperties().set(entry.getKey(), entry.getValue());
            }
            return new ProgressiveMediaSource.Factory(sourceFactory)
                    .createMediaSource(uri);

        } else if (uri.getLastPathSegment().contains(Constants.KEY_OGG)){

            DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory(Constants.KEY_USER_AGENT);
            if (extraHeaders != null) {
                for (Map.Entry<String, String> entry : extraHeaders.entrySet())
                    sourceFactory.getDefaultRequestProperties().set(entry.getKey(), entry.getValue());
            }

            return new ProgressiveMediaSource.Factory(sourceFactory, OggExtractor.FACTORY)
                    .createMediaSource(uri);

        } else {
            DefaultDashChunkSource.Factory dashChunkSourceFactory = new DefaultDashChunkSource.Factory(new DefaultHttpDataSourceFactory("ua", new DefaultBandwidthMeter()));
            DefaultHttpDataSourceFactory manifestDataSourceFactory = new DefaultHttpDataSourceFactory(Constants.KEY_USER_AGENT);
            return new DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
                    .createMediaSource(uri);
        }
    }



    public static boolean isNetworkAvailable(Context context) {
        @SuppressLint("WrongConstant") ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static void displayLongToast(Context mContext, String message) {
        try {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
