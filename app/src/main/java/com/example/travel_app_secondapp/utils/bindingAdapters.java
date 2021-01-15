package com.example.travel_app_secondapp.utils;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class bindingAdapters {
    public interface Validation {
        public boolean validate(boolean setMsg);
    }

    static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    @BindingAdapter("error")
    public static void setError(EditText editText, Object strOrResId) {
        if (strOrResId instanceof Integer) {
            editText.setError(editText.getContext().getString((Integer) strOrResId));
        } else {
            editText.setError((String) strOrResId);
        }
    }

    @BindingAdapter("ValidationOnFocus")
    public static void setValidation(EditText editText, Validation validation) {
        View.OnFocusChangeListener onFocusChangeListener = (view, hasFocus) -> {
            EditText et = (EditText) view;
            if (et.getText().length() > 0 && !hasFocus) {
                validation.validate(true);
            }
        };
        editText.setOnFocusChangeListener(onFocusChangeListener);
    }

    @BindingAdapter("onFocus")
    public static void bindFocusChange(EditText editText, View.OnFocusChangeListener onFocusChangeListener) {
        if (editText.getOnFocusChangeListener() == null) {
            editText.setOnFocusChangeListener(onFocusChangeListener);
        }
    }


    @BindingAdapter("android:text")
    public static void setIntegerText(TextView view, Integer value) {
        // this function written only because we need one-way to source binding
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static Integer getIntegerText(TextView view) {
        try {
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @BindingAdapter("android:text")
    public static void setDoubleText(TextView view, Double value) {
        // this function written only because we need one-way to source binding
        view.setText(value.toString());
    }


    @BindingAdapter("android:text")
    public static void setDateText(TextView view, Date value) {
        // this function written only because we need one-way to source binding

    }

    @InverseBindingAdapter(attribute = "android:text")
    public static Date getDateText(TextView view) {
        try {
            return format.parse(view.getText().toString());
        } catch (ParseException e) {
            return null;
        }
    }

}
