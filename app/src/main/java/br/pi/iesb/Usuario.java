package br.pi.iesb;

public class Usuario {

    private String nomeUsuario,emailUsuario,senhaUsuario,veiculo,placa,idadeUsuario,tipoUsuario;
    private Integer vagas;


    public Usuario(){

    }
    public Usuario(String nomeUsuario, String emailUsuario, String senhaUsuario, String veiculo, String placa, Integer vagas, String tipoUsuario) {

        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.senhaUsuario = senhaUsuario;
        this.veiculo = veiculo;
        this.placa = placa;
        this.vagas = vagas;
        this.tipoUsuario = tipoUsuario;

    }

    public Usuario(String nomeUsuario, String emailUsuario, String senhaUsuario, String idadeUsuario, String tipoUsuario){
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.senhaUsuario = senhaUsuario;
        this.idadeUsuario = idadeUsuario;
        this.tipoUsuario = tipoUsuario;

    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getIdadeUsuario() {
        return idadeUsuario;
    }

    public void setIdadeUsuario(String idadeUsuario) {
        this.idadeUsuario = idadeUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Integer getVagas() {
        return vagas;
    }

    public void setVagas(Integer vagas) {
        this.vagas = vagas;
    }
}
