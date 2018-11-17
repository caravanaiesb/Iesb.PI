package br.pi.iesb;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventDetails extends AppCompatActivity {
    private ImageView imgEvento;
    private TextView nomeEventView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        inicializaComponentes();
        exibirDados();
    }

    private void exibirDados() {
        String txtNome=getIntent().getStringExtra("key");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference().child("Eventos/"+txtNome);

        final long FIVE_MEGABYTE = 5120 * 1024;
        ref.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed

                Glide.with(EventDetails.this).load(bytes).into(imgEvento);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        nomeEventView.setText(txtNome);



    }

    private void inicializaComponentes() {
        nomeEventView = (TextView) findViewById(R.id.nomeEventView);
        imgEvento = (ImageView) findViewById(R.id.imgEvento);


    }
}
