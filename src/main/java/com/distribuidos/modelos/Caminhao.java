package com.distribuidos.modelos;

public class Caminhao extends MeioDeTransporte {
    private int capacidadeCarga;
    private String tipoDeCombustivel;
    private String cor;

    public Caminhao(String placa, String modelo, int ano, int capacidadeCarga, String tipoDeCombustivel, String cor) {
        super(placa, modelo, ano);
        this.capacidadeCarga = capacidadeCarga;
        this.tipoDeCombustivel = tipoDeCombustivel;
        this.cor = cor;
    }

    public Caminhao() {
        super();
        // Construtor padrão necessário para Jackson
    }

    public int getCapacidadeCarga() {
        return capacidadeCarga;
    }

    public void setCapacidadeCarga(int capacidadeCarga) {
        this.capacidadeCarga = capacidadeCarga;
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
