package br.pi.iesb;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetsenhaActivity extends AppCompatActivity {
    private EditText edtEmail;
    private Button btnEnviar;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetsenha);
        inicializarComponentes();
        eventoClick();
        String latitude = getIntent().getStringExtra("latitude");
        String longitude = getIntent().getStringExtra("longitude");

    }
    private void eventoClick(){
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtEmail.getText().toString().equals("")){
                    String email = edtEmail.getText().toString().trim();
                    resetSenha(email);
                }
            }
        });
    }

    private void resetSenha(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(ResetsenhaActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            alert("Um E-mail foi enviado para recuperar sua senha");
                            finish();
                        }
                        else{
                            alert("Email não encontrado");
                    }
                    }
                });
    }

    private void alert(String s) {
        Toast.makeText(ResetsenhaActivity.this,s,Toast.LENGTH_SHORT).show();
    }

    private void inicializarComponentes(){
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth=Dao.getFirebaseAuth();
    }
}
