package com.flloyd.mymoviememoir.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.networkConnection.NetworkConnection;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BarFragment extends Fragment {

    private String personID = "0";
    private BarChart barChart;
    private List<BarEntry> barEntryList;

    private NetworkConnection networkConnection = null;


    public BarFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bar_fragment, container, false);
        SharedPreferences sharedPref= requireActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String credential= sharedPref.getString("Credential",null);
        Log.i("Flloyd: ", "DATA in Report :" + credential);
        try {
            JSONArray jsonArrayCredentials = new JSONArray(credential);
            JSONObject j = (JSONObject) jsonArrayCredentials.getJSONObject(0).get("personid");
            personID = j.getString("personid");
        } catch (Exception ignored) { }

        Spinner spinner = view.findViewById(R.id.yearSpinner);
        barChart = view.findViewById(R.id.BarChart);
        networkConnection = new NetworkConnection();

        int year = Calendar.getInstance().get(Calendar.YEAR);
        List<String> years = new ArrayList<>();
        for(int i = 0;i < 6;i++ ) {
           years.add(String.valueOf(year));
           year--;
        }

        ArrayAdapter mAdapter = new ArrayAdapter<String>(this.requireContext(), R.layout.support_simple_spinner_dropdown_item, years);
        mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(mAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedYear = (String) adapterView.getItemAtPosition(i);
                GetMoviesWatchedPerMonth getMoviesWatchedPerMonth = new GetMoviesWatchedPerMonth();
                getMoviesWatchedPerMonth.execute(personID,selectedYear);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        barEntryList = new ArrayList<>();
        return view;
    }




    private class GetMoviesWatchedPerMonth extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(@NotNull String... params) {
            return networkConnection.getMoviesWatchedPerMonth(params[0],params[1]);
        }
        @Override
        protected void onPostExecute(JSONArray result) {


            barEntryList.clear();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.US);
            SimpleDateFormat newsdf = new SimpleDateFormat("MM", Locale.US);

            if (result!= null && !result.isNull(0)) {
                Log.i("Flloyd: ", "DATA :" + result.toString());
                ArrayList<String> months = new ArrayList<>();
                for( int i = 0 ; i < result.length(); i++ ){

                    try {
                        String month = result.getJSONObject(i).getString("month");
                        Date d1 = sdf.parse(month);
                        assert d1 != null;
                        String monthNumber = newsdf.format(d1);
                        int tempMonth = Integer.parseInt(monthNumber);
                        int temp = (int)result.getJSONObject(i).get("totalMovies");
                        float totalMovies = (float)temp;
                        Log.i("Flloyd: ", "Test :" + tempMonth +" " + totalMovies);
                        barEntryList.add(new BarEntry(tempMonth,totalMovies));
                        months.add(month);


                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }

                BarDataSet barDataSet = new BarDataSet(barEntryList,"Number of Movies");

                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(18f);
                BarData data = new BarData(barDataSet);
                barChart.setData(data);
                barChart.invalidate();
                barChart.animateY(1400);

            }

        }
    }

}
