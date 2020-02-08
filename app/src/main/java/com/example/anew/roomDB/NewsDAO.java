package com.example.anew.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.anew.model.NewsModel;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface NewsDAO {
    //Insert one item
    @Insert(onConflict = IGNORE)
    void insertItem(NewsModel news);

    // Delete one item
    @Delete
    void deleteItem(NewsModel news);

      //Get all items
    @Query("SELECT * FROM news_table")
    LiveData<List<NewsModel>> getAllData();

    //Delete All
    @Query("DELETE FROM news_table")
    void deleteAll();
}
