package com.example.anew.roomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.anew.model.NewsModel;

@Database(entities = {NewsModel.class},version = 1,exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {
    private static NewsDatabase instance;
 // Create a file storage based database
    public static NewsDatabase getDatabase(Context context){
        if(instance == null)
        {
            synchronized (NewsDatabase.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        NewsDatabase.class,
                        NewsDatabase.class.getName()).build();
            }
        }
        return instance;
    }
public static void destroyInstance()
{
    instance = null;
}

    public abstract NewsDAO newsDAO();
}
