package com.distribuidos.Cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.distribuidos.modelos.MeioDeTransporte;
import com.distribuidos.streams.VeiculoInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Cliente {

    private static final String MULTICAST_IP = "230.0.0.1";
    private static final int MULTICAST_PORTA = 6000;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        limparTela(); 
        exibirCabecalho("SISTEMA DE LOCAÇÃO DE VEÍCULOS");

    
        System.out.print("Digite seu nome de usuário: ");
        String usuario = scanner.nextLine();
        System.out.println("Bem-vindo, " + usuario + "!");

        new Thread(() -> escutarMensagensMulticast()).start();

        Socket socket = new Socket("localhost", 5000);
        System.out.println("[CLIENTE] Conectado ao servidor.");
        BufferedWriter saida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ObjectMapper mapper = new ObjectMapper();

        boolean continuar = true;

        while (continuar) {
            limparTela(); 
            exibirCabecalho("SISTEMA DE LOCAÇÃO DE VEÍCULOS");

            System.out.println("MENU");
            System.out.println("1 - Listar veículos disponíveis");
            System.out.println("2 - Alugar veículo");
            System.out.println("3 - Devolver veículo");
            System.out.println("4 - Adicionar veículo");
            System.out.println("5 - Remover veículo");
            System.out.println("6 - Aplicar multa");
            System.out.println("7 - Cancelar multa");
            System.out.println("8 - Consultar multa");
            System.out.println("9 - Listar multas");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida! Por favor, digite um número.");
                scanner.nextLine(); 
                continue;
            }
            int opcao = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcao) {
                case 1:
                    limparTela();
                    exibirCabecalho("VEÍCULOS DISPONÍVEIS");

                    VeiculoInputStream visListar = new VeiculoInputStream(socket.getInputStream());
                    listarVeiculos(mapper, saida, visListar, entrada);
                    pressioneEnterParaContinuar();
                    break;

                case 2:
                    limparTela();
                    exibirCabecalho("ALUGAR VEÍCULO");

                    VeiculoInputStream visAlugar = new VeiculoInputStream(socket.getInputStream());
                    listarVeiculos(mapper, saida, visAlugar, entrada);

                    System.out.print("\nDigite a placa do veículo que deseja alugar: ");
                    String placaAlugar = scanner.nextLine();

                    Map<String, String> reqAlugar = new HashMap<>();
                    reqAlugar.put("operacao", "ALUGAR");
                    reqAlugar.put("placa", placaAlugar);
                    saida.write(mapper.writeValueAsString(reqAlugar) + "\n");
                    saida.flush();

                    String respostaAluguel = entrada.readLine();
                    if (respostaAluguel != null) {
                        Map<String, String> respAlugar = mapper.readValue(respostaAluguel, Map.class);
                        System.out.println(">> " + respAlugar.get("mensagem"));
                    } else {
                        System.out.println("⚠️ Nenhuma resposta recebida do servidor.");
                    }

                    pressioneEnterParaContinuar();
                    break;

                case 3:
                    limparTela();
                    exibirCabecalho("DEVOLVER VEÍCULO");

                    VeiculoInputStream visDevolver = new VeiculoInputStream(socket.getInputStream());
                    listarVeiculos(mapper, saida, visDevolver, entrada);

                    System.out.print("\nDigite a placa do veículo que deseja devolver: ");
                    String placaDevolver = scanner.nextLine();

                    Map<String, String> reqDevolver = new HashMap<>();
                    reqDevolver.put("operacao", "DEVOLVER");
                    reqDevolver.put("placa", placaDevolver);
                    saida.write(mapper.writeValueAsString(reqDevolver) + "\n");
                    saida.flush();

                    String respostaDevolucao = entrada.readLine();
                    if (respostaDevolucao != null) {
                        Map<String, String> respDevolver = mapper.readValue(respostaDevolucao, Map.class);
                        System.out.println(">> " + respDevolver.get("mensagem"));
                    } else {
                        System.out.println("⚠️ Nenhuma resposta recebida do servidor.");
                    }

                    pressioneEnterParaContinuar();
                    break;

                case 4:
                    limparTela();
                    exibirCabecalho("ADICIONAR VEÍCULO");

                    System.out.print("Digite os dados do veículo (JSON): ");
                    String jsonAdd = scanner.nextLine();

                    Map<String, String> reqAdd = new HashMap<>();
                    reqAdd.put("operacao", "ADICIONAR");
                    reqAdd.put("dados", jsonAdd);
                    saida.write(mapper.writeValueAsString(reqAdd) + "\n");
                    saida.flush();

                    String respAdd = entrada.readLine();
                    if (respAdd != null) {
                        Map<String, String> resposta = mapper.readValue(respAdd, Map.class);
                        System.out.println(">> " + resposta.get("mensagem"));
                    }

                    pressioneEnterParaContinuar();
                    break;

                case 5:
                    limparTela();
                    exibirCabecalho("REMOVER VEÍCULO");

                    VeiculoInputStream visRemover = new VeiculoInputStream(socket.getInputStream());
                    listarVeiculos(mapper, saida, visRemover, entrada);

                    System.out.print("\nDigite a placa do veículo a ser removido: ");
                    String placaRemove = scanner.nextLine();

                    Map<String, String> reqRemove = new HashMap<>();
                    reqRemove.put("operacao", "REMOVER");
                    reqRemove.put("placa", placaRemove);
                    saida.write(mapper.writeValueAsString(reqRemove) + "\n");
                    saida.flush();

                    String respRemove = entrada.readLine();
                    if (respRemove != null) {
                        Map<String, String> resposta = mapper.readValue(respRemove, Map.class);
                        System.out.println(">> " + resposta.get("mensagem"));
                    }

                    pressioneEnterParaContinuar();
                    break;

                case 6:
                    limparTela();
                    exibirCabecalho("APLICAR MULTA");

                    VeiculoInputStream visAplicarMulta = new VeiculoInputStream(socket.getInputStream());
                    listarVeiculos(mapper, saida, visAplicarMulta, entrada);

                    System.out.print("\nDigite a placa do veículo: ");
                    String placaMulta = scanner.nextLine();

                    System.out.print("Digite o valor da multa: ");
                    double valorMulta = Double.parseDouble(scanner.nextLine());

                    Map<String, String> reqMulta = new HashMap<>();
                    reqMulta.put("operacao", "APLICAR_MULTA");
                    reqMulta.put("placa", placaMulta);
                    reqMulta.put("valor", String.valueOf(valorMulta));

                    saida.write(mapper.writeValueAsString(reqMulta) + "\n");
                    saida.flush();

                    String respostaMulta = entrada.readLine();
                    if (respostaMulta != null) {
                        Map<String, String> resp = mapper.readValue(respostaMulta, Map.class);
                        System.out.println(">> " + resp.get("mensagem"));
                    }

                    pressioneEnterParaContinuar();
                    break;

                case 7:
                    limparTela();
                    exibirCabecalho("CANCELAR MULTA");

                    VeiculoInputStream visCancelarMulta = new VeiculoInputStream(socket.getInputStream());
                    listarVeiculos(mapper, saida, visCancelarMulta, entrada);

                    System.out.print("\nDigite a placa do veículo: ");
                    String placaCancel = scanner.nextLine();

                    Map<String, String> reqCancel = new HashMap<>();
                    reqCancel.put("operacao", "CANCELAR_MULTA");
                    reqCancel.put("placa", placaCancel);

                    saida.write(mapper.writeValueAsString(reqCancel) + "\n");
                    saida.flush();

                    String respostaCancel = entrada.readLine();
                    if (respostaCancel != null) {
                        Map<String, String> resp = mapper.readValue(respostaCancel, Map.class);
                        System.out.println(">> " + resp.get("mensagem"));
                    }

                    pressioneEnterParaContinuar();
                    break;

                case 8:
                    limparTela();
                    exibirCabecalho("CONSULTAR MULTA");

                    VeiculoInputStream visConsultarMulta = new VeiculoInputStream(socket.getInputStream());
                    listarVeiculos(mapper, saida, visConsultarMulta, entrada);

                    System.out.print("\nDigite a placa do veículo: ");
                    String placaConsulta = scanner.nextLine();

                    Map<String, String> reqConsulta = new HashMap<>();
                    reqConsulta.put("operacao", "CONSULTAR_MULTA");
                    reqConsulta.put("placa", placaConsulta);

                    saida.write(mapper.writeValueAsString(reqConsulta) + "\n");
                    saida.flush();

                    String respostaConsulta = entrada.readLine();
                    if (respostaConsulta != null) {
                        Map<String, String> resp = mapper.readValue(respostaConsulta, Map.class);
                        System.out.println(">> " + resp.get("mensagem"));
                    }

                    pressioneEnterParaContinuar();
                    break;

                case 9:
                    limparTela();
                    exibirCabecalho("LISTAR MULTAS");

                    Map<String, String> reqListarMultas = new HashMap<>();
                    reqListarMultas.put("operacao", "LISTAR_MULTAS");

                    saida.write(mapper.writeValueAsString(reqListarMultas) + "\n");
                    saida.flush();

                    String respostaListarMultas = entrada.readLine();
                    if (respostaListarMultas != null) {
                        Map<String, String> resp = mapper.readValue(respostaListarMultas, Map.class);
                        System.out.println(">> " + resp.get("mensagem"));
                    }

                    pressioneEnterParaContinuar();
                    break;

                case 0:
                    continuar = false;
                    break;

                default:
                    System.out.println("Opção inválida.");
                    pressioneEnterParaContinuar();
            }
        }

        socket.close();
        System.out.println("Conexão encerrada.");
    }

    private static void listarVeiculos(ObjectMapper mapper, BufferedWriter saida, VeiculoInputStream vis,
            BufferedReader entrada) throws IOException {
        Map<String, String> reqListar = new HashMap<>();
        reqListar.put("operacao", "LISTAR");
        saida.write(mapper.writeValueAsString(reqListar) + "\n");
        saida.flush();

        List<MeioDeTransporte> lista = vis.lerVeiculos(MeioDeTransporte.class);

        System.out.println("\nVEÍCULOS DISPONÍVEIS:");
        for (MeioDeTransporte v : lista) {
            System.out.println(v.getPlaca() + " - " + v.getModelo() + " (" + v.getAno() + ")" +
                    (v.getDisponivel() ? " - Disponível" : " - Indisponível"));
        }
    }

    private static void escutarMensagensMulticast() {
        try {
            MulticastSocket socketMulticast = new MulticastSocket(MULTICAST_PORTA);
            InetAddress grupo = InetAddress.getByName(MULTICAST_IP);
            socketMulticast.joinGroup(grupo);

            byte[] buffer = new byte[1024];
            System.out.println("[INFO] Escutando mensagens dos administradores...");

            while (true) {
                DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
                socketMulticast.receive(pacote);
                String mensagem = new String(pacote.getData(), 0, pacote.getLength());
                System.out.println("\n[ADMIN] " + mensagem);
            }
        } catch (IOException e) {
            System.err.println("Erro no multicast: " + e.getMessage());
        }
    }

    private static void limparTela() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    private static void exibirCabecalho(String titulo) {
        String linha = "=".repeat(60);
        System.out.println(linha);
        System.out.printf("%30s%n", titulo);
        System.out.println(linha);
    }

    private static void pressioneEnterParaContinuar() {
        System.out.print("\nPressione ENTER para continuar...");
        try {
            System.in.read(); 
        } catch (IOException e) {
        }
    }
}