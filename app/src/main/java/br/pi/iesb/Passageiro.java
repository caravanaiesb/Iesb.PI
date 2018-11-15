package br.pi.iesb;

public class Passageiro {

    private String nomePassageiro,emailPassageiro,senhaPassageiro,idadePassageiro;


    public Passageiro(){

    }
    public Passageiro(String nomePassageiro,String emailPassageiro,String senhaPassageiro,String idadePassageiro){
        this.nomePassageiro = nomePassageiro;
        this.emailPassageiro = emailPassageiro;
        this.senhaPassageiro = senhaPassageiro;
        this.idadePassageiro = idadePassageiro;
    }

    public String getIdadePassageiro() {
        return idadePassageiro;
    }

    public void setIdadePassageiro(String idadePassageiro) {
        this.idadePassageiro = idadePassageiro;
    }

    public String getNomePassageiro() {
        return nomePassageiro;
    }

    public void setNomePassageiro(String nomePassageiro) {
        this.nomePassageiro = nomePassageiro;
    }

    public String getEmailPassageiro() {
        return emailPassageiro;
    }

    public void setEmailPassageiro(String emailPassageiro) {
        this.emailPassageiro = emailPassageiro;
    }

    public String getSenhaPassageiro() {
        return senhaPassageiro;
    }

    public void setSenhaPassageiro(String senhaPassageiro) {
        this.senhaPassageiro = senhaPassageiro;
    }
}
