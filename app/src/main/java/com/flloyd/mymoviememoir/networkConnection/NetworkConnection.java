package com.flloyd.mymoviememoir.networkConnection;

import android.util.Log;

import com.flloyd.mymoviememoir.M3Model.Cinema;
import com.flloyd.mymoviememoir.M3Model.CinemaID;
import com.flloyd.mymoviememoir.M3Model.Credentials;
import com.flloyd.mymoviememoir.M3Model.CredentialsID;
import com.flloyd.mymoviememoir.M3Model.Memoir;
import com.flloyd.mymoviememoir.M3Model.Person;
import com.flloyd.mymoviememoir.M3Model.PersonID;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkConnection {
    private OkHttpClient client = null;
    private String results;
    public static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");
    public NetworkConnection(){
        client=new OkHttpClient();
    }
    private static final String BASE_URL =
            "http://10.0.2.2:8080/Assignment1Modified/webresources/";


    public JSONArray getCredentials(String email, String password) {
        final String methodPath = "restws.credentials/Authenticate/" + email + "/" + password;
        Log.i("Flloyd", "request" + methodPath);
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);

        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(results);
        } catch (Exception e) {
            Log.i("Flloyd :","Test JSON Array: ");
            return null;
        }

        return jsonArray;
    }


    public JSONArray emailChecker(String email) {
        final String methodPath = "restws.credentials/findByUsername/" + email;
        Log.i("Flloyd", "request" + methodPath);
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);

        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(results);
        } catch (Exception e) {
            return null;
        }
        return jsonArray;
    }



    private String personIDFinder(String streetAddress) throws JSONException {
        final String methodPath = "restws.person/findByStreetaddress/" + streetAddress;
        Log.i("Flloyd", "request" + methodPath);
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);

        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("json " , "Result of Finder:" + results);

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(results);
        } catch (Exception e) {
            return null;
        }
        return  jsonArray.getJSONObject(0).getString("personid");
    }



    private String credentialsIDFinder(String username) throws JSONException {
        final String methodPath = "restws.credentials/findByUsername/" + username;
        Log.i("Flloyd", "request" + methodPath);
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);

        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("json " , "Result of Finder:" + results);

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(results);
        } catch (Exception e) {
            return null;
        }
        return  jsonArray.getJSONObject(0).getString("credentialid");
    }


    public String addCinema(String cinemaName, String postcode){
        Cinema cinema = new Cinema(cinemaName,postcode);
        Gson gson = new Gson();
        String cinemaJson = gson.toJson(cinema);
        String strResponse = "NULL";
        Log.i("json " , cinemaJson);
        String methodPath = "restws.cinema/";
        RequestBody body = RequestBody.create(cinemaJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response= client.newCall(request).execute();
            strResponse= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }

        if(strResponse.trim().isEmpty())
            return "OK";
        else
            return  "ERROR";
    }


    public String register(String fName, String lName, String gender, String DOB, String streetAddress,
                           String stateCode, String postCode, String email, String password)  {

        Person person = new Person(fName,lName,gender,DOB,streetAddress,stateCode,postCode);
        Gson gson = new Gson();
        String personJson = gson.toJson(person);
        String strResponse = "NULL";

        Log.i("json " , personJson);
        String methodPath = "restws.person/";

        RequestBody body = RequestBody.create(personJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response= client.newCall(request).execute();
            strResponse= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }


        String personID = null;
        try {
            personID = personIDFinder(streetAddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(strResponse.trim().isEmpty() && personID != null) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String signUpDate = now.format(dtf);
            Credentials credentials = new Credentials(email, password,signUpDate);
            PersonID personID1 = new PersonID(personID,fName,lName,gender,DOB,streetAddress,stateCode,postCode);
            credentials.setPersonid(personID1);
            String credentialsJson = gson.toJson(credentials);
            String newStrResponse="NULL";

            Log.i("json " , credentialsJson);
            String methodPath2 = "restws.credentials/";
            RequestBody body2 = RequestBody.create(credentialsJson, JSON);
            Request request2 = new Request.Builder()
                    .url(BASE_URL + methodPath2)
                    .post(body2)
                    .build();
            try {
                Response response2= client.newCall(request2).execute();
                newStrResponse= response2.body().string();
                Log.i("Flloyd :" ,"newStrResponse: " + newStrResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String credentialId = null;
            try {
                credentialId = credentialsIDFinder(email);

            } catch (JSONException e) {
                e.printStackTrace();
            }

             if (newStrResponse.trim().isEmpty() && credentialId != null) {
                 CredentialsID credentialsID = new CredentialsID(credentialId,email,password,signUpDate);
                 credentialsID.setPersonid(personID1);
                 return gson.toJson(credentialsID);
             }
             else
                 return "ERROR";
        }else
            return "ERROR";
    }



    public JSONArray topMovies (String personId) {
        final String methodPath = "restws.memoir/topFiveMovies/" + personId;
        Log.i("Flloyd", "request" + methodPath);
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);

        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(results);
        } catch (Exception e) {
            return null;
        }
        return jsonArray;
    }


    public JSONArray getCinema() {
        final String methodPath = "restws.cinema";
        Log.i("Flloyd", "request" + methodPath);
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(results);
        } catch (Exception e) {
            return null;
        }
        return jsonArray;
    }

    public String addMemoir( String moviename, String releasedate, String watchdate, String watchtime,
                             String review, String rating,String personid,String cinemaid){

        PersonID personID;
        CinemaID cinemaID;
        try {
            JSONObject person = new JSONObject(personid);
            String personId = person.getString("personid");
            String personFName = person.getString("personfname");
            String personLName = person.getString("personlname");
            String Gender = person.getString("gender");
            String DOB = person.getString("dob");
            String StreetAddress = person.getString("streetaddress");
            String state = person.getString("statecode");
            String postCode = person.getString("postcode");
            personID = new PersonID(personId, personFName, personLName, Gender, "REPLACE", StreetAddress, state, postCode);
            personID.setDob(DOB);


            JSONObject cinema = new JSONObject(cinemaid);
            String cinemaId = cinema.getString("cinemaid");
            String cinemaName =cinema.getString("cinemaname");
            String CinemaPostCode = cinema.getString("cinemapostcode");
            cinemaID = new CinemaID(cinemaId,cinemaName,CinemaPostCode);

        }
        catch (Exception e){
            return "ERROR";
        }

        Memoir memoir = new Memoir(moviename,releasedate,watchdate,watchtime,review,rating);
        memoir.setPersonid(personID);
        memoir.setCinemaid(cinemaID);
        Gson gson = new Gson();
        String memoirJson = gson.toJson(memoir);
        String strResponse = "NULL";
        Log.i("json " , memoirJson);
        String methodPath = "restws.memoir/";
        RequestBody body = RequestBody.create(memoirJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response= client.newCall(request).execute();
            strResponse= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }

        if(strResponse.trim().isEmpty())
            return "OK";
        else
            return  "ERROR";
    }


    public JSONArray getMemoir(String personID) {
        final String methodPath = "restws.memoir/findByPersonID/" + personID;
        Log.i("Flloyd", "request" + methodPath);
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(results);
        } catch (Exception e) {
            return null;
        }
        return jsonArray;
    }

    public JSONArray getMoviesWatchedPerPostCode(String personID,String startDate,String endDate) {
        final String methodPath = "restws.memoir/moviesWatchedPerPostCode/" + personID + "/" + startDate + "/" + endDate;
        Log.i("Flloyd", "request" + methodPath);
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(results);
        } catch (Exception e) {
            return null;
        }
        return jsonArray;
    }

}