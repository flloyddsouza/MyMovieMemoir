package com.flloyd.mymoviememoir.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flloyd.mymoviememoir.DataModel.MemoirCard;
import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.adapter.RecyclerMemoirAdapter;
import com.flloyd.mymoviememoir.networkConnection.NetworkConnection;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MovieMemoirFragment extends Fragment {

    private NetworkConnection networkConnection = null;
    private RecyclerView.Adapter mAdapter;
    private List<MemoirCard> memoirCardList;
    private RecyclerMemoirAdapter adapter;
    private String personID = "0";


    public MovieMemoirFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        networkConnection = new NetworkConnection();
        View view = inflater.inflate(R.layout.movie_memoir_fragment, container, false);
        memoirCardList = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewMemoirList);
        adapter = new RecyclerMemoirAdapter(memoirCardList);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);



        SharedPreferences sharedPref= getActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String credential= sharedPref.getString("Credential",null);
        Log.i("Flloyd: ", "DATA in Home :" + credential);
        try {
            JSONArray jsonArrayCredentials = new JSONArray(credential);
            JSONObject j = (JSONObject) jsonArrayCredentials.getJSONObject(0).get("personid");
            personID = j.getString("personid");
        } catch (Exception ignored) { }

        getMemoir getMemoir = new getMemoir(0);
        getMemoir.execute(personID);



        return view;
    }

    private class getMemoir extends AsyncTask<String, Void, JSONArray> {
        int sort;

        getMemoir(int sort) {
            this.sort = sort;
        }

        @Override
        protected JSONArray doInBackground(@NotNull String... params) {
            return networkConnection.getMemoir(params[0]);
        }

        @Override
        protected void onPostExecute(JSONArray result) {

            if (result!= null && !result.isNull(0)) {
                memoirCardList.clear();
                JSONArray sortedJsonArray;
                switch (sort){
                    case 1:
                        sortedJsonArray  = sortByDate(result);
                        break;
                    case 2:
                        sortedJsonArray = sortRatingUser(result);
                        break;
                    default:
                        sortedJsonArray =result;
                }

                for( int i = 0 ;i < result.length() ; i++){

                    try {
                        String releaseDate = sortedJsonArray.getJSONObject(i).getString("releasedate").substring(0,10);
                        String watchDate = sortedJsonArray.getJSONObject(i).getString("watchdate").substring(0,10);
                        String cinemaID =  sortedJsonArray.getJSONObject(i).getString("cinemaid");
                        JSONObject cinemaObj = new JSONObject(cinemaID);
                        String postCode = cinemaObj.getString("cinemapostcode");
                        float rating = Float.parseFloat( sortedJsonArray.getJSONObject(i).getString("rating"));


                        MemoirCard memoirCard = new MemoirCard(sortedJsonArray.getJSONObject(i).getString("moviename"),
                                                               releaseDate, watchDate, postCode,
                                sortedJsonArray.getJSONObject(i).getString("review"), rating);

                        memoirCardList.add(memoirCard);
                        adapter.addMemoirListItem(memoirCardList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

        }
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.sorting_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.by_date:
                Toast.makeText(getActivity(), "Sorted By Date", Toast.LENGTH_SHORT).show();
                getMemoir getMemoir = new getMemoir(1);
                getMemoir.execute(personID);
                return true;
            case R.id.by_rating_user:
                Toast.makeText(getActivity(), "Sorted By Your Rating", Toast.LENGTH_SHORT).show();
                getMemoir getMemoir1 = new getMemoir(2);
                getMemoir1.execute(personID);
                return true;
            case R.id.by_rating_api:
                Toast.makeText(getActivity(), "Sorted By Public Rating", Toast.LENGTH_SHORT).show();
                getMemoir getMemoir3 = new getMemoir(2);
                getMemoir3.execute(personID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private JSONArray sortByDate(@NotNull JSONArray result){

        List list = new ArrayList();
        for(int i = 0; i < result.length(); i++) {
            try {
                list.add(result.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONArray sortedJsonArray = new JSONArray();
        Collections.sort(list, new Comparator() {
            private static final String KEY_NAME = "watchdate";
            @Override
            public int compare(Object o1, Object o2) {
                String str1 = "";
                String str2 = "";
                try {
                    JSONObject a = (JSONObject) o1;
                    JSONObject b = (JSONObject) o2;

                    str1 = (String) a.get(KEY_NAME);
                    str2 = (String) b.get(KEY_NAME);

                } catch(JSONException e) {
                    e.printStackTrace();
                }
                return str2.compareTo(str1);
            }
        });

        for(int i = 0; i < result.length(); i++) {
            sortedJsonArray.put(list.get(i));
        }

        return sortedJsonArray;
    }


    private JSONArray sortRatingUser(@NotNull JSONArray result){

        List list = new ArrayList();
        for(int i = 0; i < result.length(); i++) {
            try {
                list.add(result.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONArray sortedJsonArray = new JSONArray();
        Collections.sort(list, new Comparator() {
            private static final String KEY_NAME = "rating";
            @Override
            public int compare(Object o1, Object o2) {
                Double str1 = 0.0;
                Double str2 = 0.0;
                try {
                    JSONObject a = (JSONObject) o1;
                    JSONObject b = (JSONObject) o2;

                    str1 = (Double) a.get(KEY_NAME);
                    str2 = (Double) b.get(KEY_NAME);

                } catch(JSONException e) {
                    e.printStackTrace();
                }
                return str2.compareTo(str1);
            }
        });

        for(int i = 0; i < result.length(); i++) {
            sortedJsonArray.put(list.get(i));
        }

        return sortedJsonArray;
    }


}
