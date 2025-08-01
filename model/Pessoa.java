package model;

import java.util.ArrayList;
import java.util.List;

public class Pessoa {
    private int codigo;
    private String nome;
    private TipoPessoa tipo;
    private List<Endereco> enderecos = new ArrayList<>();

    public Pessoa(int codigo, String nome, TipoPessoa tipo) {
    this.codigo = codigo;
    this.nome = nome;
    this.tipo = tipo;
    }

    public TipoPessoa getTipo() {
    return tipo;
}

public void setTipo(TipoPessoa tipo) {
    this.tipo = tipo;
}
    public void adicionarEndereco(Endereco endereco) {
        enderecos.add(endereco);
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

   

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    @Override
    public String toString() {
        return "Código: " + codigo + " | Nome: " + nome + " | Tipo: " + tipo +
               " | Endereços: " + enderecos.size();
    }
}
