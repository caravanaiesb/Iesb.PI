package br.pi.iesb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PassageiroLista extends AppCompatActivity {
    private RecyclerView passageiroView;
    private List<Usuario> usuartioPLista = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private PassageiroLista.PassageiroAdapter passageiroAdapter;
    private PassageiroAdapter passageiroadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passageiro_lista);
        setTitle("Lista de Passageiros");
        mostrarFeed();
    }

    private void mostrarFeed() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        String posicao = getIntent().getStringExtra("key");
        String emailMotorista = getIntent().getStringExtra("key2");
        databaseReference = firebaseDatabase.getReference("Eventos/"+posicao+"/Motoristas Disponiveis/"+emailMotorista+"/Vagas Ocupadas/");

        passageiroadapter = new PassageiroAdapter();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuartioPLista.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    Usuario a =snap.getValue(Usuario.class);
                    Log.d("Passageiros",a.toString());
                    usuartioPLista.add(a);
                }

                passageiroadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        passageiroView = (RecyclerView) findViewById(R.id.recycleViewPassageiro);
        passageiroView.setItemAnimator(new DefaultItemAnimator());
        passageiroView.setLayoutManager(new LinearLayoutManager(this));
        passageiroView.setAdapter(passageiroadapter);
    }
    public class Passageiroadapter {
    }
    public class PassageiroAdapter extends RecyclerView.Adapter<PassageiroRecycleView> {

        @Override
        public PassageiroRecycleView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.linha_passageiros,viewGroup,false);
            return new PassageiroRecycleView(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull PassageiroRecycleView passageiroRecycleView, int i) {

            Usuario usuar = usuartioPLista.get(i);
            final String[] passageiroClickado = {(String) usuar.getEmailUsuario()};
            final String posicao = getIntent().getStringExtra("key");
            passageiroRecycleView.nomePassageiro.setText(usuar.getNomeUsuario());
            passageiroRecycleView.emailPassageiro.setText(usuar.getEmailUsuario());

            passageiroRecycleView.itemView.setTag(i);
            passageiroRecycleView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent z = new Intent(PassageiroLista.this, PerfilActivity.class);
                    z.putExtra("key", posicao);
                    passageiroClickado[0] = passageiroClickado[0].replace("@","_");
                    passageiroClickado[0] = passageiroClickado[0].replace(".","*");
                    z.putExtra("key2", passageiroClickado[0]);
                    startActivity(z);
                    finish();

                }});
        }
        @Override
        public int getItemCount() {
            return usuartioPLista.size();
        }
    }
}
