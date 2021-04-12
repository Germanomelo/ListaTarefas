package com.example.tasks.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tasks.R;
import com.example.tasks.service.model.Feedback;
import com.example.tasks.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();
    private RegisterViewModel mRegisterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Botão de voltar nativo
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this.mViewHolder.editName = findViewById(R.id.edit_name);
        this.mViewHolder.editEmail = findViewById(R.id.edit_email);
        this.mViewHolder.editPassword = findViewById(R.id.edit_password);

        // Incializa variáveis
        this.mRegisterViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Cria observadores
        this.loadObservers();
    }

    private void loadObservers() {
        this.mRegisterViewModel.create.observe(this, feedback -> {
            if (feedback.isSuccess()) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, feedback.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onCreateAccount(View view) {
        String name = this.mViewHolder.editName.getText().toString();
        String email = this.mViewHolder.editEmail.getText().toString();
        String password = this.mViewHolder.editPassword.getText().toString();

        this.mRegisterViewModel.create(name, email, password);
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        EditText editName;
        EditText editEmail;
        EditText editPassword;
    }

}