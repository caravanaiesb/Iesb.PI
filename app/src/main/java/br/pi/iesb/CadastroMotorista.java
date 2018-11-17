package br.pi.iesb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CadastroMotorista extends AppCompatActivity {

    private EditText nomeMotorista,emailMotorista,senhaMotorista,edtVeiculo,edtPlaca,edtVagasVeiculo;
    private Button btnRegistrarMotorista,btnCancelarRegistroMotorista;
    private String tipoUsuario;
    private FirebaseAuth auth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista);
        inicializaComponentes();
        eventoClicks();
    }


    private void eventoClicks() {
        btnRegistrarMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nomeMotorista.getText().toString().equals("") && !senhaMotorista.getText().toString().equals("")) {
                    if(!emailMotorista.getText().toString().equals("") && !edtVeiculo.getText().toString().equals("")) {
                        if(!edtPlaca.getText().toString().equals("") && !edtVagasVeiculo.getText().toString().equals("")){
                            String email = emailMotorista.getText().toString().trim();
                            String nome = nomeMotorista.getText().toString().trim();
                            String senha = senhaMotorista.getText().toString().trim();
                            criarLoginMotorista(email,senha);
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
            }
        });


    }

    private void criarLoginMotorista(String email, String senha) {
        auth.createUserWithEmailAndPassword(email,senha)
                .addOnCompleteListener(CadastroMotorista.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            alert("Cadastrado com sucesso!");
                            cadastrarRealtime();
                            Intent i = new Intent(CadastroMotorista.this,MenuActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else{
                            alert("Erro ao tentar registrar!");
                        }
                    }
                });




    }

    private void cadastrarRealtime() {

        Motorista x = new Motorista(nomeMotorista.getText().toString(),emailMotorista.getText().toString(),senhaMotorista.getText().toString(),edtVeiculo.getText().toString(),edtPlaca.getText().toString(),Integer.valueOf(edtVagasVeiculo.getText().toString()),tipoUsuario="M");

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Usuarios");
        myRef.push().setValue(x);


    }

    private void alert(String s) {
        Toast.makeText(CadastroMotorista.this,s,Toast.LENGTH_SHORT);
    }

    private void inicializaComponentes() {
        nomeMotorista = (EditText) findViewById(R.id.edtNomeMotorista);
        emailMotorista = (EditText) findViewById(R.id.edtEmailMotorista);
        senhaMotorista = (EditText) findViewById(R.id.edtSenhaMotorista);
        edtVeiculo = (EditText) findViewById(R.id.edtVeiculo);
        edtPlaca = (EditText) findViewById(R.id.edtPlacaVeiculo);
        edtVagasVeiculo = (EditText) findViewById(R.id.edtVagasVeiculo);
        btnRegistrarMotorista =(Button) findViewById(R.id.btnRegistrarMotorista);
        btnCancelarRegistroMotorista = (Button) findViewById(R.id.btnCancelarRegistroMotorista);

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Dao.getFirebaseAuth();
    }
}
