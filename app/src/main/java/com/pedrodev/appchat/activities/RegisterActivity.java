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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.models.User;
import com.pedrodev.appchat.providers.AuthProvider;
import com.pedrodev.appchat.providers.UsersProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    private CircleImageView mCircleImageViewBack;
    private TextInputEditText mTextInputUserName;
    private TextInputEditText mTextInputEmail;
    private TextInputEditText mTextInputPassword;
    private TextInputEditText mTextInputPasswordConfirm;
    private TextInputEditText mTextInputPhoneNumber;
    private Button mButtonRegister;
    AlertDialog mDialog;
    private AuthProvider mAuthProvider;
    private UsersProvider mUsersProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mTextInputUserName =  findViewById(R.id.textInputUserName);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputPassword =  findViewById(R.id.textInputRegisterPassword);
        mTextInputPasswordConfirm = findViewById(R.id.textInputRegisterPasswordConfirm);
        mTextInputPhoneNumber = findViewById(R.id.textInputRegisterPhoneNumber);

        mCircleImageViewBack = findViewById(R.id.CircleImageBack);
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

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });


    }

    private void register(){
        String username = mTextInputUserName.getText().toString();
        String emailUser = mTextInputEmail.getText().toString();
        String passwordUser = mTextInputPassword.getText().toString();
        String passwordUserConfirm = mTextInputPasswordConfirm.getText().toString();
        String phoneNumber = mTextInputPhoneNumber.getText().toString();
        if (!username.isEmpty() && !emailUser.isEmpty() && !passwordUser.isEmpty()
                && !passwordUserConfirm.isEmpty() && !phoneNumber.isEmpty()) {
                if (isEmailValid(emailUser)){
                    if (isPhoneValid(phoneNumber)) {
                    if(passwordUser.equals(passwordUserConfirm)){
                        if(passwordUser.length()>5){
                            createUser(emailUser,username,passwordUser,phoneNumber);
                        }else{
                            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(this, "Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
                    }
                    // <-- Verificar correo -->
                    }else{
                        Toast.makeText(this, "Telefono invalido",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this, "Email invalido",Toast.LENGTH_LONG).show();
                }
            }
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isPhoneValid(String phoneNumber) {
        String expression = "/^[\\(]?[\\+]?(\\d{2}|\\d{3})[\\)]?[\\s]?((\\d{6}|\\d{8})|(\\d{3}[\\*\\.\\-\\s]){2}\\d{3}|(\\d{2}[\\*\\.\\-\\s]){3}\\d{2}|(\\d{4}[\\*\\.\\-\\s]){1}\\d{4})|\\d{8}|\\d{10}|\\d{12}$/";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    // <-- Create user -->
    private void createUser(final String email, final String username, final String password, final String PhoneNumber) {
        mDialog.show();
        mAuthProvider.register(email,username)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sesión actual del user
                    String id = mAuthProvider.getUid();
                    User user = new User();
                    user.setId(id);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPhoneNumber(PhoneNumber);
                    user.setTimestamp(new Date().getTime());
                    mUsersProvider.create(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()){
                                mDialog.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(RegisterActivity.this,"No se pudo registrar el usuario correctamente o este ya existe", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    mDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

/*
                    String id = mAuth.getCurrentUser().getUid();

Map<String, Object> map = new HashMap<>();
                    map.put("email",email);
                    map.put("userName",username);

                    mFirestore.collection("Users").document(id).set(map)
 */