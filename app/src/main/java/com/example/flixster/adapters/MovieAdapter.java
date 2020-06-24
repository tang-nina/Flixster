package com.example.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
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
    public class ViewHolder extends RecyclerView.ViewHolder{

        //one for each comp in item_movie
        TextView tv_title;
        TextView tv_overview;
        ImageView iv_poster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_overview = itemView.findViewById(R.id.tv_overview);
            iv_poster = itemView.findViewById(R.id.iv_poster);
        }

        //puts data into the view (i.e. the three fields above that correspond to the xml)
        //according to the movie object
        public void bind(Movie movie) {
            tv_title.setText(movie.getTitle());
            tv_overview.setText(movie.getOverview());

            String imageUrl;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
            }else{
                imageUrl = movie.getPosterPath();
            }

            //give Glide a context (.with), then the image path (.load), and then where to load the image into (.into)
            Glide.with(context).load(imageUrl).placeholder(R.drawable.movie_placeholder).into(iv_poster);
        }
    }
}
