package com.example.locadora.services;

import com.example.locadora.models.Aluguel;
import com.example.locadora.models.Cliente;
import com.example.locadora.models.Veiculo;
import com.example.locadora.repositories.AluguelRepository;
import com.example.locadora.repositories.ClienteRepository;
import com.example.locadora.repositories.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public Aluguel realizarAluguel(Long clienteId, Long veiculoId, LocalDate dataFim) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Veiculo veiculo = veiculoRepository.findById(veiculoId)
            .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        if (!veiculo.isDisponivel()) {
            throw new RuntimeException("Veículo não disponível");
        }

        veiculo.setDisponivel(false);
        veiculoRepository.save(veiculo);

        Aluguel aluguel = new Aluguel();
        aluguel.setCliente(cliente);
        aluguel.setVeiculo(veiculo);
        aluguel.setDataInicio(LocalDate.now());
        aluguel.setDataFim(dataFim);
        aluguel.setFinalizado(false);

        return aluguelRepository.save(aluguel);
    }

    public Aluguel finalizarAluguel(Long aluguelId) {
        return aluguelRepository.findById(aluguelId).map(aluguel -> {
            aluguel.setFinalizado(true);
            aluguel.getVeiculo().setDisponivel(true);
            veiculoRepository.save(aluguel.getVeiculo());
            return aluguelRepository.save(aluguel);
        }).orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));
    }

    public List<Aluguel> listarTodos() {
        return aluguelRepository.findAll();
    }

    public Optional<Aluguel> buscarPorId(Long id) {
        return aluguelRepository.findById(id);
    }
    public void deletar(Long id) {
        aluguelRepository.deleteById(id);
    }
}
