package com.flloyd.mymoviememoir.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.networkConnection.OMDbAPI;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailsFragment extends Fragment {

    private String name,year,image,genre,cast,releaseDate,country,director,description,rating;
    public MovieDetailsFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_details_fragment, container, false);
        String movieName = this.getArguments().getString("MovieName");
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


            try {
                JSONObject movieDetails = new JSONObject(result);
                name = movieDetails.getString("Title");
                year = movieDetails.getString("Year");
                image = movieDetails.getString("Poster");
                genre = movieDetails.getString("Genre");
                cast = movieDetails.getString("Actors");
                releaseDate = movieDetails.getString("Released");
                country = movieDetails.getString("Country");
                director = movieDetails.getString("Director");
                description = movieDetails.getString("Plot");
                rating = movieDetails.getString("imdbRating");

                Log.i("Flloyd","Extracted Results:" + name + "\n" + year + "\n" + image + "\n" + genre + "\n" + cast + "\n" + releaseDate + "\n" + country + "\n" + director + "\n" + description + "\n" + rating);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
