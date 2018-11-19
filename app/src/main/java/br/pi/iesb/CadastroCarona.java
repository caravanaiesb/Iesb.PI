package br.pi.iesb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
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

public class CadastroCarona extends AppCompatActivity {
    private Button btnRegistrarCarona,btn_Cancelar;
    private TextView nome_evento,vagas_disponiveis,veiculo_motorista,idade_motorista,nome_Motorista;
    private ImageView img_Motorista;
    private DatabaseReference databaseReference,refClick;
    private FirebaseDatabase firebaseDatabase,databaseClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_carona);
        inicializaComponentes();
        exibirDados();
    }

    private void exibirDados() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        String posicao = getIntent().getStringExtra("key");
        databaseReference = firebaseDatabase.getReference("Eventos/"+posicao);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren();
                Evento x=dataSnapshot.getValue(Evento.class);
                if ( x == null){
                }else
                {
                    nome_evento.setText(x.getTxtNomeEvento());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseClick = FirebaseDatabase.getInstance();
        String emailMotorista = getIntent().getStringExtra("key2");
        String posicao2 = getIntent().getStringExtra("key");
        refClick = databaseClick.getReference("Eventos/"+posicao+"/Motoristas Disponiveis/"+emailMotorista);
        refClick.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren();
                Usuario c=dataSnapshot.getValue(Usuario.class);
                if ( c == null){
                }else
                {
                    nome_Motorista.setText(c.getNomeUsuario());
                    veiculo_motorista.setText(c.getVeiculo());
                    idade_motorista.setText(c.getIdadeUsuario());
                    vagas_disponiveis.setText(c.getVagas().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference().child("Usuarios/"+emailMotorista );
        final long FIVE_MEGABYTE = 5120 * 1024;
        ref.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                img_Motorista.setImageBitmap(Bitmap.createScaledBitmap(bmp, img_Motorista.getWidth(),
                        img_Motorista.getHeight(), false));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });




    }

    private void inicializaComponentes() {
        btnRegistrarCarona = (Button) findViewById(R.id.btnRegistrarCarona);
        btn_Cancelar = (Button) findViewById(R.id.btn_Cancelar);
        nome_evento = (TextView) findViewById(R.id.nome_evento);
        vagas_disponiveis = (TextView) findViewById(R.id.vagas_disponiveis);
        veiculo_motorista = (TextView) findViewById(R.id.veiculo_motorista);
        idade_motorista = (TextView) findViewById(R.id.idade_motorista);
        nome_Motorista = (TextView) findViewById(R.id.nome_Motorista);
        img_Motorista = (ImageView) findViewById(R.id.img_Motorista);
    }
}
