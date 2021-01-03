package com.example.travel_app_secondapp.data;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app_secondapp.ui.companyTravels.CompanyTravelsViewModel;
import com.example.travel_app_secondapp.ui.historyTravels.HistoryTravelsFragment;
import com.example.travel_app_secondapp.ui.historyTravels.HistoryTravelsViewModel;
import com.example.travel_app_secondapp.ui.registeredTravels.RegisteredTravelsViewModel;

public class UserViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public UserViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        // TODO: could be necessary for the future
//        if (modelClass == RegisteredTravelsViewModel.class) {
//            return (T) new RegisteredTravelsViewModel(mApplication, mParam);
//        }else {
//            return (T) new CompanyTravelsViewModel(mApplication, mParam);
//        }
        return (T) new RegisteredTravelsViewModel(mApplication, mParam);
    }
}
