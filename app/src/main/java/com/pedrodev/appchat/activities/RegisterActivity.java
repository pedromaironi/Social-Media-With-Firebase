package com.pedrodev.appchat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private CircleImageView mCircleImageViewBack;
    private TextInputEditText mTextInputUserName;
    private TextInputEditText mTextInputEmail;
    private TextInputEditText mTextInputPassword;
    private TextInputEditText mTextInputPasswordConfirm;
    private Button mButtonRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mTextInputUserName = (TextInputEditText) findViewById(R.id.textInputUserName);
        mTextInputEmail = (TextInputEditText) findViewById(R.id.textInputEmail);
        mTextInputPassword = (TextInputEditText) findViewById(R.id.textInputRegisterPassword);
        mTextInputPasswordConfirm = (TextInputEditText) findViewById(R.id.textInputRegisterPasswordConfirm);
        mCircleImageViewBack = findViewById(R.id.CircleImageBack);
        mButtonRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

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
        String nameUser = mTextInputUserName.getText().toString();
        String emailUser = mTextInputEmail.getText().toString();
        String passwordUser = mTextInputPassword.getText().toString();
        String passwordUserConfirm = mTextInputPasswordConfirm.getText().toString();

        if (!nameUser.isEmpty() && !emailUser.isEmpty() && !passwordUser.isEmpty() && !passwordUserConfirm.isEmpty()) {
            if (isEmailValid(emailUser)){
                if(passwordUser.equals(passwordUserConfirm)){
                    if(passwordUser.length()>6){
                        createUser(emailUser,nameUser);
                    }else{
                        Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
                }
                // <-- Verificar correo -->
            }else{
                Toast.makeText(this, "Email invalido",Toast.LENGTH_LONG).show();
            }
        }else{

        }
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // <-- Create user -->
    private void createUser(final String email, final String username) {
        mAuth.createUserWithEmailAndPassword(email,username).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sesión actual del user
                    String id = mAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("email",email);
                    map.put("userName",username);
                    mFirestore.collection("Users").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RegisterActivity.this,"No se pudo registrar el usuario correctamente o este ya existe", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}

