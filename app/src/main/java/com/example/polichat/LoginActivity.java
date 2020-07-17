package com.example.polichat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText  email, password;
    private Button btn_login;

    //Variables para inicio de sesión
    private String emailV ="" , passwordV="";

    //cremos variable de tipo firebase auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //instanciamos variable auth
        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        btn_login = (Button) findViewById(R.id.btnLogin);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailV = email.getText().toString();
                passwordV = password.getText().toString();

                if(!emailV.isEmpty() && !passwordV.isEmpty()){
                    loginUser();
                }else{
                    Toast.makeText(LoginActivity.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loginUser(){
        mAuth.signInWithEmailAndPassword(emailV,passwordV).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, Chats.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "No se pudo iniciar sesión, compruebe los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}