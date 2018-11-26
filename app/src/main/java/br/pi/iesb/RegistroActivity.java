package br.pi.iesb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegistroActivity extends AppCompatActivity {
    private EditText edtEmail, edtSenha, edtNome,edtIdade;
    private Button btnRegistrarse,btnVoltar,btnCarregarImgPerfilPassageiro;
    private String tipoUsuario;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private final int PICK_IMAGE_REQUEST = 71;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRefe = storage.getReference();
    private Uri filePath;
    private FirebaseAuth auth2;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        setTitle("Cadastro Passageiro");
        inicializaComponentes();
        eventoClicks();
        String chave = getIntent().getStringExtra("Chave2");
        if(chave.equals("G")){
            auth2 = Dao.getFirebaseAuth();
            firebaseUser = auth2.getCurrentUser();
            edtEmail.setText(firebaseUser.getEmail());
            edtNome.setText(firebaseUser.getDisplayName());
        }
    }
    private void eventoClicks(){
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")) {


                String email = edtEmail.getText().toString().trim();
                String nome = edtNome.getText().toString().trim();
                String senha = edtSenha.getText().toString().trim();
                    String chave = getIntent().getStringExtra("Chave2");
                    if (chave.equals("G")){
                        registrarPassageiroFirebase();
                    }
                    else{
                        criarUser(email,nome,senha);
                    }

            }else{
                    alert("Preencha os campos");
                }

            }
        });

        btnCarregarImgPerfilPassageiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregarIMG();
            }
        });
    }

    private void carregarIMG() {
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
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void criarUser(String email, String nome, String senha) {
        auth.createUserWithEmailAndPassword(email,senha)
                .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            registrarPassageiroFirebase();
                        }else
                            alert("Erro no Cadastro");
                    }
                });
    }

    private void registrarPassageiroFirebase() {

        Usuario p = new Usuario(edtNome.getText().toString(),edtEmail.getText().toString(),edtSenha.getText().toString(),edtIdade.getText().toString(),tipoUsuario="P");
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Usuarios");
        String emailFireBase = edtEmail.getText().toString();
        emailFireBase = emailFireBase.replace("@","_");
        emailFireBase = emailFireBase.replace(".","*");
        enviarImg();
        myRef.child(emailFireBase).setValue(p);
        ;
        //myRef.push().setValue(p);

    }

    private void enviarImg() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String emailStorage = edtEmail.getText().toString();
            emailStorage = emailStorage.replace("@","_");
            emailStorage = emailStorage.replace(".","*");
            StorageReference ref = storageRefe.child("Usuarios/"+ emailStorage);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(RegistroActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(RegistroActivity.this,MenuActivity.class);
                            i.putExtra("Chave","L");
                            startActivity(i);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegistroActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void alert(String msg){
        Toast.makeText(RegistroActivity.this,msg,Toast.LENGTH_LONG).show();
    }

    private void inicializaComponentes(){
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnRegistrarse = (Button) findViewById(R.id.btnRegistrarse);
        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        edtIdade = (EditText) findViewById(R.id.edtIdade);
        btnCarregarImgPerfilPassageiro = (Button)findViewById(R.id.btnCarregarImgPerfilPassageiro);
    }
    protected void onStart(){
        super.onStart();
        auth = Dao.getFirebaseAuth();
    }
}
