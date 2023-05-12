package com.ahmadabuhasan.looyal.ui;

import static com.ahmadabuhasan.looyal.utils.Constant.DATA_CODE;
import static com.ahmadabuhasan.looyal.utils.Constant.DATA_NAME;
import static com.ahmadabuhasan.looyal.utils.Constant.DATA_STATE_CODE;
import static com.ahmadabuhasan.looyal.utils.Constant.DATA_STATE_NAME;
import static com.ahmadabuhasan.looyal.utils.Constant.KEY_DATE_REGISTER;
import static com.ahmadabuhasan.looyal.utils.Constant.KEY_PASSWORD;
import static com.ahmadabuhasan.looyal.utils.Constant.KEY_USERNAME;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmadabuhasan.looyal.R;
import com.ahmadabuhasan.looyal.api.ApiConfig;
import com.ahmadabuhasan.looyal.api.ApiService;
import com.ahmadabuhasan.looyal.databinding.ActivityRegisterBinding;
import com.ahmadabuhasan.looyal.model.City;
import com.ahmadabuhasan.looyal.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private ActivityRegisterBinding binding;
    private SessionManager sessionManager;

    private final List<String> valueCode = new ArrayList<>();
    private String textCode;
    private final List<String> valueName = new ArrayList<>();
    private String textName;
    private final List<String> valueStateCode = new ArrayList<>();
    private String textStateCode;
    private final List<String> valueStateName = new ArrayList<>();
    private String textStateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        getDataCity();
        binding.btnRegister.setOnClickListener(view -> register());
        binding.tvToLogin.setOnClickListener(view -> toLogin());
    }

    private void getDataCity() {
        valueCode.clear();
        valueName.clear();
        valueStateCode.clear();
        valueStateName.clear();

        ApiService apiService = ApiConfig.getApiService();
        Call<City> call = apiService.listCity();
        call.enqueue(new Callback<City>() {
            @Override
            public void onResponse(@NonNull Call<City> call, @NonNull Response<City> response) {
                if (response.isSuccessful()) {
                    City responseBody = response.body();
                    if (responseBody != null) {
                        for (int i = 0; i < responseBody.getDataCity().size(); i++) {
                            valueCode.add(responseBody.getDataCity().get(i).getCode());
                            valueName.add(responseBody.getDataCity().get(i).getName());
                            valueStateCode.add(responseBody.getDataCity().get(i).getStateCode());
                            valueStateName.add(responseBody.getDataCity().get(i).getStateName());
                        }
                        spinnerCity();
                    }
                } else {
                    if (response.body() != null) {
                        Log.e(TAG, "onFailure: " + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<City> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void spinnerCity() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, valueName);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        binding.etCity.setAdapter(adapter);

        binding.etCity.setOnItemClickListener((adapterView, view, i, l) -> {
            textCode = valueCode.get(i);
            textName = valueName.get(i);
            textStateCode = valueStateCode.get(i);
            textStateName = valueStateName.get(i);
        });
    }

    private void register() {
        String checkUsername = sessionManager.getUsername();
        String username = Objects.requireNonNull(binding.etUsername.getText()).toString();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString();
        String city = binding.etCity.getText().toString();

        if (username.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Username or password can't be empty", Toast.LENGTH_SHORT).show();
        } else if (city.isEmpty()) {
            Toast.makeText(this, "City can't be empty", Toast.LENGTH_SHORT).show();
        } else {
            if (username.equals(checkUsername)) {
                Toast.makeText(this, "You're already registered", Toast.LENGTH_SHORT).show();
            } else {

                String dateRegister;
                String datePattern = "HH:mm:ss dd-MMM-yyyy";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDateTime dateNow1 = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
                    dateRegister = dateNow1.format(formatter);
                } else {
                    Date dateNow2 = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat(datePattern, Locale.getDefault());
                    dateRegister = formatter.format(dateNow2);
                }

                sessionManager.setStringPref(KEY_USERNAME, username);
                sessionManager.setStringPref(KEY_PASSWORD, password);
                sessionManager.setStringPref(KEY_DATE_REGISTER, dateRegister);

                sessionManager.setStringPref(DATA_CODE, textCode);
                sessionManager.setStringPref(DATA_NAME, textName);
                sessionManager.setStringPref(DATA_STATE_CODE, textStateCode);
                sessionManager.setStringPref(DATA_STATE_NAME, textStateName);

                Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            }
        }
    }

    private void toLogin() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}