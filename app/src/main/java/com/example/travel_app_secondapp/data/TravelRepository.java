package com.example.travel_app_secondapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app_secondapp.entities.Travel;

import java.util.ArrayList;
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
    boolean firebaseFlag = true;
    private MutableLiveData<List<Travel>> mutableLiveData = new MutableLiveData<>();
    private LiveData<List<Travel>> liveData;
    private List<Travel> travelList;

    private static TravelRepository instance;
    /**
     * singleton attribute
     * @return the instance
     */
    public static TravelRepository getInstance(Application application, String userEmail) {
        if (instance == null)
            instance = new TravelRepository(application, userEmail);
        return instance;
    }

    private TravelRepository(Application application, String userEmail) {
        travelDataSource = TravelFirebaseDataSource.getInstance();
        historyDataSource = new HistoryDataSource(application.getApplicationContext());
        // here (in the constructor) we implements the notifyListener of the data source.
        ITravelDataSource.NotifyToTravelListListener notifyToTravelListListener = new ITravelDataSource.NotifyToTravelListListener() {
            @Override
            public void onTravelsChanged() {
                travelList = travelDataSource.getAllTravels();

                mutableLiveData.setValue(getTravelRequests(userEmail));



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
    public LiveData<List<Travel>> getAllTravels() {

        if (firebaseFlag)
          return mutableLiveData;
        return historyDataSource.getTravels();
    }

    @Override
    public MutableLiveData<Boolean> getIsSuccess() {
        return travelDataSource.getIsSuccess();
    }


    /**
     * get all travel requests which belongs to the user with this email, and has status "sent"
     * @param clientEmail the client's email address
     * @return the list which filtered by email and status of request
     */
    public List<Travel> getTravelRequests(String clientEmail){
        List<Travel> travelRequests = new ArrayList<>();
        for(Travel travel : travelList){
            if (travel.getClientEmail().equals(clientEmail)//)
                    && travel.getRequestType() == Travel.RequestType.sent)
                travelRequests.add(travel);
        }
        return travelRequests;
    }
}
