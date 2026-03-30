import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FastFoodApp {

    private static final Map<String, Funcionario> bancoFuncionarios = new HashMap<>();
    private static final List<Produto> cardapio = new ArrayList<>();

    private static GerenciadorPedidosCaixa   gerCaixa;
    private static GerenciadorPedidosCozinha gerCozinha;
    private static Scanner sc;
    private static int proximoIdPedido = 1001;

    public static void main(String[] args) {

        sc = new Scanner(System.in);
        new PersistenciaDados().inicializarArquivos();
        inicializarDados();

        gerCaixa   = new GerenciadorPedidosCaixa();
        gerCozinha = new GerenciadorPedidosCozinha(gerCaixa);

        telaInicial();

        sc.close();
        System.out.println("\n[i] Até logo!");
    }

    private static void telaInicial() {
        boolean rodando = true;
        while (rodando) {
            linha('=', 40);
            System.out.println(" — SISTEMA FAST FOOD — ");
            linha('=', 40);
            System.out.println("[1] Fazer Pedido (Cliente)");
            System.out.println("[2] Área de Funcionários");
            System.out.println("[0] Sair");
            linha('-', 40);
            System.out.print("[i] Opção: ");

            switch (lerInt()) {
                case 1: fluxoCliente();       break;
                case 2: fluxoFuncionario();   break;
                case 0: rodando = false;      break;
                default: System.out.println("[x] Opção inválida.");
            }
        }
    }

    private static void fluxoCliente() {
        linha('-', 40);
        System.out.print("[i] Seu nome: ");
        String nome = sc.nextLine().trim();
        if (nome.isEmpty()) nome = "Cliente";

        Pedido pedido = new Pedido(proximoIdPedido++, nome);
        int itemId = 1;

        boolean adicionando = true;
        while (adicionando) {
            exibirCardapio();
            System.out.println("[0] Finalizar pedido");
            System.out.print("[i] Produto: ");
            int escolha = lerInt();

            if (escolha == 0) {
                adicionando = false;
            } else if (escolha >= 1 && escolha <= cardapio.size()) {
                Produto p = cardapio.get(escolha - 1);
                System.out.print("[i] Quantidade: ");
                int qtd = lerInt();
                if (qtd > 0) {
                    pedido.adicionarItem(new ItemPedido(itemId++, qtd, p));
                    System.out.println("[i] Adicionado: " + p.getNome() +
                        " x" + qtd);
                }
            } else {
                System.out.println("[x] Opção inválida.");
            }
        }

        if (pedido.getItens().isEmpty()) {
            System.out.println("[x] Pedido cancelado (nenhum item).");
            return;
        }

        pedido.imprimirSenha();
        gerCozinha.adicionarPedido(pedido);
        System.out.println("[i] Pedido enviado para a cozinha! Aguarde sua senha.");
        pausar();
    }

    private static void fluxoFuncionario() {
        linha('-', 40);
        System.out.println(" — ÁREA DE FUNCIONÁRIOS — Login");
        linha('-', 40);

        Funcionario func = realizarLogin();
        if (func == null) {
            pausar();
            return;
        }

        System.out.println("[i] Bem-vindo(a), " + func.getNome() + " (" + func.getCargo() + ")");

        switch (func.getCargo()) {
            case COZINHA: menuCozinha(func); break;
            case CAIXA:   menuCaixa(func);   break;
        }
    }

    private static void menuCozinha(Funcionario func) {
        boolean loop = true;
        while (loop) {
            linha('-', 40);
            System.out.println(" — COZINHA — " + func.getNome());
            linha('-', 40);
            gerCozinha.exibirPedidos();
            System.out.println("[1] Marcar próximo pedido como PRONTO");
            System.out.println("[0] Sair / Voltar ao início");
            System.out.print("[i] Opção: ");

            switch (lerInt()) {
                case 1:
                    Pedido p = gerCozinha.getPedidosRecebidos().peek();
                    if (p == null) {
                        System.out.println("[x] Nenhum pedido na fila.");
                    } else {
                        gerCozinha.marcarPedidoComoPronto(p, func);
                    }
                    break;
                case 0:
                    loop = false;
                    break;
                default:
                    System.out.println("[x] Opção inválida.");
            }
        }
    }

    private static void menuCaixa(Funcionario func) {
        boolean loop = true;
        while (loop) {
            linha('-', 40);
            System.out.println(" — CAIXA — " + func.getNome());
            linha('-', 40);
            gerCaixa.exibirPedidos();
            System.out.println("[1] Concluir próximo pedido (entregar ao cliente)");
            System.out.println("[0] Sair / Voltar ao início");
            System.out.print("[i] Opção: ");

            switch (lerInt()) {
                case 1:
                    Pedido p = gerCaixa.getPedidosProntos().peek();
                    if (p == null) {
                        System.out.println("[x] Nenhum pedido pronto para entrega.");
                    } else {
                        gerCaixa.marcarPedidoConcluido(p, func, sc);
                    }
                    break;
                case 0:
                    loop = false;
                    break;
                default:
                    System.out.println("[x] Opção inválida.");
            }
        }
    }

    private static Funcionario realizarLogin() {
        System.out.print("[i] Usuário: ");
        String usuario = sc.nextLine().trim();
        System.out.print("[i] Senha  : ");
        String senha = sc.nextLine().trim();

        for (Funcionario f : bancoFuncionarios.values()) {
            if (f.getUsuario().equals(usuario) &&
                f.getSenha().equals(senha)) {
                return f;
            }
        }
        System.out.println("[x] Usuário ou senha incorretos.");
        return null;
    }

    private static void exibirCardapio() {
        linha('-', 40);
        System.out.println("[i] CARDÁPIO");
        linha('-', 40);
        for (int i = 0; i < cardapio.size(); i++) {
            Produto p = cardapio.get(i);
            System.out.printf("[i] [%d] %-22s R$ %.2f%n",
                i + 1, p.getNome(), p.getPrecoProduto());
        }
    }

    private static void inicializarDados() {
        cardapio.add(new Produto(1, "Burgão Simples",  "Pão, carne, queijo",    18.00));
        cardapio.add(new Produto(2, "Burgão Duplo",    "Pão, 2 carnes, queijo", 25.50));
        cardapio.add(new Produto(3, "Batata Frita",    "Porção média",          10.00));
        cardapio.add(new Produto(4, "Refrigerante",    "Lata 350ml",             6.00));
        cardapio.add(new Produto(5, "Milkshake",       "Ovomaltine",             5.00));

        Funcionario f1 = new Funcionario(1, "Gabriel Caixa", "gabriel.c", "senha123", Cargo.CAIXA);
        Funcionario f2 = new Funcionario(2, "Carlos Chef", "carlos.c", "senha456", Cargo.COZINHA);

        bancoFuncionarios.put(f1.getUsuario(), f1);
        bancoFuncionarios.put(f2.getUsuario(), f2);
    }

    private static int lerInt() {
        try {
            int v = Integer.parseInt(sc.nextLine().trim());
            return v;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void linha(char c, int n) {
        System.out.println("  " + String.valueOf(c).repeat(n));
    }

    private static void pausar() {
        System.out.print("\n[ENTER para voltar ao início]");
        sc.nextLine();
    }
}