package com.distribuidos.modelos;

public class Moto extends MeioDeTransporte {
    private String tipoDeCombustivel;
    private String cor;

    public Moto(String placa, String modelo, int ano, String tipoDeCombustivel, String cor) {
        super(placa, modelo, ano);
        this.tipoDeCombustivel = tipoDeCombustivel;
        this.cor = cor;
    }

    public Moto() {
        super(); // Chama o construtor padrão da superclasse
    }


    public String getTipoDeCombustivel() {
        return tipoDeCombustivel;
    }

    public void setTipoDeCombustivel(String tipoDeCombustivel) {
        this.tipoDeCombustivel = tipoDeCombustivel;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
}
