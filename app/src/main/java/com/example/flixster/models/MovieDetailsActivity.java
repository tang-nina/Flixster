package com.example.flixster.models;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flixster.R;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {
//    Declare a new field for the movie
////    Retrieve, unwrap, and assign field from onCreate
////    Add some logging to confirm deserialization


    Movie movie;

    TextView tvTitle;
    RatingBar rbVoteAverage;
    TextView tvOverview;
    TextView tvPopularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tvTitle = findViewById(R.id.tv_title);
        rbVoteAverage = findViewById(R.id.rb_vote_average);
        tvOverview = findViewById(R.id.tv_overview);
        tvPopularity = findViewById(R.id.tv_popularity);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        rbVoteAverage.setRating((float) (movie.getVoteAverage()/2.0));
        tvPopularity.setText(tvPopularity.getText() + movie.getPopularity().toString());
    }
}