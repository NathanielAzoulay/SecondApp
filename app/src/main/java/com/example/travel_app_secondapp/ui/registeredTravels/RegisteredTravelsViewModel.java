package com.example.travel_app_secondapp.ui.registeredTravels;

import android.app.Application;
import android.view.View;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app_secondapp.data.ITravelRepository;
import com.example.travel_app_secondapp.data.TravelRepository;
import com.example.travel_app_secondapp.entities.Travel;

import java.util.List;

public class RegisteredTravelsViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    ITravelRepository registeredTravelsRepository;
    public String userEmail;
    public RegisteredTravelsViewModel(Application application, String userEmail) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is registered travels fragment");
        this.userEmail = userEmail;
        registeredTravelsRepository =  TravelRepository.getInstance(application, userEmail);
    }

    void addTravel(Travel travel)
    {
        registeredTravelsRepository.addTravel(travel);
    }
    void updateTravel(Travel travel)
    {
        registeredTravelsRepository.updateTravel(travel);
    }
    LiveData<List<Travel>> getAllTravels() {
        return (LiveData<List<Travel>>)registeredTravelsRepository.getAllTravels();
    }
    MutableLiveData<Boolean> getIsSuccess()
    {
        return registeredTravelsRepository.getIsSuccess();
    }


    public LiveData<String> getText() {
        return mText;
    }

}

