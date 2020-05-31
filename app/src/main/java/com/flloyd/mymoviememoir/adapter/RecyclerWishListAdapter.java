package com.flloyd.mymoviememoir.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.flloyd.mymoviememoir.Entity.Movie;
import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.fragment.MovieDetailsFragment;
import com.flloyd.mymoviememoir.viewmodel.MovieViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class RecyclerWishListAdapter extends RecyclerView.Adapter<RecyclerWishListAdapter.ViewHolder>{

    private List<Movie> wishList;
    private MovieViewModel movieViewModel;
    public static final int FACEBOOK_ADD_STICKER_TO_STORY_REQUEST = 10;
    public static final String FACEBOOK_SHARE_STICKER_INTENT = "com.facebook.share.ADD_STICKER_TO_STORY";

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView movieName;
        TextView releaseDate;
        TextView addDateTime;
        TextView comment;
        CardView parent;

        public ViewHolder(View itemView) {
            super(itemView);
            movieName = itemView.findViewById(R.id.movieNameWish);
            releaseDate = itemView.findViewById(R.id.dateWish);
            addDateTime = itemView.findViewById(R.id.dateAdded);
            comment = itemView.findViewById(R.id.commentMemoir);
            parent = itemView.findViewById(R.id.cv_watchlist);
        }
    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }


    public RecyclerWishListAdapter(List<Movie> wishList, MovieViewModel movieViewModel) {
        this.movieViewModel = movieViewModel;
        this.wishList = wishList;
    }

    public void addWishListItem(List<Movie> wishList) {
        this.wishList = wishList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View wishListView = inflater.inflate(R.layout.wishlist_item_layout, parent, false);
        ViewHolder viewHolderWishList = new ViewHolder(wishListView);
        return viewHolderWishList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Movie movie = wishList.get(position);

        TextView movieNameTV = holder.movieName;
        movieNameTV.setText(movie.movieName);

        TextView movieReleaseDate = holder.releaseDate;
        movieReleaseDate.setText(movie.getReleaseDate());

        TextView dateTimeAdded = holder.addDateTime;
        dateTimeAdded.setText(movie.getSaveDateTime());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle(movie.getMovieName())
                        .setPositiveButton("View", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle bundle = new Bundle();
                                bundle.putString("MovieName",wishList.get(position).movieName);
                                bundle.putBoolean("Watchlist",true);
                                bundle.putBoolean("Memoir",false);
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, MovieDetailsFragment.class,bundle);
                                fragmentTransaction.addToBackStack("tag");
                                fragmentTransaction.commit();
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(final DialogInterface dialogd, int which) {
                             Log.d("Flloyd", "Delete");
                            new MaterialAlertDialogBuilder(view.getContext())
                            .setTitle("Alert")
                            .setMessage("Are you sure you want to delete " + movie.getMovieName() + " from watchlist?")
                            .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    movieViewModel.delete(movie);
                                }
                            })
                            .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    dialogd.dismiss();
                                }
                            }).show();
                            }
                        })
                        .setNeutralButton("CLOSE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 dialog.dismiss();
                            }
                        }).show();
            }
        });

    }

}
