package com.example.reminder_calendar.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.example.reminder_calendar.login.LoginRepository;
import com.example.reminder_calendar.login.Result;
import com.example.reminder_calendar.login.LoggedInUser;
import com.example.reminder_calendar.R;

public class RegistViewModel extends ViewModel {

    private MutableLiveData<RegistFormState> registFormState = new MutableLiveData<>();
    private LoginRepository loginRepository;

    RegistViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
    LiveData<RegistFormState> getRegistFormState() {
        return registFormState;
    }


    public void registDataChanged(String username, String password, String passwordConfirm) {
        if (!isUserNameValid(username)) {
            Log.e("invalid", "username");
            registFormState.setValue(new RegistFormState(R.string.invalid_username, null, null, null));
        } else if (!isPasswordValid(password)) {
            Log.e("invalid", "password");
            registFormState.setValue(new RegistFormState(null, null, R.string.invalid_password, null));
        }else if (!isPasswordMatch(password, passwordConfirm)) {
            Log.e("invalid", "confirm");
            registFormState.setValue(new RegistFormState(null, null, null, R.string.password_mismatch));
        }else {
            Log.e("valid", "valid");
            registFormState.setValue(new RegistFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username.equals("")) {
            return false;
        }else{
            return true;
        }
    }

    private boolean isEmailValid(String username) {
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return false;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isPasswordMatch(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }
}