package com.flloyd.mymoviememoir.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.networkConnection.NetworkConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private LatLng currentlocation;
    private LatLng cinemaLoc;
    private NetworkConnection networkConnection = null;
    private Map<String,String> cinemaAdress;
    private Map<String,LatLng> cinema;
    public MapFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        networkConnection = new NetworkConnection();
        cinema = new HashMap<>();
        cinemaAdress = new HashMap<>();

        cinemaAdress.put("The Astor Theatre","The Astor Theatre 3182");
        cinemaAdress.put("Village Cinemas Jam Factory","Village Cinemas Jam Factory 3141");
        cinemaAdress.put("Now Showing PTY Ltd.","Now Showing PTY Ltd. 3181");
        cinemaAdress.put("Barefoot Cinema","Barefoot Cinema Rippon Lea Estate 3185");
        cinemaAdress.put("Hotys","1341 Dandenong Rd, Chadstone VIC 3148");
        cinemaAdress.put("Classic Cinemas","9 Gordon St, Elsternwick VIC 3185");



        String streetAddress = "";
        String stateCode = "";
        String postCode = "";
        SharedPreferences sharedPref= requireActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String credential= sharedPref.getString("Credential",null);
        Log.i("Flloyd: ", "DATA in Map :" + credential);
        try {
            JSONArray jsonArrayCredentials = new JSONArray(credential);
            JSONObject j = (JSONObject) jsonArrayCredentials.getJSONObject(0).get("personid");
            streetAddress = j.getString("streetaddress");
            stateCode = j.getString("statecode");
            postCode = j.getString("postcode");
        } catch (Exception ignored) { }

        //getCinema getCinema = new getCinema();
        //getCinema.execute();

        String address = streetAddress + " " + stateCode + " " + postCode;
        Log.i("Flloyd","address" + address);


        for (Map.Entry cinemaAddressEntry : cinemaAdress.entrySet()) {

            cinemaLoc =   getLocationFromAddress(this.getContext(), (String) cinemaAddressEntry.getValue());
            cinema.put((String) cinemaAddressEntry.getKey(),cinemaLoc);
        }

        currentlocation = getLocationFromAddress(this.getContext(),address);

        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map,mapFragment).commit();
        }


        mapFragment.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(currentlocation).title("My location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        for (Map.Entry me : cinema.entrySet()) {
            mMap.addMarker(new MarkerOptions().position((LatLng) me.getValue()).title((String) me.getKey()));
        }



        mMap.addMarker(new MarkerOptions().position(cinemaLoc).title("cinema"));

        float zoomLevel = (float) 15.0;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlocation,
                zoomLevel));

    }


 /*Zoom levels
 1: World
 5: Landmass/continent
 10: City
 15: Streets
 20: Buildings
 */


    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        if(coder.isPresent()) {
            try {
                address = coder.getFromLocationName(strAddress, 1);
                if (address == null) {
                    return null;
                }
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                Log.i("Flloyd", "address" + location.getLatitude() + location.getLongitude());

                p1 = new LatLng(location.getLatitude(), location.getLongitude());

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }
        return p1;
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

                try {
                    for (int i = 0; i < result.length(); i++){
                        String cinemaName = result.getJSONObject(i).getString("cinemaname");
                        String cinemaPostCode = result.getJSONObject(i).getString("cinemapostcode");
                        LatLng cinemaLoc = getLocationFromAddress(getContext(),cinemaName + " " + cinemaPostCode);
                        cinema.put(cinemaName,cinemaLoc);
                        Log.i("Flloyd ","Cinema Lat Long "+ cinemaLoc.toString());
                    }
                }catch (Exception ignored){
                }
            }
        }
    }


}
