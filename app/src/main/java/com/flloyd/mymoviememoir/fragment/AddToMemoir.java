package com.flloyd.mymoviememoir.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.flloyd.mymoviememoir.M3Model.CinemaID;
import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.networkConnection.NetworkConnection;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddToMemoir extends Fragment {

    private EditText watchDate, watchTime,comment;
    private Spinner cinemaSpinner;
    private RatingBar ratingBar;
    private final Calendar myCalendar = Calendar.getInstance();
    private NetworkConnection networkConnection = null;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private View LayoutView;
    private List<CinemaID> cinemaObjectList = new ArrayList<>();

    public AddToMemoir() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_to_memoir_fragment, container, false);
        LayoutView = view;
        final String movieName = this.getArguments().getString("MovieName");
        final String releasedDate = this.getArguments().getString("ReleasedDate");
        final String imageURL = this.getArguments().getString("Image");
        Log.i("Flloyd: ", "DATA in Memoir :" + movieName + "\n" + releasedDate + "\n" + imageURL);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String rDate = "";
        try {
            assert releasedDate != null;
            Date date = sdf.parse(releasedDate);
            SimpleDateFormat newSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            rDate = newSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ImageView poster = view.findViewById(R.id.imagePosterAdd);
        TextView movieNameTV = view.findViewById(R.id.movieNameAdd);
        TextView releasedDateTV = view.findViewById(R.id.releaseDateAdd);
        Button addCinema = view.findViewById(R.id.addCinema);
        Button submit = view.findViewById(R.id.submit);

        watchDate = view.findViewById(R.id.watchDate);
        watchTime = view.findViewById(R.id.watchTime);
        cinemaSpinner = view.findViewById(R.id.cinema_Spinner);
        comment = view.findViewById(R.id.comment);
        ratingBar = view.findViewById(R.id.ratingMemoir);
        networkConnection = new NetworkConnection();

        List<String> cinemaList = new ArrayList<>();
        spinnerArrayAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, cinemaList);
        cinemaSpinner.setAdapter(spinnerArrayAdapter);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                watchDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR,hourOfDay);
                myCalendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
                watchTime.setText(sdf.format(myCalendar.getTime()));
            }
        };


        watchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(view.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        watchTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(view.getContext(),time,
                        myCalendar.get(Calendar.HOUR),myCalendar.get(Calendar.HOUR),
                        true).show();
            }
        });

        addCinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(view.getContext());
                builder.setTitle("Add New Cinema");
                final View customLayout = getLayoutInflater().inflate(R.layout.add_cinema_layout, null);
                builder.setView(customLayout);
                builder.setPositiveButton("ADD",null);
                builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText cinemaNameET = customLayout.findViewById(R.id.cinemaName);
                        EditText cinemaPostCodeET = customLayout.findViewById(R.id.cinemaPostCode);
                        String cinemaName = cinemaNameET.getText().toString();
                        String cinemaPostCode = cinemaPostCodeET.getText().toString();
                        if(validateCinema(customLayout,cinemaName,cinemaPostCode)){
                            AddToCinema addToCinema = new AddToCinema();
                            addToCinema.execute(cinemaName,cinemaPostCode);
                            Log.i("Flloyd","test"+cinemaNameET.getText().toString());
                            dialog.dismiss();
                        }
                    }
                });
            }
        });



        SharedPreferences sharedPref= getActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String credential = sharedPref.getString("Credential",null);
        String personID ="";
        try {
            JSONArray jsonArrayCredentials = new JSONArray(credential);
            personID = jsonArrayCredentials.getJSONObject(0).getString("personid");
        } catch (Exception e) {
            e.printStackTrace();
        }

        movieNameTV.setText(movieName);
        releasedDateTV.setText(releasedDate);
        DownLoadImageTask downLoadImageTask = new DownLoadImageTask(poster);
        downLoadImageTask.execute(imageURL);
        getCinema getCinema = new getCinema();
        getCinema.execute();

        final String finalPersonID = personID;
        final String finalRDate = rDate;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = watchDate.getText().toString();
                String time = watchTime.getText().toString();
                String comments = comment.getText().toString();
                String cinemaID = cinemaJSON(cinemaSpinner.getSelectedItem().toString());
                float rating = ratingBar.getRating();
                String ratingStar = Float.toString(rating);

                if( validateMemoir(view,date,time,comments,rating)){
                    Log.i("Flloyd ", "Memoir Data " + movieName + "\n" + finalRDate + "\n" + date + "\n" + time + "\n" + comments + "\n" + ratingStar + "\n" + finalPersonID + "\n" + cinemaID );
                    AddToMemoirDatabase addToMemoirDatabase = new AddToMemoirDatabase();
                    addToMemoirDatabase.execute(movieName,finalRDate,date,time,comments,ratingStar,finalPersonID,cinemaID);
                }
            }
        });

        return view;
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

    private class getCinema extends AsyncTask<String,Void,JSONArray> {
        @Override
        protected JSONArray doInBackground(@NotNull String... params) {
            return networkConnection.getCinema();
        }
        @Override
        protected void onPostExecute(JSONArray result) {
            if (result != null){
                Log.i("Flloyd ","JSON ARRAY: "+ result.toString());
                spinnerArrayAdapter.clear();
                cinemaObjectList.clear();
                try {
                    for (int i = 0; i < result.length(); i++){
                        String cinemaId = result.getJSONObject(i).getString("cinemaid");
                        String cinemaName = result.getJSONObject(i).getString("cinemaname");
                        String cinemaPostCode = result.getJSONObject(i).getString("cinemapostcode");
                        CinemaID cinemaID = new CinemaID(cinemaId,cinemaName,cinemaPostCode);
                        cinemaObjectList.add(cinemaID);
                        spinnerArrayAdapter.add(cinemaName);
                    }
                    spinnerArrayAdapter.notifyDataSetChanged();
                }catch (Exception ignored){
                }
            }
        }
    }

    private class AddToCinema extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(@NotNull String... params) {
            return networkConnection.addCinema(params[0],params[1]);
        }
        @Override
        protected void onPostExecute(String result) {
            Log.i("Flloyd ","result: " + result);
            if (result.equals("OK")) {
                Toast.makeText(getContext(), "Cinema Added", Toast.LENGTH_SHORT).show();
                getCinema getCinema = new getCinema();
                getCinema.execute();
            }
            else if(result.equals("ERROR")) {
                Toast.makeText(getContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class AddToMemoirDatabase extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(@NotNull String... params) {
            return networkConnection.addMemoir(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7]);
        }
        @Override
        protected void onPostExecute(String result) {
            Log.i("Flloyd ","result: " + result);
            if (result.equals("OK")) {
                Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                newMemoirFragment(LayoutView);
            }
            else if(result.equals("ERROR")) {
                Toast.makeText(getContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private boolean validateCinema(View view, String cinemaName,String cinemaPostCode){

        TextInputLayout cinemaNameLayout = view.findViewById(R.id.input_layout_cinema_name);
        boolean valid = true;
        if(cinemaName.trim().isEmpty()){
            cinemaNameLayout.setError("Cinema name cannot be empty");
            valid = Boolean.FALSE;
        }else if(cinemaName.length() > 20){
            cinemaNameLayout.setError("Cinema name cannot exceed 20 characters");
            valid = Boolean.FALSE;
        }
        else {
            cinemaNameLayout.setError(null);
        }

        TextInputLayout cinemaPostCodeLayout = view.findViewById(R.id.input_layout_cinema_postCode);
        if (cinemaPostCode.trim().isEmpty()){
            cinemaPostCodeLayout.setError("Post Code cannot be empty");
            valid = Boolean.FALSE;
        }else if (cinemaPostCode.length() != 4){
            cinemaPostCodeLayout.setError("Enter a valid Post Code");
            valid = Boolean.FALSE;
        }else {
            cinemaPostCodeLayout.setError(null);
        }
        return valid;
    }


    private boolean validateMemoir(View view, String date,String time,String comments,float rating){

        boolean valid = true;
        TextInputLayout dateLayout = view.findViewById(R.id.input_layout_watchDate);
        if (!date.isEmpty()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date d1 = null;
            Date current = new Date(System.currentTimeMillis());
            try {
                d1 = sdf.parse(date);
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

        TextInputLayout timeLayout = view.findViewById(R.id.input_layout_watchTime);
        if(time.trim().isEmpty()){
            timeLayout.setError("Select Time");
            valid = false;
        }else{
            timeLayout.setError(null);
        }

        TextInputLayout commentLayout = view.findViewById(R.id.input_layout_comment);
        if(comments.trim().isEmpty()){
            commentLayout.setError("Enter Comments");
            valid = false;
        }else if (comments.length() > 200){
            commentLayout.setError("Cannot exceed 200 Characters");
            valid = false;
        }else{
            commentLayout.setError(null);
        }

        if(rating == 0.00){
            Toast.makeText(getContext(), "Select Rating", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }


    private String cinemaJSON(String cinemaName){
        CinemaID cinema = null;
        for(CinemaID c : cinemaObjectList){
            if (c.getCinemaname().equals(cinemaName))
                 cinema = c;
        }
        Gson gson = new Gson();
        return gson.toJson(cinema);
    }

    private void newMemoirFragment(@NotNull View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new MovieMemoirFragment());
        fragmentTransaction.commit();
    }

}