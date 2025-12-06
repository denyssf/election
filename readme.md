#Sistema de Votação Eletrônica em Java 🗳️#
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Status](https://img.shields.io/badge/Status-Concluído-brightgreen?style=for-the-badge)
![Licença](https://img.shields.io/badge/Licença-MIT-blue?style=for-the-badge)
**Aplicação baseada em console (CLI) desenvolvida em Java puro, simulando todo o ciclo de uma eleição, desde o cadastro de partidos e candidatos até a apuração dos votos e emissão de relatórios.**

O projeto foca em Programação Orientada a Objetos, separação de responsabilidades e implementação rigorosa de regras de negócio.

##📋 Funcionalidades##
##👤 Painel do Organizador (Admin)##
**Gestão de Partidos: Criação de partidos com ou sem limite de candidatos.**

**Gestão de Candidatos: Visualização de lista completa e remoção de candidatos.**

**Configuração da Eleição: Definição de data e hora de início.**

**Segurança: Acesso protegido por login e senha (Padrão: admin / 1234).**

**Apuração: Encerramento da eleição, contagem de votos, cálculo de porcentagem e declaração do vencedor.**

**Relatórios: Envio simulado de e-mail com o resultado final para todos os participantes.**

##📢 Painel do Candidato##
**Cadastro Completo: Nome, CPF, E-mail, Partido e Número de escolha.**

**Validações: Verificação de idade (18 a 80 anos) e CPF válido.**

**Autenticação: Recebimento de código de acesso único via e-mail simulado.**

**Votação: Permissão para votar em si mesmo ou em outros.**

##🗳️ Painel do Eleitor##
**Cadastro: Dados pessoais com validação granular de data de nascimento (Dia/Mês/Ano).**

**Segurança: Validação de CPF único (impede duplicidade).**

**Votação: Processo de voto secreto utilizando CPF + Código de Acesso recebido por e-mail.**

##🛡️ Regras de Negócio e Validações##
**O sistema implementa diversas travas de segurança e integridade:**

**Validação de Idade: O sistema solicita dia, mês e ano separadamente e bloqueia o cadastro se a idade for < 18 ou > 80 anos.**

**Unicidade do Voto: Um CPF só pode votar uma única vez. Tentativas subsequentes são bloqueadas e logadas.**

**Validação de CPF: Algoritmo de verificação de dígitos verificadores reais.**

**Limite de Partido: O sistema respeita o limite máximo de candidatos definido pelo organizador para cada partido.**

**Sigilo: O organizador pode ver o total de eleitores, mas não tem acesso aos dados sensíveis ou em quem cada um votou.**

**Integridade Temporal: Não é possível deletar candidatos ou alterar dados sensíveis enquanto a eleição estiver ativa ou finalizada.**

##🚀 Como Executar##
**Pré-requisitos**
Java JDK 8 ou superior instalado.

**Passo a Passo**
-**Clone o repositório (ou baixe os arquivos):**

```Bash
git clone https://github.com/seu-usuario/sistema-votacao-java.git
cd sistema-votacao-java
```
-**Compile os arquivos: Certifique-se de estar na pasta onde estão os arquivos .java.**

```Bash
javac *.java
```
-**Execute o sistema:**

```Bash
java SistemaEleitoral
```

##📂 Estrutura do Projeto##
O código foi refatorado para seguir o princípio de Responsabilidade Única (SRP):

SistemaEleitoral.java: Classe principal (Main) e gerenciamento de menus.

Pessoa.java: Classe abstrata base para Eleitores e Candidatos.

Candidato.java & Eleitor.java: Entidades do domínio.

Partido.java: Estrutura de dados dos partidos.

Configuracao.java: Armazena estado da eleição e credenciais de admin.

Validador.java: Lógica de validação de CPF e tratamento de Datas.

Utils.java: Utilitários (simulação de envio de e-mail).

##🛠️ Tecnologias Utilizadas##
Java (Core): Lógica principal.

Java Time API (LocalDate, Period): Manipulação precisa de datas e idades.

Java Util (Scanner, List, Stream): Manipulação de coleções e entrada de dados.

##📝 Autor##
Desenvolvido por Denys.

Este projeto foi desenvolvido para fins de estudo sobre Lógica de Programação e Orientação a Objetos.
