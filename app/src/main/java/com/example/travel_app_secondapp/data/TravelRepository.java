package com.example.travel_app_secondapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app_secondapp.entities.Travel;
import com.example.travel_app_secondapp.entities.UserLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * The repository responsibilities are:
 * to gives an abstract data to the viewModel
 * to interface with the data sources
 * to save the history of the database which brought from firebase to a local database
 * to filter list which it gets from the firebase database for each fragment
 */
public class TravelRepository implements ITravelRepository {
    ITravelDataSource travelDataSource;
    private final IHistoryDataSource historyDataSource;
    private final MutableLiveData<List<Travel>> mutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Travel>> registeredMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Travel>> companyMutableLiveData = new MutableLiveData<>();

    private List<Travel> travelList;
    private final List<Travel> travelsRegistered = new ArrayList<>();
    private final List<Travel> travelsCompany = new ArrayList<>();
    private final List<Travel> travelsHistory = new ArrayList<>();



    private static TravelRepository instance;


    /**
     * those variables are used for the function call "onChanged"
     * at any time we call the function "get" of some mutable live data
     * (example: getAllRegisteredTravels) the relevant values of those variables will changed
     * those variables are needed for the filtering functions (such as "filterCompanyTravels")
     */
    String userEmail = "none", companyName = "none";
    UserLocation userLoc;
    double maxDist;

    /**
     * singleton attribute
     *
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
        userLoc = new UserLocation(0, 0);
        maxDist = 0;
        // here (in the constructor) we implements the notifyListener of the data source.
        ITravelDataSource.NotifyToTravelListListener notifyToTravelListListener = new ITravelDataSource.NotifyToTravelListListener() {
            @Override
            public void onTravelsChanged() {
                travelList = travelDataSource.getAllTravels();
                registeredMutableLiveData.setValue(filterRegisteredTravels());
                companyMutableLiveData.setValue(filterCompanyTravels());
                // not in use
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


    /**
     * a function that gives all the travels from the data source
     *
     * @return liveData of the list
     */
    @Override
    public LiveData<List<Travel>> getAllTravels() {
        return mutableLiveData;
    }

    /**
     * receives all the travels which belong to the registered section, it means:
     * all travels made by the user, which are relevant to their date
     *
     * @return liveData of the filtered list of the travels
     */
    public LiveData<List<Travel>> getAllRegisteredTravels(String clientEmail) {
        // repository need to know the new values since it use the filtering in it's constructor,
        // when the notify listener activates.
        userEmail = clientEmail;
        if (travelList != null)
            registeredMutableLiveData.setValue(filterRegisteredTravels());
        return registeredMutableLiveData;
    }

    /**
     * receives all the travels which belong to the company section, it means:
     * new travels in the area, travels that accepted for this company offer.
     *
     * @return liveData of the filtered list of the travels
     */
    public LiveData<List<Travel>> getAllCompanyTravels(UserLocation userLoc, double maxDist) {
        // takes only the name of the email address (example: monalisa@gmail.com -> monalisa)
        this.companyName = userEmail.replaceAll("@[a-z]+\\.+[a-z]+", "");
        // repository need to know the new values since it use the filtering in it's constructor,
        // when the notify listener activates.
        this.userLoc = userLoc;
        this.maxDist = maxDist;
        if (travelList != null) {
            companyMutableLiveData.setValue(filterCompanyTravels());
        }
        return companyMutableLiveData;
    }

    /**
     * receives all the travels which belong to the history section, it means: expired, closed, paid..
     *
     * @return liveData of the filtered list of the travels
     */
    public LiveData<List<Travel>> getAllHistoryTravels() {
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
     *
     * @return the list which filtered by email and status of "sent"
     */
    public List<Travel> filterRegisteredTravels() {
        travelsRegistered.clear();
        travelList = travelDataSource.getAllTravels();
        for (Travel travel : travelList) {
            if (travel.getClientEmail().equals(userEmail) && isDateRelevant(travel) && travel.getRequestType().getCode() < 3)
                travelsRegistered.add(travel);
        }
        return travelsRegistered;
    }

    /**
     * get all travel requests which are close to a known location, and has status "sent".
     * or, those requests which has been already accepted this company's offer
     *
     * @return the list which filtered by close range and status of "sent"
     */
    public List<Travel> filterCompanyTravels() {
        travelsCompany.clear();
        travelList = travelDataSource.getAllTravels();
        for (Travel travel : travelList) {
            UserLocation travelLoc = travel.getTravelLocation(); // check maybe his source is in our area and his request just sent
            if (((calculateDistance(userLoc.getLat(), userLoc.getLon(), travelLoc.getLat(), travelLoc.getLon())
                    < maxDist && travel.getRequestType() == Travel.RequestType.sent)
                    || isCompanyAccepted(travel)) // or maybe it's a request which already accepted for our company

                    && isDateRelevant(travel)) // all below has to be relevant with it's date
            {
                travelsCompany.add(travel);
            }
        }
        return travelsCompany;
    }


    /**
     * average radius of earth
     */
    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;

    /**
     * a function that calculates the distance between two locations
     *
     * @param userLat  location 1 latitude
     * @param userLng  location 1 longitude
     * @param venueLat location 2 latitude
     * @param venueLng location 2 longitude
     * @return the distance
     */
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
     *
     * @return the list which filtered by status of "closed"
     */
    private List<Travel> filterHistoryTravels(List<Travel> travelList) {
        travelsHistory.clear();
        for (Travel travel : travelList) {
            if (travel.getRequestType() == Travel.RequestType.close ||
                    travel.getRequestType() == Travel.RequestType.paid)
                travelsHistory.add(travel);
        }
        return travelsHistory;
    }

    /**
     * function that receive a travel and gives an answer if is expired
     *
     * @param travel instance of a travel
     * @return boolean true/false
     */
    private boolean isDateRelevant(Travel travel) {
        return (travel.getCreateDate().getTime() < travel.getTravelDate().getTime());
    }

    /**
     * function that receive a travel and gives an answer if the company has accepted by the traveler
     *
     * @param travel instance of a travel
     * @return boolean true/false
     */
    private boolean isCompanyAccepted(Travel travel) {
        if (travel.getCompany() != null) {
            if (travel.getCompany().get(companyName) != null)
                return travel.getCompany().get(companyName);
        }
        return false;
    }

}
