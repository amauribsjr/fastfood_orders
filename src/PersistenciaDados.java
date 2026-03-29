import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class PersistenciaDados {

    public void salvarHistoricoPedido(Pedido pedido) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("historico_pedidos.txt", true))) {
            String linha = String.format("ID: %d | Senha: %d | Cliente: %s | Total: R$ %.2f | Cozinheiro: %s | Caixa: %s | Pagamento: %s\n",
                    pedido.getId(),
                    pedido.getSenha(),
                    pedido.getNomeCliente(),
                    pedido.getPrecoTotal(),
                    (pedido.getFuncionarioCozinheiro() != null) ? pedido.getFuncionarioCozinheiro().getNome() : "N/A",
                    (pedido.getFuncionarioCaixa() != null) ? pedido.getFuncionarioCaixa().getNome() : "N/A",
                    pedido.getMetodoPagamento().name());
            bw.write(linha);
        } catch (IOException e) {
            System.out.println("Erro ao salvar histórico do pedido: " + e.getMessage());
        }
    }
    
    public void inicializarArquivos() {
        try {
            new File("historico_pedidos.txt").createNewFile();
            new File("funcionarios.txt").createNewFile();
        } catch (IOException e) {
            System.out.println("Erro ao criar arquivos: " + e.getMessage());
        }
    }
}