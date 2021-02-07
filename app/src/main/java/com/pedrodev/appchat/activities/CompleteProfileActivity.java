package com.pedrodev.appchat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.models.User;
import com.pedrodev.appchat.providers.AuthProvider;
import com.pedrodev.appchat.providers.UsersProvider;

import dmax.dialog.SpotsDialog;


public class CompleteProfileActivity extends AppCompatActivity {

    private TextInputEditText mTextInputUserName;
    private Button mButtonRegister;
    private AuthProvider mAuthProvider;
    private UsersProvider mUsersProvider;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        mTextInputUserName = (TextInputEditText) findViewById(R.id.textInputUserName);
        mButtonRegister = findViewById(R.id.btnRegister);

        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String nameUser = mTextInputUserName.getText().toString();
        if (!nameUser.isEmpty()) {
            updateUser(nameUser);
        } else {
            Toast.makeText(this, "Escribe un usuario correctamente", Toast.LENGTH_LONG).show();
        }
    }

    // <-- Create user -->
    private void updateUser(final String username) {
        // Sesión actual del user

        String id = mAuthProvider.getUid();
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        mDialog.show();

        mUsersProvider.update(user).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mDialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(CompleteProfileActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(CompleteProfileActivity.this, "No se pudo registrar el usuario correctamente o este ya existe", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

/*

 Map<String, Object> map = new HashMap<>();
        map.put("userName", username);
 */