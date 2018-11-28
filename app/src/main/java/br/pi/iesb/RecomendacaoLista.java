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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecomendacaoLista extends AppCompatActivity {
    private RecyclerView recyclewRec;
    private List<Usuario> usuartioRLista = new ArrayList<>();
    private List<Usuario> usuartioRecLista = new ArrayList<>();
    private DatabaseReference databaseReference,databaseReference2;
    private FirebaseDatabase firebaseDatabase,firebaseData2;
    private RecomendacaoLista.RecomendacaoAdapter recAdapter;
    private RecomendacaoAdapter recomendacaoadapter;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recomendacao_lista);
        setTitle("Amigos Sugeridos");
        mostrarFeed();
    }

    private void mostrarFeed() {
        auth = Dao.getFirebaseAuth();
        firebaseUser = auth.getCurrentUser();

        String emailFireBase = firebaseUser.getEmail();
        emailFireBase = emailFireBase.replace("@","_");
        emailFireBase = emailFireBase.replace(".","*");

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("Usuarios/"+emailFireBase+"/Amigos/");

        recomendacaoadapter = new RecomendacaoLista.RecomendacaoAdapter();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuartioRLista.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    Usuario a =snap.getValue(Usuario.class);
                    Log.d("Amigos",a.toString());
                    usuartioRLista.add(a);
                }
                for(int i=0; i<usuartioRLista.size();i++){
                    Usuario x = usuartioRLista.get(i);
                    recuperaAmigo(x);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclewRec = (RecyclerView) findViewById(R.id.recyclewRec);
        recyclewRec.setItemAnimator(new DefaultItemAnimator());
        recyclewRec.setLayoutManager(new LinearLayoutManager(this));
        recyclewRec.setAdapter(recomendacaoadapter);
    }

    private void recuperaAmigo(Usuario x) {
        firebaseData2 = FirebaseDatabase.getInstance();
        String emailRec = x.getEmailUsuario();
        emailRec = emailRec.replace("@","_");
        emailRec = emailRec.replace(".","*");
        databaseReference2 = firebaseData2.getReference("Usuarios/"+emailRec+"/Amigos/");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuartioRecLista.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren()) {
                    Usuario b = snap.getValue(Usuario.class);
                    Log.d("Amigos", b.toString());
                    usuartioRecLista.add(b);
                }
                recomendacaoadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class RecomendacaoAdapter extends RecyclerView.Adapter<RecomendacaoRecycleView> {

        @Override
        public RecomendacaoRecycleView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.linha_recomendacao,viewGroup,false);
            return new RecomendacaoRecycleView(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecomendacaoRecycleView recomendacaoRecycleView, int i) {

            Usuario usuar = usuartioRecLista.get(i);
            recomendacaoRecycleView.nomeUsuarioRec.setText(usuar.getNomeUsuario());
            recomendacaoRecycleView.idadeUsuarioRec.setText(usuar.getIdadeUsuario());
            final String[] usuarioClickado = {usuar.getEmailUsuario()};

            recomendacaoRecycleView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent z = new Intent(RecomendacaoLista.this, PerfilActivity.class);
                    z.putExtra("key", "a");
                    usuarioClickado[0] = usuarioClickado[0].replace("@","_");
                    usuarioClickado[0] = usuarioClickado[0].replace(".","*");
                    z.putExtra("key2", usuarioClickado[0]);
                    startActivity(z);
                }
            });
        }
        @Override
        public int getItemCount() {
            return usuartioRecLista.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RecomendacaoLista.this,MenuActivity.class);
        i.putExtra("Chave","L");
        startActivity(i);
        finish();
    }
}
