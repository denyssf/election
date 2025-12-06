import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaEleitoral {

    static List<Partido> partidos = new ArrayList<>();
    static List<Candidato> candidatos = new ArrayList<>();
    static List<Eleitor> eleitores = new ArrayList<>();
    static Configuracao config = new Configuracao();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== SISTEMA DE VOTAÇÃO ===");
            System.out.println("1. Área do Candidato (Cadastro/Votar)");
            System.out.println("2. Área do Eleitor (Cadastro/Votar)");
            System.out.println("3. Área do Organizador");
            System.out.println("0. Sair");
            System.out.print("Opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1": menuCandidato(); break;
                case "2": menuEleitor(); break;
                case "3": menuOrganizador(); break;
                case "0": System.exit(0);
                default: System.out.println("Opção inexistente. Tente novamente.");
            }
        }
    }

    static void menuCandidato() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- CANDIDATO ---");
            System.out.println("1. Cadastrar candidatura");
            System.out.println("2. Entrar para Votar");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            String op = scanner.nextLine();

            switch (op) {
                case "1": cadastrarCandidato(); break;
                case "2": realizarVotacao(); break;
                case "0": voltar = true; break;
                default: System.out.println("Opção inexistente. Tente novamente.");
            }
        }
    }

    static void cadastrarCandidato() {
        if (config.eleicaoFinalizada) { System.out.println("Eleição já ocorreu."); return; }
        
        System.out.print("Nome Completo: ");
        String nome = scanner.nextLine();

        
        String dataNasc = Validador.lerDataNascimento(scanner);
        if (dataNasc == null) return;
        

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        if (!Validador.isCPF(cpf)) { System.out.println("CPF Inválido!"); return; }
        if (buscarPessoa(cpf) != null) { System.out.println("CPF já cadastrado!"); return; }

        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        
        if (partidos.isEmpty()) { System.out.println("Nenhum partido cadastrado pelo organizador."); return; }
        System.out.println("Partidos disponíveis:");
        for (Partido p : partidos) System.out.println("Num: " + p.numero + " - " + p.nome);
        
        System.out.print("Digite o Número do Partido: ");
        try {
            int numPartido = Integer.parseInt(scanner.nextLine());
            Partido pSelecionado = partidos.stream().filter(p -> p.numero == numPartido).findFirst().orElse(null);

            if (pSelecionado == null) { System.out.println("Partido não encontrado."); return; }
            if (pSelecionado.limite > 0 && contagemCandidatosPorPartido(numPartido) >= pSelecionado.limite) {
                System.out.println("Limite de candidatos excedido para este partido."); return;
            }

            System.out.print("Seu número de escolha (será concatenado): ");
            String numEscolha = scanner.nextLine();
            int numeroFinal = Integer.parseInt(pSelecionado.numero + numEscolha);

            String codigo = Validador.gerarCodigo();
            Candidato novo = new Candidato(nome, cpf, email, codigo, numeroFinal, pSelecionado, dataNasc);
            candidatos.add(novo);
            
            Utils.enviarEmail(email, "Seu código de candidato/voto é: " + codigo);
            System.out.println("Cadastro realizado! Verifique seu e-mail.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Digite apenas números.");
        }
    }

    static void menuEleitor() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- ELEITOR ---");
            System.out.println("1. Cadastrar");
            System.out.println("2. Votar");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            String op = scanner.nextLine();

            switch (op) {
                case "1": cadastrarEleitor(); break;
                case "2": realizarVotacao(); break;
                case "0": voltar = true; break;
                default: System.out.println("Opção inexistente. Tente novamente.");
            }
        }
    }

    static void cadastrarEleitor() {
        System.out.print("Nome Completo: ");
        String nome = scanner.nextLine();

        
        String dataNasc = Validador.lerDataNascimento(scanner);
        if (dataNasc == null) return;
    

        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        if (!Validador.isCPF(cpf)) { System.out.println("CPF Inválido!"); return; }
        if (buscarPessoa(cpf) != null) { System.out.println("CPF já cadastrado!"); return; }

        String codigo = Validador.gerarCodigo();
        Eleitor novo = new Eleitor(nome, cpf, email, codigo, dataNasc);
        eleitores.add(novo);

        Utils.enviarEmail(email, "Seu código de eleitor é: " + codigo);
        System.out.println("Cadastro realizado! Verifique seu e-mail.");
    }

    static void realizarVotacao() {
        if (!config.eleicaoAtiva) { System.out.println("A eleição não está ativa no momento."); return; }

        System.out.print("Digite seu CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Digite seu Código de Acesso: ");
        String codigo = scanner.nextLine();

        Pessoa pessoa = buscarPessoa(cpf);
        
        if (pessoa == null || !pessoa.codigoAcesso.equals(codigo)) {
            System.out.println("Credenciais inválidas.");
            return;
        }
        if (pessoa.jaVotou) {
            System.out.println("Você JÁ votou. Tentativa bloqueada.");
            return;
        }

        System.out.println("--- URNA ---");
        
        System.out.print("Digite o número do Candidato: ");
        String strNumCand = scanner.nextLine();
        try {
            int numCand = Integer.parseInt(strNumCand);

            Candidato cand = null;
            for(Candidato c : candidatos) {
                if(c.numeroPolitico == numCand) {
                    cand = c;
                    break;
                }
            }
            
            if (cand != null) {
                System.out.println("Candidato Selecionado: " + cand.nome + " (" + cand.partido.nome + ")");
                System.out.println("[1] CONFIRMAR  |  [2] RETORNAR/CORRIGIR");
                String confirma = scanner.nextLine();
                if (confirma.equals("1")) {
                    cand.votos++;
                    pessoa.jaVotou = true; 
                    System.out.println("VOTO COMPUTADO COM SUCESSO!");
                } else {
                    System.out.println("Voto cancelado. Reinicie o processo.");
                }
            } else {
                System.out.println("Candidato inexistente (Voto Nulo). [1] Confirmar Nulo | [2] Voltar");
                if (scanner.nextLine().equals("1")) {
                    pessoa.jaVotou = true;
                    System.out.println("Voto NULO computado.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Voto cancelado.");
        }
    }

    static void menuOrganizador() {
        System.out.print("Usuário: ");
        String user = scanner.nextLine();
        System.out.print("Senha: ");
        String pass = scanner.nextLine();

        if (!user.equals(config.adminUser) || !pass.equals(config.adminPass)) {
            System.out.println("Acesso negado.");
            return;
        }

        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- PAINEL ORGANIZADOR ---");
            System.out.println("1. Criar Partido");
            System.out.println("2. Lista de Candidatos");
            System.out.println("3. Total de Eleitores (Sigiloso)");
            System.out.println("4. Deletar (Partido/Candidato)");
            System.out.println("5. Alterações (Credenciais/Data/Partido)");
            System.out.println("6. Configurar/Iniciar Eleição");
            System.out.println("7. Apuração (Relatório Final)");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            String op = scanner.nextLine();

            switch (op) {
                case "1": criarPartido(); break;
                case "2": listarCandidatos(); break;
                case "3": mostrarTotalEleitores(); break;
                case "4": menuDeletar(); break;
                case "5": menuAlteracoes(); break;
                case "6": configurarEleicao(); break;
                case "7": apuracao(); break;
                case "0": voltar = true; break;
                default: System.out.println("Opção inexistente. Tente novamente.");
            }
        }
    }

    static void criarPartido() {
        System.out.print("Nome do Partido: ");
        String nomeP = scanner.nextLine();
        System.out.print("Número do Partido: ");
        try {
            int numP = Integer.parseInt(scanner.nextLine());
            System.out.print("Tem limite de candidatos? (S/N): ");
            int lim = 0;
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                System.out.print("Qual o limite? ");
                lim = Integer.parseInt(scanner.nextLine());
            }
            partidos.add(new Partido(nomeP, numP, lim));
            System.out.println("Partido criado.");
        } catch (NumberFormatException e) {
            System.out.println("Número inválido.");
        }
    }

    static void listarCandidatos() {
        System.out.println("--- Candidatos ---");
        for (Candidato c : candidatos) {
            System.out.println("Nº: " + c.numeroPolitico + " | Nome: " + c.nome + " | Partido: " + c.partido.nome);
        }
    }

    static void mostrarTotalEleitores() {
        int total = eleitores.size() + candidatos.size();
        System.out.println("Total de eleitores aptos: " + total);
        System.out.println("(Dados detalhados ocultos por sigilo)");
    }

    static void menuDeletar() {
        if (config.eleicaoAtiva || config.eleicaoFinalizada) {
            System.out.println("Ação bloqueada: Eleição em andamento ou finalizada.");
            return;
        }
        System.out.println("1. Deletar Partido | 2. Deletar Candidato");
        String delOp = scanner.nextLine();
        if(delOp.equals("1")) {
            System.out.print("Num Partido: ");
            try {
                int n = Integer.parseInt(scanner.nextLine());
                boolean removeu = partidos.removeIf(p -> p.numero == n);
                System.out.println(removeu ? "Partido removido." : "Partido não encontrado.");
            } catch (NumberFormatException e) { System.out.println("Entrada inválida"); }
        } else if (delOp.equals("2")) {
            System.out.print("CPF Candidato: ");
            String cpfDel = scanner.nextLine();
            boolean removeu = candidatos.removeIf(c -> c.cpf.equals(cpfDel));
            System.out.println(removeu ? "Candidato removido." : "Candidato não encontrado.");
        } else {
            System.out.println("Opção inválida.");
        }
    }

    static void menuAlteracoes() {
        if (config.isDiaDaEleicao()) {
            System.out.println("Ação bloqueada: Hoje é dia de eleição.");
            return;
        }
        System.out.println("1. Alterar Admin | 2. Alterar Partido");
        String altOp = scanner.nextLine();
        if(altOp.equals("1")) {
            System.out.print("Novo User: "); config.adminUser = scanner.nextLine();
            System.out.print("Nova Senha: "); config.adminPass = scanner.nextLine();
        } else if (altOp.equals("2")) {
            System.out.println("Funcionalidade simplificada: Delete e crie novamente.");
        } else {
            System.out.println("Opção inválida.");
        }
    }

    static void configurarEleicao() {
        try {
            System.out.print("Data Eleição (aaaa-mm-dd): ");
            config.dataEleicao = LocalDate.parse(scanner.nextLine());
            System.out.print("Hora Inicio (HH:mm): ");
            String hora = scanner.nextLine();
            
            System.out.println("[SISTEMA] Agendando notificação para 5 min antes de " + hora + "...");
            for(Eleitor e : eleitores) Utils.enviarEmail(e.email, "Lembrete: Eleição começa em 5 min!");
            for(Candidato c : candidatos) Utils.enviarEmail(c.email, "Lembrete: Eleição começa em 5 min!");
            
            System.out.println("Deseja abrir a urna agora? (S/N)");
            if(scanner.nextLine().equalsIgnoreCase("S")) {
                config.eleicaoAtiva = true;
                System.out.println("URNA ABERTA!");
            }
        } catch (Exception e) {
            System.out.println("Formato de data inválido.");
        }
    }

    static void apuracao() {
        config.eleicaoAtiva = false;
        config.eleicaoFinalizada = true;
        System.out.println("\n--- RELATÓRIO DE APURAÇÃO ---");
        int totalVotos = 0;
        for(Candidato c : candidatos) totalVotos += c.votos;
        
        Candidato vencedor = null;
        int maxVotos = -1;

        StringBuilder relatorio = new StringBuilder("Resultado:\n");

        for(Candidato c : candidatos) {
            double porc = (totalVotos == 0) ? 0 : (c.votos * 100.0 / totalVotos);
            String linha = String.format("%s (%d votos) - %.2f%%", c.nome, c.votos, porc);
            System.out.println(linha);
            relatorio.append(linha).append("\n");

            if(c.votos > maxVotos) {
                maxVotos = c.votos;
                vencedor = c;
            }
        }
        
        System.out.println("Total Votos Válidos: " + totalVotos);
        if(vencedor != null) System.out.println("VENCEDOR: " + vencedor.nome);

        System.out.println("\nEnviando relatório para TODOS...");
        String msgFinal = relatorio.toString();
        for(Eleitor e : eleitores) Utils.enviarEmail(e.email, msgFinal);
        for(Candidato c : candidatos) Utils.enviarEmail(c.email, msgFinal);
    }

    static Pessoa buscarPessoa(String cpf) {
        for (Candidato c : candidatos) if (c.cpf.equals(cpf)) return c;
        for (Eleitor e : eleitores) if (e.cpf.equals(cpf)) return e;
        return null;
    }

    static long contagemCandidatosPorPartido(int numPartido) {
        long count = 0;
        for(Candidato c : candidatos) {
            if(c.partido.numero == numPartido) count++;
        }
        return count;
    }
}