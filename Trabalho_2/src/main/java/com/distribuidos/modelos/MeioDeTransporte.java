package com.distribuidos.modelos;

public class MeioDeTransporte {
    private String Placa;
    private String modelo;
    private int ano;
    private Boolean disponivel = true;
    
    public String getPlaca() {
        return Placa;
    }
    public void setPlaca(String placa) {
        Placa = placa;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public int getAno() {
        return ano;
    }
    public void setAno(int ano) {
        this.ano = ano;
    }
    public Boolean getDisponivel() {
        return disponivel;
    }
    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
    public MeioDeTransporte(String placa, String modelo, int ano) {
        Placa = placa;
        this.modelo = modelo;
        this.ano = ano;
    }
    public MeioDeTransporte() {
        // Necessário para Jackson / herança funcionar
    }
}