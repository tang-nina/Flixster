package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=";
    public static final String TAG = "MainActivity";

    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        movies = new ArrayList<Movie>();

        //Create adapter, set adapter to recycler view, set a layout manager
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        binding.rvMovies.setAdapter(movieAdapter);
        binding.rvMovies.setLayoutManager(new LinearLayoutManager(this));

        //get request for list of movies
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL + getString(R.string.moviesdb_api_key),
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONArray results = jsonObject.getJSONArray("results");

                            //do not do movies = Movie.fromJsonArray(results) bc movies will then
                            //point to a new memory address and the adapter will not know.
                            movies.addAll(Movie.fromJsonArray(results));

                            //always notify adapter after list data has changed
                            movieAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e(TAG, "Hit json exception", e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers,
                                          String response, Throwable throwable) {
                        Log.d(TAG, "onFailure");
                    }
                });
    }
}