# Projeto - Sistema de Locação de Veículos com RMI

## Descrição Geral

Este projeto implementa um sistema distribuído básico de locadora de veículos utilizando a tecnologia Java RMI (Remote Method Invocation). O objetivo é demonstrar a comunicação entre cliente e servidor em uma aplicação distribuída, permitindo operações como cadastro, listagem, aluguel e remoção de veículos.

## Funcionalidades

- Cadastrar veículo (modelo, placa, ano)
- Listar veículos disponíveis
- Realizar aluguel de veículo por dias
- Remover veículo do sistema

## Arquitetura

O sistema segue uma arquitetura cliente-servidor composta pelas seguintes partes:

- **Cliente RMI**: Interface de linha de comando que envia requisições ao servidor.
- **Servidor RMI**: Processa as operações solicitadas pelo cliente.
- **Interface Remota (`ServicoRMI`)**: Define o método remoto utilizado para comunicação.
- **Implementação do Serviço (`ServicoRMIImpl`)**: Contém a lógica de negócio das operações.

## Tecnologias Utilizadas

- Java
- Java RMI (Remote Method Invocation)
- Serialização de objetos

## Como Executar

1. Inicie o servidor RMI executando a classe `ServidorRMI`.
2. Em outra instância da JVM, execute o cliente através da classe `ClienteRMI`.
3. Utilize o menu interativo no terminal para realizar as operações disponíveis.

## Autores

Pedro Nóbrega e William Marreiro
