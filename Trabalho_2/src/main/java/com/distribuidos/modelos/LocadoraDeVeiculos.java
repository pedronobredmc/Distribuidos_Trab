package com.distribuidos.modelos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocadoraDeVeiculos implements Serializable{
    private String nome;
    private String endereco;
    private List<MeioDeTransporte> veiculosDisponiveis;
    private Map<String, Double> multas = new HashMap<>();

    public LocadoraDeVeiculos(String nome, String endereco, List<MeioDeTransporte> veiculosDisponiveis) {
        this.nome = nome;
        this.endereco = endereco;
        this.veiculosDisponiveis = veiculosDisponiveis;
    }

    public List<MeioDeTransporte> getVeiculosDisponiveis() {
        return veiculosDisponiveis;
    }

    public Map<String, Double> getMultas() {
        return multas;
    }
}