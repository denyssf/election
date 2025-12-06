import java.time.LocalDate;

public class Configuracao {
    String adminUser = "admin";
    String adminPass = "1234";
    LocalDate dataEleicao;
    boolean eleicaoAtiva = false;
    boolean eleicaoFinalizada = false;

    boolean isDiaDaEleicao() {
        return dataEleicao != null && LocalDate.now().isEqual(dataEleicao);
    }
}