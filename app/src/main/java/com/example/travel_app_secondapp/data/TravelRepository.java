package com.example.travel_app_secondapp.data;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.travel_app_secondapp.entities.Travel;

import java.util.List;

/**
 * The repository responsibilities are:
 *      to
 *      to..
 *      to filter the list which it gets from the firebase/room database for each fragment in different way
 */
public class TravelRepository implements ITravelRepository {
    ITravelDataSource  travelDataSource;
    private IHistoryDataSource historyDataSource;


    private MutableLiveData<List<Travel>> mutableLiveData = new MutableLiveData<>();



    private static TravelRepository instance;
    /**
     * singleton attribute
     * @return the instance
     */
    public static TravelRepository getInstance(Application application) {
        if (instance == null)
            instance = new TravelRepository(application);
        return instance;
    }

    private TravelRepository(Application application) {
        travelDataSource = TravelFirebaseDataSource.getInstance();
        historyDataSource = new HistoryDataSource(application.getApplicationContext());

        // here (in the constructor) we implements the notifyListener of the data source.
        ITravelDataSource.NotifyToTravelListListener notifyToTravelListListener = new ITravelDataSource.NotifyToTravelListListener() {
            @Override
            public void onTravelsChanged() {
                List<Travel> travelList = travelDataSource.getAllTravels();
                //
                mutableLiveData.setValue(travelList);
                // TODO: filter the travels which has the status: "done" for the manager's payment
                historyDataSource.clearTable();
                historyDataSource.addTravel(travelList);

            }
        };
        // here we give the instance of the listener to DataSource for his use
        // of "onTravelChanged" as a callback.
        travelDataSource.setNotifyToTravelListListener(notifyToTravelListListener);
    }

    @Override
    public void addTravel(Travel travel) {
        travelDataSource.addTravel(travel);
    }

    @Override
    public void updateTravel(Travel travel) {
        travelDataSource.updateTravel(travel);
    }

    @Override
    public MutableLiveData<List<Travel>> getAllTravels() {
        return mutableLiveData;
    }

    @Override
    public MutableLiveData<Boolean> getIsSuccess() {
        return travelDataSource.getIsSuccess();
    }
}
