package com.distribuidos.testes;

import com.distribuidos.modelos.Carro;
import com.distribuidos.modelos.MeioDeTransporte;
import com.distribuidos.modelos.Moto;
import com.distribuidos.modelos.Caminhao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.util.List;

public class TestesStream {
    public static void main(String[] args) throws Exception {
        MeioDeTransporte[] veiculos = new MeioDeTransporte[] {
            new Carro("ABC1234", "Gol", 2018, 4, "Azul", "Gasolina"),
            new Carro("XYZ5678", "Onix", 2020, 4, "Preto", "Etanol"),
            new Moto("DEF4321", "CB500", 2019, "Vermelha", "Gasolina"),
            new Moto("GHI8765", "XRE300", 2021, "Branca", "Gasolina"),
            new Caminhao("JKL1357", "Volvo FH", 2022, 20000, "Diesel", "Cinza")
        };

        ObjectMapper mapper = new ObjectMapper();

        // === 1. Escrita no System.out (JSON legível) ===
        System.out.println("=== Escrita no System.out (JSON legível) ===");
        String jsonSaida = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(veiculos);
        System.out.println(jsonSaida);
        System.out.println();

        // === 2. Escrita em arquivo (JSON puro) ===
        System.out.println("=== Escrita em arquivo JSON puro ===");
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("veiculos.json"), veiculos);

        // === 3. Leitura do arquivo JSON puro ===
        System.out.println("=== Leitura do arquivo JSON puro ===");
        List<MeioDeTransporte> lista = mapper.readValue(new File("veiculos.json"), new TypeReference<List<MeioDeTransporte>>() {});

        for (MeioDeTransporte v : lista) {
            System.out.println(v.getPlaca() + " - " + v.getModelo() + " - " + v.getAno() + " - " + v.getDisponivel());
        }
    }
}
