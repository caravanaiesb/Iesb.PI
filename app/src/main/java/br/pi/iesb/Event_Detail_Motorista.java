package br.pi.iesb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
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

public class Event_Detail_Motorista extends AppCompatActivity {
    private ImageView imgEvento;
    private TextView nomeEventView,descricaoEvento,atracaoEvento,dataEvento;
    private DatabaseReference databaseReference,refDatabase,refevento;
    private FirebaseDatabase firebaseDatabase,firebaseData,firebaseevento;
    private Button btnParticipar,btnLoc;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private Usuario c;
    String posicao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event__detail__motorista);
        inicializaComponentes();
        exibirDados();
        posicao = getIntent().getStringExtra("key");
        eventosClicks();
        String longitude = getIntent().getStringExtra("long");
        String latitude = getIntent().getStringExtra("latitude");


    }

    private void eventosClicks() {
        btnParticipar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = Dao.getFirebaseAuth();
                firebaseUser = auth.getCurrentUser();
                firebaseData = FirebaseDatabase.getInstance();

                String emailFireBase = firebaseUser.getEmail();
                emailFireBase = emailFireBase.replace("@","_");
                emailFireBase = emailFireBase.replace(".","*");


                refDatabase = firebaseData.getReference("Usuarios/"+emailFireBase);
                refDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getChildren();
                        Usuario c = dataSnapshot.getValue(Usuario.class);
                        String longitude = getIntent().getStringExtra("long");
                        String latitude = getIntent().getStringExtra("latitude");
                        if(c==null || longitude==null){

                        }
                        else
                            firebaseevento = FirebaseDatabase.getInstance();

                            DatabaseReference myRef = firebaseevento.getReference("Eventos/"+posicao+"/Motoristas Disponiveis/");
                            String emailMotorista = c.getEmailUsuario().toString();
                            emailMotorista = emailMotorista.replace("@","_");
                            emailMotorista = emailMotorista.replace(".","*");
                            c.setEvento(posicao);
                            c.setPartidaLatitude(latitude);
                            c.setPartidaLongitude(longitude);
                            Toast.makeText(Event_Detail_Motorista.this,"Você está participando do evento!",Toast.LENGTH_SHORT);
                            myRef.child(emailMotorista).setValue(c);
                            Intent i = new Intent(Event_Detail_Motorista.this,FeedeventActivity.class);
                            startActivity(i);
                            finish();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });


        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Event_Detail_Motorista.this,MenuActivity.class);
                Toast.makeText(Event_Detail_Motorista.this,"Clique no ponto de partida da Carona!",Toast.LENGTH_LONG).show();
                i.putExtra("key",posicao);
                i.putExtra("Chave","C");
                startActivity(i);
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
        btnParticipar = (Button) findViewById(R.id.btnParticipar);
        btnLoc = (Button) findViewById(R.id.btnLoc);



    }
}
