package br.pi.iesb;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class CadastroeventoActivity extends AppCompatActivity {
    private EditText txtDataEvento,txtNomeEvento,txtTipoEvento,txtAtracaoPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastroevento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        inicializaComponentes();

    }

    private void inicializaComponentes() {
        txtAtracaoPrincipal= (EditText) findViewById(R.id.txtAtracaoPrincipal);
        txtDataEvento= (EditText) findViewById(R.id.txtDataEvento);
        txtNomeEvento= (EditText) findViewById(R.id.txtNomeEvento);
        txtTipoEvento= (EditText) findViewById(R.id.txtTipoEvento);
    }

}
