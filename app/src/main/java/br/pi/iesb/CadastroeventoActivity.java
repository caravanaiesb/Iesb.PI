package br.pi.iesb;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CadastroeventoActivity extends AppCompatActivity {
    private EditText txtDataEvento,txtNomeEvento,txtTipoEvento,txtAtracaoPrincipal;
    private Button btnCancelarEvent,btnCadastrarEvent;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastroevento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        inicializaComponentes();
        eventosClicks();
        database=FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Eventos");
        DatabaseReference refEventos = database.getReference("Eventos/Eventos");
        refEventos.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Evento a = snap.getValue(Evento.class);
                    Log.d("Evento", a.toString());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void eventosClicks() {
        btnCancelarEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCadastrarEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Evento x = new Evento(txtNomeEvento.getText().toString(),txtTipoEvento.getText().toString(),txtDataEvento.getText().toString(),txtAtracaoPrincipal.getText().toString());
                adicionarLista(x);

            }
        });
    }



    private void adicionarLista(Evento x) {
        database=FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Eventos");
        DatabaseReference newRef = myRef.child("Eventos").push();
        newRef.setValue(x);

    }

    private void alert(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    private void inicializaComponentes() {
        txtAtracaoPrincipal= (EditText) findViewById(R.id.txtAtracaoPrincipal);
        txtDataEvento= (EditText) findViewById(R.id.txtDataEvento);
        txtNomeEvento= (EditText) findViewById(R.id.txtNomeEvento);
        txtTipoEvento= (EditText) findViewById(R.id.txtTipoEvento);
        btnCadastrarEvent =(Button) findViewById(R.id.btnCadastrarEvent);
        btnCancelarEvent = (Button) findViewById(R.id.btnCancelarEvent);
    }

}
