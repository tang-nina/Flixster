package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    //class is parametrized over the view holder that you created below

    Context context; //where this adapter is being constructed from
    List<Movie> movies; //actual data

    public MovieAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    //inflates a layout (from our item_movie.xml) --> creates a view
    // then, it returns that view inside a view holder.
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    //Populate data into the item through the holder
    //take the data at the position, and put it into the view inside the view holder.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie); //calling method to put data into the view
    }

    @Override
    //return total count of items in the list.
    public int getItemCount() {
        return movies.size();
    }

    //view of each item in the list (i.e. a view for each item following item_movie.xml
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public static final String CONFIG_URL = "https://api.themoviedb.org/3/configuration?api_key=";
        AsyncHttpClient client = new AsyncHttpClient();

        //one for each comp in item_movie
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        //puts data into the view (i.e. the three fields above that correspond to the xml)
        //according to the movie object
        public void bind(final Movie movie) {
            final int radius = 20; // corner radius, higher value = more rounded
            final int margin = 5; // crop margin, set to 0 for corners with no crop

            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            //place default image while get request is processing

           // rvMovies.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            Glide.with(context).load(R.drawable.movie_placeholder).fitCenter().transform(new RoundedCornersTransformation(radius, margin)).into(ivPoster);

            client.get(CONFIG_URL + context.getString(R.string.moviesdb_api_key), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    JSONObject pictureInfo = json.jsonObject;
                    try {
                        JSONObject imagesInfo = pictureInfo.getJSONObject("images");

                        String baseUrl = imagesInfo.getString("secure_base_url");

                        JSONArray bdSizes = imagesInfo.getJSONArray("backdrop_sizes");
                        String backdropSize = bdSizes.optString(1, "w780");

                        JSONArray ptSizes = imagesInfo.getJSONArray("poster_sizes");
                        String posterSize = ptSizes.optString(3, "w342");

                        String imageUrl;
                        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                            imageUrl = baseUrl+backdropSize+movie.getBackdropPath();
                        }else{
                            imageUrl = baseUrl+posterSize+movie.getPosterPath();
                        }

                        //give Glide a context (.with), then the image path (.load), and then where to load the image into (.into)
                        Glide.with(context).load(imageUrl).placeholder(R.drawable.movie_placeholder).fitCenter().transform(new RoundedCornersTransformation(radius, margin)).into(ivPoster);

                    } catch (JSONException e) {
                        Log.e("movie adapter client", "Hit json exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d("movie adapter client", "onFailure");
                }
            });

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if(position != RecyclerView.NO_POSITION){
                Movie curMovie = movies.get(position);
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(curMovie));
                context.startActivity(intent);
            }
        }
    }
}
