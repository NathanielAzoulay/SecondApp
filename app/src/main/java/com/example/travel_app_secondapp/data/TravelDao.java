package com.example.travel_app_secondapp.data;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.travel_app_secondapp.entities.Travel;
import java.util.List;

@Dao
public interface TravelDao {

    @Query("select * from travels")
    LiveData<List<Travel>> getAll();

    @Query("select * from travels where travelId=:id")
    LiveData<Travel> get(String id);

    @Query("select * from travels where travels.requestType = 3")
    LiveData<List<Travel>> getAllClosed();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Travel travel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Travel> travels);

    @Update
   void update(Travel travel);

    @Delete
    void delete(Travel... travels);

    @Query("delete from travels")
    void clear();

}
