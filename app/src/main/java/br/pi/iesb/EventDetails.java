package br.pi.iesb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

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

                int newWidth= 369;
                int newHeight= 144;
                    // PNG has not losses, it just ignores this field when compressing
                    final int COMPRESS_QUALITY = 0;

                    // Get the bitmap from byte array since, the bitmap has the the resize function
                    Bitmap bitmapImage = (BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

                    // New bitmap with the correct size, may not return a null object
                    Bitmap mutableBitmapImage = Bitmap.createScaledBitmap(bitmapImage,newWidth, newHeight, false);

                    // Get the byte array from tbe bitmap to be returned
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    mutableBitmapImage.compress(Bitmap.CompressFormat.PNG, 0 , outputStream);

                    if (mutableBitmapImage != bitmapImage) {
                        mutableBitmapImage.recycle();
                    } // else they are the same, just recycle once

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

        nomeEventView.setText(txtNome);



    }

    private void inicializaComponentes() {
        nomeEventView = (TextView) findViewById(R.id.nomeEventView);
        imgEvento = (ImageView) findViewById(R.id.imgEvento);


    }
}
