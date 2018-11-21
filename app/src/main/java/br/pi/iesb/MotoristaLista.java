package br.pi.iesb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MotoristaLista extends AppCompatActivity {
    private RecyclerView motoristaRecyclerView;
    private FirebaseDatabase database;
    private List<Usuario> usuartioLista = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private MotoristaAdapter motoristaadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motorista_lista);
        setTitle("Motoristas disponiveis");
        mostrarFeed();

    }

    private void mostrarFeed() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        String posicao = getIntent().getStringExtra("key");
        databaseReference = firebaseDatabase.getReference("Eventos/"+posicao+"/Motoristas Disponiveis/");

        motoristaadapter = new MotoristaAdapter();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuartioLista.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    Usuario a =snap.getValue(Usuario.class);
                    Log.d("Motoristas",a.toString());
                    usuartioLista.add(a);
                }

                motoristaadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        motoristaRecyclerView = (RecyclerView) findViewById(R.id.recycleViewMotorista);
        motoristaRecyclerView.setItemAnimator(new DefaultItemAnimator());
        motoristaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        motoristaRecyclerView.setAdapter(motoristaadapter);

    }

    public class Motoristadapter {
    }



    public class MotoristaAdapter extends RecyclerView.Adapter<MotoristaRecycleView> {

        @Override
        public MotoristaRecycleView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.linha_motorista,viewGroup,false);
            return new MotoristaRecycleView(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MotoristaRecycleView motoristaRecycleView, int i) {

            FirebaseStorage storage = FirebaseStorage.getInstance();
            Usuario usuar = usuartioLista.get(i);
            final String posicao = getIntent().getStringExtra("key");
            final String[] motoristaClickado = {(String) usuar.getEmailUsuario()};
            final String[] emailUsuarioStorage = {usuar.getEmailUsuario()};
            emailUsuarioStorage[0] = emailUsuarioStorage[0].replace("@","_");
            emailUsuarioStorage[0] = emailUsuarioStorage[0].replace(".","*");

            StorageReference ref = storage.getReference().child("Usuarios/"+ emailUsuarioStorage[0]);
            final long FIVE_MEGABYTE = 5120 * 1024;
            ref.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Glide.with(MotoristaLista.this).load(bytes).into(motoristaRecycleView.circleImageMotorista);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

            motoristaRecycleView.txtNomeMotorista.setText(usuar.getNomeUsuario());
            motoristaRecycleView.veiculoMotorista.setText(usuar.getVeiculo());
            motoristaRecycleView.txtVagasDiponiveis.setText(usuar.getVagas().toString());

            motoristaRecycleView.itemView.setTag(i);
            motoristaRecycleView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent z = new Intent(MotoristaLista.this, CadastroCarona.class);
                    z.putExtra("key", posicao);
                    motoristaClickado[0] = motoristaClickado[0].replace("@","_");
                    motoristaClickado[0] = motoristaClickado[0].replace(".","*");
                    z.putExtra("key2", motoristaClickado[0]);
                    startActivity(z);
                    finish();

                }});

        }


        @Override
        public int getItemCount() {
            return usuartioLista.size();
        }
    }
}



