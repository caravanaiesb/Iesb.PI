package br.pi.iesb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail,edtSenha;
    private Button btnLogin,btnRegistrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        inicializaComponentes();
        eventoClicks();
    }
    private void eventoClicks(){
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent i= new Intent(getApplicationContext(),RegistroActivity.class);
                 startActivity(i);
            }
        });
    }
    private void inicializaComponentes(){
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegistrar= (Button) findViewById(R.id.btnRegistrar);
    }
}
