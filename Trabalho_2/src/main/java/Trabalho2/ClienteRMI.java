package Trabalho2;

import Trabalho2.remote.ServicoRMI;

import java.io.*;
import java.rmi.Naming;
import java.util.Scanner;

public class ClienteRMI {
    public static void main(String[] args) {
        try {
            ServicoRMI servico = (ServicoRMI) Naming.lookup("rmi://localhost/ServicoRMI");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n==== MENU LOCAÇÃO DE VEÍCULOS ====");
                System.out.println("1. Cadastrar veículo");
                System.out.println("2. Listar veículos");
                System.out.println("3. Realizar aluguel");
                System.out.println("4. Remover veículo");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");

                int opcao = Integer.parseInt(scanner.nextLine());
                if (opcao == 0) break;

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);

                switch (opcao) {
                    case 1:
                        System.out.print("Placa: ");
                        oos.writeUTF(scanner.nextLine());
                        System.out.print("Modelo: ");
                        oos.writeUTF(scanner.nextLine());
                        System.out.print("Ano: ");
                        oos.writeInt(Integer.parseInt(scanner.nextLine()));
                        break;

                    case 2:
                        break;

                    case 3:
                        System.out.print("Placa do veículo para alugar: ");
                        oos.writeUTF(scanner.nextLine());
                        System.out.print("Dias de aluguel: ");
                        oos.writeInt(Integer.parseInt(scanner.nextLine()));
                        break;

                    case 4:
                        System.out.print("Placa do veículo para remover: ");
                        oos.writeUTF(scanner.nextLine());
                        break;

                    default:
                        System.out.println("Opção inválida.");
                        continue;
                }

                oos.flush();
                byte[] argumentos = bos.toByteArray();
                byte[] resposta = servico.doOperation("LocadoraService", String.valueOf(opcao), argumentos);
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(resposta));
                String respostaFinal = (String) ois.readObject();
                System.out.println("\n"+respostaFinal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
