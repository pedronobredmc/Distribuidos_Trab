package com.example.locadora.services;

import com.example.locadora.models.Veiculo;
import com.example.locadora.repositories.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository repository;

    public List<Veiculo> listarTodos() {
        return repository.findAll();
    }

    public Optional<Veiculo> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Veiculo criar(Veiculo veiculo) {
        veiculo.setDisponivel(true); // novo veículo inicialmente disponível
        return repository.save(veiculo);
    }

    public Veiculo atualizar(Long id, Veiculo veiculoAtualizado) {
        return repository.findById(id).map(veiculo -> {
            veiculo.setMarca(veiculoAtualizado.getMarca());
            veiculo.setModelo(veiculoAtualizado.getModelo());
            veiculo.setAno(veiculoAtualizado.getAno());
            veiculo.setPlaca(veiculoAtualizado.getPlaca());
            veiculo.setDisponivel(veiculoAtualizado.isDisponivel());
            return repository.save(veiculo);
        }).orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
