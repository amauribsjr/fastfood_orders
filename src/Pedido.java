import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pedido {
    private int id;
    private int senha;
    private String nomeCliente;
    private List<ItemPedido> itens;
    private StatusPedido status;
    private MetodoPagamento metodoPagamento;
    private boolean foiPago;
    private double precoTotal;
    private Funcionario funcionarioCaixa;
    private Funcionario funcionarioCozinheiro;

    public Pedido(int id, String nomeCliente) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.itens = new ArrayList<>();
        this.status = StatusPedido.RECEBIDO;
        this.metodoPagamento = MetodoPagamento.PENDENTE;
        this.foiPago = false;
        this.precoTotal = 0.0;
        this.senha = gerarSenha();
    }

    public void adicionarItem(ItemPedido item) {
        this.itens.add(item);
        this.precoTotal += item.calcularSubtotal();
    }

    public void alternarStatus(StatusPedido novoStatus) {
        this.status = novoStatus;
    }

    public void alternarEstadoPagamento() {
        this.foiPago = !this.foiPago;
    }

    public int gerarSenha() {
        return new Random().nextInt(900) + 100;
    }

    public void imprimirSenha() {
        System.out.println("================================");
        System.out.println("PEDIDO #" + this.id);
        System.out.println("CLIENTE: " + this.nomeCliente);
        System.out.println("SENHA: " + this.senha);
        System.out.println("TOTAL: R$ " + this.precoTotal);
        System.out.println("STATUS: " + this.status);
        System.out.println("================================\n");
    }

    public int getId() { return id; }
    public int getSenha() { return senha; }
    public StatusPedido getStatus() { return status; }
    public boolean isFoiPago() { return foiPago; }
    public double getPrecoTotal() { return precoTotal; }
    public Funcionario getFuncionarioCaixa() { return funcionarioCaixa; }
    public void setFuncionarioCaixa(Funcionario f) { this.funcionarioCaixa = f; }
    public Funcionario getFuncionarioCozinheiro() { return funcionarioCozinheiro; }
    public void setFuncionarioCozinheiro(Funcionario f) { this.funcionarioCozinheiro = f; }
    public MetodoPagamento getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(MetodoPagamento mp) { this.metodoPagamento = mp; }
    public List<ItemPedido> getItens() { return itens; }
    public String getNomeCliente() { return nomeCliente; }
}