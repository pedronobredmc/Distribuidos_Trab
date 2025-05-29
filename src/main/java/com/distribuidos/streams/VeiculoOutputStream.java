package com.distribuidos.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.distribuidos.modelos.MeioDeTransporte;

import java.io.IOException;
import java.io.OutputStream;

public class VeiculoOutputStream extends OutputStream {
    private final OutputStream destino;
    private final ObjectMapper mapper = new ObjectMapper();

    public VeiculoOutputStream(MeioDeTransporte[] veiculos, int quantidade, OutputStream destino) throws IOException {
        this.destino = destino;

        // Escreve o número de objetos
        destino.write(intToBytes(quantidade));

        for (int i = 0; i < quantidade; i++) {
            String json = mapper.writeValueAsString(veiculos[i]);
            byte[] dados = json.getBytes();

            // Escreve o tamanho do JSON e o JSON em si
            destino.write(intToBytes(dados.length));
            destino.write(dados);
        }
    }

    @Override
    public void write(int b) throws IOException {
        destino.write(b);
    }

    private byte[] intToBytes(int valor) {
        return new byte[] {
            (byte) (valor >>> 24),
            (byte) (valor >>> 16),
            (byte) (valor >>> 8),
            (byte) valor
        };
    }

}
