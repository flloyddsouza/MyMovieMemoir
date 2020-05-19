package com.flloyd.mymoviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flloyd.mymoviememoir.DataModel.TopMovie;
import com.flloyd.mymoviememoir.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<TopMovie> topMovieList;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView movieName;
        public TextView rating;
        public TextView releaseDate;

        public ViewHolder(View itemView) {
            super(itemView);
            movieName = itemView.findViewById(R.id.movieName);
            rating = itemView.findViewById(R.id.rating);
            releaseDate = itemView.findViewById(R.id.date);
        }
    }


    @Override
    public int getItemCount() {
        return topMovieList.size();
    }


    public RecyclerViewAdapter(List<TopMovie> topMovieList) {
        this.topMovieList = topMovieList;
    }
    public void addTopMovies(List<TopMovie> topMovieList) {
        this.topMovieList = topMovieList;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View unitsView = inflater.inflate(R.layout.rv_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(unitsView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        final TopMovie topMovie = topMovieList.get(position);
        Format formatter = new SimpleDateFormat("MMM dd, yyyy");
        TextView movieNameTV = viewHolder.movieName;
        movieNameTV.setText(topMovie.getMovieName());

        TextView ratingTV = viewHolder.rating;
        ratingTV.setText(topMovie.getRating().toString());
        TextView dateTV = viewHolder.releaseDate;

        Date date = topMovie.getReleaseDate();
        dateTV.setText(formatter.format(date));
    }
}