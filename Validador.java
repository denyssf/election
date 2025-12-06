import java.time.LocalDate;
import java.time.Period;
import java.util.Random;
import java.util.Scanner;

public class Validador {
    
    public static String gerarCodigo() {
        return String.valueOf(1000 + new Random().nextInt(9000));
    }

    
    public static String lerDataNascimento(Scanner scanner) {
        int dia = 0, mes = 0, ano = 0;

       
        while (true) {
            System.out.print("Dia do nascimento (1-30): ");
            try {
                dia = Integer.parseInt(scanner.nextLine());
                if (dia >= 1 && dia <= 30) break;
                System.out.println("Dia inválido! Digite entre 1 e 30.");
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números.");
            }
        }

        
        while (true) {
            System.out.print("Mês do nascimento (1-12): ");
            try {
                mes = Integer.parseInt(scanner.nextLine());
                if (mes >= 1 && mes <= 12) break;
                System.out.println("Mês inválido! Digite entre 1 e 12.");
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números.");
            }
        }

        
        while (true) {
            System.out.print("Ano do nascimento (ex: 1990): ");
            try {
                ano = Integer.parseInt(scanner.nextLine());
                
                
                LocalDate dataNasc = LocalDate.of(ano, mes, dia);
                LocalDate hoje = LocalDate.now();
                int idade = Period.between(dataNasc, hoje).getYears();

                if (idade < 18) {
                    System.out.println("ERRO: Idade insuficiente (" + idade + " anos). Mínimo 18 anos.");
                    return null; 
                } else if (idade > 80) {
                    System.out.println("ERRO: Idade superior ao permitido (" + idade + " anos). Máximo 80 anos.");
                    return null; 
                }

                
                return String.format("%02d/%02d/%04d", dia, mes, ano);

            } catch (NumberFormatException e) {
                System.out.println("Ano inválido. Digite números.");
            } catch (Exception e) {
                System.out.println("Data inválida (ex: 30 de Fevereiro não existe). Tente novamente.");
                
                return null;
            }
        }
    }

    public static boolean isCPF(String cpf) {
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;
        
        try {
            int sm = 0, peso = 10;
            for (int i = 0; i < 9; i++) sm += (cpf.charAt(i) - 48) * peso--;
            int r = 11 - (sm % 11);
            char dig10 = (r == 10 || r == 11) ? '0' : (char) (r + 48);

            sm = 0; peso = 11;
            for (int i = 0; i < 10; i++) sm += (cpf.charAt(i) - 48) * peso--;
            r = 11 - (sm % 11);
            char dig11 = (r == 10 || r == 11) ? '0' : (char) (r + 48);

            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
        } catch (Exception e) { return false; }
    }
}