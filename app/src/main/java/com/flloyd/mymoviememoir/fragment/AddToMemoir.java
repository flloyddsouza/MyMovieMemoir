package com.flloyd.mymoviememoir.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.flloyd.mymoviememoir.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddToMemoir extends Fragment {

    EditText watchDate, watchTime;
    final Calendar myCalendar = Calendar.getInstance();
    public AddToMemoir() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_to_memoir_layout, container, false);

        final String movieName = this.getArguments().getString("MovieName");
        final String releasedDate = this.getArguments().getString("ReleasedDate");
        final String imageURL = this.getArguments().getString("Image");
        Log.i("Flloyd: ", "DATA in Memoir :" + movieName + "\n" + releasedDate + "\n" + imageURL);

        ImageView poster = view.findViewById(R.id.imagePosterAdd);
        TextView movieNameTV = view.findViewById(R.id.movieNameAdd);
        TextView releasedDateTV = view.findViewById(R.id.releaseDateAdd);
        watchDate = view.findViewById(R.id.watchDate);
        watchTime = view.findViewById(R.id.watchTime);



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                watchDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR,hourOfDay);
                myCalendar.set(Calendar.MINUTE,minute);
                String myFormat = "hh.mm aa";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
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
                        false).show();
            }
        });

        String id, firstName;
        SharedPreferences sharedPref= getActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String credential= sharedPref.getString("Credential",null);
        Log.i("Flloyd: ", "DATA in Memoir :" + credential);
        try {
            JSONArray jsonArrayCredentials = new JSONArray(credential);
            id = jsonArrayCredentials.getJSONObject(0).get("credentialid").toString();
            JSONObject j = (JSONObject) jsonArrayCredentials.getJSONObject(0).get("personid");
            firstName = j.get("personfname").toString();
        } catch (Exception e) {
            id = "0";
            firstName = "User0";
        }

        movieNameTV.setText(movieName);
        releasedDateTV.setText(releasedDate);
        DownLoadImageTask downLoadImageTask = new DownLoadImageTask(poster);
        downLoadImageTask.execute(imageURL);




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




}