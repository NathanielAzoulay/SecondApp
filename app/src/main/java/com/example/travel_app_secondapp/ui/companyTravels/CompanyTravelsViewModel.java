package com.example.travel_app_secondapp.ui.companyTravels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CompanyTravelsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CompanyTravelsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is company travels fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}