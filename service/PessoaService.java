package service;

import java.util.*;
import model.*;
import java.io.*;

public class PessoaService {
    private Map<Integer, Pessoa> pessoas = new HashMap<>();

    public void cadastrarPessoa(Scanner sc) {
        System.out.print("Código: ");
        int codigo = sc.nextInt();
        sc.nextLine(); 
        if (pessoas.containsKey(codigo)) {
            System.out.println("Pessoa com esse código já existe.");
            return;
        }

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        TipoPessoa tipo = null;
        while (tipo == null) {
            System.out.println("Tipo de Pessoa:");
            System.out.println("1 - Cliente");
            System.out.println("2 - Fornecedor");
            System.out.println("3 - Ambos");
            System.out.print("Escolha uma opção (1-3): ");
            String opcao = sc.nextLine();

            switch (opcao) {
                case "1": tipo = TipoPessoa.CLIENTE; break;
                case "2": tipo = TipoPessoa.FORNECEDOR; break;
                case "3": tipo = TipoPessoa.AMBOS; break;
                default: System.out.println("Opção inválida. Tente novamente.");
            }
        }

        Pessoa novaPessoa = new Pessoa(codigo, nome, tipo);

        String adicionarMais;
        do {
            System.out.println("Cadastro de endereço:");
            System.out.print("CEP: ");
            String cep = sc.nextLine();
            System.out.print("Logradouro: ");
            String logradouro = sc.nextLine();
            System.out.print("Número: ");
            String numero = sc.nextLine();
            System.out.print("Complemento: ");
            String complemento = sc.nextLine();

            String tipoEndereco = "";
            while (true) {
                System.out.println("Tipo de Endereço:");
                System.out.println("1 - Residencial");
                System.out.println("2 - Comercial");
                System.out.println("3 - Entrega");
                System.out.println("4 - Correspondência");
                System.out.print("Escolha uma opção (1-4): ");
                String opcaoEndereco = sc.nextLine();

                switch (opcaoEndereco) {
                    case "1": tipoEndereco = "Residencial"; break;
                    case "2": tipoEndereco = "Comercial"; break;
                    case "3": tipoEndereco = "Entrega"; break;
                    case "4": tipoEndereco = "Correspondência"; break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        continue;
                }
                break;
            }

            Endereco endereco = new Endereco(cep, logradouro, numero, complemento, tipoEndereco);
            novaPessoa.adicionarEndereco(endereco);

            System.out.print("Deseja adicionar outro endereço? (s/n): ");
            adicionarMais = sc.nextLine().trim().toLowerCase();
            while (!adicionarMais.equals("s") && !adicionarMais.equals("n")) {
                System.out.print("Opção inválida. Digite 's' para sim ou 'n' para não: ");
                adicionarMais = sc.nextLine().trim().toLowerCase();
            }
        } while (adicionarMais.equals("s"));

        pessoas.put(codigo, novaPessoa);
        LogService.registrar("Pessoa cadastrada: " + novaPessoa);

        salvarTodasPessoasNoArquivo();
        System.out.println("Pessoa cadastrada com sucesso.");
    }
    
    public void listarPessoas() {
        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada.");
        } else {
            for (Pessoa p : pessoas.values()) {
                System.out.println(p);
                System.out.println("Endereços:");
                for (Endereco e : p.getEnderecos()) {
                    System.out.println("  - " + e);
                }
                System.out.println();
            }
        }
    }

    public void buscarPessoaPorCodigo(Scanner sc) {
        System.out.print("Digite o código da pessoa: ");
        int codigo = sc.nextInt();
        sc.nextLine();

        Pessoa pessoa = pessoas.get(codigo);
        if (pessoa != null) {
            System.out.println(pessoa);
            System.out.println("Endereços:");
            for (Endereco e : pessoa.getEnderecos()) {
                System.out.println("  - " + e);
            }
            LogService.registrar("Pessoa consultada por código: " + codigo);
        } else {
            System.out.println("Pessoa não encontrada.");
        }
    }

    public Pessoa getPessoaPorCodigo(int codigo) {
        return pessoas.get(codigo);
    }

   public void atualizarPessoa(Scanner sc) {
    System.out.print("Digite o código da pessoa a atualizar: ");
    int codigo = sc.nextInt();
    sc.nextLine();

    Pessoa pessoa = pessoas.get(codigo);
    if (pessoa != null) {
        System.out.println("Pessoa atual: " + pessoa);

        System.out.print("Novo nome (deixe em branco para manter): ");
        String novoNome = sc.nextLine();
        if (!novoNome.isEmpty()) pessoa.setNome(novoNome);

        System.out.print("Deseja alterar o tipo da pessoa? (s/n): ");
        String alterarTipo = sc.nextLine().trim().toLowerCase();
        while (!alterarTipo.equals("s") && !alterarTipo.equals("n")) {
            System.out.print("Opção inválida. Digite 's' ou 'n': ");
            alterarTipo = sc.nextLine().trim().toLowerCase();
        }

        if (alterarTipo.equals("s")) {
            TipoPessoa novoTipo = null;
            while (novoTipo == null) {
                System.out.println("Novo tipo:");
                System.out.println("1 - Cliente");
                System.out.println("2 - Fornecedor");
                System.out.println("3 - Ambos");
                System.out.print("Escolha uma opção: ");
                String opcao = sc.nextLine();
                switch (opcao) {
                    case "1": novoTipo = TipoPessoa.CLIENTE; break;
                    case "2": novoTipo = TipoPessoa.FORNECEDOR; break;
                    case "3": novoTipo = TipoPessoa.AMBOS; break;
                    default: System.out.println("Opção inválida.");
                }
            }
            pessoa.setTipo(novoTipo);
        }

        if (!pessoa.getEnderecos().isEmpty()) {
            String opc;
            do {
                System.out.println("Deseja atualizar, excluir ou adicionar endereço?");
                System.out.println("1 - Atualizar endereço");
                System.out.println("2 - Excluir endereço");
                System.out.println("3 - Adicionar novo endereço");
                System.out.println("0 - Nenhuma ação");
                System.out.print("Escolha uma opção: ");
                opc = sc.nextLine();

                switch (opc) {
                    case "1": 
                        for (int i = 0; i < pessoa.getEnderecos().size(); i++) {
                            System.out.println(i + " - " + pessoa.getEnderecos().get(i));
                        }
                        System.out.print("Digite o número do endereço que deseja atualizar: ");
                        int indice = sc.nextInt();
                        sc.nextLine();

                        if (indice >= 0 && indice < pessoa.getEnderecos().size()) {
                            Endereco e = pessoa.getEnderecos().get(indice);

                            System.out.print("Novo CEP (deixe em branco para manter): ");
                            String novoCep = sc.nextLine();
                            if (!novoCep.isEmpty()) e.setCep(novoCep);

                            System.out.print("Novo Logradouro (deixe em branco para manter): ");
                            String novoLogradouro = sc.nextLine();
                            if (!novoLogradouro.isEmpty()) e.setLogradouro(novoLogradouro);

                            System.out.print("Novo Número (deixe em branco para manter): ");
                            String novoNumero = sc.nextLine();
                            if (!novoNumero.isEmpty()) e.setNumero(novoNumero);

                            System.out.print("Novo Complemento (deixe em branco para manter): ");
                            String novoComplemento = sc.nextLine();
                            if (!novoComplemento.isEmpty()) e.setComplemento(novoComplemento);

                            System.out.print("Deseja alterar o tipo de endereço? (s/n): ");
                            String altTipo = sc.nextLine().trim().toLowerCase();
                            while (!altTipo.equals("s") && !altTipo.equals("n")) {
                                System.out.print("Opção inválida. Digite 's' ou 'n': ");
                                altTipo = sc.nextLine().trim().toLowerCase();
                            }

                            if (altTipo.equals("s")) {
                                String novoTipo = "";
                                while (true) {
                                    System.out.println("Novo Tipo de Endereço:");
                                    System.out.println("1 - Residencial");
                                    System.out.println("2 - Comercial");
                                    System.out.println("3 - Entrega");
                                    System.out.println("4 - Correspondência");
                                    System.out.print("Escolha uma opção (1-4): ");
                                    String op = sc.nextLine();

                                    switch (op) {
                                        case "1": novoTipo = "Residencial"; break;
                                        case "2": novoTipo = "Comercial"; break;
                                        case "3": novoTipo = "Entrega"; break;
                                        case "4": novoTipo = "Correspondência"; break;
                                        default:
                                            System.out.println("Opção inválida.");
                                            continue;
                                    }
                                    e.setTipo(novoTipo);
                                    break;
                                }
                            }

                            System.out.println("Endereço atualizado com sucesso.");
                            LogService.registrar("Endereço atualizado da pessoa " + codigo + ": " + e);
                        } else {
                            System.out.println("Índice inválido.");
                        }
                        break;

                    case "2": 
                        for (int i = 0; i < pessoa.getEnderecos().size(); i++) {
                            System.out.println(i + " - " + pessoa.getEnderecos().get(i));
                        }
                        System.out.print("Digite o número do endereço que deseja excluir: ");
                        int indiceExcluir = sc.nextInt();
                        sc.nextLine();

                        if (indiceExcluir >= 0 && indiceExcluir < pessoa.getEnderecos().size()) {
                            Endereco removido = pessoa.getEnderecos().remove(indiceExcluir);
                            System.out.println("Endereço removido com sucesso: " + removido);
                            LogService.registrar("Endereço removido da pessoa " + codigo + ": " + removido);
                        } else {
                            System.out.println("Índice inválido.");
                        }
                        break;

                    case "3": 
                        System.out.println("Cadastro de novo endereço:");
                        System.out.print("CEP: ");
                        String cep = sc.nextLine();
                        System.out.print("Logradouro: ");
                        String logradouro = sc.nextLine();
                        System.out.print("Número: ");
                        String numero = sc.nextLine();
                        System.out.print("Complemento: ");
                        String complemento = sc.nextLine();

                        String tipoEndereco = "";
                        while (true) {
                            System.out.println("Tipo de Endereço:");
                            System.out.println("1 - Residencial");
                            System.out.println("2 - Comercial");
                            System.out.println("3 - Entrega");
                            System.out.println("4 - Correspondência");
                            System.out.print("Escolha uma opção (1-4): ");
                            String opcaoEndereco = sc.nextLine();

                            switch (opcaoEndereco) {
                                case "1": tipoEndereco = "Residencial"; break;
                                case "2": tipoEndereco = "Comercial"; break;
                                case "3": tipoEndereco = "Entrega"; break;
                                case "4": tipoEndereco = "Correspondência"; break;
                                default:
                                    System.out.println("Opção inválida.");
                                    continue;
                            }
                            break;
                        }

                        Endereco novoEndereco = new Endereco(cep, logradouro, numero, complemento, tipoEndereco);
                        pessoa.adicionarEndereco(novoEndereco);
                        System.out.println("Novo endereço adicionado com sucesso.");
                        LogService.registrar("Novo endereço adicionado para pessoa " + codigo + ": " + novoEndereco);
                        break;

                    case "0":
                        break;

                    default:
                        System.out.println("Opção inválida.");
                        break;
                }

                System.out.print("Deseja realizar outra ação com endereços? (s/n): ");
                opc = sc.nextLine().trim().toLowerCase();
                while (!opc.equals("s") && !opc.equals("n")) {
                    System.out.print("Opção inválida. Digite 's' ou 'n': ");
                    opc = sc.nextLine().trim().toLowerCase();
                }
            } while (opc.equals("s"));
        }

        LogService.registrar("Pessoa atualizada: " + pessoa);
        salvarTodasPessoasNoArquivo();
        System.out.println("Pessoa atualizada com sucesso.");
    } else {
        System.out.println("Pessoa não encontrada.");
    }
}

    public void excluirPessoa(Scanner sc) {
        System.out.print("Digite o código da pessoa a excluir: ");
        int codigo = sc.nextInt();
        sc.nextLine();

        Pessoa removida = pessoas.remove(codigo);
        if (removida != null) {
            LogService.registrar("Pessoa excluída: " + removida);
            salvarTodasPessoasNoArquivo();
            System.out.println("Pessoa excluída com sucesso.");
        } else {
            System.out.println("Pessoa não encontrada.");
        }
    }

    public void carregarPessoas() {
        try (Scanner sc = new Scanner(new File("pessoas.txt"))) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(";");
                if (partes.length >= 3) {
                    int codigo = Integer.parseInt(partes[0]);
                    String nome = partes[1];
                    TipoPessoa tipo = TipoPessoa.valueOf(partes[2].toUpperCase());
                    Pessoa pessoa = new Pessoa(codigo, nome, tipo);
                    pessoas.put(codigo, pessoa);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo pessoas.txt não encontrado.");
        }

        try (Scanner sc = new Scanner(new File("enderecos.txt"))) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(";");
                if (partes.length >= 6) {
                    int codigoPessoa = Integer.parseInt(partes[0]);
                    String cep = partes[1];
                    String logradouro = partes[2];
                    String numero = partes[3];
                    String complemento = partes[4];
                    String tipo = partes[5];

                    Endereco endereco = new Endereco(cep, logradouro, numero, complemento, tipo);
                    Pessoa pessoa = pessoas.get(codigoPessoa);
                    if (pessoa != null) {
                        pessoa.adicionarEndereco(endereco);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo enderecos.txt não encontrado.");
        }

        System.out.println("Pessoas carregadas com sucesso.");
    }

    private void salvarTodasPessoasNoArquivo() {
        try (FileWriter fwPessoa = new FileWriter("pessoas.txt");
             FileWriter fwEndereco = new FileWriter("enderecos.txt")) {

            for (Pessoa pessoa : pessoas.values()) {
                fwPessoa.write(pessoa.getCodigo() + ";" + pessoa.getNome() + ";" + pessoa.getTipo().name() + "\n");

                for (Endereco e : pessoa.getEnderecos()) {
                    fwEndereco.write(pessoa.getCodigo() + ";" + e.getCep() + ";" + e.getLogradouro() + ";" +
                                     e.getNumero() + ";" + e.getComplemento() + ";" + e.getTipo() + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar pessoas: " + e.getMessage());
        }
    }
}
