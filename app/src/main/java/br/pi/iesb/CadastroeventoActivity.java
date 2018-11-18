package br.pi.iesb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastroeventoActivity extends AppCompatActivity {
    private EditText txtDataEvento,txtNomeEvento,txtTipoEvento,txtAtracaoPrincipal;
    private Button btnCancelarEvent,btnCadastrarEvent,btnCarregarImg;
    private FirebaseDatabase database;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private ImageView imgPreview;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastroevento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        inicializaComponentes();
        eventosClicks();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }
    private void eventosClicks() {
        btnCancelarEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCadastrarEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastraEvento();

            }
        });

        btnCarregarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarImg();
            }
        });
    }

    private void enviarImg() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageRef.child("Eventos/"+ txtNomeEvento.getText().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(CadastroeventoActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CadastroeventoActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void carregarImg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgPreview.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void cadastraEvento() {
        Evento c = null;
        if(validateFields(txtNomeEvento.getText().toString(),txtTipoEvento.getText().toString(),txtDataEvento.getText().toString(),txtAtracaoPrincipal.getText().toString())){
            c = new Evento(txtNomeEvento.getText().toString(),txtTipoEvento.getText().toString(),txtDataEvento.getText().toString(),txtAtracaoPrincipal.getText().toString());
            database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Eventos");
            String nomeEvento = txtNomeEvento.getText().toString();
            nomeEvento = nomeEvento.replace("@","_");
            nomeEvento = nomeEvento.replace(".","*");
            myRef.child(nomeEvento).setValue(c);

           // myRef.push().setValue(c);
            enviarImg();
        }
        else {
            alert("Falha ao tentar cadastrar novo evento");
        }
    }

    private boolean validateFields(String nomeEvento, String tipoEvento, String dataEveto, String atracao) {

       if(validateDate(dataEveto)){
         if(validaCampos(nomeEvento,tipoEvento,atracao)){
             return true;
         }
       }
       return false;
    }

    private boolean validaCampos(String nomeEvento, String tipoEvento, String atracao) {
        if (nomeEvento.length() > 4 && tipoEvento.length() > 4){
           if (atracao.length() > 4){
               return true;
           }

        }
        alert("Nome do evento deve ter mais 4 caracteres");
        return false;
    }

    private boolean validateDate(String dataEvent)  {


       /* SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
        Date dataEvento = dt.parse(String.valueOf(dataEvent));
// -----------------------
        Date date = new Date();
        String dataStringEvento = dt.format(date);

        return dataStringEvento;*/
       return true;
    }

    private void alert(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    private void inicializaComponentes() {
        txtAtracaoPrincipal= (EditText) findViewById(R.id.txtAtracaoPrincipal);
        txtDataEvento= (EditText) findViewById(R.id.txtDataEvento);
        txtNomeEvento= (EditText) findViewById(R.id.txtNomeEvento);
        txtTipoEvento= (EditText) findViewById(R.id.txtTipoEvento);
        btnCadastrarEvent =(Button) findViewById(R.id.btnCadastrarEvent);
        btnCancelarEvent = (Button) findViewById(R.id.btnCancelarEvent);
        btnCarregarImg = (Button) findViewById(R.id.btnCarregarImg);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
    }

}
