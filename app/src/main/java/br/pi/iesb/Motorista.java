package br.pi.iesb;

public class Motorista {
    private String nomeMotorista,emailMotorista,senhaMotorista,veiculo,placa,tipoUsuario;
    private Integer vagas;

    public Motorista(){

    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Motorista(String nomeMotorista, String emailMotorista, String senhaMotorista, String veiculo, String placa, Integer vagas, String tipoUsuario){

        this.nomeMotorista = nomeMotorista;
        this.emailMotorista = emailMotorista;
        this.senhaMotorista = senhaMotorista;
        this.veiculo = veiculo;
        this.placa = placa;
        this.vagas = vagas;
        this.tipoUsuario= tipoUsuario;

    }

    public String getNomeMotorista() {
        return nomeMotorista;
    }

    public void setNomeMotorista(String nomeMotorista) {
        this.nomeMotorista = nomeMotorista;
    }

    public String getEmailMotorista() {
        return emailMotorista;
    }

    public void setEmailMotorista(String emailMotorista) {
        this.emailMotorista = emailMotorista;
    }

    public String getSenhaMotorista() {
        return senhaMotorista;
    }

    public void setSenhaMotorista(String senhaMotorista) {
        this.senhaMotorista = senhaMotorista;
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

    public Integer getVagas() {
        return vagas;
    }

    public void setVagas(Integer vagas) {
        this.vagas = vagas;
    }


}
