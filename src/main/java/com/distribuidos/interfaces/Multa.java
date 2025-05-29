package com.distribuidos.interfaces;

public interface Multa {
    void aplicarMulta(String placa, double valor);
    void cancelarMulta(String placa);
    double consultarMulta(String placa);
    boolean verificarMulta(String placa);
    void listarMultas();
}
