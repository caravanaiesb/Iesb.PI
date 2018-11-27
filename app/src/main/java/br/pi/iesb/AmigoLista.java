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

public class AmigoLista extends AppCompatActivity {
    private RecyclerView amigoView;
    private List<Usuario> usuartioPLista = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private AmigoLista.AmigoAdapter amigoAdapter;
    private AmigoAdapter amigoaadapter;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amigo_lista);
        setTitle("Lista de Amigos");
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

        amigoaadapter = new AmigoLista.AmigoAdapter();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuartioPLista.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    Usuario a =snap.getValue(Usuario.class);
                    Log.d("Amigos",a.toString());
                    usuartioPLista.add(a);
                }

                amigoaadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        amigoView = (RecyclerView) findViewById(R.id.recyclewAmigo);
        amigoView.setItemAnimator(new DefaultItemAnimator());
        amigoView.setLayoutManager(new LinearLayoutManager(this));
        amigoView.setAdapter(amigoaadapter);
    }
    public class Passageiroadapter {
    }



    public class AmigoAdapter extends RecyclerView.Adapter<AmigoRecyckeVuew> {

        @Override
        public AmigoRecyckeVuew onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.linha_amigo,viewGroup,false);
            return new AmigoRecyckeVuew(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull AmigoRecyckeVuew amigoRecyckeVuew, int i) {

            Usuario usuar = usuartioPLista.get(i);
            amigoRecyckeVuew.nomeUsuario.setText(usuar.getNomeUsuario());
            amigoRecyckeVuew.idadeUsuario.setText(usuar.getIdadeUsuario());

           //amigoRecyckeVuew.itemView.setTag(i);
        }
        @Override
        public int getItemCount() {
            return usuartioPLista.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AmigoLista.this,MenuActivity.class);
        i.putExtra("Chave","L");
        startActivity(i);
        finish();
    }
}
