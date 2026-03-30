import java.util.LinkedList;
import java.util.Queue;

public class GerenciadorPedidosCozinha extends GerenciadorPedidos {
    private Queue<Pedido> pedidosRecebidos;
    private GerenciadorPedidosCaixa caixaRef;

    public GerenciadorPedidosCozinha(GerenciadorPedidosCaixa caixa) {
        this.pedidosRecebidos = new LinkedList<>();
        this.caixaRef = caixa;
    }

    public void adicionarPedido(Pedido pedido) {
        super.adicionarPedido(pedido, pedidosRecebidos);
        pedido.alternarStatus(StatusPedido.PREPARANDO);
    }

    @Override
    public void exibirPedidos() {
        System.out.println("--- Fila da Cozinha (Preparando) ---");
        for (Pedido p : pedidosRecebidos) {
            System.out.println("Senha: " + p.getSenha() + " | Cliente: " + p.getNomeCliente());
        }
        System.out.println("------------------------------------");
    }

    public void marcarPedidoComoPronto(Pedido pedido, Funcionario cozinheiro) {
        if (cozinheiro.getCargo() != Cargo.COZINHA) {
            System.out.println("Acesso Negado: Apenas cozinha pode preparar lanches.");
            return;
        }

        pedido.setFuncionarioCozinheiro(cozinheiro);
        pedido.alternarStatus(StatusPedido.PRONTO);
        super.removerPedido(pedido, pedidosRecebidos);

        caixaRef.adicionarPedidoPronto(pedido);
        System.out.println("Pedido " + pedido.getSenha() + " pronto! Enviado para entrega no caixa.");
    }

    public Queue<Pedido> getPedidosRecebidos() {
        return pedidosRecebidos;
    }
}
