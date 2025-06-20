package com.distribuidos.servidor;

import com.distribuidos.modelos.LocadoraDeVeiculos;
import com.distribuidos.modelos.MeioDeTransporte;
import com.distribuidos.streams.VeiculoOutputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private static LocadoraDeVeiculos locadora;

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        List<MeioDeTransporte> listaVeiculos = new ArrayList<>();
        try (FileReader reader = new FileReader("veiculos.json")) {
            listaVeiculos = mapper.readValue(reader, new TypeReference<List<MeioDeTransporte>>() {});
        }

        locadora = new LocadoraDeVeiculos("Minha Locadora", "Rua Exemplo, 123", listaVeiculos);

        ServerSocket servidor = new ServerSocket(5000);
        System.out.println("[SERVIDOR] Iniciado na porta 5000...");

        while (true) {
            Socket cliente = servidor.accept();
            new Thread(() -> tratarCliente(cliente)).start();
        }
    }

    private static synchronized void salvarVeiculos() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("veiculos.json"), locadora.getVeiculosDisponiveis().toArray());
    }

    private static void tratarCliente(Socket cliente) {
        try (
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            OutputStream saidaStream = cliente.getOutputStream();
            BufferedWriter saida = new BufferedWriter(new OutputStreamWriter(saidaStream))
        ) {
            ObjectMapper mapper = new ObjectMapper();
            String linha;

            while ((linha = entrada.readLine()) != null) {
                if (linha.isBlank()) continue;

                Map<String, String> req = mapper.readValue(linha, Map.class);
                String operacao = req.get("operacao");

                if ("LISTAR".equalsIgnoreCase(operacao)) {
                    List<MeioDeTransporte> lista = locadora.getVeiculosDisponiveis();
                    new VeiculoOutputStream(lista.toArray(new MeioDeTransporte[0]), lista.size(), saidaStream);
                    saidaStream.flush();

                } else if ("ALUGAR".equalsIgnoreCase(operacao)) {
                    String placa = req.get("placa");
                    boolean sucesso = false;

                    for (MeioDeTransporte v : locadora.getVeiculosDisponiveis()) {
                        if (v.getPlaca().equals(placa) && v.getDisponivel()) {
                            v.setDisponivel(false);
                            sucesso = true;
                            break;
                        }
                    }

                    Map<String, String> resposta = new HashMap<>();
                    if (sucesso) {
                        salvarVeiculos();
                        resposta.put("mensagem", "Alugado com sucesso");
                    } else {
                        resposta.put("mensagem", "Veículo não encontrado ou indisponível");
                    }
                    saida.write(mapper.writeValueAsString(resposta) + "\n");
                    saida.flush();

                } else if ("DEVOLVER".equalsIgnoreCase(operacao)) {
                    String placa = req.get("placa");
                    boolean sucesso = false;

                    for (MeioDeTransporte v : locadora.getVeiculosDisponiveis()) {
                        if (v.getPlaca().equals(placa) && !v.getDisponivel()) {
                            v.setDisponivel(true);
                            sucesso = true;
                            break;
                        }
                    }

                    Map<String, String> resposta = new HashMap<>();
                    if (sucesso) {
                        salvarVeiculos();
                        resposta.put("mensagem", "Devolução realizada com sucesso");
                    } else {
                        resposta.put("mensagem", "Veículo não encontrado ou já disponível");
                    }
                    saida.write(mapper.writeValueAsString(resposta) + "\n");
                    saida.flush();

                } else if ("APLICAR_MULTA".equalsIgnoreCase(operacao)) {
                    String placa = req.get("placa");
                    double valor = Double.parseDouble(req.get("valor"));
                    locadora.aplicarMulta(placa, valor);

                    Map<String, String> resposta = new HashMap<>();
                    resposta.put("mensagem", "Multa aplicada com sucesso.");
                    saida.write(mapper.writeValueAsString(resposta) + "\n");
                    saida.flush();

                } else if ("CANCELAR_MULTA".equalsIgnoreCase(operacao)) {
                    String placa = req.get("placa");
                    locadora.cancelarMulta(placa);

                    Map<String, String> resposta = new HashMap<>();
                    resposta.put("mensagem", "Multa cancelada com sucesso.");
                    saida.write(mapper.writeValueAsString(resposta) + "\n");
                    saida.flush();

                } else if ("CONSULTAR_MULTA".equalsIgnoreCase(operacao)) {
                    String placa = req.get("placa");
                    double valor = locadora.consultarMulta(placa);

                    Map<String, String> resposta = new HashMap<>();
                    resposta.put("mensagem", String.format("Multa para placa %s: R$%.2f", placa, valor));
                    saida.write(mapper.writeValueAsString(resposta) + "\n");
                    saida.flush();

                } else if ("LISTAR_MULTAS".equalsIgnoreCase(operacao)) {
                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<String, Double> entry : locadora.getMultas().entrySet()) {
                        sb.append(String.format("Placa: %s - Valor: R$%.2f\n", entry.getKey(), entry.getValue()));
                    }

                    Map<String, String> resposta = new HashMap<>();
                    resposta.put("mensagem", sb.toString());
                    saida.write(mapper.writeValueAsString(resposta) + "\n");
                    saida.flush();

                } else {
                    System.out.println("[AVISO] Operação desconhecida: " + operacao);
                }
            }
        } catch (IOException e) {
            System.err.println("[ERRO] ao tratar cliente: " + e.getMessage());
        } finally {
            try {
                cliente.close();
            } catch (IOException ignored) {}
        }
    }
}