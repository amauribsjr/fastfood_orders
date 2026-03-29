import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FastFoodApp {
    private static Map<String, Funcionario> bancoFuncionarios = new HashMap<>();

    public static void main(String[] args) {
        new PersistenciaDados().inicializarArquivos();
        inicializarDadosFalsos();

        Scanner scanner = new Scanner(System.in);
        GerenciadorPedidosCaixa caixa = new GerenciadorPedidosCaixa();
        GerenciadorPedidosCozinha cozinha = new GerenciadorPedidosCozinha(caixa);
        
        System.out.println("=== SISTEMA FAST FOOD INICIADO ===");

        Produto p1 = new Produto(1, "Burgão Duplo", "Pão, 2 carnes, queijo, alface e tomate", 25.50);
        Produto p2 = new Produto(2, "Batata Frita", "Porção média", 10.00);

        Pedido novoPedido = new Pedido(1001, "Edão da Massa");
        novoPedido.adicionarItem(new ItemPedido(1, 1, p1));
        novoPedido.adicionarItem(new ItemPedido(2, 2, p2));
        
        System.out.println("\n[CAIXA] Novo pedido recebido!");
        novoPedido.imprimirSenha();
        
        cozinha.adicionarPedido(novoPedido);
        cozinha.exibirPedidos();

        System.out.println("\n[COZINHA] Inserir senha do funcionário para baixar pedido:");
        Funcionario funcCozinha = realizarAutenticacao(scanner); 
        
        if (funcCozinha != null) {
            Pedido pedidoPreparo = cozinha.getPedidosRecebidos().peek();
            if (pedidoPreparo != null) {
                cozinha.marcarPedidoComoPronto(pedidoPreparo, funcCozinha);
            }
        }

        caixa.exibirPedidos();

        System.out.println("\n[CAIXA] Cliente veio retirar. Inserir senha do operador de caixa para concluir:");
        Funcionario funcCaixa = realizarAutenticacao(scanner);
        
        if (funcCaixa != null) {
            Pedido pedidoFinal = caixa.getPedidosProntos().peek();
            if (pedidoFinal != null) {
                caixa.marcarPedidoConcluido(pedidoFinal, funcCaixa);
            }
        }
        
        System.out.println("\nProcesso finalizado. Verifique o arquivo historico_pedidos.txt!");
        scanner.close();
    }

    private static Funcionario realizarAutenticacao(Scanner scanner) {
        System.out.print("> Senha do Funcionario: ");
        String senhaDigitada = scanner.next();
        
        Funcionario f = bancoFuncionarios.get(senhaDigitada);
        if (f == null) {
            System.out.println("Senha inválida ou funcionário não encontrado!");
        } else {
            System.out.println("Autenticado como: " + f.getNome() + " (" + f.getCargo() + ")");
        }
        return f;
    }

    private static void inicializarDadosFalsos() {
        Funcionario f1 = new Funcionario(1, "Gabriel Caixa", "gabriel.c", "senha123", Cargo.CAIXA);
        Funcionario f2 = new Funcionario(2, "Carlos Chef", "carlos.c", "senha456", Cargo.COZINHA);
        
        bancoFuncionarios.put(f1.getSenha(), f1);
        bancoFuncionarios.put(f2.getSenha(), f2);
    }
}