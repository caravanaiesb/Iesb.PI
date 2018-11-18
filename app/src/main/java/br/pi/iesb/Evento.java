package br.pi.iesb;

import java.util.ArrayList;
import java.util.List;

public class Evento {
    private String txtNomeEvento;
    private String txtTipoEvento;
    private String txtDataEvento;
    private String txtAtracaoPrincipal;
    private List<Usuario> motoristaLista = new ArrayList<>();


    public Evento(){

    }



    public Evento(String txtNomeEvento, String txtTipoEvento, String txtDataEvento, String txtAtracaoPrincipal) {
        this.txtNomeEvento = txtNomeEvento;
        this.txtTipoEvento = txtTipoEvento;
        this.txtDataEvento = txtDataEvento;
        this.txtAtracaoPrincipal = txtAtracaoPrincipal;

    }

    public String getTxtNomeEvento() {
        return txtNomeEvento;
    }

    public void setTxtNomeEvento(String txtNomeEvento) {
        this.txtNomeEvento = txtNomeEvento;
    }

    public String getTxtTipoEvento() {
        return txtTipoEvento;
    }

    public void setTxtTipoEvento(String txtTipoEvento) {
        this.txtTipoEvento = txtTipoEvento;
    }

    public String getTxtDataEvento() {
        return txtDataEvento;
    }

    public void setTxtDataEvento(String txtDataEvento) {
        this.txtDataEvento = txtDataEvento;
    }

    public String getTxtAtracaoPrincipal() {
        return txtAtracaoPrincipal;
    }

    public void setTxtAtracaoPrincipal(String txtAtracaoPrincipal) {
        this.txtAtracaoPrincipal = txtAtracaoPrincipal;
    }
    public List<Usuario> getMotoristaLista() {
        return motoristaLista;
    }

    public void setMotoristaLista(List<Usuario> motoristaLista) {
        this.motoristaLista = motoristaLista;
    }
}
