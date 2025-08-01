package service;

import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PedidoService {
    private Map<Integer, Pedido> pedidos = new HashMap<>();
    private PessoaService pessoaService;
    private ProdutoService produtoService;

    public PedidoService(PessoaService pessoaService, ProdutoService produtoService) {
        this.pessoaService = pessoaService;
        this.produtoService = produtoService;
    }

    public void cadastrarPedido(Scanner sc) {
        System.out.print("Número do pedido: ");
        int numero = sc.nextInt();
        sc.nextLine();

        if (pedidos.containsKey(numero)) {
            System.out.println("Já existe um pedido com esse número.");
            return;
        }

        System.out.print("Código do cliente: ");
        int codigoCliente = sc.nextInt();
        sc.nextLine();

        Pessoa cliente = pessoaService.getPessoaPorCodigo(codigoCliente);
        if (cliente == null || (cliente.getTipo() != TipoPessoa.CLIENTE && cliente.getTipo() != TipoPessoa.AMBOS)) {
            System.out.println("Cliente inválido. Deve ser do tipo CLIENTE ou AMBOS.");
            return;
        }
        if (cliente.getEnderecos().isEmpty()) {
            System.out.println("Cliente não possui endereços.");
            return;
        }

        System.out.println("Endereços do cliente:");
        for (int i = 0; i < cliente.getEnderecos().size(); i++) {
            System.out.println(i + ": " + cliente.getEnderecos().get(i));
        }

        System.out.print("Escolha o número do endereço para entrega: ");
        int indiceEndereco = sc.nextInt();
        sc.nextLine();

        if (indiceEndereco < 0 || indiceEndereco >= cliente.getEnderecos().size()) {
            System.out.println("Endereço inválido.");
            return;
        }

        Endereco enderecoEntrega = cliente.getEnderecos().get(indiceEndereco);
        Pedido pedido = new Pedido(numero, cliente, enderecoEntrega);

        while (true) {
            System.out.print("Código do produto (ou 0 para finalizar): ");
            int codigoProduto = sc.nextInt();
            sc.nextLine();

            if (codigoProduto == 0) break;

            ProdutoBase produto = produtoService.getProdutoPorCodigo(codigoProduto);
            if (produto != null) {
                System.out.println("Produto: " + produto.getDescricao() + " | Estoque disponível: " + produto.getEstoque());
                System.out.print("Quantidade desejada: ");
                int quantidade = sc.nextInt();
                sc.nextLine();

                if (quantidade <= 0) {
                    System.out.println("Quantidade inválida.");
                } else if (produto.getEstoque() < quantidade) {
                    System.out.println("Estoque insuficiente.");
                } else {
                    pedido.adicionarProduto(produto, quantidade);
                    produto.setEstoque(produto.getEstoque() - quantidade);
                    System.out.println("Produto adicionado: " + quantidade + "x " + produto.getDescricao());
                }
            } else {
                System.out.println("Produto não encontrado.");
            }
        }

        pedidos.put(numero, pedido);
        LogService.registrar("Pedido cadastrado: " + numero);
        System.out.println("Pedido registrado com sucesso.");

        salvarTodosPedidosNoArquivo();
        produtoService.salvarTodosProdutosNoArquivo();
    }

    public void listarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
        } else {
        for (Pedido p : pedidos.values()) {
            System.out.println(p);
            System.out.println("--------------------");
        }
    }
    }

    public void buscarPedidoPorNumero(Scanner sc) {
        System.out.print("Digite o número do pedido: ");
        int numero = sc.nextInt();
        sc.nextLine();

        Pedido pedido = pedidos.get(numero);
        if (pedido != null) {
            System.out.println(pedido);
            LogService.registrar("Pedido consultado: " + numero);
        } else {
            System.out.println("Pedido não encontrado.");
        }
    }

    public void atualizarPedido(Scanner sc) {
    System.out.print("Digite o número do pedido que deseja atualizar: ");
    int numero = sc.nextInt();
    sc.nextLine();

    Pedido pedido = pedidos.get(numero);
    if (pedido == null) {
        System.out.println("Pedido não encontrado.");
        return;
    }

    System.out.println("Pedido atual:\n" + pedido);

    System.out.print("Deseja remover algum produto do pedido? (s/n): ");
    String removerProduto = sc.nextLine().trim().toLowerCase();
    while (!removerProduto.equals("s") && !removerProduto.equals("n")) {
        System.out.print("Opção inválida. Digite 's' ou 'n': ");
        removerProduto = sc.nextLine().trim().toLowerCase();
    }

    if (removerProduto.equals("s")) {
        Map<ProdutoBase, Integer> produtos = pedido.getProdutos();
        if (produtos.isEmpty()) {
            System.out.println("O pedido não contém produtos.");
        } else {
            for (Map.Entry<ProdutoBase, Integer> entry : produtos.entrySet()) {
                ProdutoBase produto = entry.getKey();
                int qtd = entry.getValue();
                System.out.println("Código: " + produto.getCodigo() + " | Descrição: " + produto.getDescricao() + " | Quantidade: " + qtd);
            }

            System.out.print("Informe o código do produto que deseja remover: ");
            int codigoRemover = sc.nextInt();
            sc.nextLine();

            ProdutoBase encontrado = null;
            for (ProdutoBase p : produtos.keySet()) {
                if (p.getCodigo() == codigoRemover) {
                    encontrado = p;
                    break;
                }
            }

            if (encontrado != null) {
                int qtdRemovida = produtos.get(encontrado);
                produtos.remove(encontrado);
                ProdutoBase original = produtoService.getProdutoPorCodigo(codigoRemover);
                if (original != null) {
                    original.setEstoque(original.getEstoque() + qtdRemovida);
                }
                System.out.println("Produto removido. Estoque restaurado: +" + qtdRemovida);

                if (produtos.isEmpty()) {
                    System.out.println("O pedido ficou vazio.");
                    System.out.print("Deseja excluir este pedido? (s/n): ");
                    String excluirPedido = sc.nextLine().trim().toLowerCase();
                    while (!excluirPedido.equals("s") && !excluirPedido.equals("n")) {
                        System.out.print("Opção inválida. Digite 's' ou 'n': ");
                        excluirPedido = sc.nextLine().trim().toLowerCase();
                    }

                    if (excluirPedido.equals("s")) {
                        pedidos.remove(numero);
                        LogService.registrar("Pedido excluído após remoção de todos os produtos: " + numero);
                        salvarTodosPedidosNoArquivo();
                        produtoService.salvarTodosProdutosNoArquivo();
                        System.out.println("Pedido excluído com sucesso.");
                        return; 
                    }
                }

            } else {
                System.out.println("Produto não encontrado no pedido.");
            }
        }
    }

    System.out.print("Deseja adicionar novos produtos ao pedido? (s/n): ");
    String adicionarProduto = sc.nextLine().trim().toLowerCase();
    while (!adicionarProduto.equals("s") && !adicionarProduto.equals("n")) {
        System.out.print("Opção inválida. Digite 's' ou 'n': ");
        adicionarProduto = sc.nextLine().trim().toLowerCase();
    }

    if (adicionarProduto.equals("s")) {
        while (true) {
            System.out.print("Código do produto (ou 0 para parar): ");
            int codigoProduto = sc.nextInt();
            sc.nextLine();

            if (codigoProduto == 0) break;

            ProdutoBase produto = produtoService.getProdutoPorCodigo(codigoProduto);
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                continue;
            }

            System.out.println("Produto: " + produto.getDescricao() + " | Estoque disponível: " + produto.getEstoque());
            System.out.print("Quantidade: ");
            int quantidade = sc.nextInt();
            sc.nextLine();

            if (quantidade <= 0) {
                System.out.println("Quantidade inválida.");
            } else if (quantidade > produto.getEstoque()) {
                System.out.println("Estoque insuficiente.");
            } else {
                pedido.adicionarProduto(produto, quantidade);
                produto.setEstoque(produto.getEstoque() - quantidade);
                System.out.println("Produto adicionado: " + quantidade + "x " + produto.getDescricao());
            }
        }
    }

    LogService.registrar("Pedido atualizado (produtos removidos/adicionados): " + numero);
    salvarTodosPedidosNoArquivo();
    produtoService.salvarTodosProdutosNoArquivo();
    System.out.println("Pedido atualizado com sucesso.");
}

    public void excluirPedido(Scanner sc) {
        System.out.print("Digite o número do pedido que deseja excluir: ");
        int numero = sc.nextInt();
        sc.nextLine();

        Pedido removido = pedidos.remove(numero);
        if (removido != null) {
            for (Map.Entry<ProdutoBase, Integer> entry : removido.getProdutos().entrySet()) {
                ProdutoBase original = produtoService.getProdutoPorCodigo(entry.getKey().getCodigo());
                if (original != null) {
                    original.setEstoque(original.getEstoque() + entry.getValue());
                }
            }

            LogService.registrar("Pedido excluído: " + numero);
            salvarTodosPedidosNoArquivo();
            System.out.println("Pedido excluído com sucesso. Estoques restaurados.");
        } else {
            System.out.println("Pedido não encontrado.");
        }
        produtoService.salvarTodosProdutosNoArquivo();
    }

    public void carregarPedidos() {
        try (Scanner sc = new Scanner(new File("pedidos.txt"))) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(";");

                if (partes.length >= 5) {
                    int numero = Integer.parseInt(partes[0]);
                    int codigoCliente = Integer.parseInt(partes[1]);
                    String cepEndereco = partes[2];
                    String[] produtosInfo = partes[3].split(",");

                    Pessoa cliente = pessoaService.getPessoaPorCodigo(codigoCliente);
                    if (cliente == null) continue;

                    Endereco enderecoEntrega = cliente.getEnderecos().stream()
                            .filter(e -> e.getCep().equals(cepEndereco))
                            .findFirst().orElse(null);
                    if (enderecoEntrega == null) continue;

                    Pedido pedido = new Pedido(numero, cliente, enderecoEntrega);

                    for (String prod : produtosInfo) {
                        String[] dados = prod.split(":");
                        if (dados.length == 2) {
                            int codProduto = Integer.parseInt(dados[0]);
                            int quantidade = Integer.parseInt(dados[1]);
                            ProdutoBase produto = produtoService.getProdutoPorCodigo(codProduto);
                            if (produto != null) {
                                pedido.adicionarProduto(produto, quantidade);
                            }
                        }
                    }

                    pedidos.put(numero, pedido);
                }
            }
            System.out.println("Pedidos carregados com sucesso.");
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo pedidos.txt não encontrado.");
        }
    }

    private void salvarTodosPedidosNoArquivo() {
        try (FileWriter fw = new FileWriter("pedidos.txt")) {
            for (Pedido pedido : pedidos.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(pedido.getNumero()).append(";");
                sb.append(pedido.getCliente().getCodigo()).append(";");
                sb.append(pedido.getEnderecoEntrega().getCep()).append(";");

                for (Map.Entry<ProdutoBase, Integer> entry : pedido.getProdutos().entrySet()) {
                    sb.append(entry.getKey().getCodigo()).append(":").append(entry.getValue()).append(",");
                }

                if (!pedido.getProdutos().isEmpty()) {
                    sb.setLength(sb.length() - 1);
                }

                sb.append(";").append(pedido.calcularTotal()).append("\n");
                fw.write(sb.toString());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar pedidos: " + e.getMessage());
        }
    }
}
