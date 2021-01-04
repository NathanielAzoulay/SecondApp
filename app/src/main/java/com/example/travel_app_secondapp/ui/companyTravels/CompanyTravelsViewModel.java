 package com.example.travel_app_secondapp.ui.companyTravels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app_secondapp.data.ITravelRepository;
import com.example.travel_app_secondapp.data.TravelRepository;
import com.example.travel_app_secondapp.entities.Travel;
import com.example.travel_app_secondapp.entities.UserLocation;

import java.util.List;

 public class CompanyTravelsViewModel  extends AndroidViewModel {

    private MutableLiveData<String> mText;
     ITravelRepository travelsRepository;
     //public String userEmail;
    public CompanyTravelsViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is company travels fragment");
        //this.userEmail = userEmail;
        travelsRepository =  TravelRepository.getInstance(application);
    }

     void updateTravel(Travel travel)
     {
         travelsRepository.updateTravel(travel);
     }

    public LiveData<List<Travel>> getAllCompanyTravels(UserLocation userLocation, double maxDist) {
        return (LiveData<List<Travel>>)travelsRepository.getAllCompanyTravels(userLocation, maxDist);
    }

     MutableLiveData<Boolean> getIsSuccess()
     {
         return travelsRepository.getIsSuccess();
     }


     public LiveData<String> getText() {
         return mText;
     }

 }