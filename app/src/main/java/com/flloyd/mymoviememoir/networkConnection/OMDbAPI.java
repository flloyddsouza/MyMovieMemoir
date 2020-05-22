package com.flloyd.mymoviememoir.networkConnection;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OMDbAPI {

    private static final String API_KEY = "5e7bb8ce";
    public static String search(String keyword) {
        keyword = keyword.replace(" ", "+");
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";

        try {
            url = new URL("http://www.omdbapi.com/?t=" + keyword + "&apikey=" + API_KEY + "&type=movie");

            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
            Log.i("Flloyd ", "Result OF OMDb API: " +textResult);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }
        return textResult;
    }

}
