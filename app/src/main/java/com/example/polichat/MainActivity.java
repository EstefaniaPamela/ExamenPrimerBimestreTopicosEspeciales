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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText nombre, email, password;

    private Button btn_registrar, btnIrLoginL;

    //creacion de firebaseAuth que nos brinda el paquete de Firebase
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    //VARIABLES LOCALES QUE VAMOS A REGISTRAR
    private String nombreL, emailL, passwordL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //instanciamos el metodo de firebase
        mAuth = FirebaseAuth.getInstance();

        nombre = (EditText) findViewById(R.id.editTextName);
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        btn_registrar = (Button) findViewById(R.id.btnRegistrar);
        btnIrLoginL = (Button) findViewById(R.id.btnIrLogin);


        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombreL = nombre.getText().toString();
                emailL = email.getText().toString();
                passwordL = password.getText().toString();

                //Validacion de campos llenos
                if(!nombreL.isEmpty() && !emailL.isEmpty() && !passwordL.isEmpty()){

                    if(passwordL.length() > 5 ){
                        registrarUsuario();
                    }else{
                        Toast.makeText(MainActivity.this, "El password debe tener almenos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Obligatorio llenar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnIrLoginL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                //finish();
                Toast.makeText(MainActivity.this, "Abriendo ventana Login ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //método para registrar usuario
    private void registrarUsuario(){
        mAuth.createUserWithEmailAndPassword(emailL, passwordL).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", nombreL);
                    map.put("email", emailL);
                    map.put("password", passwordL);
                    String id = mAuth.getCurrentUser().getUid();

                    mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                startActivity(new Intent(MainActivity.this, Chats.class));
                                Toast.makeText(MainActivity.this, "Abriendo segunda ventana", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(MainActivity.this, "No se pudieron crear los datos corectamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(MainActivity.this, "No se pudo registrar este usuario!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() !=null ){
            startActivity(new Intent(MainActivity.this, Chats.class));
            finish();
        }
    }
}