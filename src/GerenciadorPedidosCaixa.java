import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class GerenciadorPedidosCaixa extends GerenciadorPedidos {
    private Queue<Pedido> pedidosProntos;
    private PersistenciaDados persistencia;

    public GerenciadorPedidosCaixa() {
        this.pedidosProntos = new LinkedList<>();
        this.persistencia = new PersistenciaDados();
    }

    public void adicionarPedidoPronto(Pedido pedido) {
        super.adicionarPedido(pedido, pedidosProntos);
    }

    @Override
    public void exibirPedidos() {
        System.out.println("--- Fila de Entrega do Caixa (Prontos) ---");
        for (Pedido p : pedidosProntos) {
            System.out.println("Senha: " + p.getSenha() + " | Cliente: " + p.getNomeCliente() + " | Pago: " + p.isFoiPago());
        }
        System.out.println("------------------------------------------");
    }

    public void registrarPagamento(Pedido pedido, Funcionario caixa, MetodoPagamento metodo) {
        if (caixa.getCargo() != Cargo.CAIXA && caixa.getCargo() != Cargo.GERENTE) {
            System.out.println("Acesso Negado: Funcionário não autorizado a receber pagamentos.");
            return;
        }
        pedido.setFuncionarioCaixa(caixa);
        pedido.setMetodoPagamento(metodo);
        if (!pedido.isFoiPago()) {
            pedido.alternarEstadoPagamento();
        }
        System.out.println("Pagamento de R$ " + pedido.getPrecoTotal() + " registrado via " + metodo);
    }

    public void marcarPedidoConcluido(Pedido pedido, Funcionario caixa, Scanner sc) {
        if (!pedido.isFoiPago()) {
            System.out.println("Pedido ainda não foi pago. Solicitando pagamento agora...");
            System.out.println("Escolha o método (1-Dinheiro, 2-Cartao, 3-Pix): ");
            int op = Integer.parseInt(sc.nextLine().trim());
            MetodoPagamento mp = (op == 1) ? MetodoPagamento.DINHEIRO : (op == 2) ? MetodoPagamento.CARTAO_DEBITO : MetodoPagamento.PIX;
            registrarPagamento(pedido, caixa, mp);
        }

        pedido.alternarStatus(StatusPedido.CONCLUIDO);
        super.removerPedido(pedido, pedidosProntos);

        persistencia.salvarHistoricoPedido(pedido);
        System.out.println("Pedido " + pedido.getSenha() + " CONCLUÍDO e arquivado com sucesso!");
    }

    public Queue<Pedido> getPedidosProntos() {
        return pedidosProntos;
    }
}
