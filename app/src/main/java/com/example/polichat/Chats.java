package com.example.polichat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Chats extends AppCompatActivity {

    private Button mSalir;
    private TextView txtNombre;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    //variables para el chat
    private RecyclerView rvMensaje;
    private EditText etNameL, etMensaje;
    private ImageButton btnImagenEnviar;

    private List<MensajeVO> lstMensajes;
    private AdapterRVMensaje mAdapterRVMensaje;

    private void setComponents(){
        rvMensaje = findViewById(R.id.rvMensaje);
        etNameL = findViewById(R.id.etName);
        btnImagenEnviar = findViewById(R.id.btnEnviar);
        lstMensajes = new ArrayList<>();
        mAdapterRVMensaje = new AdapterRVMensaje(lstMensajes);
        rvMensaje.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvMensaje.setAdapter(mAdapterRVMensaje);
        rvMensaje.setHasFixedSize(true);

        FirebaseFirestore.getInstance().collection("Chat")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentChange mDocumentChange : value.getDocumentChanges()){
                            if (mDocumentChange.getType() == DocumentChange.Type.ADDED){
                                lstMensajes.add(mDocumentChange.getDocument().toObject(MensajeVO.class));
                                mAdapterRVMensaje.notifyDataSetChanged();
                                rvMensaje.scrollToPosition(lstMensajes.size());
                            }

                        }
                    }
                });
        btnImagenEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNameL.length()==0)
                    return;
                MensajeVO mMensajeVO = new MensajeVO();
                mMensajeVO.setName(txtNombre.getText().toString());
                mMensajeVO.setMensaje(etNameL.getText().toString());
                FirebaseFirestore.getInstance().collection("Chat").add(mMensajeVO);
                etNameL.setText("");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        setComponents();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSalir = (Button) findViewById(R.id.btn_salir);
        txtNombre = (TextView) findViewById(R.id.txtNombreU);

        mSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(Chats.this, MainActivity.class));
                finish();//Permite no volver hacia atras al cerrar la sesión
            }
        });

        obtenerUsuario();
    }
    private void obtenerUsuario(){
        String id = mAuth.getCurrentUser().getUid();

        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nombre = snapshot.child("name").getValue().toString();
                    txtNombre.setText("Cuenta: "+nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}