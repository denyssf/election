public class Candidato extends Pessoa {
    int numeroPolitico;
    Partido partido;
    int votos;
    String dataNascimento; 

    public Candidato(String nome, String cpf, String email, String codigoAcesso, int numeroPolitico, Partido partido, String dataNascimento) {
        super(nome, cpf, email, codigoAcesso);
        this.numeroPolitico = numeroPolitico;
        this.partido = partido;
        this.votos = 0;
        this.dataNascimento = dataNascimento;
    }
}