package com.ahmadabuhasan.looyal.ui;

import static com.ahmadabuhasan.looyal.utils.Constant.KEY_TEXT_AREA;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmadabuhasan.looyal.R;
import com.ahmadabuhasan.looyal.databinding.ActivityMainBinding;
import com.ahmadabuhasan.looyal.utils.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SessionManager sessionManager;
    private ArrayList<String> listTextArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        greetings();
        getDataPref();
        binding.btnSave.setOnClickListener(view -> save());
        binding.imgProfile.setOnClickListener(view -> profile());
    }

    private void greetings() {
        String username = sessionManager.getUsername();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 0 && hour < 11) {
            binding.tvWelcome.setText(String.format("Selamat Pagi %s", username));
        } else if (hour >= 11 && hour < 15) {
            binding.tvWelcome.setText(String.format("Selamat Siang %s", username));
        } else if (hour >= 15 && hour < 18) {
            binding.tvWelcome.setText(String.format("Selamat Sore %s", username));
        } else {
            binding.tvWelcome.setText(String.format("Selamat Malam %s", username));
        }
    }

    private void getDataPref() {
        listTextArea = new ArrayList<>();
        String listValue = sessionManager.getTextArea();
        if (listValue != null) {
            String[] arr = listValue.split(",");
            Collections.addAll(listTextArea, arr);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTextArea);
        binding.listView.setAdapter(adapter);
    }

    private void save() {
        String textArea = Objects.requireNonNull(binding.etTextArea.getText()).toString().trim();
        if (!textArea.isEmpty()) {
            saveText(textArea);
            binding.etTextArea.setText("");
        }
    }

    private void saveText(String text) {
        listTextArea.add(text);
        sessionManager.setTextArea(KEY_TEXT_AREA, TextUtils.join(",", listTextArea));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTextArea);
        binding.listView.setAdapter(adapter);
    }

    private void profile() {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}