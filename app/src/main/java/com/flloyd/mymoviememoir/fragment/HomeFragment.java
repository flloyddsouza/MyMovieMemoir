package com.flloyd.mymoviememoir.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flloyd.mymoviememoir.DataModel.TopMovie;
import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.adapter.RecyclerViewAdapter;
import com.flloyd.mymoviememoir.networkConnection.NetworkConnection;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private NetworkConnection networkConnection = null;
    private TextView userName, currentDate;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<TopMovie> topMovies;
    private RecyclerViewAdapter adapter;



    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.home_fragment, container, false);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd yyyy");
        LocalDateTime now = LocalDateTime.now();
        String Date = now.format(dtf);


        networkConnection = new NetworkConnection();
        topMovies = new ArrayList<>();

        userName = view.findViewById(R.id.textView);
        currentDate = view.findViewById(R.id.dateText);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(topMovies);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        String id, firstName;
        SharedPreferences sharedPref= getActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String credential= sharedPref.getString("Credential",null);
        Log.i("Flloyd: ", "DATA in Home :" + credential);
        try {
            JSONArray jsonArrayCredentials = new JSONArray(credential);
            id = jsonArrayCredentials.getJSONObject(0).getString("credentialid");
            JSONObject j = (JSONObject) jsonArrayCredentials.getJSONObject(0).get("personid");
            firstName = j.getString("personfname");
        } catch (Exception e) {
            id = "0";
            firstName = "User0";
        }

        Log.i("Flloyd: ", "First Name:" + firstName);
        Log.i("Flloyd: ", "Date:" + Date);
        Log.i("Flloyd: ", "ID:" + id);
        String welcome = "Hello, \n" + firstName;
        userName.setText(welcome);
        currentDate.setText(Date);

        displayTopMovies displayTopMovies = new displayTopMovies();
        displayTopMovies.execute(id);


        return view;
    }

    private class displayTopMovies extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(@NotNull String... params) {
            return networkConnection.topMovies(params[0]);
        }
        @Override
        protected void onPostExecute(JSONArray result) {

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String movieName = null,ratingText = null, dateText = null;
            Date releaseDate = null;
            Double rating = 0.0;
            if (result!= null && !result.isNull(0)) {
                for (int i = 0; i < result.length(); i++) {

                    try {
                        movieName = result.getJSONObject(i).getString("movieName");
                        ratingText = result.getJSONObject(i).getString("rating");
                        dateText = result.getJSONObject(i).getString("releaseDate");
                        rating = Double.parseDouble(ratingText);
                        releaseDate = format.parse(dateText);
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                    saveData(movieName, rating, releaseDate);
                }
            }
        }
    }


    private void saveData(String movieName, Double rating, Date releaseDate) {
        TopMovie topMovie = new TopMovie(movieName, rating, releaseDate);
        topMovies.add(topMovie);
        adapter.addTopMovies(topMovies);
    }

}

