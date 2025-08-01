package model;

public class Produto extends ProdutoBase {
    public Produto(int codigo, String descricao, double custo, double precoVenda, int codigoFornecedor, int estoque) {
        super(codigo, descricao, custo, precoVenda, codigoFornecedor, estoque);
    }
}
