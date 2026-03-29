import java.util.Collection;

public abstract class GerenciadorPedidos {
    
    public abstract void exibirPedidos();

    public void adicionarPedido(Pedido pedido, Collection<Pedido> colecao) {
        colecao.add(pedido);
    }

    public void removerPedido(Pedido pedido, Collection<Pedido> colecao) {
        colecao.remove(pedido);
    }
}