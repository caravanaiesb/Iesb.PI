package br.pi.iesb;

import java.util.ArrayList;
import java.util.Date;

public class Evento extends ArrayList<Evento> {
    public String txtNomeEvento;
    public String txtTipoEvento;
    public String txtDataEvento;
    public String txtAtracaoPrincipal;


    public Evento(){

    }



    public Evento(String txtNomeEvento, String txtTipoEvento, String txtDataEvento, String txtAtracaoPrincipal) {
        this.txtNomeEvento = txtNomeEvento;
        this.txtTipoEvento = txtTipoEvento;
        this.txtDataEvento = txtDataEvento;
        this.txtAtracaoPrincipal = txtAtracaoPrincipal;

    }


}
