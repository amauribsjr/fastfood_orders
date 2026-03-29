public class Produto {
    private int id;
    private String nome;
    private String descricao;
    private double precoProduto;

    public Produto(int id, String nome, String descricao, double precoProduto) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.precoProduto = precoProduto;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public double getPrecoProduto() { return precoProduto; }
    
    @Override
    public String toString() {
        return nome + " (R$ " + precoProduto + ")";
    }
}