public abstract class Pessoa {
    String nome;
    String cpf;
    String email;
    String codigoAcesso;
    boolean jaVotou;

    public Pessoa(String nome, String cpf, String email, String codigoAcesso) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.codigoAcesso = codigoAcesso;
        this.jaVotou = false;
    }
}