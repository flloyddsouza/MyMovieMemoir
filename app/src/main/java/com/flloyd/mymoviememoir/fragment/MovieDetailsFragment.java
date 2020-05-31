package com.flloyd.mymoviememoir.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.flloyd.mymoviememoir.Entity.Movie;
import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.networkConnection.OMDbAPI;
import com.flloyd.mymoviememoir.viewmodel.MovieViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDetailsFragment extends Fragment {

    private RatingBar ratingBar;
    private MovieViewModel movieViewModel;
    private ImageView poster;
    private String posterURL;
    private TextView movieNameTV,YearTV,descriptionTV,genreTV,castTV,directorTV,releasedTV,countryTV;

    public MovieDetailsFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_details_fragment, container, false);
        final String movieName = this.getArguments().getString("MovieName");
        final boolean watchlist = this.getArguments().getBoolean("Watchlist");
        final boolean memoir = this.getArguments().getBoolean("Memoir");

        poster = view.findViewById(R.id.imageMovie);
        movieNameTV = view.findViewById(R.id.movieName);
        YearTV = view.findViewById(R.id.movieYear);
        descriptionTV = view.findViewById(R.id.description);
        genreTV = view.findViewById(R.id.genreText);
        castTV = view.findViewById(R.id.Cast_Text);
        ratingBar = view.findViewById(R.id.ratingBarMovie);
        directorTV = view.findViewById(R.id.directorText);
        releasedTV = view.findViewById(R.id.releasedText);
        countryTV = view.findViewById(R.id.Country_Text);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.initalizeVars((getActivity().getApplication()));


        FloatingActionButton add = view.findViewById(R.id.floating_action_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (memoir) {
                    Toast.makeText(getContext(), "Movie Already in Memoir!", Toast.LENGTH_SHORT).show();
                }else{

                if (watchlist) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Add Movie to Memoir?")
                            .setMessage("Are you sure you want to add the movie to Movie Memoir")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    addToMemoir(view, movieName, releasedTV.getText().toString(), posterURL);
                                }
                            })
                            .setNeutralButton("SHARE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    share(view);
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Add Movie to ?")
                            .setMessage("You can add the movie to your Watchlist or to your Memoir.")
                            .setPositiveButton("Watchlist", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    addToWatchList();
                                }
                            })
                            .setNegativeButton("Memoir", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    addToMemoir(view, movieName, releasedTV.getText().toString(), posterURL);
                                }
                            })
                            .setNeutralButton("SHARE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    share(view);
                                    dialog.dismiss();
                                }
                            }).show();
                }

            }
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
                posterURL = movieDetails.getString("Poster");

                Log.i("Flloyd","Extracted Results:" + name + "\n" + year + "\n" + genre + "\n" + cast + "\n" + releaseDate + "\n" + country + "\n" + director + "\n" + description + "\n" + rating +"\n" + poster);
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
                posterURL = NOT_FOUND;

            }
            movieNameTV.setText(name);
            YearTV.setText(year);
            descriptionTV.setText(description);
            genreTV.setText(genre);
            castTV.setText(cast);
            directorTV.setText(director);
            releasedTV.setText(releaseDate);
            countryTV.setText(country);

            DownLoadImageTask downLoadImageTask = new DownLoadImageTask(poster);
            downLoadImageTask.execute(posterURL);

            float ratingCalculated = 3.00f;
            try {
                ratingCalculated   = (Float.parseFloat(rating))/2;
            }catch (Exception e){
                e.printStackTrace();
            }
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

    private void addToWatchList(){
        Log.d("Flloyd", "Watchlist");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM, yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        final String mName = movieNameTV.getText().toString();
        final String reDate = releasedTV.getText().toString();
        final String addDAte = now.format(dtf);

        movieViewModel.getAllMovies().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable final List<Movie> movies)
            {
                Map<String,String> movieInDB =  new HashMap<>();
                for (Movie temp : movies) {
                    movieInDB.put(temp.getMovieName(),temp.releaseDate);
                }
                boolean containsWatchlist = (movieInDB.containsKey(mName) && movieInDB.containsValue(reDate));
                Log.d("Flloyd", "Temp : "+ containsWatchlist);

                if (containsWatchlist)
                {
                    Toast.makeText(getContext(), "Movie Already in Watchlist!", Toast.LENGTH_SHORT).show();
                }
                else{
                Movie movie = new Movie(mName, reDate,addDAte);
                movieViewModel.insert(movie);
                Toast.makeText(getContext(), "Added to Watchlist!", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    private void addToMemoir(View view, String movieName, String releasedDate, String imageURL){
        Log.d("Flloyd", "Movie Memoir");
        Bundle bundle = new Bundle();
        bundle.putString("MovieName",movieName);
        bundle.putString("ReleasedDate",releasedDate);
        bundle.putString("Image",imageURL);
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,AddToMemoir.class,bundle);
        fragmentTransaction.addToBackStack("tag");
        fragmentTransaction.commit();
    }


    private void share(@NotNull View view){
        Context context = view.getContext();
        Activity now = (Activity)context;
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(Uri.parse(posterURL), "image/*");
        intent.putExtra("content_url", Uri.parse(posterURL));
        intent.putExtra("top_background_color", "#FFFFFF");
        intent.putExtra("bottom_background_color", "#FFFFFF");
        context.startActivity(Intent.createChooser(intent, "Title"));
    }

}
