package com.flloyd.mymoviememoir.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.networkConnection.NetworkConnection;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputLayout;

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

public class PieChartFragment  extends Fragment {

    private String personID = "0";
    private EditText startDate, endDate;
    private PieChart pieChart;
    private List<PieEntry> value;
    private final Calendar myCalendar = Calendar.getInstance();
    private NetworkConnection networkConnection = null;

    public PieChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.pie_chart_fragment, container, false);

        SharedPreferences sharedPref= requireActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String credential= sharedPref.getString("Credential",null);
        Log.i("Flloyd: ", "DATA in Report :" + credential);
        try {
            JSONArray jsonArrayCredentials = new JSONArray(credential);
            JSONObject j = (JSONObject) jsonArrayCredentials.getJSONObject(0).get("personid");
            personID = j.getString("personid");
        } catch (Exception ignored) { }

        pieChart = view.findViewById(R.id.pieChart1);
        startDate = view.findViewById(R.id.startDate);
        endDate = view.findViewById(R.id.endDate);
        Button set = view.findViewById(R.id.setDate);
        networkConnection = new NetworkConnection();



        final DatePickerDialog.OnDateSetListener startDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                startDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        final DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                endDate.setText(sdf.format(myCalendar.getTime()));
            }
        };


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(view.getContext(),startDatePicker , myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(view.getContext(),endDatePicker , myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startDateText =  startDate.getText().toString();
                String endDateText = endDate.getText().toString();
                if (validateDates(view,startDateText,endDateText))
                {
                    GetMoviesWatchedPerPostCode getMoviesWatchedPerPostCode = new GetMoviesWatchedPerPostCode();
                    getMoviesWatchedPerPostCode.execute(personID,startDateText,endDateText);
                }


            }
        });


        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12f);


        value = new ArrayList<>();
        Description description = new Description();
        description.setText("Movies Watched Per Post Code");
        pieChart.setDescription(description);
        pieChart.animateXY(1400,1400);


        initalise();

        return view;
    }

    private class GetMoviesWatchedPerPostCode extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(@NotNull String... params) {
            return networkConnection.getMoviesWatchedPerPostCode(params[0],params[1],params[2]);
        }
        @Override
        protected void onPostExecute(JSONArray result) {

            if (result!= null && !result.isNull(0)) {
                Log.i("Flloyd: ", "DATA :" + result.toString());
                value.clear();

                for( int i = 0 ; i < result.length(); i++ ){

                    try {
                        String cinemaPostCode = result.getJSONObject(i).getString("cinemaPostCode");
                        int totalMovies = (int)result.getJSONObject(i).get("totalMovies");
                        Log.i("Flloyd: ", "Test :" + cinemaPostCode +" " + totalMovies);
                        value.add(new PieEntry(totalMovies,cinemaPostCode));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                PieDataSet pieDataSet = new PieDataSet(value,"Post Codes");
                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.invalidate();
            }

        }
    }


    private void initalise(){
        String startDateText = "2019-01-01";
        String endDateText = "2020-05-10";
        startDate.setText(startDateText);
        endDate.setText(endDateText);
        GetMoviesWatchedPerPostCode getMoviesWatchedPerPostCode = new GetMoviesWatchedPerPostCode();
        getMoviesWatchedPerPostCode.execute(personID,startDateText,endDateText);
    }

    private boolean validateDates(@NotNull View view, @NotNull String startDate, String endDate){

        boolean valid = true;
        TextInputLayout dateLayout = view.findViewById(R.id.input_layout_startDate);
        if (!startDate.isEmpty()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date d1;
            Date current = new Date(System.currentTimeMillis());
            try {
                d1 = sdf.parse(startDate);
                assert d1 != null;
                if(d1.compareTo(current) > 0) {
                    dateLayout.setError("Invalid Date");
                    valid = false;
                } else{
                    dateLayout.setError(null);
                }
            } catch (ParseException e) {
                dateLayout.setError("Invalid Date");
                valid = false;
            }
        }else{
            dateLayout.setError("Select Date");
            valid = false;
        }


        TextInputLayout endDateLayout = view.findViewById(R.id.input_layout_endingDate);
        if (!endDate.isEmpty()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date d1;
            Date d2;
            Date current = new Date(System.currentTimeMillis());
            try {
                d1 = sdf.parse(startDate);
                d2 = sdf.parse(endDate);
                assert d1 != null;
                assert d2 != null;
                if(d1.compareTo(d2) > 0 || d2.compareTo(current) > 0) {
                    endDateLayout.setError("Invalid Date");
                    valid = false;
                } else{
                    endDateLayout.setError(null);
                }
            } catch (ParseException e) {
                endDateLayout.setError("Invalid Date");
                valid = false;
            }
        }else{
            endDateLayout.setError("Select Date");
            valid = false;
        }

        return valid;
    }


}