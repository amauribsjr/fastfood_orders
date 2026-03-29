import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FastFoodApp {
    private static Map<String, Funcionario> bancoFuncionarios = new HashMap<>();
    private static List<Produto> cardapio = new ArrayList<>();
    
    private static GerenciadorPedidosCaixa caixa = new GerenciadorPedidosCaixa();
    private static GerenciadorPedidosCozinha cozinha = new GerenciadorPedidosCozinha(caixa);

    public static void main(String[] args) {
        new PersistenciaDados().inicializarArquivos();
        inicializarDadosFalsos();
        inicializarCardapio();

        Scanner scanner = new Scanner(System.in);
        boolean rodando = true;

        System.out.println("==================================");
        System.out.println("  BEM-VINDO AO SISTEMA FAST FOOD  ");
        System.out.println("==================================");

        while (rodando) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Novo Pedido (Cliente)");
            System.out.println("2. Acesso Restrito (Funcionário)");
            System.out.println("3. Sair do Sistema");
            System.out.print("Escolha uma opção: ");
            
            int opcao = lerInteiro(scanner);

            switch (opcao) {
                case 1:
                    menuCliente(scanner);
                    break;
                case 2:
                    menuFuncionario(scanner);
                    break;
                case 3:
                    rodando = false;
                    System.out.println("\nEncerrando o sistema. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
        
        scanner.close();
    }

    private static void menuCliente(Scanner scanner) {
        System.out.println("\n--- ÁREA DO CLIENTE ---");
        System.out.print("Digite seu nome para o pedido: ");
        String nomeCliente = scanner.nextLine();
        
        Pedido novoPedido = new Pedido(gerarIdPedido(), nomeCliente);
        boolean escolhendo = true;

        while (escolhendo) {
            System.out.println("\n--- CARDÁPIO ---");
            for (Produto p : cardapio) {
                System.out.println(p.getId() + ". " + p.getNome() + " - " + p.getDescricao() + " (R$ " + String.format("%.2f", p.getPrecoProduto()) + ")");
            }
            
            System.out.print("\nDigite o ID do produto que deseja (ou 0 para finalizar pedido): ");
            int idProduto = lerInteiro(scanner);

            if (idProduto == 0) {
                if (novoPedido.getItens().isEmpty()) {
                    System.out.println("Pedido cancelado. Nenhum item foi adicionado.");
                    return;
                }
                escolhendo = false;
            } else {
                Produto produtoEscolhido = buscarProdutoPorId(idProduto);
                if (produtoEscolhido != null) {
                    System.out.print("Quantidade: ");
                    int qtd = lerInteiro(scanner);
                    
                    if (qtd > 0) {
                        novoPedido.adicionarItem(new ItemPedido(novoPedido.getItens().size() + 1, qtd, produtoEscolhido));
                        System.out.println(">> " + qtd + "x " + produtoEscolhido.getNome() + " adicionado(s) ao carrinho!");
                    } else {
                        System.out.println("Quantidade inválida.");
                    }
                } else {
                    System.out.println("Produto não encontrado.");
                }
            }
        }

        System.out.println("\nResumo do Pedido enviado para a Cozinha:");
        novoPedido.imprimirSenha();
        cozinha.adicionarPedido(novoPedido);
    }

    private static void menuFuncionario(Scanner scanner) {
        System.out.println("\n--- ÁREA DO FUNCIONÁRIO ---");
        Funcionario func = realizarAutenticacao(scanner);
        
        if (func == null) return;

        if (func.getCargo() == Cargo.COZINHA || func.getCargo() == Cargo.GERENTE) {
            fluxoCozinha(scanner, func);
        } else if (func.getCargo() == Cargo.CAIXA || func.getCargo() == Cargo.GERENTE) {
            fluxoCaixa(scanner, func);
        }
    }

    private static void fluxoCozinha(Scanner scanner, Funcionario func) {
        System.out.println("\nPainel da Cozinha. Operador: " + func.getNome());
        cozinha.exibirPedidos();

        if (cozinha.getPedidosRecebidos().isEmpty()) {
            System.out.println("Não há pedidos pendentes na cozinha no momento.");
            return;
        }

        System.out.print("Deseja preparar e dar baixa no próximo pedido da fila? (1-Sim / 2-Não): ");
        int op = lerInteiro(scanner);
        if (op == 1) {
            Pedido pedidoPreparo = cozinha.getPedidosRecebidos().peek();
            if (pedidoPreparo != null) {
                cozinha.marcarPedidoComoPronto(pedidoPreparo, func);
            }
        }
    }

    private static void fluxoCaixa(Scanner scanner, Funcionario func) {
        System.out.println("\nPainel do Caixa. Operador: " + func.getNome());
        caixa.exibirPedidos();

        if (caixa.getPedidosProntos().isEmpty()) {
            System.out.println("Não há pedidos aguardando pagamento/entrega no momento.");
            return;
        }

        System.out.print("Deseja cobrar e entregar o próximo pedido da fila? (1-Sim / 2-Não): ");
        int op = lerInteiro(scanner);
        if (op == 1) {
            Pedido pedidoFinal = caixa.getPedidosProntos().peek();
            if (pedidoFinal != null) {
                caixa.marcarPedidoConcluido(pedidoFinal, func);
            }
        }
    }

    private static Funcionario realizarAutenticacao(Scanner scanner) {
        System.out.print("Digite sua senha de acesso (ex: senha123 para caixa, senha456 para cozinha): ");
        String senhaDigitada = scanner.nextLine();
        
        Funcionario f = bancoFuncionarios.get(senhaDigitada);
        if (f == null) {
            System.out.println("Acesso Negado: Senha inválida ou funcionário não encontrado!");
        } else {
            System.out.println("Autenticado com sucesso como: " + f.getNome() + " [" + f.getCargo() + "]");
        }
        return f;
    }

    private static void inicializarDadosFalsos() {
        Funcionario f1 = new Funcionario(1, "Gabriel Caixa", "gabriel.c", "senha123", Cargo.CAIXA);
        Funcionario f2 = new Funcionario(2, "Carlos Chef", "carlos.c", "senha456", Cargo.COZINHA);
        
        bancoFuncionarios.put(f1.getSenha(), f1);
        bancoFuncionarios.put(f2.getSenha(), f2);
    }

    private static void inicializarCardapio() {
        cardapio.add(new Produto(1, "Burgão", "Pão, carne, queijo, alface e tomate", 18.00));
        cardapio.add(new Produto(2, "Burgão Duplo", "Pão, 2 carnes, bacon, queijo, alface e tomate", 25.50));
        cardapio.add(new Produto(3, "Batata Frita", "Porção média", 10.00));
        cardapio.add(new Produto(4, "Refrigerante Cola", "Lata 350ml", 6.00));
        cardapio.add(new Produto(5, "Milkshake de Morango", "Copo 500ml", 15.00));
    }

    private static Produto buscarProdutoPorId(int id) {
        for (Produto p : cardapio) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    private static int idCounter = 1000;
    private static int gerarIdPedido() {
        return ++idCounter;
    }

    private static int lerInteiro(Scanner scanner) {
        try {
            int valor = scanner.nextInt();
            scanner.nextLine();
            return valor;
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
}