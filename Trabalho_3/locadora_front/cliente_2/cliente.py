import requests

BASE_URL = "http://localhost:8080"

def criar_cliente():
    print("=== Criar Cliente ===")
    nome = input("Nome: ")
    cpf = input("CPF: ")
    telefone = input("Telefone: ")
    email = input("Email: ")
    payload = {
        "nome": nome,
        "cpf": cpf,
        "telefone": telefone,
        "email": email
    }
    resp = requests.post(f"{BASE_URL}/clientes", json=payload)
    print(resp.json() if resp.ok else f"Erro: {resp.text}")

def listar_clientes():
    print("=== Lista de Clientes ===")
    resp = requests.get(f"{BASE_URL}/clientes")
    if resp.ok:
        for c in resp.json():
            print(f"[{c['id']}] {c['nome']} - {c['cpf']}")
    else:
        print(f"Erro: {resp.text}")

def criar_veiculo():
    print("=== Criar Veículo ===")
    placa = input("Placa: ")
    marca = input("Marca: ")
    modelo = input("Modelo: ")
    ano = input("Ano: ")
    payload = {
        "placa": placa,
        "marca": marca,
        "modelo": modelo,
        "ano": int(ano)
    }
    resp = requests.post(f"{BASE_URL}/veiculos", json=payload)
    print(resp.json() if resp.ok else f"Erro: {resp.text}")

def listar_veiculos():
    print("=== Lista de Veículos ===")
    resp = requests.get(f"{BASE_URL}/veiculos")
    if resp.ok:
        for v in resp.json():
            status = "Disponível" if v['disponivel'] else "Indisponível"
            print(f"[{v['id']}] {v['modelo']} - {v['placa']} ({status})")
    else:
        print(f"Erro: {resp.text}")

def realizar_aluguel():
    print("=== Realizar Aluguel ===")
    cliente_id = input("ID do Cliente: ")
    veiculo_id = input("ID do Veículo: ")
    data_fim = input("Data fim (YYYY-MM-DD): ")
    url = f"{BASE_URL}/alugueis/alugar"
    params = {
        "clienteId": cliente_id,
        "veiculoId": veiculo_id,
        "dataFim": data_fim
    }
    resp = requests.post(url, params=params)
    print(resp.json() if resp.ok else f"Erro: {resp.text}")

def listar_alugueis():
    print("=== Lista de Aluguéis ===")
    resp = requests.get(f"{BASE_URL}/alugueis")
    if resp.ok:
        for a in resp.json():
            status = "Finalizado" if a['finalizado'] else "Ativo"
            print(f"[{a['id']}] Cliente: {a['cliente']['nome']} | Veículo: {a['veiculo']['modelo']} | Até: {a['dataFim']} | {status}")
    else:
        print(f"Erro: {resp.text}")

def menu():
    while True:
        print("=== MENU CLIENTE PYTHON === \n1 - Criar Cliente \n 2 - Listar Clientes \n 3 - Criar Veículo \n 4 - Listar Veículos \n 5 - Realizar Aluguel \n 6 - Listar Aluguéis \n 0 - Sair")
        op = input("Opção: ")
        if op == "1":
            criar_cliente()
        elif op == "2":
            listar_clientes()
        elif op == "3":
            criar_veiculo()
        elif op == "4":
            listar_veiculos()
        elif op == "5":
            realizar_aluguel()
        elif op == "6":
            listar_alugueis()
        elif op == "0":
            break
        else:
            print("Opção inválida!")

if __name__ == "__main__":
    menu()

