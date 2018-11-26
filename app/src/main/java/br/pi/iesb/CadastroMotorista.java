package br.pi.iesb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CadastroMotorista extends AppCompatActivity {

    private EditText nomeUsuario,emailUsuario,senhaUsuario,edtVeiculo,edtPlaca,edtVagasVeiculo,idadeUsuario;
    private Button btnRegistrarMotorista,btnCancelarRegistroMotorista,btnCarregarImgPerfil;
    private String tipoUsuario;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRefe = storage.getReference();
    private Uri filePath;
    private FirebaseAuth auth2;
    private FirebaseUser firebaseUser;
    private final int PICK_IMAGE_REQUEST = 71;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista);
        inicializaComponentes();
        setTitle("Cadastro Motorista");
        eventoClicks();
        String chave = getIntent().getStringExtra("Chave2");
        if(chave.equals("G")){
            auth2 = Dao.getFirebaseAuth();
            firebaseUser = auth2.getCurrentUser();
            emailUsuario.setText(firebaseUser.getEmail());
            nomeUsuario.setText(firebaseUser.getDisplayName());
        }
    }


    private void eventoClicks() {
        btnRegistrarMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nomeUsuario.getText().toString().equals("") && !senhaUsuario.getText().toString().equals("")) {
                    if(!emailUsuario.getText().toString().equals("") && !edtVeiculo.getText().toString().equals("")) {
                        if(!edtPlaca.getText().toString().equals("") && !edtVagasVeiculo.getText().toString().equals("")){
                            String email = emailUsuario.getText().toString().trim();
                            String nome = nomeUsuario.getText().toString().trim();
                            String senha = senhaUsuario.getText().toString().trim();
                            String chave = getIntent().getStringExtra("Chave2");
                            if (chave.equals("G")){
                                cadastrarRealtime();
                            }
                            else{
                                criarLoginMotorista(email,senha);
                            }

                        }else{
                            alert("Preencha todos campos");
                        }
                    }
                    alert("Preencha todos campos");

                }else{
                    alert("Preencha todos campos");
                }

            }
        });

        btnCancelarRegistroMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CadastroMotorista.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnCarregarImgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarImg();
            }
        });


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
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void criarLoginMotorista(String email, String senha) {
        auth.createUserWithEmailAndPassword(email,senha)
                .addOnCompleteListener(CadastroMotorista.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            alert("Cadastrado com sucesso!");
                            cadastrarRealtime();
                        }
                        else{
                            alert("Erro ao tentar registrar!");
                        }
                    }
                });




    }

    private void cadastrarRealtime() {

        Usuario x = new Usuario(nomeUsuario.getText().toString(),emailUsuario.getText().toString(),senhaUsuario.getText().toString(),edtVeiculo.getText().toString(),edtPlaca.getText().toString(),edtVagasVeiculo.getText().toString(),tipoUsuario="M",idadeUsuario.getText().toString());

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Usuarios");

        String emailFireBase = emailUsuario.getText().toString();
        emailFireBase = emailFireBase.replace("@","_");
        emailFireBase = emailFireBase.replace(".","*");
        myRef.child(emailFireBase).setValue(x);
        enviarImg();


    }

    private void enviarImg() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String emailStorage = emailUsuario.getText().toString();
            emailStorage = emailStorage.replace("@","_");
            emailStorage = emailStorage.replace(".","*");
            StorageReference ref = storageRefe.child("Usuarios/"+ emailStorage);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(CadastroMotorista.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(CadastroMotorista.this,MenuActivity.class);
                            i.putExtra("Chave","L");
                            startActivity(i);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CadastroMotorista.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void alert(String s) {
        Toast.makeText(CadastroMotorista.this,s,Toast.LENGTH_SHORT);
    }

    private void inicializaComponentes() {
        nomeUsuario = (EditText) findViewById(R.id.edtNomeMotorista);
        emailUsuario = (EditText) findViewById(R.id.edtEmailMotorista);
        senhaUsuario = (EditText) findViewById(R.id.edtSenhaMotorista);
        edtVeiculo = (EditText) findViewById(R.id.edtVeiculo);
        edtPlaca = (EditText) findViewById(R.id.edtPlacaVeiculo);
        idadeUsuario = (EditText) findViewById(R.id.idadeUsuario);
        edtVagasVeiculo = (EditText) findViewById(R.id.edtVagasVeiculo);
        btnRegistrarMotorista =(Button) findViewById(R.id.btnRegistrarMotorista);
        btnCancelarRegistroMotorista = (Button) findViewById(R.id.btnCancelarRegistroMotorista);
        btnCarregarImgPerfil = (Button) findViewById(R.id.btnCarregarImgPerfil);

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Dao.getFirebaseAuth();
    }
}
