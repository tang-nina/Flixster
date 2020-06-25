package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie movie;

    TextView tvTitle;
    RatingBar rbVoteAverage;
    TextView tvOverview;
    TextView tvPopularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_movie_details);

        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        // layout of activity is stored in a special property called root
        View view = binding.getRoot();
        setContentView(view);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());
        binding.rbVoteAverage.setRating((float) (movie.getVoteAverage()/2.0));
        binding.tvPopularity.setText(binding.tvPopularity.getText() + movie.getPopularity().toString());
    }
}