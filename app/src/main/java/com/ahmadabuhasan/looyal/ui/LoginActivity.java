package com.ahmadabuhasan.looyal.ui;

import static com.ahmadabuhasan.looyal.utils.Constant.KEY_LOGIN;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmadabuhasan.looyal.R;
import com.ahmadabuhasan.looyal.databinding.ActivityLoginBinding;
import com.ahmadabuhasan.looyal.utils.SessionManager;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        binding.tvToRegister.setOnClickListener(view -> toRegister());
        binding.btnLogin.setOnClickListener(view -> login());
    }

    private void toRegister() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void login() {
        String checkUsername = sessionManager.getUsername();
        String checkPassword = sessionManager.getPassword();
        String username = Objects.requireNonNull(binding.etUsername.getText()).toString();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString();

        if (username.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Username or password can't be empty", Toast.LENGTH_SHORT).show();
        } else if (!username.equals(checkUsername)) {
            Toast.makeText(this, "You're not registered", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(checkPassword)) {
            Toast.makeText(this, "The username or password is incorrect", Toast.LENGTH_SHORT).show();
        } else {
            sessionManager.setBooleanPref(KEY_LOGIN, true);
            Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Boolean isLogin = sessionManager.getIsLogin();
        if (isLogin) {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }
}