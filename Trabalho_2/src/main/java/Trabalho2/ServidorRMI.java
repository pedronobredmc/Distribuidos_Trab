package Trabalho2;

import Trabalho2.remote.ServicoRMI;
import Trabalho2.remote.ServicoRMIImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServidorRMI {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            ServicoRMI servico = new ServicoRMIImpl();
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            Naming.rebind("ServicoRMI", servico);
            System.out.println("Servidor RMI da Locadora iniciado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
