package com.example.booklistingapp.favourites;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FavouriteItem.class}, version = 8)
public abstract class FavouritesDatabase extends RoomDatabase{
    abstract FavouritesDao favouritesDao();
    private static FavouritesDatabase instance;

    private static final int MAX_THREADS = 4;
    public static ExecutorService service = Executors.newFixedThreadPool(MAX_THREADS);
    private static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    public static FavouritesDatabase getInstance(Context context){
        if(instance == null){
            synchronized (FavouritesDatabase.class){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                                    FavouritesDatabase.class,
                                                "FavouritesDatabase")
                            .addMigrations(MIGRATION_7_8)
                            .build();
                }
            }
        }
        return instance;
    }
}
