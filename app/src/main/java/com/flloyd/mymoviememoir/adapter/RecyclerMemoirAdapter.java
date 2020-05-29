package com.flloyd.mymoviememoir.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.flloyd.mymoviememoir.DataModel.MemoirCard;
import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.fragment.MovieDetailsFragment;
import com.flloyd.mymoviememoir.networkConnection.OMDbAPI;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerMemoirAdapter  extends RecyclerView.Adapter<RecyclerMemoirAdapter.ViewHolder>{

    private List<MemoirCard> memoirCardList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView movieName;
        TextView releaseDate;
        TextView watchDate;
        TextView location;
        TextView comment;
        RatingBar ratingBar;
        ImageView poster;
        CardView parent;

        public ViewHolder(View itemView) {
            super(itemView);
            movieName = itemView.findViewById(R.id.movieNameMemoir);
            releaseDate = itemView.findViewById(R.id.movieDateMemoir);
            watchDate = itemView.findViewById(R.id.movieWatchMemoir);
            location = itemView.findViewById(R.id.movieLocationMemoir);
            comment = itemView.findViewById(R.id.commentMemoir);
            ratingBar = itemView.findViewById(R.id.ratingBarMemoir);
            poster = itemView.findViewById(R.id.image_poster_memoir);
            parent = itemView.findViewById(R.id.memoirCard);
        }
    }

    @Override
    public int getItemCount() {
        return memoirCardList.size();
    }

    public RecyclerMemoirAdapter(List<MemoirCard> memoirCardList) {
        this.memoirCardList = memoirCardList;
    }

    public void addMemoirListItem(List<MemoirCard> memoirCardList) {
        this.memoirCardList = memoirCardList;
        notifyDataSetChanged();
    }

    private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap poster = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                poster = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return poster;
        }

        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View memoirCardView = inflater.inflate(R.layout.memoir_view_layout, parent, false);
        ViewHolder viewHolderMemoir = new ViewHolder(memoirCardView);
        return viewHolderMemoir;
    }


    private class getMovieImage extends AsyncTask<String,Void,String> {
        ImageView imageView;

        public getMovieImage(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected String doInBackground(@NotNull String... params) {
            return OMDbAPI.search(params[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            Log.i("Flloyd","Result of OMDb API:" + result);
            String posterURL = "";
            try {
                JSONObject movieDetails = new JSONObject(result);
                posterURL = movieDetails.getString("Poster");
            } catch (JSONException e) {

            }
            DownLoadImageTask downLoadImageTask = new DownLoadImageTask(imageView);
            downLoadImageTask.execute(posterURL);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final MemoirCard memoirCard = memoirCardList.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);

        String releaseDate,watchDate;
        try {
            Date rDate = formatter.parse(memoirCard.getReleaseDate());
            assert rDate != null;
            releaseDate = newFormat.format(rDate);
            Date wDate = formatter.parse(memoirCard.getWatchDate());
            assert wDate != null;
            watchDate = newFormat.format(wDate);
        }catch (Exception e){
            releaseDate = memoirCard.getReleaseDate();
            watchDate = memoirCard.getWatchDate();
        }

        TextView movieNameTV = holder.movieName;
        movieNameTV.setText(memoirCard.getMovieName());

        TextView movieDate = holder.releaseDate;
        movieDate.setText(releaseDate);

        TextView watchedDate = holder.watchDate;
        watchedDate.setText(watchDate);

        TextView locationCode = holder.location;
        locationCode.setText(memoirCard.getCinemaPostCode());

        TextView comments = holder.comment;
        comments.setText(memoirCard.getComment());

        RatingBar ratingTV = holder.ratingBar;
        ratingTV.setRating(memoirCard.getRating());

        ImageView poster = holder.poster;
        getMovieImage getMovieImage = new getMovieImage(poster);
        getMovieImage.execute(memoirCard.getMovieName());


        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("MovieName",memoirCardList.get(position).getMovieName());
                bundle.putBoolean("Watchlist",false);
                bundle.putBoolean("Memoir",true);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, MovieDetailsFragment.class,bundle);
                fragmentTransaction.addToBackStack("tag");
                fragmentTransaction.commit();
            }
        });

    }

}
