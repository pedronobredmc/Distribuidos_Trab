package com.distribuidos.modelos;

public class Carro extends MeioDeTransporte {
    private int portas;
    private String cor;
    private String tipoDeCombustivel;

    public Carro(String placa, String modelo, int ano, int portas, String cor, String tipoDeCombustivel) {
        super(placa, modelo, ano);
        this.portas = portas;
        this.cor = cor;
        this.tipoDeCombustivel = tipoDeCombustivel;
    }

    public Carro() {
        // Construtor padrão necessário para Jackson
    }
    public int getPortas() {
        return portas;
    }

    public void setPortas(int portas) {
        this.portas = portas;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getTipoDeCombustivel() {
        return tipoDeCombustivel;
    }

    public void setTipoDeCombustivel(String tipoDeCombustivel) {
        this.tipoDeCombustivel = tipoDeCombustivel;
    }
}
