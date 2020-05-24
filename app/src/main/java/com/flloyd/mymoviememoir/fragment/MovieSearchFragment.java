package com.flloyd.mymoviememoir.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flloyd.mymoviememoir.DataModel.SearchResult;
import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.adapter.RecyclerSearchMovieAdapter;
import com.flloyd.mymoviememoir.networkConnection.theMovieDbAPI;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieSearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    private SearchView movieSearch;
    final static String IMAGE_URL = "https://image.tmdb.org/t/p/w600_and_h900_bestv2";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<SearchResult> searchResultList;
    private RecyclerSearchMovieAdapter adapter;
    public MovieSearchFragment() {

    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        searchResultList = new ArrayList<>();
        View view = inflater.inflate(R.layout.movie_search_fragment, container, false);

        movieSearch = view.findViewById(R.id.movieSearchView);
        movieSearch.setOnQueryTextListener(this);
        recyclerView = view.findViewById(R.id.recyclerViewSearch);
        adapter = new RecyclerSearchMovieAdapter(searchResultList);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }


    private class queryConfirm extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(@NotNull String... params) {
            return theMovieDbAPI.search(params[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                searchResultList.clear();
                JSONObject jsonObject = new JSONObject(result);
                String arrayString = jsonObject.get("results").toString();
                JSONArray jsonArray = new JSONArray(arrayString);

                for (int i = 0; i< jsonArray.length() && i< 5; i++ ){
                    String name = jsonArray.getJSONObject(i).getString("title");
                    String date = jsonArray.getJSONObject(i).getString("release_date").substring(0,4);
                    String image = IMAGE_URL + jsonArray.getJSONObject(i).getString("poster_path");
                    String backdropImage = IMAGE_URL + jsonArray.getJSONObject(i).getString("backdrop_path");
                    Log.i("Flloyd", "Name: "  + name + date + image);
                    saveData(name,date,image,backdropImage);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        queryConfirm queryConfirm = new queryConfirm();
        queryConfirm.execute(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void saveData(String movieName, String year, String image,String backdropImage) {
        SearchResult searchResult = new SearchResult(movieName,year.trim(),image,backdropImage);
        searchResultList.add(searchResult);
        adapter.addSearchResult(searchResultList);
    }
}
