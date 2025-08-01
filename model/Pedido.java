package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Pedido {
    private int numero;
    private Pessoa cliente;
    private Endereco enderecoEntrega;
    private Map<ProdutoBase, Integer> produtos = new LinkedHashMap<>();

    public Pedido(int numero, Pessoa cliente, Endereco enderecoEntrega) {
        this.numero = numero;
        this.cliente = cliente;
        this.enderecoEntrega = enderecoEntrega;
    }

    public void adicionarProduto(ProdutoBase produto, int quantidade) {
        if (quantidade <= 0) return;
        produtos.put(produto, produtos.getOrDefault(produto, 0) + quantidade);
    }

    public double calcularTotal() {
        return produtos.entrySet()
                .stream()
                .mapToDouble(entry -> entry.getKey().getPrecoVenda() * entry.getValue())
                .sum();
    }

    public int getNumero() {
        return numero;
    }

    public Pessoa getCliente() {
        return cliente;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public Map<ProdutoBase, Integer> getProdutos() {
        return produtos;
    }

    public void removerProdutoPorCodigo(int codigo) {
        produtos.entrySet().removeIf(entry -> entry.getKey().getCodigo() == codigo);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido Nº: ").append(numero).append("\n");
        sb.append("Cliente: ").append(cliente.getNome()).append(" (").append(cliente.getCodigo()).append(")\n");
        sb.append("Entrega: ").append(enderecoEntrega).append("\n");
        sb.append("Produtos:\n");
        for (Map.Entry<ProdutoBase, Integer> entry : produtos.entrySet()) {
            ProdutoBase p = entry.getKey();
            int qtd = entry.getValue();
            sb.append("- ").append(p.getDescricao())
              .append(" | Quantidade: ").append(qtd)
              .append(" | Unitário: R$").append(p.getPrecoVenda())
              .append(" | Subtotal: R$").append(p.getPrecoVenda() * qtd)
              .append("\n");
        }
        sb.append("Total: R$").append(calcularTotal());
        return sb.toString();
    }
}
