package com.distribuidos.Cliente;

import com.distribuidos.modelos.MeioDeTransporte;
import com.distribuidos.streams.VeiculoInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;

public class Cliente {

    private static final String MULTICAST_IP = "230.0.0.1";
    private static final int MULTICAST_PORTA = 6000;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // 1. Login
        System.out.print("Digite seu nome de usuário: ");
        String usuario = scanner.nextLine();
        System.out.println("Bem-vindo, " + usuario + "!");

        // 2. Thread para escutar mensagens multicast
        new Thread(() -> escutarMensagensMulticast()).start();

        // 3. Conecta ao servidor TCP
        Socket socket = new Socket("localhost", 5000);
        System.out.println("[CLIENTE] Conectado ao servidor.");
        BufferedWriter saida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ObjectMapper mapper = new ObjectMapper();

        boolean continuar = true;

        while (continuar) {
            System.out.println("\nMENU");
            System.out.println("1 - Listar veículos disponíveis");
            System.out.println("2 - Alugar veículo");
            System.out.println("3 - Devolver veículo");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida! Por favor, digite um número.");
                scanner.nextLine(); // descarta entrada inválida
                continue;
            }
            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpa buffer

            switch (opcao) {
                case 1:
                    Map<String, String> reqListar = new HashMap<>();
                    reqListar.put("operacao", "LISTAR");
                    saida.write(mapper.writeValueAsString(reqListar) + "\n");
                    saida.flush();

                    VeiculoInputStream vis = new VeiculoInputStream(socket.getInputStream());
                    List<MeioDeTransporte> lista = vis.lerVeiculos(MeioDeTransporte.class);

                    System.out.println("\nVEÍCULOS DISPONÍVEIS:");
                    for (MeioDeTransporte v : lista) {
                        System.out.println(v.getPlaca() + " - " + v.getModelo() + " (" + v.getAno() + ")" +
                                (v.getDisponivel() ? " - Disponível" : " - Indisponível"));
                    }
                    break;

                case 2:
                    System.out.print("Digite a placa do veículo que deseja alugar: ");
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
                    break;

                case 3:
                    System.out.print("Digite a placa do veículo que deseja devolver: ");
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
                    break;

                case 0:
                    continuar = false;
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }

        socket.close();
        System.out.println("Conexão encerrada.");
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
}
