public class Eleitor extends Pessoa {
    String dataNascimento;

    public Eleitor(String nome, String cpf, String email, String codigoAcesso, String dataNascimento) {
        super(nome, cpf, email, codigoAcesso);
        this.dataNascimento = dataNascimento;
    }
}