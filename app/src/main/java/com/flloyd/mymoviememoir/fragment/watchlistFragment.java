package com.flloyd.mymoviememoir.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flloyd.mymoviememoir.Entity.Movie;
import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.adapter.RecyclerWishListAdapter;
import com.flloyd.mymoviememoir.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class watchlistFragment  extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Movie> wishList;
    private RecyclerWishListAdapter adapter;

    MovieViewModel movieViewModel;
    public watchlistFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        wishList = new ArrayList<>();

        View view = inflater.inflate(R.layout.watchlist_fragment, container, false);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.initalizeVars((getActivity().getApplication()));

        recyclerView = view.findViewById(R.id.recyclerViewWish);
        adapter = new RecyclerWishListAdapter(wishList,movieViewModel);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        movieViewModel.getAllMovies().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(@Nullable final List<Movie> movies)
                    {
                        wishList.clear();
                        for (Movie temp : movies) {
                            Movie movie = new Movie(temp.getId(),temp.getMovieName(),temp.getReleaseDate(),temp.getSaveDateTime());
                            wishList.add(movie);
                        }
                        adapter.addWishListItem(wishList);
                    }
                });

        return view;
    }


}
