package com.distribuidos.servidor;

import com.distribuidos.modelos.LocadoraDeVeiculos;
import com.distribuidos.modelos.MeioDeTransporte;
import com.distribuidos.streams.VeiculoOutputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Servidor {
    private static LocadoraDeVeiculos locadora;

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Carrega veículos do arquivo JSON
        List<MeioDeTransporte> listaVeiculos;
        try (FileReader reader = new FileReader("veiculos.json")) {
            listaVeiculos = mapper.readValue(reader, new TypeReference<List<MeioDeTransporte>>() {});
        }

        locadora = new LocadoraDeVeiculos("Minha Locadora", "Rua Exemplo, 123", listaVeiculos);

        ServerSocket servidor = new ServerSocket(5000);
        System.out.println("[SERVIDOR] Iniciado na porta 5000...");

        while (true) {
            Socket cliente = servidor.accept();
            System.out.println("[CONECTADO] Cliente: " + cliente.getInetAddress());
            new Thread(() -> tratarCliente(cliente)).start();
        }
    }

    private static synchronized void salvarVeiculos() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<MeioDeTransporte> lista = locadora.getVeiculosDisponiveis();
        MeioDeTransporte[] array = lista.toArray(new MeioDeTransporte[0]);
        mapper.writeValue(new File("veiculos.json"), array);
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
                    MeioDeTransporte[] array = lista.toArray(new MeioDeTransporte[0]);
                    new VeiculoOutputStream(array, array.length, saidaStream);
                    saidaStream.flush();
                    System.out.println("[RESPOSTA] Enviada lista com " + array.length + " veículos.");

                } else if ("ALUGAR".equalsIgnoreCase(operacao)) {
                    String placa = req.get("placa");
                    boolean sucesso = locadora.alugar(placa);

                    Map<String, String> resposta = new HashMap<>();
                    if (sucesso) {
                        salvarVeiculos();  // Atualiza arquivo
                        resposta.put("mensagem", "Alugado com sucesso");
                    } else {
                        resposta.put("mensagem", "Veículo não encontrado ou indisponível");
                    }

                    String jsonResposta = mapper.writeValueAsString(resposta);
                    saida.write(jsonResposta + "\n");
                    saida.flush();

                    System.out.println("[ALUGAR] Placa: " + placa + " → " + resposta.get("mensagem"));

                } else if ("DEVOLVER".equalsIgnoreCase(operacao)) {
                    String placa = req.get("placa");
                    boolean sucesso = locadora.devolver(placa);

                    Map<String, String> resposta = new HashMap<>();
                    if (sucesso) {
                        salvarVeiculos();  // Atualiza arquivo
                        resposta.put("mensagem", "Devolução realizada com sucesso");
                    } else {
                        resposta.put("mensagem", "Veículo não encontrado ou já disponível");
                    }

                    String jsonResposta = mapper.writeValueAsString(resposta);
                    saida.write(jsonResposta + "\n");
                    saida.flush();

                    System.out.println("[DEVOLVER] Placa: " + placa + " → " + resposta.get("mensagem"));

                } else {
                    System.out.println("[AVISO] Operação não implementada: " + operacao);
                }
            }
        } catch (IOException e) {
            System.err.println("[ERRO] ao tratar cliente: " + e.getMessage());
        } finally {
            try {
                cliente.close();
                System.out.println("[DESCONECTADO] Cliente desconectado.");
            } catch (IOException e) {
                System.err.println("[ERRO] ao fechar conexão com cliente: " + e.getMessage());
            }
        }
    }

}
