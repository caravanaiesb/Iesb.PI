package br.pi.iesb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EventDetails extends AppCompatActivity {
    private ImageView imgEvento;
    private Button btnVerificar;
    private TextView nomeEventView,descricaoEvento,atracaoEvento,dataEvento;
    private DatabaseReference databaseReference,refClick;
    private FirebaseDatabase firebaseDatabase,databaseClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        inicializaComponentes();
        exibirDados();
        eventoClicks();
    }

    private void eventoClicks() {
        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseClick = FirebaseDatabase.getInstance();
                final String posicao = getIntent().getStringExtra("key");
                refClick = databaseClick.getReference("Eventos/"+posicao+"/Motoristas Disponiveis/");
                refClick.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getChildren();
                        Evento x=dataSnapshot.getValue(Evento.class);
                        if(x==null){
                            Toast.makeText(EventDetails.this,"Não há Motoristas Cadastrados",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent i = new Intent(EventDetails.this,MotoristaLista.class);
                            String clickposi = String.valueOf(posicao);
                            i.putExtra("key", clickposi);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
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
                    nomeEventView.setText(x.getTxtNomeEvento());
                    descricaoEvento.setText(x.getTxtTipoEvento());
                    dataEvento.setText(x.getTxtDataEvento());
                    atracaoEvento.setText(x.getTxtAtracaoPrincipal());
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference ref = storage.getReference().child("Eventos/"+x.getTxtNomeEvento() );
                    final long FIVE_MEGABYTE = 5120 * 1024;
                    ref.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imgEvento.setImageBitmap(Bitmap.createScaledBitmap(bmp, imgEvento.getWidth(),
                                    imgEvento.getHeight(), false));

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
        nomeEventView = (TextView) findViewById(R.id.nomeEventView);
        imgEvento = (ImageView) findViewById(R.id.imgEvento);
        dataEvento = (TextView) findViewById(R.id.dataEvento);
        descricaoEvento = (TextView) findViewById(R.id.tipoEvento);
        atracaoEvento = (TextView) findViewById(R.id.atracaoEvento);
        btnVerificar = (Button) findViewById(R.id.btnVerificar);


    }
}
