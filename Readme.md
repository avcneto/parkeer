![](https://i.imgur.com/TEiYgBt.png)  
![Licença](https://img.shields.io/badge/license-MIT-green)
![Badge em Desenvolvimento](https://img.shields.io/badge/release%20date-november/08-yellow)
![Gradle Version](https://img.shields.io/badge/gradle-x.x.x-blue)
![Java Version](https://img.shields.io/badge/java-17-blue)

# <h1 align="center">Parkeer</h1>

Apresentamos o **Parkeer**, uma poderosa ferramenta que revolucionará a forma como você gerencia os parquímetros em 
grandes centros urbanos. Com nosso sistema em sua mão, você poderá gerenciar o tempo de estacionamento dos veículos,
calcular os valores devido ao uso dos parquímetros e ainda utilizar tais informações para fins de fiscalização.
Nossa solução permite realizar todas estas funções sem atrasos e transtornos, garantindo a eficiência na execução e
segurança na manutenção dos dados.

## 📄 Índice

* [Descrição do Projeto](#descrição-do-projeto)
* [Arquitetos Responsáveis](#arquitetos-responsáveis)
* [Funcionalidades](#funcionalidades)
* [Acesso ao Projeto](#acesso-ao-projeto)
* [Execução do Projeto](#execução-do-projeto)
* [Tecnologias utilizadas](#tecnologias-utilizadas)
* [Acesso ao Banco de Dados](#acesso-ao-banco-de-dados)
* [Relatório Técnico](#relatório-técnico)
* [Desafios](#desafios)
* [Documentação Técnica](#documentação-técnica)

## Descrição do Projeto

O projeto consiste na construção de uma solução do parquímetro, que será utilizado em uma cidade movimentada, onde
milhares de motoristas utilizam o serviço diariamente. Esta construção deve ser feita empregando conceitos de APIs,
persistência de dados e tudo mais aprendido até agora. Desta forma, deverão ser utilizadas APIs para melhorar a
eficiência do sistema, utilização de banco de dados para armazenar as informações sobre veículos estacionados com
eficiência tanto na leitura quanto na gravação dos dados e, além disto, garantir a escalabilidade do sistema.

Em resumo, o projeto visa desenvolver um sistema completo para gerenciar o tempo de estacionamento dos veículos,
calcular os valores devido ao uso dos parquímetros e armazenar essas informações para fins de fiscalização,
proporcionando aos usuários informações precisas, seguras e de rápida responsividade quando utilizado.

## Arquitetos Responsáveis

| [<img src="https://avatars.githubusercontent.com/u/42851702?v=4" width=115><br><sub>Lucas Mendes</sub>](https://github.com/Luzeraaa) | [<img src="https://avatars.githubusercontent.com/u/56560361?v=4" width=115><br><sub>Aderson Neto</sub>](https://github.com/avcneto) | [<img src="https://avatars.githubusercontent.com/u/19624216?v=4" width=115><br><sub>Felipe Chimin</sub>](https://github.com/flpchimin) | [<img src="https://avatars.githubusercontent.com/u/52970727?v=4" width=115><br><sub>Gustavo Makimori</sub>](https://github.com/gyfmaki) | [<img src="https://avatars.githubusercontent.com/u/88151987?v=4" width=115><br><sub>Pedro Paratelli</sub>](https://github.com/PedroParatelli) |
|:------------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------:|

## Funcionalidades

Cadastro e consulta de usuários  
Cadastro e consulta de veículos  
Cadastro e consulta de métodos de pagamentos

Validação de usuários por e-mail e CPF.

## Acesso ao projeto

Você pode [acessar o código fonte do projeto inicial](https://github.com/avcneto/postech-parkeer/)
ou [baixá-lo](https://github.com/avcneto/postech-parkeer/archive/refs/heads/main.zip).

## Execução do Projeto

Após baixar o projeto, você pode abrir com a IDE de preferência e configurar as variáveis de ambiente para acessar o
banco de dados.

1. Fazer o [download](https://github.com/avcneto/postech-parkeer/archive/refs/heads/main.zip);
2. Instalar Docker Desktop (Caso esteja em ambiente Windowns instalar WSL);
2. Abrir com IDE de preferência;
3. Executar via terminal:
    *  `docker-compose up`
    * Certifique-se de executar dentro do diretório do projeto "postech-parkeer" onde esta localizado o arquivo docker compose.
4. Configurar as varíaveis de ambiente para acessar o banco de dados:
    * _DATASOURCE_PASSWORD=fiap
    * DATASOURCE_USERNAME=fiap
    * MYSQL_DATABASE=parkeer
    * MYSQL_PASSWORD=fiap
    * MYSQL_ROOT_PASSWORD=root
    * MYSQL_USER=fiap
    * REDIS_HOST=localhost
    * REDIS_HOST_NAME=redis
    * REDIS_PASSWORD=fiap
    * REDIS_PORT=6379
    * SECURITY_PASSWORD=fiap
5. Executar o projeto.
6. Os possíveis métodos podem ser executados:
   * Cadastro do usuário; 
   * Cadastro da forma de pagamento;
   * Cadastro do Veículo;
   * Estacionar o Veículo (início da contagem do tempo)
   * Retirar o Veículo da Vaga (manual)
   * Retirar o Veículo da Vaga (time out)


## Acesso ao Banco de Dados

A persistência de dados será realizado através do banco de dados MySQL e REDIS. Ambos os banco irão rodar em container via Dokcer.
Maiores detalhes de versão da imagem e configurações de portas verificar arquivo:

* [docker-compose.yml](https://github.com/avcneto/postech-parkeer/blob/main/docker-compose.yml)

## Tecnologias utilizadas

- Java 17 (Versão atualizada e estável da linguagem Java)
- Gradle (Ferramenta amplamente adotada para gerenciamento de dependências)
- Kotlin (Linguagem de programação)
- Spring: Webflux, Security, MVC, Data JPA, Web (Frameworks populares para desenvolvimento de aplicativos Java)
- MySQL (Sistema de gerenciamento de banco de dados que utiliza a linguagem SQL como interface)
- Redis (Armazenamento de estrutura de dados em memória, usado como um banco de dados NoSQL em memória distribuído de chave-valor, cache e agente de mensagens)
- Hibernate (Framework de mapeamento objeto-relacional para acesso a dados)
- JPA (Java Persistence API) (Especificação padrão para persistência de dados em Java)
- Lombok (Biblioteca para reduzir a verbosidade do código e automatizar tarefas comuns)
- Jakarta Bean Validation (Especificação para validação de dados em Java)
- Docker


<div style="display: inline_block"><br>
<img src=https://raw.githubusercontent.com/github/explore/5b3600551e122a3277c2c5368af2ad5725ffa9a1/topics/java/java.png width="65" height="60"
/>
<img src=https://www.eclipse.org/community/eclipse_newsletter/2015/may/images/gradlephant.png width="60" height="55"
/>
<img src=https://repository-images.githubusercontent.com/389429650/7105a193-ad96-45cc-a3be-87cdfda75ebe width="60" height="55"
/>
<img src=https://th.bing.com/th/id/R.d8469eae9c8a4aa8ba0104a9d636d5f8?rik=WXdhpHKO0QTl6g&riu=http%3a%2f%2fhmkcode.github.io%2fimages%2fspring%2fspring.png&ehk=l%2b%2fhOIEAi407AyPHHjQT0NnUHU%2fH%2bjQzbnquLbAEdSI%3d&risl=&pid=ImgRaw&r=0 width="60" height="55" width="60" height="55"
/>
<img src=https://i.imgur.com/LjohcGj.png width="70" height="55" width="70" height="55"
/>
<img src=https://www.simplilearn.com/ice9/free_resources_article_thumb/MySQL-Logo.wine.png width="60" height="55" width="60" height="55"
/>
<img src=https://cdn.icon-icons.com/icons2/2415/PNG/512/redis_plain_logo_icon_146366.png width="60" height="55" width="60" height="55"
/>
<img src=https://www.mundodocker.com.br/wp-content/uploads/2015/06/docker_facebook_share.png width="60" height="55" width="60" height="55"
/>
<img src=https://oopy.lazyrockets.com/api/v2/notion/image?src=https:%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F3ed7a304-a24b-4c45-831f-1755950e4260%2Flombok.png&blockId=552b6017-489d-4bcd-bb44-803f5e94bac9&width=256   width="60" height="55"
/>
</div>

## Relatório Técnico

A arquitetura utilizada neste projeto baseia-se na combinação de conceitos MVC (Model-View-Controller) e DDD (Domain
Driven Design).
Essa combinação permite obter os benefícios de ambos os conceitos, utilizando a arquitetura MVC para a divisão das
responsabilidades de apresentação e controle de fluxo, e o DDD para criar um modelo de domínio encapsulado e rico.

A versão 17 do Java foi escolhida como base para o projeto devido à sua estabilidade e atualização no momento do
desenvolvimento. Para facilitar a configuração e o gerenciamento de dependências, o projeto adotou o Gradle, que possui
uma estrutura simples e ampla biblioteca de plugins. Além disso, o Gradle possui uma vasta integração com repositórios
centrais e uma
documentação extensa, tornando-o uma escolha popular e confiável para a construção e gerenciamento de projetos Java.

Para de reduzir a verbosidade e os famosos códigos boilerplates do código, além de automatizar a geração de getters,
setters, construtores e outros métodos comuns, o projeto utilizou o Lombok, uma biblioteca para Java. O Lombok também
fornece a anotação Slf4j para logar erros internos da aplicação, mantendo-os ocultos do usuário final.

O Hibernate é amplamente utilizado no desenvolvimento Java devido às suas vantagens significativas. Ele simplifica o
acesso a dados, abstraindo o mapeamento objeto-relacional e automatizando tarefas comuns, aumentando a produtividade dos
desenvolvedores. Além disso, oferece portabilidade, permitindo executar aplicativos em diferentes bancos de dados, e
suporta consultas flexíveis, cache e gerenciamento de transações, proporcionando um ambiente eficiente e robusto para o
desenvolvimento de aplicativos que interagem com bancos de dados relacionais.

Para validar e garantir a integridade dos dados no aplicativo Java, foi utilizado o Jakarta Bean Validation (
anteriormente conhecida como Bean Validation 2.0).
Essa abordagem eficiente permite verificar se os dados inseridos atendem a padrões específicos, como formato de e-mail,
CPF, entre outros. O uso do @Validator com expressões regulares ajuda a manter a consistência dos
dados e reduzir erros ou entradas inválidas, oferecendo uma forma poderosa e flexível de validação de dados no projeto.

Para garantir a persistência de dados, foi implementada uma instância do MySQL (SQL) e REDIS (NoSQL) em um contêiner Docker,
proporcionando isolamento eficiente de responsabilidades, portabilidade, escalabilidade, facilidade de backup e
segurança, otimizando o desenvolvimento e a manutenção da aplicação. A arquitetura proposta com os dois banco de dados tem o intuito
de suportar grandes volumes de requisições em um pequeno espaço de tempo. O banco REDIS (em memória) recebe as requsições da aplicação
acumulando-as por um período de tempo (ganho de velocidade na comunicação). Neste delta de tempo ocorre a migração dos dados para MySQL 
e a limpeza automática dos mesmos no REDIS. Essa estratégia visa ganho de performace já que o banco REDIS utiliza chave e valor para 
comunicação e a persistência final no MySQL. Além disso as tabelas SQL tem colunas estratégicas indexadas para o ganho de performance 
em consultas.

Uma arquitetura básica na AWS para a aplicação desenvolvida pode ser desenhada da seguinte forma:
Um contêiner Docker que persiste dados no Redis e replica para o MySQL inclui 
instâncias Amazon EC2 para a aplicação, Amazon ElastiCache para o Redis (opcional), Amazon RDS para o MySQL, um Application Load Balancer (ALB) para 
distribuição de tráfego, Amazon VPC para isolamento e segurança, CloudWatch para monitoramento. 
Isso proporcionaria escalabilidade e alta disponibilidade.


## Desafios

- Definir e compreender os relacionamentos entre usuários, veículos, métodos de pagamento e vagas.
- Incluir as regras de validações bem como seus regexs.
- Tratamento de exceções para possíveis erros durante o consumo das APIs.
- Definição da arquitetura do projeto (DDD/MVC/tecnologias e outros).
- Determinação das responsabilidades dos membros da equipe.
- Subir o bando de dados em container Docker.
- Integração entre as bases SQL e NoSQL.
- Utilização do WebFlux.

## Documentação Técnica

***

### Disclaimer

Postman Collection: [Collection](src/main/resources/doc/parkeer.postman_collection.json)

