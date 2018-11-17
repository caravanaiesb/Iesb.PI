package br.pi.iesb;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.GlideModule;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeedeventActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private List<Evento> eventosLista = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseRecyclerAdapter<Evento,EventRecycleViewHolder> adapter;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feedevent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mostrarFeed();

    }

    private void mostrarFeed() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Eventos");

        eventAdapter = new EventAdapter();
        databaseReference.addValueEventListener(new ValueEventListener() {
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

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_CadastrarEvento) {
            Intent i = new Intent(FeedeventActivity.this,CadastroeventoActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }



    public class EventAdapter extends RecyclerView.Adapter<EventRecycleViewHolder> {

        @Override
        public EventRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.linha_event,viewGroup,false);
            return new EventRecycleViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final EventRecycleViewHolder holder, int i) {

            FirebaseStorage storage = FirebaseStorage.getInstance();
            final Evento model = eventosLista.get(i);
            StorageReference ref = storage.getReference().child("Eventos/" + model.getTxtNomeEvento());

            final long FIVE_MEGABYTE = 5120 * 1024;
            ref.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                    Glide.with(FeedeventActivity.this).load(bytes).into(holder.circleImg);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

            holder.txtNomeEvent.setText(model.getTxtNomeEvento());
            holder.txtDescEvent.setText(model.getTxtTipoEvento());
            holder.txtAtracao.setText(model.getTxtAtracaoPrincipal());

            holder.itemView.setTag(i);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickPosition = (int) view.getTag();
                    Intent z = new Intent(FeedeventActivity.this,EventDetails.class);
                    z.putExtra("key",model.getTxtNomeEvento());
                    startActivity(z);

                    Toast.makeText(FeedeventActivity.this, "posicao " + clickPosition, Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        public int getItemCount() {
            return eventosLista.size();
        }
        }
    }
