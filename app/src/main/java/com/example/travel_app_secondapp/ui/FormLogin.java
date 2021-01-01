package com.example.travel_app_secondapp.ui;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.example.travel_app_secondapp.BR;
import com.example.travel_app_secondapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormLogin extends BaseObservable {
    private String email;
    private String password;
    public ObservableField<Integer> emailError = new ObservableField<>();
    public ObservableField<Integer> passwordError = new ObservableField<>();
    //private FirebaseAuth mAuth;
//    FormLogin(){
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//    }

    @Bindable
    public boolean isValid() {
        boolean validEmail = isEmailValid(false);
        boolean validPassword = isPasswordValid(false);
        return  validPassword && validEmail;
    }

    @Bindable
    public boolean isOk() {
        return isEmailValid(false);
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.valid);
        notifyPropertyChanged(BR.ok);
    }

    public void setPassword(String password) {
        this.password = password;
        isPasswordValid(true);
        notifyPropertyChanged(BR.valid);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    @Bindable
    public String getPassword() {
        return password;
    }


    public boolean isEmailValid(boolean setMsg) {
        if(getEmail() == null)
            return false;
        String regex = "^(.+)@(.+)$";
        if (!RegexValidation(getEmail(),regex)) {
            if(setMsg)
                emailError.set(R.string.email_not_valid);
            return false;
        }
        emailError.set(null);
        return true;
    }

    public boolean RegexValidation(String value,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public boolean isPasswordValid(boolean setMsg){
        if(getPassword() == null)
            return false;
        if (getPassword().length() < 5){
            if(setMsg)
                passwordError.set(R.string.password_not_valid);
            return false;
        }
        passwordError.set(null);
        return true;
    }


}
