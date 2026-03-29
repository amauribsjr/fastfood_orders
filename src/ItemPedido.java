public class ItemPedido {
    private int id;
    private int quantidade;
    private Produto produto;

    public ItemPedido(int id, int quantidade, Produto produto) {
        this.id = id;
        this.quantidade = quantidade;
        this.produto = produto;
    }

    public int getQuantidade() { return quantidade; }
    public Produto getProduto() { return produto; }
    
    public double calcularSubtotal() {
        return quantidade * produto.getPrecoProduto();
    }
}