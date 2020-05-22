package com.flloyd.mymoviememoir.networkConnection;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class theMovieDbAPI {
    private static final String API_KEY = "6c41d9157a4e415ac7171a8706691d73";
    public static String search(String keyword) {
        keyword = keyword.replace(" ", "+");
        URL url = null;
        HttpURLConnection connection = null;
        StringBuilder textResult = new StringBuilder();

        try {
            url = new URL("https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query="+ keyword+ "&page=1&include_adult=false");

            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult.append(scanner.nextLine());
            }
            Log.i("Flloyd ", "Result OF API Entity: " +textResult);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }
        return textResult.toString();
    }


}
