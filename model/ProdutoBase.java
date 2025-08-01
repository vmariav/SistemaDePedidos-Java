package model;

public abstract class ProdutoBase {
    protected int codigo;
    protected String descricao;
    protected double custo;
    protected double precoVenda;
    protected int codigoFornecedor;
    protected int estoque; 

    public ProdutoBase(int codigo, String descricao, double custo, double precoVenda, int codigoFornecedor, int estoque) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.custo = custo;
        this.precoVenda = precoVenda;
        this.codigoFornecedor = codigoFornecedor;
        this.estoque = estoque;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getCusto() {
        return custo;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public int getCodigoFornecedor() {
        return codigoFornecedor;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public void setCodigoFornecedor(int codigoFornecedor) {
        this.codigoFornecedor = codigoFornecedor;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    @Override
    public String toString() {
        return "Código: " + codigo +
               " | Descrição: " + descricao +
               " | Custo: R$" + custo +
               " | Preço Venda: R$" + precoVenda +
               " | Fornecedor: " + codigoFornecedor +
               " | Estoque: " + estoque;
    }
}
