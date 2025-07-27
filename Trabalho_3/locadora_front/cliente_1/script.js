const API = 'http://localhost:8080';

function showError(error) {
  try {
    const json = JSON.parse(error.message);
    const trace = json.trace || '';
    const match = trace.match(/RuntimeException: (.*?)(\n|$)/);
    const msg = match ? match[1] : error.message;
    alert("Erro: " + msg);
  } catch (e) {
    alert("Erro: " + error.message);
  }
}

// ---------------- CLIENTES ------------------
formCliente.onsubmit = async e => {
  e.preventDefault();
  const cliente = {
    nome: nome.value,
    cpf: cpf.value,
    telefone: telefone.value,
    email: email.value
  };
  try {
    const res = await fetch(`${API}/clientes`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(cliente)
    });
    if (!res.ok) {
      const errorMsg = await res.text();
      throw new Error(errorMsg);
    }
    e.target.reset();
    listarClientes();
  } catch (err) {
    showError(err);
  }
};

async function listarClientes() {
  try {
    const res = await fetch(`${API}/clientes`);
    if (!res.ok) throw new Error(await res.text());
    const lista = await res.json();
    listaClientes.innerHTML = lista.map(c => `
      <li class="list-group-item">
        <strong>${c.nome}</strong> (${c.id})
        <button class="btn btn-sm btn-info float-end ms-2" onclick='editar("cliente", ${JSON.stringify(c)})'>Editar</button>
        <button class="btn btn-sm btn-danger float-end" onclick='deletar("cliente", ${c.id})'>Remover</button>
      </li>
    `).join('');
  } catch (err) {
    showError(err);
  }
}

// ---------------- VEICULOS ------------------
formVeiculo.onsubmit = async e => {
  e.preventDefault();
  const veiculo = {
    placa: placa.value,
    marca: marca.value,
    modelo: modelo.value,
    ano: ano.value
  };
  try {
    const res = await fetch(`${API}/veiculos`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(veiculo)
    });
    if (!res.ok) {
      const errorMsg = await res.text();
      throw new Error(errorMsg);
    }
    e.target.reset();
    listarVeiculos();
  } catch (err) {
    showError(err);
  }
};

async function listarVeiculos() {
  try {
    const res = await fetch(`${API}/veiculos`);
    if (!res.ok) throw new Error(await res.text());
    const lista = await res.json();
    listaVeiculos.innerHTML = lista.map(v => `
      <li class="list-group-item">
        <strong>${v.modelo}</strong> (${v.id}) - ${v.placa} 
        <button class="btn btn-sm btn-info float-end ms-2" onclick='editar("veiculo", ${JSON.stringify(v)})'>Editar</button>
        <button class="btn btn-sm btn-danger float-end" onclick='deletar("veiculo", ${v.id})'>Remover</button>
      </li>
    `).join('');
  } catch (err) {
    showError(err);
  }
}

// ---------------- ALUGUEIS ------------------
formAluguel.onsubmit = async e => {
  e.preventDefault();
  try {
    const res = await fetch(`${API}/alugueis/alugar?clienteId=${clienteId.value}&veiculoId=${veiculoId.value}&dataFim=${dataFim.value}`, {
      method: 'POST'
    });
    if (!res.ok) {
      const errorMsg = await res.text();
      throw new Error(errorMsg);
    }
    e.target.reset();
    listarAlugueis();
    listarVeiculos();
  } catch (err) {
    showError(err);
  }
};

async function listarAlugueis() {
  try {
    const res = await fetch(`${API}/alugueis`);
    if (!res.ok) throw new Error(await res.text());
    const lista = await res.json();
    listaAlugueis.innerHTML = lista.map(a => `
      <li class="list-group-item">
        <strong>#${a.id}</strong> - ${a.cliente.nome} alugou ${a.veiculo.modelo} até ${a.dataFim} 
        <button class="btn btn-sm btn-warning float-end ms-2" onclick='finalizarAluguel(${a.id})'>Finalizar</button>
        <button class="btn btn-sm btn-danger float-end" onclick='deletar("aluguel", ${a.id})'>Remover</button>
      </li>
    `).join('');
  } catch (err) {
    showError(err);
  }
}

async function finalizarAluguel(id) {
  try {
    const res = await fetch(`${API}/alugueis/finalizar/${id}`, { method: 'POST' });
    if (!res.ok) {
      const errorMsg = await res.text();
      throw new Error(errorMsg);
    }
    listarAlugueis();
    listarVeiculos();
  } catch (err) {
    showError(err);
  }
}

// ---------------- EDIÇÃO ------------------
async function salvarEdicao() {
  const tipo = editTipo.value;
  const id = editId.value;
  const campos = [editCampo1.value, editCampo2.value, editCampo3.value, editCampo4.value];

  let data;
  if (tipo === "cliente") {
    data = { nome: campos[0], cpf: campos[1], telefone: campos[2], email: campos[3] };
  } else if (tipo === "veiculo") {
    data = { placa: campos[0], marca: campos[1], modelo: campos[2], ano: campos[3] };
  }

  try {
    const res = await fetch(`${API}/${tipo === "cliente" ? "clientes" : "veiculos"}/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    if (!res.ok) {
      const errorMsg = await res.text();
      throw new Error(errorMsg);
    }
    bootstrap.Modal.getInstance(document.getElementById('editModal')).hide();
    listarClientes();
    listarVeiculos();
  } catch (err) {
    showError(err);
  }
}

// ---------------- DELETAR ------------------
async function deletar(tipo, id) {
  try {
    const res = await fetch(`${API}/${tipo === "cliente" ? "clientes" : tipo === "veiculo" ? "veiculos" : "alugueis"}/${id}`, {
      method: 'DELETE'
    });
    if (!res.ok) {
      const errorMsg = await res.text();
      throw new Error(errorMsg);
    }
    listarClientes();
    listarVeiculos();
    listarAlugueis();
  } catch (err) {
    showError(err);
  }
}

// ---------------- INICIALIZAÇÃO ------------------
listarClientes();
listarVeiculos();
listarAlugueis();
