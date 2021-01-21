package com.example.travel_app_secondapp.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.travel_app_secondapp.entities.Travel;
import java.util.List;

/**
 * History data source include all the travels that are closed,
 * it puts his new data to his history database, which is a Room database.
 */
public class HistoryDataSource implements IHistoryDataSource {
    private TravelDao travelDao;

    public HistoryDataSource(Context context) {
        RoomDataSource database = RoomDataSource.getInstance(context);
        travelDao = database.getTravelDao();
        travelDao.clear(); // cause crash if the database's skeleton need update
    }

    /**
     * get all the travels which saved in the history database
     * @return LiveData of all the travels allocated in the history database
     */
    public LiveData<List<Travel>> getTravels() {
        return travelDao.getAll();
    }

    /**
     * gets string id of a travel and return that travel which we're looking for.
     * @param id identification of the requested ttravel
     * @return liveDate of the requested travel
     */
    public LiveData<Travel> getTravel(String id) {
        return travelDao.get(id);
    }

    /**
     * add a single travel to the database
     * @param p the instance of the travel
     */
    public void addTravel(Travel p) {
        travelDao.insert(p);
    }

    /**
     * add a list of travels to the database
     * @param travelList the list of travels to add
     */
    public void addTravel(List<Travel> travelList) {
        travelDao.insert(travelList);
    }

    /**
     * gets a travel and edit the existing one which already located on the DB
     * @param p travel's instance
     */
    public void editTravel(Travel p) {
        travelDao.update(p);
    }

    /**
     * delete a travel by getting an instance for finding it in the DB
     * @param p travel's instance
     */
    public void deleteTravel(Travel p) {
        travelDao.delete(p);
    }

    /**
     * clear all the data in the DB
     */
    public void clearTable() {
        travelDao.clear();
    }


}
