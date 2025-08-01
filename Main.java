import model.*;
import service.*;
import util.MenuUtil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PessoaService pessoaService = new PessoaService();
        pessoaService.carregarPessoas();

        ProdutoService produtoService = new ProdutoService(pessoaService);
        produtoService.carregarProdutos();

        PedidoService pedidoService = new PedidoService(pessoaService, produtoService);
        pedidoService.carregarPedidos(); 

        int opcao;
        do {
            MenuUtil.mostrarMenu("menu_principal.txt");
            System.out.print("Opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    menuCadastro(sc, pessoaService);
                    break;
                case 2:
                    menuProduto(sc, produtoService);
                    break;
                case 3:
                    menuPedidos(sc, pedidoService);
                    break;
                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

        sc.close();
    }

    public static void menuCadastro(Scanner sc, PessoaService pessoaService) {
        int opcao;
        do {
            MenuUtil.mostrarMenu("menu_cadastro.txt");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    pessoaService.cadastrarPessoa(sc);
                    break;
                case 2:
                    pessoaService.listarPessoas();
                    break;
                case 3:
                    pessoaService.buscarPessoaPorCodigo(sc);
                    break;
                case 4:
                    pessoaService.atualizarPessoa(sc);
                    break;
                case 5:
                    pessoaService.excluirPessoa(sc);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    public static void menuProduto(Scanner sc, ProdutoService produtoService) {
        int opcao;
        do {
            MenuUtil.mostrarMenu("menu_produto.txt");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    produtoService.cadastrarProduto(sc);
                    break;
                case 2:
                    produtoService.listarProdutos();
                    break;
                case 3:
                   produtoService.buscarProdutoPorCodigo(sc);
                    break;
                case 4:
                    produtoService.atualizarProduto(sc);
                    break;
                case 5:
                    produtoService.excluirProduto(sc);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    public static void menuPedidos(Scanner sc, PedidoService pedidoService) {
        int opcao;
        do {
            MenuUtil.mostrarMenu("menu_pedidos.txt");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    pedidoService.cadastrarPedido(sc);
                    break;
                case 2:
                    pedidoService.listarPedidos();
                    break;
                case 3:
                    pedidoService.buscarPedidoPorNumero(sc);
                    break;
                case 4:
                    pedidoService.atualizarPedido(sc);
                    break;
                case 5:
                    pedidoService.excluirPedido(sc);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }
}
