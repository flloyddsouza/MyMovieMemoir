package com.flloyd.mymoviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flloyd.mymoviememoir.DataModel.SearchResult;
import com.flloyd.mymoviememoir.R;

import java.util.List;

public class RecyclerSearchMovieAdapter extends RecyclerView.Adapter<RecyclerSearchMovieAdapter.ViewHolder>  {

    private List<SearchResult> searchResultList;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView movieName;
        public TextView releaseDate;

        public ViewHolder(View itemView) {
            super(itemView);
            movieName = itemView.findViewById(R.id.movieNameSearch);
            releaseDate = itemView.findViewById(R.id.movieYearSearch);
        }
    }


    @Override
    public int getItemCount() {
        return searchResultList.size();
    }


    public RecyclerSearchMovieAdapter(List<SearchResult> searchResultList) {
        this.searchResultList = searchResultList;

    }
    public void addSearchResult(List<SearchResult> searchResultList) {
        this.searchResultList = searchResultList;
        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View resultMoviesView = inflater.inflate(R.layout.search_view_layout, parent, false);
        ViewHolder viewHolderSearch = new ViewHolder(resultMoviesView);
        return viewHolderSearch;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SearchResult searchResult = searchResultList.get(position);

        TextView movieNameTV = holder.movieName;
        movieNameTV.setText(searchResult.getName());

        TextView movieYear = holder.releaseDate;
        movieYear.setText(searchResult.getYear());
    }


}
