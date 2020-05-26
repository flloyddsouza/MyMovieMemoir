package com.flloyd.mymoviememoir.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.flloyd.mymoviememoir.DataModel.SearchResult;
import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.fragment.MovieDetailsFragment;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class RecyclerSearchMovieAdapter extends RecyclerView.Adapter<RecyclerSearchMovieAdapter.ViewHolder>    {

    private List<SearchResult> searchResultList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView movieName;
        TextView releaseDate;
        ImageView poster;
        CardView parent;

        public ViewHolder(View itemView) {
            super(itemView);
            movieName = itemView.findViewById(R.id.movieNameSearch);
            releaseDate = itemView.findViewById(R.id.movieYearSearch);
            poster = itemView.findViewById(R.id.image_poster);
            parent = itemView.findViewById(R.id.cv);
        }
    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
    }


    public RecyclerSearchMovieAdapter(List<SearchResult> searchResultList) {
        this.searchResultList = searchResultList;

    }
    public void addSearchResult(List<SearchResult> searchResultList) {
        this.searchResultList = searchResultList;
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
        View resultMoviesView = inflater.inflate(R.layout.search_view_layout, parent, false);
        ViewHolder viewHolderSearch = new ViewHolder(resultMoviesView);
        return viewHolderSearch;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final SearchResult searchResult = searchResultList.get(position);

        TextView movieNameTV = holder.movieName;
        movieNameTV.setText(searchResult.getName());

        TextView movieYear = holder.releaseDate;
        movieYear.setText(searchResult.getYear());


        ImageView poster = holder.poster;
        DownLoadImageTask downLoadImageTask = new DownLoadImageTask(poster);
        downLoadImageTask.execute(searchResult.getImage());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("MovieName",searchResultList.get(position).getName());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,MovieDetailsFragment.class,bundle);
                fragmentTransaction.addToBackStack("tag");
                fragmentTransaction.commit();
            }
        });
    }
}
