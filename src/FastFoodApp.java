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
        System.out.println("\n  Ate logo!\n");
    }

    private static void telaInicial() {
        boolean rodando = true;
        while (rodando) {
            System.out.println();
            System.out.println("  ╔══════════════════════════════════════╗");
            System.out.println("  ║         SISTEMA FAST FOOD            ║");
            System.out.println("  ╠══════════════════════════════════════╣");
            System.out.println("  ║  [1]  Fazer Pedido                   ║");
            System.out.println("  ║  [2]  Area de Funcionarios           ║");
            System.out.println("  ║  [0]  Sair                           ║");
            System.out.println("  ╚══════════════════════════════════════╝");
            System.out.print("  > ");

            switch (lerInt()) {
                case 1: fluxoCliente();     break;
                case 2: fluxoFuncionario(); break;
                case 0: rodando = false;    break;
                default: erro("Opcao invalida.");
            }
        }
    }

    private static void fluxoCliente() {
        System.out.println();
        System.out.println("  ┌─ NOVO PEDIDO ──────────────────────────┐");
        System.out.print("  │  Seu nome: ");
        String nome = sc.nextLine().trim();
        if (nome.isEmpty()) nome = "Cliente";
        System.out.println("  └────────────────────────────────────────┘");

        Pedido pedido = new Pedido(proximoIdPedido++, nome);
        int itemId = 1;

        boolean adicionando = true;
        while (adicionando) {
            exibirCardapio();
            System.out.print("  > Produto (0 para finalizar): ");
            int escolha = lerInt();

            if (escolha == 0) {
                adicionando = false;
            } else if (escolha >= 1 && escolha <= cardapio.size()) {
                Produto p = cardapio.get(escolha - 1);
                System.out.print("  > Quantidade: ");
                int qtd = lerInt();
                if (qtd > 0) {
                    pedido.adicionarItem(new ItemPedido(itemId++, qtd, p));
                    System.out.println("  + Adicionado: " + p.getNome() + " x" + qtd);
                }
            } else {
                erro("Opcao invalida.");
            }
        }

        if (pedido.getItens().isEmpty()) {
            erro("Pedido cancelado — nenhum item selecionado.");
            return;
        }

        pedido.imprimirSenha();
        gerCozinha.adicionarPedido(pedido);
        System.out.println("  Pedido enviado para a cozinha. Aguarde sua senha.");
        pausar();
    }

    private static void fluxoFuncionario() {
        System.out.println();
        System.out.println("  ┌─ LOGIN ─────────────────────────────────┐");
        System.out.print("  │  Usuario : ");
        String usuario = sc.nextLine().trim();
        System.out.print("  │  Senha   : ");
        String senha = sc.nextLine().trim();
        System.out.println("  └─────────────────────────────────────────┘");

        Funcionario func = null;
        for (Funcionario f : bancoFuncionarios.values()) {
            if (f.getUsuario().equals(usuario) && f.getSenha().equals(senha)) {
                func = f;
                break;
            }
        }

        if (func == null) {
            erro("Usuario ou senha incorretos.");
            pausar();
            return;
        }

        System.out.println("  Bem-vindo(a), " + func.getNome() + " [" + func.getCargo() + "]");

        switch (func.getCargo()) {
            case COZINHA: menuCozinha(func); break;
            case CAIXA:   menuCaixa(func);   break;
        }
    }

    private static void menuCozinha(Funcionario func) {
        boolean loop = true;
        while (loop) {
            System.out.println();
            System.out.println("  ╔══════════════════════════════════════╗");
            System.out.printf ("  ║  COZINHA  |  %-23s║%n", func.getNome());
            System.out.println("  ╠══════════════════════════════════════╣");
            gerCozinha.exibirPedidos();
            System.out.println("  ╠══════════════════════════════════════╣");
            System.out.println("  ║  [1]  Marcar proximo como PRONTO     ║");
            System.out.println("  ║  [0]  Voltar ao inicio               ║");
            System.out.println("  ╚══════════════════════════════════════╝");
            System.out.print("  > ");

            switch (lerInt()) {
                case 1:
                    Pedido p = gerCozinha.getPedidosRecebidos().peek();
                    if (p == null) erro("Nenhum pedido na fila.");
                    else gerCozinha.marcarPedidoComoPronto(p, func);
                    break;
                case 0:
                    loop = false;
                    break;
                default:
                    erro("Opcao invalida.");
            }
        }
    }

    private static void menuCaixa(Funcionario func) {
        boolean loop = true;
        while (loop) {
            System.out.println();
            System.out.println("  ╔══════════════════════════════════════╗");
            System.out.printf ("  ║  CAIXA    |  %-23s║%n", func.getNome());
            System.out.println("  ╠══════════════════════════════════════╣");
            gerCaixa.exibirPedidos();
            System.out.println("  ╠══════════════════════════════════════╣");
            System.out.println("  ║  [1]  Concluir proximo pedido        ║");
            System.out.println("  ║  [0]  Voltar ao inicio               ║");
            System.out.println("  ╚══════════════════════════════════════╝");
            System.out.print("  > ");

            switch (lerInt()) {
                case 1:
                    Pedido p = gerCaixa.getPedidosProntos().peek();
                    if (p == null) erro("Nenhum pedido pronto para entrega.");
                    else gerCaixa.marcarPedidoConcluido(p, func, sc);
                    break;
                case 0:
                    loop = false;
                    break;
                default:
                    erro("Opcao invalida.");
            }
        }
    }

    private static void exibirCardapio() {
        System.out.println();
        System.out.println("  ┌─ CARDAPIO ──────────────────────────────┐");
        for (int i = 0; i < cardapio.size(); i++) {
            Produto p = cardapio.get(i);
            System.out.printf("  │  [%d]  %-22s  R$ %5.2f  │%n",
                i + 1, p.getNome(), p.getPrecoProduto());
        }
        System.out.println("  └─────────────────────────────────────────┘");
    }

    private static void inicializarDados() {
        cardapio.add(new Produto(1, "Burgao Simples",  "Pao, carne, queijo",    18.00));
        cardapio.add(new Produto(2, "Burgao Duplo",    "Pao, 2 carnes, queijo", 25.50));
        cardapio.add(new Produto(3, "Batata Frita",    "Porcao media",          10.00));
        cardapio.add(new Produto(4, "Refrigerante",    "Lata 350ml",             6.00));
        cardapio.add(new Produto(5, "Milkshake",       "Ovomaltine",             5.00));

        Funcionario f1 = new Funcionario(1, "Gabriel Caixa", "gabriel.c", "senha123", Cargo.CAIXA);
        Funcionario f2 = new Funcionario(2, "Carlos Chef",   "carlos.c",  "senha456", Cargo.COZINHA);

        bancoFuncionarios.put(f1.getUsuario(), f1);
        bancoFuncionarios.put(f2.getUsuario(), f2);
    }

    private static int lerInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void erro(String msg) {
        System.out.println("  ! " + msg);
    }

    private static void pausar() {
        System.out.print("\n  [ENTER para voltar ao inicio] ");
        sc.nextLine();
    }
}
