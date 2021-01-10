package com.example.travel_app_secondapp.ui.registeredTravels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.travel_app_secondapp.data.ITravelRepository;
import com.example.travel_app_secondapp.data.TravelRepository;
import com.example.travel_app_secondapp.entities.Travel;
import java.util.List;

public class RegisteredTravelsViewModel extends AndroidViewModel {

    ITravelRepository travelsRepository;
    public RegisteredTravelsViewModel(Application application) {
        super(application);
        travelsRepository =  TravelRepository.getInstance(application);
    }

    void addTravel(Travel travel)
    {
        travelsRepository.addTravel(travel);
    }
    void updateTravel(Travel travel)
    {
        travelsRepository.updateTravel(travel);
    }
    LiveData<List<Travel>> getAllRegisteredTravels(String userEmail) {
        return travelsRepository.getAllRegisteredTravels(userEmail);
    }
    MutableLiveData<Boolean> getIsSuccess()
    {
        return travelsRepository.getIsSuccess();
    }

}

