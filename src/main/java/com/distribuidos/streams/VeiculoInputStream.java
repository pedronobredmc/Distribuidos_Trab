package com.distribuidos.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.distribuidos.modelos.MeioDeTransporte;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;

public class VeiculoInputStream extends InputStream {
    private final InputStream origem;
    private final ObjectMapper mapper = new ObjectMapper();

    public VeiculoInputStream(InputStream origem) {
        this.origem = origem;
    }

    public List<MeioDeTransporte> lerVeiculos(Class<? extends MeioDeTransporte> tipo) throws IOException {
        List<MeioDeTransporte> veiculos = new ArrayList<>();

        int quantidade = readInt();

        for (int i = 0; i < quantidade; i++) {
            int tamanho = readInt();
            byte[] dados = new byte[tamanho];
            origem.read(dados);
            String json = new String(dados);

            MeioDeTransporte veiculo = mapper.readValue(json, tipo);
            veiculos.add(veiculo);
        }

        return veiculos;
    }

    private int readInt() throws IOException {
        byte[] buffer = new byte[4];
        origem.read(buffer);
        return ((buffer[0] & 0xFF) << 24) |
               ((buffer[1] & 0xFF) << 16) |
               ((buffer[2] & 0xFF) << 8) |
               (buffer[3] & 0xFF);
    }

    @Override
    public int read() throws IOException {
        return origem.read();
    }
}
