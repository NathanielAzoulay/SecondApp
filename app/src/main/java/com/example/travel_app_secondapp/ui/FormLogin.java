package com.example.travel_app_secondapp.ui;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.example.travel_app_secondapp.BR;
import com.example.travel_app_secondapp.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormLogin extends BaseObservable {
    private String email;
    private String password;
    public ObservableField<Integer> emailError = new ObservableField<>();
    public ObservableField<Integer> passwordError = new ObservableField<>();

    @Bindable
    public boolean isValid() {
        boolean validEmail = isEmailValid(false);
        boolean validPassword = isPasswordValid(false);
        return validPassword && validEmail;
    }

    @Bindable
    public boolean isEmailOk() {
        return isEmailValid(false);
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.valid);
        notifyPropertyChanged(BR.emailOk);
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
        if (getEmail() == null)
            return false;
        String regex = "^(.+)@(.+)$";
        if (!RegexValidation(getEmail(), regex)) {
            if (setMsg)
                emailError.set(R.string.email_not_valid);
            return false;
        }
        emailError.set(null);
        return true;
    }

    public boolean RegexValidation(String value, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public boolean isPasswordValid(boolean setMsg) {
        if (getPassword() == null)
            return false;
        if (getPassword().length() < 5) {
            if (setMsg)
                passwordError.set(R.string.password_not_valid);
            return false;
        }
        passwordError.set(null);
        return true;
    }


}
