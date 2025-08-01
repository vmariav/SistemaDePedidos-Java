package service;

import model.*;
import java.util.*;
import java.io.*;

public class ProdutoService {
    private Map<Integer, ProdutoBase> produtos = new HashMap<>();
    private PessoaService pessoaService;

    public ProdutoService(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    public void cadastrarProduto(Scanner sc) {
        System.out.print("Código do Produto: ");
        int codigo = sc.nextInt();
        sc.nextLine();
        if (produtos.containsKey(codigo)) {
            System.out.println("Já existe um produto com esse código.");
            return;
        }

        System.out.print("Descrição: ");
        String descricao = sc.nextLine();

        System.out.print("Custo (R$): ");
        double custo = sc.nextDouble();

        System.out.print("Preço de Venda (R$): ");
        double precoVenda = sc.nextDouble();

        System.out.print("Quantidade em estoque: ");
        int estoque = sc.nextInt();
        sc.nextLine();

        System.out.print("Código do Fornecedor: ");
        int codigoFornecedor = sc.nextInt();
        sc.nextLine();

        Pessoa fornecedor = pessoaService.getPessoaPorCodigo(codigoFornecedor);
        if (fornecedor == null || (fornecedor.getTipo() != TipoPessoa.FORNECEDOR && fornecedor.getTipo() != TipoPessoa.AMBOS)) {
            System.out.println("Fornecedor inválido. Ele deve estar cadastrado como FORNECEDOR ou AMBOS.");
            return;
        }

        Produto produto = new Produto(codigo, descricao, custo, precoVenda, codigoFornecedor, estoque);
        produtos.put(codigo, produto);

        LogService.registrar("Produto cadastrado: " + produto);
        salvarTodosProdutosNoArquivo();

        System.out.println("Produto cadastrado com sucesso.");
    }

    public void buscarProdutoPorCodigo(Scanner sc) {
        System.out.print("Digite o código do produto: ");
        int codigo = sc.nextInt();
        sc.nextLine();

        ProdutoBase p = produtos.get(codigo);
        if (p != null) {
            System.out.println(p);
            LogService.registrar("Produto consultado por código: " + codigo);
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public ProdutoBase getProdutoPorCodigo(int codigo) {
        return produtos.get(codigo);
    }

    public void carregarProdutos() {
        try (Scanner sc = new Scanner(new File("produtos.txt"))) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(";");
                if (partes.length >= 6) {
                    int codigo = Integer.parseInt(partes[0]);
                    String descricao = partes[1];
                    double custo = Double.parseDouble(partes[2]);
                    double precoVenda = Double.parseDouble(partes[3]);
                    int codigoFornecedor = Integer.parseInt(partes[4]);
                    int estoque = Integer.parseInt(partes[5]);

                    Produto produto = new Produto(codigo, descricao, custo, precoVenda, codigoFornecedor, estoque);
                    produtos.put(codigo, produto);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo produtos.txt não encontrado.");
        }
    }

    public void listarProdutos() {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            for (ProdutoBase p : produtos.values()) {
                System.out.println(p);
            }
        }
    }

    public void atualizarProduto(Scanner sc) {
        System.out.print("Digite o código do produto a atualizar: ");
        int codigo = sc.nextInt();
        sc.nextLine();

        ProdutoBase produto = produtos.get(codigo);
        if (produto instanceof Produto) {
            Produto p = (Produto) produto;

            System.out.println("Produto atual: " + p);

            System.out.print("Nova descrição (deixe em branco para manter): ");
            String novaDescricao = sc.nextLine();
            if (!novaDescricao.isEmpty()) {
                p.setDescricao(novaDescricao);
            }

            System.out.print("Novo custo (ou -1 para manter): ");
            double novoCusto = sc.nextDouble();
            if (novoCusto >= 0) {
                p.setCusto(novoCusto);
            }

            System.out.print("Novo preço de venda (ou -1 para manter): ");
            double novoPrecoVenda = sc.nextDouble();
            if (novoPrecoVenda >= 0) {
                p.setPrecoVenda(novoPrecoVenda);
            }

            System.out.print("Novo código do fornecedor (ou -1 para manter): ");
            int novoFornecedor = sc.nextInt();
            sc.nextLine();
            if (novoFornecedor >= 0) {
                Pessoa novoForn = pessoaService.getPessoaPorCodigo(novoFornecedor);
                if (novoForn == null || (novoForn.getTipo() != TipoPessoa.FORNECEDOR && novoForn.getTipo() != TipoPessoa.AMBOS)) {
                    System.out.println("Fornecedor inválido. Alteração não realizada.");
                } else {
                    p.setCodigoFornecedor(novoFornecedor);
                }
            }

            System.out.print("Novo estoque (ou -1 para manter): ");
            int novoEstoque = sc.nextInt();
            sc.nextLine();
            if (novoEstoque >= 0) {
                p.setEstoque(novoEstoque);
            }

            LogService.registrar("Produto atualizado: " + p);
            salvarTodosProdutosNoArquivo();

            System.out.println("Produto atualizado com sucesso.");
        } else {
            System.out.println("Produto não encontrado ou tipo desconhecido.");
        }
    }

    public void excluirProduto(Scanner sc) {
        System.out.print("Digite o código do produto a excluir: ");
        int codigo = sc.nextInt();
        sc.nextLine();

        ProdutoBase removido = produtos.remove(codigo);
        if (removido != null) {
            LogService.registrar("Produto excluído: " + removido);
            salvarTodosProdutosNoArquivo();
            System.out.println("Produto excluído com sucesso.");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    void salvarTodosProdutosNoArquivo() {
        try (FileWriter fw = new FileWriter("produtos.txt")) {
            for (ProdutoBase p : produtos.values()) {
                fw.write(p.getCodigo() + ";" + p.getDescricao() + ";" + p.getCusto() + ";" +
                         p.getPrecoVenda() + ";" + p.getCodigoFornecedor() + ";" + p.getEstoque() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }
}
