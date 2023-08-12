package com.amst.amst_ti1_panchana;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, nameEditText;
    private Button registerButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser() {
        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String name = nameEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Registro exitoso
                            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            prefs.edit().putString(email, name).apply();

                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Error al registrar
                            Toast.makeText(Register.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Manejar el error específico de correo en uso
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(Register.this, "El correo ya se encuentra registrado", Toast.LENGTH_SHORT).show();
                        } else {
                            // Otros errores en el registro
                            Toast.makeText(Register.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void cancel(View view) {
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
    }

}