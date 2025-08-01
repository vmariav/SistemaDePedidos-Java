package service;

import java.io.FileWriter;
import java.io.IOException;
import model.*;

public class ArquivoService {

    public static void salvarPessoa(String linha) {
        salvar("pessoas.txt", linha);
    }

    public static void salvarEndereco(String linha) {
        salvar("enderecos.txt", linha);
    }

    public static void salvarProduto(String linha) {
        salvar("produtos.txt", linha);
    }

    public static void salvarPedido(Pedido pedido) {
        StringBuilder sb = new StringBuilder();
        sb.append(pedido.getNumero()).append(";");
        sb.append(pedido.getCliente().getCodigo()).append(";");
        sb.append(pedido.getEnderecoEntrega().getCep()).append(";");

        for (var entry : pedido.getProdutos().entrySet()) {
            ProdutoBase produto = entry.getKey();
            int quantidade = entry.getValue();
            sb.append(produto.getCodigo()).append(":").append(quantidade).append(",");
        }

        if (pedido.getProdutos().size() > 0) {
            sb.setLength(sb.length() - 1); 
        }

        sb.append(";").append(pedido.calcularTotal());

        salvar("pedidos.txt", sb.toString());
    }

    private static void salvar(String nomeArquivo, String linha) {
        try (FileWriter fw = new FileWriter(nomeArquivo, true)) {
            fw.write(linha + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao salvar no arquivo " + nomeArquivo + ": " + e.getMessage());
        }
    }
}
