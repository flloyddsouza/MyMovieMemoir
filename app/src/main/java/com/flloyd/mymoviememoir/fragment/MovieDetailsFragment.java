package com.flloyd.mymoviememoir.fragment;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.networkConnection.OMDbAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

public class MovieDetailsFragment extends Fragment {

    private RatingBar ratingBar;
    private TextView movieNameTV,YearTV,descriptionTV,genreTV,castTV,directorTV,releasedTV,countryTV;

    public MovieDetailsFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_details_fragment, container, false);
        String movieName = this.getArguments().getString("MovieName");
        String backdropImage = this.getArguments().getString("backdrop_path");


        ImageView poster = view.findViewById(R.id.imageMovie);
        movieNameTV = view.findViewById(R.id.movieName);
        YearTV = view.findViewById(R.id.movieYear);
        descriptionTV = view.findViewById(R.id.description);
        genreTV = view.findViewById(R.id.genreText);
        castTV = view.findViewById(R.id.Cast_Text);
        ratingBar = view.findViewById(R.id.ratingBarMovie);
        directorTV = view.findViewById(R.id.directorText);
        releasedTV = view.findViewById(R.id.releasedText);
        countryTV = view.findViewById(R.id.Country_Text);

        DownLoadImageTask downLoadImageTask = new DownLoadImageTask(poster);
        downLoadImageTask.execute(backdropImage);


        FloatingActionButton add = view.findViewById(R.id.floating_action_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Add Movie to ?")
                        .setMessage("You can add the movie to your Watchlist or to your Memoir.")
                        .setPositiveButton("Watchlist", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("Flloyd", "Watchlist");
                            }
                        })
                        .setNegativeButton("Memoir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("Flloyd", "Movie Memoir");
                            }
                        })
                        .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });



        getMovieDetails getMovieDetails = new getMovieDetails();
        getMovieDetails.execute(movieName);
        return view;
    }


    private class getMovieDetails extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(@NotNull String... params) {
            return OMDbAPI.search(params[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            Log.i("Flloyd","Result of OMDb API:" + result);
            String releaseDate, director, description, rating, country, cast, genre, year, name;
            try {
                JSONObject movieDetails = new JSONObject(result);
                name = movieDetails.getString("Title");
                year = movieDetails.getString("Year");
                genre = movieDetails.getString("Genre");
                cast = movieDetails.getString("Actors");
                releaseDate = movieDetails.getString("Released");
                country = movieDetails.getString("Country");
                director = movieDetails.getString("Director");
                description = movieDetails.getString("Plot");
                rating = movieDetails.getString("imdbRating");

                Log.i("Flloyd","Extracted Results:" + name + "\n" + year + "\n" + genre + "\n" + cast + "\n" + releaseDate + "\n" + country + "\n" + director + "\n" + description + "\n" + rating);
            } catch (JSONException e) {

                String NOT_FOUND = "Not Found";
                name = NOT_FOUND;
                year = NOT_FOUND;
                genre = NOT_FOUND;
                cast = NOT_FOUND;
                releaseDate = NOT_FOUND;
                country = NOT_FOUND;
                director = NOT_FOUND;
                description = NOT_FOUND;
                rating = NOT_FOUND;

            }
            movieNameTV.setText(name);
            YearTV.setText(year);
            descriptionTV.setText(description);
            genreTV.setText(genre);
            castTV.setText(cast);
            directorTV.setText(director);
            releasedTV.setText(releaseDate);
            countryTV.setText(country);
            float ratingCalculated = (Float.parseFloat(rating))/2;
            ratingBar.setRating(ratingCalculated);
        }
    }

    private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        DownLoadImageTask(ImageView imageView){
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


}
