package com.distribuidos.modelos;
import java.util.List;
import com.distribuidos.interfaces.Locação;

public class LocadoraDeVeiculos implements Locação {
    private String nome;
    private String endereco;
    private List<MeioDeTransporte> veiculosDisponiveis;

    public LocadoraDeVeiculos(String nome, String endereco, List<MeioDeTransporte> veiculosDisponiveis) {
        this.nome = nome;
        this.endereco = endereco;
        this.veiculosDisponiveis = veiculosDisponiveis;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    @Override
    public Boolean alugar(String placa){
        for (MeioDeTransporte veiculo : veiculosDisponiveis) {
            if (veiculo.getPlaca().equals(placa) && veiculo.getDisponivel()) {
                veiculo.setDisponivel(false);
                return true; // Aluguel realizado com sucesso
            }
        }
        return false; // Veículo não encontrado ou indisponível
    }

    @Override
    public Boolean devolver(String placa) {
        for (MeioDeTransporte veiculo : veiculosDisponiveis) {
            if (veiculo.getPlaca().equals(placa) && !veiculo.getDisponivel()) {
                veiculo.setDisponivel(true);
                return true; // Devolução realizada com sucesso
            }
        }
        return false; // Veículo não encontrado ou já disponível
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<MeioDeTransporte> getVeiculosDisponiveis() {
        return veiculosDisponiveis;
    }

    public void setVeiculosDisponiveis(List<MeioDeTransporte> veiculosDisponiveis) {
        this.veiculosDisponiveis = veiculosDisponiveis;
    }
}
