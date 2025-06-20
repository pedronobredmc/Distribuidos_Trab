package com.distribuidos.interfaces;
import com.distribuidos.modelos.MeioDeTransporte;

public interface Locação {
    Boolean alugar(String placa);
    Boolean devolver(String placa);
}
