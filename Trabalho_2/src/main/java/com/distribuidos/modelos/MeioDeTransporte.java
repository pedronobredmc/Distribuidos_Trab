package com.distribuidos.modelos;

public class MeioDeTransporte {
    private String placa;
    private String modelo;
    private int ano;
    private Boolean disponivel = true;
    
    public String getPlaca() {
        return placa;
    }
    public void setPlaca(String placa) {
        this.placa = placa;
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
        this.placa = placa.trim().toUpperCase();
        this.modelo = modelo;
        this.ano = ano;
    }

    public MeioDeTransporte() {
        // Necessário para Jackson / herança funcionar
    }
}