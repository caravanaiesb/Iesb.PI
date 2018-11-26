package br.pi.iesb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelecionaCadastroActivity extends AppCompatActivity {
    private Button btnPassageiro,btnMotorista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleciona_cadastro);
        inicializaComponentes();
        eventoClicks();
    }

    private void inicializaComponentes() {

        btnMotorista = (Button) findViewById(R.id.btnMotorista);
        btnPassageiro = (Button) findViewById(R.id.btnPassageiro);
    }

    public void eventoClicks(){
        final String chave = getIntent().getStringExtra("Chave2");
        btnPassageiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelecionaCadastroActivity.this,RegistroActivity.class);
                if(chave.equals("G")){
                    i.putExtra("Chave2","G");
                }
                startActivity(i);
                finish();
            }
        });
        btnMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelecionaCadastroActivity.this,CadastroMotorista.class);
                if(chave.equals("G")){
                    i.putExtra("Chave2","G");
                }
                startActivity(i);
                finish();
            }
        });
    }
}
