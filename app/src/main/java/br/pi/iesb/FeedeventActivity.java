package br.pi.iesb;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeedeventActivity extends AppCompatActivity {

    private RecyclerView listaDados;
    private EventAdapter eventAdapter;
    private FirebaseDatabase database;
    private List<Evento> eventosLista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedevent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Eventos");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventosLista.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    Evento a =snap.getValue(Evento.class);
                    Log.d("Eventos",a.toString());
                    eventosLista.add(a);
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setSupportActionBar(toolbar);
        listaDados = (RecyclerView) findViewById(R.id.listaDados);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listaDados.setLayoutManager(linearLayoutManager);
        eventAdapter = new EventAdapter(eventosLista);
        listaDados.setAdapter(eventAdapter);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_CadastrarEvento) {
            Intent i = new Intent(FeedeventActivity.this,CadastroeventoActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


}