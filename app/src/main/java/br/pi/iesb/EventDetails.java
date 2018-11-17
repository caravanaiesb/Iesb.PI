package br.pi.iesb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView nomeEventView,descricaoEvento,atracaoEvento,dataEvento;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private List<Evento> eventosLista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        inicializaComponentes();
        exibirDados();
    }

    private void exibirDados() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Eventos");
        final String posicao = getIntent().getStringExtra("key");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventosLista.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    Evento a =snap.getValue(Evento.class);
                    Log.d("Eventos",a.toString());
                    eventosLista.add(a);
                }
                if ( eventosLista == null){

                }else
                {
                    Evento x = eventosLista.get(Integer.parseInt(posicao));
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
                            int newWidth= imgEvento.getWidth();
                            int newHeight= imgEvento.getHeight();
                            final int COMPRESS_QUALITY = 0;

                            Bitmap bitmapImage = (BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

                            Bitmap mutableBitmapImage = Bitmap.createScaledBitmap(bitmapImage,newWidth, newHeight, false);

                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            mutableBitmapImage.compress(Bitmap.CompressFormat.PNG, 0 , outputStream);

                            if (mutableBitmapImage != bitmapImage) {
                                mutableBitmapImage.recycle();
                            }
                            bitmapImage.recycle();
                            Glide.with(EventDetails.this).load(outputStream.toByteArray()).into(imgEvento);

                            // Data for "images/island.jpg" is returns, use this as needed

                            Glide.with(EventDetails.this).load(bytes).into(imgEvento);
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


    }
}
