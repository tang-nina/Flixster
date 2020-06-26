package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends YouTubeBaseActivity {

    public static final String GET_MOVIE_URL_FRONT = "https://api.themoviedb.org/3/movie/";
    public static final String GET_MOVIE_URL_BACK = "/videos?api_key=";
    public static final String GET_MOVIE_URL_LANG = "&language=en-US";
    public static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    public static final String TAG = "MovieDetailsActivity";

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        //populate page with movie specific info
        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());
        binding.rbVoteAverage.setRating((float) (movie.getVoteAverage() / 2.0));
        binding.tvPopularity.setText(binding.tvPopularity.getText() +
                " " + movie.getPopularity().toString());

        AsyncHttpClient client = new AsyncHttpClient();
        String url = GET_MOVIE_URL_FRONT + movie.getMovieId() + GET_MOVIE_URL_BACK +
                getString(R.string.moviesdb_api_key) + GET_MOVIE_URL_LANG;

        //get request to MoviesDB for trailer link, if there is one
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    JSONObject video = results.optJSONObject(0);

                    if (video == null) { //if no video linked
                        //collapse Youtube player and Load no video available image
                        binding.player.setVisibility(View.GONE);
                        Glide.with(MovieDetailsActivity.this).load(R.drawable.no_video)
                                .fitCenter().into(binding.noVideo);
                    } else {
                        //collapse ImageView
                        binding.noVideo.setVisibility(View.GONE);

                        //load video in to YoutubeView
                        final String key = (String) video.getString("key");
                        String youtubeUrl = YOUTUBE_URL + key;

                        binding.player.initialize(getString(R.string.youtube_api_key),
                                new YouTubePlayer.OnInitializedListener() {
                                    @Override
                                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                        YouTubePlayer youTubePlayer, boolean b) {
                                        youTubePlayer.cueVideo(key);
                                    }

                                    @Override
                                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                        YouTubeInitializationResult youTubeInitializationResult) {
                                        Log.e(TAG, "Error initializing YouTube player");
                                    }
                                });
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}