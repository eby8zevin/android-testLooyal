package com.ahmadabuhasan.looyal.ui;

import static com.ahmadabuhasan.looyal.utils.Constant.DATA_CODE;
import static com.ahmadabuhasan.looyal.utils.Constant.DATA_NAME;
import static com.ahmadabuhasan.looyal.utils.Constant.DATA_STATE_CODE;
import static com.ahmadabuhasan.looyal.utils.Constant.DATA_STATE_NAME;
import static com.ahmadabuhasan.looyal.utils.Constant.KEY_DATE_REGISTER;
import static com.ahmadabuhasan.looyal.utils.Constant.KEY_LOGIN;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmadabuhasan.looyal.R;
import com.ahmadabuhasan.looyal.api.ApiConfig;
import com.ahmadabuhasan.looyal.api.ApiService;
import com.ahmadabuhasan.looyal.databinding.ActivityProfileBinding;
import com.ahmadabuhasan.looyal.model.City;
import com.ahmadabuhasan.looyal.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private ActivityProfileBinding binding;
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
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        String username = sessionManager.getUsername();
        String dateRegister = sessionManager.getDateRegister();
        String city = sessionManager.getCityName();

        binding.tvUsername.setText(username);
        binding.etTimeRegister.setText(dateRegister);
        binding.etCity.setText(city);

        enableFalse();
        binding.btnSave.setVisibility(View.INVISIBLE);
        binding.btnEdit.setOnClickListener(view -> edit());
        binding.btnLogout.setOnClickListener(view -> dialogLogout());
    }

    private void getDataCity() {
        clearCity();

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

    private void enableFalse() {
        binding.etTimeRegister.setEnabled(false);
        binding.etCity.setEnabled(false);
        binding.etCity.setClickable(false);
    }

    private void enableTrue() {
        binding.etTimeRegister.setEnabled(true);
        binding.etCity.setEnabled(true);
        binding.etCity.setClickable(true);
    }

    private void clearCity() {
        valueCode.clear();
        valueName.clear();
        valueStateCode.clear();
        valueStateName.clear();
    }

    private void edit() {
        enableTrue();
        binding.etCity.requestFocus();
        binding.btnSave.setVisibility(View.VISIBLE);

        getDataCity();
        binding.btnSave.setOnClickListener(view -> save());
    }

    private void save() {
        String text = Objects.requireNonNull(binding.etTimeRegister.getText()).toString();
        if (!text.isEmpty()) {
            sessionManager.setStringPref(KEY_DATE_REGISTER, text);

            sessionManager.setStringPref(DATA_CODE, textCode);
            sessionManager.setStringPref(DATA_NAME, textName);
            sessionManager.setStringPref(DATA_STATE_CODE, textStateCode);
            sessionManager.setStringPref(DATA_STATE_NAME, textStateName);

            clearCity();
            binding.etTimeRegister.setEnabled(false);
            binding.etCity.setEnabled(false);
            binding.etCity.setClickable(false);
            binding.btnSave.setVisibility(View.INVISIBLE);
        }
    }

    private void dialogLogout() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.log_out))
                .setMessage(getResources().getString(R.string.message_logout))
                .setNegativeButton(getResources().getString(R.string.cancel), null)
                .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> logout()).create().show();
    }

    private void logout() {
        //sessionManager.getClearData();
        sessionManager.setBooleanPref(KEY_LOGIN, false);
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}