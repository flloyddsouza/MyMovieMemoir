package com.flloyd.mymoviememoir.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.flloyd.mymoviememoir.R;

public class MovieSearchFrafment extends Fragment {
    private TextView tvMessage;
    public MovieSearchFrafment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.movie_search_fragment, container, false);
        tvMessage = view.findViewById(R.id.tv_showmessage);
        SharedPreferences sharedPref= getActivity().
                getSharedPreferences("Message", Context.MODE_PRIVATE);
        String message= sharedPref.getString("message",null);
        tvMessage.setText(message);
        return view;
    }
}
