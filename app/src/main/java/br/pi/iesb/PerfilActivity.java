package br.pi.iesb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PerfilActivity extends AppCompatActivity {
    private ImageView imgPassageiro;
    private TextView nome_passageiro,idade_passageiro;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);
        inicializaComponentes();
        exibirDados();

    }

    private void exibirDados() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        String posicao = getIntent().getStringExtra("key");
        final String emailUsuario = getIntent().getStringExtra("key2");
        databaseReference = firebaseDatabase.getReference("Usuarios/"+emailUsuario);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren();
                Usuario x=dataSnapshot.getValue(Usuario.class);
                if ( x == null){
                }else
                {
                    nome_passageiro.setText(x.getNomeUsuario());
                    idade_passageiro.setText(x.getIdadeUsuario());

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference ref = storage.getReference().child("Usuarios/"+emailUsuario);
                    final long FIVE_MEGABYTE = 5120 * 1024;
                    ref.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imgPassageiro.setImageBitmap(Bitmap.createScaledBitmap(bmp, imgPassageiro.getWidth(),
                                    imgPassageiro.getHeight(), false));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializaComponentes() {
        imgPassageiro = (ImageView) findViewById(R.id.imgPassageiro);
        nome_passageiro = (TextView)findViewById(R.id.nome_passageiro);
        idade_passageiro = (TextView) findViewById(R.id.idade_passageiro);
    }
}
