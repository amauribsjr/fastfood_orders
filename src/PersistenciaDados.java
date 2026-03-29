import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PersistenciaDados {

    public void salvarHistoricoPedido(Pedido pedido) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("historico_pedidos.txt", true))) {

            //format
            String linha = String.format(
                "ID: %d | Senha: %d | Cliente: %s | Total: R$ %.2f | " +
                "Cozinheiro: %s | Caixa: %s | Pagamento: %s%n",
                pedido.getId(), pedido.getSenha(), pedido.getNomeCliente(),
                pedido.getPrecoTotal(),
                pedido.getFuncionarioCozinheiro() != null
                    ? pedido.getFuncionarioCozinheiro().getNome() : "N/A",
                pedido.getFuncionarioCaixa() != null
                    ? pedido.getFuncionarioCaixa().getNome() : "N/A",
                pedido.getMetodoPagamento().name());
            bw.write(linha);

        } catch (IOException e) {
            System.out.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }

    public void inicializarArquivos() {
        try {
            new File("historico_pedidos.txt").createNewFile();
        } catch (IOException e) {
            System.out.println("Erro ao criar arquivos: " + e.getMessage());
        }
    }
}
