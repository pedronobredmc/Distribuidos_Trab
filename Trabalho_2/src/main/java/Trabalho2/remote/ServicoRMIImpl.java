package Trabalho2.remote;

import com.distribuidos.modelos.MeioDeTransporte;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServicoRMIImpl extends UnicastRemoteObject implements ServicoRMI {

    private List<MeioDeTransporte> veiculos = new ArrayList<>(); // Lista para armazenar veículos
    private List<String> alugueisAtivos = new ArrayList<>(); // Lista para armazenar os alugueis ativos

    public ServicoRMIImpl() throws RemoteException {
        super();
    }

@Override
public byte[] doOperation(String objectReference, String methodId, byte[] arguments) throws RemoteException {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(arguments);
         ObjectInputStream ois = new ObjectInputStream(bis)) {

        switch (methodId) {
            case "1": { // cadastrar veículo
                String modelo = ois.readUTF();
                String placa = ois.readUTF();
                int ano = ois.readInt();
                veiculos.add(new MeioDeTransporte(modelo, placa, ano));
                String resultado = "[Servidor] Veículo cadastrado: " + modelo + " - " + placa + " - " + ano;
                return serializarResultado(resultado);
            }

            case "2": { // listar veículos
                StringBuilder lista = new StringBuilder();
                for (MeioDeTransporte veiculo : veiculos) {
                    lista.append(veiculo.getModelo()).append(" - ").append(veiculo.getPlaca()).append(" - ").append(veiculo.getAno()).append("\n");
                }
                return serializarResultado("[Servidor] Lista de veículos disponíveis: \n" + lista.toString());
            }

            case "3": { // realizar aluguel
                String placa = ois.readUTF().trim().toUpperCase();
                int dias = ois.readInt();
                boolean veiculoEncontrado = false;

                for (MeioDeTransporte veiculo : veiculos) {
                    String placaRegistrada = veiculo.getPlaca().trim().toUpperCase();

                    if (placaRegistrada.equals(placa)) {
                        veiculoEncontrado = true;
                        if (!alugueisAtivos.contains(placa)) {
                            alugueisAtivos.add(placa);
                            return serializarResultado("[Servidor] Veículo " + veiculo.getModelo() + " alugado por " + dias + " dias.");
                        } else {
                            return serializarResultado("[Servidor] Veículo " + placa + " já está alugado.");
                        }
                    }
                }

                if (!veiculoEncontrado) {
                    return serializarResultado("[Servidor] Veículo não encontrado.");
                }
                break;
            }


            case "4": { // remover veículo
                String placa = ois.readUTF();
                veiculos.removeIf(veiculo -> veiculo.getPlaca().equals(placa));
                alugueisAtivos.remove(placa); // Se o veículo estava alugado, remove do aluguel
                String resultado = "[Servidor] Veículo " + placa + " removido do sistema.";
                return serializarResultado(resultado);
            }

            default:
                return serializarResultado("Método inválido.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        return serializarResultado("Erro ao processar operação: " + e.getMessage());
    }
    return new byte[0]; // Retorno padrão em caso de erro inesperado
}


    private byte[] serializarResultado(String resultado) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(resultado);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
