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

import java.util.ArrayList;
import java.util.List;

public class RegisteredTravelsViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    ITravelRepository travelsRepository;
    public RegisteredTravelsViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is registered travels fragment");
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


    public LiveData<String> getText() {
        return mText;
    }





}

