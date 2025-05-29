package com.distribuidos.modelos;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Carro.class, name = "carro"),
    @JsonSubTypes.Type(value = Moto.class, name = "moto"),
    @JsonSubTypes.Type(value = Caminhao.class, name = "caminhao")
})
public class MeioDeTransporte {
    private String Placa;
    private String modelo;
    private int ano;
    private Boolean disponivel = true;
    
    public String getPlaca() {
        return Placa;
    }
    public void setPlaca(String placa) {
        Placa = placa;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public int getAno() {
        return ano;
    }
    public void setAno(int ano) {
        this.ano = ano;
    }
    public Boolean getDisponivel() {
        return disponivel;
    }
    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
    public MeioDeTransporte(String placa, String modelo, int ano) {
        Placa = placa;
        this.modelo = modelo;
        this.ano = ano;
    }
    public MeioDeTransporte() {
        // Necessário para Jackson / herança funcionar
    }
}