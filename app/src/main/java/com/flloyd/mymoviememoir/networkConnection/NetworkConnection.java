package com.flloyd.mymoviememoir.networkConnection;

import android.util.Log;

import com.flloyd.mymoviememoir.M3Model.Credentials;
import com.flloyd.mymoviememoir.M3Model.Person;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            "http://10.0.2.2:8080/Assignment1/webresources/";

    //"http://10.0.2.2:8080/Assignment1/webresources/restws.person/";


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


    public String register(String fName, String lName, String gender, String DOB, String streetAddress, String stateCode, String postCode, String email, String password) {
        Person person = new Person(5,fName,lName,gender,DOB,streetAddress,stateCode,postCode);
        Gson gson = new Gson();
        String personJson = gson.toJson(person);
        String strResponse="";

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

        if(strResponse.trim().isEmpty()) {

            Credentials credentials = new Credentials(5, email, password, "2018-12-01");
            credentials.setPersonid(person);
            String credentialsJson = gson.toJson(credentials);
            String newStrResponse="";

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
            } catch (Exception e) {
                e.printStackTrace();
            }

            return newStrResponse;
        }else
            return strResponse;
    }


}