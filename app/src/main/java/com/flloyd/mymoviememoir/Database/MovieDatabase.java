package com.flloyd.mymoviememoir.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.flloyd.mymoviememoir.DAO.MovieDAO;
import com.flloyd.mymoviememoir.Entity.Movie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Movie.class}, version = 2, exportSchema = false)
public  abstract class MovieDatabase extends RoomDatabase {


    public abstract MovieDAO movieDAO();

    private static MovieDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static synchronized MovieDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDatabase.class, "MovieDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}
