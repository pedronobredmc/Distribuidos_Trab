package com.distribuidos.modelos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.distribuidos.interfaces.Multa; 

public class LocadoraDeVeiculos implements Multa {
    private String nome;
    private String endereco;
    private List<MeioDeTransporte> veiculosDisponiveis;
    private Map<String, Double> multas = new HashMap<>();

    public LocadoraDeVeiculos(String nome, String endereco, List<MeioDeTransporte> veiculosDisponiveis) {
        this.nome = nome;
        this.endereco = endereco;
        this.veiculosDisponiveis = veiculosDisponiveis;
    }

    @Override
    public void aplicarMulta(String placa, double valor) {
        multas.put(placa, multas.getOrDefault(placa, 0.0) + valor);
    }

    @Override
    public void cancelarMulta(String placa) {
        multas.remove(placa);
    }

    @Override
    public double consultarMulta(String placa) {
        return multas.getOrDefault(placa, 0.0);
    }

    @Override
    public boolean verificarMulta(String placa) {
        return multas.containsKey(placa);
    }

    @Override
    public void listarMultas() {
        if (multas.isEmpty()) {
            System.out.println("Nenhuma multa registrada.");
        } else {
            for (Map.Entry<String, Double> entry : multas.entrySet()) {
                System.out.printf("Placa: %s - Valor: R$%.2f%n", entry.getKey(), entry.getValue());
            }
        }
    }

    public List<MeioDeTransporte> getVeiculosDisponiveis() {
        return veiculosDisponiveis;
    }

    public Map<String, Double> getMultas() {
        return multas;
    }
}