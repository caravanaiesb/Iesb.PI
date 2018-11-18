package br.pi.iesb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroActivity extends AppCompatActivity {
    private EditText edtEmail, edtSenha, edtNome,edtIdade;
    private Button btnRegistrarse,btnVoltar;
    private String tipoUsuario;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        inicializaComponentes();
        eventoClicks();
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
                criarUser(email,nome,senha);
            }else{
                    alert("Preencha os campos");
                }

            }
        });
    }

    private void criarUser(String email, String nome, String senha) {
        auth.createUserWithEmailAndPassword(email,senha)
                .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                         alert("Cadastrado com Sucesso");
                            registrarPassageiroFirebase();
                            Intent i = new Intent(RegistroActivity.this,MenuActivity.class);
                            startActivity(i);
                            finish();
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
        myRef.child(emailFireBase).setValue(p);
        //myRef.push().setValue(p);

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
    }
    protected void onStart(){
        super.onStart();
        auth = Dao.getFirebaseAuth();
    }
}
