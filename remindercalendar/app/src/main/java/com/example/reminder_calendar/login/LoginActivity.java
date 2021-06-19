package com.example.reminder_calendar.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

//import com.example.BaiTuanTong_Frontend.SystemAdministrator.ManagerHomePage;
import com.example.reminder_calendar.HomeActivity;
//import com.example.BaiTuanTong_Frontend.ui.register.RegistActivity;
import com.example.BaiTuanTong_Frontend.utils.MD5Util;
import com.example.reminder_calendar.HttpServer;
import com.example.reminder_calendar.databinding.ActivityLoginBinding;
import com.example.reminder_calendar.databinding.ActivityToDoOneDayBinding;
import com.example.reminder_calendar.R;
import com.example.reminder_calendar.register.RegistActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private LoginViewModel loginViewModel;
    private int loginResult;
    private String username;
    private final int POST = 0;
    private final int POSTFAIL = 1;
    private int retry_time = 0;

    //private ActivityLoginBinding binding;

    private static  OkHttpClient okHttpClient = new OkHttpClient();

    //申请动态内容
    private Handler getHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            try {
                JSONObject jsonObject = new JSONObject((String)msg.obj);
                String data = jsonObject.getString("data");
                if(data.equals("登录成功")){
                    startHomePage();
                }else{
                    Toast.makeText(getApplicationContext(),"login failed " + data, Toast.LENGTH_LONG).show();
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

        setContentView(R.layout.activity_login);

        Intent intentShift = new Intent(this, RegistActivity.class);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button shiftToRegButton = findViewById(R.id.shift_to_register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        //输入时，提示格式信息
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        //告知loginViewModel，输入信息有改变
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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        //好像是如果输入密码后按回车直接登录，暂时忽略
        /*
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });*/

        //点击登录按钮则发送请求
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(LoginActivity.this.getWindow().getDecorView().getWindowToken(), 0);
                //String msg = loginViewModel.login(usernameEditText.getText().toString(),
                //         passwordEditText.getText().toString());

                username = usernameEditText.getText().toString();
                String password = MD5Util.encryp(passwordEditText.getText().toString());
                Log.e("encryp",password);
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password", password);
                Gson gson = new Gson();
                String data = gson.toJson(map);
                HttpServer.getDataFromPost(HttpServer.LOCALURL+"/api/login", data, getHandler);
                //startHomePage();
            }
        });

        //点击注册按钮则切换页面
        shiftToRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentShift);
            }
        });
    }
    // 转到HomePageActivity
    private void startHomePage(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    //转到ManageHomePage
    private void startManagerHomePage(){
//        Intent intent = new Intent(this, ManagerHomePage.class);
//        startActivity(intent);
        this.finish();
    }

    //Toast提示登录结果信息，暂时忽略
    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}