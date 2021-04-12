package com.example.tasks.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tasks.R;
import com.example.tasks.service.helper.FingerprintHelper;
import com.example.tasks.service.model.Feedback;
import com.example.tasks.viewmodel.LoginViewModel;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private final ViewHolder mViewHolder = new ViewHolder();
    private LoginViewModel mLoginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boolean access = FingerprintHelper.isAvailable(this);

        this.mViewHolder.editEmail = findViewById(R.id.edit_email);
        this.mViewHolder.editPassword = findViewById(R.id.edit_password);

        // Incializa as variáveis
        this.mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Cria observadores
        this.loadObservers();

        if(access) {
            this.mLoginViewModel.isFingerprintAvailable();
        }
    }

    public void onClickLogin(View view) {
        String email = this.mViewHolder.editEmail.getText().toString();
        String password = this.mViewHolder.editPassword.getText().toString();

        this.mLoginViewModel.login(email, password);
    }

    private void loadObservers() {
        this.mLoginViewModel.login.observe(this, feedback -> {
            if (feedback.isSuccess()) {
                startMain();
            } else {
                Toast.makeText(LoginActivity.this, feedback.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        this.mLoginViewModel.fingerprint.observe(this, isFingerprintAvaiable -> {
            if(isFingerprintAvaiable){
                openAuthentication();
            }
        });
    }

    private void openAuthentication() {
        //Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        //BiometricPrompt
        BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startMain();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        //BiometricInfo
        BiometricPrompt.PromptInfo info = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Titulo")
                .setSubtitle("Subtitulo")
                .setDescription("Description")
                .setNegativeButtonText("Cancelar")
                .build();

        biometricPrompt.authenticate(info);
    }

    private void startMain(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void onClickCreateAccount(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        EditText editEmail;
        EditText editPassword;
    }

}