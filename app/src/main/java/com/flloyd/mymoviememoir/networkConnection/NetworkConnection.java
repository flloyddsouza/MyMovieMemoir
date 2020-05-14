package com.flloyd.mymoviememoir.networkConnection;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
        Log.i("Flloyd", "request" + methodPath.toString());
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
}