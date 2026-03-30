# Fast Food Orders App

Sistema de gerenciamento de pedidos para fast food desenvolvido em Java, com foco na aplicação de conceitos de Programação Orientada a Objetos. Projeto acadêmico.

---

## Funcionalidades

- Cliente realiza pedido via terminal, seleciona itens do cardápio e recebe uma senha
- Cozinha visualiza a fila de pedidos com itens detalhados e marca como prontos
- Caixa visualiza pedidos prontos, registra pagamento e conclui a entrega
- Pedidos concluídos são gravados em `historico_pedidos.txt`

---

## Estrutura do Projeto

```
src/
├── Cargo.java                      # Enum: CAIXA, COZINHA
├── StatusPedido.java               # Enum: RECEBIDO, PREPARANDO, PRONTO, CONCLUIDO
├── MetodoPagamento.java            # Enum: PENDENTE, DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, PIX
├── Produto.java                    # Entidade: item do cardápio
├── ItemPedido.java                 # Entidade: produto + quantidade dentro de um pedido
├── Funcionario.java                # Entidade: funcionário com cargo
├── Pedido.java                     # Entidade principal: agrega itens, status e funcionários
├── GerenciadorPedidos.java         # Classe abstrata: base para os gerenciadores
├── GerenciadorPedidosCozinha.java  # Gerencia fila da cozinha
├── GerenciadorPedidosCaixa.java    # Gerencia fila do caixa e pagamentos
├── PersistenciaDados.java          # Grava histórico em arquivo .txt
└── FastFoodApp.java                # Classe principal — ponto de entrada e menus
```

---

## Como Executar

**Pré-requisito:** JDK instalado.

```bash
# Entrar na pasta src
cd src

# Compilar todos os arquivos
javac *.java

# Executar
java FastFoodApp
```

---

## Fluxo da Aplicação

```
Tela Inicial
├── [1] Fazer Pedido (Cliente)
│       → informa nome
│       → seleciona itens do cardápio
│       → recebe senha impressa
│       → pedido vai para a fila da cozinha
│
├── [2] Área de Funcionários
│       → login por usuário + senha
│       → COZINHA: vê fila com itens, marca pedido como PRONTO
│       → CAIXA:   vê pedidos prontos, registra pagamento, conclui entrega
│
└── [0] Sair
```

Ciclo de vida do pedido:
`RECEBIDO` → `PREPARANDO` → `PRONTO` → `CONCLUIDO`

---

## Credenciais de Teste

| Cargo   | Usuário    | Senha    |
|---------|------------|----------|
| Cozinha | carlos.c   | senha456 |
| Caixa   | gabriel.c  | senha123 |
