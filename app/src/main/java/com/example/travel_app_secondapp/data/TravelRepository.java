package com.example.travel_app_secondapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app_secondapp.entities.Travel;
import com.example.travel_app_secondapp.entities.UserLocation;
import com.example.travel_app_secondapp.ui.companyTravels.CompanyTravelsViewModel;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * The repository responsibilities are:
 *      to
 *      to..
 *      to filter list which it gets from the firebase database for each fragment
 */
public class TravelRepository implements ITravelRepository {
    ITravelDataSource  travelDataSource;
    private IHistoryDataSource historyDataSource;
    private MutableLiveData<List<Travel>> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Travel>> registeredMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Travel>> companyMutableLiveData = new MutableLiveData<>();
    private List<Travel> travelList;
    private final List<Travel> travelsRegistered = new ArrayList<>();
    private final List<Travel> travelsCompany = new ArrayList<>();
    private final List<Travel> travelsHistory = new ArrayList<>();

    UserLocation userLocation;
    double maxDist = 20000;

    String userEmail = "none";
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
        // TODO: singleton
        historyDataSource = new HistoryDataSource(application.getApplicationContext());
        userLocation = new UserLocation(0,0);
        // here (in the constructor) we implements the notifyListener of the data source.
        ITravelDataSource.NotifyToTravelListListener notifyToTravelListListener = new ITravelDataSource.NotifyToTravelListListener() {
            @Override
            public void onTravelsChanged() {
                travelList = travelDataSource.getAllTravels();
                registeredMutableLiveData.setValue(filterRegisteredTravels());
                companyMutableLiveData.setValue(filterCompanyTravels());
                mutableLiveData.setValue(travelList);
                historyDataSource.clearTable();
                historyDataSource.addTravel(filterHistoryTravels(travelList));
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
    public void removeTravel(Travel travel) {
        travelDataSource.removeTravel(travel.getTravelId());
    }


    @Override
    public LiveData<List<Travel>> getAllTravels(boolean fireBase) {
        return mutableLiveData;
    }

    public LiveData<List<Travel>> getAllRegisteredTravels(String clientEmail){
        userEmail = clientEmail;
        if (travelList != null)
            registeredMutableLiveData.setValue(filterRegisteredTravels());
        return registeredMutableLiveData;
    }

    public LiveData<List<Travel>> getAllCompanyTravels(UserLocation userLocation, double maxDist){
        this.userLocation = userLocation;
        this.maxDist = maxDist;
        if (travelList != null) {
            companyMutableLiveData.setValue(filterCompanyTravels());
        }
        return companyMutableLiveData;
    }

    public LiveData<List<Travel>> getAllHistoryTravels(){
        if (travelList != null) {
            historyDataSource.clearTable();
            historyDataSource.addTravel(filterHistoryTravels(travelList));
        }
        return historyDataSource.getTravels();
    }

    @Override
    public MutableLiveData<Boolean> getIsSuccess() {
        return travelDataSource.getIsSuccess();
    }

    /**
     * get all travel requests which belongs to the user with a known email, and has status "sent"
     * @return the list which filtered by email and status of "sent"
     */
    public List<Travel> filterRegisteredTravels(){
        travelsRegistered.clear();
        travelList = travelDataSource.getAllTravels();
        for(Travel travel : travelList){
            if (travel.getClientEmail().equals(userEmail))
                    //&& travel.getRequestType() == Travel.RequestType.sent)
                travelsRegistered.add(travel);
        }
        return travelsRegistered;
    }

    /**
     * get all travel requests which are close to a known location, and has status "sent"
     * @return the list which filtered by close range and status of "sent"
     */
    public List<Travel> filterCompanyTravels(){
        travelsCompany.clear();
        travelList = travelDataSource.getAllTravels();
        for(Travel travel : travelList){
            UserLocation travelLoc = travel.getTravelLocation(); // check maybe his source is in our area
            if (calculateDistance(userLocation.getLat(),userLocation.getLon(),travelLoc.getLat(),travelLoc.getLon()) < maxDist &&
                     travel.getRequestType() == Travel.RequestType.sent
                || isCompanyAccepted(travel))
            {
                travelsCompany.add(travel);
            }


        }
        return travelsCompany;
    }

    private boolean isCompanyAccepted(Travel travel) {
        if (travel.getCompany() != null){
            String companyName = userEmail.replaceAll("@[a-z]+\\.+[a-z]+", "");
            if(travel.getCompany().get(companyName) != null)
                return travel.getCompany().get(companyName);
        }
        return false;
    }


    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;
    public float calculateDistance(double userLat, double userLng, double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)) +
                (Math.cos(Math.toRadians(userLat))) *
                        (Math.cos(Math.toRadians(venueLat))) *
                        (Math.sin(lngDistance / 2)) *
                        (Math.sin(lngDistance / 2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (float) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));

    }

    /**
     * get all travel requests that their "requestType" is closed.
     * @return the list which filtered by status of "closed"
     */
    private List<Travel> filterHistoryTravels(List<Travel> travelList) {
        travelsHistory.clear();
        for (Travel travel : travelList){
            if (travel.getRequestType() == Travel.RequestType.close ||
                    travel.getRequestType() == Travel.RequestType.paid)
                travelsHistory.add(travel);
        }
        return travelsHistory;
    }

}
