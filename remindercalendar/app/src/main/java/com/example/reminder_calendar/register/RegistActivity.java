package com.example.reminder_calendar.register;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminder_calendar.HttpServer;
import com.example.reminder_calendar.R;
import com.example.reminder_calendar.HomeActivity;
import com.example.reminder_calendar.login.LoginActivity;
import com.example.reminder_calendar.register.RegistViewModel;
import com.example.reminder_calendar.register.RegistViewModelFactory;
import com.example.reminder_calendar.register.RegistViewModel;
import com.example.BaiTuanTong_Frontend.utils.MD5Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistActivity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private RegistViewModel registViewModel;
    private OkHttpClient client = HttpServer.okHttpClient;
    private String username;
    private String password;
    private String email;

    private Handler getHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            try {
                JSONObject jsonObject = new JSONObject((String)msg.obj);
                int code = jsonObject.getInt("code");
                if(code==200){
                    Intent intent = new Intent(RegistActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"register failed " + code, Toast.LENGTH_LONG).show();
                    Log.e("fail code", "code: "+code);
                }
            } catch (JSONException e) {
                Log.e("failhttp","fail");
                e.printStackTrace();
            }
            //super.handleMessage(msg);
            return true;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        registViewModel = new ViewModelProvider(this, new RegistViewModelFactory())
                .get(RegistViewModel.class);

        final EditText usernameEditText = findViewById(R.id.registUserName);
        final EditText passwordEditText = findViewById(R.id.registPassword);
        final EditText passwordConfirmText = findViewById(R.id.registConfirmPassword);
        //final EditText emailEditText = findViewById(R.id.registEmail);
        final Button registButton = findViewById(R.id.registButton);

        registViewModel.getRegistFormState().observe(this, new Observer<RegistFormState>() {
            @Override
            public void onChanged(@Nullable RegistFormState registFormState) {
                if (registFormState == null) {
                    return;
                }
                registButton.setEnabled(registFormState.isDataValid());
                if (registFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(registFormState.getUsernameError()));
                }
//                if (registFormState.getEmailError() != null) {
//                    emailEditText.setError(getString(registFormState.getEmailError()));
//                }
                if (registFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registFormState.getPasswordError()));
                }
                if (registFormState.getPasswordMisMatch() != null) {
                    passwordConfirmText.setError(getString(registFormState.getPasswordMisMatch()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registViewModel.registDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), passwordConfirmText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        //emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordConfirmText.addTextChangedListener(afterTextChangedListener);

        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject=new JSONObject();
                username = usernameEditText.getText().toString();
                password = MD5Util.encryp(passwordEditText.getText().toString());
                //email = emailEditText.getText().toString();
                try {
                    jsonObject.put("username",username);
                    jsonObject.put("password",password);
                    HttpServer.getDataFromPost(HttpServer.LOCALURL+"/api/register", jsonObject.toString(),getHandler);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            }
        });
    }

    // 转到LoginPageActivity
    private void startLoginPage(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
    // 转到HomePageActivity
    private void startHomePage(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }


}